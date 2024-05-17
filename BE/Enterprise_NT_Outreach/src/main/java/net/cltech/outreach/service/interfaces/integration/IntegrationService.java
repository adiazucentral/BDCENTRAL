package net.cltech.outreach.service.interfaces.integration;

import java.io.IOException;
import java.util.List;
import net.cltech.outreach.domain.exception.EnterpriseNTException;

import okhttp3.Response;

/**
 * Interfaz de servicios a la informacion para integrar con otros sistemas.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 22/01/2020
 * @see Creación
 */
public interface IntegrationService
{

 


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
