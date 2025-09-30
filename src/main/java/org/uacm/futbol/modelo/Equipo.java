package org.uacm.futbol.modelo;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "equipos")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Jugador> jugadores;

    public Equipo(String nombre) {
        this.nombre = nombre;
    }
    public Equipo(){}

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

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }
    @Override
    public String toString() {
        return nombre != null ? nombre : "Equipo #" + (id != null ? id : "new");
    }
}
