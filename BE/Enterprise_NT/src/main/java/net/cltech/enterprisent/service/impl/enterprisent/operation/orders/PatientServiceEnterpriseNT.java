package net.cltech.enterprisent.service.impl.enterprisent.operation.orders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.integration.HomeBoundDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DemographicDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.OrdersDao;
import net.cltech.enterprisent.dao.interfaces.operation.orders.PatientsDao;
import net.cltech.enterprisent.dao.interfaces.operation.results.ResultDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.siigo.AccountSiigo;
import net.cltech.enterprisent.domain.integration.siigo.CitySiigo;
import net.cltech.enterprisent.domain.integration.siigo.ContactSiigo;
import net.cltech.enterprisent.domain.integration.siigo.PhoneSiigo;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.PatientPhoto;
import net.cltech.enterprisent.domain.operation.reports.PatientReport;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;
import net.cltech.enterprisent.service.impl.enterprisent.masters.demographic.AccountServiceEnterpriseNT;
import net.cltech.enterprisent.service.interfaces.common.ListService;
import net.cltech.enterprisent.service.interfaces.integration.BillingIntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.service.interfaces.operation.orders.PatientService;
import net.cltech.enterprisent.service.interfaces.operation.statistics.StatisticService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementa los servicios de pacientes para Enterprise NT
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/06/2017
 * @see Creación
 */
