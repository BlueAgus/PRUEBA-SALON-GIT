package model;

import Interfaces.CrearID;
import enumeraciones.TipoServicio;
import excepciones.CodigoNoEncontradoException;
import excepciones.DNInoEncontradoException;
import gestores.GestorCliente;
import gestores.GestorProfesional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Turno implements CrearID {

    private String cod_turno;
    private String fecha;         // Fecha del turno
    private String horario;       // Hora del turno
    private String codigo_servicio;       // El servicio a realizar
    private String dni_profesional; // Profesional que atenderá el servicio
    private String dni_cliente;       // Cliente que r

    //////////////////////////////////////////////////////// CONSTRUCTORes ////////////////////////////////////////////////////

    public Turno(String cod_turno, String fecha, String horario,  String dni_profesional, String dni_cliente) {
        this.cod_turno = generarIDEunico();
        this.fecha = fecha;
        this.horario = horario;
        this.codigo_servicio = generarIDEunico();
        this.dni_profesional = dni_profesional;
        this.dni_cliente = dni_cliente;
    }

    // Constructor simplificado para crear turnos de disponibilidad sin servicio, profesional y cliente
    public Turno(String fecha, String horario) {
        this.fecha = fecha;
        this.horario = horario;
        this.codigo_servicio = null;
        this.dni_profesional = null;
        this.dni_cliente = null;
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turno turno = (Turno) o;
        return Objects.equals(fecha, turno.fecha) && Objects.equals(horario, turno.horario) && Objects.equals(codigo_servicio, turno.codigo_servicio) && Objects.equals(dni_profesional, turno.dni_profesional) && Objects.equals(dni_cliente, turno.dni_cliente);
    }


    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getCodigo_servicio() {
        return codigo_servicio;
    }

    public void setCodigo_servicio(String codigo_servicio) {
        this.codigo_servicio = codigo_servicio;
    }

    public String getDni_profesional() {
        return dni_profesional;
    }

    public void setDni_profesional(String dni_profesional) {
        this.dni_profesional = dni_profesional;
    }

    public String getDni_cliente() {
        return dni_cliente;
    }

    public void setDni_cliente(String dni_cliente) {
        this.dni_cliente = dni_cliente;
    }

    public String getCod_turno() {
        return cod_turno;
    }

    public void setCod_turno(String cod_turno) {
        this.cod_turno = cod_turno;
    }

    //////////////////////////////////////////////////////// TO STRING ////////////////////////////////////////////////////
    public String toString(TipoServicio tipoServicio, GestorCliente gestorCliente, GestorProfesional gestorProfesional) {
        try {

            return "\n        TURNO: " +
                    "\n| FECHA : " + fecha +
                    "\n| HORARIO : " + horario +
                    "\n| SERVICIO : " + tipoServicio +
                    "\n| PROFESIONAL :" + gestorProfesional.buscarPersona(dni_profesional).getNombre() +" "+ gestorProfesional.buscarPersona(dni_profesional).getApellido() +
                    "\n| CLIENTE : " + gestorCliente.buscarPersona(dni_cliente).getNombre() + gestorCliente.buscarPersona(dni_cliente).getApellido() + " DNI : " + gestorCliente.buscarPersona(dni_cliente).getDni();
        } catch (CodigoNoEncontradoException e) {
            return "Error: Servicio no encontrado para el código: " + codigo_servicio;
        } catch (DNInoEncontradoException e) {
            return "Error: Cliente o profesional no encontrado.";
        } catch (Exception e) {
            return "Error inesperado al generar los detalles del turno.";
        }

    }
}
