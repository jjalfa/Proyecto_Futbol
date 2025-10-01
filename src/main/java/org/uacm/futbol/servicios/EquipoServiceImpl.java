package org.uacm.futbol.servicios;

import org.uacm.futbol.modelo.Equipo;
import org.uacm.futbol.modelo.Gol;
import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.modelo.Partido;
import org.uacm.futbol.persistencia.EquipoDao;
import jakarta.persistence.*;

import java.util.List;

public class EquipoServiceImpl implements EquipoService {
    private final EquipoDao dao;
    private final EntityManager em;

    public EquipoServiceImpl(EntityManager em) {
        this.dao = new EquipoDao(em);
        this.em = em;
    }

    @Override
    public Equipo crear(Equipo equipo) { return dao.save(equipo); }
    @Override
    public Equipo actualizar(Equipo equipo) { return dao.save(equipo); }
    @Override
    public Equipo buscarPorId(Long id) { return dao.find(id); }
    @Override
    public List<Equipo> listarTodos() { return dao.findAll(); }
   /* @Override
    public void eliminar(Long id) {
        Equipo e = dao.find(id);
        if (e != null) dao.delete(e);
    }*/
    public void eliminarCascade(Long equipoId) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Equipo equipo = em.find(Equipo.class, equipoId);
            if (equipo == null) { tx.commit(); return; }

            //  Obtener jugadores del equipo
            List<Jugador> jugadores = em.createQuery("SELECT j FROM Jugador j WHERE j.equipo.id = :id", Jugador.class)
                    .setParameter("id", equipoId).getResultList();

            //  Para cada jugador: borrar sus goles y luego el jugador
            for (Jugador j : jugadores) {
                Jugador managedJ = em.find(Jugador.class, j.getId());
                // borrar goles del jugador y actualizar marcadores de partidos
                List<Gol> golesJugador = em.createQuery("SELECT g FROM Gol g WHERE g.jugador.id = :id", Gol.class)
                        .setParameter("id", managedJ.getId()).getResultList();
                for (Gol gol : golesJugador) {
                    Partido partido = gol.getPartido() != null ? em.find(Partido.class, gol.getPartido().getId()) : null;
                    em.remove(em.contains(gol) ? gol : em.merge(gol));
                    if (partido != null) {
                        if (managedJ.getEquipo() != null && partido.getEquipo_local() != null
                                && managedJ.getEquipo().getId().equals(partido.getEquipo_local().getId())) {
                            partido.setGoles_locales(Math.max(0, partido.getGoles_locales() - 1));
                        } else if (managedJ.getEquipo() != null && partido.getEquipo_visitante() != null
                                && managedJ.getEquipo().getId().equals(partido.getEquipo_visitante().getId())) {
                            partido.setGoles_visitantes(Math.max(0, partido.getGoles_visitantes() - 1));
                        }
                        em.merge(partido);
                    }
                }
                // borrar jugador
                em.remove(em.contains(managedJ) ? managedJ : em.merge(managedJ));
            }

            //  Borrar partidos donde el equipo participa (si se decide hacerlo)
            List<Partido> partidos = em.createQuery("SELECT p FROM Partido p WHERE p.equipo_local.id = :id OR p.equipo_visitante.id = :id", Partido.class)
                    .setParameter("id", equipoId).getResultList();
            for (Partido p : partidos) {
                Partido managedP = em.find(Partido.class, p.getId());
                // borrar goles del partido (si hay)
                List<Gol> golesPartido = em.createQuery("SELECT g FROM Gol g WHERE g.partido.id = :id", Gol.class)
                        .setParameter("id", managedP.getId()).getResultList();
                for (Gol gol : golesPartido) {
                    em.remove(em.contains(gol) ? gol : em.merge(gol));
                }
                // borrar el partido
                em.remove(managedP);
            }

            //  Finalmente borrar el equipo
            em.remove(em.contains(equipo) ? equipo : em.merge(equipo));

            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        }
    }

    @Override
    public void eliminar(Long id) {

        eliminarCascade(id);
    }

    @Override
    public long contarJugadores(Long equipoId) {
        return em.createQuery("SELECT COUNT(j) FROM Jugador j WHERE j.equipo.id = :id", Long.class)
                .setParameter("id", equipoId)
                .getSingleResult();
    }

    @Override
    public long contarPartidos(Long equipoId) {
        return em.createQuery(
                        "SELECT COUNT(p) FROM Partido p WHERE p.equipo_local.id = :id OR p.equipo_visitante.id = :id", Long.class)
                .setParameter("id", equipoId)
                .getSingleResult();
    }
}
