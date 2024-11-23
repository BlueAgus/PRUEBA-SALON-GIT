package gestores;

import abstractas.Servicio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import enumeraciones.TipoServicio;
import excepciones.CodigoNoEncontradoException;
import excepciones.DNInoEncontradoException;
import model.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class GestorTurno {
    private MapaGenerico<String, List<Turno>> listaTurnos;
    private static final String archivoTurnos = "turnos.json";
    private static Scanner scanner = new Scanner(System.in);
    Gson gson = new Gson();
    GestorDepilacion gestorDepilacion;
    GestorPestania gestorPestania;
    GestorManicura gestorManicura;
    GestorProfesional gestorProfesional;
    GestorCliente gestorCliente;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public GestorTurno(GestorDepilacion gestorDepilacion, GestorPestania gestorPestania, GestorManicura gestorManicura, GestorCliente gestorCliente) {
        this.listaTurnos = new MapaGenerico<>();
        this.gestorDepilacion = gestorDepilacion;
        this.gestorPestania = gestorPestania;
        this.gestorManicura = gestorManicura;
        this.gestorProfesional = gestorProfesional;
        this.gestorCliente = gestorCliente;
    }

    public void pedirGestorProfesionales(GestorProfesional gestorProfesional) {
        this.gestorProfesional = gestorProfesional;
    }

    ////////////////////////////////////////////////////////AGREGAR, ELIMINAR, BUSCAR Y MODIFICAR ////////////////////////////////////////////////////

    public boolean agregarTurno() {

        String dniCliente = pedirDNIcliente();//METODO pedir dni cliente

        if (dniCliente == null) {
            return false;
        }

        //pregunta que tipo de servicio
        TipoServicio tipoServicio = gestorDepilacion.pedirTipoServicio();
        String codServicio =pedirCodServicio(tipoServicio);

//

        if (codServicio == null) {
            return false;
        }

        Turno turno = elegirFechaYhorario(codServicio, tipoServicio);
        try{
            turno.setCodigo_servicio(codServicio);
        }catch (NullPointerException e){

        }
        if (turno == null) {
            return false;
        }

        while (!verificarClienteXHorario(dniCliente, turno.getHorario(), turno.getFecha())) {
            System.out.println("El cliente ya tiene reservado un turno en la misma fecha y horario!!");
            turno = elegirFechaYhorario(codServicio, tipoServicio);
        }

        String dniProfesional = pedirDNIprofesionalXservicio(codServicio, turno.getHorario(), turno.getFecha());//METODO pedir dni profesional

        if (dniProfesional == null) {
            return false;
        }

        turno.setDni_cliente(dniCliente);

        turno.setDni_profesional(dniProfesional);


        System.out.println(turno);

        if (listaTurnos.contiene(turno.getFecha())) {
            listaTurnos.getMapa().get(turno.getFecha()).add(turno);
        } else {
            List<Turno> turnos = new ArrayList<>();
            turnos.add(turno);

            listaTurnos.agregar(turno.getFecha(), turnos);
        }
        return true;
    }

    public boolean eliminarTurno() {

        String codTurno = buscarCodigoTurno();
        for (List<Turno> e : listaTurnos.getMapa().values()) {
            for (Turno t : e) {
                if (t.getCod_turno().equals(codTurno)) {
                    return e.remove(t);
                }
            }
        }
        return false;
    }

    public TipoServicio buscarTipoServicio(String codServicio) {

        try {
            Depilacion depilacion = gestorDepilacion.buscarPorCodigo(codServicio);
            if (depilacion != null) {
                return depilacion.getTipoService();
            }
        } catch (CodigoNoEncontradoException e) {


        }
        try {
            Manicura manicura = gestorManicura.buscarPorCodigo(codServicio);
            if (manicura != null) {
                return manicura.getTipoService();
            }
        } catch (CodigoNoEncontradoException e) {


        }

        try {
            Pestanias pestanias = gestorPestania.buscarPorCodigo(codServicio);
            if (pestanias != null) {
                return pestanias.getTipoService();

            }
        } catch (CodigoNoEncontradoException e) {


        }

        return null;

    }

    public String buscarCodigoTurno() {

        String dniCliente = gestorCliente.pedirDNIsinVerificacion();

        int i = 0;
        List<Turno> turnosDelCliente = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
                if (t.getDni_cliente().equals(dniCliente)) {
                    System.out.println(i + "-" + t.toString(buscarTipoServicio(t.getCodigo_servicio()), gestorCliente, gestorProfesional));
                    turnosDelCliente.add(t);
                    i++;
                }
            }
        }
        int opc = 0;
        while (true) {

            try {
                System.out.println("OPCION: (o escriba 'salir' para cancelar) ");
                String opcElegida = scanner.nextLine();

                if (opcElegida.equalsIgnoreCase("salir")) {
                    System.out.println("Operación cancelada por el usuario.");
                    return null;
                }

                ///pasa a int un string
                opc = Integer.parseInt(opcElegida);
                if (opc < 0 || opc > turnosDelCliente.size()) {
                    System.out.println("Selección inválida. Inténtelo de nuevo.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no valida. Por favor ingrese un número.");
                scanner.nextLine();
            }catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número.");
            }
        }
        return turnosDelCliente.get(opc).getCod_turno();
    }

    public void cancelarTurnosXdia(String fecha, String codServicio) {
        List<Turno> turnos = obtenerTurnosReservadosXfecha(fecha);

        System.out.println("Avisar a los siguientes clientes que su turno del dia " + fecha + "ha sido cancelado");
        for (Turno t : turnos) {
            if (t.getCodigo_servicio().equals(codServicio)) {
                Cliente cliente = null;
                try {
                    cliente = gestorCliente.buscarPersona(t.getDni_cliente());
                } catch (DNInoEncontradoException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("- " + cliente.getNombre() + " TELEFONO: " + cliente.getTelefono());
            }
        }
        turnos.clear();
    }

    public Turno buscarTurnoXclienteFechaHorario() {

        String codigoTurno = buscarCodigoTurno();

        for (List<Turno> e : listaTurnos.getMapa().values()) {
            for (Turno t : e) {
                if (t.getCod_turno().equals(codigoTurno)) {
                    return t;
                }
            }
        }
        return null;
    }

    public Turno buscarTurnoXcodigo(String codTurno) {

        for (List<Turno> e : listaTurnos.getMapa().values()) {
            for (Turno t : e) {
                if (t.getCod_turno().equals(codTurno)) {
                    return t;
                }
            }
        }
        return null;
    }

    public boolean modificarTurno(TipoServicio tipoServicio) {
        Turno t;

        List<Turno> turnosVigentes = mostrarTurnosVigentes();

        int opc = 0;
        ///eleccion de turno
        while (true) {

            System.out.println("OPCIÓN: (o escriba 'salir' para cancelar) ");
            String opcElegida = scanner.nextLine();

            if (opcElegida.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return false;
            }

            try {
                opc = Integer.parseInt(opcElegida); // Convierte el String a entero
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número.");
            }

            if (opc < 0 || opc >= turnosVigentes.size()) {
                System.out.println("Opcion no valida");
            } else {
                t = turnosVigentes.get(opc);
                break;
            }
        }

        ///modificacion de turno
        while (true) {
            System.out.println("DATOS ACTUALES DEL TURNO: " + t.toString(buscarTipoServicio(t.getCodigo_servicio()), gestorCliente, gestorProfesional));
            System.out.println("| Ingrese la opcion que desea modificar:");
            System.out.println("1- Fecha y horario");
            System.out.println("2- Profesional");
            System.out.println("3- Cliente");


            System.out.println("OPCIÓN: (o escriba 'salir' para cancelar) ");
            String opcElegida = scanner.nextLine();

            if (opcElegida.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return false;
            }

            try {
                opc = Integer.parseInt(opcElegida); // Convierte el String a entero
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número.");
            }

            if (opc < 0 || opc > 3) {
                System.out.println("Opcion no valida");
            } else {
                break;
            }
        }

        switch (opc) {
            case 1:
                Turno aux = elegirFechaYhorario(t.getCodigo_servicio(), tipoServicio);
                t.setFecha(aux.getFecha());
                t.setHorario(aux.getHorario());
                break;

            case 2:
                String dniProfesional = pedirDNIprofesionalXservicio(t.getCodigo_servicio(), t.getHorario(), t.getFecha());

                if (dniProfesional == null) {
                    break;
                }

                t.setDni_profesional(dniProfesional);
                break;
            case 3:
                String dniCliente = pedirDNIcliente();

                if (dniCliente == null) {
                    break;
                }

                t.setDni_cliente(dniCliente);
                break;
            ///no pongo default pq ya está verificado arriba
        }
        return true;
    }

    public List<Turno> mostrarTurnosVigentes() {
        int i = 0;
        List<Turno> turnosVigentes = new ArrayList<>();
        if (!(listaTurnos == null && listaTurnos.getMapa().isEmpty())) {
            for (List<Turno> list : listaTurnos.getMapa().values()) {
                for (Turno t : list) {
                   // LocalDate fecha = Turno.convertirStringALocalDate(t.getFecha());
                    LocalDate fecha = ConvertirFechaHoras.convertirStringAFecha(t.getFecha());

                    if (fecha.isAfter(LocalDate.now()) || fecha.isEqual(LocalDate.now())) {
                        System.out.println(i + "-" + t.toString(buscarTipoServicio(t.getCodigo_servicio()), gestorCliente, gestorProfesional));
                        turnosVigentes.add(t);
                        i++;
                    }
                }
            }
        } else {
            System.out.println("vaciaaaaaaaaa");
        }
        return turnosVigentes;
    }

    public void mostrarHistorialTurnos() {
        int i = 0;
        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
              //  LocalDate fecha = Turno.convertirStringALocalDate(t.getFecha());
                LocalDate fecha = ConvertirFechaHoras.convertirStringAFecha(t.getFecha());

                if (fecha.isBefore(LocalDate.now())) {
                    System.out.println(i + "-" + t.toString(buscarTipoServicio(t.getCodigo_servicio()), gestorCliente, gestorProfesional));
                    i++;
                }
            }
        }
    }

    ///turnos vigentes del cliente
    public List<Turno> buscarTurnosXdniClienteVigentes(String dniCliente) {
        List<Turno> turnos = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
                //LocalTime horario = Turno.convertirStringALocalTime(t.getHorario());
                LocalTime horario = ConvertirFechaHoras.convertirStringAHora(t.getHorario());

                if (t.getDni_cliente().equals(dniCliente) && (horario.isAfter(LocalTime.now()) || horario.equals(LocalTime.now()))) {
                    turnos.add(t);
                }
            }
        }
        return turnos;
    }

    ///turnos vigentes del profesional
    public List<Turno> buscarTurnosXdniProfesionalVigentes(String dniProfesional) {
        List<Turno> turnos = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
               // LocalDate fecha = ConvertirFechaHoras.convertirStringAFecha(t.getFecha());
               // LocalDate fecha = Turno.convertirStringALocalDate(t.getFecha());
                LocalDate fecha = ConvertirFechaHoras.convertirStringAFecha(t.getFecha());

                if (t.getDni_profesional().equals(dniProfesional) && (fecha.isAfter(LocalDate.now()) || fecha.isEqual(LocalDate.now()))) {
                    turnos.add(t);
                }
            }
        }
        return turnos;
    }

    ///historial de turnos por cliente
    public List<Turno> historialTurnosXcliente(String dniCliente) {
        List<Turno> turnos = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
               // LocalDate fecha = Turno.convertirStringALocalDate(t.getFecha());
                LocalDate fecha = ConvertirFechaHoras.convertirStringAFecha(t.getFecha());

                if (t.getDni_cliente().equals(dniCliente) && fecha.isBefore(LocalDate.now())) {
                    turnos.add(t);
                }
            }
        }
        return turnos;
    }

    ///historial de turnos por profesional
    public List<Turno> historialTurnosXprofesional(String dniProfesional) {
        List<Turno> turnos = new ArrayList<>();

        for (List<Turno> list : listaTurnos.getMapa().values()) {
            for (Turno t : list) {
                //LocalDate fecha = Turno.convertirStringALocalDate(t.getFecha());
                LocalDate fecha = ConvertirFechaHoras.convertirStringAFecha(t.getFecha());
                if (t.getDni_profesional().equals(dniProfesional) && fecha.isBefore(LocalDate.now())) {
                    turnos.add(t);
                }
            }
        }
        return turnos;
    }

