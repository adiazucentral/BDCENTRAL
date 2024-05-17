package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.PypDemographic;
import net.cltech.enterprisent.domain.masters.test.AutomaticTest;
import net.cltech.enterprisent.domain.masters.test.Concurrence;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.Profile;
import net.cltech.enterprisent.domain.masters.test.ReferenceValue;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.TestByLaboratory;
import net.cltech.enterprisent.domain.masters.test.TestByService;
import net.cltech.enterprisent.domain.masters.test.TestInformation;

/**
 * Interfaz de servicios a la informacion del maestro Pruebas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/06/2017
 * @see Creación
 */
public interface TestService
{

    /**
     * Obtener información de una prueba por tipo, estado y area.
     *
     * @param type Tipo de las pruebas a ser consultadas
     * @param state Estado de las pruebas a ser consultadas
     * @param area Area de las pruebas a ser consultadas
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<TestBasic> list(int type, Boolean state, Integer area) throws Exception;

    /**
     * Obtener lista de pruebas en las que la prueba forme parte de la formula.
     *
     * @param id Id de la prueba.
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<TestBasic> list(Integer id) throws Exception;
    
   

    /**
     * Obtener información de una prueba por tipo.
     *
     * @param type Tipo de las pruebas a ser consultadas
     * @param state Estado de las pruebas a ser consultadas
     * @param area Area de las pruebas a ser consultadas
     * @param processingBy Procesar por de las pruebas a ser consultadas
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<TestBasic> listByResultType(int type, Boolean state, Integer area, Short processingBy) throws Exception;

    /**
     * Registra una nueva prueba en la base de datos.
     *
     * @param test Instancia con los datos del prueba.
     *
     * @return Instancia con los datos del prueba.
     * @throws Exception Error en la base de datos.
     */
    public Test create(Test test) throws Exception;

    /**
     * Obtener información de una prueba por un campo especifico.
     *
     * @param id ID de la prueba a ser consultada.
     * @param code Codigo de la prueba a ser consultada.
     * @param name Nombre de la prueba a ser consultada.
     * @param abbr Abreviatura de la prueba a ser consultada.
     *
     * @return Instancia con los datos del prueba.
     * @throws Exception Error en la base de datos.
     */
    public Test get(Integer id, String code, String name, String abbr) throws Exception;

    /**
     * Actualiza la información de una prueba en la base de datos.
     *
     * @param test Instancia con los datos de la prueba.
     *
     * @return Objeto de la prueba modificada.
     * @throws Exception Error en la base de datos.
     */
    public Test update(Test test) throws Exception;

    /**
     * Actualiza la información de una prueba(codigo, nombre,abreviatura,
     * area,unidad y muestra).
     *
     * @param test Instancia con los datos de la prueba.
     *
     * @throws Exception Error en la base de datos.
     */
    public void patch(Test test) throws Exception;

    /**
     *
     * Elimina una prueba de la base de datos.
     *
     * @param id ID de la prueba.
     *
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;

    /**
     * Lista las pruebas con concurrencias desde la base de datos.
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<TestBasic> listConcurrences() throws Exception;

    /**
     * Actualiza el orden de impresion a una lista de examens.
     *
     * @param tests Lista de pruebas a ser actualizadas.
     *
     * @return Numero de registros Actualizados.
     * @throws Exception Error en la base de datos.
     */
    public int updatePrintOrder(List<TestBasic> tests) throws Exception;

    /**
     * Actualiza impuestos por exámenes
     *
     * @param tests Lista con informacion de impuestos por examen
     * @return registros afectados
     * @throws Exception Error en el servicio
     */
    public int updateTestTax(List<TestBasic> tests) throws Exception;

