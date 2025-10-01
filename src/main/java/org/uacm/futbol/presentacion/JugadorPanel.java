package org.uacm.futbol.presentacion;



import org.uacm.futbol.modelo.Equipo;
import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.modelo.Posicion;
import org.uacm.futbol.servicios.EquipoService;
import org.uacm.futbol.servicios.JugadorService;
import org.uacm.futbol.servicios.PosicionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

public class JugadorPanel extends JPanel {
    private final JugadorService jugadorService;
    private final EquipoService equipoService;
    private final PosicionService posicionService;
    private final DefaultTableModel model;
    private final JTable table;

    public JugadorPanel(JugadorService jugadorService, EquipoService equipoService, PosicionService posicionService) {
        this.jugadorService = jugadorService;
        this.equipoService = equipoService;
        this.posicionService = posicionService;
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Fecha Nac", "Posición", "Equipo"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
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

        btnNuevo.addActionListener(e -> mostrarDialogoJugador(null));
        btnEditar.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Selecciona un jugador"); return; }
            Long id = (Long) model.getValueAt(r, 0);
            Jugador j = jugadorService.buscarPorId(id);
            mostrarDialogoJugador(j);
        });

        // <- CAMBIO: eliminar con confirmación (cuenta goles) y cascada desde el servicio
        btnEliminar.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Selecciona un jugador"); return; }
            Long id = (Long) model.getValueAt(r, 0);

            try {
                long numGoles = jugadorService.contarGoles(id);
                String mensaje = "El jugador tiene " + numGoles + " gol(es).\nAl borrar se eliminarán esos goles y se actualizarán los marcadores.\n¿Continuar?";
                int opt = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar borrado", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    jugadorService.eliminarCascade(id);
                    cargar();
                    JOptionPane.showMessageDialog(this, "Jugador eliminado correctamente.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al eliminar jugador: " + ex.getMessage());
            }
        });

        btnRefrescar.addActionListener(e -> cargar());

        cargar();
    }

    private void cargar() {
        model.setRowCount(0);
        List<Jugador> lista = jugadorService.listarTodos();
        for (Jugador j : lista) {
            String pos = j.getPosicion() != null ? j.getPosicion().getNombre() : "SIN POSICION";
            String equipo = j.getEquipo() != null ? j.getEquipo().getNombre() : "SIN EQUIPO";
            model.addRow(new Object[]{j.getId(), j.getNombre(), j.getFecha_nacimiento(), pos, equipo});
        }
    }

    private void mostrarDialogoJugador(Jugador j) {
        // Obtener el owner Window
        java.awt.Window owner = SwingUtilities.getWindowAncestor(this);

        // Crear JDialog de forma segura según el tipo de owner
        final JDialog dialog;
        String title = (j == null) ? "Nuevo Jugador" : "Editar Jugador";

        if (owner instanceof java.awt.Frame) {
            dialog = new JDialog((java.awt.Frame) owner, title, true);
        } else if (owner instanceof java.awt.Dialog) {
            dialog = new JDialog((java.awt.Dialog) owner, title, true);
        } else {
            // fallback: sin owner
            dialog = new JDialog((java.awt.Frame) null, title, true);
        }

        dialog.setSize(420, 300);
        dialog.setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Nombre:"), c);
        c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.WEST;
        JTextField txtNombre = new JTextField(20);
        panel.add(txtNombre, c);

        c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Fecha Nac (YYYY-MM-DD):"), c);
        c.gridx = 1; c.gridy = 1; c.anchor = GridBagConstraints.WEST;
        JTextField txtFecha = new JTextField(12);
        panel.add(txtFecha, c);

        c.gridx = 0; c.gridy = 2; c.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Posición:"), c);
        c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.WEST;
        Vector<Posicion> posicionesVec = new Vector<>(posicionService.listarTodos());
        JComboBox<Posicion> cmbPosicion = new JComboBox<>(posicionesVec);
        panel.add(cmbPosicion, c);

        c.gridx = 0; c.gridy = 3; c.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Equipo:"), c);
        c.gridx = 1; c.gridy = 3; c.anchor = GridBagConstraints.WEST;
        Vector<Equipo> equiposVec = new Vector<>(equipoService.listarTodos());
        JComboBox<Equipo> cmbEquipo = new JComboBox<>(equiposVec);
        panel.add(cmbEquipo, c);

        // Precargar valores si editing
        if (j != null) {
            txtNombre.setText(j.getNombre());
            if (j.getFecha_nacimiento() != null) txtFecha.setText(j.getFecha_nacimiento().toString());
            if (j.getPosicion() != null) cmbPosicion.setSelectedItem(j.getPosicion());
            if (j.getEquipo() != null) cmbEquipo.setSelectedItem(j.getEquipo());
        }

        JPanel botones = new JPanel();
        JButton ok = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(ok); botones.add(cancelar);

        ok.addActionListener(a -> {
            String nombre = txtNombre.getText().trim();
            String fecha = txtFecha.getText().trim();
            Posicion pos = (Posicion) cmbPosicion.getSelectedItem();
            Equipo equipo = (Equipo) cmbEquipo.getSelectedItem();

            if (nombre.isEmpty()) { JOptionPane.showMessageDialog(dialog, "Nombre obligatorio"); return; }
            if (pos == null) { JOptionPane.showMessageDialog(dialog, "Selecciona posición"); return; }
            LocalDate fechaN = null;
            if (!fecha.isEmpty()) {
                try { fechaN = LocalDate.parse(fecha); }
                catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Formato fecha inválido (YYYY-MM-DD)"); return; }
            }

            try {
                if (j == null) {
                    Jugador nuevo = new Jugador();
                    nuevo.setNombre(nombre);
                    nuevo.setFecha_nacimiento(fechaN);
                    nuevo.setPosicion(pos);
                    nuevo.setEquipo(equipo);
                    jugadorService.crear(nuevo);
                } else {
                    j.setNombre(nombre);
                    j.setFecha_nacimiento(fechaN);
                    j.setPosicion(pos);
                    j.setEquipo(equipo);
                    jugadorService.actualizar(j);
                }
                cargar(); // refrescar la tabla
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error guardando jugador: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        cancelar.addActionListener(a -> dialog.dispose());

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(botones, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
