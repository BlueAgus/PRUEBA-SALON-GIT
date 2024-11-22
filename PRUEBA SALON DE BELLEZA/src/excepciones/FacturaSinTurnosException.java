package excepciones;

public class FacturaSinTurnosException extends RuntimeException {
    public FacturaSinTurnosException(String message) {
        super(message);
    }
}
