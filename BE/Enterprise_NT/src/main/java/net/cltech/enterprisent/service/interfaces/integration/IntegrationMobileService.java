package net.cltech.enterprisent.service.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.mobile.AuthorizedPatient;
import net.cltech.enterprisent.domain.integration.mobile.ChangePassword;
import net.cltech.enterprisent.domain.integration.mobile.LisPatient;
import net.cltech.enterprisent.domain.integration.mobile.OrderEt;
import net.cltech.enterprisent.domain.integration.mobile.PatientEmailUpdate;
import net.cltech.enterprisent.domain.integration.mobile.RestorePassword;
import net.cltech.enterprisent.domain.integration.mobile.TestEt;

/**
 * Interfaz de servicios de la app Móvil
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 19/08/2020
 * @see Creación
 */
public interface IntegrationMobileService
{

    /**
     * Verifica las credenciales enviadas
     *
     * @param userName nombre de usuario
     * @param password contraseña
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws java.lang.Exception
     */
    public AuthorizedPatient getEtUserAuthentication(String userName, String password) throws Exception;

    /**
     * Actualiza la contraseña de un usuario en la base de datos.
     *
     * @param userPassword Instancia con los datos del usuario.
     *
     * @return Si se actualizo correctamente la contraseña.
     * @throws Exception Error en la base de datos.
     */
    public ChangePassword changePassword(ChangePassword userPassword) throws Exception;

    /**
     * Retorna el objeto de paciente de LIS
     *
     * @param patientId historia del paciente
     *
     * @return Instancia con los datos del paciente del LIS.
     * @throws java.lang.Exception
     */
    public LisPatient getPatientByPatientId(String patientId) throws Exception;

    /**
     * Lista las ordenes de un paciente para la app Móvil.
     *
     * @param limit NUmero de ordenes
     * @return lista de Objeto OrderEt que representa las ordenes para el
     * paciente en la app móvil
     * @throws Exception Error en la base de datos.
     */
    public List<OrderEt> getEtOrders(int limit) throws Exception;

    /**
     * Lista las ordenes de un paciente para la app Móvil.
     *
     * @return lista de Objeto TestEt, Obtiene los examenes que estan marcados
     * para ver en ingreso de ordenes
     *
     * @throws Exception Error en la base de datos.
     */
    public List<TestEt> getEtTest() throws Exception;
    
    /**
     * restaura la contraseña de un paciente para la app Móvil.
     *
     * @param lisPatient
     * @return RestorePassword paciente en la app móvil
     * @throws Exception Error en la base de datos.
     */
    public RestorePassword restorPatientPassword(LisPatient lisPatient) throws Exception;

    /**
     * Recibe un id de una orden y retorna un dato en pdf
     *
     * @param order
     *
     * @return Dato byte[] para archivo pdf
     * @throws Exception Error presentado en el servicio
     */
    public byte[] getPDFOrder(long order) throws Exception;
    
    /**
     * Actualiza el correo electronico de un paciente según el id del paciente que se envie
     *
     * @param patient 
     * @return Cantidad de registros afectados
     * @throws Exception Error presentado en el servicio
     */
    public int patientEmailUpdate(PatientEmailUpdate patient) throws Exception;
}
