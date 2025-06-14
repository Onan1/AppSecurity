package esfe.presentacion;

import esfe.persistencia.EventoDAO;
import esfe.utils.CUD;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import esfe.dominio.Evento;

public class EventoWriteForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtNombre;
    private JTextField txtLugar;
    private JButton btnOK;
    private JButton btnCancel;

    private EventoDAO eventoDAO;
    private MainForm mainForm;
    private CUD cud;
    private Evento evento;

    public EventoWriteForm(MainForm mainForm, CUD cud, Evento evento) {
        super(mainForm, true);
        this.cud = cud;
        this.evento = evento;
        this.mainForm = mainForm;

        try {
            System.out.println("Inicializando EventoWriteForm con operación: " + cud);

            eventoDAO = new EventoDAO();

            createUIManually();

            setContentPane(mainPanel);
            setModal(true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            setSize(400, 200);
            setLocationRelativeTo(mainForm);
            setResizable(false);

            // Inicializar componentes
            init();
            setupEventListeners();

            System.out.println("EventoWriteForm inicializado correctamente");

        } catch (Exception e) {
            System.err.println("Error al inicializar EventoWriteForm: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainForm,
                    "Error al inicializar el formulario: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createUIManually() {
        System.out.println("Creando UI para EventoWriteForm...");

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblNombre = new JLabel("Nombre del Evento *:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(lblNombre, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        txtNombre.setPreferredSize(new Dimension(200, 25));
        formPanel.add(txtNombre, gbc);

        // Etiqueta y campo Lugar
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel lblLugar = new JLabel("Lugar *:");
        lblLugar.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(lblLugar, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        txtLugar = new JTextField(20);
        txtLugar.setPreferredSize(new Dimension(200, 25));
        formPanel.add(txtLugar, gbc);

        // === PANEL DE BOTONES ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnOK = new JButton("Guardar");
        btnCancel = new JButton("Cancelar");

        Dimension buttonSize = new Dimension(100, 30);
        btnOK.setPreferredSize(buttonSize);
        btnCancel.setPreferredSize(buttonSize);


        buttonPanel.add(btnOK);
        buttonPanel.add(btnCancel);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        System.out.println("UI de EventoWriteForm creada correctamente");
    }

    private void setupEventListeners() {
        System.out.println("Configurando event listeners para EventoWriteForm...");

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón Cancelar presionado");
                dispose();
            }
        });

        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón OK presionado");
                procesarFormulario();
            }
        });

        txtLugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarFormulario();
            }
        });

        System.out.println("Event listeners configurados para EventoWriteForm");
    }

    private void init() {
        System.out.println("Inicializando formulario para operación: " + cud);

        switch (this.cud) {
            case CREATE:
                setTitle("Crear Nuevo Evento");
                btnOK.setText("Crear");
                break;
            case UPDATE:
                setTitle("Modificar Evento");
                btnOK.setText("Actualizar");
                break;
            case DELETE:
                setTitle("Eliminar Evento");
                btnOK.setText("Eliminar");
                break;
        }

        setValuesControls(this.evento);

        if (cud != CUD.DELETE) {
            SwingUtilities.invokeLater(() -> txtNombre.requestFocusInWindow());
        }
    }

    private void setValuesControls(Evento evento) {
        if (evento != null) {
            txtNombre.setText(evento.getNombre() != null ? evento.getNombre() : "");
            txtLugar.setText(evento.getLugar() != null ? evento.getLugar() : "");

            if (this.cud == CUD.DELETE) {
                txtNombre.setEditable(false);
                txtLugar.setEditable(false);
                txtNombre.setBackground(new Color(240, 240, 240));
                txtLugar.setBackground(new Color(240, 240, 240));
            }
        }
    }

    private boolean validarDatos() {
        String nombre = txtNombre.getText().trim();
        String lugar = txtLugar.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El campo 'Nombre del Evento' es obligatorio",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }

        if (lugar.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El campo 'Lugar' es obligatorio",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            txtLugar.requestFocus();
            return false;
        }

        if (this.cud != CUD.CREATE && this.evento.getId() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Error: ID del evento no válido para la operación",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Asignar valores al objeto
        this.evento.setNombre(nombre);
        this.evento.setLugar(lugar);

        return true;
    }

    private void procesarFormulario() {
        try {
            System.out.println("Procesando formulario...");

            if (!validarDatos()) {
                return;
            }

            boolean resultado = false;
            String mensaje = "";
            String tituloMensaje = "";

            switch (this.cud) {
                case CREATE:
                    System.out.println("Creando evento: " + evento.getNombre());
                    Evento eventoCreado = eventoDAO.create(this.evento);
                    resultado = eventoCreado != null && eventoCreado.getId() > 0;
                    mensaje = resultado ? "Evento creado exitosamente" : "No se pudo crear el evento";
                    tituloMensaje = "Crear Evento";
                    break;

                case UPDATE:
                    System.out.println("Actualizando evento ID: " + evento.getId());
                    resultado = eventoDAO.update(this.evento);
                    mensaje = resultado ? "Evento actualizado exitosamente" : "No se pudo actualizar el evento";
                    tituloMensaje = "Actualizar Evento";
                    break;

                case DELETE:
                    System.out.println("Eliminando evento ID: " + evento.getId());
                    resultado = eventoDAO.delete(this.evento);
                    mensaje = resultado ? "Evento eliminado exitosamente" : "No se pudo eliminar el evento";
                    tituloMensaje = "Eliminar Evento";
                    break;
            }

            if (resultado) {
                JOptionPane.showMessageDialog(this, mensaje, tituloMensaje, JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Operación exitosa: " + mensaje);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Operación fallida: " + mensaje);
            }

        } catch (Exception ex) {
            String operacion = "";
            switch (this.cud) {
                case CREATE: operacion = "crear"; break;
                case UPDATE: operacion = "actualizar"; break;
                case DELETE: operacion = "eliminar"; break;
            }

            System.err.println("Error al " + operacion + " evento: " + ex.getMessage());
            ex.printStackTrace();

            JOptionPane.showMessageDialog(this,
                    "Error al " + operacion + " el evento:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}