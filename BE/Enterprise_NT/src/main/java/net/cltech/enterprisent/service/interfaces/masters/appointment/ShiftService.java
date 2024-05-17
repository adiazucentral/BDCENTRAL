/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.appointment;

import java.util.List;
import net.cltech.enterprisent.domain.masters.appointment.Shift;
import net.cltech.enterprisent.domain.masters.demographic.Branch;

/**
 * Servicios de jornadas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 14/08/2018
 * @see Creacion
 */
public interface ShiftService
{

    /**
     * Lista las jornadas desde la base de datos.
     *
     * @return Lista de jornadas.
     * @throws Exception Error en la base de datos.
     */
    public List<Shift> list() throws Exception;

    /**
     * Registra una nueva jornada en la base de datos.
     *
     * @param shift Instancia con los datos de la jornada.
     *
     * @return Instancia con los datos de la jornada.
     * @throws Exception Error en la base de datos.
     */
    public Shift create(Shift shift) throws Exception;

    /**
     * Obtener información de una jornada por una campo especifico.
     *
     * @param id ID de la jornada a ser consultado.
     * @param name Nombre de la jornada a ser consultado.
     *
     * @return Instancia con los datos de la jornada.
     * @throws Exception Error en la base de datos.
     */
    public Shift get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de una jornada en la base de datos.
     *
     * @param shift Instancia con los datos de la jornada.
     *
     * @return Objeto de la jornada modificado.
     * @throws Exception Error en la base de datos.
     */
    public Shift update(Shift shift) throws Exception;

    /**
     * Obtener información de jornadas por estado.
     *
     * @param state Estado de las jornadas a ser consultados
     *
     * @return Instancia con los datos de la jornada.
     * @throws Exception Error en la base de datos.
     */
    public List<Shift> list(boolean state) throws Exception;
    
    /**
     * Inserta las jornadas asociadas a una sede
     *
     * @param branch
     * @return Registros afectados
     * @throws java.lang.Exception Error en la base de datos
     */
    public int insertShiftsbyBranch(Branch branch) throws Exception;
    
    /**
     * consulta las jornadas asociadas a una sede
     *
     * @param branch
     * @return Registros afectados
     * @throws java.lang.Exception Error en la base de datos
     */
    public List<Shift> listShiftsbyBranch(Integer idBranch) throws Exception;
    
    public List<Shift> listShift(Integer branch, int date, int day) throws Exception;

}
