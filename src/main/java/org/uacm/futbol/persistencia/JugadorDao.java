package org.uacm.futbol.persistencia;

import jakarta.persistence.*;
import org.uacm.futbol.modelo.Jugador;

public class JugadorDao extends GenericJpaDao<Jugador> {
    public JugadorDao(EntityManager em) {
        super(em, Jugador.class);
    }
}
