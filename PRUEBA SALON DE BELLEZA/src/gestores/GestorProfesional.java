package gestores;

import Interfaces.IBuscarPorCodigo;
import abstractas.Servicio;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoServicio;
import excepciones.*;
import model.Profesional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GestorProfesional {

    public List<Profesional> profesionales;
    Scanner scanner = new Scanner(System.in);
    Gson gson = new Gson();
    private static final String archivoProfesionales = "profesionales.json";
    GestorDepilacion gestorDepilacion;
    GestorPestania gestorPestania;
    GestorManicura gestorManicura;
    GestorTurno gestorTurno;


    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public GestorProfesional(GestorTurno gestorTurno) {
        this.profesionales = new ArrayList<>();
        this.gestorDepilacion = gestorTurno.gestorDepilacion;
        this.gestorPestania = gestorTurno.gestorPestania;
        this.gestorManicura = gestorTurno.gestorManicura;
        this.gestorTurno = gestorTurno;
    }

    //------------------------------------------------------------------------------------------------------------


    public boolean agregarPersona() {
        boolean cargado;

        String dni=null;
        while (dni==null) {
            try {
                dni = pedirDNI();
            } catch (DNIyaCargadoException e) {
                System.out.printf(e.getMessage());
            }
        }

        String nombre = pedirNombre();
        String apellido = pedirApellido();

        String genero;
        while (true) {
            try {
                genero = pedirGenero();
                break;
            } catch (GeneroInvalidoException e) {
                System.out.printf(e.getMessage());
            }
        }

        String telefono;
        while (true) {
            try {
                telefono = pedirTelefono();
                break;
            } catch (TelefonoInvalidoException e) {
                System.out.println(e.getMessage());
            }
        }

        String contra = pedirContraseña();
        Profesional profesional = new Profesional(nombre, apellido, dni, genero, telefono, contra);


        int opcion=0;

        System.out.println("Ingrese los servicios que realiza: ");
        do {

            TipoServicio tipoServicio1 = gestorDepilacion.pedirTipoServicio();

            System.out.println("SELECCIONE EL TIPO DE "+ tipoServicio1+ " QUE REALIZA");
            String e = gestorTurno.pedirCodServicio(tipoServicio1);
            profesional.agregarProfesion(e);//minimo una profesion.
            System.out.println("Deseas agregar otra profesion?");
            System.out.println("1. Si deseo.");
            System.out.println("2. No deseo.");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                if (opcion == 1) {
                    e = gestorManicura.pedirCodServicio();
                    profesional.agregarProfesion(e);
                } else if (opcion != 2) {
                    System.out.println("Ingresa una opcion valida por favor.");
                }
            }catch (InputMismatchException e1){
                System.out.println("Error: Por favor, ingresa un número válido.");
                scanner.nextLine();
            }



        } while (opcion != 2);

        cargado = true;
        if (profesionales.add(profesional)) {
            System.out.printf("\n PROFESIONAL AGREGADO EXITOSAMENTE \n");
        } else {
            System.out.printf("\nERROR AL AGREGAR PROFESIONAL\n");
        }
        System.out.println(profesional);
        verificarCarga(profesional);

        profesionales.add(profesional);

        return cargado;
    }
    //------------------------------------------------------------------------------------------------------------


    public String pedirNombre() {
        String nombre = "";
        boolean nombreValido = false;

        while (!nombreValido) {
            System.out.print("Ingrese el nombre: ");
            nombre = scanner.nextLine();

            // Validar que el nombre no esté vacío y que contenga solo letras y espacios
            if (nombre.isEmpty()) {
                System.out.println("Error: El nombre no puede estar vacío. Por favor ingresa un nombre válido.");
            } else if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+( [a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*")) {
                System.out.println("Error: El nombre solo puede contener letras y espacios entre palabras.");
            } else {
                // Si el nombre es válido, formatearlo con la primera letra en mayúscula
                nombre = PasarMayuscula(nombre);
                nombreValido = true;
            }
        }
        return nombre;
    }
    //------------------------------------------------------------------------------------------------------------

    public void verificarCarga(Profesional profesional) {
        int opcion=-1;
        do {
            try {
                System.out.println("¿Deseas modificar algo de la persona?");
                System.out.println("1. Sí");
                System.out.println("2. No");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        modificarProfesional(profesional);
                        break;
                    case 2:
                        System.out.println("....");
                        break;
                    default:
                        System.out.println("Opción no válida, selecciona nuevamente.");
                        break;
            }

            }catch (InputMismatchException e)
            {
                System.out.println("Error: Por favor, ingresa un número válido.");
                scanner.nextLine();
            }
        } while (opcion != 2 && opcion != 1);
    }
    //------------------------------------------------------------------------------------------------------------

    public void mostrarTodos() {
        System.out.println(profesionales);
    }


    //------------------------------------------------------------------------------------------------------------
    public boolean eliminarPersona(String dni) {
        try {
            Profesional p = buscarPersona(dni);
            return profesionales.remove(p);

        } catch (DNInoEncontradoException e) {
            System.out.printf(e.getMessage());
        }
        return false;
    }
    //------------------------------------------------------------------------------------------------------------

    public Profesional buscarPersona(String dni) throws DNInoEncontradoException {
        for (Profesional p : profesionales) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado!!");
    }
