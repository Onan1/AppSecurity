package esfe.presentacion;

import esfe.dominio.User;
import javax.swing.*;

public class MainForm extends JFrame {
    private User userAuntenticate;

    public User getUserAuntenticate() {
        return userAuntenticate;
    }

    public void setUserAuntenticate(User userAuntenticate) {
        this.userAuntenticate = userAuntenticate;
    }

    public MainForm(){
        setTitle("Sistema en java de escritorio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        createMenu();
    }

    private void createMenu(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // ========== MENU PERFIL ==========
        JMenu menuPerfil = new JMenu("Perfil");
        menuBar.add(menuPerfil);

        JMenuItem itemChangePassword = new JMenuItem("Cambiar contraseña");
        menuPerfil.add(itemChangePassword);
        itemChangePassword.addActionListener(e -> {
            try {
                ChangePasswordForm changePassword = new ChangePasswordForm(this);
                changePassword.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir cambio de contraseña: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem itemChangeUser = new JMenuItem("Cambiar de usuario");
        menuPerfil.add(itemChangeUser);
        itemChangeUser.addActionListener(e -> {
            try {
                LoginForm loginForm = new LoginForm(this);
                loginForm.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir login: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem itemSalir = new JMenuItem("Salir");
        menuPerfil.add(itemSalir);
        itemSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro que desea salir?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // ========== MENU MANTENIMIENTO ==========
        JMenu menuMantenimiento = new JMenu("Mantenimiento");
        menuBar.add(menuMantenimiento);

        JMenuItem itemUsers = new JMenuItem("Usuarios");
        menuMantenimiento.add(itemUsers);
        itemUsers.addActionListener(e -> {
            try {
                UserReadingForm userReadingForm = new UserReadingForm(this);
                userReadingForm.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir gestión de usuarios: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // ========== MENU ITEM EVENTOS ==========
        JMenuItem itemEventos = new JMenuItem("Eventos");
        menuMantenimiento.add(itemEventos);
        itemEventos.addActionListener(e -> {
            try {
                // Opción 1: Con import (recomendado)
                EventoReadingForm eventoReadingForm = new EventoReadingForm(this);
                eventoReadingForm.setVisible(true);

                // Opción 2: Sin import (alternativa si hay problemas con import)
                // esfe.presentacion.EventoReadingForm eventoReadingForm = new esfe.presentacion.EventoReadingForm(this);
                // eventoReadingForm.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al abrir gestión de eventos: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }
}