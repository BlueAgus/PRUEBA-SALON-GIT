package model;

import abstractas.Servicio;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoServicio;

import java.time.LocalDate;

public class Depilacion extends Servicio {

    private TipoDepilacion tipoDepilacion;

    //////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Depilacion(String duracion, TipoDepilacion tipoDepilacion, double precio) {
        super(TipoServicio.DEPILACION, duracion, precio);
        this.tipoDepilacion = tipoDepilacion;
        this.precio=precio;
    }

    /////////////////////////////////////// metodos extr ////////////////////////////////////////////////////
   public void programarMantenimiento(LocalDate fecha){
        //GestorTurno turnos = new GestorTurno();

    }

    public double calcularPrecio() {
       // return GestorPrecios.obtenerPrecio(Depilacion.class, this.tipoDepilacion);
    return 0;
    }


    /////////////////////////////////GET Y SET ////////////////////////////////////////////////////

    public TipoDepilacion getTipoDepilacion() {
        return tipoDepilacion;
    }

    public void setTipoDepilacion(TipoDepilacion tipoDepilacion) {
        this.tipoDepilacion = tipoDepilacion;
    }

    ////////////////////////////////////// TO STRING ////////////////////////////////////////////////////
    @Override
    public String toString() {
        return " | DEPILACIÓN " + tipoDepilacion +
                "\n | Precio: " + precio +
                "\n | Duracion: " + duracion;
    }
}
