package Interfaces;

import java.util.UUID;

public interface CrearID {
    //Se define la implementacion del metodo, el default permite que si la clase necesitara modificar
    //el metodo, lo hiciera.
    default String generarIDEunico() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, 10);
    }
}
