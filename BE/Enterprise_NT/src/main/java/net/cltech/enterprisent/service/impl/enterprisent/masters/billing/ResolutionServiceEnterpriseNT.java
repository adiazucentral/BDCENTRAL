package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.masters.billing.ResolutionDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.FilterResolution;
import net.cltech.enterprisent.domain.masters.billing.Resolution;
import net.cltech.enterprisent.domain.masters.billing.SingularResolution;
import net.cltech.enterprisent.domain.masters.billing.SingularResolutionTest;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.service.interfaces.masters.billing.ResolutionService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Resolución para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 02/05/2018
 * @see Creación
 */
@Service
public class ResolutionServiceEnterpriseNT implements ResolutionService
{

    @Autowired
    private ResolutionDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ToolsDao toolsDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private DemographicDao demographicDao;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private DemographicService serviceDemographic;

    @Override
    public List<Resolution> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public int getResolutionIdByProvider(int provider) throws Exception
    {
        return dao.getResolutionIdByProvider(provider);
    }

    @Override
    public Resolution create(Resolution resolution) throws Exception
    {
        List<String> errors = validateFields(false, resolution);
        if (errors.isEmpty())
        {

            Resolution created = dao.create(resolution);
            trackingService.registerConfigurationTracking(null, created, Resolution.class);

            int minimun = resolution.getFromNumber();
            int maximun = resolution.getToNumber();

            String nameResolution = Constants.RESOLUTION_SEQUENCE + created.getId();

            toolsDao.createSequence(nameResolution, minimun, 1, maximun);

            return created;

        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Resolution get(Integer id) throws Exception
    {
        return dao.get(id);
    }

    @Override
    public Resolution update(Resolution resolution) throws Exception
    {
        List<String> errors = validateFields(true, resolution);
        if (errors.isEmpty())
        {
            Resolution resolutionC = dao.get(resolution.getId());
            Resolution modifited = dao.update(resolution);

            trackingService.registerConfigurationTracking(resolutionC, modifited, Resolution.class);

            int minimun = resolution.getFromNumber();
            int maximun = resolution.getToNumber();

            String nameResolution = Constants.RESOLUTION_SEQUENCE + modifited.getId();

            toolsDao.resetSequence(nameResolution, minimun, maximun);

            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        dao.delete(id);
    }

    @Override
    public List<Resolution> list(boolean state) throws Exception
    {
        return dao.list().stream().filter(resolution -> resolution.isState() == state).collect(Collectors.toList());
    }

    private List<String> validateFields(boolean isEdit, Resolution resolution) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (resolution.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(resolution.getId()) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        return errors;
    }

    @Override
    public List<SingularResolution> getResolution4505(FilterResolution filter) throws Exception
    {
        try
        {
            int idCentralSystem = 0;

            boolean account = daoConfig.get("ManejoCliente").getValue().equalsIgnoreCase("true");
            boolean physician = daoConfig.get("ManejoMedico").getValue().equalsIgnoreCase("true");
            boolean rate = daoConfig.get("ManejoTarifa").getValue().equalsIgnoreCase("true");
            boolean checkCentralSystem = daoConfig.get("ConsultarSistemaCentral").getValue().equalsIgnoreCase("true");
            boolean documenttype = daoConfig.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true");
            
            if (checkCentralSystem)
            {
                idCentralSystem = Integer.parseInt(daoConfig.get("SistemaCentralListados").getValue());
            }
            
            List<SingularResolution> resolution = dao.getResolution4505(filter, serviceDemographic.list(true), account, physician, rate, checkCentralSystem, idCentralSystem, documenttype);
            
            resolution =  resolution.stream()
                    .filter(r -> filter.getDemographics().isEmpty() || filterResolutionByDemos(filter.getDemographics(), r.getOrderNumber()))                    
                    .map( t -> t.setTests(filterExcludetestbyprofilePending(t.getTests(), filter.isGroupProfiles())))
                    .collect(Collectors.toList());
            
            List<SingularResolution> resolutionRemoveTests = dao.getResolution4505RemoveTests(filter, serviceDemographic.list(true), account, physician, rate, checkCentralSystem, idCentralSystem, documenttype);
            
            resolution.addAll(resolutionRemoveTests);
            
            return resolution;
            
        } catch (Exception e)
        {
            return null;
        }
    }

    private boolean filterResolutionByDemos(List<FilterDemographic> filterDemos, long orderNumber)
    {
        String table;
        Integer integerData;
        String strData;
        for (FilterDemographic filterDemo : filterDemos)
        {
            if (filterDemo.getOrigin().equalsIgnoreCase("o"))
            {
                switch (filterDemo.getDemographic())
                {
                    case Constants.ACCOUNT:
                        table = "lab14c1";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, orderNumber);
                        if (Tools.isInteger(strData))
                        {
                            integerData = Integer.parseInt(strData);
                            if (!filterDemo.getDemographicItems().contains(integerData))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                    case Constants.PHYSICIAN:
                        table = "lab19c1";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, orderNumber);
                        if (Tools.isInteger(strData))
                        {
                            integerData = Integer.parseInt(strData);
                            if (!filterDemo.getDemographicItems().contains(integerData))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                    case Constants.RATE:
                        table = "lab904c1";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, orderNumber);
                        if (Tools.isInteger(strData))
                        {
                            integerData = Integer.parseInt(strData);
                            if (!filterDemo.getDemographicItems().contains(integerData))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                    case Constants.ORDERTYPE:
                        table = "lab103c1";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, orderNumber);
                        if (Tools.isInteger(strData))
                        {
                            integerData = Integer.parseInt(strData);
                            if (!filterDemo.getDemographicItems().contains(integerData))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                    case Constants.BRANCH:
                        table = "lab05c1";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, orderNumber);
                        if (Tools.isInteger(strData))
                        {
                            integerData = Integer.parseInt(strData);
                            if (!filterDemo.getDemographicItems().contains(integerData))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                    case Constants.SERVICE:
                        table = "lab10c1";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, orderNumber);
                        if (Tools.isInteger(strData))
                        {
                            integerData = Integer.parseInt(strData);
                            if (!filterDemo.getDemographicItems().contains(integerData))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                    default:
                        table = "lab_demo_" + filterDemo.getDemographic();
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, orderNumber);
                        if (filterDemo.isEncoded())
                        {
                            if (Tools.isInteger(strData))
                            {
                                integerData = Integer.parseInt(strData);
                                if (!filterDemo.getDemographicItems().contains(integerData))
                                {
                                    return false;
                                }
                            } else
                            {
                                return false;
                            }
                        } else
                        {
                            if (!filterDemo.getValue().equals(strData))
                            {
                                return false;
                            }
                        }
                        break;
                }
            } else
            {
                int idPatient = ordersDao.getIdPatientByOrderNumber(orderNumber);
                switch (filterDemo.getDemographic())
                {
                    case Constants.DOCUMENT_TYPE:
                        table = "lab54c1";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, idPatient);
                        if (Tools.isInteger(strData))
                        {
                            integerData = Integer.parseInt(strData);
                            if (!filterDemo.getDemographicItems().contains(integerData))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                    case Constants.RACE:
                        table = "lab08c1";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, idPatient);
                        if (Tools.isInteger(strData))
                        {
                            integerData = Integer.parseInt(strData);
                            if (!filterDemo.getDemographicItems().contains(integerData))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SEX:
                        table = "lab80c1";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, idPatient);
                        if (Tools.isInteger(strData))
                        {
                            integerData = Integer.parseInt(strData);
                            if (!filterDemo.getDemographicItems().contains(integerData))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_ID:
                        table = "lab21c2";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, idPatient);
                        if (!filterDemo.getValue().equals(strData))
                        {
                            return false;
                        }
                        break;

                    case Constants.PATIENT_LAST_NAME:
                        table = "lab21c6";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, idPatient);
                        if (!filterDemo.getValue().equals(strData))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SURNAME:
                        table = "lab21c5";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, idPatient);
                        if (!filterDemo.getValue().equals(strData))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_NAME:
                        table = "lab21c3";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, idPatient);
                        if (!filterDemo.getValue().equals(strData))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SECOND_NAME:
                        table = "lab21c4";
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, idPatient);
                        if (!filterDemo.getValue().equals(strData))
                        {
                            return false;
                        }
                        break;
                    default:
                        table = "lab_demo_" + filterDemo.getDemographic();
                        strData = demographicDao.getValueOfAnyDemographic(filterDemo.getOrigin(), table, idPatient);
                        if (filterDemo.isEncoded())
                        {
                            if (Tools.isInteger(strData))
                            {
                                integerData = Integer.parseInt(strData);
                                if (!filterDemo.getDemographicItems().contains(integerData))
                                {
                                    return false;
                                }
                            } else
                            {
                                return false;
                            }
                        } else
                        {
                            if (!filterDemo.getValue().equals(strData))
                            {
                                return false;
                            }
                        }
                        break;
                }
            }
        }

        return true;
    }
    
        /**
     * Filtra examenes por laboratorio y establece laboratorio origen
     *
     * @param filter Orden a la cual se le realizara el filtro
     * @param search lista laboratorio a filtrar
     *
     * @return Lista de examenes filtrados
     */
    private List<SingularResolutionTest> filterExcludetestbyprofilePending(List<SingularResolutionTest> filter, boolean isGroupProfiles)
    {
        try
        {
            if (isGroupProfiles)
            {

                List<SingularResolutionTest> profiles = new LinkedList<>();
                profiles = filter.stream().filter(t -> t.getTestType() != 0).collect(Collectors.toList());
                Set<Integer> filteredProfile = profiles.stream().map(SingularResolutionTest::getIdTest).collect(Collectors.toSet());

                if (filteredProfile.size() > 0)
                {
                    filteredProfile.forEach(t ->
                    {
                        List<SingularResolutionTest> filteredTests = filter.stream()
                                .filter(p -> !(p.getTestStatus() == 4 && !p.isPrint()) && p.getSampleState() != 1)
                                .filter(c -> java.util.Objects.equals(c.getProfile(), t))
                                .collect(Collectors.toList());

                        if (filteredTests.isEmpty())
                        {
                            filter.removeIf(test -> java.util.Objects.equals(test.getIdTest(), t));
                        }
                    });
                }

                List<SingularResolutionTest> filteredTests = filter.stream()
                        .filter(t -> !(t.getTestStatus() == 4 && !t.isPrint()) && t.getSampleState() != 1)
                        .filter(c -> !filteredProfile.contains(c.getProfile()))
                        .map(test ->
                        {
                            if (test.getTestType() == 1)
                            {
                                int min = filter.stream()
                                        .filter(t -> Objects.equals(t.getProfile(), test.getIdTest()))
                                        .mapToInt(i -> i.getTestStatus()).min().orElseThrow(NoSuchElementException::new);
                                test.setTestStatus(min);
                            }

                            return test;
                        })
                        .collect(Collectors.toList());

                return filteredTests;

            } else
            {
                return filter;
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }
}
