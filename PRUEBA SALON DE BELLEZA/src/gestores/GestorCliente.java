package gestores;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import excepciones.DNInoEncontradoException;
import excepciones.DNIyaCargadoException;
import excepciones.GeneroInvalidoException;
import excepciones.TelefonoInvalidoException;
import model.Cliente;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class GestorCliente {
    public List<Cliente> clientes;
    private static final Scanner scanner = new Scanner(System.in);
    Gson gson = new Gson();
    private static String archivoClientes= "clientes.json";

    public GestorCliente() {
        this.clientes= new ArrayList<>();
    }

    public List<Cliente> getClientes() { return clientes; }

    public void setClientes(List<Cliente> clientes) { this.clientes = clientes; }

    public boolean agregarPersona() {

        boolean cargado = false;

        String dni ;
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

        String genero ;
        while (true) {
            try {
                genero = pedirGenero();
                break;
            } catch (GeneroInvalidoException e) {
                System.out.printf(e.getMessage());
            }
        }

        String telefono ;
        while (true) {
            try {
                telefono = pedirTelefono();
                break;
            } catch (TelefonoInvalidoException e) {
                System.out.println(e.getMessage());
            }
        }
        Cliente cliente = new Cliente(nombre, apellido, dni, genero, telefono);

        verificarCarga(cliente);

        if (clientes.add(cliente)) {
            System.out.printf("\nCLIENTE AGREGADO EXITOSAMENTE \n");
        } else {
            System.out.printf("\nERROR AL AGREGAR CLIENTE\n");
        }
        System.out.println(cliente);


        return cargado;
    }


    public Cliente buscarPersona(String dni) throws DNInoEncontradoException {
        for (Cliente cliente : clientes) {
            if (cliente.getDni().equals(dni)) {
                return cliente;
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado!!");
    }

    public boolean buscarPersonas(String dni) throws DNInoEncontradoException {
        for (Cliente cliente : clientes) {
            if (cliente.getDni().equals(dni)) {
                return true;
            }
        }
        throw new DNInoEncontradoException("DNI no encontrado!!");
    }

    public void modificarPersona(Cliente cliente) {
        int opcion;
        boolean continuarModificando = true;

        while (continuarModificando) {
            System.out.println("¿Qué te gustaría modificar?");
            System.out.println("1. Nombre");
            System.out.println("2. Apellido");
            System.out.println("3. DNI");
            System.out.println("4. Genero");
            System.out.println("5. Telefono");
            System.out.println("0. Salir");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        cliente.setNombre(pedirNombre());
                        break;
                    case 2:
                        cliente.setApellido(pedirApellido());
                        break;
                    case 3:
                        try {
                            cliente.setDni(pedirDNI());
                        } catch (DNIyaCargadoException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 4:
                        try {
                            cliente.setGenero(pedirGenero());
                        } catch (GeneroInvalidoException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 5:
                        try {
                            cliente.setTelefono(pedirTelefono());
                        } catch (TelefonoInvalidoException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 0:
                        continuarModificando = false;
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
        System.out.println(cliente.toString());
    }

    public void mostrarTodos() {
        for (Cliente cliente : clientes) {
            System.out.println(cliente.toString());
        }
    }

    public void verificarCarga(Cliente cliente ) {
        int opcion = -1;
        do {
            System.out.println("¿Deseas modificar algo del cliente?");
            System.out.println("1. Sí");
            System.out.println("2. No");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        modificarPersona(cliente);
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

    public String pedirTelefono() throws TelefonoInvalidoException {
        String telefono = "";
        boolean telefonoValido = false;

        while (!telefonoValido) {
            System.out.print("Ingrese el teléfono: ");
            telefono = scanner.nextLine().trim();
            scanner.nextLine();

            // Validar que el número tenga exactamente 10 dígitos y solo contenga números
            if (!telefono.matches("\\d{10}")) {
                throw new TelefonoInvalidoException("El número de teléfono debe tener  10 dígitos y solo contener números.");
            } else {
                // Si es válido, confirmamos y salimos del bucle
                telefonoValido = true;
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
                for (Cliente cliente : clientes) {
                    if (cliente.getDni().equals(dni)) {
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
            }
            else {
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
    public String pedirContraseña() {
        boolean confirmado = false;
        int opcion;
        String contraseña ;

        do {
            System.out.println("Ingresa una contraseña (entre 6 y 12 caracteres, debe contener al menos un número):");
            contraseña = scanner.nextLine();
            scanner.nextLine();

            // Validación de longitud de la contraseña y de que contenga al menos un número
            if (contraseña.length() < 6 || contraseña.length() > 12) {
                System.out.println("Tu contraseña es muy débil o tiene un tamaño incorrecto. Vuelve a intentar.");
                continue;  // Vuelve al principio del ciclo si la contraseña no es válida
            } else if (!contraseña.matches(".\\d.")) {  // Verifica que haya al menos un número
                System.out.println("Tu contraseña debe contener al menos un número. Vuelve a intentarlo.");
                continue;
            }

            System.out.println("La contraseña ingresada es: " + contraseña);
            System.out.println("Deseas modificar la contraseña?");
            System.out.println("1. SI deseo");
            System.out.println("2. NO deseo");
            opcion = scanner.nextInt();
            scanner.nextLine();

            if (opcion == 2) {
                System.out.println("Contraseña guardada");
                confirmado = true;
            } else if (opcion != 1 && opcion != 2) {
                System.out.println("No has ingresado una opción correcta");
            }

        } while (!confirmado);

        return contraseña;
    }


    public boolean verificarSiExisteCliente(String dni) throws DNInoEncontradoException {

        if (clientes == null || clientes.isEmpty()) {
            throw new DNInoEncontradoException("\nNo hay registros de clientes en el archivo especificado.");
        }
        for (Cliente p : clientes) {
            if (p.getDni().equals(dni)) {
                return true; // alguien del archivo tiene ese DNI
            }
        }
        throw new DNInoEncontradoException("\nDNI no encontrado en clientes del archivo especificado.");
    }

    public boolean eliminarPersona(String dni) {
        try {
            Cliente cliente = buscarPersona(dni);
            return clientes.remove(cliente);

        } catch (DNInoEncontradoException e) {
            System.out.printf(e.getMessage());
        }
        return false;
    }


    public void escribirClientesEnJson() {
        try (FileWriter fileWriter = new FileWriter(archivoClientes)) {
            // Convertir la lista de clientes a JSON y guardarlo en el archivo
            gson.toJson(clientes, fileWriter);
        } catch (IOException e) {
            System.out.println("No se puede guardar el archivo de clientes: " + e.getMessage());
        }
    }

    public void leerArchivoClientes() {
        try (FileReader fileReader = new FileReader(archivoClientes)) {
            // Deserializar el archivo JSON a una lista de objetos Cliente
            clientes = gson.fromJson(fileReader, new TypeToken<List<Cliente>>(){}.getType());
            System.out.println("Archivo de clientes leído exitosamente.");
        } catch (IOException e) {
            System.out.println("No se puede leer el archivo de clientes: " + e.getMessage());
        }
    }
/*
    public List<Cliente> leerArchivoClientes1() {

        List<Cliente> listaClientes = null;

        try (FileReader fileReader = new FileReader(archivoClientes)) {
            Type listType = new TypeToken<List<Cliente>>() {}.getType();
            listaClientes = gson.fromJson(fileReader, listType); // Leer y convertir desde JSON
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de clientes no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de clientes: " + e.getMessage());
        }

        return listaClientes;
    }

    public List<Cliente> leerArchivoClientes() {

        try (FileReader reader = new FileReader(archivoClientes)) {

            Cliente[] clientesArray = gson.fromJson(reader, Cliente[].class);
            List<Cliente> clientesCargados = Arrays.asList(clientesArray);

            clientes.addAll(clientesCargados);

            return clientesCargados;
        } catch (IOException e) {
            System.out.println("Error al cargar los archivos" + e.getMessage());
        }
        return null;
    }*/
}
