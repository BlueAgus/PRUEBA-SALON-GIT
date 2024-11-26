package gestores;

import Interfaces.IBuscarPorCodigo;
import abstractas.Servicio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;
import excepciones.CodigoNoEncontradoException;
import model.ConvertirFechaHoras;
import model.Manicura;
import model.Pestanias;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GestorPestania implements IBuscarPorCodigo<Servicio> {

    private static final Scanner scanner = new Scanner(System.in);
    private List<Pestanias> almacenServicios;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static String archivoPestania = "pestania.json";

    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////


    public GestorPestania() {
        this.almacenServicios = new ArrayList<>();
    }

    public void agregarServicio() {
        TipoServicio tipoService = TipoServicio.PESTANIAS;
        double precio = pedirPrecio();
        TipoPestanias tipoPestanias = pedirTipoPestanias();
        String duracion = pedirDuracion();


        GestorPrecios.modificarPrecio(Pestanias.class, tipoPestanias, precio);// no mover

        //Cuando se instancie pestanias llamara a calcular precio y obtendra el precio ingresado arriba
        Pestanias pestanias = new Pestanias(duracion, tipoPestanias);

        boolean servicioDuplicado = almacenServicios.stream()
                .filter(Pestanias.class::isInstance) // Filtrar solo objetos de tipo Depilacion
                .map(Pestanias.class::cast)         // Convertir a Depilacion
                .anyMatch(s -> s.getTipoPestanias() == tipoPestanias && s.getTipoService() == tipoService);

        if (servicioDuplicado) {
            System.out.println("El servicio ya existe: " + pestanias.getClass().getSimpleName() +" con "+pestanias.getTipoPestanias());
            System.out.println("Intente de nuevo...");
            return;
        }
        almacenServicios.add(pestanias);
        System.out.println(pestanias);
        verificarCarga(pestanias);


    }

    public boolean eliminarServicio() {

        String cod_servicio = pedirCodServicio();

        for (Pestanias servicio : almacenServicios) {
            if (servicio.getCodigo_servicio().equals(cod_servicio)) {
                return almacenServicios.remove(servicio);
            }
        }
        return false;
    }

    public Pestanias buscarServicio() throws CodigoNoEncontradoException {

        mostrarServicios();

        while (true) {
            System.out.println("Ingrese el código('salir' si quiere cancelar la operacion): ");
            String cod_Servicio = scanner.nextLine();

            if (cod_Servicio.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null;
            }


            for (Pestanias s : almacenServicios) {
                if (s.getCodigo_servicio().equals(cod_Servicio)) {
                    return s;
                }
            }
            throw new CodigoNoEncontradoException("El código de servicio no existe: " + cod_Servicio);
        }

    }

    @Override
    public Pestanias buscarPorCodigo(String codServicio) throws CodigoNoEncontradoException {
        Pestanias servicio = null;
        for (Pestanias s : almacenServicios) {
            if (s.getCodigo_servicio().equals(codServicio)) {
                servicio = s;
            }
        }
        if (servicio == null) {
            throw new CodigoNoEncontradoException("El codigo ingresado no existe..");
        }
        return servicio;
    }

    // Función que permite modificar un servicio existente
    public void modificarServicio() {

        Pestanias servicio = null;
        try {
            servicio = buscarServicio();
        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
        if (servicio != null) {
            boolean continuarModificando = true;
            while (continuarModificando) {
                try {
                    System.out.println("\n");
                    System.out.println("---------------------------------");
                    System.out.println("¿Qué te gustaría modificar?");
                    System.out.println("1. Tipo de pestaña");
                    System.out.println("2. Precio");
                    System.out.println("3. Duración");
                    System.out.println("4. Salir");
                    System.out.println("---------------------------------");
                    System.out.println("Ingrese una opción: ");
                    int opcion = scanner.nextInt();
                    scanner.nextLine();

                    switch (opcion) {
                        case 1:
                            servicio.setTipoPestanias(pedirTipoPestanias());
                            break;
                        case 2:
                            servicio.setPrecio(pedirPrecio());
                            break;
                        case 3:
                            servicio.setDuracion(pedirDuracion());
                            break;
                        case 4:
                            continuarModificando = false;
                            break;
                        default:
                            System.out.println("Opción no válida.");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Ingrese un numero válido, por favor");
                    scanner.nextLine();
                }

                System.out.println("Servicio modificado:");
                System.out.println(servicio);
            }
        }

    }

    public void modificarServicioParametro(Pestanias servicio) {

        boolean continuarModificando = true;
        while (continuarModificando) {
            try{
                System.out.println("\n");
                System.out.println("---------------------------------");
                System.out.println("¿Qué te gustaría modificar?");
                System.out.println("1. Tipo de pestaña");
                System.out.println("2. Precio");
                System.out.println("3. Duración");
                System.out.println("0. Salir");
                System.out.println("---------------------------------");
                System.out.println("Ingrese una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        servicio.setTipoPestanias(pedirTipoPestanias());
                        break;
                    case 2:
                        servicio.setPrecio(pedirPrecio());
                        break;
                    case 3:
                        servicio.setDuracion(pedirDuracion());
                        break;
                    case 0:
                        continuarModificando = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }

                System.out.println("Servicio modificado:");
                System.out.println(servicio);
            }catch (InputMismatchException e) {
                System.out.println("Ingrese un numero válido, por favor");
                scanner.nextLine();
            }

        }
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

    public void verificarCarga(Pestanias servicio) {
        int opcion = -1;
        do {
            System.out.println("¿Deseas modificar algo del servicio?");
            System.out.println("1. Sí");
            System.out.println("2. No");
            try {

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        modificarServicioParametro(servicio);
                        break;
                    case 2:
                        System.out.println("....");
                        break;
                    default:
                        System.out.println("Opción no válida, selecciona nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor ingrese un numero como opción");
                scanner.nextLine();
            }
        } while (opcion != 2 && opcion != 1);
    }

    public String pedirCodServicio() {
        // Verifica si la lista de servicios está vacía
        if (almacenServicios.isEmpty()) {
            System.out.println("No hay servicios disponibles.");
            return null;
        }

        // Muestra los servicios disponibles
        for (int i = 0; i < almacenServicios.size(); i++) {
            System.out.println((i + 1) + ". \n" + almacenServicios.get(i).toString());
        }

        int opc = 0;

        while (true) {
            try {
                System.out.println("OPCIÓN: (o escriba 'salir' para cancelar) ");
                String opcElegida = scanner.nextLine();

                // Verifica si el usuario quiere cancelar
                if (opcElegida.equalsIgnoreCase("salir")) {
                    System.out.println("Operación cancelada por el usuario.");
                    return null;
                }

                // Convierte la opción ingresada a un número
                opc = Integer.parseInt(opcElegida);

                // Valida que la opción esté dentro del rango permitido
                if (opc < 1 || opc > almacenServicios.size()) {
                    System.out.println("Selección inválida. Inténtelo de nuevo.");
                } else {
                    // Retorna el código del servicio correspondiente
                    return almacenServicios.get(opc - 1).getCodigo_servicio();
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, vuelva a intentarlo.");
            }
        }
    }

    // Validación del precio
    private double pedirPrecio() {
        double precio = -1;
        while (precio <= 0) {
            try {
                System.out.print("Introduce el precio del servicio: ");
                precio = scanner.nextDouble();
                scanner.nextLine();

                if (precio <= 0 || precio > 500000) {
                    System.out.println("El precio debe ser mayor a 0 y menor a 500000");
                }
            } catch (InputMismatchException a) {
                System.out.println("Solo es posible ingresar numeros");
                scanner.nextLine();
            }
        }
        return precio;
    }

    private String pedirDuracion() {
        int h = -1;
        int m = -1;
        while (m < 0 || m > 59 || h > 4 || h < 0) {
            try {
                System.out.print("Introduce las horas que durara el servicio (0-3):");
                h = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Introduce los minutos que durara el servicio (0-59): ");
                m = scanner.nextInt();
                scanner.nextLine();
                if (m < 0 || m > 59 || h > 4 || h < 0) {
                    System.out.println("La hora no es valida ! Volvamos a cargarla. ");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ingrese un numero válido, por favor");
                scanner.nextLine();
            }

        }
        String duracion = String.format("%02d:%02d", h, m);
        return duracion;
    }


    public TipoPestanias pedirTipoPestanias() {

        TipoPestanias tipo = null;
        int opcion = -1;


        do {
            try {
                System.out.println("Selecciona el tipo de pestañas:");
                System.out.println("1. Clásicas");
                System.out.println("2. 2D");
                System.out.println("3. 3D");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        tipo = TipoPestanias.CLASICAS;
                        break;
                    case 2:
                        tipo = TipoPestanias.DOS_D;
                        break;
                    case 3:
                        tipo = TipoPestanias.TRES_D;
                        break;
                    default:
                        System.out.println("Opción no válida, selecciona nuevamente.");
                }
            } catch (InputMismatchException a) {
                System.out.println("Opción no válida, selecciona nuevamenteee");
                scanner.nextLine();
            }
        } while (opcion != 1 && opcion != 2 && opcion != 3);

        return tipo;
    }

    public void reportarFalla( GestorTurno gestorTurno) {
        Pestanias pestanias = null;
        try {
            pestanias = buscarServicio();
        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }

        String hoy = ConvertirFechaHoras.convertirFechaAString(LocalDate.now());

        gestorTurno.cancelarTurnosXdia(hoy, pestanias.getCodigo_servicio());
    }


    ////////////////////////////////////////GET ////////////////////////////////////////////////////


    public List<Pestanias> getAlmacenServicios() {
        return almacenServicios;
    }

    public void setAlmacenServicios(List<Pestanias> almacenServicios) {
        this.almacenServicios = almacenServicios;
    }

    public void mostrarServicios() {
        if(almacenServicios.isEmpty()){
            System.out.println("No hay servicios de pestañas");
        }else {
            for (Pestanias d : almacenServicios) {
                String tipoPestaniasStr = switch (d.getTipoPestanias()) {
                    case TRES_D -> "3D";
                    case DOS_D -> "2D";
                    case CLASICAS -> "CLÁSICAS";
                };

                System.out.println("- " + d.getCodigo_servicio() + ": " + d.getTipoService() + " " + tipoPestaniasStr + "--PRECIO: " + d.getPrecio());
            }
        }

    }



    ///////////////////////////////////////ARCHIVOS//////////////////////////////////////////////////////////////////.

    public void escribirPestañasEnJson() {
        try (FileWriter writer = new FileWriter(archivoPestania)) {
            gson.toJson(almacenServicios, writer); // Convierte la lista a JSON y la escribe en el archivo
        } catch (IOException e) {
            System.out.println("Error al guardar los datos del Servicio de Pestañas");
        }
    }

    public void leerPestañasDesdeJson() {
        try (FileReader reader = new FileReader(archivoPestania)) {
            // Definir el tipo de la lista de objetos `Pestañas`
            List<Pestanias> servicios = gson.fromJson(reader, new TypeToken<List<Pestanias>>() {
            }.getType());
            almacenServicios = servicios != null ? servicios : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error al leer los datos almacenados del Servicio de Pestañas");
            almacenServicios = new ArrayList<>(); // Inicializa una lista vacía si falla
        }
    }
}

