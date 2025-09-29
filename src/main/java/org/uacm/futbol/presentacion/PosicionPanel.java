package org.uacm.futbol.presentacion;


import org.uacm.futbol.modelo.Posicion;
import org.uacm.futbol.servicios.PosicionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PosicionPanel extends JPanel {
    private final PosicionService posicionService;
    private final DefaultTableModel model;
    private final JTable table;

    public PosicionPanel(PosicionService posicionService) {
        this.posicionService = posicionService;
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnCrear = new JButton("Crear");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        botones.add(btnCrear); botones.add(btnEditar); botones.add(btnEliminar); botones.add(btnRefrescar);
        add(botones, BorderLayout.SOUTH);

        btnCrear.addActionListener(e -> crear());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnRefrescar.addActionListener(e -> cargar());

        cargar();
    }

    private void cargar() {
        model.setRowCount(0);
        List<Posicion> lista = posicionService.listarTodos();
        for (Posicion p : lista) model.addRow(new Object[]{p.getId(), p.getNombre()});
    }

    private void crear() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre de la posición:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            Posicion p = new Posicion(nombre.trim());
            posicionService.crear(p);
            cargar();
        }
    }

    private void editar() {
        int r = table.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Selecciona una posición"); return; }
        Long id = (Long) model.getValueAt(r, 0);
        Posicion p = posicionService.buscarPorId(id);
        String nuevo = JOptionPane.showInputDialog(this, "Nuevo nombre:", p.getNombre());
        if (nuevo != null && !nuevo.trim().isEmpty()) {
            p.setNombre(nuevo.trim());
            posicionService.actualizar(p);
            cargar();
        }
    }

    private void eliminar() {
        int r = table.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Selecciona una posición"); return; }
        Long id = (Long) model.getValueAt(r, 0);
        if (JOptionPane.showConfirmDialog(this, "Eliminar posición id=" + id + " ?","Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            posicionService.eliminar(id);
            cargar();
        }
    }
}

