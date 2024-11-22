package abstractas;

import Interfaces.CrearID;
import enumeraciones.TipoServicio;

import java.util.Objects;

public abstract class Servicio implements CrearID {
    protected String codigo_servicio ; //
    protected TipoServicio tipoService;
    protected String duracion;
    protected double precio ;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Servicio(TipoServicio tipoService, String duracion, double precio) {

        this.tipoService = tipoService;
        this.duracion = duracion;
        this.precio= precio;
        this.codigo_servicio = this.generarIDEunico();
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////
    public abstract double calcularPrecio();

    //@Override // aca modificamos el metodo de la interfaz
    public String generarIDEunico() {
        long numeroUnico = (long) (Math.random() * 100L);  // Genera un número entre 0 y 100
        return String.valueOf(numeroUnico);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servicio servicio = (Servicio) o;
        return Objects.equals(codigo_servicio, servicio.codigo_servicio);
    }

    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////
    public String getCodigo_servicio() {return codigo_servicio;}

    public TipoServicio getTipoService() {
        return tipoService;
    }

    public void setTipoService(TipoServicio tipoService) {
        this.tipoService = tipoService;
    }

    public void setCodigo_servicio(String codigo_servicio) {
        this.codigo_servicio = codigo_servicio;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
