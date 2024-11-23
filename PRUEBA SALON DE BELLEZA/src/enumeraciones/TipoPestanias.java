package enumeraciones;

public enum TipoPestanias {
    TRES_D,
    DOS_D,
    CLASICAS;

    public String getNombre() {
        return name().toLowerCase(); // O cualquier otra lógica de formato
    }
}
