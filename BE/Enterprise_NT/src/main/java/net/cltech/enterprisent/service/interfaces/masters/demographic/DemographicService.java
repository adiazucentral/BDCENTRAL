package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.configuration.DemographicReportEncryption;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.test.AlarmDays;
import net.cltech.enterprisent.domain.masters.test.ExcludeTest;
import net.cltech.enterprisent.domain.masters.test.TestBasic;

/**
 * Interfaz de servicios a la informacion del maestro Demografico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/05/2017
 * @see Creación
 */
public interface DemographicService {

    /**
     * Lista los demograficos desde la base de datos.
     *
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    public List<Demographic> list() throws Exception;

    /**
     * Registra un nuevo demografico en la base de datos.
     *
     * @param demographic Instancia con los datos del demografico.
     *
     * @return Instancia con los datos del demografico.
     * @throws Exception Error en la base de datos.
     */
    public Demographic create(Demographic demographic) throws Exception;

    /**
     * Obtener información de un demografico por un campo especifico.
     *
     * @param id ID del demografico a ser consultado.
     * @param name Nombre del demografico a ser consultado.
     * @param sort Ordenamiento
     *
     * @return Instancia con los datos del área.
     * @throws Exception Error en la base de datos.
     */
    public Demographic get(Integer id, String name, Integer sort) throws Exception;

    /**
     * Actualiza la información de un demografico en la base de datos.
     *
     * @param demographic Instancia con los datos del demografico.
     *
     * @return Objeto del demografico modificado.
     * @throws Exception Error en la base de datos.
     */
    public Demographic update(Demographic demographic) throws Exception;

    /**
     *
     * Elimina un demografico de la base de datos.
     *
     * @param id ID del demografico.
     *
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;

    /**
     * Lista los demograficos desde la base de datos por estado.
     *
     * @param state Estado.
     *
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    public List<Demographic> list(boolean state) throws Exception;

    /**
     * Lista los demograficos desde la base de datos por estado.
     *
     * @param filterdemographics
     *
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    public List<Demographic> getDemographicIds(List<Integer> filterdemographics) throws Exception;

    /**
     * Lista los demograficos desde la base de datos por el campo codificado.
     *
     * @param encoded
     * @return Lista de demograficos.
     * @throws Exception Error en la base de datos.
     */
    public List<Demographic> listByEncoded(boolean encoded) throws Exception;

    /**
     * Lista los demograficos filtrandolos por el origen activos
     *
     * @param source Origen: H->Historia, O->Orden.
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.demographic.Demographic}.
     * @throws Exception Error en la base de datos.
     */
    public List<Demographic> listBySource(String source) throws Exception;

    /**
     * Lista examenes excluidos de un demografico item
     *
     * @param id id demografico
     * @param idItem id item
     *
     * @return lista examenes
     * @throws Exception Error en la base de datos
     */
    public List<ExcludeTest> listDemographicTest(Integer id, Integer idItem) throws Exception;

    /**
     * Elimina examenes que se excluyen de un demografico item
     *
     * @param id id demografico
     * @param idItem id demografico item
     *
     * @return registros insertados
     * @throws Exception Error en la base de datos
     */
    public int deleteDemographicTest(Integer id, Integer idItem) throws Exception;

    /**
     * Inserta demograficos
     *
     * @param tests lista de demograficos
     *
     * @return registros insertados
     * @throws Exception Error en base de datos
     */
    public int insertDemographicTest(List<ExcludeTest> tests) throws Exception;

    /**
     * Consulta los demograficos
     *
     * @return Lista de demograficos, incluyendo los demograficos por defecto
     * @throws Exception Error en la base de datos.
     */
    public List<Demographic> demographicsList() throws Exception;
    
    /**
     * Consulta los demograficos
     *
     * @param origin
     * @return Lista de demograficos, incluyendo los demograficos por defecto
     * @throws Exception Error en la base de datos.
     */
    public List<Demographic> demographicsListordering(String origin) throws Exception;

    /**
     * Inserta examenes con días de alarma
     *
     * @param alarmDays lista de examene con dias de alarma
     *
     * @return registros insertados
     * @throws Exception Error en la base de datos
     */
    public int insertAlarmDaysTest(AlarmDays alarmDays) throws Exception;

    /**
     * Elimina examenes con días de alarma
     *
     * @param idDemographic id demografico
     * @param demographicItem id demografico item
     *
     * @return registros eliminados
     * @throws Exception Error en la base de datos
     */
    public int deleteAlarmDaysTest(Integer idDemographic, Integer demographicItem) throws Exception;

    /**
     * Listar examenes con días de alarma
     *
     * @param idDemographic id demografico
     * @param demographicItem id demografico item
     *
     * @return lista examenes con dias de alarma
     * @throws Exception Error en base de datos
     */
    public List<TestBasic> listAlarmDaysTest(Integer idDemographic, Integer demographicItem) throws Exception;

    /**
     * Actualizar el ordenamiento de una lista de demograficos
     *
     * @param demographics
     * @throws Exception Error en base de datos
     */
    public void updateOrder(List<Demographic> demographics) throws Exception;
    
    /**
     * Actualizar el ordenamiento de una lista de demograficos
     *
     * @param demographics
     * @throws Exception Error en base de datos
     */
    public void updateOrderAll(List<Demographic> demographics) throws Exception;

    /**
     * Inserta los demograficos con sus respectivos items en encriptación de
     * reportes por demografico
     *
     * @param demographics
     * @throws Exception Error en base de datos
     */
    public void saveDemographicReportEncrypt(List<DemographicReportEncryption> demographics) throws Exception;

    /**
     * Obtiene los items de un demografico correspondiente a encriptación de
     * reportes por demografico
     *
     * @param idDemographic
     * @return itemsDemographics correspondientes a encriptación de reportes por
     * demografico
     * @throws Exception Error en base de datos
     */
    public List<DemographicReportEncryption> getDemographicReportEncryptById(int idDemographic) throws Exception;

    /**
     * Obtiene los items de un demografico correspondiente a encriptación de
     * reportes por demografico
     *
     * @param idDemographic
     * @param demographicItem
     * @return itemsDemographics correspondientes a encriptación de reportes por
     * demografico
     * @throws Exception Error en base de datos
     */
    public List<DemographicReportEncryption> getDemographicByIdAndDemographicitem(int idDemographic, int demographicItem) throws Exception;
}
