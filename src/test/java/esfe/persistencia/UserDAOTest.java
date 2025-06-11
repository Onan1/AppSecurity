package esfe.persistencia;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import esfe.dominio.User;

import java.util.ArrayList;
import java.util.Random;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    void setUp(){
        userDAO = new UserDAO();
    }

    private  User create(User user) throws SQLException{
        User res = userDAO.create(user);

        return res;
    }
    private void update(User user) throws SQLException{

        user.setName(user.getName() + "_u");
        user.setName("u" + user.getEmail());
        user.setStatus((byte)1);

        boolean res = userDAO.update(user);

        assertTrue(res, "La actualizacion del usuario deberia ser exitosa.");

        getById(user);
    }
    private void getById(User user) throws SQLException{
        User res= userDAO.getById(user.getId());

        assertNotNull(res, "El usuario obtenido por ID no deberia ser nulo.");
        assertEquals(user.getId(), res.getId(), "El ID del usuario obtenido debe ser igual al original.");
        assertEquals(user.getName(), res.getName(),"El nombre del usuario obtenido debe ser igual al esperado.");
        assertEquals(user.getEmail(), res.getEmail(),"El email del usuario obtenido debe ser igual al esperado.");
        assertEquals(user.getStatus(), res.getStatus(),"El status del usuario obtenido debe ser igual al esperado.");
    }

    private void search(User user) throws SQLException{
        ArrayList<User> users = userDAO.search(user.getName());
        boolean find = false;

        for(User userItem : users) {
            if (userItem.getName().contains(user.getName())) {
                find = true;
            } else {
                find = false;
                break;
            }
        }
        assertTrue(find,"El nombre buscado no fue encontrado:" + user.getName());
    }
    private void delete(User user) throws SQLException{
        boolean res = userDAO.delete(user);

        assertTrue(res,"La eliminacion del usuario deberia ser exitosa.");

        User res2 = userDAO.getById(user.getId());

        assertNull(res2, "El usuario deberia haber sido eliminado y no encontrado por ID");
    }
    private void autenticate(User user) throws SQLException{
        User res= userDAO.authenticate(user);

        assertNotNull(res, "La autenticacion deberia retornar un usuario no nulo si es exitosa.");
        assertEquals(res.getEmail(), user.getEmail(), "El email del usuario autenticado debe coincidir con el email proporciono.");
        assertEquals(res.getStatus(),1,"El status del usuario autenticado debe ser 1 (activo).");
    }
    private void autenticateFails(User user) throws SQLException{
        User res = userDAO.authenticate(user);

        assertNull(res, "La autenticacion deberia fallar y retornar null para credenciales invalidas.");
    }
    private void updatePassword(User user) throws SQLException{
        boolean res = userDAO.updatePassword(user);

        assertTrue(res, "La actualizacion de la contrasea deberia ser exitosa.");

        autenticate(user);
    }
    @Test
    void testUserDAO() throws SQLException{
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String strEmail = "test" + num + "@example.com";
        User user = new User(0,"Test User", "password", strEmail,(byte)2);

        User testUser = create(user);

        update(testUser);

        search(testUser);

        testUser.setPasswordHash(user.getPasswordHash());

        autenticate(testUser);

        testUser.setPasswordHash("12345");
        autenticate(testUser);

        testUser.setPasswordHash("new-password");
        updatePassword(testUser);
        testUser.setPasswordHash("new-password");
        autenticate(testUser);

        delete(testUser);
    }
    @Test
    void createUser() throws SQLException{
        User user = new User(0, "admin", "12345", "admin@gmail.com",(byte)1);
        User res = userDAO.create(user);
        assertNotEquals(res, null);
    }

}