/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.StandardizationDemographic;
import net.cltech.enterprisent.domain.masters.test.CentralSystem;
import net.cltech.enterprisent.domain.masters.test.HomologationCode;
import net.cltech.enterprisent.domain.masters.test.Standardization;
import net.cltech.enterprisent.domain.masters.user.StandardizationUser;

/**
 * Interfaz de servicios a la informacion del maestro Sistema Central
 *
 * @version 1.0.0
 * @author cmartin
 * @since 06/06/2017
 * @see Creación
 */
public interface CentralSystemService
{

    /**
     * Lista los sistemas centrales desde la base de datos.
     *
     * @return Lista de sistemas centrales.
     * @throws Exception Error en la base de datos.
     */
    public List<CentralSystem> list() throws Exception;

    /**
     * Registra una nueva Sistema Central en la base de datos.
     *
     * @param centralSystem Instancia con los datos del Sistema Central.
     *
     * @return Instancia con los datos del Sistema Central.
     * @throws Exception Error en la base de datos.
     */
    public CentralSystem create(CentralSystem centralSystem) throws Exception;

    /**
     * Obtener información de un Sistema Central por un campo especifico.
     *
     * @param id ID del Sistema Central a ser consultada.
     * @param name Nombre del Sistema Central a ser consultada.
     *
     * @return Instancia con los datos del Sistema Central.
     * @throws Exception Error en la base de datos.
     */
    public CentralSystem get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de un Sistema Central en la base de datos.
     *
     * @param centralSystem Instancia con los datos del Sistema Central.
     *
     * @return Objeto del Sistema Central modificada.
     * @throws Exception Error en la base de datos.
     */
    public CentralSystem update(CentralSystem centralSystem) throws Exception;

    /**
     * Obtener información de un sistema central por estado.
     *
     * @param state Estado de los sistemas centrales a ser consultados
     *
     * @return Instancia con los datos del sistema central.
     * @throws Exception Error en la base de datos.
     */
    public List<CentralSystem> list(boolean state) throws Exception;

    /**
     * Consulta lista de examenes con sus códigos de homologación
     *
     * @param centralSystem id sistema central
     * @param all si en la consulta se incluyen paquetes
     *
     * @return Lista de examenes
     * @throws Exception Error en la base de datos.
     */
    public List<Standardization> standardizationList(int centralSystem, boolean all) throws Exception;
    
    /**
     * consulta todos los codigos de homologacion de los examenes en un sistema central 
     *
     * @param centralSystem id sistema central
     * @param all si en la consulta se incluyen paquetes
     *
     * @return Lista de examenes
     * @throws Exception Error en la base de datos.
     */
    public List<HomologationCode> gethomologationcodes(int centralSystem) throws Exception;

    /**
     * Valida si el código central existe en un examen diferente al enviado
     *
     * @param centralSystem id del sistema central
     * @param code codego que desea validar
     * @param test id del examen al que se le dea hacer la validación
     *
     * @return si existe el código en otro examen
     * @throws Exception Error en la base de datos.
     */
    public boolean standardizationCodeExists(int centralSystem, String code, int test) throws Exception;

    /**
     * Realiza la homologación de un examen
     *
     * @param standardization bean con los codigos de homologacion
     *
     * @return bean con los codigos de homologacion
     * @throws Exception Error en la base de datos.
     */
    public Standardization addStandardizationTest(Standardization standardization) throws Exception;

    /**
     * Consulta los demograficos item por sistema central y demografico.
     *
     * @param idCentralSystem Id sistema central.
     * @param idDemographic Id demografico
     *
     * @return Lista de demograficos, incluyendo los demograficos por defecto
     * @throws Exception Error en la base de datos.
     */
    public List<StandardizationDemographic> demographicsItemList(int idCentralSystem, int idDemographic) throws Exception;

    /**
     * Insertas la homologacion de demograficos
     *
     * @param demographic bean con datos a insertar
     *
     * @return informacion de datos insertados
     * @throws Exception Error en la base de datos.
     */
    public StandardizationDemographic insertStandardizationDemographic(StandardizationDemographic demographic) throws Exception;

    /**
     * Valida si el código central existe en un demografico item diferente al
     * enviado
     *
     * @param centralSystem id del sistema central
     * @param code codigo que desea validar
     * @param demographic Id del demografico
     * @param demographicItem Id del demografico item
     *
     * @return si existe el código en otro examen
     * @throws Exception Error en la base de datos.
     */
    public boolean standardizationCodeExists(int centralSystem, String code, int demographic, int demographicItem) throws Exception;

    /**
     * Inserta homologaciones de demograficos
     *
     * @param demographics bean con datos a insertar
     *
     * @return cantidad de datos insertados
     * @throws Exception Error en la base de datos.
     */
    public int insertStandardizationDemographicAll(List<StandardizationDemographic> demographics) throws Exception;

    /**
     * Consulta los usuarios por sistema central.
     *
     * @param idCentralSystem Id sistema central.
     *
     * @return Lista de usuarios.
     * @throws Exception Error en la base de datos.
     */
    public List<StandardizationUser> usersList(int idCentralSystem) throws Exception;

    /**
     * Insertas la homologacion de usuarios
     *
     * @param user bean con datos a insertar
     *
     * @return informacion de datos insertados
     * @throws Exception Error en la base de datos.
     */
    public StandardizationUser insertStandardizationUser(StandardizationUser user) throws Exception;

    /**
     * Valida si el código central existe en un usuario diferente al enviado
     *
     * @param centralSystem id del sistema central
     * @param code codigo que desea validar
     * @param user Id del usuario
     *
     * @return si existe el código en otro examen
     * @throws Exception Error en la base de datos.
     */
    public boolean standardizationUserCodeExists(int centralSystem, String code, int user) throws Exception;
}
