package net.cltech.enterprisent.service.impl.enterprisent.operation.pathology;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.common.TrackingPathologyDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.CaseDao;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.SamplePathologyDao;
import net.cltech.enterprisent.domain.operation.pathology.SamplePathology;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.operation.pathology.CaseSearch;
import net.cltech.enterprisent.domain.operation.pathology.Case;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.CaseService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.SamplePathologyService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementa los servicios de informacion de los casos de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 23/02/2020
 * @see Creaciòn
 */
@Service
public class CaseServiceEnterpriseNT implements CaseService
{
    @Autowired
    private CaseDao dao;
    @Autowired
    private SamplePathologyDao samplePathologyDao;
    @Autowired
    private ConfigurationService configurationServices;
    @Autowired
    private ToolsDao toolsDao;
    @Autowired
    private TrackingPathologyDao trackingPathologyDao;
    @Autowired
    private SamplePathologyService samplePathologyService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public Case get(Integer id, Integer studyType, Long number, Long order) throws Exception
    {
        Case casePat = dao.get(id, studyType, number, order);
        if( casePat != null ) {
            casePat.setUserCreated(trackingPathologyDao.get(casePat.getUserCreated().getId()));
            casePat.setUserUpdated(trackingPathologyDao.get(casePat.getUserUpdated().getId()));
            casePat.setPathologist(trackingPathologyDao.getUser(casePat.getPathologist().getId()));
            casePat.setSpecimens(samplePathologyService.getByCase(casePat.getId()));
            dao.getDataPatient(casePat, casePat.getOrder().getNumberOrder());
        }
        return casePat;
    }
      
    @Transactional(transactionManager = "transactionManager", isolation = Isolation.READ_COMMITTED, rollbackFor =
    {
        EnterpriseNTException.class, Exception.class
    })
    @Override
    public synchronized Case create(Case casePat) throws Exception
    {
        List<String> errors = validateFields(casePat, false);
        if (errors.isEmpty())
        {
            try
            {
                boolean autonumeric = false;
                int caseDigits = Integer.parseInt(configurationServices.get("DigitosCaso").getValue().trim());
                String typeCase = configurationServices.get("TipoNumeroCaso").getValue().trim();
                errors = new ArrayList<>(0);
                boolean error = false;

                if(casePat.getNumberCase() == null || casePat.getNumberCase() == 0) {
                    autonumeric = true;
                } 
                else if(typeCase.equals("Diario")) {
                    if(String.valueOf(casePat.getNumberCase()).length() < (8 + caseDigits)) {
                        error = true;
                    }
                }
                else if(typeCase.equals("Mensual")) {
                    if(String.valueOf(casePat.getNumberCase()).length() < (6 + caseDigits)) {
                        error = true;
                    }
                }
                else if(typeCase.equals("Anual")) {
                    if(String.valueOf(casePat.getNumberCase()).length() < (4 + caseDigits)) {
                        error = true;
                    }
                }
                if(error) {
                    //El numero de caso enviado no es correcto
                    errors.add("1|case " + casePat.getNumberCase() + " not valid");
                    throw new EnterpriseNTException(errors);   
                }

                Case caseCreated = null;
                try
                {
                    //Si es autonumerico invoca el metodo para genera el autonumerico
                    if (autonumeric)
                    {
                        casePat.setNumberCase(getAutonumericNumber(casePat));
                    }
                    caseCreated = dao.create(casePat);
                }
                catch (DuplicateKeyException ex)
                {
                    String err;
                    err = "1|case duplicate - " + casePat.getNumberCase();
                    throw new EnterpriseNTException(Arrays.asList(err));
                }

                saveSpecimens(casePat);
                
                return caseCreated;

            }
            catch (EnterpriseNTException exeptionNT)
            {
                if (exeptionNT.getErrorFields().isEmpty())
                {
                    throw new EnterpriseNTException(Arrays.asList("Check the log for more information about the error that was just generated"));
                }
                else
                {
                    throw new EnterpriseNTException(exeptionNT.getErrorFields());
                }
            }
            catch (SQLException sQLException)
            {
                String addMessagge = casePat.getNumberCase()!= null ? casePat.getNumberCase().toString() + " create case excepcion - " : "";
                throw new SQLException(addMessagge + sQLException);
            }
            catch (Exception exeption)
            {
                String addMessagge = casePat.getNumberCase() != null ? casePat.getNumberCase().toString() + " create case excepcion - " : "";
                throw new Exception(addMessagge + exeption);
            }
        }
        else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }
    
