package gestores;

import Interfaces.IBuscarPorCodigo;
import abstractas.Servicio;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoManicura;
import excepciones.CodigoNoEncontradoException;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GestorManicura implements IBuscarPorCodigo<Servicio> {
    private static Scanner scanner = new Scanner(System.in);
    private List<Manicura> almacenServicios;
    Gson gson = new Gson();
    private static String archivoManicura = "manicura.json";


    public GestorManicura() {
        this.almacenServicios = new ArrayList<>();
    }

    public List<Manicura> getAlmacenServicios() {
        return almacenServicios;
    }

    public void setAlmacenServicios(List<Manicura> almacenServicios) {
        this.almacenServicios = almacenServicios;
    }


    public void agregarServicio() {
        System.out.println("\n");
        System.out.println("Vamos a cargar un servicio...");
        double precio = pedirPrecio();
        String duracion = pedirDuracion();
        System.out.println("En cuanto al diseño..");
        double precioDisenio = pedirDisenio(); //si pone que si devuelve un valor, si pone que no devuelve 0
        TipoManicura tipoManicura = pedirTipoManicura();
        boolean tieneDisenio = precioDisenio > 0;
        GestorPrecios.modificarPrecio(Manicura.class, tipoManicura, precio);
        if(tieneDisenio){
            GestorPrecios.setPrecioDisenio(precioDisenio);
        }
        //Ingresamos el precio al gestor que despues sera calculado en la llamada de este en las clases

        Manicura manicura = new Manicura(duracion, tipoManicura, tieneDisenio); //saque esto (, precio, disenio);
        almacenServicios.add(manicura);
        System.out.println(manicura);
        verificarCarga(manicura);
    }

    public boolean eliminarServicio() {

        String cod_servicio = pedirCodServicio();

        for (Manicura servicio : almacenServicios) {
            if (servicio.getCodigo_servicio().equals(cod_servicio)) {
                return almacenServicios.remove(servicio);
            }
        }
        return false;
    }

    @Override
    public Manicura buscarPorCodigo(String codServicio) throws CodigoNoEncontradoException {
        Manicura servicio = null;
        for (Manicura s : almacenServicios) {
            if (s.getCodigo_servicio().equals(codServicio)) {
                servicio = s;
            }
        }
        if (servicio == null) {
            throw new CodigoNoEncontradoException("El codigo ingresado no existe..");
        }
        return servicio;
    }

    public Manicura buscarServicio() throws CodigoNoEncontradoException {
        mostrarManicura();

        while (true) {
            System.out.println("Ingrese el código('salir' si quiere cancelar la operacion): ");
            String cod_Servicio = scanner.nextLine();

            if (cod_Servicio.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null;
            }
            for (Manicura s : almacenServicios) {
                if (s.getCodigo_servicio().equals(cod_Servicio)) {
                    return s;
                }
            }
            throw new CodigoNoEncontradoException("El código de servicio no existe: " + cod_Servicio);
        }
    }

    public void modificarServicio() {

        Manicura servicio = null;
        try {
            servicio = buscarServicio();
        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }

        boolean continuarModificando = true;
        while (continuarModificando) {
            System.out.println("¿Qué te gustaría modificar del servicio de manicura ?");
            System.out.println("1. Precio");
            System.out.println("2. Duración");
            System.out.println("3. Tipo de manicura");
            System.out.println("2. Diseño");
            System.out.println("0. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                   servicio.setPrecio(pedirPrecio());

                    break;
                case 2:
                    servicio.setDuracion(pedirDuracion());
                    break;
                case 3:
                    servicio.setTipoManicura(pedirTipoManicura());
                    break;
                case 0:
                    continuarModificando = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

            System.out.println("Manicura modificada:");
            System.out.println(servicio);
        }
    }

    public void modificarServicioParametro(Manicura servicio) {

        boolean continuarModificando = true;
        while (continuarModificando) {
            System.out.println("¿Qué te gustaría modificar?");
            System.out.println("1. Duración");
            System.out.println("2. Precio");
            System.out.println("3. Tipo de manicura");
            System.out.println("4. Diseño");
            System.out.println("0. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    servicio.setDuracion(pedirDuracion());
                    break;
                case 2:
                    servicio.setPrecio(pedirPrecio());
                    break;
                case 3:
                    servicio.setTipoManicura(pedirTipoManicura());
                    break;
                case 4:
                    servicio.setPrecioDisenio(pedirDisenio());

                    break;
                case 0:
                    continuarModificando = false;
                default:
                    System.out.println("Opción no válida.");
            }

            System.out.println("Servicio modificado:");
            System.out.println(servicio);
        }
    }

    public void verificarCarga(Manicura servicio) {
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
        //muestra servicios y devuelve el codigo
        for (int i = 1; i <= almacenServicios.size(); i++) {
            System.out.println(i + ". \n" + almacenServicios.get(i));
        }
        int opc = 0;
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
        double precio = 0;
        while (precio <= 0) {
            try {
                System.out.print("Introduce el precio: ");
                precio = scanner.nextDouble();
                scanner.nextLine();


                if (precio <= 0) {
                    System.out.println("El precio debe ser mayor a 0.");
                }
            } catch (InputMismatchException | NullPointerException a) {
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

    public TipoManicura pedirTipoManicura() {
        TipoManicura tipo = null;
        int opcion = -1;
        do {
            try {
                System.out.println("\nSelecciona el tipo de manicura:");
                System.out.println("1. Esculpidas");
                System.out.println("2. Gel");
                System.out.println("3. Semipermanente");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        tipo = TipoManicura.SEMIPERMANENTE;
                        break;
                    case 2:
                        tipo = TipoManicura.GEL;
                        break;
                    case 3:
                        tipo = TipoManicura.ESCULPIDAS;
                        break;
                    default:
                        System.out.println("Opción no válida, selecciona nuevamente.");
                }

            } catch (InputMismatchException a) {
                System.out.println("Opción no válida, selecciona nuevamente.");
                scanner.nextLine();
            }
        } while (opcion != 1 && opcion != 2 && opcion != 3);
        return tipo;
    }

    public double pedirDisenio() {
        int opcion = 0;
        double disenio = 0;

        do {
            try {
                System.out.println("Desea agregar un diseño al servicio?");
                System.out.println("1. Si");
                System.out.println("2. No");
                opcion = scanner.nextInt();
                scanner.nextLine();

                if (opcion != 1 && opcion != 2) {
                    System.out.println("No haz ingresado una opcion valida, vuelve a agregar. ");
                }

            } catch (InputMismatchException a) {
                System.out.println("No haz ingresado una opcion valida, vuelve a agregar. ");
                scanner.nextLine();
            }
        } while (opcion != 1 && opcion != 2);

        if (opcion == 1) {
            disenio = pedirPrecio();

        } else {
            disenio = 0;
        }
        return disenio;
    }


    public void reportarFalla(GestorCliente cliente, GestorTurno gestorTurno) {
        Manicura manicura = null;
        try {
            manicura = buscarServicio();
        } catch (CodigoNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
       // String hoy = Turno.convertirLocalDateAString(LocalDate.now());
        String hoy = ConvertirFechaHoras.convertirFechaAString(LocalDate.now());

        gestorTurno.cancelarTurnosXdia(hoy, manicura.getCodigo_servicio());
    }

    ////////////////////////////////////////GET ////////////////////////////////////////////////////
    public void mostrarManicura() {
        for (Manicura m : almacenServicios)
            System.out.println(m);
    }

    public void mostrarServicios() {
        for (Manicura d : almacenServicios) {
            System.out.println("- " + d.getCodigo_servicio() + ": " + d.getTipoService() + " " + d.getTipoManicura() + "--PRECIO: " + d.getPrecio());
        }
    }

    /////////////////////////////////////////////ARCHIVOS/////////////////////////////////////////////////////////////////////////////.

    public void escribirServiciosEnJson() {
        try (FileWriter writer = new FileWriter(archivoManicura)) {
            gson.toJson(almacenServicios, writer); // Convierte la lista a JSON y la escribe en el archivo
        } catch (IOException e) {
            System.out.println("Error al guardar los datos del Servicio de Manicura");
        }
    }

    public void leerServiciosDesdeJson() {
        try (FileReader reader = new FileReader(archivoManicura)) {
            // Definir el tipo de la lista de objetos `Depilacion`
            List<Manicura> servicios = gson.fromJson(reader, new TypeToken<List<Manicura>>() {
            }.getType());
            almacenServicios = servicios != null ? servicios : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error al leer los datos almacenados del Servicio de Manicura");
            almacenServicios = new ArrayList<>(); // Inicializa una lista vacía si falla
        }
    }

}
