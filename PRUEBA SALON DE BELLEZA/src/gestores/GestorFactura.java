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
import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.function.Predicate;

public class GestorFactura {

    private GestorGenerico<Factura> caja; // asi no me confundo
    Gson gson;
    private final String archivoFacturas = "facturas.json";
    private GestorTurno gestorTurno; // Para buscar turnos
    Scanner scan = new Scanner(System.in);
    private List<IBuscarPorCodigo<? extends Servicio>> gestores;
    private GestorCliente gestorCliente;
    private GestorProfesional gestorProfesional;


    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public GestorFactura(GestorTurno gestorTurno, List<IBuscarPorCodigo<? extends Servicio>> gestores, GestorCliente gestorCliente, GestorProfesional gestorProfesional) {
        this.caja = new GestorGenerico<>();
        this.gson = new Gson();
        this.gestorTurno = gestorTurno;
        this.gestores=gestores;
        this.gestorCliente = gestorCliente;
        this.gestorProfesional = gestorProfesional;
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
            scan.nextLine();
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
            try {
                System.out.println("Seleccione el número del turno para agregar a la factura:");
                for (int i = 0; i < turnosCliente.size(); i++) {
                    System.out.println((i + 1) + ". " + turnosCliente.get(i).toString(gestorCliente, gestorProfesional));
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
            } catch (InputMismatchException a) {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
                scan.nextLine();
            }

        }
    }


    private Cliente obtenerCliente() {
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
        int opcion=-1;
        do {
            try {
                System.out.println("\n");
                System.out.println("---------------------------------");
                System.out.println("Ingrese una opcion...");
                System.out.println("1. Tipo de pago -");
                System.out.println("2. Modificar el cliente -");
                System.out.println("3. Turnos declarados en la factura-");
                System.out.println("4. Aplicar descuento-");
                System.out.println("0. Salir");
                System.out.println("---------------------------------");
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

            } catch (InputMismatchException a) {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
                scan.nextLine();
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

    public void verificarCarga(Factura factura) {
        int opcion = -1;
        do {
            try {
                System.out.println("¿Deseas modificar algo de la factura?");
                System.out.println("1. Sí");
                System.out.println("2. No");

                opcion = scan.nextInt();
                scan.nextLine();

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
            } catch (InputMismatchException a) {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
                scan.nextLine();
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
        } catch (InputMismatchException a) {
            System.out.println("Caracter invalido..Ingrese un numero por favor!");
            scan.nextLine();
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
            try {
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
            } catch (InputMismatchException a) {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
                scan.nextLine();
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

    public double gananciaXmes(int mes, int año) {

        double total = 0;
        for (Factura f : caja.getLista()) {
            LocalDate fecha = ConvertirFechaHoras.convertirStringAFecha(f.getFecha());

            if (fecha.getMonthValue() == mes && fecha.getYear() == año) {
                total += f.getPrecioFinal();
            }
        }
        return total;
    }

    public double gananciaXaño(int año) {

        double total = 0;
        for (Factura f : caja.getLista()) {
            LocalDate fecha = ConvertirFechaHoras.convertirStringAFecha(f.getFecha());

            if (fecha.getYear() == año) {
                total += f.getPrecioFinal();
            }
        }
        return total;
    }
    ////////////////////////////////////////////////////////MANEJO DE ARCHIVOS ////////////////////////////////////////////////////

    public void escribirFacturasEnEnJson() {
        try (FileWriter writer = new FileWriter(this.archivoFacturas)) {
            gson.toJson(caja.getLista(), writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("No se puede guardar el archivo de facturas: " + e.getMessage());
        }
    }

    public void leerArchivoFacturas() {

            try (FileReader fileReader = new FileReader(archivoFacturas)) {
                // Deserializar el archivo JSON a una lista de facturas
                List<Factura> listaFacturas = gson.fromJson(fileReader, new TypeToken<List<Factura>>() {
                }.getType());

                if (listaFacturas != null) {
                    // Limpia la lista existente en caja y agrega las facturas deserializadas
                    this.caja.vaciarAlmacen();
                    for (Factura factura : listaFacturas) {
                        ///factura.setGestores(this.gestores); // Inicializa gestores después de deserializar
                        this.caja.agregar(factura);
                    }
                }

            } catch (FileNotFoundException e) {
                System.out.println("No se encontró el archivo. Se iniciará con un historial vacío.");
                this.caja = new GestorGenerico<>();
            } catch (IOException e) {
                System.out.println("No se puede leer el archivo de facturas: " + e.getMessage());
            }

    }


    public String verFacturas() {
        StringBuilder sb = new StringBuilder();
        sb.append("==== Lista de Facturas ====\n");

        if (caja == null || caja.getLista().isEmpty()) {
            sb.append("No hay facturas registradas.\n");
        } else {
            int contador = 1;
            for (Factura factura : caja.getLista()) { // Asegúrate de que obtenerTodos devuelva una lista de facturas.
                sb.append("Factura ").append(contador++).append(":\n");
                sb.append(factura.toString()).append("\n");
                sb.append("--------------------------\n");
            }
        }

        return sb.toString();
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

}