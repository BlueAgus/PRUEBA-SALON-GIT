package enumeraciones;

public enum TipoDepilacion {
    CERA,
    LASER;

    public String getNombre() {
        return name().toLowerCase(); // O cualquier otra lógica de formato
    }
}
