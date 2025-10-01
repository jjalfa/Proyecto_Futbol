package org.uacm.futbol.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "jugadores")
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jugador")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_equipo")
    private Equipo equipo;
    @ManyToOne
    @JoinColumn(name = "id_posicion")
    private Posicion posicion;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "fecha_nacimiento")
    private LocalDate fecha_nacimiento;

   /* @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gol> goles = new ArrayList<>();*/

    public Jugador(LocalDate fecha_nacimiento,Equipo equipo,Posicion posicion,String nombre) {
        this.equipo = equipo;
        this.posicion = posicion;
        this.nombre = nombre;
        this.fecha_nacimiento = fecha_nacimiento;
    }
    public Jugador(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
    @Override
    public String toString() {
        String pos = (posicion != null) ? posicion.getNombre() : "Sin posición";
        String eq = (equipo != null) ? equipo.getNombre() : "Sin equipo";
        return nombre + " (" + pos + ", " + eq + ")";
    }

}
