import enumeraciones.TipoServicio;
import gestores.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //probando si guarda bien - agu
        //soy cei
  /*
        ///DEPILACION
        GestorDepilacion gestorDepilacion = new GestorDepilacion();

        System.out.println("DEPILACIONES: ");

        gestorDepilacion.leerServiciosDesdeJson();
        gestorDepilacion.mostrarServicios();

        System.out.println("AGREGAR SERVICIO");
        gestorDepilacion.agregarServicio();

        System.out.println("ELIMINAR SERVICIO");
        gestorDepilacion.eliminarServicio();

        System.out.println("MOSTRAR SERVICIOS");
        gestorDepilacion.mostrarServicios();

        gestorDepilacion.escribirServiciosEnJson();
  */

  /*
        /// PESTAÑAS
        GestorPestania gestorPestania = new GestorPestania();

        gestorPestania.leerServiciosDesdeJson();

        gestorPestania.mostrarServicios();

        System.out.println("AGREGAR SERVICIO");
        gestorPestania.agregarServicio();

        System.out.println("ELIMINAR SERVICIO");
        gestorPestania.eliminarServicio();

        System.out.println("MOSTRAR SERVICIOS");
        gestorPestania.mostrarServicios();

        gestorPestania.escribirServiciosEnJson();
  */

  /*    ///MANICURA
        GestorManicura gestorManicura= new GestorManicura();


        gestorManicura.leerServiciosDesdeJson();

        gestorManicura.mostrarServicios();

        System.out.println("AGREGAR SERVICIO");
        gestorManicura.agregarServicio();

        System.out.println("ELIMINAR SERVICIO");
        gestorManicura.eliminarServicio();

        System.out.println("MOSTRAR SERVICIOS");
        gestorManicura.mostrarServicios();

        gestorManicura.escribirServiciosEnJson();
*/

        ///CLIENTE
/*
        GestorCliente gestorCliente= new GestorCliente();

        gestorCliente.leerArchivoClientes();

        gestorCliente.mostrarTodos();

        System.out.println("AGREGAR CLIENTE");
        gestorCliente.agregarPersona();

        System.out.println("ELIMINAR CLIENTE");
        gestorCliente.eliminarPersona("45131280");

        System.out.println("MOSTRAR CLIENTES");
        gestorCliente.mostrarTodos();

        gestorCliente.escribirClientesEnJson();
*/

        ///PROFESIONAL
        ///crea y guarda todas las listas del archivo en cada gestor
 /*      GestorDepilacion gestorDepilacion = new GestorDepilacion();
        gestorDepilacion.leerServiciosDesdeJson();

        GestorPestania gestorPestania = new GestorPestania();
        gestorPestania.leerServiciosDesdeJson();

        GestorManicura gestorManicura= new GestorManicura();
        gestorManicura.leerServiciosDesdeJson();



        GestorProfesional gestorProfesional=null;

        GestorTurno gestorTurno= new GestorTurno(gestorDepilacion, gestorPestania, gestorManicura, gestorProfesional);

        gestorProfesional= new GestorProfesional(gestorTurno);

        gestorProfesional.leerProfesionalesDesdeJson();

        gestorProfesional.mostrarTodos();


        System.out.println("AGREGAR PROFESIONAL");
        gestorProfesional.agregarPersona();

        System.out.println("ELIMINAR PROFESIONAL");
        gestorProfesional.eliminarPersona("45131280");

        System.out.println("MOSTRAR PROFESIONALES");
        gestorProfesional.mostrarTodos();

        gestorProfesional.escribirProfesionalesEnJson();

/*
        ///RECEPCIONISTAS

        GestorRecepcionista gestorRecepcionista= new GestorRecepcionista();

        gestorRecepcionista.leerDesdeJson();

        gestorRecepcionista.mostrarTodos();


        System.out.println("AGREGAR RECEP");
        gestorRecepcionista.agregarPersona();

       System.out.println("ELIMINAR RECEP");
        gestorRecepcionista.eliminarPersona("45131280");

        System.out.println("MOSTRAR RECEPS");
        gestorRecepcionista.mostrarTodos();


        gestorRecepcionista.escribirArchivoRecepcionistas();*/


        ////ADMINISTRADOR!
/*
        GestorAdministrador gestorAdministrador= new GestorAdministrador();

        gestorAdministrador.leerDesdeJSON();

        gestorAdministrador.mostrarTodos();


        System.out.println("AGREGAR RECEP");
        gestorAdministrador.agregarAdministrador();

        System.out.println("ELIMINAR RECEP");
        gestorAdministrador.eliminarAdministradorDeLaLista("45131280");

        System.out.println("MOSTRAR RECEPS");
        gestorAdministrador.mostrarTodos();




        gestorAdministrador.guardarArchivoAdministradores();
*/

/*

        GestorDepilacion gestorDepilacion = new GestorDepilacion();
        gestorDepilacion.leerServiciosDesdeJson();

        GestorPestania gestorPestania = new GestorPestania();
        gestorPestania.leerServiciosDesdeJson();

        GestorManicura gestorManicura = new GestorManicura();
        gestorManicura.leerServiciosDesdeJson();

        GestorCliente gestorCliente = new GestorCliente();
        gestorCliente.leerArchivoClientes();


        GestorTurno gestorTurno= new GestorTurno(gestorDepilacion, gestorPestania, gestorManicura,gestorCliente);
        GestorProfesional gestorProfesional =  new GestorProfesional(gestorTurno);


        gestorTurno.pedirGestorProfesionales(gestorProfesional);



        gestorProfesional.leerProfesionalesDesdeJson();

        gestorTurno.cargarTurnosDesdeArchivo();


        gestorPestania.mostrarServicios();
        gestorTurno.mostrarHistorialTurnos();

        System.out.println("MOSTRAR TURNOS");
        gestorTurno.mostrarTurnosVigentes();




        System.out.println("AGREGAR ADMIN");
        gestorTurno.agregarTurno();

        System.out.println("ELIMINAR ADMIN");
        gestorTurno.eliminarTurno();



        gestorTurno.guardarTurnosEnArchivo();

 */

 // Gestor Precios, no lo pude probar todavbia
  /*      System.out.println(GestorPrecios.verPrecios());

        GestorPrecios.escribirPreciosEnEnJson();

        GestorPrecios.leerArchivoPrecios();
*/
    }
}