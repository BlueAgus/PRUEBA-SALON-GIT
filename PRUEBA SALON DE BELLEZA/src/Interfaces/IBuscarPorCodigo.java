package Interfaces;

import abstractas.Servicio;
import excepciones.CodigoNoEncontradoException;

public interface IBuscarPorCodigo<T extends Servicio> {

   T buscarPorCodigo(String codServicio) throws CodigoNoEncontradoException;

}
//Lo use en todos los gestores de servicios y me sirve para buscar en factura -agus