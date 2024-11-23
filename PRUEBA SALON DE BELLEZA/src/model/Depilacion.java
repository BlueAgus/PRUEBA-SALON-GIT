package model;

import abstractas.Servicio;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoServicio;
import gestores.GestorPrecios;

import java.time.LocalDate;

public class Depilacion extends Servicio {

    private TipoDepilacion tipoDepilacion;

    //////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Depilacion(String duracion, TipoDepilacion tipoDepilacion) {
        super(TipoServicio.DEPILACION, duracion);
        this.tipoDepilacion = tipoDepilacion;
    }

    /////////////////////////////////////// metodos extr ////////////////////////////////////////////////////
   public void programarMantenimiento(LocalDate fecha){
        //GestorTurno turnos = new GestorTurno();

    }

    public double calcularPrecio() {
       return this.precio = GestorPrecios.obtenerPrecio(Depilacion.class, this.tipoDepilacion);

    }
    @Override
    public void setPrecio(double precio) {
        super.setPrecio(precio);
        GestorPrecios.modificarPrecio(Manicura.class, this.tipoDepilacion, precio);
        //Actualizamos el gestor, esto porque en algunos lados se actualiza el precio
        //usando el set y esto lo va a modificar en el gestor para que tengan el mismo valor

    }//En el get de servicio llamamos a calcular precio para cada ves que se quiere ver no aseguramos de que esta actualizado



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
