package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.cltech.enterprisent.dao.interfaces.masters.test.ContainerDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.SampleDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.test.Container;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.SampleByService;
import net.cltech.enterprisent.service.interfaces.masters.test.SampleService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro de Muestra para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author enavas
 * @see 28/04/2017
 * @see Creaciòn
 */
@Service
public class SampleServiceEnterpriseNT implements SampleService
{

    @Autowired
    private SampleDao sampleDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ContainerDao containerDao;

    @Override
    public List<Sample> list() throws Exception
    {
        return sampleDao.list();
    }

    @Override
    public List<Sample> list(boolean state) throws Exception
    {
        return new ArrayList<>(CollectionUtils.filter(sampleDao.list(), (Object o) -> ((Sample) o).isState() == state));
    }

    @Override
    public Sample create(Sample sample) throws Exception
    {
        List<String> errors = validateFields(false, sample);
        if (errors.isEmpty())
        {
            trackingService.registerConfigurationTracking(null, sample, Sample.class);
            return sampleDao.create(sample);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Sample> get(Integer id, String name, String code, Integer container, Boolean state) throws Exception
    {
        return sampleDao.get(id, name, code, container, state);
    }

    @Override
    public List<Sample> typeFilter(String types) throws Exception
    {
        List<String> listTypes = Arrays.asList(types.split(","));
        return sampleDao.list().stream()
                .filter(sample -> sample.isState())
                .filter(sample -> hasTypes(listTypes, sample))
                .collect(Collectors.toList());
    }

    @Override
    public Sample update(Sample sample) throws Exception
    {
        List<String> errors = validateFields(true, sample);
        if (errors.isEmpty())
        {
            Sample sampleval = sampleDao.get(sample.getId(), null, null, null, null).get(0);
            trackingService.registerConfigurationTracking(sampleval, sample, Sample.class);
            return sampleDao.update(sample);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {

    }

    @Override
    public List<SampleByService> listSampleByService(int idService) throws Exception
    {
        return sampleDao.listSampleByService(idService);
    }

    @Override
    public SampleByService insertSamplesByService(SampleByService sample) throws Exception
    {
        List<String> errors = validateSampleByService(sample);
        if (errors.isEmpty())
        {
            SampleByService previous = listSampleByService(sample.getService().getId()).stream().filter((SampleByService s) -> Objects.equals(s.getSample().getId(), sample.getSample().getId())).findFirst().orElse(null);
            previous.getSample().setName("");
            SampleByService resp = sampleDao.insertSamplesByService(sample);
            trackingService.registerConfigurationTracking(previous, resp, SampleByService.class);
            return resp;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Sample> subSamples(int sampleId) throws Exception
    {
        return sampleDao.listSubSample(sampleId).stream()
                .filter(sample -> hasTypes(Arrays.asList("3"), sample))//Microbiología
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Sample> listSubSampleSelect(int sampleId) throws Exception
    {
        return sampleDao.listSubSampleSelect(sampleId).stream()
                .filter(sample -> hasTypes(Arrays.asList("3"), sample))//Microbiología
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Sample> samplesMicrobiology() throws Exception
    {
        return sampleDao.list().stream()
                .filter(sample -> hasTypes(Arrays.asList("3"), sample))//Microbiología
                .collect(Collectors.toList());
    }

    @Override
    public int assignSubSamples(Sample sample) throws Exception
    {
        List<String> errors = validateSubSample(sample);
        if (errors.isEmpty())
        {
            Sample before = new Sample(sample.getId());
            before.setSubSamples(subSamples(sample.getId()).stream()
                    .filter(selected -> selected.isSelected())
                    .collect(Collectors.toList()));

            trackingService.registerConfigurationTracking(before, sample, Sample.class);
            sampleDao.deleteSubSamples(sample.getId());
            return sampleDao.insertSubSample(sample);
        } else
        {
            throw new EnterpriseNTException(errors);
        }

    }

    private List<String> validateFields(boolean isEdit, Sample sample) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();

        //si Esta editando
        if (isEdit)
        {
            if (sample.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (sampleDao.get(sample.getId(), null, null, null, null).isEmpty())
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        // validamos campos obligatorios , y que no pueden repetirse
        if (sample.getName() != null && !sample.getName().isEmpty())
        {
            List<Sample> list = sampleDao.get(null, sample.getName(), null, null, null);
            Sample samplerival = null;

            if (!list.isEmpty())
            {
                samplerival = list.get(0);
                if (isEdit)
                {
                    //comparamos que corresponda al mismo ID
                    if (!Objects.equals(sample.getId(), samplerival.getId()))
                    {
                        errors.add("1|Name");
                    }
                } else
                {
                    //guardar
                    errors.add("1|Name");
                }
            }

        } else
        {
            errors.add("0|Name");
        }

        if (sample.getCodesample() != null && !sample.getCodesample().isEmpty())
        {

            List<Sample> list = sampleDao.get(null, null, sample.getCodesample(), null, null);
            Sample samplerival = null;

            if (!list.isEmpty())
            {
                samplerival = list.get(0);
                if (isEdit)
                {
                    //comparamos que corresponda al mismo ID
                    if (!Objects.equals(sample.getId(), samplerival.getId()))
                    {
                        errors.add("1|Code|id");
                    }
                } else
                {
                    //guardar
                    errors.add("1|Code");
                }
            }

        } else
        {
            errors.add("0|Code");
        }

        if (sample.getContainer() != null)
        {
            if (sample.getContainer().getId() == null || sample.getContainer().getId() == 0)
            {
                errors.add("0|Id Container");
            } else
            {
                Container contanerival = containerDao.get(sample.getContainer().getId(), null, null);

                if (contanerival == null)
                {
                    errors.add("2|Id Container");
                }
            }
        } else
        {
            errors.add("0|Container");
        }

        if (sample.getCanstiker() == null)
        {
            errors.add("null|stiker");
        }

        if (sample.getDaysstored() == null)
        {
            errors.add("null|Days stored");
        }

        if (sample.getLaboratorytype() != null && !sample.getLaboratorytype().isEmpty())
        {
            for (String lab : sample.getLaboratorytype().split(","))
            {
                // en lab80 codigo es 2 hasta 5
                if (Integer.parseInt(lab) != 2 && Integer.parseInt(lab) != 3 && Integer.parseInt(lab) != 4 && Integer.parseInt(lab) != 5)
                {
                    errors.add("3|Laboratory type");
                    break;
                }
            }
        } else
        {
            errors.add("0|Laboratory type");
        }

        return errors;
    }

    private List<String> validateSampleByService(SampleByService sample)
    {
        List<String> errors = new ArrayList<>();
        if (sample.getSample().getId() == null || sample.getSample().getId() == 0)
        {
            errors.add("0|sample");
        }

        if (sample.getService().getId() == null || sample.getService().getId() == 0)
        {
            errors.add("0|service");
        }

        if (sample.getExpectedTime() == null)
        {
            errors.add("0|expectedTime");
        }

        return errors;
    }

    /**
     * Validación de campos para la asignación de submuestras
     *
     * @param sample bean con informacion de la relación
     *
     * @return lista con errores
     * @throws Exception
     */
    private List<String> validateSubSample(Sample sample) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (sample.getId() == null)
        {
            errors.add("0|id");
        }
        return errors;
    }

    /**
     * Verifica si la muestra pertenece a los tipos enviados
     *
     * @param filterTypes tipos de muestra a buscar
     * @param sample muestra que verifica
     *
     * @return si la muestra es de los tipos enviados
     */
    private boolean hasTypes(List<String> filterTypes, Sample sample)
    {
        return Stream.of(sample.getLaboratorytype().split(","))
                .anyMatch(type -> filterTypes.contains(type));
    }
    
    @Override
    public Sample getTypeLaboratoryBySample(int sample) throws Exception
    {
        return sampleDao.getTypeLaboratoryBySample(sample);
    }

}
