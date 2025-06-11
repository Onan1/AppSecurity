package esfe.persistencia;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import esfe.dominio.User;
import esfe.utils.PasswordHasher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectionManagerTest{
    ConectionManager conectionManager;
    @BeforeEach

    void setUp() throws SQLException{
        conectionManager = ConectionManager.getInstance();
    }

    @AfterEach
    void tearDown() throws SQLException{
        if (conectionManager !=null){
            conectionManager.disconnect();
            conectionManager = null;
        }
    }
    @Test void connect() throws SQLException{
        Connection conn = conectionManager.connect();

        assertNotNull(conn, "La conexion no debe ser nula");
        assertFalse(conn.isClosed(), "La conexion no debe estar abierta");
        if (conn != null){
            conn.close();
        }
    }
}