package esfe.presentacion;

import esfe.persistencia.UserDAO;
import esfe.utils.CBOption;
import esfe.utils.CUD;

import javax.swing.*;
import java.awt.event.ActionEvent;

import esfe.dominio.User;

public class UserWriteForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<CBOption> cbStatus;
    private JButton btnOK;
    private JButton btnCancel;
    private JLabel lbPassword;

    private UserDAO userDAO;
    private MainForm mainForm;
    private CUD cud;
    private User en;

    public UserWriteForm(MainForm mainForm, CUD cud, User user){
        this.cud = cud;
        this.en = user;
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        setContentPane(mainPanel);
        setModal(true);
        init();
        pack();
        setLocationRelativeTo(mainForm);

        btnCancel.addActionListener(e -> this.dispose());  // Sin ActionEvent

        btnOK.addActionListener(e -> ok());  // Sin ActionEvent
    }

    private void init(){
        initCBStatus();

        switch (this.cud){
            case CREATE:
                setTitle("Crear Usuario");
                btnOK.setText("Guardar");
                break;
            case UPDATE:
                setTitle("Modificar Usuario");
                btnOK.setText("Guardar");
                break;
            case DELETE:
                setTitle("Eliminar Usuario");
                btnOK.setText("Eliminar");  // Corregido: eliminé la línea duplicada
                break;
        }
        setValuesControls(this.en);
    }

    private void initCBStatus(){
        DefaultComboBoxModel<CBOption> model = (DefaultComboBoxModel<CBOption>) cbStatus.getModel();

        model.addElement(new CBOption("ACTIVO", (byte)1));  // Corregido: sin displayText

        model.addElement(new CBOption("INACTIVO", (byte)2));  // Corregido: sin displayText y paréntesis
    }

    private void setValuesControls(User user){
        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());

        cbStatus.setSelectedItem(new CBOption(null, user.getStatus()));  // Sin displayText:

        if (this.cud == CUD.CREATE){
            cbStatus.setSelectedItem(new CBOption(null, (byte)1));  // Sin displayText: y value:
        }

        if (this.cud == CUD.DELETE){
            txtName.setEditable(false);
            txtEmail.setEditable(false);
            cbStatus.setEnabled(false);  // Corregido: setEnabled en lugar de setEditable
        }

        if (this.cud != CUD.CREATE){
            txtPassword.setVisible(false);
            lbPassword.setVisible(false);
        }
    }

    private boolean getValuesControls(){
        boolean res = false;

        CBOption selectedOption = (CBOption) cbStatus.getSelectedItem();
        byte status = selectedOption != null ? (byte) (selectedOption.getValue()) : (byte) 0;

        if (txtName.getText().trim().isEmpty()){
            return res;
        }
        else if (txtEmail.getText().trim().isEmpty()){
            return res;
        }
        else if (status == (byte) 0){
            return res;
        }
        else if (this.cud != CUD.CREATE && this.en.getId() == 0){
            return res;
        }

        res = true;

        this.en.setName(txtName.getText());
        this.en.setEmail(txtEmail.getText());
        this.en.setStatus(status);

        if (this.cud == CUD.CREATE){
            this.en.setPasswordHash(new String(txtPassword.getPassword()));  // Sin paréntesis extra
            if (this.en.getPasswordHash().trim().isEmpty()){
                return false;
            }
        }

        return res;
    }

    private void ok(){
        try {
            boolean res = getValuesControls();

            if (res){
                boolean r = false;

                switch (this.cud){
                    case CREATE:
                        User user = userDAO.create(this.en);
                        if (user.getId() > 0){
                            r = true;
                        }
                        break;
                    case UPDATE:
                        r = userDAO.update(this.en);
                        break;
                    case DELETE:
                        r = userDAO.delete(this.en);
                        break;
                }

                if (r){
                    JOptionPane.showMessageDialog(null,  // Sin parentComponent:
                            "Transaccion realizada exitosamente",  // Sin message:
                            "Informacion", JOptionPane.INFORMATION_MESSAGE);  // Sin title:

                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null,  // Sin parentComponent:
                            "No se logro realizar ninguna accion",  // Sin message:
                            "ERROR", JOptionPane.ERROR_MESSAGE);  // Sin title:
                }

            } else {
                JOptionPane.showMessageDialog(null,  // Sin parentComponent:
                        "Los campos con * son obligatorios",  // Sin message:
                        "Validacion", JOptionPane.WARNING_MESSAGE);  // Sin title:
            }
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,  // Sin parentComponent:
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);  // Sin title:
        }
    }
}