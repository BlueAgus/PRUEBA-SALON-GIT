package model;

import Interfaces.CrearID;
import Interfaces.IBuscarPorCodigo;
import abstractas.Servicio;
import excepciones.CodigoNoEncontradoException;
import excepciones.FacturaSinTurnosException;
import excepciones.TurnoExistenteException;
import excepciones.TurnoNoExistenteException;
import enumeraciones.*;
import gestores.GestorDepilacion;
import gestores.GestorManicura;
import gestores.GestorPestania;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static model.ConvertirFechaHoras.convertirFechaAString;
import static model.ConvertirFechaHoras.convertirHoraAString;

public class Factura implements CrearID {

    private String codigoFactura;
    private TipoDePago tipoPago;
    private double precioFinal = 0.0;
    private Cliente cliente;
    private List<Turno> turnosPorCliente;
    private double descuento;
    private double ajuste = 0.0;
    private String fecha;
    private String hora;
    private transient List<IBuscarPorCodigo<? extends Servicio>> gestores;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////

    public Factura(TipoDePago tipoPago, Cliente cliente) {

        this.codigoFactura = this.generarIDEunico(); // aca usamos el metodo de la interfaz directamente
        this.tipoPago = tipoPago;
        this.precioFinal = 0.0;
        this.descuento = 0.0;
        this.ajuste = 0.0;
        this.turnosPorCliente = new ArrayList<>();
        this.cliente = cliente;

        LocalDate fechaActual = LocalDate.now();
        this.fecha = convertirFechaAString(fechaActual);
        LocalTime horaActual = LocalTime.now();
        this.hora = convertirHoraAString(horaActual);
        this.gestores = Arrays.asList(new GestorDepilacion(), new GestorManicura(), new GestorPestania());
    }

    //////////////////////////////////////////////////////// metodos extr ////////////////////////////////////////////////////


    private Servicio buscarServicioEnGestores(String codigoServicio) throws CodigoNoEncontradoException {
        for (IBuscarPorCodigo<? extends Servicio> gestor : gestores) {
            try {
                return gestor.buscarPorCodigo(codigoServicio);
            } catch (CodigoNoEncontradoException e) {
                //si no lo encuentre sigue con el otro
            }
        }
        throw new CodigoNoEncontradoException("Servicio no encontrado en ningún gestor para el código: " + codigoServicio);
    }

    public String detallesDeServicios() {
        StringBuilder detalles = new StringBuilder();
        Map<TipoServicio, Integer> cantidadPorServicio = new HashMap<>();

        for (Turno turno : turnosPorCliente) {
            try {
                Servicio servicio = buscarServicioEnGestores(turno.getCodigo_servicio());
                TipoServicio tipoServicio = servicio.getTipoService();

                cantidadPorServicio.put(tipoServicio, cantidadPorServicio.getOrDefault(tipoServicio, 0) + 1);
            } catch (CodigoNoEncontradoException e) {
                detalles.append("Servicio no encontrado para el código: ").append(turno.getCodigo_servicio()).append("\n");
            }
        }
        for (Map.Entry<TipoServicio, Integer> entry : cantidadPorServicio.entrySet()) {
            detalles.append(entry.getKey().name().toLowerCase())
                    .append(" x")
                    .append(entry.getValue())
                    .append("\n");
        }
        return detalles.toString();
    }

    public double calcularPrecioFinal() {
        double precioBase = 0.0;

        for (Turno turno : turnosPorCliente) {
            try {
                Servicio servicio = buscarServicioEnGestores(turno.getCodigo_servicio());
                precioBase += servicio.calcularPrecio();
            } catch (CodigoNoEncontradoException e) {
                System.out.println("Servicio no encontrado para el código: " + turno.getCodigo_servicio());
            }
        }

        this.precioFinal = tipoPago.calcularPagoTotal(precioBase);
        this.ajuste = this.precioFinal - precioBase;
        return this.precioFinal;
    }

    public void agregarTurno(Turno turno) throws TurnoExistenteException {
        if (turnosPorCliente.contains(turno)) {
            throw new TurnoExistenteException("El turno ya está ingresado en la factura.");
        }
        turnosPorCliente.add(turno);
    }

    public void eliminarTurno(Turno turno) throws TurnoNoExistenteException, FacturaSinTurnosException {
        if (!turnosPorCliente.contains(turno)) {
            throw new TurnoNoExistenteException("El turno que desea eliminar en la factura no existe aqui.");
        }

        if (turnosPorCliente.size() == 1) {
            throw new FacturaSinTurnosException("La factura debe contener al menos un turno, en caso contrario eliminar la factura completa");
        }

        turnosPorCliente.remove(turno);
        System.out.println("El turno fue quitado de la factura final");

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Factura factura = (Factura) o;
        return Objects.equals(codigoFactura, factura.codigoFactura);
    }

    ////////////////////////////////////////////////////////GET Y SET ////////////////////////////////////////////////////
    public String getCodigoFactura() {
        return codigoFactura;
    }

    public TipoDePago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoDePago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public List<Turno> getTurnosPorCliente() {
        return turnosPorCliente;
    }

    public void setTurnosPorCliente(List<Turno> turnosPorCliente) {
        this.turnosPorCliente = turnosPorCliente;
    }

    public double getAjuste() {
        return ajuste;
    }

    public void setAjuste(double ajuste) {
        this.ajuste = ajuste;
    }

    public String getFecha() {
        return fecha;
    }


    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public List<IBuscarPorCodigo<? extends Servicio>> getGestores() {
        return gestores;
    }

    public void setGestores(List<IBuscarPorCodigo<? extends Servicio>> gestores) {
        this.gestores = gestores;
    }
    ////////////////////////////////////// TO STRING ////////////////////////////////////////////////////

    @Override
    public String toString() {
        return
                "| Detalles Factura: " +
                        "| Metodo de pago: " + tipoPago + "\n" +
                        "| Precio final : " + precioFinal + "\n" +
                        "| Descuento aplicado : " + descuento + "\n" +
                        "| Ajuste aplicado : " + mostrarAjuste() + "\n" +
                        "| Servicios aplicados : " + detallesDeServicios() + "\n" +
                        "| Datos del cliente : " + cliente.datosClienteSinGenero() + "\n" +
                        "| Fecha : " + fecha + "\n" +
                        "| Hora : " + hora+ "\n" +

                        "=========================================\n";
    }

    private String mostrarAjuste() {
        if (ajuste == 0) {
            return "Sin ajuste";
        }
        // aca muestra el signo del numero , si es posi o negativo
        return String.format("%+.2f", ajuste);
    }
}
