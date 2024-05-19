package net.cltech.enterprisent.service.impl.enterprisent.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationConsultaWebHisDao;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.BranchDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.LaboratoryDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.TestDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.homebound.TestHomeBound;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.PypDemographic;
import net.cltech.enterprisent.domain.masters.test.AutomaticTest;
import net.cltech.enterprisent.domain.masters.test.Concurrence;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.Profile;
import net.cltech.enterprisent.domain.masters.test.ReferenceValue;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.TestByLaboratory;
import net.cltech.enterprisent.domain.masters.test.TestByService;
import net.cltech.enterprisent.domain.masters.test.TestInformation;
import net.cltech.enterprisent.service.interfaces.common.ListService;
import net.cltech.enterprisent.service.interfaces.masters.test.TestService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.JWT;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Pruebas para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 22/04/2017
 * @see Creaciòn
 */
@Service
public class TestServiceEnterpriseNT implements TestService
{

    @Autowired
    private TestDao testDao;
    @Autowired
    private BranchDao branchDao;
    @Autowired
    private LaboratoryDao labDao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ListService listService;
    @Autowired
    private HttpServletRequest request;
    
    @Override
    public List<TestBasic> list(int type, Boolean state, Integer area) throws Exception
    {
        return testDao.listAll().stream()
                .filter(filterType(type))
                .filter(filter -> state == null || filter.isState() == state)
                .filter(filter -> area == null || area.equals(filter.getArea().getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TestBasic> listBranch(int type, Boolean state, Integer area) throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        return testDao.listAll(idbranch).stream()
                .filter(filterType(type))
                .filter(filter -> state == null || filter.isState() == state)
                .filter(filter -> area == null || area.equals(filter.getArea().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TestBasic> list(Integer id) throws Exception
    {
        return testDao.list(id);
    }

    @Override
    public List<TestBasic> listByResultType(int type, Boolean state, Integer area, Short processingBy) throws Exception
    {
        return testDao.list().stream()
                .filter(filter -> filter.getTestType() == 0)
                .filter(filter -> filter.getResultType() == type)
                .filter(filter -> state == null || filter.isState() == state)
                .filter(filter -> area == null || area.equals(filter.getArea().getId()))
                .filter(filter -> processingBy == null || Objects.equals(filter.getProcessingBy(), processingBy))
                .collect(Collectors.toList());
    }

    @Override
    public Test create(Test test) throws Exception
    {
        List<String> errors = validateFields(false, test);
        if (errors.isEmpty())
        {
            Test created = testDao.create(test);
            trackingService.registerConfigurationTracking(null, created, Test.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Test get(Integer id, String code, String name, String abbr) throws Exception
    {
        return testDao.get(id, code, name, abbr);
    }

    @Override
    public Test update(Test test) throws Exception
    {
        List<String> errors = validateFields(true, test);
        if (errors.isEmpty())
        {
            Test testC = testDao.get(test.getId(), null, null, null);
            testC.setRequirements(testC.getRequirements().stream()
                .filter(filter -> filter.isSelected() == true).collect(Collectors.toList()));
                                   
            Test modifited = testDao.update(test);
            testC.setConcurrences(testDao.getConcurrences(test.getId()).stream()
                .filter(filter -> filter.isSelected() == true).collect(Collectors.toList()));
            
            if ((int) testC.getTestType() == LISEnum.TestType.TEST.getValue())
            {
                trackingService.registerConfigurationTracking(testC, modifited, Test.class);
            } else if ((int) testC.getTestType() == LISEnum.TestType.PROFILE.getValue())
            {
                trackingService.registerConfigurationTracking(testC, modifited, Test.class, "net.cltech.enterprisent.domain.masters.test.Profile");
            } else if ((int) testC.getTestType() == LISEnum.TestType.PACKAGE.getValue())
            {
                trackingService.registerConfigurationTracking(testC, modifited, Test.class, "net.cltech.enterprisent.domain.masters.test.Package");
            }
            return modifited;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void delete(Integer id) throws Exception
    {
        testDao.delete(id);
    }

    @Override
    public List<TestBasic> listConcurrences() throws Exception
    {
        return testDao.listConcurrences();
    }

    @Override
    public List<TestByLaboratory> listTestLaboratory(int idBranch, int idLaboratory) throws Exception
    {
        List<TestByLaboratory> tests = new ArrayList();
        for (TestByLaboratory test : testDao.listTestLaboratory(idBranch))
        {
            TestByLaboratory newTest = new TestByLaboratory(test.getTest());
            newTest.setUser(test.getUser());
            newTest.setLastTransaction(test.getLastTransaction());

            if (!tests.contains(newTest))
            {
                tests.add(newTest);
            }
            newTest = tests.get(tests.indexOf(newTest));

            if (newTest.getUrgency() == null)
            {
                newTest.setUrgency((short) 2);
            }
            if (newTest.getRoutine() == null)
            {
                newTest.setRoutine((short) 2);
            }

            if (newTest.getUrgency() == 2 && test.getGroupType() == 1)
            {
                if (test.getGroupType() == 1 && test.getIdLaboratory() == idLaboratory)
                {
                    newTest.setUrgency((short) 1);
                } else
                {
                    if (test.getGroupType() == 1 && test.getIdLaboratory() != idLaboratory)
                    {
                        newTest.setUrgency((short) 0);
                    } else
                    {
                        newTest.setUrgency((short) 2);
                    }
                }
            }

            if (newTest.getRoutine() == 2 && test.getGroupType() == 2)
            {
                if (test.getGroupType() == 2 && test.getIdLaboratory() == idLaboratory)
                {
                    newTest.setRoutine((short) 1);
                } else
                {
                    if (test.getGroupType() == 2 && test.getIdLaboratory() != idLaboratory)
                    {
                        newTest.setRoutine((short) 0);
                    } else
                    {
                        newTest.setRoutine((short) 2);
                    }
                }
            }
        }
        return tests;
    }

    @Override
    public int insertTestLaboratory(List<TestByLaboratory> tests) throws Exception
    {
        final List<String> errors = new ArrayList<>();
        for (TestByLaboratory test : tests)
        {
            if (test.getTest().getId() == null || test.getTest().getId() == 0)
            {
                errors.add("0|test");
            }

            if (test.getIdBranch() == null || test.getIdBranch() == 0)
            {
                errors.add("0|branch");
            }

            if (test.getIdLaboratory() == null || test.getIdLaboratory() == 0)
            {
                errors.add("0|laboratory");
            }
            if (!errors.isEmpty())
            {
                break;
            }
        }
        if (errors.isEmpty() && !tests.isEmpty())
        {
            //Consulta informacion de la sede
            Branch branch = branchDao.get(tests.get(0).getIdBranch(), null, null, null);
            //Consulta informacion del laboratorio
            Laboratory laboratory = labDao.filterById(tests.get(0).getIdLaboratory());
            boolean insert = false;
            for (TestByLaboratory test : tests)
            {
                TestByLaboratory oldTestLaboratory = null;
                List<TestByLaboratory> oldsTestLaboratory = getTestByBranchByLaboratory(test.getTest().getId(), test.getIdBranch(), test.getIdLaboratory());
                if (!oldsTestLaboratory.isEmpty())
                {
                    oldTestLaboratory = oldsTestLaboratory.get(0);
                }
                if ((oldTestLaboratory == null && !(Objects.equals(test.getUrgency(), 2) && Objects.equals(test.getRoutine(), 2))) || !Objects.equals(test.getUrgency(), oldTestLaboratory.getUrgency()) || !Objects.equals(test.getRoutine(), oldTestLaboratory.getRoutine()))
                {
                    //Consulta informacion del examen
                    Test testT = testDao.get(test.getTest().getId(), null, null, null);
                    //Establece propiedades al nuevo objeto 
                    TestBasic testBasic = new TestBasic();
                    testBasic.setId(testT.getId());
                    testBasic.setCode(testT.getCode());
                    testBasic.setName(testT.getName());
                    test.setNameBranch(branch.getName());
                    test.setNameLaboratory(laboratory.getName());
                    test.setTest(testBasic);
                    //Envia a auditoria
                    trackingService.registerConfigurationTracking(oldTestLaboratory, test, TestByLaboratory.class);
                    insert = true;
                }
            }
            return insert ? testDao.insertTestLaboratory(tests) : 0;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Laboratory getLaboratory(int branch, int test, int groupType) throws Exception
    {
        return testDao.getLaboratory(branch, test, groupType);
    }

    @Override
    public List<Concurrence> getConcurrence(int testType) throws Exception
    {
        return testDao.getConcurrences(testType);
    }

    @Override
    public List<ReferenceValue> getReferenceValues(int test) throws Exception
    {
        return testDao.listReferenceValues(test);
    }

    @Override
    public ReferenceValue insertReferenceValues(ReferenceValue referenceValue) throws Exception
    {
        List<String> errors = validateReferenceValue(referenceValue);
        if (errors.isEmpty())
        {
            ReferenceValue created = testDao.insertReferenceValues(referenceValue);
            trackingService.registerConfigurationTracking(null, created, ReferenceValue.class);
            return created;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public ReferenceValue updateReferenceValue(ReferenceValue referenceValue) throws Exception
    {
        List<String> errors = validateReferenceValue(referenceValue);
        if (errors.isEmpty())
        {
            ReferenceValue old = getReferenceValues(referenceValue.getTest().getId()).stream().filter((ReferenceValue r) -> Objects.equals(r.getId(), referenceValue.getId())).findFirst().orElse(null);
            if (!Objects.equals(old.getGender().getId(), referenceValue.getGender().getId())
                    || !Objects.equals(old.getRace().getId(), referenceValue.getRace().getId())
                    || !Objects.equals(old.getUnitAge(), referenceValue.getUnitAge())
                    || !Objects.equals(old.getAgeMin(), referenceValue.getAgeMin())
                    || !Objects.equals(old.getAgeMax(), referenceValue.getAgeMax())
                    || !Objects.equals(old.getNormalMin(), referenceValue.getNormalMin())
                    || !Objects.equals(old.getNormalMax(), referenceValue.getNormalMax())
                    || !Objects.equals(old.getPanicMin(), referenceValue.getPanicMin())
                    || !Objects.equals(old.getPanicMax(), referenceValue.getPanicMax())
                    || !Objects.equals(old.getReportableMin(), referenceValue.getReportableMin())
                    || !Objects.equals(old.getReportableMax(), referenceValue.getReportableMax())
                    || !Objects.equals(old.getComment(), referenceValue.getComment())
                    || !Objects.equals(old.isCriticalCh(), referenceValue.isCriticalCh())
                    || !Objects.equals(old.isMandatoryNotation(), referenceValue.isMandatoryNotation())
                    || !Objects.equals(old.getAnalizerUserId(), referenceValue.getAnalizerUserId())
                    || !Objects.equals(old.getCommentEnglish(), referenceValue.getCommentEnglish()))
            {
                ReferenceValue updated = testDao.updateReferenceValue(referenceValue);
                trackingService.registerConfigurationTracking(old, updated, ReferenceValue.class);
                return updated;
            }
            return referenceValue;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public void inactivateReferenceValues(int id) throws Exception
    {
        testDao.inactivateReferenceValues(id);
    }

    private List<String> validateReferenceValue(ReferenceValue referenceValue)
    {
        List<String> errors = new ArrayList<>();
        if (referenceValue.getTest().getId() == null || referenceValue.getTest().getId() == 0)
        {
            errors.add("0|test");
        }

        if (referenceValue.getUser().getId() == null || referenceValue.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        if (referenceValue.getGender().getId() != null && referenceValue.getGender().getId() != 0)
        {
            if (referenceValue.getGender().getId() != 7 && referenceValue.getGender().getId() != 8 && referenceValue.getGender().getId() != 9 && referenceValue.getGender().getId() != 42)
            {
                errors.add("3|gender");
            }
        } else
        {
            errors.add("0|gender");
        }

        return errors;
    }

    /**
     * Valida datos para actualizar valores de delta check
     *
     * @param test Bean a validar
     *
     * @return Lista co errores encontrados
     * @throws Exception
     */
    private List<String> validateDeltaFields(TestBasic test) throws Exception
    {
        List<String> errors = new ArrayList<>();
        if (test.getId() == null)
        {
            errors.add("0|id");
        }
        if (test.getDeltacheckDays() == null)
        {
            errors.add("0|deltadays");
        }
        if (test.getDeltacheckMin() == null)
        {
            errors.add("0|deltamin");
        }
        if (test.getDeltacheckMax() == null)
        {
            errors.add("0|deltamax");
        }
        if (test.getUser().getId() == null)
        {
            errors.add("0|userId");
        }
        return errors;
    }

    private List<String> validateFields(boolean isEdit, Test test) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (test.getTestType() != null)
        {
            errors.addAll(commonValidate(test, isEdit));
            if (test.getLevel().getId() != null)
            {
                if (test.getLevel().getId() < 33 || test.getLevel().getId() > 37)
                {
                    errors.add("3|level");
                }
            }

            if (test.getGender().getId() != null)
            {
                if (test.getGender().getId() != 7 && test.getGender().getId() != 8 && test.getGender().getId() != 9 && test.getGender().getId() != 42)
                {
                    errors.add("3|gender");
                }
            } else
            {
                errors.add("0|gender");
            }

            if (test.getPrintOnReport() != null && test.getPrintOnReport() != 0)
            {
                if (test.getPrintOnReport() < 1 && test.getPrintOnReport() > 3)
                {
                    errors.add("3|print on reports");
                }
            } else
            {
                errors.add("0|print on reports");
            }

            if (test.getTestType() == 0)
            {
                if (test.getDeliveryDays() == null)
                {
                    errors.add("0|delivery days");
                }

                if (test.getSelfValidation() != null && test.getSelfValidation() != 0)
                {
                    if (test.getSelfValidation() < 1 && test.getSelfValidation() > 5)
                    {
                        errors.add("3|selfvalidation");
                    }
                } else
                {
                    errors.add("0|selfvalidation");
                }

                if (test.getProcessingBy() != null && test.getProcessingBy() != 0)
                {
                    if (test.getProcessingBy() < 1 && test.getProcessingBy() > 2)
                    {
                        errors.add("3|processing by");
                    }
                } else
                {
                    errors.add("0|processing by");
                }

                if (test.getResultType() != null && test.getResultType() != 0)
                {
                    if (test.getResultType() != 1 && test.getResultType() != 2)
                    {
                        errors.add("3|result type");
                    }
                } else
                {
                    errors.add("0|result type");
                }
            } else
            {
                if (test.getTestType() == 1)
                {
                    if (test.getArea().getId() == null)
                    {
                        errors.add("0|area");
                    }

                    if (test.getConcurrences().size() < 1)
                    {
                        errors.add("0|concurrences");
                    }
                } else
                {
                    if (test.getTestType() == 1 || test.getTestType() == 2)
                    {
                        if (test.getConcurrences().size() < 1)
                        {
                            errors.add("0|concurrences");
                        }
                    }
                }
            }
        } else
        {
            errors.add("0|test type");
        }
        return errors;
    }

    /**
     * Retorna filtro correspondiente con el tipo.
     *
     * @param type Tipo de prueba: 0 -> Examenes, 1 -> Perfiles, 2 -> Paquetes, 3 -> Examenes y Perfiles, 4 -> Perfiles y Paquetes.
     *
     * @return filtro correspondiente con el tipo.
     */
    private Predicate<TestBasic> filterType(int type)
    {
        if (type == 0 || type == 1 || type == 2)
        {
            return a -> a.getTestType() == type;
        } else
        {
            if (type == 3)
            {
                return a -> a.getTestType() == 0 || a.getTestType() == 1;

            } else
            {
                if (type == 4)
                {
                    return a -> a.getTestType() == 1 || a.getTestType() == 2;
                } else
                {
                    if (type == 5)
                    {
                        return a -> true;
                    } else
                    {
                        return a -> true;
                    }
                }
            }
        }
    }

    @Override
    public int updatePrintOrder(List<TestBasic> tests) throws Exception
    {
        List<String> errors = validateFieldsImport(tests);
        if (errors.isEmpty())
        {
            //Realiza la auditoria del examen
//            String ids = "";
//            for (TestBasic test : tests)
//            {
//                ids += "'" + test.getId() + "',";
//            }
//            ids = ids.substring(0, ids.lastIndexOf(","));
//            //Consulta los examenes enviados en base de datos
//            List<TestBasic> oldTests = testDao.list(ids);
//            Test oldTestT;
//            Test oldTestT2;
//            for (TestBasic oldTest : oldTests)
//            {
//                oldTestT = new Test(oldTest.getId());
//                oldTestT.setPrintOrder(oldTest.getPrintOrder());
//                oldTestT2 = tests
//                        .stream()
//                        .filter(t -> Objects.equals(t.getId(), oldTest.getId()))
//                        .map((t) ->
//                        {
//                            Test s = new Test();
//                            s.setId(t.getId());
//                            s.setPrintOrder(t.getPrintOrder());
//                            return s;
//                        }).findFirst()
//                        .get();
//                //Compara si los dias de procesamiento son diferentes
//                if (!oldTestT.getPrintOrder().equals(oldTestT2.getPrintOrder()))
//                {
//                    trackingService.registerConfigurationTracking(oldTestT, oldTestT2, Test.class);
//                }
//            }
            trackingService.registerConfigurationTracking(null, tests, TestBasic.class);
            return testDao.updatePrintOrder(tests);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    private List<String> validateFieldsImport(List<TestBasic> tests) throws Exception
    {
        List<String> errors = new ArrayList<>();
        int linea = 1;
        for (TestBasic test : tests)
        {
            if (test.getId() == null)
            {
                errors.add("0|id|" + linea);
            }
            if (test.getPrintOrder() == null)
            {
                errors.add("0|printOrder|" + linea);
            }

            linea++;
        }
        return errors;
    }

    @Override
    public TestBasic updateDeltacheck(TestBasic deltacheck) throws Exception
    {
        List<String> errors = validateDeltaFields(deltacheck);
        if (errors.isEmpty())
        {
            TestBasic previous = testDao.informationTestBasic(deltacheck.getId());
            testDao.updateDeltacheck(deltacheck);
            trackingService.registerConfigurationTracking(previous, deltacheck, TestBasic.class);
            return deltacheck;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public int updateProcessingDays(List<TestBasic> tests) throws Exception
    {
        //Realiza la auditoria del examen
//        String ids = "";
//        ids = tests.stream().map((test) -> "'" + test.getId() + "',").reduce(ids, String::concat);
//        ids = ids.substring(0, ids.lastIndexOf(","));
//        //Consulta los examenes enviados en base de datos
//        List<TestBasic> oldTests = testDao.list(ids);
//        Test oldTestT;
//        Test oldTestT2;
//        for (TestBasic oldTest : oldTests)
//        {
//            oldTestT = new Test(oldTest.getId());
//            oldTestT.setProcessingDays(oldTest.getProcessingDays());
//            oldTestT2 = tests
//                    .stream()
//                    .filter(t -> Objects.equals(t.getId(), oldTest.getId()))
//                    .map((t) ->
//                    {
//                        Test s = new Test();
//                        s.setId(t.getId());
//                        s.setProcessingDays(t.getProcessingDays());
//                        return s;
//                    }).findFirst()
//                    .get();
//            //Compara si los dias de procesamiento son diferentes
//            if (oldTestT.getProcessingDays() == null || !oldTestT.getProcessingDays().equals(oldTestT2.getProcessingDays()))
//            {
//                trackingService.registerConfigurationTracking(oldTestT, oldTestT2, Test.class);
//            }
//        }
        trackingService.registerConfigurationTracking(null, tests, TestBasic.class);
        //Realiza el ingreso en batch de los datos
        return testDao.updateProcessingDays(tests);
    }

    @Override
    public List<AutomaticTest> listAutomaticTest(int idTest) throws Exception
    {
        return testDao.listAutomaticTest(idTest);
    }

    @Override
    public int insertAutomaticTest(List<AutomaticTest> tests, int idTest) throws Exception
    {
        List<String> errors = validateAutomaticTest(tests);
        if (errors.isEmpty())
        {
            List<AutomaticTest> listPrevious = listAutomaticTest(idTest);
            Integer insert = testDao.insertAutomaticTest(tests, idTest);
            trackingService.registerConfigurationTracking(listPrevious, tests, AutomaticTest.class);
            return insert;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    private List<String> validateAutomaticTest(List<AutomaticTest> automaticTests)
    {
        List<String> errors = new ArrayList<>();
        int quantity = 0;
        for (AutomaticTest automaticTest : automaticTests)
        {
            quantity++;
            if (automaticTest.getId() == null || automaticTest.getId() == 0)
            {
                errors.add("0|id Test|" + quantity);
            }

            if (automaticTest.getSign().getId() != null && automaticTest.getSign().getId() != 0)
            {
                if (automaticTest.getSign().getId() < 50 || automaticTest.getSign().getId() > 56)
                {
                    errors.add("3|sign|" + quantity);
                }
            } else
            {
                errors.add("0|sign|" + quantity);
            }

            if (automaticTest.getResult1() == null)
            {
                errors.add("0|result 1|" + quantity);
            }

            if (automaticTest.getAutomaticTest().getId() == null || automaticTest.getAutomaticTest().getId() == 0)
            {
                errors.add("0|automatic test|" + quantity);
            }
        }

        return errors;
    }

    private List<String> commonValidate(Test test, boolean isEdit) throws Exception
    {
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (test.getId() == null)
            {
                errors.add("0|id");
            } else
            {
                if (testDao.get(test.getId(), null, null, null) == null)
                {
                    errors.add("2|id");
                }
            }
        }

        if (test.getCode() != null)
        {
            Test testC = testDao.get(null, test.getCode(), null, null);
            if (testC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(test.getId(), testC.getId()))
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

        if (test.getAbbr() != null && !test.getAbbr().isEmpty())
        {
            Test testC = testDao.get(null, null, null, test.getAbbr());
            if (testC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(test.getId(), testC.getId()))
                    {
                        errors.add("1|abbreviation");
                    }
                } else
                {
                    errors.add("1|abbreviation");
                }
            }
        } else
        {
            errors.add("0|abbreviation");
        }

        if (test.getTestType() == 0 && test.getArea().getId() == null)
        {
            errors.add("0|area");
        }

        if (test.getUser().getId() == null || test.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;

    }

    @Override
    public void patch(Test test) throws Exception
    {
        List<String> errors = commonValidate(test, true);
        if (errors.isEmpty())
        {
            Test oldTest = testDao.get(test.getId(), null, null, null);
            trackingService.registerConfigurationTracking(oldTest, test, Test.class);
            testDao.updateTestFields(test);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public int insertPyPTest(PypDemographic pyp) throws Exception
    {
        List<String> errors = validatePyP(pyp);
        if (errors.isEmpty())
        {
            PypDemographic old = getPypTest(pyp.getId());
            old = old.getTests().isEmpty() ? null : old;
            testDao.deletePyPTest(pyp.getId());
            trackingService.registerConfigurationTracking(old, pyp, PypDemographic.class);
            return testDao.insertPyPTest(pyp);
        } else
        {
            throw new EnterpriseNTException(errors);
        }

    }

    @Override
    public PypDemographic getPypTest(Integer id) throws Exception
    {
        PypDemographic pyp = testDao.getPyPTests(id);
        List<Item> list = listService.list(Constants.LIST_GENDER_ITEM);
        pyp.setNameGender(list.stream().filter((Item item) -> item.getId() == pyp.getGender()).map((Item item) -> item.getEsCo()).findFirst().orElse(null));
        if (pyp.getUnit() == 1)
        {
            pyp.setNameUnit("Años");
        } else
        {
            pyp.setNameUnit("Días");
        }
        return pyp;
    }

    /**
     * Valida parametros para inserción de datos
     *
     * @param pyp Datos de promoción y prevención
     *
     * @return Lista de errores
     * @throws Exception Error
     */
    private List<String> validatePyP(PypDemographic pyp) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (pyp.getId() == null)
        {
            errors.add("0|id");
        }

        if (pyp.getUser().getId() == null || pyp.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;

    }

    @Override
    public List<TestBasic> testMicrobilogy() throws Exception
    {
        return testDao.list().stream()
                .filter(filter -> filter.isState() == true)
                .filter(filter -> filter.getSample().getId() != 0)
                .filter(filter -> filter.getSample().getLaboratorytype().contains("3"))
                .filter(filter -> filter.getTestType() == 0)
                .collect(Collectors.toList());

    }

    @Override
    public List<TestBasic> testMediaCulture(boolean isMediaCulture) throws Exception
    {
        return testDao.listTestMediaCulture().stream()
                .filter(filter -> filter.isState() == true)
                .filter(filter -> filter.getSample().getId() != 0)
                .filter(filter -> filter.getSample().getLaboratorytype().contains("3"))
                .filter(filter -> filter.getTestType() == 0)
                .filter(filter -> filter.isSelected() == isMediaCulture)
                .collect(Collectors.toList());

    }

    @Override
    public List<TestByService> listTestByService(int idService) throws Exception
    {
        return testDao.listTestByService(idService);
    }

    @Override
    public TestByService insertTestByService(TestByService test) throws Exception
    {
        List<String> errors = validateTestByService(test);
        if (errors.isEmpty())
        {
            TestByService testPrevious = listTestByService(test.getService().getId()).stream().filter((TestByService t) -> Objects.equals(test.getTest().getId(), t.getTest().getId())).findFirst().orElse(null);
            testPrevious.getService().setName("");
            TestByService testCurrent = testDao.insertTestByService(test);
            trackingService.registerConfigurationTracking(testPrevious, testCurrent, TestByService.class);
            return testCurrent;
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    private List<String> validateTestByService(TestByService test)
    {
        List<String> errors = new ArrayList<>();
        if (test.getTest().getId() == null || test.getTest().getId() == 0)
        {
            errors.add("0|test");
        }

        if (test.getService().getId() == null || test.getService().getId() == 0)
        {
            errors.add("0|service");
        }

        if (test.getExpectedTime() == null)
        {
            errors.add("0|expectedTime");
        }

        if (test.getMaximumTime() == null)
        {
            errors.add("0|maximumTime");
        }

        return errors;
    }

    @Override
    public List<TestBasic> listTestConfidential() throws Exception
    {
        return testDao.list().stream()
                .filter(filter -> filter.isState())
                .filter(filter -> filter.isConfidential())
                .filter(filter -> filter.getTestType() == 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestBasic> getForOrderEntry() throws Exception
    {
        int idbranch = JWT.decode(request).getBranch();
        return testDao.listTestByBranch(idbranch).stream()
                .filter(filter -> filter.isState())
                .filter(filter -> filter.getViewInOrderEntry() == 1 || filter.getDeleteProfile() == 1)
                .collect(Collectors.toList());

    }

    @Override
    public List<Test> getChilds(int id) throws Exception
    {
        return testDao.getChilds(id);
    }

    @Override
    public List<Requirement> getRequirements(List<Test> tests) throws Exception
    {
        String idsAsString = "";
        idsAsString = tests.stream().map((test) -> "" + test.getId() + ",").reduce(idsAsString, String::concat);
        idsAsString = idsAsString.contains(",") ? idsAsString.substring(0, idsAsString.lastIndexOf(",")) : idsAsString;
        return testDao.getRequirements(idsAsString)
                .stream()
                .filter(t -> t.isState())
                .sorted((Requirement o1, Requirement o2)
                        ->
                {
                    return o1.getId().compareTo(o2.getId());
                }).distinct()
                .collect(Collectors.toList());

    }

    @Override
    public int updateTestTax(List<TestBasic> tests) throws Exception
    {
        List<TestBasic> previous = list((int) tests.get(0).getTestType(), tests.get(0).isState(), tests.get(0).getArea().getId() == 0 ? null : tests.get(0).getArea().getId());
        int registers = testDao.updateTax(tests);
        trackingService.registerConfigurationTracking(previous, tests, null, "net.cltech.enterprisent.domain.masters.test.taxesProof");
        return registers;
    }

    @Override
    public List<Profile> getProfiles() throws Exception
    {
        return testDao.getProfiles();
    }

    @Override
    public List<TestByLaboratory> getTestByBranchByLaboratory(Integer testId, Integer branchId, Integer laboratoryId) throws Exception
    {
        List<TestByLaboratory> tests = new ArrayList<>();
        for (TestByLaboratory test : testDao.TestByBranchByLaboratory(testId, branchId, laboratoryId))
        {
            TestByLaboratory newTest = test;
            if (!tests.contains(newTest))
            {
                if (newTest.getUrgency() == null)
                {
                    newTest.setUrgency((short) 2);
                }
                if (newTest.getRoutine() == null)
                {
                    newTest.setRoutine((short) 2);
                }
                tests.add(newTest);
            }
            newTest = tests.get(tests.indexOf(newTest));

            if (test.getGroupType() == 1)
            {
                newTest.setUrgency((short) 1);
            } else
            {
                newTest.setRoutine((short) 1);
            }
            if (newTest.getUrgency() == 1 && newTest.getRoutine() == 1)
            {
                newTest.setGroupType(null);
            }
        }
        return tests;
    }

    @Override
    public TestInformation informationByid(long test, int type) throws Exception
    {
        TestInformation testInformation = null;
        testInformation = testDao.informationTestByid(test, type);
        if (testInformation != null)
        {
            if (type == 0)
            {
                testInformation.setRequirements(testDao.requirementByTest(test));
            } else
            {
                testInformation.setTests(testDao.getChildsByProfile(test));
                testInformation.setRequirements(testDao.requirementByTest(test));
            }
        }
        return testInformation;
    }

    public List<TestHomeBound> testHomeBound() throws Exception
    {

        return null;
    }
    
    @Override
    public List<Integer> getChildrenIds(int id) throws Exception
    {
        return testDao.getChildrenIds(id);
    }
    
    @Override
    public int getTestTypeByTestId(int id) throws Exception
    {
        return testDao.getTestTypeByTestId(id);
    }
    
    @Override
    public int getSampleState(int id, long idOrder) throws Exception
    {
        return testDao.getSampleState(id, idOrder);
    }
    
    @Override
    public List<Test> getChildsPackageOrProfile(int id, long idOrder, boolean isPackage) throws Exception
    {
        return testDao.getChildsPackageOrProfile(id, idOrder, isPackage);
    }
}
