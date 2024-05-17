/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.FeeSchedule;

/**
 * Interfaz de servicios a la informacion del maestro Vigencia
 *
 * @version 1.0.0
 * @author enavas
 * @since 10/08/2017
 * @see Creación
 */
public interface FeeScheduleService
{

    /**
     * Lista las vigencias desde la base de datos.
     *
     * @return Lista de vigencias.
     * @throws Exception Error en la base de datos.
     */
    public List<FeeSchedule> list() throws Exception;

    /**
     * Registra una nueva vigencia en la base de datos.
     *
     * @param feeSchedule Instancia con los datos de la vigencia.
     *
     * @return Instancia con los datos de la vigencia.
     * @throws Exception Error en la base de datos.
     */
    public FeeSchedule create(FeeSchedule feeSchedule) throws Exception;

    /**
     * Obtener información de una vigencia por un campo especifico.
     *
     * @param id ID de la vigencia a ser consultada.
     * @param name Nombre de la vigencia a ser consultada.
     *
     * @return Instancia con los datos de la vigencia.
     * @throws Exception Error en la base de datos.
     */
    public FeeSchedule get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de una vigencia en la base de datos.
     *
     * @param feeSchedule Instancia con los datos de la vigencia.
     *
     * @return Objeto de la vigencia modificada.
     * @throws Exception Error en la base de datos.
     */
    public FeeSchedule update(FeeSchedule feeSchedule) throws Exception;

    /**
     * Obtener información de unas vigencias por estado.
     *
     * @param state Estado de las vigencias a ser consultadas
     *
     * @return lista de Instancia con los datos de las vigencias.
     * @throws Exception Error en la base de datos.
     */
    public List<FeeSchedule> list(boolean state) throws Exception;

}
