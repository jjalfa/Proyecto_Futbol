package org.uacm.futbol.persistencia;

import jakarta.persistence.*;
import org.uacm.futbol.modelo.Equipo;

public class EquipoDao extends GenericJpaDao<Equipo> {
    public EquipoDao(EntityManager em) {
        super(em, Equipo.class);
    }
}