@Service
public class PatientServiceEnterpriseNT implements PatientService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PatientsDao dao;
    @Autowired
    private DemographicDao demographicDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private ResultDao resultDao;
    @Autowired
    private BillingIntegrationService billingIntegrationService;
    @Autowired
    private HomeBoundDao homeBoundDao;
    @Autowired
    private ListService listService;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private OrdersDao ordersDao;

    //@Transactional(readOnly = false, isolation=Isolation.READ_UNCOMMITTED)
    @Override
    public Patient get(int id) throws Exception
    {
        List<Demographic> demos = demographicDao.list().stream().filter((Demographic t) -> t.getOrigin().equals("H") && t.isState()).collect(Collectors.toList());
        return dao.get(id, demos);
    }    
    
    @Override
    public Patient getEmail(int id) throws Exception
    {
        List<Demographic> demos = demographicDao.list().stream().filter((Demographic t) -> t.getOrigin().equals("H") && t.isState()).collect(Collectors.toList());
        return dao.getEmail(id, demos);
    }

    @Override
    public Patient get(String patientId) throws Exception
    {
        String encryptedPatientId = Tools.encrypt(patientId);
        List<Demographic> demos = demographicDao.list().stream().filter((Demographic t) -> t.getOrigin().equals("H") && t.isState()).collect(Collectors.toList());
        return dao.get(encryptedPatientId, demos);
    }

    @Override
    public Patient get(String patientId, int documentType, int id) throws Exception
    {
        String encryptedPatientId = Tools.encrypt(patientId);
        List<Demographic> demos = demographicDao.list().stream().filter((Demographic t) -> t.getOrigin().equals("H") && t.isState()).collect(Collectors.toList());

        return dao.get(encryptedPatientId, documentType, demos, id);

    }

    @Override
    public Patient getPatienByDemographic(int demographicId, String demographicValue) throws Exception
    {
        List<Demographic> demos = demographicDao.list().stream().filter((Demographic t) -> t.getOrigin().equals("H") && t.isState()).collect(Collectors.toList());
        return dao.getPatienByDemographic(demographicId, demographicValue, demos);
    }

    @Transactional
    @Override
    public Patient create(Patient patient, int user) throws Exception
    {
        Patient searchPatient = null;
        if (Boolean.parseBoolean(configurationService.get("ManejoTipoDocumento").getValue()))
        {
            //Busca paciente con tipo de documento
            if (patient.getPatientId() != null)
            {
                if (patient.getDocumentType() != null)
                {
                    searchPatient = get(patient.getPatientId(), patient.getDocumentType().getId(), 0);
                } else

                {
                    List<String> errorFields = new ArrayList<>(0);
                    errorFields.add("1|Document Type is required");
                    throw new EnterpriseNTException(errorFields);
                }
            } else
            {
                List<String> errorFields = new ArrayList<>(0);
                errorFields.add("2|Patient Id is not defined");
                throw new EnterpriseNTException(errorFields);
            }
        } else
        {
            //Buscar paciente sin tipo de documento
            searchPatient = get(patient.getPatientId(), patient.getDocumentType().getId(), 0);
        }
        if (searchPatient == null)
        {
            boolean automaticPatientId = Boolean.parseBoolean(configurationService.get("HistoriaAutomatica") == null ? "False" : configurationService.get("HistoriaAutomatica").getValue());
            if (automaticPatientId)
            {
                patient.setPatientId(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
            } else
            {
                patient.setPatientId(Tools.encrypt(patient.getPatientId()));
            }
            //Encripta los campos del paciente
            patient.setName1(Tools.encrypt(patient.getName1()));
            patient.setName2(Tools.encrypt(patient.getName2()));
            patient.setLastName(Tools.encrypt(patient.getLastName()));
            patient.setSurName(Tools.encrypt(patient.getSurName()));
            //Registra los campos de usuario y fecha de insercion
            patient.setLastUpdateDate(new Date());
            User userBean = new User();
            userBean.setId(user);
            patient.setLastUpdateUser(userBean);
            // Creación de contraseña para el sistema de consulta web
            boolean randomPassword = configurationService.getValue("ContrasenaPaciente").isEmpty() ? false : configurationService.getValue("ContrasenaPaciente").equalsIgnoreCase("1");
            if (randomPassword)
            {
                patient.setPasswordWebQuery(Tools.generatePassword());
            } else
            {
                String identification = Tools.decrypt(patient.getPatientId());
                int start;
                if (identification.length() >= 4)
                {
                    start = identification.length() - 4;
                    patient.setPasswordWebQuery(Tools.encrypt(identification.substring(start, identification.length())));
                } else
                {
                    int count = identification.length();
                    String zerosBefore = "";
                    while (count <= 3)
                    {
                        count++;
                        zerosBefore += "0";
                    }
                    identification = zerosBefore + identification;
                    start = identification.length() - 4;
                    patient.setPasswordWebQuery(Tools.encrypt(identification.substring(start, identification.length())));
                }
            }
            //Registra en auditoria la insercion

            trackingService.registerConfigurationTracking(null, patient, Patient.class);

            //Inserta el paciente
            if (patient.isHomebound())
            {
                // Obtenemos la lista de generos
                String genderCode = patient.getSex().getCode();
                List<Item> items = listService.list(6);
                Item itemAux = items.stream().filter(item -> item.getCode().equals(genderCode)).findAny().orElse(new Item());
                patient.getSex().setId(itemAux.getId());
                patient = homeBoundDao.insertPatientFromHomebound(patient);

                if (patient.getDiagnosis() != null && patient.getDiagnosis().isEmpty() == false)
                {
                    AuthorizedUser userToken = JWT.decode(request);
                    List<CommentOrder> commentsInsert = new ArrayList<>();

                    CommentOrder commentOrder = new CommentOrder();
                    commentOrder.setIdRecord(Long.valueOf(patient.getId()));
                    commentOrder.setComment(patient.getDiagnosis());
                    commentOrder.setType((short) 2);
                    commentOrder.setUser(userToken);

                    commentsInsert.add(commentOrder);
                    commentDao.insertCommentOrder(commentsInsert);
                }
            } else
            {
                patient = dao.insert(patient);
            }
            int id = patient.getId();
            if (automaticPatientId)
            {
                patient.setPatientId(Tools.encrypt(String.valueOf(id)));
                patient = dao.update(patient);
            }
            statisticService.savePatient(id);

            //Auditoria del paciente
            List<AuditOperation> audit = new ArrayList<>();

            audit.add(new AuditOperation(0L, patient.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_PATIENT, Tools.jsonObject(getByPatientIdAsListDemographics(null, 0, id)), null, null, null, null));
            trackingService.registerOperationTracking(audit);

            // Hilo para la creación de tercero en Siigo
            final Patient finalPatient = patient;
            CompletableFuture.runAsync(() ->
            {
                try
                {
                    if (configurationService.get("IntegracionSiigo").getValue().equalsIgnoreCase("True"))
                    {
                        AccountSiigo accountSiigo = new AccountSiigo();
                        CitySiigo citySiigo = new CitySiigo();
                        PhoneSiigo phoneSiigo = new PhoneSiigo();
                        ContactSiigo contactSiigo = new ContactSiigo();

                        String name1 = Tools.decrypt(finalPatient.getName1());
                        String name2 = Tools.decrypt(finalPatient.getName2());
                        String lastName1 = Tools.decrypt(finalPatient.getLastName());
                        String lastName2 = Tools.decrypt(finalPatient.getSurName());

                        accountSiigo.setPerson_type("Person");
                        // Nombres del tercero = nombres del paciente:
                        accountSiigo.getName().add(name1);
                        accountSiigo.getName().add(lastName1);

                        accountSiigo.setId_type("0");
                        accountSiigo.setIdDocumentType(finalPatient.getDocumentType().getId());
                        accountSiigo.setIdentification(Tools.decrypt(finalPatient.getPatientId()));

                        // Cargamos el objeto ciudad:
                        citySiigo.setCity_code("05001");
                        citySiigo.setCountry_code("Co");
                        citySiigo.setState_code("05");
                        accountSiigo.getAddress().setCity(citySiigo);
                        accountSiigo.getAddress().setAddress(finalPatient.getAddress().isEmpty() ? "N/A" : finalPatient.getAddress());
                        //accountSiigo.setPostalCode(finalPatient.getPostalCode());
                        //Cargamos el telefono:
                        phoneSiigo.setExtension("");
                        phoneSiigo.setIndicative("");
                        if (Tools.isInteger(finalPatient.getPhone()))
                        {
                            phoneSiigo.setNumber(finalPatient.getPhone());
                        } else
                        {
                            phoneSiigo.setNumber("0");
                        }
                        accountSiigo.getPhones().add(phoneSiigo);

                        contactSiigo.setFirst_name(name1);
                        contactSiigo.setLast_name(lastName1);
                        contactSiigo.setEmail(finalPatient.getEmail());
                        contactSiigo.setPhone(phoneSiigo);
                        accountSiigo.getContacts().add(contactSiigo);
                        //accountSiigo.setDirectorID(0);
                        //accountSiigo.setSalesmanID(0);
                        //accountSiigo.setCollectorID(0);
                        //accountSiigo.setPrincipalContactID(0);
                        //accountSiigo.setFiscalResponsibilities(finalPatient.getResponsable());
                        billingIntegrationService.sendToCreateAThird(accountSiigo);
                    }
                } catch (Exception ex)
                {
                    Logger.getLogger(AccountServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            return patient;
        } else
        {
            List<String> errorFields = new ArrayList<>(0);
            errorFields.add("0|Patient already exists");
            throw new EnterpriseNTException(errorFields);
        }
    }

    @Transactional
    @Override
    public Patient update(Patient patient, int user) throws Exception
    {
        List<String> errorFields = new ArrayList<>(0);
        List<DemographicValue> demopatientold = getByPatientIdAsListDemographics(null, 0, patient.getId());
        //Obtiene el paciente por id
        Patient oldPatient = get(patient.getId());
        Patient alreadyExists = new Patient();
        if (oldPatient != null)
        {
            if (patient.getId() != 0)
            {
                // Guardamos la contraseña del paciente en la siguiente variable:
                String passwordWebQuery = oldPatient.getPasswordWebQuery();
                if (Boolean.parseBoolean(configurationService.getValue("ManejoTipoDocumento")))
                {
                    if (oldPatient.getDocumentType().getId() != null)
                    {
                        if (!oldPatient.getDocumentType().getId().equals(patient.getDocumentType().getId()) || !oldPatient.getPatientId().equals(patient.getPatientId()))
                        {
                            //Si el tipo de documento o la historia es diferente consulta si ya existe una historia con ese tipo de documento
                            alreadyExists = get(patient.getPatientId(), patient.getDocumentType().getId(), 0);
                        }
                    } else
                    {
                        //Si el tipo de documento antiguo es null se consulta si ya existe una historia con ese tipo de documento
                        alreadyExists = get(patient.getPatientId(), patient.getDocumentType().getId(), 0);
                    }
                } else
                {
                    if (!oldPatient.getPatientId().equals(patient.getPatientId()))
                    {
                        //Si la historia es diferente a la anterior, busca si ya existe esa historia
                        alreadyExists = get(patient.getPatientId());
                    }
                }

                if (alreadyExists != null && alreadyExists.getId() != null)
                {

                    errorFields.add("0|Patient exists");
                    throw new EnterpriseNTException(errorFields);
                }
                //Si no existe ese paciente
                patient.setLastUpdateDate(new Date());
                User userBean = new User();
                userBean.setId(user);
                patient.setLastUpdateUser(userBean);
                //Encripta los campos del paciente
                patient.setPatientId(Tools.encrypt(patient.getPatientId()));
                patient.setName1(Tools.encrypt(patient.getName1()));
                patient.setName2(Tools.encrypt(patient.getName2()));
                patient.setLastName(Tools.encrypt(patient.getLastName()));
                patient.setSurName(Tools.encrypt(patient.getSurName()));
                // Creación de contraseña para el sistema de consulta web
                boolean randomPassword = configurationService.getValue("ContrasenaPaciente").isEmpty() ? false : configurationService.getValue("ContrasenaPaciente").equalsIgnoreCase("1");
                int indicatorPasswordPatient = dao.getIndicatorPasswordPatient(patient.getId());
                
                if ((passwordWebQuery == null || passwordWebQuery.isEmpty()) || indicatorPasswordPatient == 0)
                {
                    if (randomPassword)
                    {
                        patient.setPasswordWebQuery(Tools.generatePassword());
                    } else
                    {
                        String identification = Tools.decrypt(patient.getPatientId());
                        int start;
                        if (identification.length() >= 4)
                        {
                            start = identification.length() - 4;
                            patient.setPasswordWebQuery(Tools.encrypt(identification.substring(start, identification.length())));
                        } else
                        {
                            int count = identification.length();
                            String zerosBefore = "";
                            while (count <= 3)
                            {
                                count++;
                                zerosBefore += "0";
                            }
                            identification = zerosBefore + identification;
                            start = identification.length() - 4;
                            patient.setPasswordWebQuery(Tools.encrypt(identification.substring(start, identification.length())));
                        }
                    }
                } else {
                    patient.setPasswordWebQuery(Tools.encrypt(passwordWebQuery));
                }
                //En caso de existir registra en auditoria y actualiza el paciente
                if (patient.isHomebound())
                {
                    // Obtenemos la lista de generos
                    String genderCode = patient.getSex().getCode();
                    List<Item> items = listService.list(6);
                    Item itemAux = items.stream().filter(item -> item.getCode().equals(genderCode)).findAny().orElse(new Item());
                    patient.getSex().setId(itemAux.getId());
                    patient = homeBoundDao.updatePatientFromHomebound(patient);
                } else
                {
                    patient = dao.update(patient);
                }
                int id = patient.getId();
                statisticService.savePatient(id);

                //Auditoria del paciente
                List<AuditOperation> audit = new ArrayList<>();
                List<DemographicValue> demopatientnew = getByPatientIdAsListDemographics(null, 0, patient.getId());
                List<DemographicValue> demographicupdate = Tools.compareDemographics(demopatientold, demopatientnew);
                if (demographicupdate.size() > 0)
                {
                    audit.add(new AuditOperation(0L, patient.getId(), null, AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_PATIENT, Tools.jsonObject(demographicupdate), null, null, null, null));
                    trackingService.registerOperationTracking(audit);
                }
                return patient;
            } else
            {
                String name = configurationService.getValue("Nombres");
                String lastname = configurationService.getValue("Apellidos");

                if ((name != null && !name.isEmpty()) || (lastname != null && !lastname.isEmpty()))
                {
                    if (!oldPatient.getName1().equals(name) || !oldPatient.getLastName().equals(lastname))
                    {
                        if (name != null && !name.isEmpty())
                        {
                            oldPatient.setName1(name);
                        }
                        if (lastname != null && !lastname.isEmpty())
                        {
                            oldPatient.setLastName(lastname);
                        }
                        dao.update(oldPatient);
                    }
                }

                return patient;
            }
        } else
        {
            errorFields.add("0|Patient no exists");
            throw new EnterpriseNTException(errorFields);
        }
    }

    @Transactional
    @Override
    public Patient save(Patient patient, int user, boolean updatePatient) throws Exception
    {
        
        if (patient.getId() == null)
        {
            if (patient.getPatientId() == null)
            {
                //Nuevo paciente con historia automatica
                //Establece una historia temporal
                patient.setPatientId("-1");
                //Ingresa el paciente con una historia temporal
                Patient created = create(patient, user);
                //Despues de creado el paciente se establece la historia como el mismo id
                created.setPatientId(String.valueOf(created.getId()));
                //Se actualiza el paciente
                return update(patient, user);
            } else
            {
                //Consulta por historia si el paciente existe
                Patient p = null;
                if (Boolean.parseBoolean(configurationService.get("ManejoTipoDocumento").getValue()))
                {
                    if (patient.getDocumentType() != null && patient.getDocumentType().getId() != null)
                    {

                        p = get(patient.getPatientId(), patient.getDocumentType().getId(), 0);
                    } else

                    {
                        List<String> errors = new ArrayList<>(0);
                        errors.add("5|Document Type is required");
                        throw new EnterpriseNTException(errors);
                    }

                } else
                {
                    p = get(patient.getPatientId(), patient.getDocumentType().getId(), 0);
                }
                
                int indicatorPasswordPatient = 0;
                
                if(p != null){
                    indicatorPasswordPatient = dao.getIndicatorPasswordPatient(p.getId());
                }
                
                if (updatePatient == true || indicatorPasswordPatient == 0)
                {
                    if (p == null)
                    {

                        //Crea un nuevo paciente
                        return create(patient, user);
                    } else
                    {
                        //Se debe actualizar un paciente y se le establece el id que tiene en base de datos
                        patient.setId(p.getId());
                        return update(patient, user);
                    }
                } else
                {
                    return p;
                }
            }
        } else
        {
            //Actualiza el paciente
            return update(patient, user);
        }
    }

    @Override
    public Patient get(long order) throws Exception
    {
        return dao.get(order, demographicDao.list().stream().filter((Demographic t) -> t.getOrigin().equals("H") && t.isState()).collect(Collectors.toList()));
    }

    @Override
    public Patient getBasicPatientbyOrder(long order) throws Exception
    {
        return dao.getPatienbyOrder(order);
    }

    @Override
    public List<DemographicValue> getAsListDemographics(long order) throws Exception
    {
        List<Demographic> demos = demographicDao.list().stream().filter((Demographic t) -> t.getOrigin().equals("H") && t.isState()).collect(Collectors.toList());
        Patient patient = dao.get(order, demos);
        if (patient != null)
        {
            List<DemographicValue> demosValues = new ArrayList<>(0);
            DemographicValue demo = null;
            //Historia
            //Id de historia
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_DB_ID);
            demo.setDemographic("Patient Db Id");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(String.valueOf(patient.getId()));
            demosValues.add(demo);
            //Tipo de Documento
            if (Boolean.parseBoolean(configurationService.get("ManejoTipoDocumento").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.DOCUMENT_TYPE);
                demo.setDemographic("Document Type");
                demo.setEncoded(true);
                demo.setCodifiedId(patient.getDocumentType() != null ? patient.getDocumentType().getId() : null);
                demo.setCodifiedCode(patient.getDocumentType() != null ? patient.getDocumentType().getAbbr() : null);
                demo.setCodifiedName(patient.getDocumentType() != null ? patient.getDocumentType().getName() : null);
                demosValues.add(demo);
            }
            //Cedula
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_ID);
            demo.setDemographic("Patient Id");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getPatientId());
            demosValues.add(demo);

            //Apellido
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_LAST_NAME);
            demo.setDemographic("Last Name");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getLastName());
            demosValues.add(demo);

            //Segundo Apellido
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_SURNAME);
            demo.setDemographic("Surname");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getSurName());
            demosValues.add(demo);

            //Nombre
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_NAME);
            demo.setDemographic("Name");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getName1());
            demosValues.add(demo);

            //Nombre
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_SECOND_NAME);
            demo.setDemographic("Name 2");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getName2());
            demosValues.add(demo);

            //Sexo
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_SEX);
            demo.setDemographic("Sex");
            demo.setEncoded(true);
            demo.setCodifiedId(patient.getSex().getId());
            demo.setCodifiedCode(patient.getSex().getCode());
            demo.setCodifiedName(patient.getSex().getEsCo());
            demosValues.add(demo);

            //Fecha Nacimiento
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_BIRTHDAY);
            demo.setDemographic("Birthday");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(new SimpleDateFormat(configurationService.get("FormatoFecha").getValue()).format(patient.getBirthday()));
            demosValues.add(demo);

            //Email
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_EMAIL);
            demo.setDemographic("Email");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getEmail());
            demosValues.add(demo);

            //Telefono
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_PHONE);
            demo.setDemographic("Phone");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getPhone());
            demosValues.add(demo);

            //Dirección
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_ADDRESS);
            demo.setDemographic("Address");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getAddress());
            demosValues.add(demo);

            //Consulta los demograficos fijos
            //Dato Fijo Peso
            if (Boolean.parseBoolean(configurationService.get("ManejoPeso").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.WEIGHT);
                demo.setDemographic("Weigth");
                demo.setEncoded(false);
                demo.setNotCodifiedValue(patient.getWeight() == null ? "" : patient.getWeight().toString());
                demosValues.add(demo);
            }
            //Manejo Peso
            if (Boolean.parseBoolean(configurationService.get("ManejoTalla").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.SIZE);
                demo.setDemographic("Size");
                demo.setEncoded(false);
                demo.setNotCodifiedValue(patient.getSize() == null ? "" : patient.getSize().toString());
                demosValues.add(demo);
            }
            //Manejo Raza
            if (Boolean.parseBoolean(configurationService.get("ManejoRaza").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.RACE);
                demo.setDemographic("Race");
                demo.setEncoded(true);
                demo.setCodifiedId(patient.getRace() != null ? patient.getRace().getId() : null);
                demo.setCodifiedCode(patient.getRace() != null ? patient.getRace().getCode() : null);
                demo.setCodifiedName(patient.getRace() != null ? patient.getRace().getName() : null);
                demosValues.add(demo);
            }
            if (patient.getDemographics() != null)
            {
                demosValues.addAll(patient.getDemographics());
            }
            return demosValues;
        }
        return new ArrayList<>();

    }

    @Override
    public List<DemographicValue> getByPatientIdAsListDemographics(String patientId) throws Exception
    {
        List<DemographicValue> demosValues = new ArrayList<>(0);
        Patient patient = get(patientId);
        if (patient != null)
        {
            DemographicValue demo = null;
            //Historia
            //Id de historia
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_DB_ID);
            demo.setDemographic("Patient Db Id");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(String.valueOf(patient.getId()));
            demosValues.add(demo);
            //Tipo de Documento
            if (Boolean.parseBoolean(configurationService.get("ManejoTipoDocumento").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.DOCUMENT_TYPE);
                demo.setDemographic("Document Type");
                demo.setEncoded(true);
                demo.setCodifiedId(patient.getDocumentType() != null ? patient.getDocumentType().getId() : null);
                demo.setCodifiedCode(patient.getDocumentType() != null ? patient.getDocumentType().getAbbr() : null);
                demo.setCodifiedName(patient.getDocumentType() != null ? patient.getDocumentType().getName() : null);
                demosValues.add(demo);
            }
            //Cedula
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_ID);
            demo.setDemographic("Patient Id");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getPatientId());
            demosValues.add(demo);

            //Apellido
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_LAST_NAME);
            demo.setDemographic("Last Name");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getLastName());
            demosValues.add(demo);

            //Segundo Apellido
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_SURNAME);
            demo.setDemographic("Surname");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getSurName());
            demosValues.add(demo);

            //Nombre
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_NAME);
            demo.setDemographic("Name");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getName1());
            demosValues.add(demo);

            //Nombre
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_SECOND_NAME);
            demo.setDemographic("Name 2");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getName2());
            demosValues.add(demo);

            //Sexo
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_SEX);
            demo.setDemographic("Sex");
            demo.setEncoded(true);
            demo.setCodifiedId(patient.getSex().getId());
            demo.setCodifiedCode(patient.getSex().getCode());
            demo.setCodifiedName(patient.getSex().getEsCo());
            demosValues.add(demo);

            //Fecha Nacimiento
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_BIRTHDAY);
            demo.setDemographic("Birthday");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(new SimpleDateFormat(configurationService.get("FormatoFecha").getValue()).format(patient.getBirthday()));
            demosValues.add(demo);

            //Email
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_EMAIL);
            demo.setDemographic("Email");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getEmail());
            demosValues.add(demo);

            //Telefono
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_PHONE);
            demo.setDemographic("Phone");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getPhone());
            demosValues.add(demo);

            //Dirección
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_ADDRESS);
            demo.setDemographic("Address");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getAddress());
            demosValues.add(demo);

            //Consulta los demograficos fijos
            //Dato Fijo Peso
            if (Boolean.parseBoolean(configurationService.get("ManejoPeso").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.WEIGHT);
                demo.setDemographic("Weigth");
                demo.setEncoded(false);
                demo.setNotCodifiedValue(patient.getWeight() == null ? "" : patient.getWeight().toString());
                demosValues.add(demo);
            }
            //Manejo Peso
            if (Boolean.parseBoolean(configurationService.get("ManejoTalla").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.SIZE);
                demo.setDemographic("Size");
                demo.setEncoded(false);
                demo.setNotCodifiedValue(patient.getSize() == null ? "" : patient.getSize().toString());
                demosValues.add(demo);
            }
            //Manejo Raza
            if (Boolean.parseBoolean(configurationService.get("ManejoRaza").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.RACE);
                demo.setDemographic("Race");
                demo.setEncoded(true);
                demo.setCodifiedId(patient.getRace() != null ? patient.getRace().getId() : null);
                demo.setCodifiedCode(patient.getRace() != null ? patient.getRace().getCode() : null);
                demo.setCodifiedName(patient.getRace() != null ? patient.getRace().getName() : null);
                demosValues.add(demo);
            }
            //Diagnostico Permanente
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_COMMENT);
            demo.setDemographic("Comment");
            demo.setEncoded(false);
