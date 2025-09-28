package org.uacm.futbol.modelo;
import jakarta.persistence.*;
import java.util.Date;
@Entity
@Table(name = "partidos")
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partido")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_equipo_local")
    private Equipo equipo_local;
    @ManyToOne
    @JoinColumn(name = "id_equipo_visitante")
    private Equipo equipo_visitante;
    @Column(name = "goles_visitante")
    private int goles_visitantes;

    @Column(name = "goles_local")
    private int goles_locales;
    @Column(name = "fecha_partido")
    private Date fecha_partido;

    public Partido(Equipo equipo_local, Equipo equipo_visitante, int goles_visitantes, int goles_locales, Date fecha_partido) {
        this.equipo_local = equipo_local;
        this.equipo_visitante = equipo_visitante;
        this.goles_visitantes = goles_visitantes;
        this.goles_locales = goles_locales;
        this.fecha_partido = fecha_partido;
    }
    public Partido(){}

    public Equipo getEquipo_local() {
        return equipo_local;
    }

    public void setEquipo_local(Equipo equipo_local) {
        this.equipo_local = equipo_local;
    }

    public Equipo getEquipo_visitante() {
        return equipo_visitante;
    }

    public void setEquipo_visitante(Equipo equipo_visitante) {
        this.equipo_visitante = equipo_visitante;
    }

    public int getGoles_visitantes() {
        return goles_visitantes;
    }

    public void setGoles_visitantes(int goles_visitantes) {
        this.goles_visitantes = goles_visitantes;
    }

    public int getGoles_locales() {
        return goles_locales;
    }

    public void setGoles_locales(int goles_locales) {
        this.goles_locales = goles_locales;
    }

    public Date getFecha_partido() {
        return fecha_partido;
    }

    public void setFecha_partido(Date fecha_partido) {
        this.fecha_partido = fecha_partido;
    }
}
