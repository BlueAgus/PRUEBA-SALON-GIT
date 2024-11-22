package excepciones;

public class FacturaNoExistenteException extends RuntimeException {
    public FacturaNoExistenteException(String message) {
        super(message);
    }
}
