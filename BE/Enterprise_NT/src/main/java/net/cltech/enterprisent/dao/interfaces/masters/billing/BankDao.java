package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Bank;

/**
 * Representa los métodos de acceso a base de datos para la información de los Bancos.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/06/2017
 * @see Creación
 */
public interface BankDao
{
    /**
     * Lista los bancos desde la base de datos.
     *
     * @return Lista de bancos.
     * @throws Exception Error en la base de datos.
     */
    public List<Bank> list() throws Exception;

    /**
     * Registra un nuevo banco en la base de datos.
     *
     * @param bank Instancia con los datos del banco.
     * @return Instancia con los datos del banco.
     * @throws Exception Error en la base de datos.
     */
    public Bank create(Bank bank) throws Exception;
    
    /**
     * Obtener información de un banco por un campo especifico.
     *
     * @param id ID del banco a ser consultado.
     * @param name Nombre del banco a ser consultado.
     * @return Instancia con los datos del banco.
     * @throws Exception Error en la base de datos.
     */
    public Bank get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de un banco en la base de datos.
     *
     * @param bank Instancia con los datos del banco.
     * @return Objeto del banco modificada.
     * @throws Exception Error en la base de datos.
     */
    public Bank update(Bank bank) throws Exception;

    /**
     *
     * Elimina un banco de la base de datos.
     *
     * @param id ID del banco.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
}
