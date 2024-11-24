package menus;

import gestores.*;

import java.nio.channels.ScatteringByteChannel;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuProfesional {


    public void menuProfesional(GestorCliente cliente, GestorTurno turnos, String dniProfesional, GestorDepilacion depilacion, GestorManicura manicura, GestorPestania pestania)
    {
        Scanner scanner = new Scanner(System.in);
        int opcion=-1;
        do {
            try {
                System.out.println("\n");
                System.out.println("--------------------");
                System.out.println("Bienvenido al menu de Profesional ");
                System.out.println("1.Ver historial de turnos ");
                System.out.println("2.Ver turnos próximos");
                System.out.println("3.Reportar falla de servicio");
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
                        turnos.historialTurnosXprofesional(dniProfesional);
                        break;
                    case 2:
                        turnos.buscarTurnosXdniProfesionalVigentes(dniProfesional);
                        break;
                    case 3:

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

                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException a)
            {
                System.out.println("Caracter invalido..Ingrese un numero por favor!");
            }
        } while (opcion != 0);
    }
}