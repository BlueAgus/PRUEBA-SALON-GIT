package gestores;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoManicura;
import enumeraciones.TipoPestanias;
import excepciones.CodigoNoEncontradoException;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GestorPrecios {

    private static final double precioDepiCera = 12000.0;
    private static final double precioDepiLaser = 18000.0;

    private static final double precioManiGel = 13000.0;
    private static final double precioManiEsculpidas = 20000.0;
    private static final double precioManiSemi = 10000.0;
    private static double precioDisenio = 2000.0;

    private static final double precioPestanias3D = 20000.0;
    private static final double precioPestanias2D = 16000.0;
    private static final double precioPestaniasClasic = 14000.0;
    private static String archivoPrecios = "precios.json";

    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Class.class, new TypeAdapter<Class<?>>() {
                @Override
                public void write(JsonWriter out, Class<?> value) throws IOException {
                    out.value(value.getSimpleName()); // Guarda solo el nombre simple de la clase
                }
                @Override
                public Class<?> read(JsonReader in) throws IOException {
                    String className = in.nextString();
                    try {
                        // Ajusta el paquete aquí según tu proyecto, si las clases están en el paquete "model"
                        return Class.forName("model." + className);
                    } catch (ClassNotFoundException e) {
                        throw new IOException("Clase no encontrada: " + e.getMessage(), e);
                    }
                }
            })
            .setPrettyPrinting()
            .create();


    // mapa anidado
    private static Map<Class<?>, Map<Enum<?>, Double>> precios = new HashMap<>();

    static {
        // inicializa los mapas de cada clase vacios
        precios.put(Depilacion.class, new HashMap<>());
        precios.put(Manicura.class, new HashMap<>());
        precios.put(Pestanias.class, new HashMap<>());
        //clave   vaaaaloooooor
        // aca le asignamos su otro mapa anidado Mapa(  clase, (enum, precio) );
        //depilacion                                           clave, valor
        precios.get(Depilacion.class).put(TipoDepilacion.CERA, precioDepiCera);
        precios.get(Depilacion.class).put(TipoDepilacion.LASER, precioDepiLaser);

        // manicura
        precios.get(Manicura.class).put(TipoManicura.GEL, precioManiGel);
        precios.get(Manicura.class).put(TipoManicura.ESCULPIDAS, precioManiEsculpidas);
        precios.get(Manicura.class).put(TipoManicura.SEMIPERMANENTE, precioManiSemi);

        //
        precios.get(Pestanias.class).put(TipoPestanias.TRES_D, precioPestanias3D);
        precios.get(Pestanias.class).put(TipoPestanias.DOS_D, precioPestanias2D);
        precios.get(Pestanias.class).put(TipoPestanias.CLASICAS, precioPestaniasClasic);
    }


    public static double obtenerPrecio(Class<?> claseServicio, Enum<?> tipo) {
        return precios.get(claseServicio).get(tipo);
        //sacamos primero el valor de la clave "clase" que el mapa anidado, y luego sacamos el valor del segundo mapa que es el precio
    }

    public static void modificarPrecio(Class<?> claseServicio, Enum<?> tipo, double nuevoPrecio) {
        //aca obtenemos una referencia, no una copia
        Map<Enum<?>, Double> mapaPrecios = precios.get(claseServicio);
        if (mapaPrecios == null) {
            throw new IllegalArgumentException("No se encontró un mapa de precios para la clase: " + claseServicio.getName());
        }
        if (!mapaPrecios.containsKey(tipo)) {
            throw new IllegalArgumentException("El tipo " + tipo + " no pertenece a la clase " + claseServicio.getName());
        }
        mapaPrecios.put(tipo, nuevoPrecio);
    }

    // solo va a usarse para  manicura
    public static double agregarDisenio(Enum<?> tipo) {
        if (!(tipo instanceof TipoManicura)) {
            throw new IllegalArgumentException("El diseño solo aplica a servicios de manicura.");
        }
        double precioBase = obtenerPrecio(Manicura.class, tipo);
        return precioBase + precioDisenio;
    }

    public static double getPrecioDisenio() {
        return precioDisenio;
    }


    public static void setPrecioDisenio(double nuevoPrecioDisenio) {
        if (nuevoPrecioDisenio < 0) {
            throw new ArithmeticException("El precio de diseño no puede ser negativo.");
        }
        precioDisenio = nuevoPrecioDisenio;
    }

    public static void aumentarTodosLosPrecios(double porcentaje) {

        double aumento = 1 + (porcentaje / 100);

        // se usa este en vez de un for porque no es facil de usar con un mapa anidado y no queda lindo

        //               clave,        valor(o sea el mapa anidado)
        precios.forEach((claseIndice, mapaPreciosIndice) -> {//itera sobre todos los pares clave-valor de precios
            // y aca iteramos sobre el mapa aniado que contiene el enum y el precio para actualizar
            mapaPreciosIndice.replaceAll((tipoIndice, precioIndice) -> precioIndice * aumento);
            //este metodo actualiza los valores del map, toma los valores actuales y modifica
        });
    }

    public static void aumentarPreciosPorClase(Class<?> servicio, double porcentaje) {
        if (porcentaje < 0) {
            throw new ArithmeticException("El porcentaje de aumento no puede ser negativo.");
        }
        double aumento = 1 + (porcentaje / 100);

        // Verificamos si la clase existe en el mapa de precios
        Map<Enum<?>, Double> mapaPrecios = precios.get(servicio); // obtenemos el valor de precios (la clase anidada)
        if (mapaPrecios != null) {
            // el metodo replaceAll reemplaza todos los valores de una
            mapaPrecios.replaceAll((tipoIndice, precioIndice) -> precioIndice * aumento);
        } else {
            throw new IllegalArgumentException("No se encontraron precios para la clase: " + servicio.getSimpleName());
        }
    }

    public static void aplicarDescuento(String codigoFactura, double porcentajeDescuento, List<Factura> facturas) throws CodigoNoEncontradoException {
        // Validar el porcentaje de descuento
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100.");
        }
        Factura facturaEncontrada = null;

        for (Factura factura : facturas) {
            if (factura.getCodigoFactura().equals(codigoFactura)) {
                facturaEncontrada = factura;
                break; // Salimos del bucle al encontrar la factura
            }
        }
        if (facturaEncontrada == null) {
            throw new CodigoNoEncontradoException("Factura con código " + codigoFactura + " no encontrada.");
        }

        double precioOriginal = facturaEncontrada.getPrecioFinal();
        double descuento = precioOriginal * (porcentajeDescuento / 100);

        double nuevoPrecioFinal = precioOriginal - descuento;
        facturaEncontrada.setPrecioFinal(nuevoPrecioFinal);
        facturaEncontrada.setDescuento(descuento);

        // Mostrar mensaje informativo (opcional)
        System.out.println("Descuento del " + porcentajeDescuento + "% aplicado. Descuento: " + descuento + ". Nuevo precio final: " + nuevoPrecioFinal);

        // Retornar el valor del descuento aplicado
    }

    private static Map<String, Map<String, Double>> convertirMapaParaJSON() {
        Map<String, Map<String, Double>> mapaJSON = new HashMap<>();

        for (Map.Entry<Class<?>, Map<Enum<?>, Double>> entrada : precios.entrySet()) {
            String nombreClase = entrada.getKey().getSimpleName(); // Convierte la clase a su nombre simple
            Map<String, Double> preciosPorTipo = new HashMap<>();

            for (Map.Entry<Enum<?>, Double> subEntrada : entrada.getValue().entrySet()) {
                preciosPorTipo.put(subEntrada.getKey().name(), subEntrada.getValue()); // Usa el nombre del enum
            }

            mapaJSON.put(nombreClase, preciosPorTipo);
        }

        return mapaJSON;
    }

    public static void guardarPreciosEnArchivo() {
        try (FileWriter writer = new FileWriter("precios.json")) {
            Map<String, Map<String, Double>> mapaParaJSON = convertirMapaParaJSON();
            String json = gson.toJson(mapaParaJSON); // Serializa el mapa convertido
            writer.write(json);
            System.out.println("Precios guardados exitosamente en precios.json");
        } catch (IOException e) {
            System.err.println("Error al guardar precios en el archivo: " + e.getMessage());
        }
    }

