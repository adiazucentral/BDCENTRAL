package net.cltech.enterprisent.service.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.results.TestHistory;

/**
 * Interfaz de servicios de la consulta web HIS
 * 
 * @version 1.0.0
 * @author Julian
 * @since 26/03/2020
 * @see Creación
 */
public interface IntegrationConsultaWebHisService
{
    /**
     * Se obtienen todas y cada uno de los examenes que se le han llevado a cabo al paciente
     * por medio del número de la orden his
     *
     * @param idOrderHis
     * @return
     * @throws java.lang.Exception
     */
    public Patient getAllTestOfPatientByIdOrderHis(String idOrderHis) throws Exception;
    
    /**
     * Se obtienen todas las historias asignadas a un paciente
     *
     * @param idPatient
     * @param idTest
     * @return
     * @throws java.lang.Exception
     */
    public List<TestHistory> allPacientHistory(int idPatient, int idTest) throws Exception;
}
