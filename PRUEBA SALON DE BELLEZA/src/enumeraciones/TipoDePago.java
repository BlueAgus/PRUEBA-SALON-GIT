package enumeraciones;

public enum TipoDePago {
    EFECTIVO(-15),  // 15% de descuento
    TARJETA(15),    // 15% de recargo
    TRANSFERENCIA(0);  // Sin ajuste, 0%

    private final int porcentajeAjuste;  // Porcentaje de ajuste (descuento o recargo)

    // Constructor del enum que recibe el porcentaje de ajuste
    TipoDePago(int porcentajeAjuste) {
        this.porcentajeAjuste = porcentajeAjuste;
    }

    // Getter para obtener el porcentaje de ajuste
    public int getPorcentajeAjuste() {
        return porcentajeAjuste;
    }

    // Met odo que calcula el total ajustado según el tipo de pago
    public double calcularPagoTotal(double precio) {
        double ajuste = (porcentajeAjuste / 100.0) * precio;  // Ajuste en base al porcentaje
        return precio + ajuste;  // Precio ajustado (descuento o recargo)
    }
}
