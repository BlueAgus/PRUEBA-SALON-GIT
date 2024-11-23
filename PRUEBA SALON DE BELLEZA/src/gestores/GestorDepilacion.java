package gestores;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoServicio;
import excepciones.CodigoNoEncontradoException;
import model.Depilacion;
import model.Pestanias;
import model.Turno;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GestorDepilacion {

    private static final Scanner scanner = new Scanner(System.in);
    private List<Depilacion> almacenServicios;
    Gson gson = new Gson();
    private static String archivoDepilacion = "depilacion.json";

    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////


    public GestorDepilacion() {
        this.almacenServicios = new ArrayList<>();
    }

    public void agregarServicio() {

        TipoServicio tipoService = TipoServicio.DEPILACION;
        double precio = pedirPrecio();
        String duracion = pedirDuracion();
        TipoDepilacion tipoDepilacion = pedirTipoDepilacion();

        GestorPrecios.modificarPrecio(Depilacion.class, tipoDepilacion , precio);

        //Cuando se instancie depilacion llamara a calcular precio y obtendra el precio ingresado arriba
        Depilacion depilacion = new Depilacion(duracion, tipoDepilacion);
        almacenServicios.add(depilacion);
        System.out.println(depilacion);
        verificarCarga(depilacion);

    }

    public boolean eliminarServicio() {

        String cod_servicio = pedirCodServicio();

        for (Depilacion depilacion : almacenServicios) {
            if (depilacion.getCodigo_servicio().equals(cod_servicio)) {
                return almacenServicios.remove(depilacion);
            }
        }
        return false;
    }

    ///sin codigo
    public Depilacion buscarServicio() throws CodigoNoEncontradoException {

        mostrarServicios();

        while (true) {
            System.out.println("Ingrese el código('salir' si quiere cancelar la operacion): ");
            String cod_Servicio = scanner.nextLine();

            if (cod_Servicio.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null;
            }

            for (Depilacion depilacion : almacenServicios) {
                if (depilacion.getCodigo_servicio().equals(cod_Servicio)) {
                    return depilacion;
                }
            }
            throw new CodigoNoEncontradoException("El código de servicio no existe: " + cod_Servicio);
        }

    }

    ///con codigo
    public Depilacion buscarServicioCodigo(String codServicio) throws CodigoNoEncontradoException {
        Depilacion depilacion = null;
        for (Depilacion s : almacenServicios) {
            if (s.getCodigo_servicio().equals(codServicio)) {
                depilacion = s;
            }
        }
        if (depilacion == null) {
            throw new CodigoNoEncontradoException("El codigo ingresado no existe..");
        }
        return depilacion;
    }


    public void modificarServicio() {

        Depilacion depilacion = null;
        try {
            depilacion = buscarServicio();
        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }

        if (depilacion != null) {
            boolean continuarModificando = true;
            while (continuarModificando) {
                System.out.println("¿Qué te gustaría modificar?(0 para cancelar operacion)");
                System.out.println("1. Tipo de depilación");
                System.out.println("2. Precio");
                System.out.println("3. Duración");
                System.out.println("0. Salir");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        depilacion.setTipoDepilacion(pedirTipoDepilacion());
                    case 2:
                        depilacion.setPrecio(pedirPrecio());
                        break;
                    case 3:
                        depilacion.setDuracion(pedirDuracion());
                        break;
                    case 0:
                        continuarModificando = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }

                System.out.println("Servicio modificado:");
                System.out.println(depilacion);
            }
        }

    }

    public void modificarServicioParametro(Depilacion depilacion) {

        boolean continuarModificando = true;
        while (continuarModificando) {
            System.out.println("¿Qué te gustaría modificar?(0 para cancelar operacion)");
            System.out.println("1. Tipo de depilación");
            System.out.println("2. Precio");
            System.out.println("3. Duración");
            System.out.println("0. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    depilacion.setTipoDepilacion(pedirTipoDepilacion());
                    break;
                case 2:
                    depilacion.setPrecio(pedirPrecio());
                    break;
                case 3:
                    depilacion.setDuracion(pedirDuracion());
                    break;
                case 0:
                    continuarModificando = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

            System.out.println("Servicio modificado:");
            System.out.println(depilacion);
        }
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////

    public void verificarCarga(Depilacion depilacion) {
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
                        modificarServicioParametro(depilacion);
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

        for (int i = 0; i < almacenServicios.size(); i++) {
            System.out.println(i + "- \n" + almacenServicios.get(i));
        }
        int opc;
        while (true) {

            try {
                System.out.println("OPCION: (o escriba 'salir' para cancelar) ");
                String opcElegida = scanner.nextLine();


                if (opcElegida.equalsIgnoreCase("salir")) {
                    System.out.println("Operación cancelada por el usuario.");
                    return null;
                }

                ///pasa a int un string
                opc = Integer.parseInt(opcElegida);
                if (opc < 0 || opc > almacenServicios.size()) {
                    System.out.println("Selección inválida. Inténtelo de nuevo.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no valida. Por favor vuelva a intentarlo");

            }
        }
        return almacenServicios.get(opc).getCodigo_servicio();
    }

    // Validación del precio
    private double pedirPrecio() {
        double precio = -1;
        while (precio <= 0) {
            try {
                System.out.print("Introduce el precio del servicio: ");
                precio = scanner.nextDouble();
                scanner.nextLine();

                if (precio <= 0) {
                    System.out.println("El precio debe ser mayor a 0.");
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
        while (m < 0 || m > 59 || h > 23 || h < 0) {
            try {
                System.out.print("Introduce las horas que durara el servicio (0-23):");
                h = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Introduce los minutos que durara el servicio (0-59): ");
                m = scanner.nextInt();
                scanner.nextLine();
                if (m < 0 || m > 59 || h > 23 || h < 0) {
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

    public TipoDepilacion pedirTipoDepilacion() {

        TipoDepilacion tipo = null;

        int opcion=-1;
        do {
            try {
                System.out.println("Selecciona el tipo de depilación:");
                System.out.println("1. Cera");
                System.out.println("2. Láser");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        tipo = TipoDepilacion.CERA;
                        break;
                    case 2:
                        tipo = TipoDepilacion.LASER;
                        break;
                    default:
                        System.out.println("Opción no válida, selecciona nuevamente.");
                        break;
                }
            } catch (InputMismatchException a) {

                System.out.println("Opción no válida, selecciona nuevamenteeee");
                scanner.nextLine();
            }
        } while (opcion != 1 && opcion != 2);


        return tipo;
    }


    public TipoServicio pedirTipoServicio() {

        TipoServicio tipo = null;
        while (tipo == null) {
            try
            {
                System.out.println("Selecciona el tipo de servicio:");
                System.out.println("1. Uñas");
                System.out.println("2. Pestañas");
                System.out.println("3. Depilación");
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        tipo = TipoServicio.MANICURA;
                        break;
                    case 2:
                        tipo = TipoServicio.PESTANIAS;
                        break;
                    case 3:
                        tipo = TipoServicio.DEPILACION;
                        break;
                    default:
                        System.out.println("Opción no válida, selecciona nuevamente.");
                }
            }catch (InputMismatchException e1){
                System.out.println("Error: Por favor, ingresa un número válido.");
                scanner.nextLine();
            }

        }
        return tipo;
    }


    public void reportarFalla(GestorCliente cliente, GestorTurno gestorTurno) {
        Depilacion depilacion = null;
        try {
            depilacion = buscarServicio();
        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
        String hoy= Turno.convertirLocalDateAString(LocalDate.now());
        gestorTurno.cancelarTurnosXdia(hoy, cliente, depilacion.getCodigo_servicio());
    }

    ////////////////////////////////////////GET Y SET////////////////////////////////////////////////////

    public List<Depilacion> getAlmacenServicios() {
        return almacenServicios;
    }

    public void mostrarServicios() {
        for (Depilacion d : almacenServicios) {
            System.out.println("- " + d.getCodigo_servicio() + ": " + d.getTipoService() + " " + d.getTipoDepilacion() + "--PRECIO: " + d.getPrecio());
        }
    }

    /////////////ARCHIVOS.
    public void escribirServiciosEnJson() {
        try (FileWriter writer = new FileWriter(archivoDepilacion)) {
            gson.toJson(almacenServicios, writer); // Convierte la lista a JSON y la escribe en el archivo
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo JSON: " + e.getMessage());
        }
    }

    public void leerServiciosDesdeJson() {
        try (FileReader reader = new FileReader(archivoDepilacion)) {
            // Definir el tipo de la lista de objetos `Depilacion`
            List<Depilacion> servicios = gson.fromJson(reader, new TypeToken<List<Depilacion>>() {
            }.getType());
            almacenServicios = servicios != null ? servicios : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
            almacenServicios = new ArrayList<>(); // Inicializa una lista vacía si falla
        }
    }
}
