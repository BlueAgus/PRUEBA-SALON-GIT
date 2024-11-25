package model;

import abstractas.Servicio;
import enumeraciones.TipoManicura;
import enumeraciones.TipoServicio;
import gestores.GestorPrecios;

import java.time.LocalTime;

public class Manicura extends Servicio {

    private TipoManicura tipoManicura;
    private boolean disenio;
    private double precioDisenio;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public Manicura(String duracion, TipoManicura tipoManicura, boolean disenio) {
        super(TipoServicio.MANICURA, duracion);
        this.tipoManicura = tipoManicura;
        this.precioDisenio = GestorPrecios.getPrecioDisenio();
        this.disenio = disenio;
        this.precio = calcularPrecio();
    }
    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////


    @Override
    public double calcularPrecio() {
        double precioBase = GestorPrecios.obtenerPrecio(Manicura.class, tipoManicura);
        this.precio = precioBase + (disenio ? precioDisenio : 0);
        return this.precio;
    }

    @Override
    public void setPrecio(double precio) {
        super.setPrecio(precio);
        GestorPrecios.modificarPrecio(Manicura.class, this.tipoManicura, precio);
        //Actualizamos el gestor, esto porque en algunos lados se actualiza el precio
        //usando el set y esto lo va a modificar en el gestor para que tengan el mismo valor

    }//En el get de servicio llamamos a calcular precio para cada ves que se quiere ver no aseguramos de que esta actualizado


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
