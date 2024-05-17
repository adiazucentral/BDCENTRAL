/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.integration;

import java.io.IOException;
import java.util.List;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.user.User;
import okhttp3.Response;

/**
 * Interfaz de servicios a la informacion para integrar con otros sistemas.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/08/2018
 * @see Creación
 */
public interface IntegrationService
{

    /**
     * Listar usuarios.
     *
     * @return Lista de usuarios.
     * @throws Exception Error en la base de datos.
     */
    public List<User> listUser() throws Exception;

    /**
     * Listar usuarios simple.
     *
     * @return Lista de usuarios.
     * @throws Exception Error en la base de datos.
     */
    public List<User> listUserSimple() throws Exception;

    /**
     * Listar sedes.
     *
     * @return Lista de sedes.
     * @throws Exception Error en la base de datos.
     */
    public List<Branch> listBranches() throws Exception;

    /**
     *
     * @param response
     * @throws IOException
     * @throws EnterpriseNTException
     */
    public void processResponseError(Response response) throws IOException, EnterpriseNTException;

    /**
     * Metodo para consumir servicios rest de tipo post en la cual se deba
     * manejar los errores
     *
     * @param <T> Cualquier tipo de objeto
     * @param data Data requerida por la peticion
     * @param valueTypeReturn De que clase va a ser el valor a devolver
     * @param url Direccion url del servicio a consumir
     * @return
     * @throws Exception
     */
    public <T> T post(String data, Class<T> valueTypeReturn, String url) throws Exception;

    /**
     * Metodo para consumir servicios rest de tipo put en la cual se deba
     * manejar los errores
     *
     * @param <T> Cualquier tipo de objeto
     * @param data Data requerida por la peticion
     * @param valueTypeReturn De que clase va a ser el valor a devolver
     * @param url Direccion url del servicio a consumir
     * @return
     * @throws Exception
     */
    public <T> T put(String data, Class<T> valueTypeReturn, String url) throws Exception;

    /**
     * Metodo para consumir servicios rest de tipo put que no retorna valor en
     * la cual se deba manejar los errores
     *
     * @param <T> Cualquier tipo de objeto
     * @param data Data requerida por la peticion
     * @param url Direccion url del servicio a consumir
     * @throws Exception
     */
    public <T> void putVoid(String data, String url) throws Exception;

    /**
     * Metodo para consumir servicios rest de tipo get en la cual se deba
     * manejar los errores, que solo retorna un objeto
     *
     * @param <T> Cualquier tipo de objeto
     * @param valueTypeReturn De que clase va a ser el valor a devolver
     * @param url Direccion url del servicio a consumir
     * @return
     * @throws Exception
     */
    public <T> T get(Class<T> valueTypeReturn, String url) throws Exception;

    /**
     * Metodo para consumir servicios rest de tipo post en la cual se deba
     * manejar los errores, que retorna una lista de objetos
     *
     * @param <T> Cualquier tipo de objeto
     * @param valueTypeReturn De que clase va a ser el valor a devolver
     * @param url Direccion url del servicio a consumir
     * @return
     * @throws Exception
     */
    public <T> List<T> getList(Class<T> valueTypeReturn, String url) throws Exception;

    /**
     * Metodo para consumir servicios rest de tipo delete en la cual se deba
     * manejar los errores
     *
     * @param token Token opcional para el consumo del servicio
     * @param url Direccion url del servicio a consumir
     *
     * @throws Exception
     */
    public void delete(String url, String token) throws Exception;
}
