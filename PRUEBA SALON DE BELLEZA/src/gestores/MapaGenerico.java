package gestores;

import java.util.HashMap;
import java.util.Map;

public class MapaGenerico <K,V>{
    private Map<K, V> mapa;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public MapaGenerico() {
        this.mapa = new HashMap<>();
    }
    //La clave es unica y el valor puede repetirse
    ////////////////////////////////////////////////////////AGREGAR, BUSCAR Y ELIMINAR ////////////////////////////////////////////////////
    public void agregar(K clave, V valor){
        mapa.put(clave, valor);
    }

    public V obtener(K clave){
        return mapa.get(clave); // Devuelve el valor asociado a la clave
    }

    public void eliminar(K clave){
        mapa.remove(clave);
    }

    public boolean contiene(K clave){
        return mapa.containsKey(clave);
    }

    ////////////////////////////////////////////////////////GET  ////////////////////////////////////////////////////
    public Map<K, V> getMapa() {
        return mapa;
    }

    public void setMapa(Map<K, V> mapa) {
        this.mapa = mapa;
    }
}
