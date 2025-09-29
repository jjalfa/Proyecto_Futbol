package org.uacm.futbol.presentacion;



import org.uacm.futbol.modelo.Equipo;
import org.uacm.futbol.servicios.EquipoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EquipoPanel extends JPanel {
    private final EquipoService equipoService;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public EquipoPanel(EquipoService equipoService) {
        this.equipoService = equipoService;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnCrear = new JButton("Crear");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");

        botones.add(btnCrear);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnRefrescar);
        add(botones, BorderLayout.SOUTH);

        btnCrear.addActionListener(e -> crearEquipo());
        btnEditar.addActionListener(e -> editarEquipo());
        btnEliminar.addActionListener(e -> eliminarEquipo());
        btnRefrescar.addActionListener(e -> cargarEquipos());

        cargarEquipos();
    }

    private void cargarEquipos() {
        tableModel.setRowCount(0);
        List<Equipo> equipos = equipoService.listarTodos();
        for (Equipo eq : equipos) {
            tableModel.addRow(new Object[]{eq.getId(), eq.getNombre()});
        }
    }

    private void crearEquipo() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del equipo:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            Equipo e = new Equipo(nombre.trim());
            equipoService.crear(e);
            cargarEquipos();
        }
    }

    private void editarEquipo() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecciona un equipo"); return; }
        Long id = (Long) tableModel.getValueAt(row, 0);
        Equipo e = equipoService.buscarPorId(id);
        String nuevo = JOptionPane.showInputDialog(this, "Nuevo nombre:", e.getNombre());
        if (nuevo != null && !nuevo.trim().isEmpty()) {
            e.setNombre(nuevo.trim());
            equipoService.actualizar(e);
            cargarEquipos();
        }
    }

    private void eliminarEquipo() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecciona un equipo"); return; }
        Long id = (Long) tableModel.getValueAt(row, 0);
        int r = JOptionPane.showConfirmDialog(this, "Eliminar equipo id=" + id + " ?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            equipoService.eliminar(id);
            cargarEquipos();
        }
    }
}