    @Transactional(transactionManager = "transactionManager", isolation = Isolation.READ_COMMITTED)
    @Override
    public Case update(Case casePat) throws Exception
    {
        List<String> errors = validateFields(casePat, true);
        if (errors.isEmpty())
        {
            AuthorizedUser session = JWT.decode(request);
            casePat.setUserUpdated(session);
            casePat = dao.update(casePat);
            saveSpecimens(casePat);
            return casePat;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    private long getAutonumericNumber(Case casePat) throws Exception
    {
        int nextValue = toolsDao.nextValPathology(Constants.SEQUENCE_PATHOLOGY + casePat.getStudyType().getId());

        if (nextValue != -1 && nextValue != -2)
        {
            int caseDigits = Integer.parseInt(configurationServices.get("DigitosCaso").getValue().trim());
            String typeCase = configurationServices.get("TipoNumeroCaso").getValue().trim();
            String date = casePat.getCreatedDateShort().toString();
            if(typeCase.equals("Mensual")) {
                date = date.substring(0, 6);
            }
            else if(typeCase.equals("Anual")) {
                date = date.substring(0, 4);
            }
            return Tools.getCompleteCaseNumber(date, caseDigits, nextValue);
        }
        else if (nextValue == -2)
        {
            //No se tiene numeracion asignada
            List<String> errors = new ArrayList<>(0);
            errors.add("5|The numeration limit of the case was passed.");
            throw new EnterpriseNTException(errors);
        }
        else
        {
            //No se tiene numeracion asignada
            List<String> errors = new ArrayList<>(0);
            errors.add("4|Case numbering has not found.");
            throw new EnterpriseNTException(errors);
        }
    }
    
    private void saveSpecimens(Case casePat) throws Exception
    {
        List<Specimen> samples = samplePathologyService.getByCase(casePat.getId());
        if(samples.size() > 0) {
            String idssamples = samples.stream().map(sample -> sample.getId().toString()).collect(Collectors.joining(","));
            samplePathologyDao.deleteSampleStudies(idssamples);
            samplePathologyDao.deleteContentSamples(idssamples);
        }
        samplePathologyDao.saveSpecimens(casePat.getId(), casePat.getSpecimens());
    }
        
    @Override
    public List<CaseSearch> getByEntryDate(int date, int branch) throws Exception
    {
        List<CaseSearch> cases = dao.getByEntryDate(date, branch);
        cases.forEach( casePat -> {
            try
            {
                dao.getDataOrder(casePat, casePat.getOrderNumber());
            } catch (Exception e)
            {
            }
        });
        return cases;
    }
    
    @Override
    public void changeStatus(Case casePat) throws Exception
    {
        if(casePat.getId() != null && casePat.getStatus() != null) {
            AuthorizedUser session = JWT.decode(request);
            casePat.setUserUpdated(session);
            dao.changeStatus(casePat);
        }
    }
    
    /**
    * Valida los campos enviados de un caso
    *
    * @param validate entidad a validar
    *
    * @return lista de campos que son requeridos y no estan establecidos
    */
    private List<String> validateFields(Case validate, boolean isEdit) throws Exception
    {
        List<String> errors = new ArrayList<>();
        
        if (isEdit)
        {
            if (validate.getId() == null)
            {
                errors.add("0|Id");

            } else
            {
                if (get(validate.getId(), null, null, null) == null)
                {
                    errors.add("2|id");//No existe id
                }
            }
        } else {
            if(validate.getUserCreated().getId() == null || validate.getUserCreated().getId().equals(0)) {
                errors.add("0|userCreated");
            }
        }

        if (validate.getStudyType().getId() == null || validate.getStudyType().getId() == 0)
        {
            errors.add("0|studyType");
        }

        if (validate.getCreatedDateShort() == null || validate.getCreatedDateShort() == 0)
        {
            errors.add("0|createdDateShort");
        }

        if (validate.getOrder().getNumberOrder() == null || validate.getOrder().getNumberOrder() == 0)
        {
            errors.add("0|order");
        }

        if (validate.getBranch().getId() == null || validate.getBranch().getId() == 0)
        {
            errors.add("0|branch");
        }

        errors.addAll(validateSpecimen(validate));
        
        return errors;
    }
    
    /**
    * Valida los campos enviados de los especimenes
    *
    * @param validate entidad a validar
    *
    * @return lista de campos que son requeridos y no estan establecidos
    */
    private List<String> validateSpecimen(Case validate) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (validate.getSpecimens().size() > 0)
        {
            int row = 0;
            for (Specimen specimen : validate.getSpecimens())
            {
                if(specimen.getId() == null || specimen.getId() == 0) {
                    errors.add("0|id|" + row);
                }
                
                if(specimen.getOrgan().getId() == null || specimen.getOrgan().getId() == 0) {
                    errors.add("0|organ|" + row);
                }
               
                if(specimen.getSamples().isEmpty()) {
                    errors.add("0|samples|" + row);
                }
                
                if(specimen.getStudies().isEmpty()) {
                    errors.add("0|studies|" + row);
                }
                
                errors.addAll(validateSamples(specimen, row));
                
                row++;
            }
        } else
        {
            errors.add("0|specimens");
        }
        return errors;
    }
    
    /**
    * Valida los campos enviados de las muestras
    *
    * @param validate entidad a validar
    *
    * @return lista de campos que son requeridos y no estan establecidos
    */
    private List<String> validateSamples(Specimen validate, int row) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (validate.getSamples().size() > 0)
        {
            int r = 0;
            for (SamplePathology sample : validate.getSamples())
            {
                if(sample.getQuantity() == 0) {
                    errors.add("0|quantity|" + row + "|" + r);
                }
                if(sample.getContainer().getId() == null || sample.getContainer().getId() == 0) {
                    errors.add("0|container|" + row + "|" + r);
                }
                if(sample.getFixative().getId() == null || sample.getFixative().getId() == 0) {
                    errors.add("0|fixative|" + row + "|" + r);
                }
                r++;
            }
        } else
        {
            errors.add("0|samples|" + row);
        }
        return errors;
    }
    
    @Override
    public List<CaseSearch> getFilterCases(FilterPathology filter) throws Exception
    {
        List<CaseSearch> cases = dao.getFilterCases(filter, JWT.decode(request).getBranch());
        cases.forEach( casePat -> {
            try
            {
                dao.getDataOrder(casePat, casePat.getOrderNumber());
            } catch (Exception e)
            {
            }
        });
        return cases;
    }
}
