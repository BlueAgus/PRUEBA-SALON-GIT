package menus;

import enumeraciones.TipoServicio;
import excepciones.CodigoNoEncontradoException;
import excepciones.DNInoEncontradoException;
import excepciones.FacturaNoExistenteException;
import gestores.*;
import model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuAdministrador {

    Scanner scanner = new Scanner(System.in);

    public void mostrarMenu(String dni, GestorCliente clientes, GestorProfesional profesionales, GestorRecepcionista recepcionista, GestorAdministrador administradores, GestorPestania pestania, GestorDepilacion depilacion, GestorManicura manicura, GestorTurno turnos, GestorFactura facturas) {

        int opcion;

        do {
            System.out.println("--------------------");
            System.out.println("Bienvenido al menu de administrador ");
            System.out.println("1.Menu de usuarios");
            System.out.println("2.Menu de servicios");
            System.out.println("3.Menu de turnos");
            System.out.println("4.Menu de facturas");
            System.out.println("0. Volver al inicio");
            System.out.println("--------------------");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    menuUsuarios(dni, clientes, profesionales, recepcionista, administradores, pestania, depilacion, manicura, turnos);
                    break;
                case 2:
                    menuServicio(pestania, depilacion, manicura, clientes, turnos);
                    break;
                case 3:
                    menuTurnos(turnos, clientes, profesionales);
                    break;
                case 4:
                    menuFacturas(facturas, clientes, turnos);
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void menuUsuarios(String dni, GestorCliente clientes, GestorProfesional profesionales, GestorRecepcionista recepcionista, GestorAdministrador administradores, GestorPestania pestania, GestorDepilacion depilacion, GestorManicura manicura, GestorTurno turnos) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1.Gestionar recepcionistas");
            System.out.println("2.Gestionar profesionales");
            System.out.println("3.Gestionar clientes");
            System.out.println("4.Gestionar mis datos");
            System.out.println("0- Volver al Menú anterior");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    subMenuRecepcionista(recepcionista, pestania, depilacion, manicura);
                    break;
                case 2:
                    subMenuProfesionales(profesionales, pestania, depilacion, manicura);
                    break;
                case 3:
                    subMenuClientes(clientes, pestania, depilacion, manicura);
                    break;
                case 4:

                    try {
                        Administrador administrador = administradores.buscarPersona(dni);

                        administradores.modificarAdministrador(administrador);

                    } catch (DNInoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }

                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }


    public void subMenuRecepcionista(GestorRecepcionista recepcionistas, GestorPestania pestania, GestorDepilacion depilacion, GestorManicura manicura) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("RECEPCIONISTA");
            System.out.println("1.Agregar");
            System.out.println("2.Eliminar");
            System.out.println("3.Buscar por su dni");
            System.out.println("4.Modificar datos");
            System.out.println("0. Volver al Menú anterior");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    recepcionistas.agregarPersona();
                    break;
                case 2:
                    System.out.println("¿Cual es el dni del Recepcionista que desea eliminar?");

                    String dni = recepcionistas.pedirDNIsinVerificacion();
                    if (recepcionistas.eliminarPersona(dni)) {
                        System.out.println("Recepcionista eliminado exitosamente!");
                    } else {
                        System.out.println("Error al eliminar recepcionista");
                    }
                    break;
                case 3:

                    System.out.println("¿Cual es el dni del Recepcionista que desea buscar?");

                    String dni1 = recepcionistas.pedirDNIsinVerificacion();

                    try {
                        Recepcionista recepcionista = (Recepcionista) recepcionistas.buscarPersona(dni1);
                        System.out.println(recepcionista);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("¿Cual es el DNI del Recepcionista al que le desea modificar los datos");

                    String dni2 = recepcionistas.pedirDNIsinVerificacion();
                    try {
                        Recepcionista recepcionista = recepcionistas.buscarPersona(dni2);
                        System.out.println(recepcionista);
                        recepcionistas.modificarPersona(recepcionista);
                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }


    public void subMenuProfesionales(GestorProfesional profesionales, GestorPestania pestania, GestorDepilacion depilacion, GestorManicura manicura) {

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n");
            System.out.println("PROFESIONALES");
            System.out.println("1.Agregar");
            System.out.println("2.Eliminar");
            System.out.println("3.Modificar datos");
            System.out.println("4.Buscar por DNI");
            System.out.println("5.Mostrar profesionales del salon ");
            ///o 3 listar, 1 listra manicura etc
            System.out.println("6.Mostrar profesionales por servicio específico");
            System.out.println("0. Volver al Menú anterior");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    profesionales.agregarPersona();
                    break;
                case 2:
                    System.out.println("¿Cual es el dni del profesional que desea eliminar?");

                    String dni = profesionales.pedirDNIsinVerificacion();
                    if (profesionales.eliminarPersona(dni)) {
                        System.out.println("Profesional eliminado exitosamente!");
                    } else {
                        System.out.println("Error al eliminar");
                    }
                    break;

                case 3:
                    System.out.println("¿Cual es el DNI del profesional al que le desea modificar los datos");

                    String dni2 = profesionales.pedirDNIsinVerificacion();

                    try {
                        Profesional profesional = profesionales.buscarPersona(dni2);

                        System.out.println(profesional);

                        profesionales.modificarProfesional(profesional);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }

                    break;
                case 4:

                    System.out.println("¿Cual es el dni del profesional que desea buscar?");

                    String dni1 = profesionales.pedirDNIsinVerificacion();

                    try {
                        Profesional persona = profesionales.buscarPersona(dni1);
                        System.out.println(persona);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                case 5:
                    profesionales.mostrarTodos();
                    break;

                case 6:
                    /*
                    Servicio servicio = null;
                    while (true) {
                        try {
                            servicio = servicios.buscarServicio();
                        } catch (CodigoNoEncontradoException e) {
                            System.out.println(e.getMessage());
                            continue;
                        }

                        for (Profesional p : profesionales.getProfesionales()) {
                            if (p.verificarProfesion(servicio.getCodigo_servicio())) {
                                System.out.println(p.toString());
                            }
                        }
                        break;
                    }*/
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    public void subMenuClientes(GestorCliente clientes, GestorPestania pestania, GestorDepilacion depilacion, GestorManicura manicura) {

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        do {
            try {
                System.out.println("\n");
                System.out.println("CLIENTES");
                System.out.println("1.Agregar");
                System.out.println("2.Eliminar");
                System.out.println("3.Buscar por su dni ");
                System.out.println("4.Modificar datos ");
                System.out.println("5.Mostrar todos lo clientes");
                System.out.println("0. Volver al Menú anterior");
                System.out.print("Ingrese una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    case 1:
                        clientes.agregarPersona();
                        break;
                    case 2:
                        System.out.println("¿Cual es el dni del cliente que desea eliminar?");

                        String dni = clientes.pedirDNIsinVerificacion();
                        if (clientes.eliminarPersona(dni)) {
                            System.out.println("Cliente eliminado exitosamente!");
                        } else {
                            System.out.println("Error al eliminar el cliente");
                        }
                        break;
                    case 3:

                        System.out.println("¿Cual es el dni del cliente que desea buscar?");

                        String dni1 = clientes.pedirDNIsinVerificacion();

                        try {
                            Cliente cliente = clientes.buscarPersona(dni1);
                            System.out.println(cliente);

                        } catch (DNInoEncontradoException a) {
                            System.out.println(a.getMessage());
                        }
                        break;
                    case 4:
                        System.out.println("¿Cual es el DNI del cliente al que le desea modificar los datos");

                        String dni2 = clientes.pedirDNIsinVerificacion();
                        try {
                            Cliente cliente = clientes.buscarPersona(dni2);
                            System.out.println(cliente);

                            clientes.modificarPersona(cliente);

                        } catch (DNInoEncontradoException a) {
                            System.out.println(a.getMessage());
                        }
                        break;
                    case 5:
                        clientes.mostrarTodos();
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException a) {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
            }
        } while (opcion != 0);

    }

    public void menuServicio(GestorPestania pestania, GestorDepilacion depilacion, GestorManicura manicura, GestorCliente cliente, GestorTurno turnos) {

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        do {
            try {
                System.out.println("\n");
                System.out.println("SERVICIOS");
                System.out.println("1.Agregar");
                System.out.println("2.Eliminar ");
                System.out.println("3.Modificar ");
                System.out.println("4.Buscar servicio por tipo ");
                System.out.println("5.Mostrar todos los servicios");
                System.out.println("6.Reportar falla de un servicio");
                System.out.println("0. Volver al Menú anterior");
                System.out.print("Ingrese una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 0:
                        System.out.println("Saliendo...");
                        break;

                    case 1:
                        int opc = -1; // Variable para almacenar la opción seleccionada

                        do {
                            System.out.println("Que servicio desea agregar? ");
                            System.out.println("1. Depilacion");
                            System.out.println("2. Manicura");
                            System.out.println("3. Pestañas");
                            System.out.print("Seleccione una opción (1-3): ");
                            opc = scanner.nextInt();

                            if (opc == 1) {
                                depilacion.agregarServicio();
                            } else if (opc == 2) {
                                manicura.agregarServicio();
                            } else if (opc == 3) {
                                pestania.agregarServicio();
                            } else {
                                System.out.println("¡Opción no válida! Por favor, intente de nuevo.");
                            }
                        } while (opc < 1 || opc > 3);
                        scanner.close();
                        break;
                    case 2:
                        int opc2 = -1; // Variable para almacenar la opción seleccionada

                        do {
                            System.out.println("\n");
                            System.out.println("Que servicio desea eliminar? ");
                            System.out.println("1. Depilacion");
                            System.out.println("2. Manicura");
                            System.out.println("3. Pestañas");
                            System.out.print("Seleccione una opción (1-3): ");
                            opc2 = scanner.nextInt();

                            if (opc2 == 1) {
                                if (depilacion.eliminarServicio()) {
                                    System.out.println("Depilacion eliminada exitosamente");
                                } else {
                                    System.out.println("Error al eliminar servicio");
                                }
                            } else if (opc2 == 2) {
                                if (manicura.eliminarServicio()) {
                                    System.out.println("Manicura eliminada exitosamente");
                                } else {
                                    System.out.println("Error al eliminar servicio");
                                }
                            } else if (opc2 == 3) {
                                if (pestania.eliminarServicio()) {
                                    System.out.println("Pestana eliminada exitosamente");
                                } else {
                                    System.out.println("Error al eliminar servicio");
                                }
                            } else {
                                System.out.println("¡Opción no válida! Por favor, intente de nuevo.");
                            }
                        } while (opc2 < 1 || opc2 > 3);
                        scanner.close();
                        break;

                    case 3:
                        int op3 = -1;
                        do {
                            System.out.println("\n");
                            System.out.println("¿Qué servicio desea modificar?");
                            System.out.println("1. Depilación");
                            System.out.println("2. Manicura");
                            System.out.println("3. Pestañas");
                            System.out.print("Seleccione una opción (1-3): ");
                            op3 = scanner.nextInt();

                            if (op3 == 1) {
                                depilacion.modificarServicio();
                            } else if (op3 == 2) {
                                manicura.modificarServicio();
                            } else if (op3 == 3) {
                                pestania.modificarServicio();
                            } else {
                                System.out.println("¡Opción no válida! Por favor, intente de nuevo.");
                            }
                        } while (op3 < 1 || op3 > 3);
                        scanner.close();
                        break;
                    case 4:
                        int opc4 = -1;
                        do {
                            System.out.println("\n");
                            System.out.println("¿Qué servicio desea elegir?");
                            System.out.println("1. Depilación");
                            System.out.println("2. Manicura");
                            System.out.println("3. Pestañas");
                            System.out.print("Seleccione una opción (1-3): ");
                            opc4 = scanner.nextInt();

                            if (opc4 == 1) {
                                System.out.println("SERVICIOS DE DEPILACION-----------------------");
                                depilacion.mostrarServicios();
                            } else if (opc4 == 2) {
                                System.out.println("SERVICIOS DE MANICURA-----------------------");
                                manicura.mostrarManicura();
                            } else if (opc4 == 3) {
                                System.out.println("SERVICIOS DE PESTAÑAS-----------------------");
                                pestania.mostrarServicios();
                            } else {
                                System.out.println("¡Opción no válida! Por favor, intente de nuevo.");
                            }
                        } while (opc4 < 1 || opc4 > 3);
                        scanner.close();

                        break;
                    case 5:
                        System.out.println("\n");
                        System.out.println("Estos son los servicios disponibles en Queens: ");
                        System.out.println("SERVICIOS DE DEPILACION-----------------------");
                        depilacion.mostrarServicios();
                        System.out.println("---------------------------------------------");
                        System.out.println("SERVICIOS DE MANICURA-----------------------");
                        manicura.mostrarManicura();
                        System.out.println("---------------------------------------------");
                        System.out.println("SERVICIOS DE PESTAÑAS-----------------------");
                        pestania.mostrarServicios();
                        System.out.println("---------------------------------------------");

                        break;
                    case 6:

                        int opc5 = -1;
                        do {
                            System.out.println("\n");
                            System.out.println("¿En qué servicio desea reportar falla?");
                            System.out.println("1. Depilación");
                            System.out.println("2. Manicura");
                            System.out.println("3. Pestañas");
                            System.out.print("Seleccione una opción (1-3): ");
                            opc5 = scanner.nextInt();

                            if (opc5 == 1) {
                                depilacion.reportarFalla(cliente, turnos);

                            } else if (opc5 == 2) {
                                manicura.reportarFalla(cliente, turnos);
                            } else if (opc5 == 3) {
                                pestania.reportarFalla(cliente, turnos);
                            } else {
                                System.out.println("¡Opción no válida! Por favor, intente de nuevo.");
                            }
                        } while (opc5 < 1 || opc5 > 3);
                        scanner.close();

                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException a) {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
            }
        } while (opcion != 0);

    }

    public void menuTurnos(GestorTurno turnos, GestorCliente clientes, GestorProfesional profesionales) {

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        do {
            try {
                System.out.println("\n");
                System.out.println("TURNOS");
                System.out.println("1.Agregar ");
                System.out.println("2.Eliminar ");
                System.out.println("3.Modificar datos ");
                System.out.println("4.Buscar un turno");
                System.out.println("5.Listar turnos proximos ");
                System.out.println("6.Listar historial de turnos");
                System.out.println("7.Turnos segun profesional");
                System.out.println("8.Turnos segun cliente");
                System.out.println("0. Volver al Menú anterior");
                System.out.print("Ingrese una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    case 1:
                        if (turnos.agregarTurno()) {
                            System.out.println("Turno agregado exitosamente!");
                        } else {
                            System.out.println("Error al agregar turno");
                        }
                        break;
                    case 2:
                        turnos.eliminarTurno();
                        break;
                    case 3:
                        int opc = -1; // Variable para almacenar la opción seleccionada

                        do {
                            System.out.println("\n");
                            System.out.println("¿De que servicio desea eliminar el turno? ");
                            System.out.println("1. Depilacion");
                            System.out.println("2. Manicura");
                            System.out.println("3. Pestañas");
                            System.out.print("Seleccione una opción (1-3): ");
                            opc = scanner.nextInt();

                            if (opc == 1) {
                                turnos.modificarTurno(TipoServicio.DEPILACION);
                            } else if (opc == 2) {
                                turnos.modificarTurno(TipoServicio.MANICURA);
                            } else if (opc == 3) {
                                turnos.modificarTurno(TipoServicio.PESTANIAS);
                            } else {
                                System.out.println("¡Opción no válida! Por favor, intente de nuevo.");
                            }
                        } while (opc < 1 || opc > 3);
                        scanner.close();

                        break;
                    case 4:
                        Turno turno = turnos.buscarTurnoXclienteFechaHorario();
                        if (turno == null) {
                            System.out.println("Turno buscado no encontrado.");
                        } else {
                            System.out.println("Turno buscado:");
                            System.out.println(turno);
                        }
                        break;
                    case 5:
                        List<Turno> turnosProximos = turnos.mostrarTurnosVigentes();
                        int contador = 0;

                        for (Turno turno1 : turnosProximos) {
                            System.out.println(contador + " " + turno1);
                            contador++;
                        }
                        break;
                    case 6:
                        turnos.mostrarHistorialTurnos();
                        break;
                    case 7:
                        turnosXprofesional(turnos, profesionales);
                        break;
                    case 8:
                        turnosXcliente(turnos, clientes);
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException a) {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
            }
        } while (opcion != 0);

    }

    public void turnosXprofesional(GestorTurno turnos, GestorProfesional profesionales) {

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        do {
            try {
                System.out.println("\n");
                System.out.println("1.Turnos proximos de un profesional");
                System.out.println("2.Historial de turnos de un profesional");
                System.out.println("0. Volver al Menú anterior");
                System.out.print("Ingrese una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    case 1:
                        String dni = profesionales.pedirDNIsinVerificacion();
                        List<Turno> turnosProximos = turnos.buscarTurnosXdniProfesionalVigentes(dni);

                        int contador = 0;
                        if (turnosProximos.isEmpty()) {
                            System.out.println("El profesional no tiene turnos agendados proximamente");
                        } else {
                            for (Turno turno : turnosProximos) {
                                System.out.println(contador + " " + turno);
                                contador++;
                            }
                        }
                        break;
                    case 2:
                        String dni1 = profesionales.pedirDNIsinVerificacion();
                        List<Turno> historialTurnos = turnos.historialTurnosXprofesional(dni1);

                        int contador1 = 0;
                        if (historialTurnos.isEmpty()) {
                            System.out.println("El profesional no tiene un historial de turnos");
                        } else {
                            for (Turno turno : historialTurnos) {
                                System.out.println(contador1 + " " + turno);
                                contador1++;
                            }
                        }
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException a) {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
            }
        }
        while (opcion != 0);

    }

    public void turnosXcliente(GestorTurno turnos, GestorCliente clientes) {

        Scanner scanner = new Scanner(System.in);
        int opcion=-1;

        do {
            try {
                System.out.println("\n");
            System.out.println("1.Turnos proximos de un cliente especifico");
            System.out.println("2.Historial de turnos de un cliente");
            System.out.println("0. Volver al Menú anterior");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    String dni = clientes.pedirDNIsinVerificacion();
                    List<Turno> turnosVigentes = turnos.buscarTurnosXdniClienteVigentes(dni);
                    int contador1 = 0;
                    if (turnosVigentes.isEmpty()) {
                        System.out.println("El cliente no tiene agendado turnos proximamente");
                    } else {
                        for (Turno turno : turnosVigentes) {
                            System.out.println(contador1 + " " + turno);
                            contador1++;
                        }
                    }
                    break;
                case 2:
                    String dni1 = clientes.pedirDNIsinVerificacion();
                    List<Turno> historialTurnos = turnos.historialTurnosXcliente(dni1);
                    int contador = 0;
                    if (historialTurnos.isEmpty()) {
                        System.out.println("El cliente no tiene un historial de turnos");
                    } else {
                        for (Turno turno : historialTurnos) {
                            System.out.println(contador + " " + turno);
                            contador++;
                        }
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
            }catch (InputMismatchException a)
            {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
            }
        } while (opcion != 0);

    }

    public void menuFacturas(GestorFactura facturas, GestorCliente clientes, GestorTurno gestorTurno) {

        Scanner scanner = new Scanner(System.in);
        int opcion=-1;

        do {
            try {
                System.out.println("\n");
                System.out.println("Facturas");
            System.out.println("1.Agregar ");
            System.out.println("2.Eliminar ");
            System.out.println("3.Modificar ");
            System.out.println("4.Buscar");
            System.out.println("5.Ver historial de facturas");
            System.out.println("6.Ver historial de facturas por cliente");
            System.out.println("7.Resumen de ganancia");
            System.out.println("0. Volver al Menú anterior");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    facturas.crearFactura();
                    break;
                case 2:
                    System.out.println("Para eliminar una factura, se solicita el DNI del cliente");
                    while (true) {
                        String dni = clientes.pedirDNIsinVerificacion();
                        try {
                            facturas.historialFacturasPorCliente(dni);

                            while (true) {
                                System.out.println("Ingrese el codigo de la factura que quiere eliminar:");
                                String codigo = scanner.nextLine();
                                scanner.nextLine();

                                try {
                                    facturas.eliminarFactura(codigo);
                                    break;
                                } catch (FacturaNoExistenteException e) {
                                    System.out.println(e.getMessage());
                                    System.out.println("¿Desea intentar de nuevo? S/N");
                                    String respuesta = scanner.nextLine();
                                    if (!respuesta.equalsIgnoreCase("S")) {
                                        System.out.println("Operación cancelada.");
                                        return;
                                    }
                                }
                            }
                            break;
                        } catch (DNInoEncontradoException e) {
                            System.out.println(e.getMessage());
                            System.out.println("¿Desea intentar de nuevo? S/N");
                            String respuesta = scanner.nextLine();
                            if (!respuesta.equalsIgnoreCase("S")) {
                                System.out.println("Operación cancelada.");
                                return;
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("Para modificar una factura, se solicita el DNI del cliente al que corresponda");
                    String dni1 = clientes.pedirDNIsinVerificacion();
                    try {
                        facturas.historialFacturasPorCliente(dni1);
                        facturas.modificarFactura();
                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }

                    break;
                case 4:
                    buscarFacturas(facturas, clientes);
                    break;
                case 5:
                    System.out.println(facturas.getArchivoFacturas());
                    break;
                case 6:
                    String dni = clientes.pedirDNIsinVerificacion();
                    try {
                        facturas.historialFacturasPorCliente(dni);

                    } catch (DNInoEncontradoException a) {
                        System.out.println(a.getMessage());
                    }
                    break;
                case 7:
                    int opc = 0;
                    while (true) {

                        System.out.println("1- Ganancia de un día específico");
                        //System.out.println("2- Ganancia de un mes específico");
                        // System.out.println("3- Ganancia de un año específico");
                        System.out.println("0. Volver al Menú anterior");
                        System.out.print("Ingrese una opción: ");
                        try {
                            opc = scanner.nextInt();

                            if (opc < 0 || opc > 3) {
                                System.out.println("Opcion no valida");
                            } else {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("OPCION INVALIDA");
                        }
                    }

                    switch (opc) {
                        case 1:
                            LocalDate fecha = gestorTurno.pedirFecha();
                            String f = convertirFechaAString(fecha);

                            if (fecha == null) {
                                break;
                            } else {
                                System.out.println("Ganancia del día: " + fecha + " " + facturas.gananciaXdia(f));
                            }
                            break;
                        case 2:
                            int mes = 0;
                            int año = 0;

                            while (true) {
                                try {
                                    System.out.println("Ingrese el mes: ");
                                    mes = scanner.nextInt();
                                    scanner.nextLine();

                                    System.out.println("Ingrese el año: ");
                                    año = scanner.nextInt();
                                    scanner.nextLine();

                                    if (mes < 0 || mes > 12 || año < 2024 || año > 2050) {
                                        System.out.println("Error en la fecha!");
                                    } else {
                                        LocalDate fechaHoy = LocalDate.now();

                                        LocalDate fechaIngresada = LocalDate.of(año, mes, 1);

                                        // Verificar si la fecha ingresada es posterior a la actual
                                        if (fechaIngresada.isAfter(fechaHoy)) {
                                            System.out.println("La fecha ingresada es posterior a hoy.");
                                        } else {
                                            break;
                                        }
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Opcion invalida, ingrese una opcion valida");
                                }

                                //  System.out.println("Ganancia: " + Month.of(mes).getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()) + " " + año + ": " + facturas.gananciaXmes(mes, año));
                            }

                            break;
                        case 3:
                            int año1 = 0;

                            while (true) {
                                try {

                                    System.out.println("Ingrese el año: ");
                                    año1 = scanner.nextInt();
                                    scanner.nextLine();

                                    if (año1 < 2024 || año1 > 2050) {
                                        System.out.println("Error en la fecha!");
                                    } else {
                                        break;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Opcion invalida, ingrese una opcion valida");
                                }
                            }

                            //System.out.println("Ganancia del año " + año1 + facturas.gananciaXaño(año1));

                            break;
                        case 0:
                            break;
                        default:
                            System.out.println("Opcion invalida");
                    }


                    break;
                default:
                    System.out.println("Opción no válida.");
            }
            }catch (InputMismatchException a)
            {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
            }
        } while (opcion != 0);
    }

    public void buscarFacturas(GestorFactura facturas, GestorCliente clientes) {

        Scanner scanner = new Scanner(System.in);
        int opcion=-1;

        do {
            try {
                System.out.println("\n");
            System.out.println("1.Buscar por codigo ");
            System.out.println("2.Buscar por fecha ");
            System.out.println("3.Buscar por cliente ");
            System.out.println("0. Volver al Menú anterior");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    while (true) {
                        System.out.println("Ingrese el codigo de la factura:");
                        String codigo = scanner.nextLine();
                        try {
                            Factura factura = facturas.buscarFacturaPorCodigo(codigo);
                            System.out.println(factura);
                            break;
                        } catch (CodigoNoEncontradoException a) {
                            System.out.println(a.getMessage());
                            System.out.println("¿Desea intentar de nuevo? S/N");
                            String respuesta = scanner.nextLine();
                            if (!respuesta.equalsIgnoreCase("S")) {
                                System.out.println("Operación cancelada.");
                                return;
                            }
                        }
                    }
                    break;
                case 2:
                    while (true) {
                        System.out.println("Ingrese la fecha (YYYY-MM-DD):");
                        String fecha = scanner.nextLine();

                        List<Factura> facturasXfecha = facturas.verHistorialPorFecha(fecha);

                        if (facturasXfecha == null || facturasXfecha.isEmpty()) {
                            System.out.println("Intente nuevamente.");
                            continue;
                        }
                        System.out.println(facturasXfecha);
                        break;
                    }
                    break;
                case 3:
                    while (true) {
                        String dni = clientes.pedirDNIsinVerificacion();
                        try {
                            facturas.historialFacturasPorCliente(dni);
                            break;
                        } catch (DNInoEncontradoException e) {
                            System.out.println(e.getMessage());
                            System.out.println("¿Desea intentar de nuevo? S/N");
                            String respuesta = scanner.nextLine();
                            if (!respuesta.equalsIgnoreCase("S")) {
                                System.out.println("Operación cancelada.");
                                return;
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
            }catch (InputMismatchException a)
            {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
            }
        } while (opcion != 0);
    }

    public void menuPrecios(GestorPestania pestania, GestorDepilacion depilacion, GestorManicura manicura) {

        Scanner scanner = new Scanner(System.in);
        int opcion=-1;

        do {
            try {
                System.out.println("\n");
            System.out.println("1. Modificar precio base de un servicio");
            System.out.println("2. Aumentar TODOS los precios");//aumentar todos o por clase
            System.out.println("3. Aumentar precios de un tipo de servicio");//aumentar todos o por clase
            System.out.println("4. Ver todos los precios");
            System.out.println("0. Volver al Menú anterior");
            System.out.print("Ingrese una opción: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    int opc5 = -1;
                    do {
                        System.out.println("\n");
                        System.out.println("¿Qué servicio desea modificar el precio?");
                        System.out.println("1. Depilación");
                        System.out.println("2. Manicura");
                        System.out.println("3. Pestañas");
                        System.out.print("Seleccione una opción (1-3): ");
                        opc5 = scanner.nextInt();

                        if (opc5 == 1) {
                            depilacion.mostrarServicios();
                            Depilacion depi;
                            while (true) {
                                try {
                                    depi = depilacion.buscarServicio();
                                    break;
                                } catch (CodigoNoEncontradoException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            double precio;

                            while (true) {

                                try {
                                    System.out.println("Ingrese el nuevo precio para el servicio DEPILACION(o escriba 'salir' para cancelar): ");

                                    String opcElegida = scanner.nextLine();

                                    if (opcElegida.equalsIgnoreCase("salir")) {
                                        System.out.println("Operación cancelada por el usuario.");
                                        return;
                                    }
                                    ///pasa a int un string
                                    precio = Double.parseDouble(opcElegida);
                                    if (precio < 0 || precio > 500000) {
                                        System.out.println("Precio fuera de rango! vuelva a intentar un numero entre 0 y 500000");
                                    } else {
                                        break;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Entrada no valida. Por favor ingrese un número.");
                                    scanner.nextLine();
                                }
                            }
                            GestorPrecios.modificarPrecio(depi.getClass(), depi.getTipoDepilacion(), precio);
                            System.out.println("CAMBIADO EXITOSAMENTE! El precio de DEPILACION se ha actualizado: ");
                            System.out.println("NUEVO PRECIO: " + GestorPrecios.obtenerPrecio(depi.getClass(), depi.getTipoService()));

                        } else if (opc5 == 2) {
                            manicura.mostrarManicura();
                            Manicura manicura1;
                            while (true) {
                                try {
                                    manicura1 = manicura.buscarServicio();
                                    break;
                                } catch (CodigoNoEncontradoException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            double precio;

                            while (true) {

                                try {
                                    System.out.println("Ingrese el nuevo precio para el servicio MANICURA(o escriba 'salir' para cancelar): ");

                                    String opcElegida = scanner.nextLine();

                                    if (opcElegida.equalsIgnoreCase("salir")) {
                                        System.out.println("Operación cancelada por el usuario.");
                                        return;
                                    }
                                    ///pasa a int un string
                                    precio = Double.parseDouble(opcElegida);
                                    if (precio < 0 || precio > 500000) {
                                        System.out.println("Precio fuera de rango! vuelva a intentar un numero entre 0 y 500000");
                                    } else {
                                        break;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Entrada no valida. Por favor ingrese un número.");
                                    scanner.nextLine();
                                }
                            }
                            GestorPrecios.modificarPrecio(manicura1.getClass(), manicura1.getTipoManicura(), precio);
                            System.out.println("CAMBIADO EXITOSAMENTE! El precio de MANICURA se ha actualizado: ");
                            System.out.println("NUEVO PRECIO: " + GestorPrecios.obtenerPrecio(manicura1.getClass(), manicura1.getTipoService()));

                        } else if (opc5 == 3) {
                            pestania.mostrarServicios();
                            Pestanias pestanias;
                            while (true) {
                                try {
                                    pestanias = pestania.buscarServicio();
                                    break;
                                } catch (CodigoNoEncontradoException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            double precio;

                            while (true) {

                                try {
                                    System.out.println("Ingrese el nuevo precio para el servicio PESTAÑAS(o escriba 'salir' para cancelar): ");

                                    String opcElegida = scanner.nextLine();

                                    if (opcElegida.equalsIgnoreCase("salir")) {
                                        System.out.println("Operación cancelada por el usuario.");
                                        return;
                                    }
                                    ///pasa a int un string
                                    precio = Double.parseDouble(opcElegida);
                                    if (precio < 0 || precio > 500000) {
                                        System.out.println("Precio fuera de rango! vuelva a intentar un numero entre 0 y 500000");
                                    } else {
                                        break;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Entrada no valida. Por favor ingrese un número.");
                                    scanner.nextLine();
                                }
                            }
                            GestorPrecios.modificarPrecio(pestanias.getClass(), pestanias.getTipoPestanias(), precio);
                            System.out.println("CAMBIADO EXITOSAMENTE! El precio de DEPILACION se ha actualizado: ");
                            System.out.println("NUEVO PRECIO: " + GestorPrecios.obtenerPrecio(pestanias.getClass(), pestanias.getTipoService()));

                        } else {
                            System.out.println("¡Opción no válida! Por favor, intente de nuevo.");
                        }
                    } while (opc5 < 1 || opc5 > 3);

                    break;
                case 2:

                    double porcentaje;

                    while (true) {
                        try {
                            System.out.println("Ingrese el porcentaje a aumentar: ");
                            porcentaje = scanner.nextDouble();

                            if (porcentaje < 0 || porcentaje > 100) {
                                System.out.println("Porcentaje INVALIDO");
                            } else {
                                break;
                            }

                        } catch (Exception e) {
                            System.out.println("Entrada no válida. Por favor, ingrese un número.");
                            scanner.next();

                        }
                    }
                    GestorPrecios.aumentarTodosLosPrecios(porcentaje);
                    System.out.println("PRECIOS MODIFICADOS: ");
                    System.out.println(GestorPrecios.verPrecios());

                    break;
                case 3:

                    int opc = -1; // Variable para almacenar la opción seleccionada

                    do {
                        System.out.println("\n");
                        System.out.println("¿De que servicio desea incrementar los precios? ");
                        System.out.println("1. Depilacion");
                        System.out.println("2. Manicura");
                        System.out.println("3. Pestañas");
                        System.out.print("Seleccione una opción (1-3): ");
                        opc = scanner.nextInt();

                        double porcentaje2 = 0;

                        while (true) {
                            try {
                                System.out.println("Ingrese el porcentaje a aumentar: ");
                                porcentaje2 = scanner.nextDouble();

                                if (porcentaje2 < 0 || porcentaje2 > 100) {
                                    System.out.println("Porcentaje INVALIDO");
                                } else {
                                    break;
                                }

                            } catch (Exception e) {
                                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                                scanner.next();

                            }
                        }
                        if (opc == 1) {
                            System.out.println("-----MODIFICANDO-----");
                            GestorPrecios.aumentarPreciosPorClase(depilacion.getClass(), porcentaje2);
                            System.out.println(GestorPrecios.verPrecioDepi());
                        } else if (opc == 2) {
                            System.out.println("-----MODIFICANDO-----");
                            GestorPrecios.aumentarPreciosPorClase(manicura.getClass(), porcentaje2);
                            System.out.println(GestorPrecios.verPreciosManicura());
                        } else if (opc == 3) {
                            System.out.println("-----MODIFICANDO-----");
                            GestorPrecios.aumentarPreciosPorClase(pestania.getClass(), porcentaje2);
                            System.out.println(GestorPrecios.verPrecioPestanias());
                        } else {
                            System.out.println("¡Opción no válida! Por favor, intente de nuevo.");
                        }
                    } while (opc < 1 || opc > 3);
                    scanner.close();

                    break;
                case 4:
                    System.out.println(GestorPrecios.verPrecios());
                    break;

                default:
                    System.out.println("Opción no válida.");
                    break;
            }
            }catch (InputMismatchException a)
            {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
            }
        } while (opcion != 0);

    }

    public static String convertirFechaAString(LocalDate fecha) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Puedes ajustar el formato según necesites
        return fecha.format(formatoFecha);
    }

}
