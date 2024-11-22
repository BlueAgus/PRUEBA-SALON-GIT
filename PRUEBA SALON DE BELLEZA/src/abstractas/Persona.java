package abstractas;

public abstract class Persona {
    /// lo cambio aca y en servicio por portected para que las clases hijas accedan directamente
    protected String nombre;
    protected String apellido;
    protected String dni;
    protected String genero;
    protected String telefono;

    //////////////////////////////////////////////////////// CONSTRUCTOR ////////////////////////////////////////////////////
    public Persona(){

    }


    public Persona(String nombre, String apellido, String dni, String genero, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.genero = genero;
        this.telefono = telefono;
    }

//////////////////////////////////////GETTERS Y SETTERS//////////////////////////////////////////////////////////////////////////////////

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() { return genero; }

    public void setGenero(String genero) { this.genero = genero; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) {  this.telefono = telefono; }


    //////////////////////////////////////////////////////// TO STRING ////////////////////////////////////////////////////
    @Override
    public String toString() {
        return
                "| Nombre : " + nombre + "\n" +
                        "| Apellido : " + apellido + "\n" +
                        "| DNI : " + dni + "\n" +
                        "| Genero : " + genero + "\n" +
                        "| Telefono : " + telefono + "\n" +
                        "=========================================\n";
    }

    public String datosClienteSinGenero(){
        return "------------------"+
                "Nombre: "+ getNombre()+"\n"+
                "Apellido: "+ getApellido()+"\n"+
                "DNI: "+ getDni()+"\n"+
                "Telefono: " + getTelefono() +"\n"+
                "-----------------\n";
    }
}