/*
    public static void escribirPreciosEnEnJson() {
        try (FileWriter writer = new FileWriter(archivoPrecios)) {
            gson.toJson(precios, writer);
            writer.close();
            System.out.println("Historial de precio cargados con exito!");

        } catch (IOException e) {
            System.out.println("No se puede guardar el archivo de precios: " + e.getMessage());
        }
    }*/
/*
    public static void guardarPreciosEnArchivo() {
        try (FileWriter writer = new FileWriter(archivoPrecios)) {
            String json = gson.toJson(precios); // Usa el Gson con el adaptador
            writer.write(json); // Escribe el JSON en el archivo
            System.out.println("Precios guardados exitosamente en el archivo: " + archivoPrecios);
        } catch (IOException e) {
            System.err.println("Error al guardar precios en el archivo: " + e.getMessage());
        }
    }*/


    public static void cargarPreciosDesdeArchivo() {
        try (FileReader reader = new FileReader(archivoPrecios)) {
            // Leer y deserializar el archivo JSON al mapa de precios
            Type tipoMapa = new TypeToken<Map<Class<?>, Map<Enum<?>, Double>>>() {}.getType();
            precios = gson.fromJson(reader, tipoMapa);

            if (precios == null) {
                precios = new HashMap<>(); // Si no hay datos, inicializa el mapa vacío
            }

            System.out.println("Precios cargados correctamente desde el archivo JSON.");
        } catch (FileNotFoundException e) {
            System.out.println("Archivo JSON no encontrado. Se creará uno nuevo al guardar.");
            precios = new HashMap<>(); // Inicializa el mapa si el archivo no existe
        } catch (IOException e) {
            System.out.println("Error al leer los precios: " + e.getMessage());
        }
    }



    /*  public static void leerArchivoPrecios() {
        try (FileReader reader = new FileReader(archivoPrecios)) {
            Type tipoMapa = new TypeToken<Map<Class<?>, Map<Enum<?>, Double>>>() {
            }.getType();
                precios = gson.fromJson(reader, tipoMapa);
            System.out.println("Archivo de precios leído exitosamente.");

        } catch (IOException e) {
            System.out.println("No se puede leer el archivo de clientes: " + e.getMessage());
        }
    }
*/


    public static String verPrecios() {
        return
                "Depilacion con cera: " + precioDepiCera +"\n"+
                        "Depilacion con Laser: " + precioDepiLaser +"\n"+
                        "Manicura con gel : " + precioManiGel +"\n"+
                        "Manicura con esculpidas: " + precioManiEsculpidas +"\n"+
                        "Manicura semipermanentes: " + precioManiSemi +"\n"+
                        "Precio extra diseño : " + precioDisenio +"\n"+
                        "Pestañas 2D: " + precioPestanias2D +"\n"+
                        "Pestañas 3d: " + precioPestanias3D +"\n"+
                        "Pestañas clasicas: " + precioPestaniasClasic;
    }

    public static String verPrecioDepi() {
        return
                "Depilacion con cera: " + precioDepiCera +"\n"+
                        "Depilacion con Laser: " + precioDepiLaser;

    }

    public static String verPrecioPestanias() {
        return
                "Pestañas 2D: " + precioPestanias2D +"\n"+
                        "Pestañas 3d: " + precioPestanias3D +"\n"+
                        "Pestañas clasicas: " + precioPestaniasClasic;
    }

    public static String verPreciosManicura() {
        return "Manicura con gel : " + precioManiGel + "\n"+
                "Manicura con esculpidas: " + precioManiEsculpidas +"\n"+
                "Manicura semipermanentes: " + precioManiSemi +"\n"+
                "Precio extra diseño : " + precioDisenio;

    }
}
