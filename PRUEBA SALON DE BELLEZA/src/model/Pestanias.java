package model;

import abstractas.Servicio;
import enumeraciones.TipoPestanias;
import enumeraciones.TipoServicio;

public class Pestanias extends Servicio {
    private TipoPestanias tipoPestanias;

    //////////////////////////////////// CONSTRUCTOR ///////////////////////////////////////////////

    public Pestanias(String duracion, TipoPestanias tipoPestanias, double precio) {
        super(TipoServicio.PESTANIAS, duracion, precio);
        this.tipoPestanias = tipoPestanias;
        this.precio=precio;
    }

    ////////////////////////////////// metodos extr //////////////////////////////////////////////

    @Override
    public double calcularPrecio() {/*
        return GestorPrecios.obtenerPrecio(Pestanias.class, this.tipoPestanias);
    */
    return 0;
    }

    /////////////////////////////////GET Y SET ////////////////////////////////////////////////////

    public TipoPestanias getTipoPestanias() {return tipoPestanias;}

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
