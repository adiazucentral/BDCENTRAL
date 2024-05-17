/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.integration;

import java.io.IOException;
import java.util.List;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.OrderHisPendingResults;
import net.cltech.enterprisent.domain.DTO.integration.minsa.results.OrderPendingResults;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.epidemiology.EpidemiologicalEvents;
import net.cltech.enterprisent.domain.integration.generalMinsa.GeneralOrder;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.user.User;
import okhttp3.Response;

/**
 * Interfaz de servicios a la informacion para integrar con Minsa
 *
 * @version 1.0.0
 * @author bbonilla
 * @since 29/04/2022
 * @see Creación
 *
 * @author hpoveda
 * @since 29/04/2022
 * @see Creación
 */
public interface IntegrationMinsaService
{

    /**
     * Consultar resultados
     *
     * @param centralSytem
     * @return Lista de resultados.
     */
    List<OrderPendingResults> pendingResultsOrdersEpi(int centralSytem, int days);

    /**
     * Consultar resultados
     *
     * @return Lista de resultados.
     */
    public List<EpidemiologicalEvents> epidemiologicalEvents() throws Exception;

    /**
     * Insertar orden enviada al his
     *
     * @param hisPendingResults
     * @return Insercion exitosa
     */
    boolean insertOrderHis(List<OrderHisPendingResults> hisPendingResults);

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

    /**
     * Crea una orden y realiza otro tipo de procesamientos con la misma
     *
     * @param orders
     * @throws Exception
     */
    public void reportResult(List<GeneralOrder> orders) throws Exception;

}
