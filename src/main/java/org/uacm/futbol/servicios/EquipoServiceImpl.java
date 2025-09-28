package org.uacm.futbol.servicios;

import org.uacm.futbol.modelo.Equipo;
import org.uacm.futbol.persistencia.EquipoDao;
import jakarta.persistence.*;

import java.util.List;

public class EquipoServiceImpl implements EquipoService {
    private final EquipoDao dao;

    public EquipoServiceImpl(EntityManager em) {
        this.dao = new EquipoDao(em);
    }

    @Override
    public Equipo crear(Equipo equipo) { return dao.save(equipo); }
    @Override
    public Equipo actualizar(Equipo equipo) { return dao.save(equipo); }
    @Override
    public Equipo buscarPorId(Long id) { return dao.find(id); }
    @Override
    public List<Equipo> listarTodos() { return dao.findAll(); }
    @Override
    public void eliminar(Long id) {
        Equipo e = dao.find(id);
        if (e != null) dao.delete(e);
    }
}
