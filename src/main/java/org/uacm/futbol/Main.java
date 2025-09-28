package org.uacm.futbol;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.uacm.futbol.modelo.Equipo;
import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.modelo.Posicion;
import org.uacm.futbol.servicios.EquipoService;
import org.uacm.futbol.servicios.EquipoServiceImpl;
import org.uacm.futbol.servicios.JugadorService;
import org.uacm.futbol.servicios.JugadorServiceimpl;

import java.time.LocalDate;
import java.util.Date;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BaseFutbol");
        EntityManager em = emf.createEntityManager();
        EquipoService equipoService = new EquipoServiceImpl(em);
        JugadorService jugadorService = new JugadorServiceimpl(em);

        Equipo e = new Equipo();
        e.setNombre("FC America");
        Equipo creado = equipoService.crear(e);
        System.out.println("Equipo creado con id: " + creado.getId());

        Jugador j = new Jugador();
        j.setNombre("ronaldo");
        LocalDate fechaNac = LocalDate.parse("2000-05-12");
        j.setFecha_nacimiento(fechaNac);
        j.setEquipo(creado);
        jugadorService.crear(j);

        em.close();
        emf.close();


    }
}