//            demo.setNotCodifiedValue(patient.getDiagnostic());
            demosValues.add(demo);

            if (patient.getDemographics() != null)
            {
                demosValues.addAll(patient.getDemographics());
            }
            return demosValues;
        } else
        {
            return demosValues;
        }
    }

    @Override
    public List<DemographicValue> getByPatientIdAsListDemographics(String patientId, int documentType, int id) throws Exception
    {
        List<DemographicValue> demosValues = new ArrayList<>(0);
        Patient patient = get(patientId, documentType, id);
        if (patient != null)
        {
            DemographicValue demo = null;
            //Historia
            //Id de historia
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_DB_ID);
            demo.setDemographic("Patient Db Id");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(String.valueOf(patient.getId()));
            demosValues.add(demo);
            //Tipo de Documento
            if (Boolean.parseBoolean(configurationService.get("ManejoTipoDocumento").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.DOCUMENT_TYPE);
                demo.setDemographic("Document Type");
                demo.setEncoded(true);
                demo.setCodifiedId(patient.getDocumentType() != null ? patient.getDocumentType().getId() : null);
                demo.setCodifiedCode(patient.getDocumentType() != null ? patient.getDocumentType().getAbbr() : null);
                demo.setCodifiedName(patient.getDocumentType() != null ? patient.getDocumentType().getName() : null);
                demosValues.add(demo);
            }
            //Cedula
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_ID);
            demo.setDemographic("Patient Id");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getPatientId());
            demosValues.add(demo);

            //Apellido
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_LAST_NAME);
            demo.setDemographic("Last Name");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getLastName());
            demosValues.add(demo);

            //Segundo Apellido
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_SURNAME);
            demo.setDemographic("Surname");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getSurName());
            demosValues.add(demo);

            //Nombre
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_NAME);
            demo.setDemographic("Name");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getName1());
            demosValues.add(demo);

            //Nombre
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_SECOND_NAME);
            demo.setDemographic("Name 2");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getName2());
            demosValues.add(demo);

            //Sexo
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_SEX);
            demo.setDemographic("Sex");
            demo.setEncoded(true);
            demo.setCodifiedId(patient.getSex().getId());
            demo.setCodifiedCode(patient.getSex().getCode());
            demo.setCodifiedName(patient.getSex().getEsCo());
            demosValues.add(demo);

            //Fecha Nacimiento
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_BIRTHDAY);
            demo.setDemographic("Birthday");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(new SimpleDateFormat(configurationService.get("FormatoFecha").getValue()).format(patient.getBirthday()));
            demosValues.add(demo);

            //Email
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_EMAIL);
            demo.setDemographic("Email");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getEmail());
            demosValues.add(demo);

            //Telefono
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_PHONE);
            demo.setDemographic("Phone");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getPhone());
            demosValues.add(demo);

            //Dirección
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_ADDRESS);
            demo.setDemographic("Address");
            demo.setEncoded(false);
            demo.setNotCodifiedValue(patient.getAddress());
            demosValues.add(demo);

            //Consulta los demograficos fijos
            //Dato Fijo Peso
            if (Boolean.parseBoolean(configurationService.get("ManejoPeso").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.WEIGHT);
                demo.setDemographic("Weigth");
                demo.setEncoded(false);
                demo.setNotCodifiedValue(patient.getWeight() == null ? "" : patient.getWeight().toString());
                demosValues.add(demo);
            }
            //Manejo Peso
            if (Boolean.parseBoolean(configurationService.get("ManejoTalla").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.SIZE);
                demo.setDemographic("Size");
                demo.setEncoded(false);
                demo.setNotCodifiedValue(patient.getSize() == null ? "" : patient.getSize().toString());
                demosValues.add(demo);
            }
            //Manejo Raza
            if (Boolean.parseBoolean(configurationService.get("ManejoRaza").getValue()))
            {
                demo = new DemographicValue();
                demo.setIdDemographic(Constants.RACE);
                demo.setDemographic("Race");
                demo.setEncoded(true);
                demo.setCodifiedId(patient.getRace() != null ? patient.getRace().getId() : null);
                demo.setCodifiedCode(patient.getRace() != null ? patient.getRace().getCode() : null);
                demo.setCodifiedName(patient.getRace() != null ? patient.getRace().getName() : null);
                demosValues.add(demo);
            }

            //Diagnostico Permanente
            demo = new DemographicValue();
            demo.setIdDemographic(Constants.PATIENT_COMMENT);
            demo.setDemographic("Comment");
            demo.setEncoded(false);
