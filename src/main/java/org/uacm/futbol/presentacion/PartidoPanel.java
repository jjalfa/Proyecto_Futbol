package org.uacm.futbol.presentacion;


import org.uacm.futbol.modelo.Equipo;
import org.uacm.futbol.modelo.Partido;
import org.uacm.futbol.servicios.EquipoService;
import org.uacm.futbol.servicios.PartidoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

public class PartidoPanel extends JPanel {
    private final PartidoService partidoService;
    private final EquipoService equipoService;
    private final DefaultTableModel model;
    private final JTable table;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PartidoPanel(PartidoService partidoService, EquipoService equipoService) {
        this.partidoService = partidoService;
        this.equipoService = equipoService;
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID","Fecha","Local","Visitante","Goles L","Goles V"}, 0){
            @Override public boolean isCellEditable(int r, int c){ return false;}
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        botones.add(btnNuevo); botones.add(btnEditar); botones.add(btnEliminar); botones.add(btnRefrescar);
        add(botones, BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> mostrarDialogoPartido(null));
        btnEditar.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Selecciona un partido"); return; }
            Long id = (Long) model.getValueAt(r,0);
            Partido p = partidoService.buscarPorId(id);
            mostrarDialogoPartido(p);
        });
        btnEliminar.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Selecciona un partido"); return; }
            Long id = (Long) model.getValueAt(r,0);
            if (JOptionPane.showConfirmDialog(this, "Eliminar partido id=" + id + " ?","Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                partidoService.eliminar(id);
                cargar();
            }
        });
        btnRefrescar.addActionListener(e -> cargar());

        cargar();
    }

    private void cargar() {
        model.setRowCount(0);
        List<Partido> lista = partidoService.listarTodos();
        for (Partido p : lista) {
            String fecha = p.getFecha_partido() != null ? p.getFecha_partido().format(dtf) : "";
            String local = p.getEquipo_local() != null ? p.getEquipo_local().getNombre() : "N/D";
            String visita = p.getEquipo_visitante() != null ? p.getEquipo_visitante().getNombre() : "N/D";
            model.addRow(new Object[]{p.getId(), fecha, local, visita, p.getGoles_locales(), p.getGoles_visitantes()});
        }
    }

    private void mostrarDialogoPartido(Partido p) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        final JDialog dialog;
        String title = (p == null) ? "Nuevo Partido" : "Editar Partido";
        if (owner instanceof Frame) dialog = new JDialog((Frame) owner, title, true);
        else if (owner instanceof Dialog) dialog = new JDialog((Dialog) owner, title, true);
        else dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(480,300);
        dialog.setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(); c.insets = new Insets(6,6,6,6);

        c.gridx=0; c.gridy=0; c.anchor=GridBagConstraints.EAST; panel.add(new JLabel("Fecha (YYYY-MM-DD HH:mm):"), c);
        c.gridx=1; c.gridy=0; c.anchor=GridBagConstraints.WEST; JTextField txtFecha = new JTextField(16); panel.add(txtFecha, c);

        c.gridx=0; c.gridy=1; c.anchor=GridBagConstraints.EAST; panel.add(new JLabel("Equipo Local:"), c);
        c.gridx=1; c.gridy=1; c.anchor=GridBagConstraints.WEST;
        Vector<Equipo> equiposVec = new Vector<>(equipoService.listarTodos());
        JComboBox<Equipo> cmbLocal = new JComboBox<>(equiposVec);
        panel.add(cmbLocal, c);

        c.gridx=0; c.gridy=2; c.anchor=GridBagConstraints.EAST; panel.add(new JLabel("Equipo Visitante:"), c);
        c.gridx=1; c.gridy=2; c.anchor=GridBagConstraints.WEST;
        JComboBox<Equipo> cmbVisit = new JComboBox<>(equiposVec);
        panel.add(cmbVisit, c);

        // precargar si editar
        if (p != null) {
            if (p.getFecha_partido() != null) txtFecha.setText(p.getFecha_partido().format(dtf));
            if (p.getEquipo_local() != null) cmbLocal.setSelectedItem(p.getEquipo_local());
            if (p.getEquipo_visitante() != null) cmbVisit.setSelectedItem(p.getEquipo_visitante());
        }

        JPanel botones = new JPanel();
        JButton ok = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(ok); botones.add(cancelar);

        ok.addActionListener(a -> {
            String fechaStr = txtFecha.getText().trim();
            Equipo local = (Equipo) cmbLocal.getSelectedItem();
            Equipo visitante = (Equipo) cmbVisit.getSelectedItem();
            if (local == null || visitante == null) { JOptionPane.showMessageDialog(dialog, "Selecciona ambos equipos"); return; }
            if (local.equals(visitante)) { JOptionPane.showMessageDialog(dialog, "Local y visitante no pueden ser el mismo equipo"); return; }
            LocalDateTime fecha = null;
            if (!fechaStr.isEmpty()) {
                try { fecha = LocalDateTime.parse(fechaStr, dtf); }
                catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Formato de fecha inválido (YYYY-MM-DD HH:mm)"); return; }
            }

            try {
                if (p == null) {
                    Partido nuevo = new Partido();
                    nuevo.setFecha_partido(fecha);
                    nuevo.setEquipo_local(local);
                    nuevo.setEquipo_visitante(visitante);
                    nuevo.setGoles_locales(0);
                    nuevo.setGoles_visitantes(0);
                    partidoService.crear(nuevo);
                } else {
                    p.setFecha_partido(fecha);
                    p.setEquipo_local(local);
                    p.setEquipo_visitante(visitante);
                    partidoService.actualizar(p);
                }
                cargar();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error guardando partido: " + ex.getMessage());
            }
        });

        cancelar.addActionListener(a -> dialog.dispose());

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(botones, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
