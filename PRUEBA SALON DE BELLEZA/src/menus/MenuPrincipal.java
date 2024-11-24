package menus;

import excepciones.DNInoEncontradoException;
import gestores.*;
import model.Administrador;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {
    Scanner scanner = new Scanner(System.in);

    static String archivoPrecios = "precios.json";
    static String archivoServicios = "servicios.json";
    static String archivoTurnos = "turnos.json";
    static String archivoFacturas = "facturas.json";

    ///personas
    GestorProfesional profesionales;
    GestorAdministrador administradores;
    GestorRecepcionista recepcionistas;
    GestorCliente clientes;


    ///servicios
    GestorDepilacion gestorDepilacion;
    GestorPestania gestorPestania;
    GestorManicura gestorManicura;

    GestorTurno gestorTurno;
    GestorFactura gestorFactura;


    public void menuPrincipal() {

        MenuAdministrador menuAdministrador = new MenuAdministrador();
        MenuRecepcionista menuRecepcionista = new MenuRecepcionista();
        MenuProfesional menuProfesional = new MenuProfesional();

        inicioSistema();

        int opcion = -1;
        do {

            System.out.println("Bienvenido a Estetica Queens!\n");
            System.out.println("¿Quién está ingresando?");
            System.out.println("--------------------");
            System.out.println("1. Administrador ");
            System.out.println("2. Recepcionista");
            System.out.println("3. Profesional");
            System.out.println("0. Salir ");
            System.out.println("--------------------");
            System.out.print("Ingrese una opción: ");
            try {
                opcion = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Opcion invalida: ingrese numeros del 0 al 3");
                scanner.nextLine();
            }

            switch (opcion) {
                case 1:
                    //administrador
                    if (primerIngreso()) {
                        llenarAdministrador();
                    } else {
                        System.out.println("Bienvenido administrador ");
                        String dni = iniciarSesion(1);
                        if (dni != null) {
                            menuAdministrador.mostrarMenu(dni, clientes, profesionales, recepcionistas, administradores, gestorPestania, gestorDepilacion, gestorManicura, gestorTurno, gestorFactura);
                        }
                    }
                    break;
                case 2:
                    //recepcionista
                    if (primerIngreso()) {
                        System.out.println("Un administrador debe ingresar por primera vez al sistema. ");
                    } else {
                        String dni1 = iniciarSesion(2);
                        if (dni1 != null) {
                            System.out.println("Bienvenido Recepcionista !");
                            menuRecepcionista.menuRecepcionistas(clientes, profesionales, recepcionistas, administradores, gestorDepilacion, gestorManicura, gestorPestania, gestorTurno, gestorFactura);
                        }
                    }
                    break;
                case 3:
                    //profesional
                    if (primerIngreso()) {
                        System.out.println("Un administrador debe ingresar por primera vez al sistema. ");
                    } else {
                        String dni3 = iniciarSesion(3);
                        if (dni3 != null) {
                            System.out.println("Bienvenido profesional! ");
                            menuProfesional.menuProfesional(clientes, gestorTurno, dni3, gestorDepilacion, gestorManicura, gestorPestania);
                        }
                    }
                    break;
                case 0:
                    boolean salir = false;
                    do {
                        System.out.println("Deseas guardar los cambios realizados?");
                        System.out.println("1.Salir sin guardar. ");
                        System.out.println("1.Salir y guardar todos los cambios.");

                        int opc = scanner.nextInt();
                        scanner.nextLine();
                        if (opcion == 1) {
                            cerrarSistema();
                            System.out.println("Se ha guardado con exito.");
                            salir = true;
                        } else if (opcion == 2) {
                            System.out.println("saliendo...");
                            salir = true;
                        }
                    } while (!salir);

                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void inicioSistema() {
        gestorDepilacion = new GestorDepilacion();
        gestorPestania = new GestorPestania();
        gestorManicura = new GestorManicura();


        gestorTurno = new GestorTurno(gestorDepilacion, gestorPestania, gestorManicura, clientes);


        profesionales = new GestorProfesional(gestorTurno);
        administradores = new GestorAdministrador();
        recepcionistas = new GestorRecepcionista();
        clientes = new GestorCliente();

        //gestorFactura= new GestorFactura(gestorTurno,)


        gestorTurno.pedirGestorProfesionales(profesionales);


        profesionales.escribirProfesionalesEnJson();
        profesionales.leerProfesionalesDesdeJson();

        administradores.guardarArchivoAdministradores();
        administradores.leerDesdeJSON();

        recepcionistas.escribirArchivoRecepcionistas();
        recepcionistas.leerDesdeJson();

        clientes.escribirClientesEnJson();
        clientes.leerArchivoClientes();

        gestorDepilacion.escribirServiciosEnJson();
        gestorDepilacion.leerServiciosDesdeJson();

        gestorPestania.escribirServiciosEnJson();
        gestorPestania.leerServiciosDesdeJson();

        gestorManicura.escribirServiciosEnJson();
        gestorManicura.leerServiciosDesdeJson();

        gestorTurno.cargarTurnosDesdeArchivo();
        gestorTurno.guardarTurnosEnArchivo();

        //gestorFactura.leerArchivoFacturas();

    }


    public void cerrarSistema() {
        //gaurda tooodo en archivos.
        profesionales.escribirProfesionalesEnJson();
        administradores.guardarArchivoAdministradores();
        recepcionistas.escribirArchivoRecepcionistas();
        clientes.escribirClientesEnJson();

        gestorDepilacion.escribirServiciosEnJson();
        gestorPestania.escribirServiciosEnJson();
        gestorManicura.escribirServiciosEnJson();

        gestorTurno.guardarTurnosEnArchivo();
        gestorFactura.escribirFacturasEnEnJson();

        System.out.println("Se ha cerrado el sistema. ");
    }

    public void llenarAdministrador() {

        administradores.guardarArchivoAdministradores();
        System.out.println("Bienvenido administrador ! ");
    }

    //1 admin/ 2 recepcionista/ 3 profesional
    public String pedirDatos(int tipoPersona) {
        boolean tienecuenta = false;
        String dni = administradores.pedirDNIsinVerificacion();
        String contra = null;
        String contrapedida;
        boolean valido = false;

        do {
            try {
                switch (tipoPersona) {
                    case 1:
                        if (administradores.verificarSiExisteAdministrador(dni)) {
                            contra = administradores.buscarContraseña(dni);

                        }
                        break;
                    case 2:
                        if (recepcionistas.buscarPersonas(dni)) {
                            contra = recepcionistas.buscarContraseña(dni);
                        }
                        break;
                    case 3:
                        if (profesionales.buscarPersonas(dni)) {
                        contra = profesionales.buscarContraseña(dni);
                    }
                        break;
                }

                if (contra == null) {
                    System.out.println("no tiene contrasenia..");
                    break;
                }
                contrapedida = pedirContraseña();

                if (contrapedida.equals(contra)) {
                    valido = true;
                    tienecuenta = true; //
                } else {
                    System.out.println("Contraseña incorrecta. Inténtalo nuevamente.");
                }
            } catch (DNInoEncontradoException e) {
                System.out.println(e.getMessage() + " Vuelva a intentar");
                scanner.nextLine();
            }
        } while (!valido);

        if (valido) {
            return dni;
        } else {
            return null;
        }
    }

    //1 admin/ 2 recepcionista/ 3 profesional
    public String iniciarSesion(int tipoPersona) {
        String dni = pedirDatos(tipoPersona);

        if (dni != null) {
            System.out.println("Entrando..");
        } else {
            System.out.println("No tienes cuenta aun...");
            System.out.println("Verifica que el administrador te haya cargado correctamente..");
        }
        return dni;
    }

    public String pedirContraseña() {
        String contraseña = "";
        do {
            System.out.println("Ingresa una contraseña (entre 6 y 12 caracteres, debe contener al menos un número):");
            contraseña = scanner.nextLine();

            // Validación de longitud de la contraseña y de que contenga al menos un número
            if (contraseña.length() < 6 || contraseña.length() > 12) {
                System.out.println("Tu contraseña es muy débil o tiene un tamaño incorrecto. Vuelve a intentar.");
            } else if (!contraseña.matches(".\\d.")) {  // Verifica que haya al menos un número
                System.out.println("Tu contraseña debe contener al menos un número. Vuelve a intentarlo.");
            }
        } while (contraseña.length() < 6 || contraseña.length() > 12 || !contraseña.matches(".\\d.")); // Bucle sigue hasta que la contraseña sea válida

        return contraseña;
    }

    public boolean primerIngreso() {

        boolean primeringreso = false;
        for (Administrador a : administradores.getAdministradores()) {
            if (a.getContraseña().equals("12345678") && a.getDni().equals("12345678")) {
                primeringreso = true;
            }
        }
        return primeringreso;
    }

//no se que onda esto... NO LO BORREN POR LAS DUDAS.
    /*
    public boolean verificarDniAdministradores(String dni, GestorPersona administradores) {
        List<Administrador> e = administradores.leerArchivoPersona("administradores.json");
        boolean a = false;
        for (Persona aux : e) {
            if (aux.getDni().equals(dni)) {
                a = true;
            }
            return a;
        }
    }

    public boolean verificarContraseniaAdministrador(String dni, String contrasenia, GestorPersona administradores) {
        List<Administrador> e = administradores.leerArchivoPersona("administradores.json");
        boolean a = false;
        for (Administrador aux : e) {
            if (aux.getContraseña().equals(contrasenia)) {
                a = true;
            }
        }
        return a;
    }

    public boolean verificarDniProfesionales(String dni, GestorPersona profesionales) {
        List<Profesional> e = profesionales.leerArchivoPersona("profesionales.json");
        boolean a = false;
        for (Profesional aux : e) {
            if (aux.getDni().equals(dni)) {
                a = true;
            }
        }
        return a;
    }

    public boolean verificarContraseniaProfesional(String dni, String contrasenia, GestorPersona profesionales) {
        List<Profesional> e = profesionales.leerArchivoPersona("profesionales.json");
        boolean a = false;
        for (Profesional aux : e) {
            if (aux.getDni().equals(dni) && aux.getContraseña().equals(contrasenia)) {
                a = true;
            }
        }
        return a;
    }

    public boolean verificarDniRecepcionistas(String dni, GestorPersona recepcionistas) {
        List<Recepcionista> e = recepcionistas.leerArchivoPersona("recepcionistas.json");
        boolean a = false;
        for (Recepcionista aux : e) {
            if (aux.getDni().equals(dni)) {
                a = true;
            }
        }
        return a;
    }

    public boolean verificarContraseniaRecepcionista(String dni, String contrasenia, GestorPersona recepcionistas) {
        List<Recepcionista> e = recepcionistas.leerArchivoPersona("recepcionistas.json");
        boolean a = false;
        for (Recepcionista aux : e) {
            if (aux.getDni().equals(dni) && aux.getContraseña().equals(contrasenia)) {
                a = true;
            }
        }
        return a;
    }
*/
}
