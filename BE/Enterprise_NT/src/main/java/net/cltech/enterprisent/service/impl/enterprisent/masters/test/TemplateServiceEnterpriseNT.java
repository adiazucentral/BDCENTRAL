package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.test.TemplateDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.GeneralTemplateOption;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.service.interfaces.masters.test.TemplateService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a microorganismo
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 31/07/2017
 * @see Creación
 */
@Service
public class TemplateServiceEnterpriseNT implements TemplateService
{

    @Autowired
    private TemplateDao dao;

    @Autowired
    private TrackingService trackingService;

    /**
     * Valida que se encuentren los campos requeridos
     *
     * @param validate entidad a validar
     *
     * @return lista de campos que son requeridos y no estan establecidos
     */
    private List<String> validateFields(List<OptionTemplate> templates) throws Exception
    {

        List<String> errors = new ArrayList<>();
        if (!templates.isEmpty())
        {
            if (templates.get(0).getIdTest() == null)
            {
                errors.add("0|testId");
            }
            int position = 0;
            for (OptionTemplate template : templates)
            {
                if (template.getResults().isEmpty())
                {
                    errors.add("0|results|" + position);
                }
                position++;
            }
        }
        return errors;
    }

    @Override
    public GeneralTemplateOption getById(int id) throws Exception
    {
        GeneralTemplateOption general = new GeneralTemplateOption();
        general.setComment(dao.getsTestCommentForTemplates(id));
        List<OptionTemplate> list = dao.list(id);
        List<OptionTemplate> groupedList = new ArrayList<>();
        list.forEach((optionTemplate) ->
        {
            if (!groupedList.contains(optionTemplate))
            {
                groupedList.add(optionTemplate);
            } else
            {
                groupedList.get(groupedList.indexOf(optionTemplate)).getResults().addAll(optionTemplate.getResults());
            }
        });
        
        general.setOptionTemplates(groupedList);
        return general;
    }

    @Override
    public void create(GeneralTemplateOption general) throws Exception
    {
        List<String> errors = validateFields(general.getOptionTemplates());
        if (errors.isEmpty())
        {
            Integer id = general.getOptionTemplates().get(0).getIdTest();
            List<OptionTemplate> listPrevious = getById(id).getOptionTemplates();
            dao.deleteTemplateItems(id);
            dao.deleteTemplate(id);
            
            if(general.getComment() != null && !general.getComment().isEmpty())
            {
                dao.updateTestCommentForTemplates(general.getComment(), id);
            }
            
            int sort = 0;
            for (OptionTemplate template : general.getOptionTemplates())
            {
                template.setSort(sort);
                template.setId(dao.createTemplate(template));
                dao.createAllItems(template);
                sort++;
            }
            if (sort > 1)
            {
                trackingService.registerConfigurationTracking(listPrevious, general.getOptionTemplates(), OptionTemplate.class);
            }
        } else
        {
            EnterpriseNTException ex = new EnterpriseNTException("");
            ex.setErrorFields(errors);
            throw ex;
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        dao.deleteTemplateItems(id);
        dao.deleteTemplate(id);
    }

}
