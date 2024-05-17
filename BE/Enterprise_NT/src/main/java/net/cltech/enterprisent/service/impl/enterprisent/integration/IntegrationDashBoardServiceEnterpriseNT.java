package net.cltech.enterprisent.service.impl.enterprisent.integration;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.integration.DashboardDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardCommon;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardLicense;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardOpportunityTime;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardProductivity;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardSample;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardSampleTaking;
import net.cltech.enterprisent.domain.integration.dashboard.DashboardConfiguration;
import net.cltech.enterprisent.domain.integration.dashboard.DashboardUrl;
import net.cltech.enterprisent.domain.integration.dashboard.TestDashboard;
import net.cltech.enterprisent.domain.integration.dashboard.TestTrackingDashboard;
import net.cltech.enterprisent.domain.integration.dashboard.TestValidDashboard;
import net.cltech.enterprisent.domain.integration.dashboard.UserDashboard;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationDashBoardService;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.ServiceService;
import net.cltech.enterprisent.service.interfaces.masters.test.AreaService;
import net.cltech.enterprisent.service.interfaces.masters.test.SampleService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.integration.DashBoardLog;
import net.cltech.enterprisent.tools.log.integration.MiddlewareLog;
import net.cltech.enterprisent.tools.log.integration.SigaLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Implementacion de tableros para Enterprise NT.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/07/2018
 * @see Creacion
 */
@Service
public class IntegrationDashBoardServiceEnterpriseNT implements IntegrationDashBoardService
{

    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private TestService testService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private SampleService sampleService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private DashboardDao dao;

    @Autowired
    private IntegrationService integrationService;

    @Override
    public List<DashBoardCommon> listServices() throws Exception
    {
        return serviceService.list(true).stream().map(service -> new DashBoardCommon(service.getId(), service.getCode(), service.getName())).collect(Collectors.toList());
    }

    @Override
    public List<DashBoardCommon> listBranches() throws Exception
    {
        return branchService.list(true).stream().map(branch -> new DashBoardCommon(branch.getId(), branch.getCode(), branch.getName())).collect(Collectors.toList());
    }

    @Override
    public List<DashBoardCommon> listTests() throws Exception
    {
        return testService.list(5, true, null).stream().map(test -> new DashBoardCommon(test.getId(), test.getCode(), test.getName())).collect(Collectors.toList());
    }

    @Override
    public List<DashBoardCommon> listAreas() throws Exception
    {
        return areaService.list(true).stream().map(area -> new DashBoardCommon(area.getId(), null, area.getName())).collect(Collectors.toList());
    }

