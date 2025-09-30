package org.uacm.futbol.presentacion;

import org.uacm.futbol.modelo.Gol;
import org.uacm.futbol.modelo.Jugador;
import org.uacm.futbol.modelo.Partido;
import org.uacm.futbol.servicios.GolService;
import org.uacm.futbol.servicios.JugadorService;
import org.uacm.futbol.servicios.PartidoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class GolPanel extends JPanel {
    private final GolService golService;
    private final PartidoService partidoService;
    private final JugadorService jugadorService;
    private final DefaultTableModel model;
    private final JTable table;

    public GolPanel(GolService golService, PartidoService partidoService, JugadorService jugadorService) {
        this.golService = golService;
        this.partidoService = partidoService;
        this.jugadorService = jugadorService;
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID","Partido","Jugador","Minuto"}, 0){
            @Override public boolean isCellEditable(int r, int c){ return false;}
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        botones.add(btnNuevo); botones.add(btnEliminar); botones.add(btnRefrescar);
        add(botones, BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> mostrarDialogoGol(null));
        btnEliminar.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Selecciona un gol"); return; }
            Long id = (Long) model.getValueAt(r,0);
            if (JOptionPane.showConfirmDialog(this, "Eliminar gol id=" + id + " ?","Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                golService.eliminar(id);
                cargar();
            }
        });
        btnRefrescar.addActionListener(e -> cargar());

        cargar();
    }

    private void cargar() {
        model.setRowCount(0);
        List<Gol> lista = golService.listarTodos();
        for (Gol g : lista) {
            Partido p = g.getPartido();
            Jugador j = g.getJugador();
            String partidoDesc = p != null ? (p.getEquipo_local().getNombre() + " vs " + p.getEquipo_visitante().getNombre()) : "N/D";
            String jugadorDesc = j != null ? j.getNombre() : "N/D";
            model.addRow(new Object[]{g.getId(), partidoDesc, jugadorDesc, g.getMinuto()});
        }
    }

    private void mostrarDialogoGol(Gol g) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        final JDialog dialog;
        String title = (g == null) ? "Nuevo Gol" : "Editar Gol";
        if (owner instanceof Frame) dialog = new JDialog((Frame) owner, title, true);
        else if (owner instanceof Dialog) dialog = new JDialog((Dialog) owner, title, true);
        else dialog = new JDialog((Frame) null, title, true);
        dialog.setSize(420,260);
        dialog.setLocationRelativeTo(owner);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints(); c.insets = new Insets(6,6,6,6);

        c.gridx=0; c.gridy=0; c.anchor=GridBagConstraints.EAST; panel.add(new JLabel("Partido:"), c);
        c.gridx=1; c.gridy=0; c.anchor=GridBagConstraints.WEST;
        Vector<Partido> partidosVec = new Vector<>(partidoService.listarTodos());
        JComboBox<Partido> cmbPartido = new JComboBox<>(partidosVec);
        panel.add(cmbPartido, c);

        c.gridx=0; c.gridy=1; c.anchor=GridBagConstraints.EAST; panel.add(new JLabel("Jugador:"), c);
        c.gridx=1; c.gridy=1; c.anchor=GridBagConstraints.WEST;
        Vector<Jugador> jugadoresVec = new Vector<>(jugadorService.listarTodos());
        JComboBox<Jugador> cmbJugador = new JComboBox<>(jugadoresVec);
        panel.add(cmbJugador, c);

        c.gridx=0; c.gridy=2; c.anchor=GridBagConstraints.EAST; panel.add(new JLabel("Minuto:"), c);
        c.gridx=1; c.gridy=2; c.anchor=GridBagConstraints.WEST;
        JTextField txtMin = new JTextField(6);
        panel.add(txtMin, c);

        if (g != null) {
            if (g.getPartido() != null) cmbPartido.setSelectedItem(g.getPartido());
            if (g.getJugador() != null) cmbJugador.setSelectedItem(g.getJugador());
            txtMin.setText(String.valueOf(g.getMinuto()));
        }

        JPanel botones = new JPanel();
        JButton ok = new JButton("Guardar");
        JButton cancelar = new JButton("Cancelar");
        botones.add(ok); botones.add(cancelar);

        ok.addActionListener(a -> {
            Partido partido = (Partido) cmbPartido.getSelectedItem();
            Jugador jugador = (Jugador) cmbJugador.getSelectedItem();
            String minStr = txtMin.getText().trim();
            if (partido == null || jugador == null) { JOptionPane.showMessageDialog(dialog, "Selecciona partido y jugador"); return; }
            int minuto = 0;
            try { minuto = Integer.parseInt(minStr); if (minuto < 0) throw new NumberFormatException(); }
            catch (Exception ex) { JOptionPane.showMessageDialog(dialog, "Minuto inválido"); return; }

            try {
                if (g == null) {
                    Gol nuevo = new Gol();
                    nuevo.setPartido(partido);
                    nuevo.setJugador(jugador);
                    nuevo.setMinuto(minuto);
                    golService.crear(nuevo);

                    // actualizar marcador del partido según equipo del jugador
                    if (jugador.getEquipo() != null && partido.getEquipo_local() != null && partido.getEquipo_visitante() != null) {
                        if (jugador.getEquipo().getId().equals(partido.getEquipo_local().getId())) {
                            partido.setGoles_locales(partido.getGoles_locales() + 1);
                        } else if (jugador.getEquipo().getId().equals(partido.getEquipo_visitante().getId())) {
                            partido.setGoles_visitantes(partido.getGoles_visitantes() + 1);
                        }
                        partidoService.actualizar(partido);
                    }
                } else {
                    g.setPartido(partido);
                    g.setJugador(jugador);
                    g.setMinuto(minuto);
                    golService.actualizar(g);
                }
                cargar();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error guardando gol: " + ex.getMessage());
            }
        });

        cancelar.addActionListener(a -> dialog.dispose());

        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(botones, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
