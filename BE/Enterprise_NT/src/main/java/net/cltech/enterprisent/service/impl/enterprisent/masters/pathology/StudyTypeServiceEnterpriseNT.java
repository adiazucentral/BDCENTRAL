/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.pathology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import net.cltech.enterprisent.dao.interfaces.masters.pathology.StudyTypeDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.StudyType;
import net.cltech.enterprisent.domain.masters.pathology.Study;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.pathology.StudyTypeService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios del maestro de tipo de estudios de patología
 *
 * @version 1.0.0
 * @author omendez
 * @see 26/10/2020
 * @see Creaciòn
 */
@Service
public class StudyTypeServiceEnterpriseNT implements StudyTypeService
{
    @Autowired
    private StudyTypeDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private ToolsDao toolsDao;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;

    @Override
    public StudyType create(StudyType create) throws Exception
    {
        List<String> errors = validateFields(create, false);
        if (errors.isEmpty())
        {
            StudyType created = dao.create(create);
            updateCounters(created);
            trackingService.registerConfigurationTracking(null, created, StudyType.class);
            return created;
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }
 
    @Override
    public List<StudyType> list() throws Exception
    {
        List<StudyType> studies = dao.list();
        studies.forEach( study -> {
            try
            {
                study.setUserCreated(trackingPathologyDao.get(study.getUserCreated().getId()));
                study.setUserUpdated(trackingPathologyDao.get(study.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        return studies;
    }
    
    @Override
    public StudyType findById(Integer id) throws Exception
    {
        try
        {
            StudyType found = dao.list().stream()
                .filter(studyType -> studyType.getId().equals(id))
                .findFirst()
                .orElse(null);
            
            found.setUserCreated(trackingPathologyDao.get(found.getUserCreated().getId()));
            found.setUserUpdated(trackingPathologyDao.get(found.getUserUpdated().getId()));
            if (found != null)
            {
                found.setStudies(dao.listStudyTypeStudies(id));
            }
            return found;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public StudyType findByCode(String code) throws Exception
    {
        try
        {
            StudyType found = dao.list().stream()
                .filter(studyType -> studyType.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
            
            found.setUserCreated(trackingPathologyDao.get(found.getUserCreated().getId()));
            found.setUserUpdated(trackingPathologyDao.get(found.getUserUpdated().getId()));
            if (found != null)
            {
                found.setStudies(dao.listStudyTypeStudies(found.getId()));
            }
            return found;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public StudyType findByName(String name) throws Exception
    {
        try
        {
            StudyType found = dao.list().stream()
                .filter(studyType -> studyType.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
            
            found.setUserCreated(trackingPathologyDao.get(found.getUserCreated().getId()));
            found.setUserUpdated(trackingPathologyDao.get(found.getUserUpdated().getId()));
            
            if (found != null)
            {
                found.setStudies(dao.listStudyTypeStudies(found.getId()));
            }
            return found;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public StudyType findstudyTypeByStudy(Integer study) throws Exception
    {
        try
        {
            StudyType found = dao.findstudyTypeByStudy(study);
            
            found.setUserCreated(trackingPathologyDao.get(found.getUserCreated().getId()));
            found.setUserUpdated(trackingPathologyDao.get(found.getUserUpdated().getId()));
            if (found != null)
            {
                found.setStudies(dao.listStudyTypeStudies(found.getId()));
            }
            return found;
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public Study findStudyById(Integer study, Integer studyType) throws Exception
    {
        return dao.findStudyById( study, studyType );
    }
    
    @Override
    public StudyType update(StudyType update) throws Exception
    {
        List<String> errors = validateFields(update, true);
        if (errors.isEmpty())
        {
            StudyType old = findById(update.getId());
            StudyType updated = dao.update(update);
            updateCounters(update);
            trackingService.registerConfigurationTracking(old, update, StudyType.class);
            return updated;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    /**
     * Valida los campos enviados del tipo de estudio
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(StudyType validate, boolean isEdit) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (findById(validate.getId()) == null)
                {
                    errors.add("2|id");//No existe id
                }
            }
            
            if(validate.getUserUpdated().getId() == null || validate.getUserUpdated().getId().equals(0)) {
                errors.add("0|userUpdated");
            }
        } else {
            if(validate.getUserCreated().getId() == null || validate.getUserCreated().getId().equals(0)) {
                errors.add("0|userCreated");
            }
        }
        
        if (validate.getCode()== null || validate.getCode().trim().isEmpty())
        {
            errors.add("0|code");
        } else
        {
            StudyType unitFromDB = findByCode(validate.getCode());
            if (unitFromDB != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(unitFromDB.getId())))
                {
                    errors.add("1|code");
                }
            }
        }
        
        if (validate.getName() == null || validate.getName().trim().isEmpty())
        {
            errors.add("0|name");
        } else
        {
            StudyType unitFromDB = findByName(validate.getName());
            if (unitFromDB != null)
            {
                if (!isEdit || (isEdit && !validate.getId().equals(unitFromDB.getId())))
                {
                    errors.add("1|name");
                }
            }
        }
        
        if (validate.getStatus() == null)
        {
            errors.add("0|status");
        }
        
        errors.addAll(validateFieldsStudies(validate));

        return errors;

    }
    
    /**
     * Valida los campos enviados de los estudios
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFieldsStudies(StudyType validate) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (validate.getStudies().size() > 0)
        {
            int row = 0;
            for (Study study : validate.getStudies())
            {
                if (study.getId() == null || study.getId() == 0)
                {
                    errors.add("0|studyId|" + row);
                }
                
                Study found = findStudyById(study.getId(), validate.getId()); 
                if (found != null)
                {
                    errors.add("1|study|" + row);
                }
                row++;
            }
        } else
        {
            errors.add("0|Studies");
        }

        return errors;
    }
    
    @Override
    public List<StudyType> filterByState(int state) throws Exception
    {
        List<StudyType> filter = dao.list().stream()
                .filter(studyType -> studyType.isState() == state)
                .collect(Collectors.toList());
        
        filter.forEach( study -> {
            try
            {
                study.setUserCreated(trackingPathologyDao.get(study.getUserCreated().getId()));
                study.setUserUpdated(trackingPathologyDao.get(study.getUserUpdated().getId()));
            } catch (Exception e) {}
        });
        
        return filter;
    }

    @Override
    public int createStudies(StudyType createStudies) throws Exception
    {
        List<String> errors = validateFieldsStudies(createStudies);
        if (errors.isEmpty())
        {
            return dao.createStudyTypeStudies(createStudies);

        } else
        {
            throw new EnterpriseNTException(errors);

        }
    }
    
    /**
    * Actualiza los contadores de la numeracion por tipo de estudio 
    *
    * @param studyType
    * {@link net.cltech.enterprisent.domain.masters.pathology.StudyType}
    * @throws Exception Error presentado en el servicio
    */
    private void updateCounters(StudyType studyType) throws Exception
    {        
        String sequenceName = Constants.SEQUENCE_PATHOLOGY + studyType.getId();
        
        int minimun = 1;
        int maximun = Integer.parseInt(String.join("", Collections.nCopies(Integer.parseInt(configurationService.get("DigitosCaso").getValue().trim()), "9")));
        
        if (!toolsDao.validateSequencePathology(sequenceName))
        {
            toolsDao.createSequencePathology(Constants.SEQUENCE_PATHOLOGY + studyType.getId(), minimun, 1, maximun);
        } else
        {
            toolsDao.resetSequencePathology(Constants.SEQUENCE_PATHOLOGY + studyType.getId(), minimun, maximun);
        }
    }
}
