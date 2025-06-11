package esfe.presentacion;

import esfe.dominio.User;
import esfe.persistencia.UserDAO;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChangePasswordForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnChangePassword;

    private UserDAO userDAO;
    private MainForm mainForm;

    public ChangePasswordForm(MainForm mainForm){
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        txtEmail.setText(mainForm.getUserAuntenticate().getEmail());
        setContentPane(mainPanel);
        setModal(true);
        setTitle("Cambiar password");
        pack();
        setLocationRelativeTo(mainForm);

        btnChangePassword.addActionListener(e-> ChangePassword());
    }

    private void ChangePassword(){
        try {
            User userAut = mainForm.getUserAuntenticate();

            User user = new User();
            user.setId(userAut.getId());
            user.setPasswordHash(new String(txtPassword.getPassword()));

            if (user.getPasswordHash().isEmpty()){
                JOptionPane.showMessageDialog(null, "La contraseña es obligatoria",
                        "Validacion", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean res = userDAO.updatePassword(user);  // Faltaba el signo =

            if (res){
                this.dispose();
                LoginForm loginForm = new LoginForm(this.mainForm);  // Quitamos paréntesis extra
                loginForm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se logró cambiar la contraseña",
                        "Cambiar contraseña", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
}