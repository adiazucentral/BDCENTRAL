package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ListDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.DiagnosticDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.test.Diagnostic;
import net.cltech.enterprisent.domain.masters.test.DiagnosticByTest;
import net.cltech.enterprisent.domain.masters.test.ListDiagnosticByTest;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.masters.test.TestByDiagnostic;
import net.cltech.enterprisent.service.interfaces.masters.test.DiagnosticService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Diagnostico para
 * Enterprise NT
 *
 * @version 1.0.0
 * @author enavas
 * @see 21/06/2017
 * @see Creaciòn
 */
@Service
public class DiagnosticServiceEnterpriseNT implements DiagnosticService
{

    @Autowired
    private DiagnosticDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ListDao listDao;
    @Autowired
    private DiagnosticDao DiagnosticDao;

    @Override
    public List<Diagnostic> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public List<Diagnostic> get(Integer id, String code, String name, Integer type, Boolean state) throws Exception
    {
        return dao.get(id, code, name, type, state);
    }

    @Override
    public int createAll(List<Diagnostic> diagnostics) throws Exception
    {
        long time_start, time_end;
        time_start = System.currentTimeMillis();

        List<Diagnostic> validate = validateFieldsImport(diagnostics);

        time_end = System.currentTimeMillis();
        System.err.println("validando en " + (time_end - time_start) + " milliseconds");
        if (validate.size() > 0)
        {
            time_start = System.currentTimeMillis();
            //QUITAMOS LOS REPETIDOS  Y VALIDAMOS QUE NO HAYA CODIGOS REPETIDOS EN EL IMPORTADOS
            List<Diagnostic> diagnosticsval = validate.stream()
                    .distinct()
                    .collect(Collectors.toList());
            int registers = dao.createAll(diagnosticsval);
            time_end = System.currentTimeMillis();
            System.err.println("Insertando en " + (time_end - time_start) + " milliseconds");
            return registers;
        } else
        {
            return 0;
        }
    }

