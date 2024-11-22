package excepciones;

public class TurnoNoExistenteException extends RuntimeException {
    public TurnoNoExistenteException(String message) {
        super(message);
    }
}
