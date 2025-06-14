package esfe.dominio;

public class Evento {
    private int id;
    private String nombre;
    private String lugar;

    public Evento(int id, String nombre, String lugar) {
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
    }

    public Evento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}