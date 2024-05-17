package net.cltech.enterprisent.service.impl.enterprisent.masters.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.common.TrackingDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.common.Tracking;
import net.cltech.enterprisent.domain.common.TrackingDetail;
import net.cltech.enterprisent.domain.integration.siga.SigaService;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicItem;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.microbiology.Antibiotic;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.operation.common.AuditOperation;
import net.cltech.enterprisent.domain.operation.tracking.AuditFilter;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationSigaService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicItemService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.OrderTypeService;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.AntibioticService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.DestinationService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.start.StartApp;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

/**
 * Implementa los servicios de informacion de la auditoria para Enterprise NT
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Para cuando se crea una clase incluir
 *
 * @version 1.0.0
 * @author enavas
 * @see 28/04/2017
 * @see Creacion de los Servicios de la auditoria con base de datos relacionales
 * para Enterprise NT
 */
@Service
public class TrackingServiceEnterpriseNT implements TrackingService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TrackingDao dao;
    @Autowired
    private OrderTypeService orderTypeService;
    @Autowired
    private DemographicService demographicService;
    @Autowired
    private DemographicItemService demographicItemService;
    @Autowired
    private DestinationService destinationService;
    @Autowired
    private AntibioticService service;
    @Autowired
    private IntegrationSigaService integrationSigaService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private ConfigurationDao configurationDao;
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public void registerConfigurationTracking(Object oldObject, Object newObject, Class type)
    {
        try
        {
            //Se crea el objeto de auditoria
            Tracking bean = new Tracking();
            bean.setModule(type.getName());
            registerAudit(oldObject, newObject, type, bean);

            if (type.getSimpleName().equals("Configuration") && configurationService.getValue("ManejoMultiSedes").toLowerCase().equals("true"))
            {
                registerBranch(request, newObject);
            }
        } catch (Exception ex)
        {
            StartApp.logger.log(Level.SEVERE, "Error registrando auditoria", ex);
        }
    }

    /**
     * Extract path from a controller mapping. /controllerUrl/** => return
     * matched
     *
     **
     * @param request incoming request.
     * @param newobject
     *
     */
    public void registerBranch(final HttpServletRequest request, Object newobject) throws Exception
    {
        String s = newobject.toString();
        String path = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        String type = (String) request.getMethod();
        List<Branch> listBranchs = branchService.list();

        ObjectMapper mapper = new ObjectMapper();
        String object = mapper.writeValueAsString(newobject);

        dao.registerBranch(listBranchs, path, type, object);
    }

    @Override
    public void registerConfigurationTracking(Object oldObject, Object newObject, Class type, String comment)
    {
        try
        {
            //Se crea el objeto de auditoria
            Tracking bean = new Tracking();
            if (comment.equals(""))
            {
                bean.setModule(type.getName());
            } else
            {
                bean.setModule(comment);
            }
            registerAudit(oldObject, newObject, type, bean);
        } catch (Exception ex)
        {
            StartApp.logger.log(Level.SEVERE, "Error registrando auditoria", ex);
        }
    }

    private void registerAudit(Object oldObject, Object newObject, Class type, Tracking bean)
    {
        try
        {
            //Obtiene la sesión de la peticion
            AuthorizedUser user = getRequestUser();
            bean.setDate(new Date());
            bean.setUserId(user.getId());
            bean.setUser(user.getUserName());
            bean.setUserName(user.getLastName() + " " + user.getName());
            bean.setUrl(request.getRequestURI());
            bean.setHost(request.getLocalName());
            List fielList;
            String jsonContent = "";
            ObjectMapper mapper = new ObjectMapper();
            List<TrackingDetail> trackingFields = new ArrayList<>(0);
            TrackingDetail detailField = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //validamos si es una Lista
            boolean isList = newObject instanceof Collection;
            if (isList)
            {
                if (oldObject == null)
                {
                    //INSERCION
                    bean.setState(Tracking.STATE_INSERT);
                    detailField = new TrackingDetail();
                    detailField.setField(newObject.getClass().getTypeName());
                    detailField.setFieldList(newObject.getClass().getTypeName());
                    mapper = new ObjectMapper();
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    jsonContent = mapper.writeValueAsString(newObject);
                    detailField.setNewValue(newObject != null ? jsonContent : null);
                    trackingFields.add(detailField);
                    bean.setFields(trackingFields);
                    dao.create(bean);
                } else if (newObject != null)
                {
                    //ACTUALIZAR
                    bean.setState(Tracking.STATE_UPDATE);
                    detailField = new TrackingDetail();
                    mapper = new ObjectMapper();
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    if (oldObject != null && newObject != null)
                    {
                        if (!oldObject.equals(newObject))
                        {
                            detailField.setOldValue(mapper.writeValueAsString(oldObject));
                            detailField.setNewValue(mapper.writeValueAsString(newObject));
                            detailField.setFieldList(newObject.getClass().getTypeName());
                            trackingFields.add(detailField);
                        }
                    } else
                    {
                        if (oldObject != null)
                        {
                            detailField.setOldValue(mapper.writeValueAsString(oldObject));
                            trackingFields.add(detailField);
                        } else if (newObject != null)
                        {
                            detailField.setNewValue(mapper.writeValueAsString(newObject));
                            trackingFields.add(detailField);
                        }
                    }
                    if (!trackingFields.isEmpty())
                    {
                        bean.setFields(trackingFields);
                        dao.create(bean);
                    }
                }
            } else
            {
                List<Field> fields = getAllFields(new ArrayList<>(), type);
                if (oldObject == null)
                {
                    //Es una insercion
                    bean.setState(Tracking.STATE_INSERT);
                    for (Field field : fields)
                    {
                        field.setAccessible(true);
                        detailField = new TrackingDetail();
                        detailField.setField(field.getName());
                        if (field.get(newObject) != null)
                        {
                            if (field.getType().getClass().getSimpleName().equals("Date"))
                            {
                                detailField.setNewValue(sdf.format(((Date) field.get(newObject))));
                            } else if (field.getType().getName().equals("java.util.List"))
                            {
                                fielList = (List) field.get(newObject);
                                if (fielList != null && fielList.size() > 0)
                                {
                                    detailField.setFieldList(fielList.get(0).getClass().getTypeName());
                                    mapper = new ObjectMapper();
                                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                                    jsonContent = mapper.writeValueAsString(field.get(newObject));
                                    detailField.setNewValue(field.get(newObject) != null ? jsonContent : null);
                                }
                            } else if (field.getType().getName().equals("java.lang.String"))
                            {
                                detailField.setNewValue(field.get(newObject) != null ? field.get(newObject).toString() : null);
                            } else
                            {
                                detailField.setNewValue(field.get(newObject) != null ? mapper.writeValueAsString(field.get(newObject)) : null);
                            }
                            trackingFields.add(detailField);
                        }
                    }
                    bean.setFields(trackingFields);
                    dao.create(bean);
                } else if (newObject != null)
                {
                    //Es una actualización
                    bean.setState(Tracking.STATE_UPDATE);
                    for (Field field : fields)
                    {
                        field.setAccessible(true);
                        detailField = new TrackingDetail();
                        if (type.getSimpleName().equals("Configuration"))
                        {
                            detailField.setField(((Configuration) newObject).getKey());
                        } else
                        {
                            detailField.setField(field.getName());
                        }
                        if (field.getType().getClass().getSimpleName().equals("Date"))
                        {
                            if (field.get(oldObject) != null && field.get(newObject) != null)
                            {
                                if (!field.get(oldObject).equals(field.get(newObject)))
                                {
                                    detailField.setOldValue(sdf.format(((Date) field.get(oldObject))));
                                    detailField.setNewValue(sdf.format(((Date) field.get(newObject))));
                                    trackingFields.add(detailField);
                                }
                            } else
                            {
                                if (field.get(oldObject) != null)
                                {
                                    detailField.setOldValue(sdf.format(((Date) field.get(oldObject))));
                                    trackingFields.add(detailField);
                                } else if (field.get(newObject) != null)
                                {
                                    detailField.setNewValue(sdf.format(((Date) field.get(newObject))));
                                    trackingFields.add(detailField);
                                }
                            }
                        } else if (field.getType().getSimpleName().equals("Item"))
                        {
                            if (field.get(oldObject) != null && field.get(newObject) != null)
                            {
                                if (!field.get(oldObject).toString().equals(field.get(newObject).toString()))
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            } else
                            {
                                if (field.get(oldObject) != null)
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    trackingFields.add(detailField);
                                } else if (field.get(newObject) != null)
                                {
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            }
                        } else if (field.getType().getName().equals("java.util.List"))
                        {
                            mapper = new ObjectMapper();
                            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                            fielList = (List) field.get(newObject);

                            if (fielList != null && fielList.size() > 0)
                            {
                                detailField.setFieldList(fielList.get(0).getClass().getTypeName());
                            } else
                            {
                                fielList = (List) field.get(oldObject);
                                if (fielList != null && fielList.size() > 0)
                                {
                                    detailField.setFieldList(fielList.get(0).getClass().getTypeName());
                                }
                            }

                            if (field.get(oldObject) != null && field.get(newObject) != null)
                            {
                                if (!field.get(oldObject).equals(field.get(newObject)))
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            } else
                            {
                                if (field.get(oldObject) != null)
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    trackingFields.add(detailField);
                                } else if (field.get(newObject) != null)
                                {
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            }
                        } else if (field.getType().getName().equals("java.lang.String"))
                        {
                            if (field.get(oldObject) != null && field.get(newObject) != null)
                            {
                                if (!field.get(oldObject).equals(field.get(newObject)))
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            } else
                            {
                                if (field.get(oldObject) != null)
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    trackingFields.add(detailField);
                                } else if (field.get(newObject) != null)
                                {
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            }
                        } else
                        {
                            if (field.get(oldObject) != null && field.get(newObject) != null)
                            {
                                if (!field.get(oldObject).equals(field.get(newObject)))
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            } else
                            {
                                if (field.get(oldObject) != null)
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    trackingFields.add(detailField);
                                } else if (field.get(newObject) != null)
                                {
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            }
                        }
                    }
                    if (!trackingFields.isEmpty())
                    {
                        bean.setFields(trackingFields);
                        dao.create(bean);
                    }
                }
            }
        } catch (Exception ex)
        {
            StartApp.logger.log(Level.SEVERE, "Error registrando auditoria", ex);
        }
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type)
    {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null)
        {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }

    @Override
    public void registerOperationTracking(List<AuditOperation> audits) throws Exception
    {
        try
        {
            AuthorizedUser user = getRequestUser();
            audits.stream().forEach((AuditOperation audit) ->
            {
                audit.setUser(user.getId());
            });
        } catch (Exception e)
        {
            AuthorizedUser userSuper = new AuthorizedUser(1);
            audits.stream().forEach((AuditOperation audit) ->
            {
                audit.setUser(userSuper.getId());
            });
        }

        dao.insertOperationTracking(audits);
    }

    @Override
    public void registerOperationTracking(List<AuditOperation> audits, int mw) throws Exception
    {
        audits.stream().forEach((AuditOperation audit) ->
        {
            audit.setUser(mw);
        });
        dao.insertOperationTracking(audits);
    }

    @Override
    public List<Tracking> get(int initialDate, int finalDate, String module, Integer iduser) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date i = sdf.parse(String.valueOf(initialDate) + " 00:00:00");
        Date f = sdf.parse(String.valueOf(finalDate) + " 23:59:59");
        return dao.get(new Timestamp(i.getTime()), new Timestamp(f.getTime()), module, iduser);
    }

    @Override
    public List<Tracking> get(String initialDate, String finalDate, String module, Integer iduser) throws Exception
    {
        List<Timestamp> times = Tools.rangeDates(initialDate, finalDate);
        return dao.get(times.get(0), times.get(1), module, iduser);
    }

    @Override
    public List<Tracking> get(int initialDate, int finalDate) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date i = sdf.parse(String.valueOf(initialDate) + " 00:00:00");
        Date f = sdf.parse(String.valueOf(finalDate) + " 23:59:59");
        return dao.get(new Timestamp(i.getTime()), new Timestamp(f.getTime()), null);
    }

    @Override
    public AuthorizedUser getRequestUser() throws Exception
    {
        return JWT.decode(request);
    }

    private HashMap buildValues(List<String> key, List<String> value) throws Exception
    {
        HashMap<String, String> hashValues = new HashMap<>();
        for (int i = 0; i < key.size(); i++)
        {
            hashValues.put(key.get(i), value == null ? key.get(i) : value.get(i));
        }
        return hashValues;
    }

    private HashMap buildValues(List<String> key, List<String> value, boolean dataDefault) throws Exception
    {
        HashMap<String, String> hashValues = new HashMap<>();
        if (dataDefault)
        {
            hashValues.put("0", "No aplica");
            hashValues.put("?", "Sin seleccionar");
        }
        for (int i = 0; i < key.size(); i++)
        {
            hashValues.put(key.get(i), value == null ? key.get(i) : value.get(i));
        }
        return hashValues;
    }

    @Override
    public HashMap<String, HashMap<String, String>> getConstants() throws Exception
    {
        HashMap<String, HashMap<String, String>> constants = new HashMap<>();
        constants.put("TipoNumeroOrden", buildValues(Arrays.asList("General", "Servicio", "Sede"), null));
        constants.put("ContrasenaPaciente", buildValues(Arrays.asList("1", "2"), Arrays.asList("Contraseña aleatoria", "Ultimos dígitos del documento")));
        constants.put("SeparadorMuestra", buildValues(Arrays.asList(".", "-"), Arrays.asList("Punto", "Guion")));
        constants.put("TipoImpresion", buildValues(Arrays.asList("1", "2"), Arrays.asList("Por equipo", "Por servicio")));
        constants.put("ResolverImpresora", buildValues(Arrays.asList("1", "2"), Arrays.asList("TCP/IP", "Nombre de equipo")));
        constants.put("ImprimirInformeFinal", buildValues(Arrays.asList("1", "2"), Arrays.asList("Pruebas validadas", "Órdenes completas")));
        constants.put("EnviarCorreo", buildValues(Arrays.asList("1", "2"), Arrays.asList("Paciente", "Médico")));
        constants.put("AgrupacionOrdenes", buildValues(Arrays.asList("1", "2"), Arrays.asList("Tipo de orden", "Servicio")));
        constants.put("Facturacion", buildValues(Arrays.asList("0", "1", "2"), Arrays.asList("No aplica", "General", "USA")));
        constants.put("Trazabilidad", buildValues(Arrays.asList("1", "2", "3", "4"), Arrays.asList("No aplica", "Entrada de muestra", "Completa", "Lista")));
        constants.put("Trazabilidades", buildValues(Arrays.asList("1", "2"), Arrays.asList("General", "Tipo de orden")));
        constants.put("WhonetTipo", buildValues(Arrays.asList("1", "2"), Arrays.asList("Sitio anatómico", "Submuestra")));
        List<OrderType> listOrderType = orderTypeService.list();
        constants.put("ValorInicialTipoOrden", buildValues(
                listOrderType.stream().map((OrderType o) -> o.getId().toString()).collect(Collectors.toList()),
                listOrderType.stream().map((OrderType o) -> o.getName()).collect(Collectors.toList()),
                true)
        );
        List<Demographic> listDemographic = demographicService.demographicsList();
        HashMap<String, String> hashValuesDemographic = buildValues(
                listDemographic.stream().map((o) -> o.getId().toString()).collect(Collectors.toList()),
                listDemographic.stream().map((o) -> o.getName()).collect(Collectors.toList()),
                true);
        constants.put("DemograficoExcluirPrueba", hashValuesDemographic);
        constants.put("DemograficoInconsistensias", buildValues(
                listDemographic.stream().filter((o) -> o.getOrigin() != null && o.getOrigin().equals("H")).map((o) -> o.getId().toString()).collect(Collectors.toList()),
                listDemographic.stream().filter((o) -> o.getOrigin() != null && o.getOrigin().equals("H")).map((o) -> o.getName()).collect(Collectors.toList()),
                true)
        );
        constants.put("DemograficoPyP", hashValuesDemographic);
        constants.put("DemograficoTituloInforme", hashValuesDemographic);
        constants.put("DemograficoHistograma", buildValues(
                listDemographic.stream().filter((o) -> o.getId() > 0).map((o) -> o.getId().toString()).collect(Collectors.toList()),
                listDemographic.stream().filter((o) -> o.getId() > 0).map((o) -> o.getName()).collect(Collectors.toList()),
                true)
        );
        List<DemographicItem> listDemographicItem = demographicItemService.get(null, null, null, 1, null);
        constants.put("DemograficoItemHistograma", buildValues(
                listDemographicItem.stream().map((o) -> o.getId().toString()).collect(Collectors.toList()),
                listDemographicItem.stream().map((o) -> o.getName()).collect(Collectors.toList()),
                true)
        );
        List<Destination> listDestination = destinationService.list(true);
        HashMap<String, String> hashValuesDestination = buildValues(
                listDestination.stream().map((o) -> o.getId().toString()).collect(Collectors.toList()),
                listDestination.stream().map((o) -> o.getName()).collect(Collectors.toList()),
                true);
        constants.put("DestinoVerificaCentralMuestras", hashValuesDestination);
        constants.put("DestinoVerificaDesecho", hashValuesDestination);
        constants.put("DestinoVerificaMicrobiologia", hashValuesDestination);
        List<Antibiotic> listAntibiotics = service.filterByState(true);
        HashMap<String, String> hashValuesAntibiotics = buildValues(
                listAntibiotics.stream().map((o) -> o.getId().toString()).collect(Collectors.toList()),
                listAntibiotics.stream().map((o) -> o.getName()).collect(Collectors.toList()),
                true);
        constants.put("WhonetAPB", hashValuesAntibiotics);
        constants.put("WhonetEDTA", hashValuesAntibiotics);
        constants.put("WhonetTHM", hashValuesAntibiotics);
        List<Branch> listBranchs = branchService.list();
        constants.put("SedeSIGA", buildValues(
                listBranchs.stream().map((o) -> o.getId().toString()).collect(Collectors.toList()),
                listBranchs.stream().map((o) -> o.getName()).collect(Collectors.toList()),
                true)
        );
        if (configurationDao.get("SedeSIGA").getValue() != null && !configurationDao.get("SedeSIGA").getValue().trim().equals(""))
        {
            List<SigaService> listSiga = integrationSigaService.listService(Integer.parseInt(configurationDao.get("SedeSIGA").getValue()));
            HashMap<String, String> hashValuesSiga = buildValues(
                    listSiga.stream().map((o) -> String.valueOf(o.getId())).collect(Collectors.toList()),
                    listSiga.stream().map((o) -> o.getName()).collect(Collectors.toList()),
                    true);
            constants.put("OrdenesSIGA", hashValuesSiga);
            constants.put("VerificacionSIGA", hashValuesSiga);
        }
        return constants;
    }

    @Override
    public void registerConfigurationTracking(Object oldObject, Object newObject, Class type, AuthorizedUser userAuth)
    {
        try
        {
            //Se crea el objeto de auditoria
            Tracking bean = new Tracking();
            bean.setModule(type.getName());
            registerAudit(oldObject, newObject, type, bean, userAuth);

            if (type.getSimpleName().equals("Configuration") && configurationService.getValue("ManejoMultiSedes").toLowerCase().equals("true"))
            {
                registerBranch(request, newObject);
            }
        } catch (Exception ex)
        {
            StartApp.logger.log(Level.SEVERE, "Error registrando auditoria", ex);
        }
    }

    private void registerAudit(Object oldObject, Object newObject, Class type, Tracking bean, AuthorizedUser userAuth)
    {
        try
        {
            //Obtiene la sesión de la peticion
            AuthorizedUser user = userAuth;
            bean.setDate(new Date());
            bean.setUserId(user.getId());
            bean.setUser(user.getUserName());
            bean.setUserName(user.getLastName() + " " + user.getName());
            bean.setUrl(request.getRequestURI());
            bean.setHost(request.getLocalName());
            List fielList;
            String jsonContent = "";
            ObjectMapper mapper = new ObjectMapper();
            List<TrackingDetail> trackingFields = new ArrayList<>(0);
            TrackingDetail detailField = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //validamos si es una Lista
            boolean isList = newObject instanceof Collection;
            if (isList)
            {
                if (oldObject == null)
                {
                    //INSERCION
                    bean.setState(Tracking.STATE_INSERT);
                    detailField = new TrackingDetail();
                    detailField.setField(newObject.getClass().getTypeName());
                    detailField.setFieldList(newObject.getClass().getTypeName());
                    mapper = new ObjectMapper();
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    jsonContent = mapper.writeValueAsString(newObject);
                    detailField.setNewValue(newObject != null ? jsonContent : null);
                    trackingFields.add(detailField);
                    bean.setFields(trackingFields);
                    dao.create(bean);
                } else if (newObject != null)
                {
                    //ACTUALIZAR
                    bean.setState(Tracking.STATE_UPDATE);
                    detailField = new TrackingDetail();
                    mapper = new ObjectMapper();
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    if (oldObject != null && newObject != null)
                    {
                        if (!oldObject.equals(newObject))
                        {
                            detailField.setOldValue(mapper.writeValueAsString(oldObject));
                            detailField.setNewValue(mapper.writeValueAsString(newObject));
                            detailField.setFieldList(newObject.getClass().getTypeName());
                            trackingFields.add(detailField);
                        }
                    } else
                    {
                        if (oldObject != null)
                        {
                            detailField.setOldValue(mapper.writeValueAsString(oldObject));
                            trackingFields.add(detailField);
                        } else if (newObject != null)
                        {
                            detailField.setNewValue(mapper.writeValueAsString(newObject));
                            trackingFields.add(detailField);
                        }
                    }
                    if (!trackingFields.isEmpty())
                    {
                        bean.setFields(trackingFields);
                        dao.create(bean);
                    }
                }
            } else
            {
                List<Field> fields = getAllFields(new ArrayList<>(), type);
                if (oldObject == null)
                {
                    //Es una insercion
                    bean.setState(Tracking.STATE_INSERT);
                    for (Field field : fields)
                    {
                        field.setAccessible(true);
                        detailField = new TrackingDetail();
                        detailField.setField(field.getName());
                        if (field.get(newObject) != null)
                        {
                            if (field.getType().getClass().getSimpleName().equals("Date"))
                            {
                                detailField.setNewValue(sdf.format(((Date) field.get(newObject))));
                            } else if (field.getType().getName().equals("java.util.List"))
                            {
                                fielList = (List) field.get(newObject);
                                if (fielList != null && fielList.size() > 0)
                                {
                                    detailField.setFieldList(fielList.get(0).getClass().getTypeName());
                                    mapper = new ObjectMapper();
                                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                                    jsonContent = mapper.writeValueAsString(field.get(newObject));
                                    detailField.setNewValue(field.get(newObject) != null ? jsonContent : null);
                                }
                            } else if (field.getType().getName().equals("java.lang.String"))
                            {
                                detailField.setNewValue(field.get(newObject) != null ? field.get(newObject).toString() : null);
                            } else
                            {
                                detailField.setNewValue(field.get(newObject) != null ? mapper.writeValueAsString(field.get(newObject)) : null);
                            }
                            trackingFields.add(detailField);
                        }
                    }
                    bean.setFields(trackingFields);
                    dao.create(bean);
                } else if (newObject != null)
                {
                    //Es una actualización
                    bean.setState(Tracking.STATE_UPDATE);
                    for (Field field : fields)
                    {
                        field.setAccessible(true);
                        detailField = new TrackingDetail();
                        if (type.getSimpleName().equals("Configuration"))
                        {
                            detailField.setField(((Configuration) newObject).getKey());
                        } else
                        {
                            detailField.setField(field.getName());
                        }
                        if (field.getType().getClass().getSimpleName().equals("Date"))
                        {
                            if (field.get(oldObject) != null && field.get(newObject) != null)
                            {
                                if (!field.get(oldObject).equals(field.get(newObject)))
                                {
                                    detailField.setOldValue(sdf.format(((Date) field.get(oldObject))));
                                    detailField.setNewValue(sdf.format(((Date) field.get(newObject))));
                                    trackingFields.add(detailField);
                                }
                            } else
                            {
                                if (field.get(oldObject) != null)
                                {
                                    detailField.setOldValue(sdf.format(((Date) field.get(oldObject))));
                                    trackingFields.add(detailField);
                                } else if (field.get(newObject) != null)
                                {
                                    detailField.setNewValue(sdf.format(((Date) field.get(newObject))));
                                    trackingFields.add(detailField);
                                }
                            }
                        } else if (field.getType().getSimpleName().equals("Item"))
                        {
                            if (field.get(oldObject) != null && field.get(newObject) != null)
                            {
                                if (!field.get(oldObject).toString().equals(field.get(newObject).toString()))
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            } else
                            {
                                if (field.get(oldObject) != null)
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    trackingFields.add(detailField);
                                } else if (field.get(newObject) != null)
                                {
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            }
                        } else if (field.getType().getName().equals("java.util.List"))
                        {
                            mapper = new ObjectMapper();
                            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                            fielList = (List) field.get(newObject);

                            if (fielList != null && fielList.size() > 0)
                            {
                                detailField.setFieldList(fielList.get(0).getClass().getTypeName());
                            } else
                            {
                                fielList = (List) field.get(oldObject);
                                if (fielList != null && fielList.size() > 0)
                                {
                                    detailField.setFieldList(fielList.get(0).getClass().getTypeName());
                                }
                            }

                            if (field.get(oldObject) != null && field.get(newObject) != null)
                            {
                                if (!field.get(oldObject).equals(field.get(newObject)))
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            } else
                            {
                                if (field.get(oldObject) != null)
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    trackingFields.add(detailField);
                                } else if (field.get(newObject) != null)
                                {
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            }
                        } else if (field.getType().getName().equals("java.lang.String"))
                        {
                            if (field.get(oldObject) != null && field.get(newObject) != null)
                            {
                                if (!field.get(oldObject).equals(field.get(newObject)))
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            } else
                            {
                                if (field.get(oldObject) != null)
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    trackingFields.add(detailField);
                                } else if (field.get(newObject) != null)
                                {
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            }
                        } else
                        {
                            if (field.get(oldObject) != null && field.get(newObject) != null)
                            {
                                if (!field.get(oldObject).equals(field.get(newObject)))
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            } else
                            {
                                if (field.get(oldObject) != null)
                                {
                                    detailField.setOldValue(mapper.writeValueAsString(field.get(oldObject)));
                                    trackingFields.add(detailField);
                                } else if (field.get(newObject) != null)
                                {
                                    detailField.setNewValue(mapper.writeValueAsString(field.get(newObject)));
                                    trackingFields.add(detailField);
                                }
                            }
                        }
                    }
                    if (!trackingFields.isEmpty())
                    {
                        bean.setFields(trackingFields);
                        dao.create(bean);
                    }
                }
            }
        } catch (Exception ex)
        {
            StartApp.logger.log(Level.SEVERE, "Error registrando auditoria", ex);
        }
    }

    @Override
    public void registerInvoiceAudit(AuditOperation audit) throws Exception
    {
        AuthorizedUser user = getRequestUser();
        audit.setUser(user.getId());
        if (audit.getExecutionType().equals(AuditOperation.EXECUTION_TYPE_CB) || audit.getExecutionType().equals(AuditOperation.EXECUTION_TYPE_PT))
        {
            audit.setOrder(audit.getInvoiceId());
            audit.setInvoiceId(0L);
        } else
        {
            audit.setOrder(0L);
        }
        dao.insertInvoiceAudit(audit);
    }
    
    @Override
    public List<Tracking> getAuditFilter(AuditFilter filters) throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date i = sdf.parse(String.valueOf(filters.getInitDate()) + " 00:00:00");
        Date f = sdf.parse(String.valueOf(filters.getEndDate()) + " 23:59:59");
        return dao.get(new Timestamp(i.getTime()), new Timestamp(f.getTime()), filters.getModule(), filters.getUser());
    }
}
