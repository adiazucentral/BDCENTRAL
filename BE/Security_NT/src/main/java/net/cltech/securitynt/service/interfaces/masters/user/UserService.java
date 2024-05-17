package net.cltech.securitynt.service.interfaces.masters.user;

import java.util.List;
import net.cltech.securitynt.domain.common.AuthenticationUserType;
import net.cltech.securitynt.domain.common.AuthorizedUser;
import net.cltech.securitynt.domain.masters.user.User;
import net.cltech.securitynt.domain.masters.user.UserPassword;

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
     * @param userName nombre de usuario
     * @param password contraseña
     * @param branch id sede de acceso
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws net.cltech.securitynt.domain.exception.EnterpriseNTException
     */
    public AuthorizedUser authenticate(String userName, String password, Integer branch) throws Exception;
    
     /**
     * Verifica las credenciales enviadas
     *
     * @param userName nombre de usuario
     * @param password contraseña     
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws net.cltech.securitynt.domain.exception.EnterpriseNTException
     */
    //public AuthorizedUser authenticateWeb(String userName, String password) throws Exception;
     
    /**
     * Verifica las credenciales enviadas con el tipo de usuario
     *
     * @param authenticatingUser Usuario que se autentica desde consulta web
     * 
     * @return Instancia con los datos de la unidad de medida.
     * @throws net.cltech.securitynt.domain.exception.EnterpriseNTException
     */   
     public AuthorizedUser authenticateWeb(AuthenticationUserType authenticatingUser) throws Exception;
    
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
     * Reiniciar contador fallas de inicio de sesion
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public boolean updateCountFail() throws Exception;
    
    /**
     * Reiniciar contador fallas de inicio de sesion e consulta web
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public boolean updateCountFailWeb() throws Exception;

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
     *
     * @return Si se actualizo correctamente la contraseña.
     * @throws Exception Error en la base de datos.
     */
    public boolean updatePassword(UserPassword userPassword) throws Exception;  
    
    /**
     * Actualiza la contraseña de un usuario en consulta web en la base de datos.
     *
     * @param userPassword Instancia con los datos para cambiar la contraseña.
     *
     * @return Si se actualizo correctamente la contraseña.
     * @throws Exception Error en la base de datos.
     */
    public boolean updatePasswordWeb(UserPassword userPassword) throws Exception;    

    /**
     * Realiza la validacion de inactividad para desactivar los usuarios.
     *
     * @param users Lista de
     * {@link net.cltech.securitynt.domain.masters.user.User}
     * @param token
     * @throws Exception Error en la base de datos.
     */
    public void deactivateUsers(List<User> users, String token) throws Exception;
    

    /**
     * Realiza la validacion de inactividad para desactivar los usuarios.
     *
     * @param user
     * @throws Exception Error en la base de datos.
     */
    public void changeStateUser(User user) throws Exception;
    
    /**
     * Verifica las credenciales enviadas sin que necesariamente el producto este licenciado
     *
     * @param userName nombre de usuario
     * @param password contraseña
     * @param branch id sede de acceso
     *
     * @return Instancia con los datos de autenticación
     * @throws net.cltech.securitynt.domain.exception.EnterpriseNTException
     */
    public AuthorizedUser integrationAuthentication(String userName, String password, Integer branch) throws Exception;
}
