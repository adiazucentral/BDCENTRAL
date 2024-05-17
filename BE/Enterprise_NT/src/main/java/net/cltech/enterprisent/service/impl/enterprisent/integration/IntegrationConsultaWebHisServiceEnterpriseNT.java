package net.cltech.enterprisent.service.impl.enterprisent.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationConsultaWebHisDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicDao;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.test.Profile;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.results.HistoryFilter;
import net.cltech.enterprisent.domain.operation.results.TestHistory;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationConsultaWebHisService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de integración de consulta web HIS
 *
 * @version 1.0.0
 * @author Julian
 * @since 26/02/2020
 * @see Creación
 */
@Service
public class IntegrationConsultaWebHisServiceEnterpriseNT implements IntegrationConsultaWebHisService
{

    @Autowired
    private IntegrationConsultaWebHisDao integrationConsultaWebHisDao;
    @Autowired
    private PatientService patientService;
    @Autowired
    private TestService testService;
    @Autowired
    private ResultsService resultsService;
    @Autowired
    private DemographicDao demographicDao;
    
    /**
     * Se obtienen todas y cada uno de los examenes que se le han llevado a cabo al paciente por medio del número de la orden his
     *
     * @param idOrderHis
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public Patient getAllTestOfPatientByIdOrderHis(String idOrderHis) throws Exception
    {
        try
        {
            List<TestBasic> listAllTestsOfPatient = new ArrayList<>();
            Patient patient = integrationConsultaWebHisDao.getPatient(idOrderHis, demographicDao.list().stream().filter((Demographic t) -> t.getOrigin().equals("H") && t.isState()).collect(Collectors.toList()));
            patient.setListAllTestsOfPatient(integrationConsultaWebHisDao.allExamesForPatient(patient.getId()));
            return patient;
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Se obtienen todas las historias asignadas a un paciente desde su primer historial hasta el ultimo
     *
     * @param idPatient
     * @param idTest
     * @return
     * @throws java.lang.Exception
     */
    @Override
    public List<TestHistory> allPacientHistory(int idPatient, int idTest) throws Exception
    {
        try
        {
            Test test = testService.get(idTest, null, null, null);
            List<Profile> listProfiles = new ArrayList<>();
            List<Profile> listPackages = new ArrayList<>();
            List<Profile> listPackageAux = new ArrayList<>();
            List<Integer> listTests = new ArrayList<>();
            switch (test.getTestType())
            {
                case 1:
                    listProfiles = testService.getProfiles().stream().filter(profile -> Objects.equals(profile.getProfileId(), idTest)).collect(Collectors.toList());
                    listProfiles.removeIf(profile -> profile.getTestType() > 0);
                    for (Profile objP : listProfiles)
                    {
                        if (objP != null && !listTests.contains(objP.getTestId()))
                        {
                            listTests.add(objP.getTestId());
                        }
                    }
                    break;
                case 2:
                    listPackages = testService.getProfiles().stream().filter(profile -> Objects.equals(profile.getProfileId(), idTest)).collect(Collectors.toList());
                    for (Profile listPackage : listPackages)
                    {
                        List<Profile> listAux = testService.getProfiles().stream().filter(profile -> Objects.equals(profile.getProfileId(), listPackage.getTestId())).collect(Collectors.toList());
                        if (!listAux.isEmpty())
                        {
                            listAux.stream().filter((profile) -> (profile != null && !listPackageAux.contains(profile))).forEachOrdered((profile) ->
                            {
                                listPackageAux.add(profile);
                            });
                        }
                    }
                    if (!listPackageAux.isEmpty())
                    {
                        listPackages.addAll(listPackageAux);
                    }
                    listPackages.removeIf(profile -> profile.getTestType() > 0);
                    for (Profile objP : listPackages)
                    {
                        if (objP != null && !listTests.contains(objP.getTestId()))
                        {
                            listTests.add(objP.getTestId());
                        }
                    }
                    break;
                default:
                    listTests.add(idTest);
                    break;
            }
            HistoryFilter historyFilter = new HistoryFilter();
            historyFilter.setId(idPatient);
            historyFilter.setTestId(listTests);
            List<TestHistory> listTestshistorys = resultsService.listTestHistory(historyFilter);
            
            listTestshistorys = listTestshistorys.stream()      
                .filter(line -> !line.getHistory().isEmpty())
                .collect(Collectors.toList());   
           
            return listTestshistorys;
            
        } catch (Exception e)
        {
            return null;
        }
    }
}
