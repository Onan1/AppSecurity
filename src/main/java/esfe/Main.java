package esfe;

import esfe.presentacion.LoginForm;
import esfe.presentacion.MainForm;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MainForm mainForm = new MainForm();
                mainForm.setVisible(true);

                LoginForm loginForm = new LoginForm(mainForm);
                loginForm.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error al iniciar la aplicación: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}