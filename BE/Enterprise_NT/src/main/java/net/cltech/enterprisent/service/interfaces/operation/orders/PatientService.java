package net.cltech.enterprisent.service.interfaces.operation.orders;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.PatientPhoto;
import net.cltech.enterprisent.domain.operation.reports.PatientReport;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;

/**
 * Servicios sobre los pacientes de la aplicación
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/06/2017
 * @see Creacion
 */
public interface PatientService
{

    /**
     * Obtiene el paciente por id de base de datos
     *
     * @param id Id base de datos
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public Patient get(int id) throws Exception;
    /**
     * Obtiene el paciente por id de base de datos
     *
     * @param id Id base de datos
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public Patient getEmail(int id) throws Exception;

    /**
     * Obtiene el paciente por el numero de historia
     *
     * @param patientId Numero de historia
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public Patient get(String patientId) throws Exception;

    /**
     * Obtiene el paciente por el numero de historia y tipo de documento
     *
     * @param patientId Numero de historia
     * @param documentType Id tipo documento
     * @param id
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public Patient get(String patientId, int documentType, int id) throws Exception;

    /**
     * Obtiene el paciente por el demografico no codificado
     *
     * @param demographicId Id del demografico
     * @param demographicValue Value del demografico a buscar
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public Patient getPatienByDemographic(int demographicId, String demographicValue) throws Exception;

    /**
     * Obtiene el paciente de una orden
     *
     * @param order Numero de Orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public Patient get(long order) throws Exception;

    /**
     * Obtiene informacion basica del paciente de una orden
     *
     * @param order Numero de Orden
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public Patient getBasicPatientbyOrder(long order) throws Exception;

    /**
     * Obtiene el paciente de una orden pero todos los datos del paciente los
     * retorna como un lista de demograficos
     *
     * @param order Numero de Orden
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.DemographicValue}
     * en caso de no encontrar retornara un vacio
     * @throws Exception Error en el servicio
     */
    public List<DemographicValue> getAsListDemographics(long order) throws Exception;

    /**
     * Obtiene el paciente por historia pero todos los datos del paciente los
     * retorna como un lista de demograficos
     *
     * @param patientId Historia
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.DemographicValue}
     * en caso de no encontrar retornara un vacio
     * @throws Exception Error en el servicio
     */
    public List<DemographicValue> getByPatientIdAsListDemographics(String patientId) throws Exception;

    /**
     * Obtiene el paciente por historia pero todos los datos del paciente los
     * retorna como un lista de demograficos
     *
     * @param patientId Historia
     * @param documentType Id tipo documento
     * @param id
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.orders.DemographicValue}
     * en caso de no encontrar retornara un vacio
     * @throws Exception Error en el servicio
     */
    public List<DemographicValue> getByPatientIdAsListDemographics(String patientId, int documentType, int id) throws Exception;

    /**
     * Crea un paciente en el sistema con sus demograficos
     *
     * @param patient
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @param user Id usuario de creación
     *
     * @return Paciente creado en base de datos
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @throws Exception Error en el servicio
     */
    public Patient create(Patient patient, int user) throws Exception;

    /**
     * Actualiza la informacion de un paciente
     *
     * @param patient
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @param user Id usuario de creación
     *
     * @return Paciente creado en base de datos
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @throws Exception Error en el servicio
     */
    public Patient update(Patient patient, int user) throws Exception;

    /**
     * Guarda o actualiza un paciente verificando si existe en base de datos
     *
     * @param patient
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @param user Id usuario de creación
     * @param updatePatient
     *
     * @return Paciente creado o actualizado en base de datos
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @throws Exception Error en el servicio
     */
    public Patient save(Patient patient, int user, boolean updatePatient) throws Exception;

    /**
     * Guarda o actualiza un paciente verificando si existe en base de datos
     *
     * @param patient
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @param user Id usuario de creación
     *
     * @return Paciente creado o actualizado en base de datos
     * {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * @throws Exception Error en el servicio
     */
    public Patient save(Patient patient, int user) throws Exception;