    @Override
    public List<DashBoardProductivity> listProductivity(Integer branch, Integer section, Integer hours) throws Exception
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
            Date dateNow = new Date();
            String currentTime = sdf.format(dateNow);
            Date i = sdf.parse(String.valueOf(currentTime));
            //Se restan las horas a la fecha y hora actual
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            Date tempDate = calendar.getTime();
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - hours);
            tempDate = calendar.getTime();
            String hourback = sdf.format(tempDate);
            Date f = sdf.parse(String.valueOf(hourback));

            List<DashBoardProductivity> list = new ArrayList<>();
            DashBoardProductivity obj = new DashBoardProductivity();
            list = dao.listProductivityByDashboard(new Timestamp(i.getTime()), new Timestamp(f.getTime()), branch, section);
            
            SigaLog.info("CONSULTA TABLERO " + list.size());
            
            
            if (list != null && !list.isEmpty())
            {
                // Se asignan los valores que no cambian al objeto de productividad por sección
                obj.setIdBranch(list.get(0).getIdBranch());
                obj.setIdSection(list.get(0).getIdSection());
                obj.setBranchName(list.get(0).getBranchName());
                obj.setSectionName(list.get(0).getSectionName());
                // Se hace un conteo total y se guarda en el objeto de productividad por sección
                list.forEach(productivity ->
                {
                    obj.setPrinted(obj.getPrinted() + productivity.getPrinted());
                    obj.setResults(obj.getResults() + productivity.getResults());
                    obj.setValidated(obj.getValidated() + productivity.getValidated());
                    obj.setVerified(obj.getVerified() + productivity.getVerified());
                });
            }
            // Se vacia la lista para que me posteriormente este servicio en su respuesta
            // No contenga una lista de varios objetos, sino que solo contenga un solo objeto
            list = new ArrayList<>();
            list.add(obj);
            return list;
        } catch (Exception e)
        {
            throw new EnterpriseNTException("Error in System");
        }

    }

    @Override
    public List<DashBoardSample> listSamples() throws Exception
    {
        return sampleService.list(true).stream().map(sample -> new DashBoardSample(sample.getId(), sample.getCodesample(), sample.getName(), sample.isPrintable(), sample.getCanstiker())).collect(Collectors.toList());
    }

    @Override
    public void dashBoardOpportunityTime(Long idOrder, List<Integer> idsTests, int action)
    {
        try

        {
            HashMap<String, Boolean> licenses = boardLicenses();
            boolean keyValidation = configurationService.getValue("TableroValidacion").equalsIgnoreCase("true");
            boolean keyOpportunityTime = configurationService.getValue("TableroTiempoDeOportunidad").equalsIgnoreCase("true");
            boolean keyTestTracking = configurationService.getValue("TableroSeguimientoDePruebas").equalsIgnoreCase("true");
            if (configurationService.getValue("IntegracionDashBoard").equalsIgnoreCase("true"))
            {
                if (licenses != null && !licenses.isEmpty())
                {
                    final String uriValidate = configurationService.getValue("UrlDashBoard") + "/BoardV";
                    final String uriOpportunityTime = configurationService.getValue("UrlDashBoard") + "/BoardTOE";
                    final String uriTracing = configurationService.getValue("UrlDashBoard") + "/BoardSPU";

                    DashBoardOpportunityTime opportunityTime = new DashBoardOpportunityTime();
                    TestTrackingDashboard testTracking = new TestTrackingDashboard();
                    RestTemplate restTemplate = new RestTemplate();
                    List<Integer> profileIds = new ArrayList<>();
                    String demoHospitalLocation = configurationService.getValue("UbicacionHospitalaria").equals("0") ? "" : configurationService.getValue("UbicacionHospitalaria");
                    
                    String demographicSendDashboard = configurationService.getValue("demoTableros");
                    String itemSendDashboardDemographic = configurationService.getValue("itemDemoTableros");
                    
                    switch (action)
                    {
                        case 1:
                            for (Integer idTest : idsTests)
                            {
                                opportunityTime = dao.getOpportunityTime(idOrder, idTest, demographicSendDashboard, itemSendDashboardDemographic);
                                testTracking = dao.getTestTracking(idOrder, idTest, demoHospitalLocation);
                                
                               
                                // Indica el estado del examen: 1 - Ingreso
                                if (opportunityTime.getVerifyDate() != null)
                                {
                                    //Verificado
                                    testTracking.setState(2);
                                } else 
                                {
                                    testTracking.setState(1);
                                }
                                
                                if(opportunityTime == null ){
                                    DashBoardLog.info("Oren no encontrada para enviar " + idOrder );
                                }
                                
                                if (keyValidation && licenses.get("TableroValidacion") && opportunityTime != null)
                                {
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    try
                                    {
                                        DashBoardLog.info("Envia la información para INSERTAR en tablero de validación");
                                        DashBoardLog.info("Url: " + uriValidate);
                                        DashBoardLog.info("Data Post: " + Tools.jsonObject(Arrays.asList(opportunityTime)));
                                        restTemplate.postForObject(uriValidate, Arrays.asList(opportunityTime), String.class);
                                    } catch (Exception e)
                                    {
                                        DashBoardLog.error(e.getMessage());
                                    }
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                }
                                if (keyOpportunityTime && licenses.get("TableroTiempoDeOportunidad"))
                                {
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    try
                                    {
                                        DashBoardLog.info("Envia la información para INSERTAR en tablero de tiempos de oportunidad");
                                        DashBoardLog.info("Url: " + uriOpportunityTime);
                                        DashBoardLog.info("Data Post: " + Tools.jsonObject(Arrays.asList(opportunityTime.clean())));
                                        restTemplate.postForObject(uriOpportunityTime, Arrays.asList(opportunityTime.clean()), String.class);
                                    } catch (Exception e)
                                    {
                                        DashBoardLog.error(e.getMessage());
                                    }
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                }
                                if (keyTestTracking && licenses.get("TableroSeguimientoDePruebas"))
                                {
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    try
                                    {
                                        DashBoardLog.info("Envia la información para INSERTAR en tablero de seguimiento de pruebas");
                                        DashBoardLog.info("Url: " + uriTracing);
                                        DashBoardLog.info("Data Post: " + Tools.jsonObject(Arrays.asList(testTracking)));
                                        restTemplate.postForObject(uriTracing, Arrays.asList(testTracking), String.class);
                                    } catch (Exception e)
                                    {
                                        DashBoardLog.error(e.getMessage());
                                    }
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                }
                            }
                            break;
                        case 2:
                           for (Integer searchProfile : idsTests)
                            {
                                int idProfile = dao.getIdsProfilesOfTheOrder(idOrder, searchProfile);
                                if (idProfile != 0 && !profileIds.contains(idProfile))
                                {
                                    profileIds.add(idProfile);
                                }
                            }

                            if (!profileIds.isEmpty())
                            {
                                idsTests.addAll(profileIds);
                            }
                            for (Integer idTest : idsTests)
                            {
                               
                                int profile = profileIds.stream().filter(testTofilter -> Objects.equals(testTofilter, idTest)).findAny().orElse(0);
                                opportunityTime = dao.getOpportunityTime(idOrder, idTest, demographicSendDashboard, itemSendDashboardDemographic);
                                testTracking = dao.getTestTracking(idOrder, idTest, demoHospitalLocation);
                                if (profile > 0)
                                {
                                    Integer analyteCounter = dao.profileAnalyteCount(profile);
                                    Integer validatedAnalyteCounter = dao.validatedAnalyteCounter(idOrder, profile);
                                    if (Objects.equals(analyteCounter, validatedAnalyteCounter))
                                    {
                                        opportunityTime.setValidated(true);
                                        opportunityTime.setValidateDate(new Date(System.currentTimeMillis()));
                                    }
                                }

                                // Indica el estado del examen: 1 - Ingreso
                                if (opportunityTime.getValidateDate() == null)
                                {
                                    //Verificado
                                    testTracking.setState(2);
                                } else
                                {
                                    //Validado
                                    testTracking.setState(3);
                                    testTracking.setValidateDate(opportunityTime.getValidateDate());
                                }

                                if (keyValidation && licenses.get("TableroValidacion"))
                                {
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    try
                                    {
                                        DashBoardLog.info("Envia la información para ACTUALIZAR validación en tableros.");
                                        DashBoardLog.info("Url: " + uriValidate);
                                        DashBoardLog.info("Data Put: " + Tools.jsonObject(Arrays.asList(opportunityTime)));
                                        restTemplate.put(uriValidate, Arrays.asList(opportunityTime));
                                    } catch (Exception e)
                                    {
                                        DashBoardLog.error(e.getMessage());
                                    }
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                }
                                if (keyOpportunityTime && licenses.get("TableroTiempoDeOportunidad"))
                                {
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    try
                                    {
                                        DashBoardLog.info("Envia la información para ACTUALIZAR tiempos de oportunidad en tableros.");
                                        DashBoardLog.info("Url: " + uriOpportunityTime);
                                        DashBoardLog.info("Data Put: " + Tools.jsonObject(Arrays.asList(opportunityTime.clean())));
                                        restTemplate.put(uriOpportunityTime, Arrays.asList(opportunityTime.clean()));
                                    } catch (Exception e)
                                    {
                                        DashBoardLog.error(e.getMessage());
                                    }
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                }
                                if (keyTestTracking && licenses.get("TableroSeguimientoDePruebas"))
                                {
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    try
                                    {
                                        DashBoardLog.info("Envia la información para ACTUALIZAR seguimientos de pruebas en tableros.");
                                        DashBoardLog.info("Url: " + uriTracing);
                                        DashBoardLog.info("Data Put: " + Tools.jsonObject(Arrays.asList(testTracking)));
                                        restTemplate.put(uriTracing, Arrays.asList(testTracking), String.class);
                                    } catch (Exception e)
                                    {
                                        DashBoardLog.error(e.getMessage());
                                    }
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                }
                            }
                            break;
                        case 3:
                            if (idsTests != null && !idsTests.isEmpty())
                            {
                                for (Integer searchProfile : idsTests)
                                {
                                    int idProfile = dao.getIdsProfilesOfTheOrder(idOrder, searchProfile);
                                    if (idProfile != 0 && !profileIds.contains(idProfile))
                                    {
                                        profileIds.add(idProfile);
                                    }
                                }

                                if (!profileIds.isEmpty())
                                {
                                    idsTests.addAll(profileIds);
                                }

                                for (Integer idTest : idsTests)
                                {
                                    if (keyValidation && licenses.get("TableroValidacion"))
                                    {
                                        DashBoardLog.info("----------------------------------------------------------------------------------------");
                                        try
                                        {
                                            DashBoardLog.info("Envia la información para ELIMINAR validación en tableros.");
                                            DashBoardLog.info("Url delete: " + uriValidate + "/order/" + idOrder + "/test/" + idTest);
                                            restTemplate.delete(uriValidate + "/order/" + idOrder + "/test/" + idTest);
                                        } catch (Exception e)
                                        {
                                            DashBoardLog.error(e.getMessage());
                                        }
                                        DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    }
                                    if (keyOpportunityTime && licenses.get("TableroTiempoDeOportunidad"))
                                    {
                                        DashBoardLog.info("----------------------------------------------------------------------------------------");
                                        try
                                        {
                                            DashBoardLog.info("Envia la información para ELIMINAR tiempos de oportunidad en tableros.");
                                            DashBoardLog.info("Url delete: " + uriOpportunityTime + "/order/" + idOrder + "/test/" + idTest);
                                            restTemplate.delete(uriOpportunityTime + "/order/" + idOrder + "/test/" + idTest);
                                        } catch (Exception e)
                                        {
                                            DashBoardLog.error(e.getMessage());
                                        }
                                        DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    }
                                    if (keyTestTracking && licenses.get("TableroSeguimientoDePruebas"))
                                    {
                                        DashBoardLog.info("----------------------------------------------------------------------------------------");
                                        try
                                        {
                                            DashBoardLog.info("Envia la información para ELIMINAR seguimientos de pruebas en tableros.");
                                            DashBoardLog.info("Url delete: " + uriTracing + "/order/" + idOrder + "/test/" + idTest);
                                            restTemplate.delete(uriTracing + "/order/" + idOrder + "/test/" + idTest);
                                        } catch (Exception e)
                                        {
                                            DashBoardLog.error(e.getMessage());
                                        }
                                        DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    }
                                }
                            } else
                            {
                                if (keyValidation && licenses.get("TableroValidacion"))
                                {
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    try
                                    {
                                        DashBoardLog.info("Envia la información para ELIMINAR validación en tableros.");
                                        DashBoardLog.info("Url delete: " + uriValidate + "/order/" + idOrder);
                                        restTemplate.delete(uriValidate + "/order/" + idOrder);
                                    } catch (Exception e)
                                    {
                                        DashBoardLog.error(e.getMessage());
                                    }
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                }
                                if (keyOpportunityTime && licenses.get("TableroTiempoDeOportunidad"))
                                {
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    try
                                    {
                                        DashBoardLog.info("Envia la información para ELIMINAR tiempos de oportunidad en tableros.");
                                        DashBoardLog.info("Url delete: " + uriOpportunityTime + "/order/" + idOrder);
                                        restTemplate.delete(uriOpportunityTime + "/order/" + idOrder);
                                    } catch (Exception e)
                                    {
                                        DashBoardLog.error(e.getMessage());
                                    }
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                }
                                if (keyTestTracking && licenses.get("TableroSeguimientoDePruebas"))
                                {
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                    try
                                    {
                                        DashBoardLog.info("Envia la información para ELIMINAR seguimientos de pruebas en tableros.");
                                        DashBoardLog.info("Url delete: " + uriTracing + "/order/" + idOrder);
                                        restTemplate.delete(uriTracing + "/order/" + idOrder);
                                    } catch (Exception e)
                                    {
                                        DashBoardLog.error(e.getMessage());
                                    }
                                    DashBoardLog.info("----------------------------------------------------------------------------------------");
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e)
        {
            DashBoardLog.info("Error al acceder a servicios de Tableros, Error: " + e.getMessage());
        }
    }
    
    @Override
    public TestValidDashboard getTestSendDashoboard(int days)
    {
        try
        {
            
            long fromOrder = Tools.buildInitialOrder(days, configurationService.getIntValue("DigitosOrden"));
            long untilOrder = Tools.buildFinalOrder(configurationService.getIntValue("DigitosOrden"));
            
            HashMap<String, Boolean> licenses = boardLicenses();
            boolean keyValidation = configurationService.getValue("TableroValidacion").equalsIgnoreCase("true");
            boolean keyOpportunityTime = configurationService.getValue("TableroTiempoDeOportunidad").equalsIgnoreCase("true");
            boolean keyTestTracking = configurationService.getValue("TableroSeguimientoDePruebas").equalsIgnoreCase("true");
            if (configurationService.getValue("IntegracionDashBoard").equalsIgnoreCase("true"))
            {
                if (licenses != null && !licenses.isEmpty())
                {
                    final String uriValidate = configurationService.getValue("UrlDashBoard") + "/BoardV";
                    final String uriOpportunityTime = configurationService.getValue("UrlDashBoard") + "/BoardTOE";
                    final String uriTracing = configurationService.getValue("UrlDashBoard") + "/BoardSPU";

                    List<DashBoardOpportunityTime> opportunityTime = new LinkedList<>();
                                     
                    String demographicSendDashboard = configurationService.getValue("demoTableros");
                    String itemSendDashboardDemographic = configurationService.getValue("itemDemoTableros");
                    
                    opportunityTime = dao.getTestSendDashoboard(fromOrder, untilOrder, demographicSendDashboard, itemSendDashboardDemographic);
        
                    TestValidDashboard testValidDashboard = new TestValidDashboard();
                    testValidDashboard.setDashBoardOpportunityTime(opportunityTime);
                    testValidDashboard.setTableroSeguimientoDePruebas(keyTestTracking && licenses.get("TableroSeguimientoDePruebas"));
                    testValidDashboard.setTableroValidacion(keyValidation && licenses.get("TableroValidacion"));
                    testValidDashboard.setTableroTiempoDeOportunidad(keyOpportunityTime && licenses.get("TableroTiempoDeOportunidad")); 
                    testValidDashboard.setUriOpportunityTime(uriOpportunityTime);
                    testValidDashboard.setUriTracing(uriTracing);
                    testValidDashboard.setUriValidate(uriValidate);
                   
                    return testValidDashboard;
                }
            }
        } catch (Exception e)
        {
            DashBoardLog.info("Error al acceder a servicios de Tableros, Error: " + e.getMessage());
            return null;
        }
        return null;
    }
    
    @Override
    public TestValidDashboard getTestEntry(int days)
    {
        try
        {
            
            long fromOrder = Tools.buildInitialOrder(days, configurationService.getIntValue("DigitosOrden"));
            long untilOrder = Tools.buildFinalOrder(configurationService.getIntValue("DigitosOrden"));
            
            HashMap<String, Boolean> licenses = boardLicenses();
            boolean keyValidation = configurationService.getValue("TableroValidacion").equalsIgnoreCase("true");
            boolean keyOpportunityTime = configurationService.getValue("TableroTiempoDeOportunidad").equalsIgnoreCase("true");
            boolean keyTestTracking = configurationService.getValue("TableroSeguimientoDePruebas").equalsIgnoreCase("true");
            if (configurationService.getValue("IntegracionDashBoard").equalsIgnoreCase("true"))
            {
                if (licenses != null && !licenses.isEmpty())
                {
                    final String uriValidate = configurationService.getValue("UrlDashBoard") + "/BoardV";
                    final String uriOpportunityTime = configurationService.getValue("UrlDashBoard") + "/BoardTOE";
                    final String uriTracing = configurationService.getValue("UrlDashBoard") + "/BoardSPU";

                    List<DashBoardOpportunityTime> opportunityTime = new LinkedList<>();
                                     
                    String demographicSendDashboard = configurationService.getValue("demoTableros");
                    String itemSendDashboardDemographic = configurationService.getValue("itemDemoTableros");
                    
                    opportunityTime = dao.getTestEntry(fromOrder, untilOrder, demographicSendDashboard, itemSendDashboardDemographic);
        
                    TestValidDashboard testValidDashboard = new TestValidDashboard();
                    testValidDashboard.setDashBoardOpportunityTime(opportunityTime);
                    testValidDashboard.setTableroSeguimientoDePruebas(keyTestTracking && licenses.get("TableroSeguimientoDePruebas"));
                    testValidDashboard.setTableroValidacion(keyValidation && licenses.get("TableroValidacion"));
                    testValidDashboard.setTableroTiempoDeOportunidad(keyOpportunityTime && licenses.get("TableroTiempoDeOportunidad")); 
                    testValidDashboard.setUriOpportunityTime(uriOpportunityTime);
                    testValidDashboard.setUriTracing(uriTracing);
                    testValidDashboard.setUriValidate(uriValidate);
                   
                    return testValidDashboard;
                }
            }
        } catch (Exception e)
        {
            DashBoardLog.info("Error al acceder a servicios de Tableros, Error: " + e.getMessage());
            return null;
        }
        return null;
    }
    
    @Override
    public boolean updateSentDashboard(long idOrder, int idTest) 
    {
        try {
            return dao.updateSentDashboard(idOrder, idTest) > 0;
        } catch (Exception ex) {
            return false;
        }
    }
    
    @Override
    public boolean updateSentDashboardEntry(long idOrder, int idTest)
    {
        try {
            return dao.updateSentDashboardEntry(idOrder, idTest) > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Boolean testUrl(DashboardUrl url) throws EnterpriseNTException
    {
        try
        {
            final String urlLIS = url.getUrl() + "/api/configurationV";
            final HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            int status = restTemplate.exchange(urlLIS, HttpMethod.GET, entity, Void.class).getStatusCodeValue();
            boolean result = false;
            if (status == 200 || status == 204)
            {
                result = true;
            }
            return result;
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }

    @Override
    public List<DashboardConfiguration> configuration() throws EnterpriseNTException, Exception
    {
        try
        {
            List<DashboardConfiguration> list = new ArrayList<>();
            if (configurationService.getValue("IntegracionDashBoard").equalsIgnoreCase("true"))
            {
                final String urlLIS = configurationService.getValue("UrlDashBoard") + "/configurationV";
                final HttpHeaders headers = new HttpHeaders();
                HttpEntity<String> httpEntity = new HttpEntity<>(headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<DashboardConfiguration[]> responseEntity = restTemplate.exchange(urlLIS, HttpMethod.GET, httpEntity, DashboardConfiguration[].class);
                if (responseEntity.getBody() != null)
                {
                    list = Arrays.asList(responseEntity.getBody());
                }
            }
            return list;
        } catch (IllegalArgumentException | HttpClientErrorException ex)
        {
            throw new EnterpriseNTException(Arrays.asList("URL not found"));
        }
    }

    @Override
    public List<DashBoardLicense> licenses(boolean admin) throws Exception
    {
        HashMap<String, Boolean> licences = boardLicenses();
        List<DashBoardLicense> records = new ArrayList<>(0);
        DashBoardLicense dashboard = new DashBoardLicense();
        dashboard.setAccessTab(licences.get("TableroTomaDeMuestra"));
        dashboard.setIdTab("01");
        dashboard.setProduct("lis");
        dashboard.setTotalTab("01");
        records.add(dashboard);

        dashboard = new DashBoardLicense();
        dashboard.setAccessTab(licences.get("TableroProductividadPorSeccion"));
        dashboard.setIdTab("02");
        dashboard.setProduct("lis");
        dashboard.setTotalTab("01");
        records.add(dashboard);

        dashboard = new DashBoardLicense();
        dashboard.setAccessTab(licences.get("TableroTiempoDeOportunidad"));
        dashboard.setIdTab("03");
        dashboard.setProduct("lis");
        dashboard.setTotalTab("02");
        records.add(dashboard);

        dashboard = new DashBoardLicense();
        dashboard.setAccessTab(licences.get("TableroValidacion"));
        dashboard.setIdTab("04");
        dashboard.setProduct("lis");
        dashboard.setTotalTab("01");
        records.add(dashboard);

        dashboard = new DashBoardLicense();
        dashboard.setAccessTab(licences.get("TableroCalificacionDelServicio"));
        dashboard.setIdTab("05");
        dashboard.setProduct("sig");
        dashboard.setTotalTab("01");
        records.add(dashboard);

        dashboard = new DashBoardLicense();
        dashboard.setAccessTab(licences.get("TableroSeguimientoDePruebas"));
        dashboard.setIdTab("06");
        dashboard.setProduct("lis");
        dashboard.setTotalTab("02");
        records.add(dashboard);

        return records;
    }

    @Override
    public List<UserDashboard> getUserDashboard(String user, String password) throws Exception
    {
        List<UserDashboard> listUserDashboards = new ArrayList<>();
        UserDashboard userDashboard = new UserDashboard();
        userDashboard = dao.getUserDashboard(user, password);
        if (userDashboard == null)
        {
            UserDashboard noUser = new UserDashboard();
            noUser.setLastName("");
            noUser.setPassword(null);
            noUser.setSuccess(false);
            noUser.setValorIv(null);
            noUser.setValorSalr(null);
            noUser.setName("");
            noUser.setId(0);
            noUser.setKey(false);
            noUser.setUser("");
            noUser.setAccessDirect(false);
            noUser.setAdministrator(false);
            listUserDashboards.add(noUser);

            return listUserDashboards;
        } else
        {
            listUserDashboards.add(userDashboard);
            return listUserDashboards;
        }
    }

    @Override
    public List<TestDashboard> listTestsDashboard() throws Exception
    {
        try
        {
            List<TestDashboard> listTest = new ArrayList<>();

            listTest = dao.listTestsDashboard();

            return listTest;

        } catch (Exception e)
        {
            throw new EnterpriseNTException("Error in System");
        }

    }

    /**
     * Validacion de la orden a verificar
     *
     * @param order
     *
     * @return
     * @throws Exception
     */
    private List<String> validateFields(String user) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (user == null || user == "")
        {
            errors.add("0|can not send name null");
            return errors;
        }

        return errors;
    }

    /**
     * Obtiene el licenciamiento de los tableros
     *
     * @return Licenciamientos de los tableros
     * @throws Exception Error en la base de datos.
     */
    @Override
    public HashMap<String, Boolean> boardLicenses() throws Exception
    {
        try
        {

            final String url = configurationService.getValue("UrlSecurity") + "/api/license/boardLicenses";
            return integrationService.get(HashMap.class, url);
        } catch (Exception e)
        {
            return new HashMap<>();
        }
    }

    @Override
    public void dashBoardHospitalSampling(Long idOrder, Integer idSample, List<Integer> tests, int action)
    {
        try
        {
            HashMap<String, Boolean> licenses = boardLicenses();
            if (configurationService.getValue("IntegracionDashBoard").equalsIgnoreCase("true") && configurationService.getValue("TableroTomaDeMuestra").equalsIgnoreCase("true"))
            {
                if (licenses.get("TableroTomaDeMuestra"))
                {
                    final String uriHospitalSampling = configurationService.getValue("UrlDashBoard") + "/BoardTMH";

                    DashBoardSampleTaking sampleTaking = new DashBoardSampleTaking();
                    RestTemplate restTemplate = new RestTemplate();
                    String demoHospitalLocation = configurationService.getValue("UbicacionHospitalaria").equals("0") ? "" : configurationService.getValue("UbicacionHospitalaria");
                    
                    tests = tests.stream().distinct().collect(Collectors.toList());
                    
                    switch (action)
                    {
                        case 4:
                            for (Integer idTest : tests)
                            {
                                DashBoardLog.info("----------------------------------------------------------------------------------------");
                                try
                                {
                                    sampleTaking = dao.getHospitalSampling(idOrder, idSample, idTest, demoHospitalLocation);
                                    DashBoardLog.info("Envia la información para INSERTAR tomas de muestras hopitalarias en tableros");
                                    DashBoardLog.info("Url: " + uriHospitalSampling);
                                    DashBoardLog.info("Data Post: " + Tools.jsonObject(Arrays.asList(sampleTaking)));
                                    restTemplate.postForObject(uriHospitalSampling, Arrays.asList(sampleTaking), String.class);
                                } catch (Exception e)
                                {
                                    DashBoardLog.error(e.getMessage());
                                }
                                DashBoardLog.info("----------------------------------------------------------------------------------------");
                            }
                            break;
                        case 5:
                            for (Integer idTest : tests)
                            {
                                DashBoardLog.info("----------------------------------------------------------------------------------------");
                                try
                                {
                                    sampleTaking = dao.getHospitalSampling(idOrder, idSample, idTest, demoHospitalLocation);
                                    DashBoardLog.info("Envia la información para ACTUALIZAR tomas de muestras hopitalarias en tableros");
                                    DashBoardLog.info("Url: " + uriHospitalSampling);
                                    DashBoardLog.info("Data Put: " + Tools.jsonObject(Arrays.asList(sampleTaking)));
                                    restTemplate.put(uriHospitalSampling, Arrays.asList(sampleTaking), String.class);
                                } catch (Exception e)
                                {
                                    DashBoardLog.error(e.getMessage());
                                }
                                DashBoardLog.info("----------------------------------------------------------------------------------------");
                            }
                            break;
                        case 6:
                            DashBoardLog.info("----------------------------------------------------------------------------------------");
                            try
                            {
                                DashBoardLog.info("Envia la información para ELIMINAR tomas de muestras hopitalarias en tableros");
                                DashBoardLog.info("Url delete: " + uriHospitalSampling + "/order/" + idOrder + "/sample/" + idSample);
                                restTemplate.delete(uriHospitalSampling + "/order/" + idOrder + "/sample/" + idSample);
                            } catch (Exception e)
                            {
                                DashBoardLog.error(e.getMessage());
                            }
                            DashBoardLog.info("----------------------------------------------------------------------------------------");
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e)
        {
            DashBoardLog.info("Error al acceder a servicios de Tableros, Error: " + e.getMessage());
        }
    }
}
