/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.service.interfaces.security;

import java.util.List;
import net.cltech.outreach.domain.common.AuthenticationUser;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.domain.common.Email;
import net.cltech.outreach.domain.common.JWTToken;
import net.cltech.outreach.domain.masters.configuration.UserPassword;

/**
 * Servicios de autenticacion de usuarios para la consulta web.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 24/04/2017
 * @see Creacion
 */
public interface AuthenticationService
{

    /**
     * Verifica las credenciales enviadas
     *
     * @param userName nombre de usuario
     * @param password contraseña
     * @param type Tipo de acceso: 1 -> Medico, 2 -> Paciente, 3 -> Cliente, 4
     * -> Usuario
     * @param historyType
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws Exception Error en la base de datos.
     */
    public AuthorizedUser authenticate(String userName, String password, Integer type, Integer historyType) throws Exception;

     /**
     * Verifica las credenciales enviadas
     *
     * @param user
     * {@link net.cltech.enterprisent.domain.common.AuthenticationUser}
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws java.lang.Exception
     */
    public JWTToken authenticateWeb(AuthenticationUser user) throws Exception;
    
    /**
     * Metodo para solicitar restablecimiento de la contraseña
     *
     * @param userName nombre de usuario
     * @param type tipo de usuario
     * @param historyNumber Si el tipo de usuario es 2 (paciente) 
     * el numero de la historia del paciente nos servira para hacer una busqueda mas especifica. 
     * 
     * @return Token para restaurar contraseña
     * @throws Exception Error en la solicitud
     */
    public List<JWTToken> recovery(String userName, Integer type, String historyNumber) throws Exception;

    /**
     * Metodo para establecer la nueva contraseña
     *
     * @param password nueva contraseña
     * @return registros afectados
     * @throws Exception Error en el servicio
     */
    public int reset(String password) throws Exception;

    /**
     * Realiza el envio de correo
     *
     * @param email informacion del correo
     * @return informaciónde del correo
     * @throws Exception Error en el servicio
     */
    public String sendEmail(Email email) throws Exception;
    
     /**
     * Actualiza la contraseña de un usuario en la base de datos.
     *
     * @param userPassword Instancia con los datos para cambiar la contraseña.
     * @throws Exception Error en la base de datos.
     */
    public void updatePassword(UserPassword userPassword) throws Exception;
    
    

}
