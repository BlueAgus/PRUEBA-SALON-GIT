package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ConvertirFechaHoras {

    public static String convertirFechaAString(LocalDate fecha) {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Puedes ajustar el formato según necesites
        return fecha.format(formatoFecha);
    }
    public static String convertirHoraAString(LocalTime hora) {
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss"); // Puedes ajustar el formato según necesites
        return hora.format(formatoHora);
    }

    public static LocalDate convertirStringAFecha(String fecha) {
        try {
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(fecha, formatoFecha);
        } catch (DateTimeParseException e) {
            System.out.println("Error al convertir la fecha: " + e.getMessage());
            return null; // O lanzar una excepción personalizada si lo prefieres
        }
    }

    public static LocalTime convertirStringAHora(String hora) {
        try {
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
            return LocalTime.parse(hora, formatoHora);
        } catch (DateTimeParseException e) {
            System.out.println("Error al convertir la hora: " + e.getMessage());
            return null; // O lanzar una excepción personalizada
        }
    }

    public static LocalTime convertirStringALocalTime(String timeString) {
        try {
            // Asegúrate de que no haya espacios adicionales
            timeString = timeString.trim();
            return LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Error al convertir el string a LocalTime: " + timeString, e);
        }
    }


}

/*
public static LocalDate convertirStringAFecha(String fecha) {
    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // El mismo formato que usaste para la fecha
    return LocalDate.parse(fecha, formatoFecha);
}

public static LocalTime convertirStringAHora(String hora) {
    DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss"); // El mismo formato que usaste para la hora
    return LocalTime.parse(hora, formatoHora);
}*/