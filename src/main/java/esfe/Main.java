package esfe;

import esfe.presentacion.LoginForm;
import esfe.presentacion.MainForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm =new MainForm();
            mainForm.setVisible(true);
            LoginForm loginForm = new LoginForm(mainForm);
            loginForm.setVisible(true);
        });
    }
}