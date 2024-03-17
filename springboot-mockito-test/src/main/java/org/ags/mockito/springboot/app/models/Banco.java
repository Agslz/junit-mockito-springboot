package org.ags.mockito.springboot.app.models;

public class Banco {

    private Long id;
    private String nombre;
    private int totalTrasnferencias;

    public Banco() {
    }

    public Banco(Long id, String nombre, int totalTrasnferencias) {
        this.id = id;
        this.nombre = nombre;
        this.totalTrasnferencias = totalTrasnferencias;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTotalTrasnferencias() {
        return totalTrasnferencias;
    }

    public void setTotalTrasnferencias(int totalTrasnferencias) {
        this.totalTrasnferencias = totalTrasnferencias;
    }
}
