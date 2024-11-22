package excepciones;

public class FacturaYaExistenteException extends RuntimeException {
    public FacturaYaExistenteException(String message) {
        super(message);
    }
}
