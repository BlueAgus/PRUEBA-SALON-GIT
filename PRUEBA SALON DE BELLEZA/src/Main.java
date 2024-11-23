import Interfaces.IBuscarPorCodigo;
import abstractas.Servicio;
import enumeraciones.TipoDepilacion;
import enumeraciones.TipoServicio;
import gestores.*;
import model.Depilacion;

import java.util.ArrayList;
import java.util.List;

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
/*
 // Gestor Precios, no lo pude probar todavbia

     // "LASER": 18000.0,
        //GestorPrecios.modificarPrecio(Depilacion.class, TipoDepilacion.LASER, 20000);
        //GestorPrecios.setPrecioDisenio(10000); FUNCIONA Y GUARDA EN ARCHIVO
     //   System.out.println(GestorPrecios.verPrecios());
     //   GestorPrecios.guardarPreciosEnArchivo();
        GestorPrecios.cargarPreciosDesdeArchivo();

        System.out.println("---------------------------------------");

        System.out.println(GestorPrecios.verPrecios());
        //SE HIZO LA LUZ
*/
        // COMO SE VE ESTO ABAJO **
      /*
        GestorDepilacion gestorDepilacion = new GestorDepilacion();
        gestorDepilacion.agregarServicio();
        System.out.println("================================");
        GestorPestania gestorPestania = new GestorPestania();
        GestorManicura gestorManicura = new GestorManicura();
        GestorCliente gestorCliente = new GestorCliente();

        gestorCliente.agregarPersona();
        GestorTurno gestorTurno = new GestorTurno(gestorDepilacion, gestorPestania, gestorManicura, gestorCliente);

        System.out.println("----------------------------------------");
        gestorTurno.agregarTurno();

        List<IBuscarPorCodigo<? extends Servicio>> gestores = new ArrayList<>();
        gestores.add(gestorDepilacion);
        gestores.add(gestorPestania);
        gestores.add(gestorManicura);

        GestorFactura gestorFactura = new GestorFactura(gestorTurno, gestores);

       // gestorFactura.crearFactura();
       no llegue a probar factura, me queme la cabeza, y llegue has
       */
    }
}

// ACA ***


/*Introduce el precio del servicio: 1515
Introduce las horas que durara el servicio (0-23):1
Introduce los minutos que durara el servicio (0-59): 0
Selecciona el tipo de depilación:
1. Cera
2. Láser
Elige una opción: 1
 | DEPILACIÓN CERA
 | Precio: 1515.0
 | Duracion: 01:00
¿Deseas modificar algo del servicio?
1. Sí
2. No
2
....
================================
Ingrese el DNI:
41715115
Ingrese el nombre: agustina
Ingrese el apellido: massuco
Ingrese el GÉNERO (M, F, O):
f
Ingrese el teléfono: 2236693137 ACA ARRANCO CON UN ENTER Y ME TIRA LO DE ABAJO PERO NO TOQUE NADA
El número de teléfono debe tener  10 dígitos y solo contener números.
Ingrese el teléfono: 2236693137 ACA CREO QUE APRETE ENTER DOS VECES

¿Deseas modificar algo del cliente?
1. Sí
2. No
2
....

CLIENTE AGREGADO EXITOSAMENTE
| Nombre : Agustina
| Apellido : Massuco
| DNI : 41715115
| Genero : F
| Telefono : 2236693137
=========================================

---------------------------------------- //ACA ABRIA QUE ACLARAR QUE ESTAS RESERVANDO UN TURNO
Ingrese el DNI del cliente (o escriba 'salir' para cancelar):
41715115
Selecciona el tipo de servicio:
1. Uñas
2. Pestañas
3. Depilación
3
0-
 | DEPILACIÓN CERA
 | Precio: 1515.0
 | Duracion: 01:00

  | Duracion: 01:00
¿Deseas modificar algo del servicio?
1. Sí
2. No
2
....
================================
Ingrese el DNI:
41715115
Ingrese el nombre: agustina
Ingrese el apellido: massuco
Ingrese el GÉNERO (M, F, O):
f
Ingrese el teléfono: 2236693137
El número de teléfono debe tener  10 dígitos y solo contener números.
Ingrese el teléfono: 2236693137

¿Deseas modificar algo del cliente?
1. Sí
2. No
2
....

CLIENTE AGREGADO EXITOSAMENTE
| Nombre : Agustina
| Apellido : Massuco
| DNI : 41715115
| Genero : F
| Telefono : 2236693137
=========================================

----------------------------------------
Ingrese el DNI del cliente (o escriba 'salir' para cancelar):
41715115
Selecciona el tipo de servicio:
1. Uñas
2. Pestañas
3. Depilación
3
0-
 | DEPILACIÓN CERA
 | Precio: 1515.0
 | Duracion: 01:00
OPCION: (o escriba 'salir' para cancelar)
0
Ingrese la fecha (YYYY-MM-DD): (o escriba 'salir' para cancelar)
2024-11-25
Turnos disponibles del dia: 2024-11-25
0- 09:00
1- 10:00
2- 11:00
3- 12:00
4- 13:00
5- 14:00
6- 15:00
7- 16:00
8- 17:00
OPCIÓN: (o escriba 'salir' para cancelar)
0
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "gestores.GestorProfesional.getProfesionales()" because "this.gestorProfesional" is null
	at gestores.GestorTurno.pedirDNIprofesionalXservicio(GestorTurno.java:737)
	at gestores.GestorTurno.agregarTurno(GestorTurno.java:84)
	at Main.main(Main.java:255)

Process finished with exit code 1 // bueno llegue hasta y se rompio tod no se porque 
*/