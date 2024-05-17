/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.Procedure;
import net.cltech.enterprisent.domain.masters.microbiology.TestProcedure;

/**
 * Interfaz de servicios a la informacion del maestro Procedimiento
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/08/2017
 * @see Creación
 */
public interface ProcedureService
{

    /**
     * Lista los procedimientos desde la base de datos.
     *
     * @return Lista de procedimientos.
     * @throws Exception Error en la base de datos.
     */
    public List<Procedure> list() throws Exception;

    /**
     * Registra un nuevo Procedimiento en la base de datos.
     *
     * @param procedure Instancia con los datos del Procedimiento.
     *
     * @return Instancia con los datos del Procedimiento.
     * @throws Exception Error en la base de datos.
     */
    public Procedure create(Procedure procedure) throws Exception;

    /**
     * Obtener información de un Procedimiento por un campo especifico.
     *
     * @param id ID del Procedimiento a ser consultado.
     * @param code Codigo del Procedimiento a ser consultado.
     * @param name Nombre del Procedimiento a ser consultado.
     *
     * @return Instancia con los datos de la Procedimiento.
     * @throws Exception Error en la base de datos.
     */
    public Procedure get(Integer id, String code, String name) throws Exception;

    /**
     * Actualiza la información de un Procedimiento en la base de datos.
     *
     * @param procedure Instancia con los datos de la Procedimiento.
     *
     * @return Objeto de la Procedimiento modificada.
     * @throws Exception Error en la base de datos.
     */
    public Procedure update(Procedure procedure) throws Exception;

    /**
     * Obtener información de un Procedimiento por estado.
     *
     * @param state Estado de los procedimientos a ser consultados
     *
     * @return Instancia con los datos del Procedimiento.
     * @throws Exception Error en la base de datos.
     */
    public List<Procedure> list(boolean state) throws Exception;

    /**
     * Lista los procedimientos por prueba desde la base de datos.
     *
     * @param idtest Id del examen
     * @return Lista de medios de cultivo por prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<TestProcedure> listTestProcedure(Integer idtest) throws Exception;

    /**
     * Inserta los procedimientos por prueba desde la base de datos.
     *
     * @param testProcedures Instancia con los datos de la prueba y
     * procedimiento.
     * @return registros insertados
     * @throws Exception Error en la base de datos
     */
    public int insertTestProcedure(List<TestProcedure> testProcedures) throws Exception;

}
