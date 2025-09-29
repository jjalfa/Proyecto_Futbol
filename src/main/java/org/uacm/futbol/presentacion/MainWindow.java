package org.uacm.futbol.presentacion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.uacm.futbol.servicios.*;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public MainWindow() {
        super("Gestor de Torneo - Interfaz Swing");
        // crea EMF/EM (igual que en tu Main no-UI)
        this.emf = Persistence.createEntityManagerFactory("BaseFutbol");
        this.em = emf.createEntityManager();

        EquipoService equipoService = new EquipoServiceImpl(em);
        JugadorService jugadorService = new JugadorServiceimpl(em);
        PosicionService posicionService = new PosicionServiceimpl(em);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Equipos", new EquipoPanel(equipoService));
        tabs.addTab("Jugadores", new JugadorPanel(jugadorService, equipoService, posicionService));
        tabs.addTab("Posiciones", new PosicionPanel(posicionService));

        add(tabs, BorderLayout.CENTER);
    }

    public void close() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow w = new MainWindow();
            w.setVisible(true);

            // Añadir shutdown hook para cerrar EMF
            Runtime.getRuntime().addShutdownHook(new Thread(w::close));
        });
    }
}
