package net.cltech.enterprisent.service.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Rate;

/**
 * Interfaz de servicios a la informacion del maestro Tarifas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 13/06/2017
 * @see Creación
 */
public interface RateService
{

    /**
     * Lista las tarifas desde la base de datos.
     *
     * @return Lista de tarifas.
     * @throws Exception Error en la base de datos.
     */
    public List<Rate> list() throws Exception;

    /**
     * Registra una nueva tarifa en la base de datos.
     *
     * @param rate Instancia con los datos de la tarifa.
     *
     * @return Instancia con los datos de la tarifa.
     * @throws Exception Error en la base de datos.
     */
    public Rate create(Rate rate) throws Exception;

    /**
     * Obtener información de una tarifa por un campo especifico.
     *
     * @param id   ID de la tarifa a ser consultada.
     * @param code Codigo de la tarifa a ser consultada.
     * @param name Nombre de la tarifa a ser consultada.
     *
     * @return Instancia con los datos de la tarifa.
     * @throws Exception Error en la base de datos.
     */
    public Rate get(Integer id, String code, String name) throws Exception;

    /**
     * Actualiza la información de una tarifa en la base de datos.
     *
     * @param rate Instancia con los datos de la tarifa.
     *
     * @return Objeto de la tarifa modificada.
     * @throws Exception Error en la base de datos.
     */
    public Rate update(Rate rate) throws Exception;

    /**
     * Obtener información de una tarifa por estado.
     *
     * @param state Estado de los tarifas a ser consultadas
     *
     * @return Instancia con los datos de los tarifas.
     * @throws Exception Error en la base de datos.
     */
    public List<Rate> list(boolean state) throws Exception;

    /**
     * Obtener pagadores.
     *
     * @return Instancia con los datos de los pagadores.
     * @throws Exception Error en la base de datos.
     */
    public List<Rate> listPayers() throws Exception;

}
