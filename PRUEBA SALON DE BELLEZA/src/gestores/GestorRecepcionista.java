package gestores;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import excepciones.DNInoEncontradoException;
import excepciones.DNIyaCargadoException;
import excepciones.GeneroInvalidoException;
import excepciones.TelefonoInvalidoException;
import model.Recepcionista;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GestorRecepcionista {
    private static final Scanner scanner = new Scanner(System.in);
    private List<Recepcionista> almacenPersonas;
    private static final String archivoRecepcionista = "recepcionistas.json";


    ////////////////////////////////////////////////////////Setter, getter, constructor ///////////

    public GestorRecepcionista() {
        this.almacenPersonas = new ArrayList<>();
    }

    public List<Recepcionista> getAlmacenPersonas() {
        return almacenPersonas;
    }

    public void setAlmacenPersonas(List<Recepcionista> almacenPersonas) {
        this.almacenPersonas = almacenPersonas;
    }

    // ////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ///////////////////////////////////////////////////
    public boolean agregarPersona() {
        boolean cargado;

        String dni;
        while (true) {
            try {
                dni = pedirDNI();
                break;
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

        String contra2 = pedirContraseña();
        Recepcionista recepcionista = new Recepcionista(nombre, apellido, dni, genero, telefono, contra2);
        cargado = true;

        System.out.println(recepcionista);
        verificarCarga(recepcionista);
        almacenPersonas.add(recepcionista);

        mostrarTodos();
        return cargado;
    }

    public boolean eliminarPersona(String dni) {
        try {
            Recepcionista p = buscarPersona(dni);
            return almacenPersonas.remove(p);
        } catch (DNInoEncontradoException e) {
            System.out.printf(e.getMessage());
        }
        return false;
    }

    public Recepcionista buscarPersona(String dni) throws DNInoEncontradoException {
        for (Recepcionista p : almacenPersonas) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado!!");
    }

    public boolean buscarPersonas(String dni) throws DNInoEncontradoException {
        for (Recepcionista p : almacenPersonas) {
            if (p.getDni().equals(dni)) {
                return true;
            }
        }
        throw new DNInoEncontradoException("DNI no encontrado!!");
    }

    public String buscarContraseña(String dni) {
        for (Recepcionista p : almacenPersonas) {
            if (p.getDni().equals(dni)) {
                return p.getContraseña();
            }
        }
        return null;
    }

    public void modificarPersona(Recepcionista persona) {
        int opcion;
        boolean continuarModificando = true;

        while (continuarModificando) {
            System.out.println("\n");
            System.out.println("---------------------------------");
            System.out.println("¿Qué te gustaría modificar?");
            System.out.println("1. Nombre");
            System.out.println("2. Apellido");
            System.out.println("3. DNI");
            System.out.println("4. Genero");
            System.out.println("5. Telefono");
            System.out.println("6. Contraseña");
            System.out.println("0. Salir");
            System.out.println("---------------------------------");
            System.out.println("Ingrese una opción: ");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        persona.setNombre(pedirNombre());
                        break;
                    case 2:
                        persona.setApellido(pedirApellido());
                        break;
                    case 3:
                        try {
                            persona.setDni(pedirDNI());
                        } catch (DNIyaCargadoException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4:
                        try {
                            persona.setGenero(pedirGenero());
                        } catch (GeneroInvalidoException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 5:
                        try {
                            persona.setTelefono(pedirTelefono());
                        } catch (TelefonoInvalidoException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 6:
                        continuarModificando = false;
                        break;
                    case 7:
                        persona.setContraseña(pedirContraseñaNueva(persona.getContraseña()));
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor, ingresa un número válido.");
                scanner.nextLine(); // Limpiar el buffer
            }
        }
        System.out.println("¡MODIFICADO EXITOSAMENTE!");
        System.out.println(persona.toString());

    }

    public void mostrarTodos() {
        for (Recepcionista p : almacenPersonas) {
            System.out.println(p.toString());
        }
    }

    public void verificarCarga(Recepcionista recepcionista) {
        int opcion = -1;
        do {
            try {
                System.out.println("¿Deseas modificar algo de la persona?");
                System.out.println("1. Sí");
                System.out.println("2. No");

                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        modificarPersona(recepcionista);
                        break;
                    case 2:
                        System.out.println("....");
                        break;
                    default:
                        System.out.println("Opción no válida, selecciona nuevamente.");
                        break;
                }
            } catch (InputMismatchException d) {
                System.out.println("Error: Por favor, ingresa un número válido.");
                scanner.nextLine();
            }


        } while (opcion != 2 && opcion != 1);
    }

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
                for (Recepcionista a : almacenPersonas) {
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

    //contraseña entre 6 y 12 caracteres!!
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
            } else if (!nuevaContrasenia.matches("[A-Za-z0-9]+")) {
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
                    } else if (opcion == 1) {
                        pedirContraseña();

                    } else if (opcion != 1) {
                        System.out.println("Opción no válida. Intenta de nuevo.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Por favor, ingresa un número válido.");
                    scanner.nextLine();
                }

            }
        } while (true);

        return nuevaContrasenia;
    }

    public String pedirContraseña() {
        boolean confirmado = false;
        int opcion;
        String contraseña;

        while (true) {
            System.out.println("Ingresa una contraseña (entre 6 y 12 caracteres, debe contener al menos un número):");
            contraseña = scanner.nextLine();

            // Validación de longitud de la contraseña y de que contenga al menos un número
            if (contraseña.length() < 6 || contraseña.length() > 12) {
                System.out.println("Tu contraseña es muy débil o tiene un tamaño incorrecto. Vuelve a intentar.");
                // Vuelve al principio del ciclo si la contraseña no es válida
            } else if (!contraseña.matches("[A-Za-z0-9]+")) {  // Verifica que haya al menos un número
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
            System.out.println("Ingrese una opción:");
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

                    } else if (!contraseña.matches("[A-Za-z0-9]+")) {  // Verifica que haya al menos un número
                        System.out.println("Tu contraseña debe contener al menos un número. Vuelve a intentarlo.");

                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Por favor, ingresa un número válido.");
                scanner.nextLine();
            }

        } while (!confirmado);

        return contraseña;
    }


    ///////VERIFICACIONES

    public boolean verificarSiExisteRecepcionista(String dni) throws DNInoEncontradoException {

        if (almacenPersonas == null || almacenPersonas.isEmpty()) {
            throw new DNInoEncontradoException("\nNo hay registros de recepcionistas en el archivo especificado.");
        }
        for (Recepcionista p : almacenPersonas) {
            if (p.getDni().equals(dni)) {
                return true; // alguien del archivo tiene ese DNI
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado en recepcionistas del archivo especificado.");
    }

    /////////////////////////MANEJO DE ARCHIVOS DE PERSONAS.//////////////////////////
    public void escribirArchivoRecepcionistas() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(archivoRecepcionista), almacenPersonas);
        } catch (IOException e) {
            System.out.println("Error al guardar los datos de Recepcionistas");
        }
    }

    public void guardarEnJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            objectMapper.writeValue(new File(archivoRecepcionista), almacenPersonas);

        } catch (IOException e) {
            System.out.println("Error al guardar los datos Recepcionistas");
        }
    }

    // Leer desde JSON
    public void leerDesdeJson() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File file = new File(archivoRecepcionista);
            if (file.exists()) {
                // Asegurar que los datos deserializados sean asignados a una lista mutable
                Recepcionista[] recepcionistasArray = objectMapper.readValue(file, Recepcionista[].class);
                almacenPersonas = new ArrayList<>(List.of(recepcionistasArray)); // Convertir a ArrayList

            } else {
                System.out.println("El archivo no existe.");
            }
        } catch (IOException e) {
            System.out.println("Error al leer los datos almacenados de Recepcionistas");
        }
    }
}
