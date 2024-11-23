package gestores;
import Interfaces.IBuscarPorCodigo;
import abstractas.Servicio;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoDePago;
import excepciones.*;
import model.Cliente;
import model.ConvertirFechaHoras;
import model.Factura;
import model.Turno;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.RecursiveTask;
import java.util.function.Predicate;

public class GestorFactura {

    private GestorGenerico<Factura> caja ; // asi no me confundo
    Gson gson ;
    private final String archivoFacturas = "facturas.json";
    private GestorTurno gestorTurno; // Para buscar turnos
    Scanner scan = new Scanner(System.in);


    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public GestorFactura(GestorTurno gestorTurno, List<IBuscarPorCodigo<? extends Servicio>> gestores) {
        this.caja = new GestorGenerico<>();
        this.gson = new Gson();
        this.gestorTurno = gestorTurno;
    }

    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////

    public void crearFactura() {

        try {

            Cliente cliente = obtenerCliente();
            if (cliente == null) return;

            TipoDePago tipoPago = pedirTipoPago();

            Factura factura = new Factura(tipoPago, cliente);

            List<Turno> turnosCliente = gestorTurno.buscarTurnosXdniClienteVigentes(cliente.getDni());
            if (turnosCliente.isEmpty()) {
                System.out.println("No hay turnos reservados para este cliente.");
                return;
            }

            agregarTurnosAFactura(factura, turnosCliente);

            System.out.println("¿Aplicar descuento? (SI/NO):");
            if (scan.nextLine().equalsIgnoreCase("SI")) {
                aplicarDescuento(factura);
            }

            if (agregarFactura(factura)) {
                System.out.println("Factura creada correctamente:");
                System.out.println(factura);
            }

        } catch (DNInoEncontradoException | TurnoExistenteException | FacturaYaExistenteException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void agregarTurnosAFactura(Factura factura, List<Turno> turnosCliente) {
        boolean continuar = true;

        while (continuar) {
            System.out.println("Seleccione el número del turno para agregar a la factura:");
            for (int i = 0; i < turnosCliente.size(); i++) {
                System.out.println((i + 1) + ". " + turnosCliente.get(i));
            }

            int seleccion = scan.nextInt();
            if (seleccion < 1 || seleccion > turnosCliente.size()) {
                System.out.println("Selección inválida. Intente nuevamente.");
                continue;
            }

            Turno turnoSeleccionado = turnosCliente.get(seleccion - 1);
            if (!factura.getTurnosPorCliente().contains(turnoSeleccionado)) {
                factura.agregarTurno(turnoSeleccionado);
                System.out.println("Turno agregado correctamente.");
            } else {
                System.out.println("El turno ya está en la factura.");
            }

            System.out.println("¿Desea agregar otro turno? (SI/NO):");
            continuar = scan.next().equalsIgnoreCase("SI");
        }
    }


    private Cliente obtenerCliente() {
        GestorCliente gestorCliente = new GestorCliente(); // Suponiendo que este gestor ya está configurado
        Cliente cliente = null;

        while (cliente == null) {
            try {
                System.out.println("Ingrese el DNI del cliente:");
                String dni = scan.nextLine();
                cliente = gestorCliente.buscarPersona(dni);
            } catch (DNInoEncontradoException e) {
                System.out.println(e.getMessage());
                System.out.println("¿Desea intentar nuevamente? (SI/NO):");
                if (!scan.nextLine().equalsIgnoreCase("SI")) {
                    return null;
                }
            }
        }
        return cliente;
    }

    private boolean agregarFactura(Factura factura) throws FacturaYaExistenteException {
        for (Factura f : caja.getLista()) {
            if (f.getCodigoFactura().equals(factura.getCodigoFactura())) {
                throw new FacturaYaExistenteException("La factura ya existe en el sistema.");
            }
        }
        caja.agregar(factura);
        return true;
    }



    public void eliminarFactura(String codigoFactura) throws FacturaNoExistenteException {
        Factura facturaAEliminar = null;

        // Buscar la factura por código
        for (Factura f : caja.getLista()) {
            if (f.getCodigoFactura().equals(codigoFactura)) {
                facturaAEliminar = f;
                break;
            }
        }

        if (facturaAEliminar == null) {
            throw new FacturaNoExistenteException("La factura que desea eliminar no existe.");
        }

        System.out.println("¿Está seguro que desea eliminar la factura del cliente "
                + facturaAEliminar.getCliente().getNombre() + "? (SI/NO)");
        String rta = scan.nextLine();

        if (rta.equalsIgnoreCase("si")) {
            caja.eliminar(facturaAEliminar);
            System.out.println("Factura eliminada exitosamente.");

        } else {
            System.out.println("La factura no fue eliminada.");
        }



    }

    public Factura buscarFacturaPorCodigo(String codigo) throws CodigoNoEncontradoException {

        Factura factura = null;
        for (Factura factu : caja.getLista()) {

            if (factu.getCodigoFactura().equals(codigo)) {
                factura = factu;
            }
        }

        if (factura == null) {
            throw new CodigoNoEncontradoException("El codigo ingresado no existe!");
        }
        return factura;
    }

    ///aca se entiende que anterior a este metodo se muestran las facturas y de ahi se saca el codigo, todas las del dni de la persona que queremos
    public void modificarFactura() {
        Factura facturaModificada = null;

        // Bucle para validar el código de la factura
        while (facturaModificada == null) {
            System.out.println("Ingrese el código de factura:");
            String codigo = scan.nextLine();

            try {
                facturaModificada = buscarFacturaPorCodigo(codigo);
            } catch (CodigoNoEncontradoException e) {
                System.out.println(e.getMessage());
                System.out.println("¿Desea intentarlo de nuevo? (Si/No):");
                if (!scan.nextLine().equalsIgnoreCase("Si")) {
                    return;
                }
            }
        }

        ///Elegir que se modifica
        int opcion;
        do {
            //no te olvides e actualizar la fecha!!
            System.out.println("Ingrese una opcion...");
            System.out.println("1. Tipo de pago -");
            System.out.println("2. Modificar el cliente -");
            System.out.println("3. Turnos declarados en la factura-");
            System.out.println("4. Aplicar descuento-"); //Esto puede ser en algun descuento especial o flasheo yo
            System.out.println("0. Salir");
            System.out.print("Ingrese una opción: ");
            opcion = scan.nextInt();
            scan.nextLine();

            switch (opcion) {
                case 1:
                    TipoDePago tipo = pedirTipoPago();
                    facturaModificada.setTipoPago(tipo);
                    System.out.println("Tipo de pago actualizado.");

                    modificarFechaFactura(facturaModificada);
                    break;
                case 2:
                    modificarCliente(facturaModificada);
                    //actualizar la fecha a la actual
                    modificarFechaFactura(facturaModificada);

                    break;
                case 3:

                    gestionarTurnos(facturaModificada, gestorTurno);
                    modificarFechaFactura(facturaModificada);

                    break;
                case 4:
                    aplicarDescuento(facturaModificada);
                    modificarFechaFactura(facturaModificada);

                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }


    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////
    private void aplicarDescuento(Factura factura) {

        try {
            System.out.println("Precio final actual " + factura.getPrecioFinal());
            // metodo de descuento en gestor precios
            System.out.println("Ingrese el porcentaje de descuento");
            double desc = scan.nextDouble();
            scan.nextLine();
            GestorPrecios.aplicarDescuento(factura.getCodigoFactura(), desc, caja.getLista());

        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    public void verificarCarga(Factura factura, Scanner scanner) {
        int opcion;
        do {
            System.out.println("¿Deseas modificar algo de la factura?");
            System.out.println("1. Sí");
            System.out.println("2. No");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    modificarFactura();
                    break;
                case 2:
                    System.out.println("....");
                    break;
                default:
                    System.out.println("Opción no válida, selecciona nuevamente.");
                    break;
            }
        } while (opcion != 2 && opcion != 1);
    }

    private void gestionarTurnos(Factura factura, GestorTurno turnos) {
        try {
            System.out.println("Turnos actuales:");
            for (int i = 0; i < factura.getTurnosPorCliente().size(); i++) {
                System.out.println((i + 1) + ". " + factura.getTurnosPorCliente().get(i));
            }
            System.out.println("Seleccione un turno para eliminar o ingrese 0 para agregar uno nuevo:");
            int opcion = scan.nextInt();
            scan.nextLine();

            if (opcion > 0 && opcion <= factura.getTurnosPorCliente().size()) {
                Turno turno = factura.getTurnosPorCliente().get(opcion - 1);
                factura.eliminarTurno(turno);
                System.out.println("Turno eliminado.");
            } else if (opcion == 0) {
                System.out.println("Ingrese el código del turno:");
                String codTurno = scan.nextLine();
                Turno nuevoTurno = turnos.buscarTurnoXcodigo(codTurno);
                factura.agregarTurno(nuevoTurno);
                System.out.println("Turno agregado.");
            } else {
                System.out.println("Opción no válida.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void modificarCliente(Factura factura) {
        GestorCliente clientes = new GestorCliente();

        try {
            System.out.println("Ingrese el DNI del cliente:");
            String dni = scan.nextLine();
            Cliente cliente = clientes.buscarPersona(dni);
            factura.setCliente(cliente);
            System.out.println("Cliente actualizado.");
        } catch (DNInoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private TipoDePago pedirTipoPago() {
        TipoDePago tipo = null;


        while (tipo == null) {
            System.out.println("Seleccione el tipo de pago:");
            TipoDePago[] opciones = TipoDePago.values();
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }
            int opcion = scan.nextInt();
            scan.nextLine(); // Limpiar buffer
            if (opcion > 0 && opcion <= opciones.length) {
                tipo = opciones[opcion - 1];
            } else {
                System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
        return tipo;
    }

    public void modificarFechaFactura(Factura factura) {
      String nuevaFecha = ConvertirFechaHoras.convertirFechaAString(LocalDate.now());
        factura.setFecha(nuevaFecha);
    }



    public List<Factura> verHistorialPorFecha(String fecha) {

        if (fecha == null || fecha.isEmpty()) {
            System.out.println("La fecha proporcionada no registra facturas.");
            return new ArrayList<>();
        }

        LocalDate fecha1; // esto lo hago para usar metodos del local
        try {
            // Convertir el string de fecha a LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Ajusta el patrón según el formato de la fecha
            fecha1 = LocalDate.parse(fecha, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("El formato de fecha proporcionado no es válido.");
            return new ArrayList<>();
        }

        if (fecha1.isAfter(LocalDate.now())) {
            System.out.println("La fecha proporcionada no puede ser en el futuro.");
            return new ArrayList<>();
        }

        Predicate<Factura> condicion = factura -> (factura.getFecha().equals(fecha));
        return caja.filtrarPorCondicion(condicion);
    }



    public void historialFacturasPorCliente(String dni) throws DNInoEncontradoException {
        // Validar si el DNI es null o está vacío
        if (dni == null || dni.isEmpty()) {
            throw new IllegalArgumentException("El DNI proporcionado es inválido.");
        }

        List<Factura> facturasEncontradas = new ArrayList<>();

        // Recorremos todas las facturas en el historial
        for (Factura factu : caja.getLista()) {
            // Si el DNI del cliente de la factura coincide con el DNI del parametro
            if (factu.getCliente().getDni().equals(dni)) {
                facturasEncontradas.add(factu);  // se agrega a la lisya
            }
        }
        if (facturasEncontradas.isEmpty()) {
            throw new DNInoEncontradoException("El DNI ingresado no pertenece a ninguno de nuestros clientes, intente de nuevo.");
        }

        //para mostrar las facturas ordanadas por fecha
        facturasEncontradas.sort(new Comparator<Factura>() {
            @Override
            public int compare(Factura f1, Factura f2) {
                return f1.getFecha().compareTo(f2.getFecha());
            }
        });

        // info del cliente
        GestorCliente clientes = new GestorCliente();
        Cliente cliente = clientes.buscarPersona(dni);

        // mostrar las facturas
        System.out.println("Historial de facturas para el cliente con DNI " + dni +
                " Nombre y apellido: " + cliente.getNombre() + " " + cliente.getApellido());
        for (Factura factura : facturasEncontradas) {
            System.out.println("----------------------------------");
            System.out.println(factura);
            System.out.println("----------------------------------");

        }
    }
    public double gananciaXdia(String fecha) {
        double total = 0;
        for (Factura f : caja.getLista()) {
            if (f.getFecha().equals(fecha)) {
                total += f.getPrecioFinal();
            }
        }
        return total;
    }
    /*
        public double gananciaXmes(int mes, int año) {

            double total = 0;
            for (Factura f : historial.getAlmacen()) {
                if (f.getFecha().getMonthValue() == mes && f.getFecha().getYear() == año) {
                    total += f.getPrecioFinal();
                }
            }
            return total;
        }

        public double gananciaXaño(int año) {

            double total = 0;
            for (Factura f : historial.getAlmacen()) {
                if (f.getFecha().getYear() == año) {
                    total += f.getPrecioFinal();
                }
            }
            return total;
        }
    */
    ////////////////////////////////////////////////////////MANEJO DE ARCHIVOS ////////////////////////////////////////////////////

    public  void escribirFacturasEnEnJson() {
        try (FileWriter writer = new FileWriter(this.archivoFacturas)) {
            gson.toJson(caja.getLista(), writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("No se puede guardar el archivo de facturas: " + e.getMessage());
        }
    }

    public void leerArchivoFacturas() {
        try (FileReader fileReader = new FileReader(archivoFacturas)) {
            // Deserializar el archivo JSON a una lista de objetos Cliente
            this.caja = gson.fromJson(fileReader, new TypeToken<GestorGenerico<Factura>>() { }.getType());
            System.out.println("Archivo de clientes leído exitosamente.");

        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo. Se iniciará con un historial vacío.");
            this.caja = new GestorGenerico<>();
        } catch (IOException e) {
            System.out.println("No se puede leer el archivo de facturas: " + e.getMessage());
        }
    }


    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////


    public String getArchivoFacturas() {
        return archivoFacturas;
    }

    public GestorGenerico<Factura> getCaja() {
        return caja;
    }

    public void setCaja(GestorGenerico<Factura> caja) {
        this.caja = caja;
    }



    /*  public void crearFactura() {
        GestorTurno turnos = new GestorTurno();
        GestorCliente clientes = new GestorCliente();
        Scanner scan = new Scanner(System.in);

        try {
            // Pedir cliente por DNI
            Cliente cliente = null;
            while (cliente == null) {
                try {
                    System.out.println("Ingrese el DNI del cliente:");
                    String dni = scan.nextLine();
                    cliente = clientes.buscarPersona(dni);

                } catch (DNInoEncontradoException e) {
                    System.out.println(e.getMessage());
                    System.out.println("¿Desea intentar nuevamente? (SI/NO):");
                    if (!scan.nextLine().equalsIgnoreCase("SI")) {// ese equals ignora si es en mayus o minus
                        return; //salir si el usuario ingresa NO
                    }
                }
            }

            //pedir tipo de pago
            TipoDePago tipo = pedirTipoPago();
            Factura factura = new Factura(tipo, cliente, gestor);

            //buscamos los turnos de hoy y futuros de ese cliente
            List<Turno> turnosCliente = turnos.buscarTurnosXdniClienteVigentes(cliente.getDni());

            if (turnosCliente.isEmpty()) {
                System.out.println("No hay turnos reservados para este cliente.");
                return;
            }

            //mostrarle los turnos del dia de la fecha y los proximos
            System.out.println("Estos son los turnos recientes del cliente: ");
            for (int i = 0; i < turnosCliente.size(); i++) {
                System.out.println((i + 1) + ". " + turnosCliente.get(i));
            }

            boolean continuar = true;
            while (continuar) {

                System.out.println(" ");// dejo un espacio
                System.out.print("Ingrese el número del turno a pagar: ");
                int nroTurno = scan.nextInt();

                if (nroTurno < 1 || nroTurno > turnosCliente.size()) { //corroboramos que el numero esye bien
                    System.out.println("Número de turno inválido. Intente nuevamente.");
                    continue;
                }

                Turno turnoSeleccionado = turnosCliente.get(nroTurno - 1);

                if (!factura.getTurnosPorCliente().contains(turnoSeleccionado)) {
                    factura.agregarTurno(turnoSeleccionado);
                    System.out.println("El turno se agregó correctamente a la factura.");
                } else {
                    System.out.println("El turno ya está en la factura.");
                }


                System.out.print("¿Desea agregar otro turno? (SI/NO): ");
                String rta = scan.next().toLowerCase();
                continuar = rta.equals("si");
            }

            System.out.println("Aplicar descuento?(SI/NO)");
            String rta2 = scan.next().toLowerCase();

            if (rta2.equalsIgnoreCase("si")) {
                aplicarDescuento(factura, scan);
            } else {
                return;
            }

            // montrar factura y agregarla
            boolean seCargo = agregarFactura(factura);
            if (seCargo) {
                System.out.println("La factura fue ingresada correctamente, aqui tiene los detalles:");
                System.out.println(factura);
                verificarCarga(factura, scan);
            }

        } catch (TurnoExistenteException | FacturaYaExistenteException e) {
            System.out.println(e.getMessage());
        }
    }*/

}