package org.uacm.futbol.modelo;


import jakarta.persistence.*;

@Entity
@Table(name = "posiciones")
public class Posicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_posicion")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    public Posicion(String nombre) {
        this.nombre = nombre;
    }
    public Posicion(){}

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
    @Override
    public String toString() {
        return nombre != null ? nombre : "Posición #" + (id != null ? id : "new");
    }
}
