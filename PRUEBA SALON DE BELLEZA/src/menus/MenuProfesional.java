package menus;

import gestores.*;
import model.Turno;

import java.nio.channels.ScatteringByteChannel;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuProfesional {

    public void menuProfesional(GestorCliente cliente, GestorTurno turnos, String dniProfesional, GestorDepilacion depilacion, GestorManicura manicura, GestorPestania pestania) {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;
        do {
            try {
                System.out.println("\n");
                System.out.println("---------------------------------");
                System.out.println("Bienvenido al menu de Profesional ");
                System.out.println("1.Ver historial de turnos ");
                System.out.println("2.Ver turnos próximos");
                System.out.println("3.Reportar falla de servicio");
                System.out.println("0. Volver al Menú anterior");
                System.out.println("---------------------------------");
                System.out.print("Ingrese una opción: ");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {

                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    case 1:
                        List<Turno> turnosProximos = turnos.buscarTurnosXdniProfesionalVigentes(dniProfesional);

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
                        List<Turno> historialTurnos = turnos.historialTurnosXprofesional(dniProfesional);

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
                    case 3:

                        int opc5 = -1;
                        do {
                            try {
                                System.out.println("\n");
                                System.out.println("---------------------------------");
                                System.out.println("¿En qué servicio desea reportar falla?");
                                System.out.println("1. Depilación");
                                System.out.println("2. Manicura");
                                System.out.println("3. Pestañas");
                                System.out.println("---------------------------------");
                                System.out.print("Seleccione una opción (1-3): ");
                                opc5 = scanner.nextInt();
                                scanner.nextLine();

                                if (opc5 == 1) {
                                    depilacion.reportarFalla(turnos);
                                } else if (opc5 == 2) {
                                    manicura.reportarFalla(turnos);
                                } else if (opc5 == 3) {
                                    pestania.reportarFalla(turnos);
                                } else {
                                    System.out.println("\n¡Opción no válida! Por favor, intente de nuevo.");
                                }
                            } catch (InputMismatchException a) {
                                System.out.println("Caracter invalido..Ingrese un numero por favor!");
                                scanner.nextLine();
                            }

                        } while (opc5 < 1 || opc5 > 3);

                        break;
                    default:
                        System.out.println("\nOpción no válida.");
                }
            } catch (InputMismatchException a) {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
                scanner.nextLine();
            }
        } while (opcion != 0);
    }
}