package org.uacm.futbol.persistencia;

import jakarta.persistence.EntityManager;
import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.modelo.Posicion;

public class PosicionDao extends GenericJpaDao<Posicion>{
    public PosicionDao(EntityManager em) {
        super(em, Posicion.class);
    }
}
