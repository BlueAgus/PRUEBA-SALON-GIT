package model;

import abstractas.Persona;

import javax.print.DocFlavor;

public class Administrador extends Recepcionista {

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Administrador() {
    }

    public Administrador(String nombre, String apellido, String dni, String genero, String telefono, String contraseña) {
        super(nombre, apellido, dni, genero, telefono,contraseña);
    }

}
