package org.uacm.futbol.servicios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.uacm.futbol.modelo.Gol;
import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.modelo.Partido;
import org.uacm.futbol.persistencia.JugadorDao;


import java.util.List;

public class JugadorServiceimpl implements JugadorService{
    private final JugadorDao dao;
    private final EntityManager em;

    public JugadorServiceimpl(EntityManager em) {
        this.dao = new JugadorDao(em);
        this.em = em;
    }

    @Override
    public Jugador crear(Jugador jugador) { return dao.save(jugador); }
    @Override
    public Jugador actualizar(Jugador jugador) { return dao.save(jugador); }
    @Override
    public Jugador buscarPorId(Long id) { return dao.find(id); }
    @Override
    public List<Jugador> listarTodos() { return dao.findAll(); }
    /*@Override
    public void eliminar(Long id) {
        Jugador e = dao.find(id);
        if (e != null) dao.delete(e);
    }*/
    public void eliminarCascade(Long jugadorId) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Jugador jugador = em.find(Jugador.class, jugadorId);
            if (jugador == null) {
                tx.commit();
                return;
            }

            //  Borrar Goles del jugador y actualizar marcador del partido correspondiente
            List<Gol> goles = em.createQuery("SELECT g FROM Gol g WHERE g.jugador.id = :id", Gol.class)
                    .setParameter("id", jugadorId).getResultList();

            for (Gol gol : goles) {
                Partido partido = gol.getPartido() != null ? em.find(Partido.class, gol.getPartido().getId()) : null;

                // eliminar gol
                em.remove(em.contains(gol) ? gol : em.merge(gol));

                // actualizar marcador si corresponde
                if (partido != null) {
                    Long idEquipoJugador = jugador.getEquipo() != null ? jugador.getEquipo().getId() : null;
                    if (idEquipoJugador != null) {
                        if (partido.getEquipo_local() != null && idEquipoJugador.equals(partido.getEquipo_local().getId())) {
                            partido.setGoles_locales(Math.max(0, partido.getGoles_locales() - 1));
                        } else if (partido.getEquipo_visitante() != null && idEquipoJugador.equals(partido.getEquipo_visitante().getId())) {
                            partido.setGoles_visitantes(Math.max(0, partido.getGoles_visitantes() - 1));
                        }
                        em.merge(partido);
                    }
                }
            }

            //  Ahora eliminar el jugador
            em.remove(em.contains(jugador) ? jugador : em.merge(jugador));
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
    public long contarGoles(Long jugadorId) {
        return em.createQuery("SELECT COUNT(g) FROM Gol g WHERE g.jugador.id = :id", Long.class)
                .setParameter("id", jugadorId)
                .getSingleResult();
    }

}