/*
// Asi se veria implementando la interfaz y generalizamos el CodigoNoEncontrado, que seria lo mismo que dni no encontrado
//-Agus
//Igual la interfaz extiende de servicio pero se podria sacar o hacer otro que extienda de Persona
//esto serviria porque si todas las personas usan una interfaz quiza facilitaria algunos metodos de busqueda o no se

     @Override
    public Profesional buscarPorCodigo(String dni) throws CodigoNoEncontradoException{
        for (Profesional p : profesionales) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }
        throw new CodigoNoEncontradoException("\nDNI no encontrado!!");
    }

*/

    //------------------------------------------------------------------------------------------------------------

    public boolean buscarPersonas(String dni) throws DNInoEncontradoException {
        for (Profesional p : profesionales) {
            if (p.getDni().equals(dni)) {
                return true;
            }
        }
        throw new DNInoEncontradoException("DNI no encontrado!!");
    }



    //------------------------------------------------------------------------------------------------------------

    public String pedirTelefono() {
        String telefono = "";
        boolean telefonoValido = false;

        // Limpia cualquier entrada residual del buffer antes de empezar
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (!telefonoValido) {
            try {
                System.out.print("Ingrese el teléfono: ");
                telefono = scanner.nextLine().trim();

                // Validar que el número tenga exactamente 10 dígitos y solo contenga números
                if (!telefono.matches("\\d{10}")) {
                    throw new TelefonoInvalidoException("El número de teléfono debe tener 10 dígitos y solo contener números.");
                } else {
                    // Si es válido, confirmamos y salimos del bucle
                    telefonoValido = true;
                }
            } catch (TelefonoInvalidoException e) {
                System.out.println(e.getMessage());
            }
        }
        return telefono;
    }

    //------------------------------------------------------------------------------------------------------------
    private String PasarMayuscula(String nombre) {
        String[] palabras = nombre.split(" "); // Separar las palabras por espacio
        StringBuilder nombreFormateado = new StringBuilder();

        for (String palabra : palabras) {
            // Poner la primera letra en mayúscula y las demás en minúscula
            if (!palabra.isEmpty()) {
                nombreFormateado.append(palabra.substring(0, 1).toUpperCase()) // Primera letra en mayúscula
                        .append(palabra.substring(1).toLowerCase()) // Resto de la palabra en minúscula
                        .append(" "); // Agregar espacio entre palabras
            }
        }
        // Eliminar el último espacio vacio
        return nombreFormateado.toString().trim();
    }

    //------------------------------------------------------------------------------------------------------------
    public String pedirApellido() {
        String apellido = "";
        boolean apellidoValido = false;

        while (!apellidoValido) {
            System.out.print("Ingrese el apellido: ");
            apellido = scanner.nextLine();

            // Validar que el apellido no esté vacío y que contenga solo letras y espacios
            if (apellido.isEmpty()) {
                System.out.println("Error: El apellido no puede estar vacío. Por favor ingresa un apellido válido.");
            } else if (!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+( [a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*")) {
                System.out.println("Error: El apellido solo puede contener letras y espacios entre palabras.");
            } else {
                // Si el apellido es válido, formatearlo con la primera letra en mayúscula
                apellido = PasarMayuscula(apellido);
                apellidoValido = true;
            }
        }
        return apellido;
    }

    //------------------------------------------------------------------------------------------------------------
    public String pedirDNI() throws DNIyaCargadoException {
        String dni = "";
        boolean dnivalido = false;

        while (!dnivalido) {
            System.out.println("Ingrese el DNI: ");
            dni = scanner.nextLine();

            // no esté vacío
            if (dni.isEmpty()) {
                System.out.println("Error: El DNI no puede estar vacío.");
            }
            //  contenga números
            else if (!dni.matches("\\d+")) {
                System.out.println("Error: El DNI solo puede contener números.");
            }
            //  dígitos
            else if (dni.length() != 8) {
                System.out.println("Error: El DNI debe tener exactamente 8 dígitos.");
            }
            // Verificar si el DNI ya está cargado en el sistema
            else {
                boolean dniRepetido = false;
                for (Profesional a : profesionales) {
                    if (a.getDni().equals(dni)) {
                        dniRepetido = true;
                        break;
                    }
                }
                if (dniRepetido) {
                    throw new DNIyaCargadoException("DNI ya cargado en el sistema: " + dni);
                } else {
                    dnivalido = true;
                }
            }
        }
        return dni;
    }

    //------------------------------------------------------------------------------------------------------------
    public String pedirDNIsinVerificacion() {
        String dni = "";
        boolean dnivalido = false;

        while (!dnivalido) {
            System.out.println("Ingrese el DNI: ");
            dni = scanner.nextLine();

            // no esté vacío
            if (dni.isEmpty()) {
                System.out.println("Error: El DNI no puede estar vacío.");
            }
            //  contenga números
            else if (!dni.matches("\\d+")) {
                System.out.println("Error: El DNI solo puede contener números.");
            }
            //  dígitos
            else if (dni.length() != 8) {
                System.out.println("Error: El DNI debe tener exactamente 8 dígitos.");
            } else {
                dnivalido = true;
            }
        }
        return dni;
    }

    //------------------------------------------------------------------------------------------------------------
    public String pedirContraseñaNueva(String contraseniaVieja) {
        String nuevaContrasenia;
        int opcion;

        do {
            System.out.println("La contraseña actual es: " + contraseniaVieja);
            nuevaContrasenia = pedirContraseña();

            // Validación: Contraseña no puede ser vacía y debe ser diferente
            if (nuevaContrasenia.equals(contraseniaVieja)) {
                System.out.println("Has ingresado la misma contraseña. Intenta de nuevo.");
            } else if (nuevaContrasenia.isEmpty()) {
                System.out.println("La contraseña no puede estar vacía. Intenta de nuevo.");
            } else if (!nuevaContrasenia.matches(".\\d.")) {//tiene al menos un num?
                System.out.println("La contraseña debe contener al menos un número. Intenta de nuevo.");
            } else if (nuevaContrasenia.length() < 6 || nuevaContrasenia.length() > 12) {
                System.out.println("La contraseña debe tener entre 6 y 12 caracteres. Intenta de nuevo.");
            } else {
                System.out.println("Has establecido la nueva contraseña: " + nuevaContrasenia);
                System.out.println("¿Deseas modificarla de nuevo?");
                System.out.println("1. Sí, deseo modificarla de nuevo.");
                System.out.println("2. No, estoy satisfecho.");

                try {
                    // Validar entrada del usuario
                    while (!scanner.hasNextInt()) {
                        System.out.println("Por favor, selecciona una opción válida (1 o 2):");
                        scanner.next();
                    }
                    opcion = scanner.nextInt();
                    scanner.nextLine();
                    if (opcion == 2) {
                        System.out.println("Contraseña definitiva establecida.");
                        break;
                    } else if (opcion != 1) {
                        System.out.println("Opción no válida. Intenta de nuevo.");
                    }
                }catch (InputMismatchException e)
                {
                    System.out.println("Error: Por favor, ingresa un número válido.");
                    scanner.nextLine();
                }

            }
        } while (true);

        return nuevaContrasenia;
    }

    public String pedirContraseña() {
        boolean confirmado = false;
        int opcion = -1;
        String contraseña;

        while (true) {
            System.out.println("Ingresa una contraseña (entre 6 y 12 caracteres, debe contener al menos un número):");
            contraseña = scanner.nextLine();

            // Validación de longitud de la contraseña y de que contenga al menos un número
            if (contraseña.length() < 6 || contraseña.length() > 12) {
                System.out.println("Tu contraseña es muy débil o tiene un tamaño incorrecto. Vuelve a intentar.");
                // Vuelve al principio del ciclo si la contraseña no es válida
            } else if (!contraseña.matches(".*\\d.*")) {  // Verifica que haya al menos un número
                System.out.println("Tu contraseña debe contener al menos un número. Vuelve a intentarlo.");

            } else {
                break;
            }
        }

        do {
            System.out.println("La contraseña ingresada es: " + contraseña);
            System.out.println("Deseas modificar la contraseña?");
            System.out.println("1. SI deseo");
            System.out.println("2. NO deseo");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();


                if (opcion == 2) {
                    System.out.println("Contraseña guardada");
                    confirmado = true;
                } else if (opcion != 1 && opcion != 2) {
                    System.out.println("No has ingresado una opción correcta");
                }
                if (opcion == 1) {
                    System.out.println("Ingresa una contraseña (entre 6 y 12 caracteres, debe contener al menos un número):");
                    contraseña = scanner.nextLine();

                    // Validación de longitud de la contraseña y de que contenga al menos un número
                    if (contraseña.length() < 6 || contraseña.length() > 12) {
                        System.out.println("Tu contraseña es muy débil o tiene un tamaño incorrecto. Vuelve a intentar.");
                        continue;  // Vuelve al principio del ciclo si la contraseña no es válida
                    } else if (!contraseña.matches(".*\\d.*")) {  // Verifica que haya al menos un número
                        System.out.println("Tu contraseña debe contener al menos un número. Vuelve a intentarlo.");
                        continue;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor, ingresa un número válido.");
                scanner.nextLine();
            }

        } while (!confirmado);

        return contraseña;
    }

    public String buscarContraseña(String dni) {
        for (Profesional p : profesionales) {
            if (p.getDni().equals(dni)) {
                return p.getContraseña();
            }
        }
        return null;
    }

    //------------------------------------------------------------------------------------------------------------
    public String pedirGenero() throws GeneroInvalidoException {
        String genero;
        while (true) {
            System.out.println("Ingrese el GÉNERO (M, F, O): ");
            genero = scanner.next().toUpperCase();  // Capturamos la entrada como String

            // Verificar que la entrada tiene exactamente un carácter
            if (genero.length() != 1) {
                throw new GeneroInvalidoException("Debes ingresar solo un carácter para el género.");
            }

            // Convertimos el String a un carácter para la validación
            char generoChar = genero.charAt(0);

            // Verificar si el carácter es válido
            if (generoChar != 'M' && generoChar != 'F' && generoChar != 'O') {
                throw new GeneroInvalidoException("GÉNERO INVÁLIDO\n");
            } else {
                break;
            }
        }
        return genero;  // Retornar el String que contiene el género válido
    }
    //------------------------------------------------------------------------------------------------------------

    public boolean verificarSiExisteProfesional(String dni) throws DNInoEncontradoException {

        if (profesionales == null || profesionales.isEmpty()) {
            throw new DNInoEncontradoException("\nNo hay registros de profesionales en el archivo especificado.");
        }
        for (Profesional p : profesionales) {
            if (p.getDni().equals(dni)) {
                return true; // alguien del archivo tiene ese DNI
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado en profesionales del archivo especificado.");
    }

    public void modificarProfesional(Profesional profesional) {
        int opcion;
        boolean continuarModificando = true;
        while (continuarModificando) {

            System.out.println("¿Qué te gustaría modificar del profesional?");
            System.out.println("1. Nombre");
            System.out.println("2. Apellido");
            System.out.println("3. DNI");
            System.out.println("4. Genero");
            System.out.println("5. Telefono");
            System.out.println("6. Servicios que ofrece");
            System.out.println("7. Modificar contraseña");
            System.out.println("8. Salir");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        profesional.setNombre(pedirNombre());
                        break;
                    case 2:
                        profesional.setApellido(pedirApellido());
                        break;
                    case 3:
                        try {
                            profesional.setDni(pedirDNI());
                        } catch (DNIyaCargadoException e) {
                            System.out.printf(e.getMessage());
                        }
                        break;
                    case 4:
                        try {
                            profesional.setGenero(pedirGenero());
                        } catch (GeneroInvalidoException e) {
                            System.out.printf(e.getMessage());
                        }
                        break;
                    case 5:
                        try {
                            profesional.setTelefono((pedirTelefono()));
                        } catch (TelefonoInvalidoException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 6:
                        TipoServicio tipoServicio1 = gestorDepilacion.pedirTipoServicio();
                        String e = gestorTurno.pedirCodServicio(tipoServicio1);
                        profesional.agregarProfesion(e);
                        break;
                    case 7:
                        profesional.setContraseña(pedirContraseñaNueva(profesional.getContraseña()));
                        break;
                    case 8:
                        continuarModificando = false;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor, ingresa un número válido.");
                scanner.nextLine(); // Limpiar el buffer
            }
            System.out.println("MODIFICADO EXITOSAMENTE!");
            System.out.println(profesional.toString());
        }
    }
    ///////////////////////////////////////////ARCHIVOS/////////////////////////////////////////////////////////////////

    public void escribirProfesionalesEnJson() {
        try (FileWriter fileWriter = new FileWriter(archivoProfesionales)) {
            gson.toJson(profesionales, fileWriter);
        } catch (IOException e) {
            System.out.println("Error al guardar los datos de profesionales: " + e.getMessage());
        }
    }

    public void leerProfesionalesDesdeJson() {
        try (FileReader fileReader = new FileReader(archivoProfesionales)) {
            profesionales = gson.fromJson(fileReader, new TypeToken<List<Profesional>>() {}.getType());
            if (profesionales == null) {
                profesionales = new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            System.out.println("El archivo de profesionales no existe, se creará uno nuevo al guardar.");
            profesionales = new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Error al leer los datos almacenados de Profesionales");
        }
    }



    //------------------------------------------------------------------------------------------------------------


    public List<Profesional> getProfesionales() {
        return profesionales;
    }

    public void setProfesionales(List<Profesional> profesionales) {
        this.profesionales = profesionales;
    }
}