    /**
     * Lista las pruebas desde la base de datos.
     *
     * @param idBranch Id sede por la que se realizara la consulta.
     * @param idLaboratory Id laboratorio por el que se realizara la consulta.
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<TestByLaboratory> listTestLaboratory(int idBranch, int idLaboratory) throws Exception;

    /**
     * Lista las pruebas confidenciales desde la base de datos.
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<TestBasic> listTestConfidential() throws Exception;

    /**
     * Inserta pruebas por laboratorio desde la base de datos.
     *
     * @param tests Lista de pruebas con laboratorio, sede y grupo tipo de orden
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public int insertTestLaboratory(List<TestByLaboratory> tests) throws Exception;

    /**
     * Obtiene laboratorio.
     *
     * @param branch Id sede.
     * @param test Id Examen.
     * @param groupType Grupo tipo de orden.
     *
     * @return Instancia con los datos del laboratorio.
     * @throws Exception Error en la base de datos.
     */
    public Laboratory getLaboratory(int branch, int test, int groupType) throws Exception;

    /**
     * Lista las pruebas desde la base de datos.
     *
     * @param testType Tipo de prueba.
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<Concurrence> getConcurrence(int testType) throws Exception;

    /**
     * Lista los valores de referencia de una prueba desde la base de datos.
     *
     * @param test Id de Prueba.
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<ReferenceValue> getReferenceValues(int test) throws Exception;

    /**
     * Inserta y actualiza valores de referencia a una prueba desde la base de
     * datos.
     *
     * @param referenceValue Valor de referencia.
     *
     * @return Numero de campos registrados.
     * @throws java.lang.Exception
     */
    public ReferenceValue insertReferenceValues(ReferenceValue referenceValue) throws Exception;

    /**
     * Actualiza la información de un valor de referencia en la base de datos.
     *
     * @param referenceValue Instancia con los datos del valor de referencia.
     *
     * @return Objeto de la prueba modificada.
     * @throws Exception Error en la base de datos.
     */
    public ReferenceValue updateReferenceValue(ReferenceValue referenceValue) throws Exception;

    /**
     * Inactiva el valor de referencia en la base de datos.
     *
     * @param id Id del valor de referencia.
     *
     * @throws Exception Error en la base de datos.
     */
    public void inactivateReferenceValues(int id) throws Exception;

    /**
     * Actualiza la informacipon de delta check por examen
     *
     * @param deltacheck Bean TestBasic con información de deltacheck
     *
     * @return información actualizada
     * @throws Exception
     */
    public TestBasic updateDeltacheck(TestBasic deltacheck) throws Exception;

    /**
     * Actualiza los dias de procesamiento de examenes
     *
     * @param tests Lista de examenes
     *
     * @return Numero de datos actualizados.
     * @throws Exception
     */
    public int updateProcessingDays(List<TestBasic> tests) throws Exception;

    /**
     * Lista las pruebas automaticas de una prueba desde la base de datos.
     *
     * @param idTest Id de la prueba en la que se va a hacer la consulta.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    public List<AutomaticTest> listAutomaticTest(int idTest) throws Exception;

    /**
     * Inserta pruebas automaticas desde la base de datos.
     *
     * @param tests Lista de pruebas automaticas
     * @param idTest Id de la prueba.
     *
     * @return Numero de campos registrados.
     * @throws java.lang.Exception
     */
    public int insertAutomaticTest(List<AutomaticTest> tests, int idTest) throws Exception;

    /**
     * Inserta examenes de demograficos pyp
     *
     * @param pyp información de pyp y examenes
     *
     * @return Registros insertados
     * @throws java.lang.Exception Error
     */
    public int insertPyPTest(PypDemographic pyp) throws Exception;

    /**
     * Obtiene los examenes asignados a un demografico pyp
     *
     * @param id del demográfico item
     *
     * @return PypDemographic
     * @throws java.lang.Exception Error
     */
    public PypDemographic getPypTest(Integer id) throws Exception;