/////////////////////////////////////////////MANEJO DE SERVICIOS!!!!////////////////////////////////////////

    public String pedirCodServicio(TipoServicio tipoServicio) {

        String codServicio = null;

        switch (tipoServicio) {
            case TipoServicio.DEPILACION:
                codServicio = gestorDepilacion.pedirCodServicio();
                break;
            case TipoServicio.MANICURA:
                codServicio = gestorManicura.pedirCodServicio();
                break;
            case TipoServicio.PESTANIAS:
                codServicio = gestorPestania.pedirCodServicio();
                break;
        }
        return codServicio;
    }


/////////////////////////////////////////////MANEJO DE CLIENTES!!!!////////////////////////////////////////

    public String pedirDNIcliente() {
        String dniCliente;
        while (true) {

            System.out.println("Ingrese el DNI del cliente (o escriba 'salir' para cancelar):");
            dniCliente = scanner.nextLine();

            if (dniCliente.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null; // Devuelve null o lanza una excepción según el diseño
            }

            try {
                gestorCliente.verificarSiExisteCliente(dniCliente);
                return dniCliente;

            } catch (DNInoEncontradoException e) {
                System.out.println(e.getMessage());
            }
        }

        ///tendriamos que verificar que el cliente no tenga más turnos en el mismo horario y dia?? o podriamos decir que puede reservar turnos para otra persona
    }

    public boolean verificarClienteXHorario(String dni, String horario, String fecha) {
        List<Turno> turnos = obtenerTurnosReservadosXfecha(fecha);

        if (turnos != null) {
            for (Turno t : turnos) {
                if (t.getDni_cliente().equals(dni) && t.getHorario().equals(horario)) {
                    return false; ///retorna falso si el cliente tiene un turno con el mismo horario solicitado
                }
            }
        }

        return true;//si no hay ningun turno con el mismo horario y cliente retorna true

    }