    @Override
    public Diagnostic create(Diagnostic diagnostic) throws Exception
    {
        List<String> errors = validateFields(false, diagnostic);
        if (errors.isEmpty())
        {
            trackingService.registerConfigurationTracking(null, diagnostic, Diagnostic.class);
            return dao.create(diagnostic);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Diagnostic update(Diagnostic diagnostic) throws Exception
    {
        List<String> errors = validateFields(true, diagnostic);
        if (errors.isEmpty())
        {
            Diagnostic diagnosticval = dao.get(diagnostic.getId(), null, null, null, null).get(0);
            trackingService.registerConfigurationTracking(diagnosticval, diagnostic, Diagnostic.class);
            return dao.update(diagnostic);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public int assignToTest(DiagnosticByTest assign) throws Exception
    {
        dao.deleteTestDiagnosis(assign.getIdTest());
        trackingService.registerConfigurationTracking(null, assign, DiagnosticByTest.class);
        return dao.insertTestDiagnosis(assign.getDiagnostics(), assign.getIdTest());
    }

    @Override
    public int assignToTest(TestByDiagnostic assign) throws Exception
    {
        dao.deleteTestByDiagnostic(assign.getIdDiagnostic());
        trackingService.registerConfigurationTracking(null, assign, TestByDiagnostic.class);
        return dao.insertDiagnosticTest(assign.getTests(), assign.getIdDiagnostic());
    }

    @Override
    public List<Diagnostic> listByTest(int idTest) throws Exception
    {
        return dao.listTestDiagnosis(idTest);
    }

    /**
     * Validación de campos para crear/actualizar diagnostico
     *
     * @param isEdit es actualizacion
     * @param diagnostic Informacion del diagnostico
     * @return Lista de errores, lista vacia si no hay errores
     * @throws Exception Error en la validacion
     */
    private List<String> validateFields(boolean isEdit, Diagnostic diagnostic) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();

        //si Esta editando
        if (isEdit)
        {
            if (diagnostic.getId() == null)
            {
                errors.add("0|id");
                return errors;
            } else
            {
                if (dao.get(diagnostic.getId(), null, null, null, null).isEmpty())
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        // validamos campos obligatorios , y que no pueden repetirse
        if (diagnostic.getCode() != null && !diagnostic.getCode().isEmpty())
        {
            List<Diagnostic> list = dao.get(null, diagnostic.getCode(), null, null, null);
            Diagnostic diagnosticrival = null;
            if (!list.isEmpty())
            {
                diagnosticrival = list.get(0);
                if (isEdit)
                {
                    //comparamos que corresponda al mismo ID
                    if (!Objects.equals(diagnostic.getId(), diagnosticrival.getId()))
                    {
                        errors.add("1|Code");
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

        if (diagnostic.getName() == null && diagnostic.getName().isEmpty())
        {
            errors.add("0|Name");
        }

        if (diagnostic.getType().getId() != null && diagnostic.getType().getId() != 0)
        {
            List<Item> items = new ArrayList<>(CollectionUtils.filter(listDao.list(38), (Object o) -> ((Item) o).getId() == diagnostic.getType().getId()));
            if (items.isEmpty())
            {
                errors.add("3|type");
            }
        } else
        {
            errors.add("0|type");
        }
        return errors;
    }

    /**
     * Validacion para importar diagnosticos
     *
     * @param diagnosis Lista de diagnosticos
     * @return Lista de diagnosticos importados
     * @throws Exception Error en la validacion
     */
    private List<Diagnostic> validateFieldsImport(List<Diagnostic> diagnosis) throws Exception
    {
        List<String> errors = new ArrayList<>();
        //lista de diagnosticos
        List<Diagnostic> saved = dao.list();
        //lista de tipo de diagnosticos
        List<Item> items = listDao.list(38);

        List<Diagnostic> valid = new ArrayList<>();

        int linea = 1;
        for (Diagnostic diagnostic : diagnosis)
        {
            //validamos el codigo
            if (!alreadyExistsCode(diagnostic, saved))
            {

                //VALIDAMOS EL CODIGO DE TIPO DE DIAGNOSTICO
                if (!alreadyExistsType(diagnostic, items))
                {
                    errors.add("1|type|" + linea);
                }

                if (diagnostic.getName() == null || diagnostic.getName().isEmpty())
                {
                    errors.add("0|name|" + linea);
                }

                valid.add(diagnostic);
            }

            linea++;
        }

        if (!errors.isEmpty())
        {

            throw new EnterpriseNTException(errors);
        }

        return valid;
    }

    /**
     * Metodo para verificar si el codigo del diagnostico existe en una lista
     *
     * @param diagnostic
     * @param searchList lista de busqueda
     *
     * @return si existe en la lista
     */
    public boolean alreadyExistsCode(Diagnostic diagnostic, List<Diagnostic> searchList)
    {
        return searchList.stream()
                .anyMatch(m -> m.getCode().trim().equalsIgnoreCase(diagnostic.getCode().trim()));
    }

    /**
     * Metodo para verificar si el tipo de diagnostico del diagnostico existe en
     * una lista
     *
     * @param diagnostic
     * @param searchList lista de busqueda
     *
     * @return si existe en la lista
     */
    public boolean alreadyExistsType(Diagnostic diagnostic, List<Item> searchList)
    {
        return searchList.stream()
                .anyMatch(m -> Objects.equals(m.getId(), diagnostic.getType().getId()));
    }

    @Override
    public List<Test> listTests(Integer id) throws Exception
    {
        return dao.getListTest(id);
    }

    @Override
    public List<Test> listTestWithDiagnostic(int id) throws Exception
    {
        return dao.listDiagnosticTest(id);
    }

    @Override
    public List<DiagnosticByTest> listTest(ListDiagnosticByTest listDiagnosticByTest) throws Exception
    {
        List<DiagnosticByTest> listElements = new ArrayList<>();
        if (listDiagnosticByTest.getTests().size() > 0)
        {
            List<Diagnostic> listDiagnostic = listDiagnosticByTest.getDiagnostics();
            String idsTest = listDiagnosticByTest.getTests().stream().map(result -> result.getId().toString()).collect(Collectors.joining(","));
            String idsDiagnostic = listDiagnosticByTest.getDiagnostics().stream().map(result -> result.getId().toString()).collect(Collectors.joining(","));
            List<Test> list = dao.listTestByDiagnostic(idsTest, idsDiagnostic);
            for (Test test : list)
            {
                DiagnosticByTest newDiagnosticByTest = listElements.stream().filter(result -> result.getIdTest() == test.getId()).findAny().orElse(null);
                if (newDiagnosticByTest == null)
                {
                    newDiagnosticByTest = new DiagnosticByTest();
                    newDiagnosticByTest.setIdTest(test.getId());
                    newDiagnosticByTest.setDiagnostics(new ArrayList<>());
                    listElements.add(newDiagnosticByTest);
                }
                if (listDiagnostic.size() > 0)
                {
                    Diagnostic aux2 = listDiagnostic.stream().filter(result -> Objects.equals(result.getId(), test.getDiagnostic())).findAny().orElse(null);
                    if (aux2 != null)
                    {
                        newDiagnosticByTest.getDiagnostics().add(aux2);
                    }
                }
            }
        }
        return listElements;
    }

    @Override
    public List<Diagnostic> ListDiagnostics(long order) throws Exception
    {
        return DiagnosticDao.ListDiagnostics(order);
    }

}