    /**
     * Obtiene los examenes de Microbiologia .
     *
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<TestBasic> testMicrobilogy() throws Exception;

    /**
     * Obtiene los examenes de Microbiologia y que tengan asociados medios de
     * cultivoc.
     *
     * @param isMediaCulture Indica si se consultan examenes con medio de
     * cultivo.
     * @return Instancia con los datos de la prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<TestBasic> testMediaCulture(boolean isMediaCulture) throws Exception;

    /**
     * Lista los examenes de un servicio desde la base de datos.
     *
     * @param idService Id del examen en la que se va a hacer la consulta.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    public List<TestByService> listTestByService(int idService) throws Exception;

    /**
     * Inserta examenes por servicio desde la base de datos.
     *
     * @param test Objeto a ser ingresado.
     *
     * @return Numero de campos registrados.
     * @throws java.lang.Exception
     */
    public TestByService insertTestByService(TestByService test) throws Exception;

    /**
     * Obtiene los examenes que se muestran en ingreso de ordenes, Activos y con
     * flag de ingreso de ordenes.
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.TestBasic}
     * @throws Exception Error en el servicio
     */
    public List<TestBasic> getForOrderEntry() throws Exception;

    /**
     * Obtiene los hijos asociados a un perfil o paquete
     *
     * @param id Id perfil o paquete
     * @return Lista de {@link net.cltech.enterprisent.domain.masters.test.Test}
     * @throws Exception Error en el servicio
     */
    public List<Test> getChilds(int id) throws Exception;

    /**
     * Obtiene todos los requerimientos activos asocidas a los examenes
     * enviados. No se retornan requisitos repetidos
     *
     * @param tests Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Test}
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Requirement}
     * @throws Exception Error en el servicio
     */
    public List<Requirement> getRequirements(List<Test> tests) throws Exception;

    /**
     * Obtiene los perfiles con sus examenes
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Profile}
     * @throws Exception Error en el servicio
     */
    public List<Profile> getProfiles() throws Exception;

    /**
     * Obtiene datos de sedes, laboratorios y examenes asociados
     *
     * @param testId
     * @param branchId
     * @param laboratoryId
     * @return Lista de sedes, laboratorios y examenes asociados
     * {@link net.cltech.enterprisent.domain.masters.test.Profile}
     * @throws Exception Error en el servicio
     */
    public List<TestByLaboratory> getTestByBranchByLaboratory(Integer testId, Integer branchId, Integer laboratoryId) throws Exception;

    /**
     * Obtiene la informacion del examen
     *
     * @param test Id del examen o perfil
     * @param type Tipo del examen
     * @return Informacion del examen
     * {@link net.cltech.enterprisent.domain.masters.test.TestInformation}
     * @throws Exception Error en el servicio
     */
    public TestInformation informationByid(long test, int type) throws Exception;
    
    /**
     * Obtiene los Ids de los hijos asociados a un perfil o paquete
     *
     * @param id Id perfil o paquete
     * @return Lista de Ids de los examenes hijos
     * @throws Exception Error en el servicio
     */
    public List<Integer> getChildrenIds(int id) throws Exception;
    
    /**
     * Obtiene el tipo de examen de un examen por el id de este
     *
     * @param id Id del examen en custion
     * @return Tipo de examen
     * @throws Exception Error en el servicio
     */
    public int getTestTypeByTestId(int id) throws Exception;
    
      /**
     * Obtiene el tipo de examen de un examen por el id de este
     *
     * @param id Id del examen en custion
     * @return Tipo de examen
     * @throws Exception Error en el servicio
     */
    public int getSampleState(int id, long idOrder) throws Exception;
    
    /**
     * Obtiene los hijos asociados a un perfil o paquete
     *
     * @param id Id perfil o paquete
     * @param idOrder Ide de la orden
     * @param isPackage Indica si es paquete o perfil
     * @return Lista de {@link net.cltech.enterprisent.domain.masters.test.Test}
     * @throws Exception Error en el servicio
     */
    public List<Test> getChildsPackageOrProfile(int id, long idOrder, boolean isPackage) throws Exception;
        
}
