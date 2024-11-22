package model;

import abstractas.Servicio;
import enumeraciones.TipoManicura;
import enumeraciones.TipoServicio;

import java.time.LocalTime;

public class Manicura extends Servicio {
    private TipoManicura tipoManicura;
    private boolean disenio;
    //private static double precioDisenio = GestorPrecios.getPrecioDisenio();
    private double precioDisenio;


    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Manicura(String duracion, TipoManicura tipoManicura, double precio, double precioDisenio) {
        super(TipoServicio.MANICURA, duracion, precio);
        this.tipoManicura = tipoManicura;
        this.precioDisenio = precioDisenio;
        this.precio = calcularPrecio();
        if (precioDisenio > 0) {
            disenio = true;
        } else {

            disenio = false;
        }
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////


    @Override
    public double calcularPrecio() {

        return precio + precioDisenio;
    }

    ////////////////////////////////////GET Y SET //////////////////////////////////////////////////

    public TipoManicura getTipoManicura() {
        return tipoManicura;
    }

    public void setTipoManicura(TipoManicura tipoManicura) {
        this.tipoManicura = tipoManicura;
    }

    public boolean isDisenio() {
        return disenio;
    }

    public void setDisenio(boolean disenio) {
        this.disenio = disenio;
    }

    public double getPrecioDisenio() {
        return precioDisenio;
    }

    public void setPrecioDisenio(double precioDisenio) {
        this.precioDisenio = precioDisenio;
    }


    ////////////////////////////////// TO STRING ////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "| MANICURA: " + tipoManicura +
                (disenio ? " con diseño " : " sin diseño ")+
                " \n| Precio: " + calcularPrecio() +
                " \n| Duracion: " + duracion;
    }


}