    /**
     * Obtiene el paciente por el numero de historia y tipo de documento
     *
     * @param patientId Numero de historia
     * @param documentType Id tipo documento
     *
     * @return {@link net.cltech.enterprisent.domain.operation.orders.Patient}
     * null en caso de no encontrarlo
     * @throws Exception Error en el servicio
     */
    public Patient get(String patientId, int documentType) throws Exception;

    /**
     * Actualiza la foto de un paciente
     *
     * @param photo
     *
     * @throws Exception Error en base de datos
     */
    public void updatePhoto(PatientPhoto photo) throws Exception;

    /**
     * Obtiene la foto de un paciente
     *
     * @param id Id DB Paciente
     *
     * @return
     * {@link net.cltech.enterprisent.domain.operation.orders.PatientPhoto} Foto
     * en base64, null en caso de tener foto
     * @throws Exception Error en base de datos
     */
    public PatientPhoto getPhoto(int id) throws Exception;

    /**
     * Lista de pacientes por historia / tipo de docuemnto
     *
     * @param lastName Primer Apellido
     * @param surName Segundo Apellido
     * @param gender id del genero
     * @param demographics demograficos del paciente null para no consultar
     * demograficos
     *
     * @return Error
     * @throws java.lang.Exception
     */
    public List<Patient> listByLastName(String name, String name1, String lastName, String surName, int gender, List<Demographic> demographics) throws Exception;

    /**
     * Busca paciente por nombres, apellidos y fechas de nacimiento
     *
     * @param lastName Apellido
     * @param surName Segundo Apellido
     * @param name1 Nombre1
     * @param name2 Nombre2
     * @param sex Sexo
     * @param birthday Fecha de Nacimiento
     * @return Lista de pacientes
     * @throws Exception Error en el servicio
     */
    public List<Patient> getPatientBy(String lastName, String surName, String name1, String name2, Integer sex, Long birthday) throws Exception;

    /**
     * Registra el historico del paciente
     *
     * @param historicalResult
     * @param user
     * @return Lista de pacientes
     * @throws Exception Error en el servicio
     */
    public HistoricalResult createPatientHistory(HistoricalResult historicalResult, int user) throws Exception;

    /**
     * Actualizar el historico del paciente
     *
     * @param historicalResult
     * @return Lista de pacientes
     * @throws Exception Error en el servicio
     */
    public HistoricalResult updatePatientHistory(HistoricalResult historicalResult) throws Exception;

    /**
     * Obtiene la cantidad total de pacientes
     *
     * @return Lista de pacientes
     * @throws Exception Error en el servicio
     */
    public Integer numberPatients() throws Exception;

    /**
     * Obtiene la lista de pacientes por pagina
     *
     * @param patientReport
     * @return Lista de pacientes
     * @throws Exception Error en el servicio
     */
    public List<Patient> listPatientsByPag(PatientReport patientReport) throws Exception;

    /**
     * Obtiene un paciente con la informacion mas basica requerida
     *
     * @param documentType
     * @param history
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    public Patient getBasicPatientInformation(int documentType, String history) throws Exception;

    /**
     * Obtiene un paciente con la informacion mas basica requerida
     *
     * @param initialDate
     * @param endDate
     * @param patientStatus
     * @param filterType
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    public List<Patient> getBasicPatientInformationByDate(Integer initialDate, Integer endDate, int patientStatus, int filterType) throws Exception;

    /**
     * Actualiza la informacion mas basica requerida para un paciente
     *
     * @param patient
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    public int updateBasicPatientInformation(Patient patient) throws Exception;

    /**
     * Actualiza la informacion mas basica requerida para un paciente
     *
     * @param patient
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    public int updateStatePatient(Patient patient) throws Exception;
}