//            demo.setNotCodifiedValue(patient.getDiagnostic());
            demosValues.add(demo);

            if (patient.getDemographics() != null)
            {
                demosValues.addAll(patient.getDemographics());
            }
            return demosValues;
        } else
        {
            return demosValues;
        }
    }

    @Override
    public void updatePhoto(PatientPhoto photo) throws Exception
    {
        dao.updatePhoto(photo);
    }

    @Override
    public PatientPhoto getPhoto(int id) throws Exception
    {
        PatientPhoto photo = dao.getPhoto(id);
        return photo == null ? null : (photo.getPhotoInBase64() == null || photo.getPhotoInBase64().trim().isEmpty() ? null : photo);
    }

    @Override
    public List<Patient> listByLastName(String name, String name1, String lastName, String surName, int gender, List<Demographic> demographics) throws Exception
    {
        if (name != null && !name.toLowerCase().equals("undefined"))
        {
            name = Tools.encrypt(name);
        } else
        {
            name = null;
        }
        
        if (name1 != null && !name1.toLowerCase().equals("undefined"))
        {
            name1 = Tools.encrypt(name1);
        } else
        {
            name1 = null;
        }
        
        if (lastName != null && !lastName.toLowerCase().equals("undefined"))
        {
            lastName = Tools.encrypt(lastName);
        } else
        {
            lastName = null;
        }

        if (surName != null && !surName.toLowerCase().equals("undefined"))
        {
            surName = Tools.encrypt(surName);
        } else
        {
            surName = null;
        }   
        return dao.list(gender, name, name1, lastName, surName, null, null, demographics);
    }

    @Override
    public List<Patient> getPatientBy(String lastName, String surName, String name1, String name2, Integer sex, Long birthday) throws Exception
    {
        if (lastName != null && !lastName.trim().isEmpty())
        {
            lastName = Tools.encrypt(lastName);
        } else
        {
            lastName = null;
        }

        if (surName != null && !surName.trim().isEmpty())
        {
            surName = Tools.encrypt(surName);
        } else
        {
            surName = null;
        }

        if (name1 != null && !name1.trim().isEmpty())
        {
            name1 = Tools.encrypt(name1);
        } else
        {
            name1 = null;
        }

        if (name2 != null && !name2.trim().isEmpty())
        {
            name2 = Tools.encrypt(name2);
        } else
        {
            name2 = null;
        }
        if (sex == 0)
        {
            sex = null;
        }
        Date dob;
        if (birthday == 0)
        {
            dob = null;
        } else
        {
            dob = new Date(birthday);
        }
        return dao.get(lastName, surName, name1, name2, dob, sex);
    }

    @Override
    public HistoricalResult createPatientHistory(HistoricalResult historicalResult, int iduser) throws Exception
    {
        List<String> errors = validatedPatientHistory(historicalResult, true);
        if (errors.isEmpty())
        {
            if (historicalResult.getLastResultUser() == null)
            {
                historicalResult.setLastResultUser(new User());
                historicalResult.setSecondLastResultDateTemp(new Date());
                if (iduser == -1)
                {

                    historicalResult.getLastResultUser().setId(JWT.decode(request).getId());
                } else
                {
                    historicalResult.getLastResultUser().setId(iduser);
                }
            }
            return dao.createPatientHistory(historicalResult);
        } else
        {
            ResultsLog.info("ERROR createPatientHistory: " + Tools.jsonObject(errors));
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public HistoricalResult updatePatientHistory(HistoricalResult historicalResult) throws Exception
    {
        List<String> errors = validatedPatientHistory(historicalResult, false);
        if (errors.isEmpty())
        {

            if (historicalResult.getLastResult() != null && historicalResult.getLastResultUser() == null)
            {
                historicalResult.setLastResultUser(new User());
                historicalResult.getLastResultUser().setId(JWT.decode(request).getId());
            }
            return dao.updatePatientHistory(historicalResult);
        } else
        {
            ResultsLog.info("ERROR createPatientHistory: " + Tools.jsonObject(errors));
            throw new EnterpriseNTException(errors);
        }
    }

    private List<String> validatedPatientHistory(HistoricalResult historicalResult, boolean create) throws Exception
    {
        List<String> errors = new ArrayList<>(0);
        if (historicalResult.getPatientId() == 0)
        {
            errors.add("0|Id patient not exist");
        }
        if (historicalResult.getTestId() == 0)
        {
            errors.add("0|Id test not exist");
        }
        HistoricalResult resp = resultDao.get(historicalResult.getPatientId(), historicalResult.getTestId());
        if (create == true && resp != null)
        {
            errors.add("1|Duplicate record");
        } else if (create == false && resp == null)
        {
            errors.add("1|Registration does not exist");
        }
        return errors;
    }

    @Override
    public Integer numberPatients() throws Exception
    {
        return dao.numberPatients();
    }

    @Override
    public List<Patient> listPatientsByPag(PatientReport patientReport) throws Exception
    {
        return dao.listPatientsByPag(patientReport);
    }

    /**
     * Obtiene un paciente con la informacion mas basica requerida
     *
     * @param documentType
     * @param history
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    @Override
    public Patient getBasicPatientInformation(int documentType, String history) throws Exception
    {
        return dao.getBasicPatientInformation(documentType, history);
    }

    /**
     * Obtiene un paciente con la informacion mas basica requerida
     *
     * @param initialDate
     * @param endDate
     * @param filterType
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    @Override
    public List<Patient> getBasicPatientInformationByDate(Integer initialDate, Integer endDate, int patientStatus, int filterType) throws Exception
    {
        return dao.getBasicPatientInformationByDate(initialDate, endDate, patientStatus, filterType);
    }

    /**
     * Actualiza la informacion mas basica requerida para un paciente
     *
     * @param patient
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    @Override
    public int updateBasicPatientInformation(Patient patient) throws Exception
    {
        try
        {
            int rowsAffect = 0;
            Patient patientOld = get(patient.getId());
            // Paciente Actualizado con los datos suministrados
            Patient patientTwo = get(patient.getId());
            patientTwo.setName1(Tools.encrypt(patient.getName1()));
            patientTwo.setName2(Tools.encrypt(patient.getName2()));
            patientTwo.setLastName(Tools.encrypt(patient.getLastName()));
            patientTwo.setSurName(Tools.encrypt(patient.getSurName()));
            patientTwo.setBirthday(patient.getBirthday());
            patientTwo.setStatus(patient.getStatus());
            patientTwo.getSex().setId(patient.getSex().getId());
            patientTwo.getDocumentType().setId(patient.getDocumentType().getId());
            //Registra en auditoria la insercion
            trackingService.registerConfigurationTracking(patientOld, patientTwo, Patient.class);
            //Auditoria del paciente
            List<AuditOperation> audit = new ArrayList<>();
            int id = patient.getId();
            audit.add(new AuditOperation(0L, patient.getId(), null, AuditOperation.ACTION_INSERT, AuditOperation.TYPE_PATIENT, Tools.jsonObject(getByPatientIdAsListDemographics(null, 0, id)), null, null, null, null));
            trackingService.registerOperationTracking(audit);

            rowsAffect = dao.updateBasicPatientInformation(patient);
            if (patient.getStatus() > 1)
            {
                boolean exist = dao.verifyExistenceCourtData(patient.getId());
                patient.setDataTribunal(patient.getDataTribunal() == null ? "" : patient.getDataTribunal());
                if (rowsAffect != -1 && !exist)
                {
                    rowsAffect = dao.insertCourtData(patient);
                } else if (rowsAffect != -1 && exist)
                {
                    rowsAffect = dao.updateCourtData(patient);
                }
            }

            if (rowsAffect != -1)
            {
                // Obtenemos la ultima orden que se le a realizado a un paciente
                long idLastOrder = dao.lastOrderPatient(patient.getId());
                ordersDao.returnToOriginalState(idLastOrder);
            }
            return rowsAffect;
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Actualiza la informacion mas basica requerida para un paciente
     *
     * @param patient
     * @return Paciente
     * @throws Exception Error en el servicio
     */
    @Override
    public int updateStatePatient(Patient patient) throws Exception
    {
        try
        {
            int rowsAffect = 0;

            // Paciente Actualizado con los datos suministrados
            //Patient patientTwo = get(patient.getId());
            //patientTwo.setStatus(patient.getStatus());
            //Auditoria del paciente
            // List<AuditOperation> audit = new ArrayList<>();
            //audit.add(new AuditOperation(0L, patientTwo.getId(), AuditOperation.ACTION_UPDATE, AuditOperation.TYPE_DEMOGRAPHIC, Tools.jsonObject(patientTwo), null, null));
            //trackingService.registerOperationTracking(audit);
            rowsAffect = dao.updateStatePatient(patient);
            /*if (patient.getStatus() > 1)
            {
                boolean exist = dao.verifyExistenceCourtData(patient.getId());
                patient.setDataTribunal(patient.getDataTribunal() == null ? "" : patient.getDataTribunal());
                if (rowsAffect != -1 && !exist)
                {
                    rowsAffect = dao.insertCourtData(patient);
                }
                else if (rowsAffect != -1 && exist)
                {
                    rowsAffect = dao.updateCourtData(patient);
                }
            }*/

            if (rowsAffect != -1)
            {
                // Obtenemos la ultima orden que se le a realizado a un paciente
                long idLastOrder = dao.lastOrderPatient(patient.getId());
                ordersDao.returnToOriginalState(idLastOrder);
            }
            return rowsAffect;
        } catch (Exception e)
        {
            return -1;
        }
    }

    @Override
    public Patient save(Patient patient, int user) throws Exception
    {
        if (patient.getId() == null)
        {
            if (patient.getPatientId() == null)
            {
                //Nuevo paciente con historia automatica
                //Establece una historia temporal
                //patient.setPatientId("-1");
                //Ingresa el paciente con una historia temporal
                Patient created = create(patient, user);
                //Despues de creado el paciente se establece la historia como el mismo id
                created.setPatientId(String.valueOf(created.getId()));
                //Se actualiza el paciente
                return create(patient, user); //update(patient, user);
            } else
            {
                //Consulta por historia si el paciente existe
                Patient p = null;
                if (Boolean.parseBoolean(configurationService.get("ManejoTipoDocumento").getValue()))
                {
                    if (patient.getDocumentType() != null && patient.getDocumentType().getId() != null)
                    {

                        try
                        {
                            p = get(patient.getPatientId(), patient.getDocumentType().getId());
                        } catch (Exception e)
                        {
                            p = null;
                        }

                    } else
                    {
                        OrderCreationLog.info("no DOCUMENTE TYPE");
                        List<String> errors = new ArrayList<>(0);
                        errors.add("5|Document Type is required");
                        throw new EnterpriseNTException(errors);
                    }

                } else
                {
                    try
                    {
                        p = get(patient.getPatientId(), patient.getDocumentType().getId());
                    } catch (Exception e)
                    {
                        p = null;
                    }
                }

                if (p == null)
                {
                    //Crea un nuevo paciente

                    return create(patient, user);
                } else
                {
                    //Se debe actualizar un paciente y se le establece el id que tiene en base de datos
                    patient.setId(p.getId());
                    return update(patient, user);
                }
            }
        } else
        {
            //Actualiza el paciente
            return update(patient, user);
        }
    }

    @Override
    public Patient get(String patientId, int documentType) throws Exception
    {
        String encryptedPatientId = Tools.encrypt(patientId);
        List<Demographic> demos = demographicDao.list().stream().filter((Demographic t) -> t.getOrigin().equals("H") && t.isState()).collect(Collectors.toList());
        return dao.get(encryptedPatientId, documentType, demos);
    }
}
