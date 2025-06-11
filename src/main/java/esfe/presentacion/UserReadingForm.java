package esfe.presentacion;

import esfe.persistencia.UserDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import esfe.dominio.User;
import esfe.utils.CUD;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class UserReadingForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtName;
    private JButton btnCreate;
    private JTable table1;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JScrollPane tableUsers;

    private UserDAO userDAO;
    private MainForm mainForm;

    public UserReadingForm(MainForm mainForm){
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        setContentPane(mainPanel);
        setModal(true);
        setTitle("Buscar Usuario");
        pack();
        setLocationRelativeTo(mainForm);

        // KeyListener corregido
        txtName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!txtName.getText().isEmpty()){
                    search(txtName.getText());
                } else {
                    DefaultTableModel emptyModel = new DefaultTableModel();
                    table1.setModel(emptyModel);  // Corregido: era tableUsers
                }
            }
        });

        // ActionListener para crear
        btnCreate.addActionListener(e -> {  // Sin ActionEvent
            UserWriteForm userWriteForm = new UserWriteForm(this.mainForm, CUD.CREATE, new User());
            userWriteForm.setVisible(true);

            DefaultTableModel emptyModel = new DefaultTableModel();
            table1.setModel(emptyModel);  // Corregido: era tableUsers
        });

        // ActionListener para actualizar
        btnUpdate.addActionListener(e -> {  // Sin ActionEvent, paréntesis corregidos
            User user = getUserFromTableRow();

            if (user != null){
                UserWriteForm userWriteForm = new UserWriteForm(this.mainForm, CUD.UPDATE, user);
                userWriteForm.setVisible(true);

                DefaultTableModel emptyModel = new DefaultTableModel();
                table1.setModel(emptyModel);  // Corregido: era tableUsers
            }
        });

        // ActionListener para eliminar - corregido (era txtName, debe ser btnDelete)
        btnDelete.addActionListener(e -> {  // Corregido: era txtName y actionEvent
            User user = getUserFromTableRow();

            if (user != null){
                UserWriteForm userWriteForm = new UserWriteForm(this.mainForm, CUD.DELETE, user);  // Corregido: era this.main
                userWriteForm.setVisible(true);

                DefaultTableModel emptyModel = new DefaultTableModel();
                table1.setModel(emptyModel);  // Corregido: era EmptyModel
            }
        });
    }

    private void search(String query){
        try {
            ArrayList<User> users = userDAO.search(query);
            createTable(users);
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,  // Sin parentComponent:
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);  // Sin title:
        }
    }

    public void createTable(ArrayList<User> users){
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        // Agregar columnas corregido
        model.addColumn("Id");      // Corregido: era modeladdColumn(ColumnName)
        model.addColumn("Nombre");  // Corregido: era modeladdColumn(ColumnName)
        model.addColumn("Email");   // Corregido: era modeladdColumn(ColumnName)
        model.addColumn("Status");  // Corregido: era modeladdColumn(ColumnName)

        this.table1.setModel(model);  // Corregido: era tableUsers

        Object[] row = new Object[4];  // Inicializar array

        for (int i = 0; i < users.size(); i++){
            User user = users.get(i);

            // Llenar el array row
            row[0] = user.getId();
            row[1] = user.getName();
            row[2] = user.getEmail();
            row[3] = user.getStrEstatus();

            model.addRow(row);  // Agregar la fila completa
        }

        hideCol(0);  // Ocultar columna ID
    }

    // Método para ocultar columna corregido
    private void hideCol(int pColumna){
        this.table1.getColumnModel().getColumn(pColumna).setMaxWidth(0);      // Corregido sintaxis
        this.table1.getColumnModel().getColumn(pColumna).setMinWidth(0);      // Corregido sintaxis
        this.table1.getTableHeader().getColumnModel().getColumn(pColumna).setMaxWidth(0);  // Corregido
        this.table1.getTableHeader().getColumnModel().getColumn(pColumna).setMinWidth(0);  // Corregido
    }

    private User getUserFromTableRow(){
        User user = null;
        try{
            int filaSelect = this.table1.getSelectedRow();  // Corregido: era tableUsers
            int id = 0;

            if (filaSelect != -1){
                id = (int) this.table1.getValueAt(filaSelect, 0);  // Sin column:
            } else {
                JOptionPane.showMessageDialog(null,  // Sin parentComponent:
                        "Seleccionar una fila de la tabla.",  // Sin message:
                        "Validacion", JOptionPane.WARNING_MESSAGE);  // Sin title:
                return null;
            }

            user = userDAO.getById(id);  // Corregido: era getByUd

            if (user.getId() == 0){
                JOptionPane.showMessageDialog(null,  // Sin parentComponent:
                        "No se encontro ningun usuario",  // Sin message:, corregido "usuairo"
                        "Validacion", JOptionPane.WARNING_MESSAGE);  // Sin title:
                return null;
            }

            return user;
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,  // Sin parentComponent:
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);  // Sin title:
            return null;
        }
    }
}