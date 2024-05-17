package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.DocumentTypeDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DocumentTypeService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Tipo de Documento para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 29/08/2017
 * @see Creaciòn
 */
@Service
public class DocumentTypeServiceEnterpriseNT implements DocumentTypeService
{

    @Autowired
    private DocumentTypeDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<DocumentType> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public DocumentType create(DocumentType documentType) throws Exception
    {
        List<String> errors = validateFields(false, documentType);
        if (errors.isEmpty())
        {
            DocumentType created = dao.create(documentType);
            trackingService.registerConfigurationTracking(null, created, DocumentType.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public DocumentType get(Integer id, String abrr, String name) throws Exception
    {
        return dao.get(id, abrr, name);
    }

    @Override
    public DocumentType update(DocumentType documentType) throws Exception
    {
        List<String> errors = validateFields(true, documentType);
        if (errors.isEmpty())
        {
            DocumentType documentTypeC = dao.get(documentType.getId(), null, null);
            DocumentType modifited = dao.update(documentType);
            trackingService.registerConfigurationTracking(documentTypeC, modifited, DocumentType.class);
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
    public List<DocumentType> list(boolean state) throws Exception
    {
        List<DocumentType> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((DocumentType) o).isState() == state));
        return filter;
    }

    /**
     * Validacion de campos
     *
     * @param isEdit       es edicion
     * @param documentType Tipo de Documento
     *
     * @return Lista de errores
     *         0 -> Datos vacios
     *         1 -> Esta duplicado
     *         2 -> Id no existe solo aplica para modificar
     * @throws Exception Errores
     */
    private List<String> validateFields(boolean isEdit, DocumentType documentType) throws Exception
    {

        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (documentType.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(documentType.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (documentType.getAbbr() != null)
        {
            DocumentType documentTypeC = dao.get(null, documentType.getAbbr(), null);
            if (documentTypeC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(documentType.getId(), documentTypeC.getId()))
                    {
                        errors.add("1|abbr");
                    }
                } else
                {
                    errors.add("1|abbr");
                }
            }
        } else
        {
            errors.add("0|abbr");
        }

        if (documentType.getName() != null && !documentType.getName().isEmpty())
        {
            DocumentType documentTypeC = dao.get(null, null, documentType.getName());
            if (documentTypeC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(documentType.getId(), documentTypeC.getId()))
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

        if (documentType.getUser().getId() == null || documentType.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }
}
