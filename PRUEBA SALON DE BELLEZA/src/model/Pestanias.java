package model;

import abstractas.Servicio;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;
import gestores.GestorPrecios;

public class Pestanias extends Servicio {
    private TipoPestanias tipoPestanias;

    //////////////////////////////////// CONSTRUCTOR ///////////////////////////////////////////////

    public Pestanias(String duracion, TipoPestanias tipoPestanias) {
        super(TipoServicio.PESTANIAS, duracion);
        this.tipoPestanias = tipoPestanias;
      //  this.precio=precio; No le pasamos el precio porque una vez que se instancia se calcula con el metodo
    }

    ////////////////////////////////// metodos extr //////////////////////////////////////////////

    @Override
    public double calcularPrecio() {
        return this.precio = GestorPrecios.obtenerPrecio(Pestanias.class, this.tipoPestanias);

    }
    @Override
    public void setPrecio(double precio) {
        super.setPrecio(precio);
        GestorPrecios.modificarPrecio(Manicura.class, this.tipoPestanias, precio);
        //Actualizamos el gestor, esto porque en algunos lados se actualiza el precio
        //usando el set y esto lo va a modificar en el gestor para que tengan el mismo valor

    }

    /////////////////////////////////GET Y SET ////////////////////////////////////////////////////

    public TipoPestanias getTipoPestanias() {
        return tipoPestanias;}

    public void setTipoPestanias(TipoPestanias tipoPestanias) {
        this.tipoPestanias = tipoPestanias;
    }

    /////////////////////////////// TO STRING ////////////////////////////////////////////////////

    @Override
    public String toString() {

        String tipoPestaniasStr = switch (this.tipoPestanias) {
            case TRES_D -> "3D";
            case DOS_D -> "2D";
            case CLASICAS -> "CLÁSICAS";
        };

        return "\n| PESTAÑAS " + tipoPestaniasStr+
                " \n| Precio: " + precio +
                " \n| Duracion: " + duracion ;
    }
}
