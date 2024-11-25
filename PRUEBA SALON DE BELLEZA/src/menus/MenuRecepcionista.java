package menus;

import excepciones.CodigoNoEncontradoException;
import gestores.*;
import model.Profesional;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Stream;

public class MenuRecepcionista extends MenuAdministrador {

    public void menuRecepcionistas(GestorCliente clientes, GestorProfesional profesionales, GestorRecepcionista recepcionista, GestorAdministrador administrador, GestorDepilacion depilacion,GestorManicura manicura,GestorPestania pestania, GestorTurno turnos, GestorFactura facturas) {

        Scanner scanner = new Scanner(System.in);
        int opcion=-1;

        do {
            try {
                System.out.println("\n");
                System.out.println("--------------------");
                System.out.println("Bienvenido al menu de recepcionista ");
                System.out.println("1.Gestion de clientes");
                System.out.println("2.Gestion de turnos");
                System.out.println("3.Gestion de facturas");
                System.out.println("4.Mostrar nuestros servicios");
                System.out.println("5.Ver profesionales en el salón");
                System.out.println("0. Volver al Menú anterior");
                System.out.println("--------------------");
                System.out.print("Ingrese una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    case 1:
                        subMenuClientes(clientes, pestania, depilacion, manicura);
                        break;
                    case 2:
                        menuTurnos(turnos, clientes, profesionales);
                        break;
                    case 3:
                        menuFacturas(facturas, clientes, turnos);
                        break;
                    case 4:
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
                    case 5:
                        mostrarProfesionales(profesionales);
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            }catch (InputMismatchException a)
            {
                System.out.println("Caracter invalido..Ingrese un numero por favor");
                scanner.nextLine();
            }
        } while (opcion != 0);

    }

    public void mostrarProfesionales(GestorProfesional profesionales) {

        Scanner scanner = new Scanner(System.in);
        int opcion=-1;

        do {
            try {
                System.out.println("\n");
                System.out.println("1.Mostrar profesionales del salón ");
                System.out.println("2.Mostrar profesionales por servicio específico ");
                System.out.println("0. Volver al Menú anterior");
                System.out.print("Ingrese una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    case 1:
                        profesionales.mostrarTodos();
                        break;
                    case 2:
                    /*
                    Servicio servicio = null;
                    while (true) {
                        try {
                            servicio = servicios.buscarServicio();
                        } catch (CodigoNoEncontradoException e) {
                            System.out.println(e.getMessage());
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
            }catch (InputMismatchException a)
            {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
            }
        } while (opcion != 0);

    }
}
