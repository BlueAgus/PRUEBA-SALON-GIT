package gestores;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoManicura;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;
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

    private static double precioDisenio = 0;
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

        precios.get(Depilacion.class).put(TipoDepilacion.CERA, 0.0);
        precios.get(Depilacion.class).put(TipoDepilacion.LASER, 0.0);

        precios.get(Manicura.class).put(TipoManicura.GEL, 0.0);
        precios.get(Manicura.class).put(TipoManicura.ESCULPIDAS, 0.0);
        precios.get(Manicura.class).put(TipoManicura.SEMIPERMANENTE, 0.0);

        precios.get(Pestanias.class).put(TipoPestanias.TRES_D, 0.0);
        precios.get(Pestanias.class).put(TipoPestanias.DOS_D, 0.0);
        precios.get(Pestanias.class).put(TipoPestanias.CLASICAS, 0.0);
    }


    public static double obtenerPrecio(Class<?> claseServicio, Enum<?> tipo) {
        Map<Enum<?>, Double> mapaPrecios = precios.get(claseServicio);

        if (mapaPrecios == null || !mapaPrecios.containsKey(tipo)) {
            throw new IllegalArgumentException("No se encontró un precio para el tipo " + tipo + " en la clase " + claseServicio.getName());
        }

        return mapaPrecios.get(tipo); // Ahora sabemos que el valor no será null
    }

    public static void modificarPrecio(Class<?> claseServicio, Enum<?> tipo, double nuevoPrecio) {

        Map<Enum<?>, Double> mapaPrecios = precios.get(claseServicio);
        if (mapaPrecios == null) {
            throw new IllegalArgumentException("No se encontró un mapa de precios para la clase: " + claseServicio.getName());
        }
        if (!mapaPrecios.containsKey(tipo)) {
            throw new IllegalArgumentException("El tipo " + tipo + " no pertenece a la clase " + claseServicio.getName());
        }
        mapaPrecios.put(tipo, nuevoPrecio);
    }

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

        precios.forEach((claseIndice, mapaPreciosIndice) -> {
            mapaPreciosIndice.replaceAll((tipoIndice, precioIndice) -> precioIndice * aumento);
        });
    }

    public static void aumentarPreciosPorClase(Class<?> servicio, double porcentaje) {
        if (porcentaje < 0) {
            throw new ArithmeticException("El porcentaje de aumento no puede ser negativo.");
        }
        double aumento = 1 + (porcentaje / 100);

        Map<Enum<?>, Double> mapaPrecios = precios.get(servicio);
        if (mapaPrecios != null) {
            // el metodo replaceAll reemplaza todos los valores de una
            mapaPrecios.replaceAll((tipoIndice, precioIndice) -> precioIndice * aumento);
        } else {
            throw new IllegalArgumentException("No se encontraron precios para la clase: " + servicio.getSimpleName());
        }
    }

    public static void aplicarDescuento(String codigoFactura, double porcentajeDescuento, List<Factura> facturas) throws CodigoNoEncontradoException {
        if (porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100.");
        }
        Factura facturaEncontrada = null;

        for (Factura factura : facturas) {
            if (factura.getCodigoFactura().equals(codigoFactura)) {
                facturaEncontrada = factura;
                break;
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

        System.out.println("Descuento del " + porcentajeDescuento + "% aplicado. Descuento: " + descuento + ". Nuevo precio final: " + nuevoPrecioFinal);

    }

    private static Map<String, Map<String, Double>> convertirMapaParaJSON() {
        Map<String, Map<String, Double>> mapaJSON = new HashMap<>();

        for (Map.Entry<Class<?>, Map<Enum<?>, Double>> entrada : precios.entrySet()) {
            String nombreClase = entrada.getKey().getSimpleName();
            Map<String, Double> preciosPorTipo = new HashMap<>();

            for (Map.Entry<Enum<?>, Double> subEntrada : entrada.getValue().entrySet()) {
                preciosPorTipo.put(subEntrada.getKey().name(), subEntrada.getValue());
            }

            mapaJSON.put(nombreClase, preciosPorTipo);
        }

        return mapaJSON;
    }

    public static void guardarPreciosEnArchivo() {
        try (FileWriter writer = new FileWriter("precios.json")) {
            Map<String, Map<String, Double>> mapaParaJSON = convertirMapaParaJSON();

            if (precioDisenio != 0.0) {
                mapaParaJSON.put("PrecioDiseño", Map.of("Diseño", precioDisenio));
            }

            String json = gson.toJson(mapaParaJSON);
            writer.write(json);
            System.out.println("Precios guardados exitosamente en precios.json");
        } catch (IOException e) {
            System.err.println("Error al guardar precios en el archivo: " + e.getMessage());
        }
    }


    public static void cargarPreciosDesdeArchivo() {
        try (FileReader reader = new FileReader(archivoPrecios)) {
            Type tipoMapaJSON = new TypeToken<Map<String, Map<String, Double>>>() {}.getType();
            Map<String, Map<String, Double>> mapaJSON = gson.fromJson(reader, tipoMapaJSON);

            if (mapaJSON == null) {
                precios = new HashMap<>();
                return;
            }

            for (Map.Entry<String, Map<String, Double>> entrada : mapaJSON.entrySet()) {
                String tipoServicio = entrada.getKey();
                Map<String, Double> mapaInterno = entrada.getValue();

                if (tipoServicio.equals("PrecioDiseño")) {
                    precioDisenio = mapaInterno.getOrDefault("Diseño", 0.0);
                    continue;
                }

                try {
                    TipoServicio servicioEnum = TipoServicio.valueOf(tipoServicio.toUpperCase());
                    Map<Enum<?>, Double> mapaConvertido = new HashMap<>();
                    for (Map.Entry<String, Double> subEntrada : mapaInterno.entrySet()) {
                        mapaConvertido.put(servicioEnum, subEntrada.getValue());
                    }
                    precios.put(servicioEnum.getClass(), mapaConvertido);
                } catch (IllegalArgumentException e) {
                    System.err.println("El tipo de servicio " + tipoServicio + " no corresponde a un valor de TipoServicio.");
                }
            }

            System.out.println("Precios cargados correctamente desde el archivo JSON.");
        } catch (FileNotFoundException e) {
            System.out.println("Archivo JSON no encontrado. Se creará uno nuevo al guardar.");
            precios = new HashMap<>();
        } catch (IOException e) {
            System.err.println("Error al leer los precios: " + e.getMessage());
        }
    }

    public static String verPrecios() {
        StringBuilder sb = new StringBuilder();

        sb.append("Precios de los servicios:");
        sb.append("\n--------------------------\n");
        for (Map.Entry<Class<?>, Map<Enum<?>, Double>> entrada : precios.entrySet()) {
            Class<?> clase = entrada.getKey();
            Map<Enum<?>, Double> mapaInterno = entrada.getValue();

            for (Map.Entry<Enum<?>, Double> subEntrada : mapaInterno.entrySet()) {
                String tipoServicio = subEntrada.getKey().toString().replace("_", " ").toLowerCase();
                double precio = subEntrada.getValue();
                sb.append(clase.getSimpleName()).append(" ").append(tipoServicio).append(": ").append(precio).append("\n");
            }
        }
        sb.append("Precio Diseño: ").append(precioDisenio);
        sb.append("\n--------------------------\n");
        return sb.toString();
    }

    public static String verPrecioDepi() {
        StringBuilder sb = new StringBuilder();
        Map<Enum<?>, Double> mapaDepi = precios.get(Depilacion.class);

        sb.append("Precios de los servicios de depilacion:");
        sb.append("\n--------------------------\n");
        if (mapaDepi != null) {
            for (Map.Entry<Enum<?>, Double> entrada : mapaDepi.entrySet()) {
                sb.append("Depilación ").append(entrada.getKey().toString().toLowerCase())
                        .append(": ").append(entrada.getValue()).append("\n");
            }
        }
        sb.append("\n--------------------------\n");
        return sb.toString();
    }



    public static String verPrecioPestanias() {
        StringBuilder sb = new StringBuilder();
        Map<Enum<?>, Double> mapaPestanias = precios.get(Pestanias.class);

        sb.append("Precios de los servicios de pestañas:");
        sb.append("\n--------------------------\n");
        if (mapaPestanias != null) {
            for (Map.Entry<Enum<?>, Double> entrada : mapaPestanias.entrySet()) {
                sb.append("Pestañas ").append(entrada.getKey().toString().replace("_", " ").toLowerCase())
                        .append(": ").append(entrada.getValue()).append("\n");
            }
        }
        sb.append("\n--------------------------\n");
        return sb.toString();
    }

    public static String verPreciosManicura() {
        StringBuilder sb = new StringBuilder();
        Map<Enum<?>, Double> mapaManicura = precios.get(Manicura.class);

        sb.append("Precios de los servicios de manicura :");
        sb.append("\n--------------------------\n");
        if (mapaManicura != null) {
            for (Map.Entry<Enum<?>, Double> entrada : mapaManicura.entrySet()) {
                sb.append("Manicura con ").append(entrada.getKey().toString().toLowerCase())
                        .append(": ").append(entrada.getValue()).append("\n");
            }
        }
        sb.append("Precio Diseño: ").append(precioDisenio);
        sb.append("\n--------------------------\n");
        return sb.toString();
    }

}
/*
    public static void modificarPrecio(Class<?> claseServicio, Enum<?> tipo, double nuevoPrecio) {
        // Asegurar que exista un mapa para la clase
        precios.putIfAbsent(claseServicio, new HashMap<>());

        Map<Enum<?>, Double> mapaPrecios = precios.get(claseServicio);

        // Si el tipo no está presente, inicialízalo (si es válido hacerlo)
        if (!mapaPrecios.containsKey(tipo)) {
            System.out.println("El tipo " + tipo + " no estaba registrado. Inicializándolo con el nuevo precio.");
        }

        // Actualizar o añadir el precio
        mapaPrecios.put(tipo, nuevoPrecio);
        System.out.println("Precio actualizado: " + nuevoPrecio + " para tipo " + tipo + " en " + claseServicio.getSimpleName());
    }
*/



}
