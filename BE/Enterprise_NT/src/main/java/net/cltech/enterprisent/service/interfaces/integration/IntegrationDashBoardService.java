package net.cltech.enterprisent.service.interfaces.integration;

import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardCommon;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardLicense;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardProductivity;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardSample;
import net.cltech.enterprisent.domain.integration.dashboard.DashboardConfiguration;
import net.cltech.enterprisent.domain.integration.dashboard.DashboardUrl;
import net.cltech.enterprisent.domain.integration.dashboard.TestDashboard;
import net.cltech.enterprisent.domain.integration.dashboard.TestValidDashboard;
import net.cltech.enterprisent.domain.integration.dashboard.UserDashboard;

/**
 * Interfaz de servicios a la informacion de tableros.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/07/2018
 * @see Creación
 */
public interface IntegrationDashBoardService
{

    /**
     * Lista de servicios.
     *
     * @return Lista de servicios.
     * @throws Exception Error en la base de datos.
     */
    public List<DashBoardCommon> listServices() throws Exception;

    /**
     * Lista de sedes.
     *
     * @return Lista de sedes.
     * @throws Exception Error en la base de datos.
     */
    public List<DashBoardCommon> listBranches() throws Exception;

    /**
     * Lista de examenes.
     *
     * @return Lista de sedes.
     * @throws Exception Error en la base de datos.
     */
    public List<DashBoardCommon> listTests() throws Exception;

    /**
     * Lista de areas.
     *
     * @return Lista de areas.
     * @throws Exception Error en la base de datos.
     */
    public List<DashBoardCommon> listAreas() throws Exception;

    /**
     * Lista la productividad por sede y sección
     *
     * @param brach
     * @param section
     * @param hours
     * @return conteo del estado de los examenes
     * @throws Exception Error en el servicio
     */
    public List<DashBoardProductivity> listProductivity(Integer brach, Integer section, Integer hours) throws Exception;

    /**
     * Lista de muestras
     *
     * @return conteo del estado de los examenes
     * @throws Exception Error en el servicio
     */
    public List<DashBoardSample> listSamples() throws Exception;

    /**
     * Envia la información para insertar tiempos de oportunidad en tableros.
     *
     * @param idOrder Numero de Orden
     * @param idTest Id del examen
     * @param action Indica la acción que se debe realizar: 1 -> Insertar, 2 ->
     * Actualizar, 3 -> Eliminar.
     */
    public void dashBoardOpportunityTime(Long idOrder, List<Integer> idTest, int action);
    
    /**
     * consulta los examenes validados y q no se han enviado a tableros.
     * @param days
     * @return TestValidDashboard
     */
    public TestValidDashboard getTestSendDashoboard(int days);
    
    
    /**
     * consulta los examenes validados y q no se han enviado a tableros.
     * @param days
     * @return TestValidDashboard
     */
    public TestValidDashboard getTestEntry(int days);
    
    
    /**
     * consulta los examenes validados y q no se han enviado a tableros.
     * @param orderNumber
     * @param idtest
     * @return TestValidDashboard
     */
    public boolean updateSentDashboard(long orderNumber, int idtest);
    
    /**
     * consulta los examenes validados y q no se han enviado a tableros.
     * @param orderNumber
     * @param idtest
     * @return TestValidDashboard
     */
    public boolean updateSentDashboardEntry(long orderNumber, int idtest);

    /**
     * Realiza la prueba de url para el tablero de dashboard.
     *
     * @param url
     * @return valido
     * @throws java.lang.Exception
     */
    public Boolean testUrl(DashboardUrl url) throws Exception;

    /**
     * Obtiene la lista de tableros configurados.
     *
     * @return valido
     * @throws net.cltech.enterprisent.domain.exception.EnterpriseNTException
     * @throws java.lang.Exception
     */
    public List<DashboardConfiguration> configuration() throws EnterpriseNTException, Exception;

    /**
     * Obtiene las licencias de los tableros.
     *
     * @param admin
     * @return lista de licencias
     * @throws net.cltech.enterprisent.domain.exception.EnterpriseNTException
     * @throws java.lang.Exception
     */
    public List<DashBoardLicense> licenses(boolean admin) throws Exception;

    /**
     * Obtiene usuario para tableros.
     *
     * @param user
     * @param password
     * @return user para tablero
     * @throws net.cltech.enterprisent.domain.exception.EnterpriseNTException
     * @throws java.lang.Exception
     */
    public List<UserDashboard> getUserDashboard(String user, String password) throws Exception;

    /**
     * Lista de examenes para el tablero.
     *
     * @return Lista de examenes.
     * @throws Exception Error en la base de datos.
     */
    public List<TestDashboard> listTestsDashboard() throws Exception;

    /**
     * Obtiene el licenciamiento de los tableros
     *
     * @return Licenciamientos de los tableros
     * @throws Exception Error al obtener los licenciamientos desde security
     */
    public HashMap<String, Boolean> boardLicenses() throws Exception;

    /**
     * Envia la información para insertar toma de muestras hospitalarias en
     * tableros.
     *
     * @param idOrder Numero de Orden
     * @param idSample
     * @param tests
     * @param action Indica la acción que se debe realizar: 1 -> Insertar, 2 ->
     * Actualizar, 3 -> Eliminar.
     */
    public void dashBoardHospitalSampling(Long idOrder, Integer idSample, List<Integer> tests, int action);
}
