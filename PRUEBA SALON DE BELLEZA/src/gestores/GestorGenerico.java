package gestores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class GestorGenerico<T> {

    private List<T> lista = new ArrayList<>();


    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR////////////////////////////////////////////////////

    public boolean agregar(T elemento) {

        return lista.add(elemento);

    }

    public boolean eliminar(T elemento) {
        return lista.remove(elemento);
    }



    public void mostrar() {
        if (lista.isEmpty()) {
            System.out.println("No hay elementos en el almacén.");
        } else {
            for (T item : lista) {
                System.out.println(item);
            }
        }
    }

    public ArrayList<T> filtrarPorCondicion(Predicate<T> condicion) {
        ArrayList<T> resultado = new ArrayList<>();

        for (T elemento : lista) {
            if (condicion.test(elemento)) {
                resultado.add(elemento);
            }
        }
        return resultado;
    }

    public T obtenerPorIndice(int indice) {
        if (indice >= 0 && indice < lista.size()) {
            return lista.get(indice);
        } else {
            throw new IndexOutOfBoundsException("Índice fuera de rango.");
        }
    }
    public int obtenerIndice(T elemento) {
        return lista.indexOf(elemento);
    }

    public int buscarIndicePorCondicion(Predicate<T> condicion) {
        for (int i = 0; i < lista.size(); i++) {
            if (condicion.test(lista.get(i))) {
                return i;
            }
        }
        return -1; // Si no se encuentra ningún elemento
    }

    public boolean actualizarElemento(int indice, T nuevoElemento) {
        if (indice >= 0 && indice < lista.size()) {
            lista.set(indice, nuevoElemento);
            return true;
        } else {
            return false;
        }
    }

    public boolean existe(T elemento) {
        return lista.contains(elemento);
    }

    public void vaciarAlmacen() {
        lista.clear();
    }


    public T[] convertirAArray(T[] array) {
        return lista.toArray(array);
    }


    public void ordenar(Comparator<T> comparador) {
        lista.sort(comparador);
    }

    // Ej   gestor.ordenar((p1, p2) -> Integer.compare(p1.getEdad(), p2.getEdad()));

    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////


    public List<T> getLista() {
        return lista;
    }

    public void setLista(List<T> lista) {
        this.lista = lista;
    }
}
