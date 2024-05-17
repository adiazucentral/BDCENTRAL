package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.dao.interfaces.operation.statistics.StatisticDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.configuration.DemographicReportEncryption;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.test.AlarmDays;
import net.cltech.enterprisent.domain.masters.test.ExcludeTest;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Demografico para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 02/05/2017
 * @see Creaciòn
 */
@Service
public class DemographicServiceEnterpriseNT implements DemographicService
{

    @Autowired
    private DemographicDao dao;
    @Autowired
    private ConfigurationDao configurationDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private StatisticDao statisticDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public List<Demographic> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Demographic create(Demographic demographic) throws Exception
    {
        List<String> errors = validateFields(false, demographic);
        if (errors.isEmpty())
        {
            Demographic created = dao.create(demographic);
            if (demographic.getOrigin().equals("H"))
            {
                ordersDao.addDemographicToPatient(demographic);
                statisticDao.addDemographicToPatient(demographic);
            } else
            {
                int yearsQuery = Integer.parseInt(configurationService.getValue("AniosConsultas"));
                ordersDao.addDemographicToOrder(demographic, yearsQuery);
                statisticDao.addDemographicToOrder(demographic, yearsQuery);
            }
            trackingService.registerConfigurationTracking(null, created, Demographic.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Demographic get(Integer id, String name, Integer sort) throws Exception
    {
        return dao.get(id, name, sort);
    }

    @Override
    public Demographic update(Demographic demographic) throws Exception
    {
        List<String> errors = validateFields(true, demographic);
        if (errors.isEmpty())
        {
            Demographic demographicC = dao.get(demographic.getId(), null, null);
            Demographic modifited = dao.update(demographic);
            trackingService.registerConfigurationTracking(demographicC, modifited, Demographic.class);
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
    public List<Demographic> list(boolean state) throws Exception
    {
        return new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Demographic) o).isState() == state));
    }

    @Override
    public List<Demographic> getDemographicIds(List<Integer> filterdemographics) throws Exception
    {
        return dao.getDemographicIds(filterdemographics);
    }

    @Override
    public List<Demographic> listByEncoded(boolean encoded) throws Exception
    {
        return new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Demographic) o).isEncoded() == encoded && ((Demographic) o).isState() == true));
    }

    @Override
    public List<Demographic> demographicsList() throws Exception
    {
        List<Demographic> demographics = new ArrayList<>();
        if (configurationDao.get("ManejoCliente").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new Demographic(Constants.ACCOUNT, "CLIENTE", true));
        }
        if (configurationDao.get("ManejoMedico").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new Demographic(Constants.PHYSICIAN, "MEDICO", true));
        }
        if (configurationDao.get("ManejoTarifa").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new Demographic(Constants.RATE, "TARIFA", true));
        }
        demographics.add(new Demographic(Constants.BRANCH, "SEDE", true));
        if (configurationDao.get("ManejoServicio").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new Demographic(Constants.SERVICE, "SERVICIO", true));
        }
        if (configurationDao.get("ManejoRaza").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new Demographic(Constants.RACE, "RAZA", true));
        }
        demographics.add(new Demographic(Constants.ORDERTYPE, "TIPO ORDEN", true));
        if (configurationDao.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true"))
        {
            demographics.add(new Demographic(Constants.DOCUMENT_TYPE, "TIPO DE DOCUMENTO", true));
        }
        demographics.add(new Demographic(Constants.ORDER_HIS, "ORDEN HIS", false));
        demographics.add(new Demographic(Constants.PATIENT_EMAIL, "EMAIL", false));

        demographics.addAll(list(true));
        return demographics;
    }

    @Override
    public List<Demographic> demographicsListordering(String origin) throws Exception
    {

        List<Demographic> Demofixed = dao.listOderingfixed();
        List<Demographic> demographics = new ArrayList<>();

        Map<String, Demographic> map = new HashMap<>();

        for (Demographic demographic : Demofixed)
        {

            if (demographic.getId() == Constants.DOCUMENT_TYPE)
            {
                map.put("typeDocumentValue", demographic);
            }

            if (configurationDao.get("MedicosAuxiliares").getValue().equalsIgnoreCase("true"))
            {
                int cantPhysician = Integer.parseInt(configurationDao.get("TotalMedicosAuxiliares").getValue());
                for (int i = 1; i <= cantPhysician; i += 1)
                {
                    int intTest = 200 + i;
                    intTest = intTest * -1;
                    if (demographic.getId() == intTest)
                    {
                        map.put(String.valueOf(intTest), demographic);
                    }
                }
            }

            if (demographic.getId() == Constants.PATIENT_ID)
            {
                map.put("patientIDValue", demographic);
            }
            if (demographic.getId() == Constants.PATIENT_LAST_NAME)
            {
                map.put("patientLastNameValue", demographic);
            }
            if (demographic.getId() == Constants.PATIENT_SURNAME)
            {
                map.put("patientSurnameValue", demographic);
            }
            if (demographic.getId() == Constants.PATIENT_NAME)
            {
                map.put("patientNameValue", demographic);
            }
            if (demographic.getId() == Constants.PATIENT_SECOND_NAME)
            {
                map.put("patientSecondNameValue", demographic);
            }
            if (demographic.getId() == Constants.PATIENT_SEX)
            {
                map.put("patientSexValue", demographic);
            }
            if (demographic.getId() == Constants.PATIENT_BIRTHDAY)
            {

                map.put("patientBirthdayValue", demographic);
            }
            if (demographic.getId() == Constants.PATIENT_AGE)
            {

                map.put("patientAgeValue", demographic);
            }
            if (demographic.getId() == Constants.PATIENT_EMAIL)
            {

                map.put("patientEmailValue", demographic);
            }
            if (demographic.getId() == Constants.PATIENT_PHONE)
            {

                map.put("patientphoneValue", demographic);
            }
            if (demographic.getId() == Constants.WEIGHT)
            {

                map.put("WeightValue", demographic);
            }
            if (demographic.getId() == Constants.SIZE)
            {

                map.put("patientSizeValue", demographic);
            }
            if (demographic.getId() == Constants.PATIENT_ADDRESS)
            {

                map.put("patientAddresValue", demographic);
            }
            if (demographic.getId() == Constants.RACE)
            {

                map.put("patientRaceValue", demographic);
            }
            if (demographic.getId() == Constants.ORDER_ID)
            {

                map.put("patientOrdenValue", demographic);
            }
            if (demographic.getId() == Constants.ORDERTYPE)
            {

                map.put("OrderTypeValue", demographic);
            }
            if (demographic.getId() == Constants.BRANCH)
            {

                map.put("BranchValue", demographic);
            }
            if (demographic.getId() == Constants.SERVICE)
            {

                map.put("ServiceValue", demographic);
            }
            if (demographic.getId() == Constants.ACCOUNT)
            {

                map.put("accountValue", demographic);
            }
            if (demographic.getId() == Constants.RATE)
            {

                map.put("rateValue", demographic);
            }
            if (demographic.getId() == Constants.PHYSICIAN)
            {

                map.put("physicianValue", demographic);
            }
        }

        if (origin.equals("H"))
        {
            // TIPO DE DOCUMENTO
            if (configurationDao.get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true"))
            {
                Demographic typeDocument = new Demographic();
                if (map.get("typeDocumentValue") != null)
                {

                    typeDocument.setId(Constants.DOCUMENT_TYPE);
                    typeDocument.setName("0645");
                    typeDocument.setOrigin("H");
                    typeDocument.setOrderingDemo((int) map.get("typeDocumentValue").getOrdering());
                    typeDocument.setLastTransaction(map.get("typeDocumentValue").getLastTransaction());
                    typeDocument.setUser(map.get("typeDocumentValue").getUser());
                    demographics.add(typeDocument);
                } else
                {
                    typeDocument.setId(Constants.DOCUMENT_TYPE);
                    typeDocument.setName("0645");
                    typeDocument.setOrigin("H");
                    typeDocument.setOrderingDemo(1);
                    demographics.add(typeDocument);
                }
            }

            // HISTORIA 
            Demographic patientID = new Demographic();
            if (map.get("patientIDValue") != null)
            {
                patientID.setId(Constants.PATIENT_ID);
                patientID.setName("0070");
                patientID.setOrigin("H");
                patientID.setOrderingDemo((int) map.get("patientIDValue").getOrdering());
                patientID.setLastTransaction(map.get("patientIDValue").getLastTransaction());
                patientID.setUser(map.get("patientIDValue").getUser());
                demographics.add(patientID);
            } else
            {
                patientID.setId(Constants.PATIENT_ID);
                patientID.setName("0070");
                patientID.setOrigin("H");
                patientID.setOrderingDemo(2);
                demographics.add(patientID);
            }
            // DEMOGRAFICOS 
            List<Demographic> demoInFixed = dao.listOderingH();
            for (Demographic demographic : demoInFixed)
            {
                Demographic patientDemoFixed = new Demographic();
                patientDemoFixed.setId(demographic.getId());
                patientDemoFixed.setName(demographic.getName());
                patientDemoFixed.setOrigin("H");

                List<Demographic> filters;
                filters = Demofixed.stream()
                        .filter(p -> p.getId() == demographic.getId())
                        .collect(Collectors.toList());

                if (filters.isEmpty())
                {
                    patientDemoFixed.setOrderingDemo(demographic.getOrderingDemo() + 15);
                } else
                {
                    patientDemoFixed.setOrderingDemo((int) filters.get(0).getOrdering());
                }
                demographics.add(patientDemoFixed);

            }
            // PRIMER APELLIDO
            Demographic patientLastName = new Demographic();
            if (map.get("patientLastNameValue") != null)
            {
                patientLastName.setId(Constants.PATIENT_LAST_NAME);
                patientLastName.setName("1359");
                patientLastName.setOrigin("H");
                patientLastName.setOrderingDemo((int) map.get("patientLastNameValue").getOrdering());
                patientLastName.setLastTransaction(map.get("patientLastNameValue").getLastTransaction());
                patientLastName.setUser(map.get("patientLastNameValue").getUser());

                demographics.add(patientLastName);
            } else
            {
                patientLastName.setId(Constants.PATIENT_LAST_NAME);
                patientLastName.setName("1359");
                patientLastName.setOrigin("H");
                patientLastName.setOrderingDemo(3);
                demographics.add(patientLastName);
            }
            //SEGUNDO APELLIDO
            Demographic patientSurname = new Demographic();
            if (map.get("patientSurnameValue") != null)
            {
                patientSurname.setId(Constants.PATIENT_SURNAME);
                patientSurname.setName("1360");
                patientSurname.setOrigin("H");
                patientSurname.setOrderingDemo((int) map.get("patientSurnameValue").getOrdering());
                patientSurname.setLastTransaction(map.get("patientSurnameValue").getLastTransaction());
                patientSurname.setUser(map.get("patientSurnameValue").getUser());
                demographics.add(patientSurname);
            } else
            {
                patientSurname.setId(Constants.PATIENT_SURNAME);
                patientSurname.setName("1360");
                patientSurname.setOrigin("H");
                patientSurname.setOrderingDemo(4);
                demographics.add(patientSurname);
            }
            //NOMBRE
            Demographic patientName = new Demographic();
            if (map.get("patientNameValue") != null)
            {
                patientName.setId(Constants.PATIENT_NAME);
                patientName.setName("1361");
                patientName.setOrigin("H");
                patientName.setOrderingDemo((int) map.get("patientNameValue").getOrdering());
                patientName.setLastTransaction(map.get("patientNameValue").getLastTransaction());
                patientName.setUser(map.get("patientNameValue").getUser());
                demographics.add(patientName);
            } else
            {
                patientName.setId(Constants.PATIENT_NAME);
                patientName.setName("1361");
                patientName.setOrigin("H");
                patientName.setOrderingDemo(5);
                demographics.add(patientName);
            }
            //SEGUNDO NOMBRE
            Demographic patientSecondName = new Demographic();
            if (map.get("patientSecondNameValue") != null)
            {
                patientSecondName.setId(Constants.PATIENT_SECOND_NAME);
                patientSecondName.setName("1362");
                patientSecondName.setOrigin("H");
                patientSecondName.setOrderingDemo((int) map.get("patientSecondNameValue").getOrdering());
                patientSecondName.setLastTransaction(map.get("patientSecondNameValue").getLastTransaction());
                patientSecondName.setUser(map.get("patientSecondNameValue").getUser());

                demographics.add(patientSecondName);
            } else
            {
                patientSecondName.setId(Constants.PATIENT_SECOND_NAME);
                patientSecondName.setName("1362");
                patientSecondName.setOrigin("H");
                patientSecondName.setOrderingDemo(6);
                demographics.add(patientSecondName);
            }
            //SEXO
            Demographic patientSex = new Demographic();
            if (map.get("patientSexValue") != null)
            {
                patientSex.setId(Constants.PATIENT_SEX);
                patientSex.setName("0221");
                patientSex.setOrigin("H");
                patientSex.setOrderingDemo((int)map.get("patientSexValue").getOrdering());
                patientSex.setLastTransaction(map.get("patientSexValue").getLastTransaction());
                patientSex.setUser(map.get("patientSexValue").getUser());

                demographics.add(patientSex);
            } else
            {
                patientSex.setId(Constants.PATIENT_SEX);
                patientSex.setName("0221");
                patientSex.setOrigin("H");
                patientSex.setOrderingDemo(7);
                demographics.add(patientSex);
            }
            //FECHA DE NACIMIENTO
            Demographic patientBirthday = new Demographic();
            if (map.get("patientBirthdayValue") != null)
            {
                patientBirthday.setId(Constants.PATIENT_BIRTHDAY);
                patientBirthday.setName("1363");
                patientBirthday.setOrigin("H");
                patientBirthday.setOrderingDemo((int) map.get("patientBirthdayValue").getOrdering());
                patientBirthday.setLastTransaction(map.get("patientBirthdayValue").getLastTransaction());
                patientBirthday.setUser(map.get("patientBirthdayValue").getUser());
                demographics.add(patientBirthday);
            } else
            {
                patientBirthday.setId(Constants.PATIENT_BIRTHDAY);
                patientBirthday.setName("1363");
                patientBirthday.setOrigin("H");
                patientBirthday.setOrderingDemo(8);
                demographics.add(patientBirthday);
            }
            //EDAD
            Demographic patientAge = new Demographic();
            if (map.get("patientAgeValue") != null)
            {
                patientAge.setId(Constants.PATIENT_AGE);
                patientAge.setName("0418");
                patientAge.setOrigin("H");
                patientAge.setOrderingDemo((int) map.get("patientAgeValue").getOrdering());
                patientAge.setLastTransaction(map.get("patientAgeValue").getLastTransaction());
                patientAge.setUser(map.get("patientAgeValue").getUser());

                demographics.add(patientAge);
            } else
            {
                patientAge.setId(Constants.PATIENT_AGE);
                patientAge.setName("0418");
                patientAge.setOrigin("H");
                patientAge.setOrderingDemo(9);
                demographics.add(patientAge);
            }
            //EMAIL
            Demographic patientEmail = new Demographic();
            if (map.get("patientEmailValue") != null)
            {
                patientEmail.setId(Constants.PATIENT_EMAIL);
                patientEmail.setName("0086");
                patientEmail.setOrigin("H");
                patientEmail.setOrderingDemo((int) map.get("patientEmailValue").getOrdering());
                patientEmail.setLastTransaction(map.get("patientEmailValue").getLastTransaction());
                patientEmail.setUser(map.get("patientEmailValue").getUser());

                demographics.add(patientEmail);
            } else
            {
                patientEmail.setId(Constants.PATIENT_EMAIL);
                patientEmail.setName("0086");
                patientEmail.setOrigin("H");
                patientEmail.setOrderingDemo(10);
                demographics.add(patientEmail);
            }
            //TELEFONO
            Demographic patientphone = new Demographic();
            if (map.get("patientphoneValue") != null)
            {
                patientphone.setId(Constants.PATIENT_PHONE);
                patientphone.setName("0077");
                patientphone.setOrigin("H");
                patientphone.setOrderingDemo((int) map.get("patientphoneValue").getOrdering());
                patientphone.setLastTransaction(map.get("patientphoneValue").getLastTransaction());
                patientphone.setUser(map.get("patientphoneValue").getUser());

                demographics.add(patientphone);
            } else
            {
                patientphone.setId(Constants.PATIENT_PHONE);
                patientphone.setName("0077");
                patientphone.setOrigin("H");
                patientphone.setOrderingDemo(11);
                demographics.add(patientphone);
            }
            //PESO
            if (configurationDao.get("ManejoPeso").getValue().equalsIgnoreCase("true"))
            {
                Demographic Weight = new Demographic();
                if (map.get("WeightValue") != null)
                {
                    Weight.setId(Constants.WEIGHT);
                    Weight.setName("0470");
                    Weight.setOrigin("H");
                    Weight.setOrderingDemo((int) map.get("WeightValue").getOrdering());
                    Weight.setLastTransaction(map.get("WeightValue").getLastTransaction());
                    Weight.setUser(map.get("WeightValue").getUser());

                    demographics.add(Weight);
                } else
                {
                    Weight.setId(Constants.WEIGHT);
                    Weight.setName("0470");
                    Weight.setOrigin("H");
                    Weight.setOrderingDemo(12);
                    demographics.add(Weight);
                }
            }
            //TALLA
            if (configurationDao.get("ManejoTalla").getValue().equalsIgnoreCase("true"))
            {
                Demographic patientSize = new Demographic();
                if (map.get("patientSizeValue") != null)
                {
                    patientSize.setId(Constants.SIZE);
                    patientSize.setName("0471");
                    patientSize.setOrigin("H");
                    patientSize.setOrderingDemo((int) map.get("patientSizeValue").getOrdering());
                    patientSize.setLastTransaction(map.get("patientSizeValue").getLastTransaction());
                    patientSize.setUser(map.get("patientSizeValue").getUser());

                    demographics.add(patientSize);
                } else
                {
                    patientSize.setId(Constants.SIZE);
                    patientSize.setName("0471");
                    patientSize.setOrigin("H");
                    patientSize.setOrderingDemo(13);
                    demographics.add(patientSize);
                }
            }
            //DIRRECION
            Demographic patientAddres = new Demographic();
            if (map.get("patientAddresValue") != null)
            {
                patientAddres.setId(Constants.PATIENT_ADDRESS);
                patientAddres.setName("0076");
                patientAddres.setOrigin("H");
                patientAddres.setOrderingDemo((int) map.get("patientAddresValue").getOrdering());
                patientAddres.setLastTransaction(map.get("patientAddresValue").getLastTransaction());
                patientAddres.setUser(map.get("patientAddresValue").getUser());

                demographics.add(patientAddres);
            } else
            {
                patientAddres.setId(Constants.PATIENT_ADDRESS);
                patientAddres.setName("0076");
                patientAddres.setOrigin("H");
                patientAddres.setOrderingDemo(14);
                demographics.add(patientAddres);
            }
            //RAZA
            if (configurationDao.get("ManejoRaza").getValue().equalsIgnoreCase("true"))
            {
                Demographic patientRace = new Demographic();
                if (map.get("patientRaceValue") != null)
                {
                    patientRace.setId(Constants.RACE);
                    patientRace.setName("0174");
                    patientRace.setOrigin("H");
                    patientRace.setOrderingDemo((int) map.get("patientRaceValue").getOrdering());
                    patientRace.setLastTransaction(map.get("patientRaceValue").getLastTransaction());
                    patientRace.setUser(map.get("patientRaceValue").getUser());

                    demographics.add(patientRace);
                } else
                {
                    patientRace.setId(Constants.RACE);
                    patientRace.setName("0174");
                    patientRace.setOrigin("H");
                    patientRace.setOrderingDemo(15);
                    demographics.add(patientRace);
                }
            }

        } else
        {
            //ORDEN
            Demographic patientOrden = new Demographic();
            if (map.get("patientOrdenValue") != null)
            {
                patientOrden.setId(Constants.ORDER_ID);
                patientOrden.setName("0061");
                patientOrden.setOrigin("O");
                patientOrden.setOrderingDemo((int) map.get("patientOrdenValue").getOrdering());
                patientOrden.setLastTransaction(map.get("patientOrdenValue").getLastTransaction());
                patientOrden.setUser(map.get("patientOrdenValue").getUser());

                demographics.add(patientOrden);
            } else
            {
                patientOrden.setId(Constants.ORDER_ID);
                patientOrden.setName("0061");
                patientOrden.setOrigin("O");
                patientOrden.setOrderingDemo(1);
                demographics.add(patientOrden);
            }

            if (configurationDao.get("MedicosAuxiliares").getValue().equalsIgnoreCase("true"))
            {
                int cantPhysician = Integer.parseInt(configurationDao.get("TotalMedicosAuxiliares").getValue());
                for (int i = 1; i <= cantPhysician; i += 1)
                {
                    Demographic auxphysician = new Demographic();
                    int intTest = 200 + i;
                    intTest = intTest * -1;
                    String ordering = String.valueOf(intTest);
                    String name = "";
                    if (i == 1)
                    {
                        name = "1364";
                    }
                    if (i == 2)
                    {
                        name = "1365";
                    }
                    if (i == 3)
                    {
                        name = "1366";
                    }
                    if (i == 4)
                    {
                        name = "1367";
                    }
                    if (i == 5)
                    {
                        name = "1368";
                    }
                    if (map.get(ordering) != null)
                    {
                        auxphysician.setId(intTest);
                        auxphysician.setName(name);
                        auxphysician.setOrigin("O");
                        auxphysician.setOrderingDemo((int) map.get(ordering).getOrdering());
                        auxphysician.setLastTransaction(map.get(ordering).getLastTransaction());
                        auxphysician.setUser(map.get(ordering).getUser());

                        demographics.add(auxphysician);
                    } else
                    {
                        auxphysician.setId(intTest);
                        auxphysician.setName(name);
                        auxphysician.setOrigin("O");
                        auxphysician.setOrderingDemo(7 + i);
                        demographics.add(auxphysician);
                    }

                }

            }

            int cantPhysician = 0;
            if (configurationDao.get("MedicosAuxiliares").getValue().equalsIgnoreCase("true"))
            {
                cantPhysician = Integer.parseInt(configurationDao.get("TotalMedicosAuxiliares").getValue());
            }
            List<Demographic> demoInFixed = dao.listOderingO();
            for (Demographic demographic : demoInFixed)
            {
                Demographic patientDemoFixed = new Demographic();
                patientDemoFixed.setId(demographic.getId());
                patientDemoFixed.setName(demographic.getName());
                patientDemoFixed.setOrigin("O");

                List<Demographic> filters;
                filters = Demofixed.stream()
                        .filter(p -> p.getId() == demographic.getId())
                        .collect(Collectors.toList());

                if (filters.isEmpty())
                {
                    patientDemoFixed.setOrderingDemo(demographic.getOrderingDemo() + 7 + cantPhysician);
                } else
                {
                    patientDemoFixed.setOrderingDemo((int) filters.get(0).getOrdering());
                }
                demographics.add(patientDemoFixed);

            }

            //TIPO DE ORDEN
            Demographic OrderType = new Demographic();
            if (map.get("OrderTypeValue") != null)
            {
                OrderType.setId(Constants.ORDERTYPE);
                OrderType.setName("0133");
                OrderType.setOrigin("O");
                OrderType.setOrderingDemo((int) map.get("OrderTypeValue").getOrdering());
                OrderType.setLastTransaction(map.get("OrderTypeValue").getLastTransaction());
                OrderType.setUser(map.get("OrderTypeValue").getUser());

                demographics.add(OrderType);
            } else
            {
                OrderType.setId(Constants.ORDERTYPE);
                OrderType.setName("0133");
                OrderType.setOrigin("O");
                OrderType.setOrderingDemo(2);
                demographics.add(OrderType);
            }

            //SEDE
            Demographic Branch = new Demographic();
            if (map.get("BranchValue") != null)
            {
                Branch.setId(Constants.BRANCH);
                Branch.setName("0003");
                Branch.setOrigin("O");
                Branch.setOrderingDemo((int) map.get("BranchValue").getOrdering());
                Branch.setLastTransaction(map.get("BranchValue").getLastTransaction());
                Branch.setUser(map.get("BranchValue").getUser());
                demographics.add(Branch);
            } else
            {
                Branch.setId(Constants.BRANCH);
                Branch.setName("0003");
                Branch.setOrigin("O");
                Branch.setOrderingDemo(3);
                demographics.add(Branch);
            }
            //SERVICIO
            if (configurationDao.get("ManejoServicio").getValue().equalsIgnoreCase("true"))
            {
                Demographic Service = new Demographic();
                if (map.get("ServiceValue") != null)
                {
                    Service.setId(Constants.SERVICE);
                    Service.setName("0175");
                    Service.setOrigin("O");
                    Service.setOrderingDemo((int) map.get("ServiceValue").getOrdering());
                    Service.setLastTransaction(map.get("ServiceValue").getLastTransaction());
                    Service.setUser(map.get("ServiceValue").getUser());
                    demographics.add(Service);
                } else
                {
                    Service.setId(Constants.SERVICE);
                    Service.setName("0175");
                    Service.setOrigin("O");
                    Service.setOrderingDemo(4);
                    demographics.add(Service);
                }
            }
            //CLIENTE
            if (configurationDao.get("ManejoCliente").getValue().equalsIgnoreCase("true"))
            {
                Demographic account = new Demographic();
                if (map.get("accountValue") != null)
                {
                    account.setId(Constants.ACCOUNT);
                    account.setName("0248");
                    account.setOrigin("O");
                    account.setOrderingDemo((int) map.get("accountValue").getOrdering());
                    account.setLastTransaction(map.get("accountValue").getLastTransaction());
                    account.setUser(map.get("accountValue").getUser());
                    demographics.add(account);
                } else
                {
                    account.setId(Constants.ACCOUNT);
                    account.setName("0248");
                    account.setOrigin("O");
                    account.setOrderingDemo(5);
                    demographics.add(account);
                }
            }
            //TARIFA
            if (configurationDao.get("ManejoTarifa").getValue().equalsIgnoreCase("true"))
            {
                Demographic rate = new Demographic();
                if (map.get("rateValue") != null)
                {
                    rate.setId(Constants.RATE);
                    rate.setName("0307");
                    rate.setOrigin("O");
                    rate.setOrderingDemo((int) map.get("rateValue").getOrdering());
                    rate.setLastTransaction(map.get("rateValue").getLastTransaction());
                    rate.setUser(map.get("rateValue").getUser());
                    demographics.add(rate);
                } else
                {
                    rate.setId(Constants.RATE);
                    rate.setName("0307");
                    rate.setOrigin("O");
                    rate.setOrderingDemo(6);
                    demographics.add(rate);
                }
            }
            //MEDICO
            if (configurationDao.get("ManejoMedico").getValue().equalsIgnoreCase("true"))
            {
                Demographic physician = new Demographic();
                if (map.get("physicianValue") != null)
                {
                    physician.setId(Constants.PHYSICIAN);
                    physician.setName("0225");
                    physician.setOrigin("O");
                    physician.setOrderingDemo((int) map.get("physicianValue").getOrdering());
                    physician.setLastTransaction(map.get("physicianValue").getLastTransaction());
                    physician.setUser(map.get("physicianValue").getUser());
                    demographics.add(physician);
                } else
                {
                    physician.setId(Constants.PHYSICIAN);
                    physician.setName("0225");
                    physician.setOrigin("O");
                    physician.setOrderingDemo(7);
                    demographics.add(physician);
                }
            }

        }
        return demographics;
    }

    /**
     * Validar campos del objeto
     *
     * @param isEdit Indica si se esta editando el demografico
     * @param demographic Demografico
     * @return Lista de errores
     * @throws Exception
     */
    private List<String> validateFields(boolean isEdit, Demographic demographic) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (demographic.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(demographic.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (demographic.getOrdering() != null)
        {
//            Demographic demographicC = dao.get(null, null, demographic.getOrdering().intValue(), demographic.getOrigin());
//            if (demographicC != null)
//            {
//                if (isEdit)
//                {
//                    if (!Objects.equals(demographic.getId(), demographicC.getId()))
//                    {
//                        errors.add("1|ordering");
//                    }
//                } else
//                {
//                    errors.add("1|ordering");
//                }
//            }
        } else
        {
            errors.add("0|ordering");
        }

        if (demographic.getName() != null && !demographic.getName().isEmpty())
        {
            Demographic demographicC = dao.get(null, demographic.getName(), null);
            if (demographicC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(demographic.getId(), demographicC.getId()))
                    {
                        errors.add("1|name");
                    }
                } else
                {
                    errors.add("1|name");
                }
            }
        } else
        {
            errors.add("0|name");
        }

        if (demographic.getOrigin() != null && !demographic.getOrigin().isEmpty())
        {
            if (!demographic.getOrigin().equals("O") && !demographic.getOrigin().equals("H"))
            {
                errors.add("3|origin");
            }
        } else
        {
            errors.add("0|origin");
        }

        if (demographic.getObligatory() != null)
        {
            if (demographic.getObligatory() != 0 && demographic.getObligatory() != 1 && demographic.getObligatory() != 2)
            {
                errors.add("3|obligatory");
            }
        } else
        {
            errors.add("0|obligatory");
        }

        if (demographic.getUser().getId() == null || demographic.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

    @Override
    public List<ExcludeTest> listDemographicTest(Integer id, Integer idItem) throws Exception
    {
        return dao.listDemographicTest(id, idItem);
    }

    @Override
    public int deleteDemographicTest(Integer id, Integer idItem) throws Exception
    {
        return dao.deleteDemographicTest(id, idItem);
    }

    @Override
    public int insertDemographicTest(List<ExcludeTest> tests) throws Exception
    {
        List<ExcludeTest> listPrevious = listDemographicTest(tests.get(0).getId(), tests.get(0).getDemographicItem())
                .stream()
                .map((ExcludeTest excludeTest)
                        ->
                {
                    excludeTest.setDemographicItemName(tests.get(0).getDemographicItemName());
                    return excludeTest;
                })
                .collect(Collectors.toList());
        dao.deleteDemographicTest(tests.get(0).getId(), tests.get(0).getDemographicItem());
        Integer insert = dao.insertDemographicTest(tests);
        trackingService.registerConfigurationTracking(listPrevious, tests, ExcludeTest.class);
        return insert;
    }

    @Override
    public int insertAlarmDaysTest(AlarmDays alarmDays) throws Exception
    {
        AlarmDays previous = new AlarmDays();
        previous.setDemographic(alarmDays.getDemographic());
        previous.getDemographic().setDemographicItemName("");
        previous.setTest(listAlarmDaysTest(alarmDays.getDemographic().getId(), alarmDays.getDemographic().getDemographicItem()));
        dao.deleteAlarmDaysTest(alarmDays.getDemographic().getId(), alarmDays.getDemographic().getDemographicItem());
        int insert = dao.insertAlarmDaysTest(alarmDays);
        trackingService.registerConfigurationTracking(previous, alarmDays, AlarmDays.class);
        return insert;
    }

    @Override
    public int deleteAlarmDaysTest(Integer idDemographic, Integer demographicItem) throws Exception
    {
        return dao.deleteAlarmDaysTest(idDemographic, demographicItem);
    }

    @Override
    public List<TestBasic> listAlarmDaysTest(Integer idDemographic, Integer demographicItem) throws Exception
    {
        return dao.listAlarmDaysTest(idDemographic, demographicItem);
    }

    @Override
    public List<Demographic> listBySource(String source) throws Exception
    {
        return dao.list().stream().filter(demo -> demo.isState())
                .filter(origin -> origin.getOrigin().equals(source))
                .collect(Collectors.toList());
    }

    @Override
    public void updateOrder(List<Demographic> demographics) throws Exception
    {
        final AuthorizedUser user = JWT.decode(request);
        demographics.stream().forEach((d)
                ->
        {
            try
            {
                d.setUser(user);
                Demographic demographicC = dao.get(d.getId(), null, null);
                Demographic modifited = dao.updateOrder(d);
                trackingService.registerConfigurationTracking(demographicC, modifited, Demographic.class);
            } catch (Exception ex)
            {
                Logger.getLogger(DemographicServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void updateOrderAll(List<Demographic> demographics) throws Exception
    {
        final AuthorizedUser user = JWT.decode(request);
        demographics.stream().forEach((d)
                ->
        {
            try
            {
                d.setUser(user);
                Demographic demographicC = dao.getOrdening(d.getId());
                if (demographicC != null)
                {
                    Demographic modifited = dao.updateOrdering(d);
                    trackingService.registerConfigurationTracking(demographicC, modifited, Demographic.class);
                } else
                {
                    Demographic create = dao.newOrdering(d);
                    trackingService.registerConfigurationTracking(null, create, Demographic.class);
                }

            } catch (Exception ex)
            {
                Logger.getLogger(DemographicServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Inserta los demograficos con sus respectivos items en encriptación de
     * reportes por demografico
     *
     * @param demographics
     * @throws Exception Error en base de datos
     */
    @Override
    public void saveDemographicReportEncrypt(List<DemographicReportEncryption> demographics) throws Exception
    {
        try
        {
            // Obtengo la lista de maestros antigua
            List<DemographicReportEncryption> oldMastersList = dao.getDemographicsReportEncrypt(demographics);
            // Elimino todos los registros de los demograficos que vayan llegando en la lista de la tabla
            dao.deleteDemographicsReportEncrypt(oldMastersList);
            // Se realiza una inserción en batch de la lista en la tabla
            dao.createDemographicsReportEncrypt(demographics);
            // Inserto la trazabilidad:
            trackingService.registerConfigurationTracking(oldMastersList, demographics, DemographicReportEncryption.class);
        } catch (Exception e)
        {
            e.getMessage();
        }
    }

    /**
     * Obtiene los items de un demografico correspondiente a encriptación de
     * reportes por demografico
     *
     * @param idDemographic
     * @return itemsDemographics correspondientes a encriptación de reportes por
     * demografico
     * @throws Exception Error en base de datos
     */
    @Override
    public List<DemographicReportEncryption> getDemographicReportEncryptById(int idDemographic) throws Exception
    {
        List<DemographicReportEncryption> mastersList = new ArrayList<>();
        try
        {
            if (idDemographic > 0)
            {
                mastersList = dao.getDemographicsReportEncryptById(idDemographic);
            } else
            {
                mastersList = dao.demographicsPermanentReportEncryptById(idDemographic);
            }
            return mastersList;
        } catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<DemographicReportEncryption> getDemographicByIdAndDemographicitem(int idDemographic, int demographicItem) throws Exception
    {
        List<DemographicReportEncryption> mastersList = new ArrayList<>();
        try
        {
            if (idDemographic > 0)
            {
                mastersList = dao.getDemographicByIdAndDemographicitem(idDemographic, demographicItem);
            }

            return mastersList;
        } catch (Exception e)
        {
            return null;
        }
    }

}
