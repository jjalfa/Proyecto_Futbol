package org.uacm.futbol.modelo;
import jakarta.persistence.*;
@Entity
@Table(name = "goles")
public class Gol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gol")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_partido")
    private Partido partido;
    @ManyToOne
    @JoinColumn(name = "id_jugador")
    private Jugador jugador;
    @Column(name = "minuto")
    private int minuto;

    public Gol(Partido partido, Jugador jugador, int minuto) {
        this.partido = partido;
        this.jugador = jugador;
        this.minuto = minuto;
    }
    public Gol(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }
}
