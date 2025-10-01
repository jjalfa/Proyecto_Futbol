package org.uacm.futbol.persistencia;

import jakarta.persistence.*;
import org.uacm.futbol.modelo.Jugador;

import java.util.List;

public class JugadorDao extends GenericJpaDao<Jugador> {
    public JugadorDao(EntityManager em) {
        super(em, Jugador.class);
    }

    public List<Jugador> findByEquipoId(Long equipoId) {
        return em.createQuery("SELECT j FROM Jugador j WHERE j.equipo.id = :id", Jugador.class)
                .setParameter("id", equipoId)
                .getResultList();
    }
}
