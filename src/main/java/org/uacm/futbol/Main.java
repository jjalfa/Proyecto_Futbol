package org.uacm.futbol;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.uacm.futbol.modelo.Equipo;
import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.modelo.Posicion;
import org.uacm.futbol.servicios.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BaseFutbol");
        EntityManager em = emf.createEntityManager();
        EquipoService equipoService = new EquipoServiceImpl(em);
        JugadorService jugadorService = new JugadorServiceimpl(em);
        PosicionService posicionService= new PosicionServiceimpl(em);
    /*
       try {
            System.out.println("=== CREAR EQUIPOS ===");
            Equipo e1 = new Equipo("Club Porcinos FC");
            e1 = equipoService.crear(e1);
            System.out.println("Equipo creado id=" + e1.getId() + " nombre=" + e1.getNombre());

            Equipo e2 = new Equipo("Deportiva Azul");
            e2 = equipoService.crear(e2);
            System.out.println("Equipo creado id=" + e2.getId()+ " nombre=" + e2.getNombre());

            System.out.println("=== CREAR POSICION ===");
            Posicion p1 = new Posicion("medio-portero");
            p1 = posicionService.crear(p1);
            System.out.println("Posicion creada :"+ p1.getNombre());
            System.out.println("\n=== LISTAR POSICIONES===");
            List<Posicion> posiciones = posicionService.listarTodos();
            posiciones.forEach(eq -> System.out.println(eq.getId() + " - " + eq.getNombre()));

            System.out.println("\n=== LISTAR EQUIPOS ===");
            List<Equipo> equipos = equipoService.listarTodos();
            equipos.forEach(eq -> System.out.println(eq.getId() + " - " + eq.getNombre()));

            System.out.println("\n=== ACTUALIZAR EQUIPO ===");
            e1.setNombre("Club Prueba United");
            equipoService.actualizar(e1);
            System.out.println("Nombre actualizado: " + equipoService.buscarPorId(e1.getId()).getNombre());

            System.out.println("\n=== LISTAR EQUIPOS ACTUALIZADO===");
            List<Equipo> equipos2 = equipoService.listarTodos();
            equipos2.forEach(eq -> System.out.println(eq.getId() + " - " + eq.getNombre()));


            System.out.println("\n=== CREAR JUGADOR (asociando equipo existente) ===");
            Jugador j1 = new Jugador();
            j1.setNombre("Juan Marquez");
            j1.setFecha_nacimiento(LocalDate.parse("2000-05-12"));
            Posicion posicionj1 = posicionService.buscarPorId(3L);
            // Asociar equipo ya persistido (obtenido del servicio)
            Equipo equipoExistente = equipoService.buscarPorId(e2.getId());
            j1.setEquipo(equipoExistente);
            j1.setPosicion(posicionj1);
            j1 = jugadorService.crear(j1);
            System.out.println("Jugador creado id=" + j1.getId() + " equipo=" + j1.getEquipo().getNombre());

            System.out.println("\n=== LISTAR JUGADORES ===");
            List<Jugador> jugadores = jugadorService.listarTodos();
            jugadores.forEach(j -> System.out.println(j.getId() + " - " + j.getNombre()
                    + " - " + j.getPosicion().getNombre() + " - equipo: " + (j.getEquipo() != null ? j.getEquipo().getNombre() : "null")));

            System.out.println("\n=== ACTUALIZAR JUGADOR (cambiar posicion) ===");
            Posicion p = posicionService.buscarPorId(2L);
            j1.setPosicion(p);
            Jugador a =jugadorService.actualizar(j1);
            System.out.println("Jugador actualizado: " + a.getPosicion().getNombre());

            System.out.println("\n=== BORRAR JUGADOR ===");
            jugadorService.eliminar(j1.getId());
            System.out.println("Jugador eliminado.");

            System.out.println("\n=== BORRAR EQUIPOS ===");
            equipoService.eliminar(e1.getId());
            equipoService.eliminar(e2.getId());
            System.out.println("Equipos eliminados.");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
       */
    }
}


