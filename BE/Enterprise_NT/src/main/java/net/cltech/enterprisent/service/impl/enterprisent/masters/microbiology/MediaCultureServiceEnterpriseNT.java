/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.dao.interfaces.masters.microbiology.MediaCultureDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCulture;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCultureTest;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.service.interfaces.masters.microbiology.MediaCultureService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro de Medio de cultivo para
 * EnterpriseNT
 *
 * @version 1.0.0
 * @author enavas
 * @see 11/08/2017
 * @see Creaciòn
 */
@Service
public class MediaCultureServiceEnterpriseNT implements MediaCultureService
{

    @Autowired
    private MediaCultureDao dao;
    @Autowired
    private TrackingService trackingService;

    @Override
    public List<MediaCulture> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public MediaCulture create(MediaCulture mediaCulture) throws Exception
    {
        List<String> errors = validateFields(false, mediaCulture);
        if (errors.isEmpty())
        {
            MediaCulture created = dao.create(mediaCulture);
            trackingService.registerConfigurationTracking(null, created, MediaCulture.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public MediaCulture get(Integer id, String code, String name) throws Exception
    {
        return dao.get(id, code, name);
    }

    @Override
    public MediaCulture update(MediaCulture mediaCulture) throws Exception
    {
        List<String> errors = validateFields(true, mediaCulture);
        if (errors.isEmpty())
        {
            MediaCulture mediaCultureC = dao.get(mediaCulture.getId(), null, null);
            MediaCulture modifited = dao.update(mediaCulture);
            trackingService.registerConfigurationTracking(mediaCultureC, modifited, MediaCulture.class);
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<MediaCulture> list(boolean state) throws Exception
    {
        List<MediaCulture> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((MediaCulture) o).isState() == state));
        return filter;
    }

    /**
     * Valida campos obligatorios para el medio de cultivo
     *
     *
     *
     * @return lista de errores encontrados
     * @throws Exception Error en el servicio
     */
    private List<String> validateFields(boolean isEdit, MediaCulture mediaCulture) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (mediaCulture.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(mediaCulture.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        if (mediaCulture.getCode() != null && !mediaCulture.getCode().isEmpty())
        {
            MediaCulture mediaCultureC = dao.get(null, mediaCulture.getCode(), null);
            if (mediaCultureC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(mediaCulture.getId(), mediaCultureC.getId()))
                    {
                        errors.add("1|code");
                    }
                } else
                {
                    errors.add("1|code");
                }
            }
        } else
        {
            errors.add("0|code");
        }

        if (mediaCulture.getName() != null && !mediaCulture.getName().isEmpty())
        {
            MediaCulture mediaCultureC = dao.get(null, null, mediaCulture.getName());
            if (mediaCultureC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(mediaCulture.getId(), mediaCultureC.getId()))
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

        return errors;
    }

    @Override
    public MediaCultureTest listMediaCulture(Integer idTest) throws Exception
    {
        return dao.listMediacultureTest(idTest);
    }

    @Override
    public int createMediaCultureTest(MediaCultureTest mediaCultureTest) throws Exception
    {
        trackingService.registerConfigurationTracking(listMediaCulture(mediaCultureTest.getTestId()), mediaCultureTest, MediaCultureTest.class);
        return dao.insertMediaCulture(mediaCultureTest);
    }

    @Override
    public TestBasic getMediaCultureTest(int test) throws Exception
    {
        return dao.getMediaCultureTest(test);
    }

}
