package esfe.presentacion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import esfe.dominio.User;
import esfe.persistencia.UserDAO;

public class LoginForm extends JDialog {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;
    private JPanel mainPanel;

    private UserDAO userDAO;
    private MainForm mainForm;

    public LoginForm(MainForm mainForm){
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        setContentPane(mainPanel);
        setModal(true);
        setTitle("Login");
        pack();
        setLocationRelativeTo(mainForm);

        btnSalir.addActionListener(e -> System.exit(0));  // Sin status:
        btnLogin.addActionListener(e -> login());  // Cambié Login() por login()

        // WindowListener corregido
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void login(){  // Cambié el nombre del método
        try{
            User user = new User();
            user.setEmail(txtEmail.getText());
            user.setPasswordHash(new String(txtPassword.getPassword()));

            User userAut = userDAO.authenticate(user);  // Quitado "UserDAO." si no es estático

            if(userAut != null && userAut.getId() > 0 && userAut.getEmail().equals(user.getEmail())){
                this.mainForm.setUserAuntenticate(userAut);
                this.dispose();
            }
            else{
                JOptionPane.showMessageDialog(null,  // Sin parentComponent:
                        "Email y Password incorrecto",  // Sin message:
                        "Login",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(null,  // Sin parentComponent:
                    ex.getMessage(),
                    "Sistema",  // Corregido "Sistem"
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
