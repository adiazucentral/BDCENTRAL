package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.RatesOfAccount;

/**
 * Interfaz de servicios a la informacion del maestro Clientes
 *
 * @version 1.0.0
 * @author cmartin
 * @since 31/05/2017
 * @see Creación
 */
public interface AccountService
{

    /**
     * Lista los clientes desde la base de datos.
     *
     * @return Lista de clientes.
     * @throws Exception Error en la base de datos.
     */
    public List<Account> list() throws Exception;

    /**
     * Registra un nuevo cliente en la base de datos.
     *
     * @param account Instancia con los datos del cliente.
     *
     * @return Instancia con los datos del cliente.
     * @throws Exception Error en la base de datos.
     */
    public Account create(Account account) throws Exception;

    /**
     * Obtener información de un cliente por un campo especifico.
     *
     * @param id ID del cliente a ser consultado.
     * @param name Nombre del cliente a ser consultado.
     * @param nit Nit del cliente a ser consultado.
     * @param codeEps Codigo EPS del cliente a ser consultado.
     * @param username Usuario del cliente a ser consultado.
     *
     * @return Instancia con los datos del cliente.
     * @throws Exception Error en la base de datos.
     */
    public Account get(Integer id, String name, String nit, String codeEps, String username) throws Exception;

    /**
     * Actualiza la información de un cliente en la base de datos.
     *
     * @param account Instancia con los datos del cliente.
     *
     * @return Objeto del account modificada.
     * @throws Exception Error en la base de datos.
     */
    public Account update(Account account) throws Exception;

    /**
     * Obtener información de un cliente por estado.
     *
     * @param state Estado de los clientes a ser consultadas
     *
     * @return Instancia con los datos del área.
     * @throws Exception Error en la base de datos.
     */
    public List<Account> list(boolean state) throws Exception;

    /**
     *
     * Lista las tarifas asociadas al cliente
     *
     * @param id ID del cliente.
     * @return lista de tarifas del cliente.
     *
     * @throws Exception Error en base de datos.
     */
    public List<RatesOfAccount> getRates(Integer id) throws Exception;
    
    /**
     *
     * Lista las tarifas asociadas al cliente
     *
     * @param id ID del cliente.
     * @return lista de tarifas del cliente.
     *
     * @throws Exception Error en base de datos.
     */
    public List<RatesOfAccount> getRatesByAccount(Integer id) throws Exception;

    /**
     *
     * Inserta las tarifas asociadas al cliente
     *
     * @param ratesOfAccounts lista de tarifas del cliente.
     * @return la cantidad de registros insertados.
     *
     * @throws Exception Error en base de datos.
     */
    public int insertRates(List<RatesOfAccount> ratesOfAccounts) throws Exception;

    /**
     *
     * Elimina las tarifas asociadas al cliente
     *
     * @param idAccount Id del cliente.
     * @return numero de registro elinador.
     *
     * @throws Exception Error en base de datos.
     */
    public int deleteRates(Integer idAccount) throws Exception;

}
