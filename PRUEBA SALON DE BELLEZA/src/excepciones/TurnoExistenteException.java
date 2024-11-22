package excepciones;

public class TurnoExistenteException extends RuntimeException {
    public TurnoExistenteException(String message) {
        super(message);
    }
}
