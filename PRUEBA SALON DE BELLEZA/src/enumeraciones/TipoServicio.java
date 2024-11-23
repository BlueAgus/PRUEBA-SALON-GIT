package enumeraciones;

public enum TipoServicio {
    MANICURA,
    PESTANIAS,
    DEPILACION;

    public String getNombre() {
        return name().toLowerCase(); // O cualquier otra lógica de formato
    }
}
