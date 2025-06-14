package esfe.presentacion;

import esfe.dominio.Evento;
import esfe.persistencia.EventoDAO;
import esfe.utils.CUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class EventoReadingForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JTable tblEventos;
    private JButton btnCreate;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnCancel;

    private EventoDAO eventoDAO;
    private MainForm mainForm;
    private ArrayList<Evento> eventos;

    public EventoReadingForm(MainForm mainForm) {
        super(mainForm, "Gestión de Eventos", true);
        this.mainForm = mainForm;
        this.eventos = new ArrayList<>();

        try {
            eventoDAO = new EventoDAO();

            // SIEMPRE crear la UI manualmente para evitar problemas
            createUIManually();

            setContentPane(mainPanel);
            setModal(true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            // Configurar la ventana
            setSize(900, 600);
            setLocationRelativeTo(mainForm);
            setResizable(true);

            // Inicializar componentes
            initTable();
            setupEventListeners();
            fillTable("");

            System.out.println("EventoReadingForm inicializado correctamente");

        } catch (Exception e) {
            System.err.println("Error al inicializar EventoReadingForm: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainForm,
                    "Error al inicializar el formulario de eventos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createUIManually() {
        System.out.println("Creando UI manualmente...");

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === PANEL SUPERIOR PARA BÚSQUEDA ===
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Búsqueda"));

        txtSearch = new JTextField(25);
        btnSearch = new JButton("Buscar");

        searchPanel.add(new JLabel("Buscar evento:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // === TABLA CON SCROLL ===
        tblEventos = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblEventos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Eventos"));
        scrollPane.setPreferredSize(new Dimension(850, 400));

        // === PANEL DE BOTONES ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());

        btnCreate = new JButton("Nuevo");
        btnUpdate = new JButton("Editar");
        btnDelete = new JButton("Eliminar");
        btnCancel = new JButton("Cerrar");

        // Configurar botones con apariencia uniforme (igual al botón de buscar)
        Dimension buttonSize = new Dimension(120, 35);

        // Aplicar el mismo estilo a todos los botones
        JButton[] buttons = {btnCreate, btnUpdate, btnDelete, btnCancel, btnSearch};
        for (JButton button : buttons) {
            button.setPreferredSize(buttonSize);
            // Remover colores personalizados para que todos tengan la apariencia por defecto
            button.setBackground(null);
            button.setForeground(null);
            // Opcional: aplicar el Look and Feel del sistema
            button.setOpaque(false);
        }

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnCancel);

        // === AGREGAR COMPONENTES AL PANEL PRINCIPAL ===
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        System.out.println("UI creada. Botones inicializados: " +
                (btnCreate != null) + ", " + (btnUpdate != null) + ", " +
                (btnDelete != null) + ", " + (btnCancel != null));
    }

    private void setupEventListeners() {
        System.out.println("Configurando event listeners...");

        // === BOTÓN NUEVO ===
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón Nuevo presionado");
                abrirFormularioCrear();
            }
        });

        // === BOTÓN EDITAR ===
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón Editar presionado");
                abrirFormularioEditar();
            }
        });

        // === BOTÓN ELIMINAR ===
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón Eliminar presionado");
                abrirFormularioEliminar();
            }
        });

        // === BOTÓN CERRAR ===
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón Cerrar presionado");
                dispose();
            }
        });

        // === BOTÓN BUSCAR ===
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarEventos();
            }
        });

        // === BÚSQUEDA CON ENTER ===
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarEventos();
                }
            }
        });

        // === DOBLE CLIC EN TABLA ===
        tblEventos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    abrirFormularioEditar();
                }
            }
        });

        System.out.println("Event listeners configurados correctamente");
    }

    private void abrirFormularioCrear() {
        try {
            System.out.println("Abriendo formulario de creación...");
            Evento nuevoEvento = new Evento();
            EventoWriteForm form = new EventoWriteForm(this.mainForm, CUD.CREATE, nuevoEvento);
            form.setVisible(true);
            refreshTable();
            System.out.println("Formulario de creación abierto correctamente");
        } catch (Exception ex) {
            System.err.println("Error al abrir formulario de creación: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al abrir formulario de creación: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormularioEditar() {
        int selectedRow = tblEventos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un evento para editar",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            System.out.println("Abriendo formulario de edición...");
            Evento eventoOriginal = eventos.get(selectedRow);

            // Crear una copia del evento
            Evento eventoParaEditar = new Evento();
            eventoParaEditar.setId(eventoOriginal.getId());
            eventoParaEditar.setNombre(eventoOriginal.getNombre());
            eventoParaEditar.setLugar(eventoOriginal.getLugar());

            EventoWriteForm form = new EventoWriteForm(this.mainForm, CUD.UPDATE, eventoParaEditar);
            form.setVisible(true);
            refreshTable();
            System.out.println("Formulario de edición abierto correctamente");
        } catch (Exception ex) {
            System.err.println("Error al abrir formulario de edición: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al abrir formulario de edición: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFormularioEliminar() {
        int selectedRow = tblEventos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un evento para eliminar",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Evento eventoOriginal = eventos.get(selectedRow);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro que desea eliminar el evento: " + eventoOriginal.getNombre() + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("Abriendo formulario de eliminación...");

                // Crear una copia del evento
                Evento eventoParaEliminar = new Evento();
                eventoParaEliminar.setId(eventoOriginal.getId());
                eventoParaEliminar.setNombre(eventoOriginal.getNombre());
                eventoParaEliminar.setLugar(eventoOriginal.getLugar());

                EventoWriteForm form = new EventoWriteForm(this.mainForm, CUD.DELETE, eventoParaEliminar);
                form.setVisible(true);
                refreshTable();
                System.out.println("Formulario de eliminación abierto correctamente");
            }
        } catch (Exception ex) {
            System.err.println("Error al abrir formulario de eliminación: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al abrir formulario de eliminación: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEventos() {
        String searchText = txtSearch.getText().trim();
        fillTable(searchText);

        if (eventos.isEmpty() && !searchText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No se encontraron eventos que coincidan con: '" + searchText + "'",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void initTable() {
        System.out.println("Inicializando tabla...");

        String[] columns = {"ID", "Nombre del Evento", "Lugar"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };

        tblEventos.setModel(model);
        tblEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblEventos.setRowSelectionAllowed(true);
        tblEventos.setColumnSelectionAllowed(false);

        // Configurar columnas
        if (tblEventos.getColumnModel().getColumnCount() > 0) {
            tblEventos.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
            tblEventos.getColumnModel().getColumn(1).setPreferredWidth(400);  // Nombre
            tblEventos.getColumnModel().getColumn(2).setPreferredWidth(300);  // Lugar
        }

        // Estilo de tabla
        tblEventos.setRowHeight(30);
        tblEventos.setShowGrid(true);
        tblEventos.setGridColor(Color.LIGHT_GRAY);
        tblEventos.setSelectionBackground(new Color(184, 207, 229));

        System.out.println("Tabla inicializada correctamente");
    }

    private void fillTable(String searchText) {
        try {
            System.out.println("Llenando tabla con búsqueda: '" + searchText + "'");

            DefaultTableModel model = (DefaultTableModel) tblEventos.getModel();
            model.setRowCount(0);

            if (searchText == null || searchText.trim().isEmpty()) {
                eventos = eventoDAO.search("");
            } else {
                eventos = eventoDAO.search(searchText);
            }

            if (eventos != null) {
                for (Evento evento : eventos) {
                    Object[] row = {
                            evento.getId(),
                            evento.getNombre(),
                            evento.getLugar()
                    };
                    model.addRow(row);
                }
                System.out.println("Se cargaron " + eventos.size() + " eventos");
            } else {
                eventos = new ArrayList<>();
                System.out.println("No se obtuvieron eventos (lista null)");
            }

            setTitle("Gestión de Eventos - " + eventos.size() + " evento(s)");

        } catch (Exception ex) {
            System.err.println("Error al llenar tabla: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los eventos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            eventos = new ArrayList<>();
        }
    }

    public void refreshTable() {
        SwingUtilities.invokeLater(() -> {
            fillTable(txtSearch != null ? txtSearch.getText() : "");
        });
    }
}