package net.cltech.enterprisent.service.interfaces.masters.user;

import java.util.List;
import net.cltech.enterprisent.domain.common.AuthenticationUser;
import net.cltech.enterprisent.domain.common.JWTToken;
import net.cltech.enterprisent.domain.common.UserHomeBound;
import net.cltech.enterprisent.domain.masters.test.ExcludeTest;
import net.cltech.enterprisent.domain.masters.user.Email;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.masters.user.UserAnalyzer;
import net.cltech.enterprisent.domain.masters.user.UserByBranchByAreas;
import net.cltech.enterprisent.domain.masters.user.UserIntegration;
import net.cltech.enterprisent.domain.masters.user.UserPassword;
import net.cltech.enterprisent.domain.masters.user.UserRecoveryPassword;

/**
 * Servicios de usuarios
 *
 * @version 1.0.0
 * @author eacuna
 * @since 25/04/2017
 * @see Creacion
 *
 * @author cmartin
 * @version 1.0.0
 * @since 12/05/2017
 * @see Se agregaron metodos para el funcionamiento maestro usuario.
 */
public interface UserService
{

    /**
     * Verifica las credenciales enviadas
     *
     * @param user
     * {@link net.cltech.enterprisent.domain.common.AuthenticationUser}
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws java.lang.Exception
     */
    public JWTToken authenticate(AuthenticationUser user) throws Exception;

    /**
     * Lista los usuarios desde la base de datos.
     *
     * @return Lista de usuarios.
     * @throws Exception Error en la base de datos.
     */
    public List<User> list() throws Exception;

    /**
     * Lista los usuarios simple desde la base de datos.
     *
     * @return Lista de usuarios.
     * @throws Exception Error en la base de datos.
     */
    public List<User> SimpleUserList() throws Exception;

    /**
     * Lista de usuarios con id y fecha de ultimo ingreso al sistema, para
     * verifiacion de estado.
     *
     * @return Lista de usuarios.
     * @throws Exception Error en la base de datos.
     */
    public List<User> listDeactivate() throws Exception;

    /**
     * Registra una nueva usuario en la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public User create(User user) throws Exception;

    /**
     * Obtener información de un usuario por un campo especifico.
     *
     * @param id ID del usuario a ser consultada.
     * @param username Usuario del usuario a ser consultada.
     * @param identification Identificacion del usuario a ser consultada.
     * @param signatureCode Codigo firma del usuario a ser consultada.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public User get(Integer id, String username, String identification, String signatureCode) throws Exception;

    /**
     * si el username tiene permisos para agregar examenes en ingreso de ordenes.
     *
     * @param username Usuario del usuario a ser consultada.
     * @param password password del usuario a ser consultada.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public User getUpdateTestEntry(String username, String password) throws Exception;

    /**
     * Obtener información basica del usuario
     *
     * @param id ID del usuario a ser consultada.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public User getBasicUser(Integer id) throws Exception;

    /**
     * Actualiza la información de un usuario en la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     *
     * @return Objeto del usuario modificada.
     * @throws Exception Error en la base de datos.
     */
    public User update(User user) throws Exception;

    /**
     * Cambiar estado usuario
     *
     * @param user Instancia con los datos del usuario.
     *
     * @return Objeto del usuario modificada.
     * @throws Exception Error en la base de datos.
     */
    public User changeStateUser(User user) throws Exception;

    /**
     *
     * Elimina un usuario de la base de datos.
     *
     * @param id ID del usuario.
     *
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;

    /**
     * Obtener información de un usuario por estado.
     *
     * @param state Estado de los usuarios a ser consultados
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public List<User> list(boolean state) throws Exception;

    /**
     * Inserta los examenes relacionados al usuario
     *
     * @param excludeList lista de examenes
     *
     * @return registros insertados
     * @throws Exception Error en la base de datos.
     */
    public int insertTest(List<ExcludeTest> excludeList) throws Exception;

    /**
     * Elimina examenes los examenes relacionados al usuario
     *
     * @param id id usuario
     *
     * @return registros eliminados
     * @throws Exception Error en la base de datos.
     */
    public int deleteTest(Integer id) throws Exception;

    /**
     * lista los examenes relacionados al usuario
     *
     * @param id
     *
     * @return lista de examenes
     * @throws Exception Error en la base de datos.
     */
    public List<ExcludeTest> listTest(Integer id) throws Exception;

    /**
     * Actualiza la contraseña de un usuario en la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     *
     * @return Si se actualizo correctamente la contraseña.
     * @throws Exception Error en la base de datos.
     */
    public boolean updateProfile(User user) throws Exception;

    /**
     * Actualiza la contraseña de un usuario en la base de datos.
     *
     * @param userPassword Instancia con los datos para cambiar la contraseña.
     * @throws Exception Error en la base de datos.
     */
    public void updatePassword(UserPassword userPassword) throws Exception;

    /**
     * Recuperar la contraseña de un usuario.
     *
     * @param userPassword Instancia con los datos para recuperar la contraseña.
     * @throws Exception Error en la base de datos.
     */
    public void recoverPassword(UserRecoveryPassword userPassword) throws Exception;

    /**
     * Realiza el envio de correo
     *
     * @param email informacion del correo
     * @return informaciónde del correo
     * @throws Exception Error en el servicio
     */
    public String sendEmail(Email email) throws Exception;

    /**
     * Realiza la consulta que trae los usuarios por sede y areas
     *
     * @param filter Objeto con informacion del filtro ha realizar
     * @return Lista de {@link net.cltech.enterprisent.domain.masters.user.User}
     * @throws Exception Error en el servicio
     */
    public List<User> getByBranchAreas(UserByBranchByAreas filter) throws Exception;

    /**
     * Registra una nueva usuario para integracion o analizador en la base de
     * datos.
     *
     * @param user Instancia con los datos del usuario.
     *
     * @return Instancia con los datos del usuario.
     * {@link net.cltech.enterprisent.domain.masters.user.User}
     * @throws Exception Error en la base de datos.
     */
    public User createByType(User user) throws Exception;
    
    /**
     * Registra una nueva usuario para integracion o analizador en la base de
     * datos.
     * @param code
     * @return Instancia con los datos del usuario.
     * {@link net.cltech.enterprisent.domain.masters.user.User}
     * @throws Exception Error en la base de datos.
     */
    public  UserIntegration userIntegration(String code) throws Exception;

    /**
     * Actualiza la información de un usuario para integracion o analizador en
     * la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     *
     * @return Objeto del usuario modificada.
     * @throws Exception Error en la base de datos.
     */
    public User updateByType(User user) throws Exception;

    /**
     * Verifica las credenciales enviadas en Home Bound
     *
     * @param user
     * {@link net.cltech.enterprisent.domain.common.AuthenticationUser}
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws java.lang.Exception
     */
    public JWTToken authenticateLaboratory(UserHomeBound user) throws Exception;

    /**
     * Obtiene una lista con los usuarios de analizadores con destino en
     * microbiologia
     *
     * @return Lista de usuarios
     * @throws java.lang.Exception
     */
    public List<UserAnalyzer> getUsersAnalyzers() throws Exception;

    /**
     * Verifica las credenciales enviadas sin que necesariamente el producto
     * este licenciado
     *
     * @param user
     * @return Instancia con los datos de autenticación
     * @throws java.lang.Exception
     */
    public JWTToken integrationAuthentication(AuthenticationUser user) throws Exception;
}
