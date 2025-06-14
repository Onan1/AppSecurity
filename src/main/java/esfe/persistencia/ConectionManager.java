package esfe.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectionManager {
    private static final String STR_CONNECTION = "jdbc:mysql://127.0.0.1:4406/SecurityDB2025?" +
            "user=root&" +
            "password=monroy11";

    private Connection connection;
    private static ConectionManager instance;

    private ConectionManager() {
        this.connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver JDBC de MySQL. Asegúrate de que el conector MySQL esté en el classpath.", e);
        }
    }

    public synchronized Connection connect() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            try {
                this.connection = DriverManager.getConnection(STR_CONNECTION);
            } catch (SQLException exception) {
                throw new SQLException("Error al conectar a la base de datos MySQL: " + exception.getMessage(), exception);
            }
        }
        return this.connection;
    }

    public void disconnect() throws SQLException {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException exception) {
                throw new SQLException("Error al cerrar la conexión MySQL: " + exception.getMessage(), exception);
            } finally {
                this.connection = null;
            }
        }
    }

    public static synchronized ConectionManager getInstance() {
        if (instance == null) {
            instance = new ConectionManager();
        }
        return instance;
    }
}