/////////////////////////////////////////////MANEJO DE FECHAS!!!!////////////////////////////////////////

    ///retorna un turno con la fecha y el horario elegido
    //aca se rompe pero no se porque
    public Turno elegirFechaYhorario(String cod_servicio, TipoServicio tipoServicio) {

        String fecha = ConvertirFechaHoras.convertirFechaAString(pedirFecha());
        //aca use la clase de convertir fecha pero antes con el otro metodo tambien se rompia
        if (fecha == null) {
            return null;
        }
        ///guarda los horarios disponibles y los muestra
        List<LocalTime> horariosDisponibles = mostrarTurnosDisponiblesXfecha(fecha, cod_servicio, tipoServicio);

        int indiceHorario=0;
        ///seleccion de horario
        while (true) {
            try {
                if (horariosDisponibles.isEmpty()) {
                    System.out.println("No hay turnos disponibles en la fecha seleccionada");
                    ///NO HAY HORARIOS DISPONIBLES
                } else {

                    System.out.println("OPCIÓN: (o escriba 'salir' para cancelar) ");
                    String opcElegida = scanner.nextLine();

                    if (opcElegida.equalsIgnoreCase("salir")) {
                        System.out.println("Operación cancelada por el usuario.");

                    }

                    try {
                        indiceHorario = Integer.parseInt(opcElegida); // Convierte el String a entero
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Debe ingresar un número.");

                    }

                    if (indiceHorario < 0 || indiceHorario >= horariosDisponibles.size()) {
                        System.out.println("Opcion no valida");
                    } else {
                        break;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no valida. Por favor ingrese un número.");
                scanner.nextLine();///limpia buffer
            }catch (NullPointerException i)
            {
                System.out.println("Entrada no valida. Por favor ingrese un número.");
                scanner.nextLine();
            }catch (NumberFormatException r)
            {
                System.out.println("Entrada no valida. Por favor ingrese un número.");
                scanner.nextLine();
            }
        }

       // String horario = Turno.convertirLocalTimeAString(horariosDisponibles.get(indiceHorario));
        String horario = ConvertirFechaHoras.convertirHoraAString(horariosDisponibles.get(indiceHorario));
        Turno turno = new Turno(fecha, horario);
        ///System.out.println(turno);
        return turno;
    }

    ///retorna la fecha ingresada por el usuario
    public LocalDate pedirFecha() {
        LocalDate fecha = null;
        boolean valido = false;
        while (!valido) {
            System.out.println("Ingrese la fecha (YYYY-MM-DD): (o escriba 'salir' para cancelar)");

            // Lo guarda en un string para verificar que no haya escrito "salir"
            String fechaIngresada = scanner.nextLine();

            if (fechaIngresada.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null; // Devuelve null o lanza una excepción según el diseño
            }

            try {
                // Convierte el string al formato correcto reemplazando los ":" por "-"
                String fechaFormateada = fechaIngresada.replace(":", "-");
                fecha = LocalDate.parse(fechaFormateada);

                if (fecha.isBefore(LocalDate.now())) {
                    System.out.println("Error: La fecha debe ser en el futuro.");
                } else if (fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    System.out.println("Error: No se pueden seleccionar turnos en domingo.");
                } else {
                    valido = true;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido. Por favor, use el formato YYYY:MM:DD");
            }
        }
        return fecha;
    }

    ///retorna una lista con los horarios disponibles x fecha especifica y servicio
    public List<LocalTime> mostrarTurnosDisponiblesXfecha(String fecha, String cod_servicio, TipoServicio tipoServicio) {
        List<Turno> turnosReservados = obtenerTurnosReservadosXfecha(fecha);///retorna la lista de turnos reservados para una fecha

        List<LocalTime> horariosDisponibles = new ArrayList<>();///en esta lista guardamos horarios disponibles

        LocalTime horaInicio = LocalTime.of(9, 0);  // 9 a.m.
        LocalTime horaFin = LocalTime.of(18, 0);    // 6 p.m.

        ////agregar metodo que busque el servicio por codigo
        int i = 0; ///para el indice de horarios disponibles
        long hora = 0;
        long minutos = 0;
        LocalTime duracion = null;
        try {
            switch (tipoServicio) {
                case TipoServicio.DEPILACION:
                 // duracion = Turno.convertirStringALocalTime(gestorDepilacion.buscarPorCodigo(cod_servicio).getDuracion());
                   // con el de arriba tiraba error y con el de abajo noo
                    duracion = ConvertirFechaHoras.convertirStringALocalTime(gestorDepilacion.buscarPorCodigo(cod_servicio).getDuracion());
                    break;
                case TipoServicio.MANICURA:
                    //duracion = Turno.convertirStringALocalTime(gestorManicura.buscarPorCodigo(cod_servicio).getDuracion());
                    duracion = ConvertirFechaHoras.convertirStringAHora(gestorManicura.buscarPorCodigo(cod_servicio).getDuracion());

                    break;
                case TipoServicio.PESTANIAS:
                   // duracion = Turno.convertirStringALocalTime(gestorPestania.buscarPorCodigo(cod_servicio).getDuracion());
                    duracion = ConvertirFechaHoras.convertirStringAHora(gestorPestania.buscarPorCodigo(cod_servicio).getDuracion());

                    break;
            }

            hora = (long) duracion.getHour();
            minutos = (long) duracion.getMinute();

        } catch (CodigoNoEncontradoException  e) {
            System.out.println(e.getMessage());
        }


        System.out.println("Turnos disponibles del dia: " + fecha);
        while (horaInicio.isBefore(horaFin)) {///recorre todos los horarios

            if (!siEstaHorario(turnosReservados, horaInicio, cod_servicio)) {
                //si te retorna true es porque ese horario ya esta reservado, si da false lo muestra como horario disponible
                System.out.println(i + "- " + horaInicio);
                i++;
                horariosDisponibles.add(horaInicio);
            }
            // Avanzamos la duracion del sevicio para el siguiente turno
            horaInicio = horaInicio.plusHours(hora).plusMinutes(minutos);
        }
        if (i == 0) {
            System.out.println("No hay turnos disponibles");
        }
        return horariosDisponibles;
    }

    /// retorna lista de turnos RESERVADOS por fecha específica
    public List<Turno> obtenerTurnosReservadosXfecha(String fecha) {
        return listaTurnos.obtener(fecha);
    }

    ///retorna true si el horario está ocupado por un turno del mismo servicio
    public boolean siEstaHorario(List<Turno> turnos, LocalTime horario, String cod_servicio) {
        if (turnos == null || turnos.isEmpty()) {
            return false;
        }

        for (Turno t : turnos) {
            if (t.getHorario().equals(horario) && t.getCodigo_servicio().equals(cod_servicio)) {
                return true;
            }
        }
        return false;
    }

/////////////////////////////////////////////MANEJO DE PROFESIONALES!!!!////////////////////////////////////////

    ///devuelve el DNI del profesional
///filtra por servicio, por horario y fecha
    /*public String pedirDNIprofesionalXservicio(String codServicio, String horario, String fecha) {

        if (gestorProfesional.getProfesionales() == null || gestorProfesional.getProfesionales().isEmpty()) {
            System.out.println("No hay profesionales disponibles.");
            return null;
        }

        int opc = 0;
        List<Profesional> disponibles = new ArrayList<>();

        while (true) {
            System.out.println("Profesionales disponibles:");
            for (int i = 0; i < gestorProfesional.getProfesionales().size(); i++) {

                if (gestorProfesional.getProfesionales().get(i).verificarProfesion(codServicio) && verificarProfesionalXhorario(gestorProfesional.getProfesionales().get(i).getDni(), horario, fecha)) {
                    System.out.println(opc + "- " + gestorProfesional.getProfesionales().get(i).getNombre() + " " + gestorProfesional.getProfesionales().get(i).getApellido());
                    disponibles.add(gestorProfesional.getProfesionales().get(i));
                    opc++;
                }
            }
            try {
                System.out.println("OPCION: (o escriba 'salir' para cancelar) ");
                String opcElegida = scanner.nextLine();


                if (opcElegida.equalsIgnoreCase("salir")) {
                    System.out.println("Operación cancelada por el usuario.");
                    return null;
                }

                ///pasa a int un string
                opc = Integer.parseInt(opcElegida);
                if (opc < 0 || opc > gestorProfesional.getProfesionales().size()) {
                    System.out.println("Selección inválida. Inténtelo de nuevo.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada no valida. Por favor ingrese un número.");
                scanner.nextLine();
            }catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número.");
            }
        }
        return gestorProfesional.getProfesionales().get(opc).getDni();
    }*/

    public String pedirDNIprofesionalXservicio(String codServicio, String horario, String fecha) {

        if (gestorProfesional.getProfesionales() == null || gestorProfesional.getProfesionales().isEmpty()) {
            System.out.println("No hay profesionales disponibles.");
            return null;
        }

        List<Profesional> disponibles = new ArrayList<>();

        // Filtrar profesionales disponibles
        for (Profesional profesional : gestorProfesional.getProfesionales()) {
            if (profesional.verificarProfesion(codServicio) &&
                    verificarProfesionalXhorario(profesional.getDni(), horario, fecha)) {
                disponibles.add(profesional);
            }
        }

        if (disponibles.isEmpty()) {
            System.out.println("No hay profesionales disponibles para este servicio, horario o fecha.");
            return null;
        }

        // Mostrar profesionales disponibles
        while (true) {
            System.out.println("Profesionales disponibles:");
            for (int i = 0; i < disponibles.size(); i++) {
                Profesional profesional = disponibles.get(i);
                System.out.println(i + "- " + profesional.getNombre() + " " + profesional.getApellido());
            }

            // Solicitar opción al usuario
            System.out.println("OPCIÓN: (o escriba 'salir' para cancelar) ");
            String opcElegida = scanner.nextLine();

            if (opcElegida.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada por el usuario.");
                return null;
            }

            try {
                int opc = Integer.parseInt(opcElegida);
                if (opc < 0 || opc >= disponibles.size()) {
                    System.out.println("Selección inválida. Inténtelo de nuevo.");
                } else {
                    return disponibles.get(opc).getDni(); // Retorna el DNI del profesional seleccionado
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor ingrese un número.");
            }
        }
    }

    public boolean verificarProfesionalXhorario(String dniProfesional, String horario, String fecha) {
        List<Turno> turnos = obtenerTurnosReservadosXfecha(fecha);
        if (turnos != null) {
            for (Turno t : turnos) {
                if (t.getDni_profesional().equals(dniProfesional) && t.getHorario().equals(horario)) {
                    return false; ///retorna falso si el profesional tiene un turno con el mismo horario solicitado
                }
            }
        } else {
            return true;
        }

        return true;//si no hay ningun turno con el mismo horario y profesional retorna true
    }

    /////////////////////////////////////////////MANEJO DE ARCHIVO TURNOS!!!!////////////////////////////////////////
    public void guardarTurnosEnArchivo() {
        try (FileWriter writer = new FileWriter(archivoTurnos)) {
            // Serializar el mapa de turnos a JSON
            gson.toJson(listaTurnos, writer);
            System.out.println("Turnos guardados correctamente en el archivo JSON.");
        } catch (IOException e) {
            System.out.println("Error al guardar los turnos: " + e.getMessage());
        }
    }

    public void cargarTurnosDesdeArchivo() {
        try (FileReader reader = new FileReader(archivoTurnos)) {
            // Leer y deserializar el archivo JSON al mapa de turnos
            Type tipoMapa = new TypeToken<MapaGenerico<String, List<Turno>>>() {
            }.getType();
            listaTurnos = gson.fromJson(reader, tipoMapa);

            if (listaTurnos == null) {
                listaTurnos = new MapaGenerico<>(); // En caso de que el archivo esté vacío
            }

            System.out.println("Turnos cargados correctamente desde el archivo JSON.");
        } catch (FileNotFoundException e) {
            System.out.println("Archivo JSON no encontrado. Se creará uno nuevo al guardar.");
            listaTurnos = new MapaGenerico<>();
        } catch (IOException e) {
            System.out.println("Error al leer los turnos: " + e.getMessage());
        }

    }


    public HashMap<String, List<Turno>> leerArchivoTurnos() {
/*
        try (FileReader reader = new FileReader(archivoTurnos)) {
            // Deserializar como HashMap<String, List<Turno>>
            Type tipoMapa = new TypeToken<HashMap<String, List<Turno>>>() {}.getType();

            // Convertir las claves de String a LocalDate
            HashMap<LocalDate, List<Turno>> listaTurnos = new HashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD");

            for (Map.Entry<String, List<Turno>> entry : listaTurnos.entrySet()) {
                LocalDate fecha = LocalDate.parse(entry.getKey(), formatter);
                listaTurnos.put(fecha, entry.getValue());
            }
            return listaTurnos;
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo");
            return new HashMap<>(); // Devuelve un mapa vacío en caso de error
        }*/
        return null


                ;
    }
  // le saque el parametro (HashMap<String, List<Turno>> listaTurnos) si mal no entiendo guarda la lista de la clase ya
    public void guardarEnArchivoTurnos() {
        // Crear instancia de Gson para convertir a JSON con formato bonito
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter("turnos.json")) {


            // Convertir la lista de TurnoArchivo a formato JSON
            String json = gson.toJson(listaTurnos);

            // Escribir el JSON en el archivo
            writer.write(json);

        } catch (IOException e) {
            System.err.println("Error al guardar el archivo: " + e.getMessage());
        }
    }
/*
    public List<TurnoArchivo> convertirMapa(HashMap<LocalDate, List<Turno>> mapa) {
        List<TurnoArchivo> turnosConvertidos = new ArrayList<>();

        // Recorrer todo el mapa
        for (List<Turno> turnos : mapa.values()) {
            // Recorrer cada Turno en la lista y convertirlo
            for (Turno turno : turnos) {
                TurnoArchivo turnoConvertido = convertirTurno(turno);
                turnosConvertidos.add(turnoConvertido);
            }
        }
        return turnosConvertidos;
    }

    public TurnoArchivo convertirTurno(Turno turnoOriginal) {
        // Formateadores para fecha y hora
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

        // Convertir LocalDate y LocalTime a String
        String fecha = turnoOriginal.getFecha().format(formatoFecha);
        String horario = turnoOriginal.getHorario().format(formatoHora);

        // Crear y retornar el nuevo objeto TurnoArchivo
        return new TurnoArchivo(
                turnoOriginal.getCod_turno(),
                fecha,
                horario,
                turnoOriginal.getCodigo_servicio(),
                turnoOriginal.getDni_profesional(),
                turnoOriginal.getDni_cliente()
        );
    }*/
////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////

    public MapaGenerico<String, List<Turno>> getListaTurnos() {
        return listaTurnos;
    }

    public void setListaTurnos(HashMap<String, List<Turno>> listaTurnos) {
        this.listaTurnos.setMapa(listaTurnos);
    }
}
