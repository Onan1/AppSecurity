package esfe.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import esfe.dominio.Evento;

public class EventoDAO {
    private ConectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public EventoDAO(){
        conn = ConectionManager.getInstance();
    }

    public Evento create(Evento evento) throws SQLException {
        Evento res = null;
        try (PreparedStatement ps = conn.connect().prepareStatement(
                "INSERT INTO Evento (nombre, lugar) VALUES (?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getLugar());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGenerado = generatedKeys.getInt(1);
                        res = getById(idGenerado);
                    } else {
                        throw new SQLException("Creating evento failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el evento: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return res;
    }

    public boolean update(Evento evento) throws SQLException {
        boolean res = false;
        try (PreparedStatement ps = conn.connect().prepareStatement(
                "UPDATE Evento SET nombre = ?, lugar = ? WHERE id = ?"
        )) {
            ps.setString(1, evento.getNombre());
            ps.setString(2, evento.getLugar());
            ps.setInt(3, evento.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al modificar el evento: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }

        return res;
    }

    public boolean delete(Evento evento) throws SQLException {
        boolean res = false;
        try (PreparedStatement ps = conn.connect().prepareStatement(
                "DELETE FROM Evento WHERE id = ?"
        )) {
            ps.setInt(1, evento.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el evento: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }

        return res;
    }

    public ArrayList<Evento> search(String nombre) throws SQLException {
        ArrayList<Evento> records = new ArrayList<>();

        try (PreparedStatement ps = conn.connect().prepareStatement(
                "SELECT id, nombre, lugar FROM Evento WHERE nombre LIKE ?")) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Evento evento = new Evento();
                    evento.setId(rs.getInt("id"));
                    evento.setNombre(rs.getString("nombre"));
                    evento.setLugar(rs.getString("lugar"));
                    records.add(evento);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar eventos: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }

        return records;
    }

    public Evento getById(int id) throws SQLException {
        Evento evento = null;

        try (PreparedStatement ps = conn.connect().prepareStatement("SELECT id, nombre, lugar " +
                "FROM Evento " +
                "WHERE id = ?")) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    evento = new Evento();
                    evento.setId(rs.getInt("id"));
                    evento.setNombre(rs.getString("nombre"));
                    evento.setLugar(rs.getString("lugar"));
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener un evento por id: " + ex.getMessage(), ex);
        } finally {
            conn.disconnect();
        }
        return evento;
    }
}