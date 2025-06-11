package esfe.presentacion;

import esfe.dominio.User;

import javax.swing.*;
import java.awt.event.ActionEvent;

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
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Corregido
        createMenu();
    }

    private void createMenu(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuPerfil = new JMenu("Perfil");  // Sin s:
        menuBar.add(menuPerfil);

        JMenuItem itemChangePassword = new JMenuItem("Cambiar contraseña");  // Sin text:
        menuPerfil.add(itemChangePassword);
        itemChangePassword.addActionListener(e -> {  // Sin ActionEvent
            ChangePasswordForm changePassword = new ChangePasswordForm(this);  // Sin mainForm:
            changePassword.setVisible(true);
        });

        JMenuItem itemChangeUser = new JMenuItem("Cambiar de usuario");  // Sin text:
        menuPerfil.add(itemChangeUser);
        itemChangeUser.addActionListener(e -> {  // Sin ActionEvent
            LoginForm loginForm = new LoginForm(this);  // Sin mainForm:
            loginForm.setVisible(true);
        });

        JMenuItem itemSalir = new JMenuItem("Salir");  // Sin text:
        menuPerfil.add(itemSalir);
        itemSalir.addActionListener(e -> System.exit(0));  // Corregido System y sin status:

        JMenu menuMantenimiento = new JMenu("Mantenimiento");  // Era JMenuItem, debe ser JMenu
        menuBar.add(menuMantenimiento);  // Faltaba agregar al menuBar

        JMenuItem itemUsers = new JMenuItem("Usuarios");  // Declaración que faltaba
        menuMantenimiento.add(itemUsers);
        itemUsers.addActionListener(e -> {  // Sin ActionEvent
            UserReadingForm userReadingForm = new UserReadingForm(this);  // Sin mainForm:
            userReadingForm.setVisible(true);
        });
    }
}