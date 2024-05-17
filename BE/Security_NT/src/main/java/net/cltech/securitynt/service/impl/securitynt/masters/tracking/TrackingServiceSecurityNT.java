/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.service.impl.securitynt.masters.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import net.cltech.securitynt.dao.interfaces.common.TrackingDao;
import net.cltech.securitynt.domain.common.AuthorizedUser;
import net.cltech.securitynt.domain.common.Tracking;
import net.cltech.securitynt.domain.common.TrackingDetail;
import net.cltech.securitynt.domain.masters.configuration.Configuration;
import net.cltech.securitynt.service.interfaces.masters.tracking.TrackingService;
import net.cltech.securitynt.start.StartApp;
import net.cltech.securitynt.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class TrackingServiceSecurityNT implements TrackingService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TrackingDao dao;

    @Override
    public void registerConfigurationTracking(Object oldObject, Object newObject, Class type)
    {
        try
        {
            //Se crea el objeto de auditoria
            Tracking bean = new Tracking();
            bean.setModule(type.getName());
            registerAudit(oldObject, newObject, type, bean);
        } catch (Exception ex)
        {
            StartApp.logger.log(Level.SEVERE, "Error registrando auditoria", ex);
        }
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
    public AuthorizedUser getRequestUser() throws Exception
    {
        return JWT.decode(request);
    }

}
