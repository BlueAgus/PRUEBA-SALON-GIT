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
    //private static double precioDisenio = GestorPrecios.getPrecioDisenio();


    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
  /// saque esto double precio, double precioDisenio prque a se calcula con el gestor
    public Manicura(String duracion, TipoManicura tipoManicura, boolean disenio) {
        super(TipoServicio.MANICURA, duracion);
        this.tipoManicura = tipoManicura;
        this.precio = calcularPrecio(); // es importante que esto este luego de que se defina el tipo
        this.precioDisenio = GestorPrecios.getPrecioDisenio();
        //this.precio = calcularPrecio(); esto esta en servicio
        this.disenio = disenio;

    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////


    /*@Override
    public double calcularPrecio() {
        double precioBase = GestorPrecios.obtenerPrecio(Manicura.class, tipoManicura);
        this.precio = precioBase + (disenio ? precioDisenio : 0);
        return this.precio;
    }*/

    @Override
    public double calcularPrecio() {
        return this.precio+precioDisenio;
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

    /*@Override
    public String toString() {
        return "| MANICURA: " + tipoManicura +
                (disenio ? " con diseño " : " sin diseño ")+
                " \n| Precio: " + precio +
                " \n| Duracion: " + duracion;
    }*/


}
