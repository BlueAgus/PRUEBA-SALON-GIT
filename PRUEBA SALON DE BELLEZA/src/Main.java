import Interfaces.IBuscarPorCodigo;
import abstractas.Servicio;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoServicio;
import gestores.*;
import menus.MenuPrincipal;
import model.Administrador;
import model.Depilacion;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MenuPrincipal menuPrincipal = new MenuPrincipal();
        menuPrincipal.menuPrincipal();
    }
}
