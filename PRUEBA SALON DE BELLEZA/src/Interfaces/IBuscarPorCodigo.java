package Interfaces;

import abstractas.Servicio;
import excepciones.CodigoNoEncontradoException;

public interface IBuscarPorCodigo<T extends Servicio> {
    T buscarPorCodigo(String codServicio) throws CodigoNoEncontradoException;

}
