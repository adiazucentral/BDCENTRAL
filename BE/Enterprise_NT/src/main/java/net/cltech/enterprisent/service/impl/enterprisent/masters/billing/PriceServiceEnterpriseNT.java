package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.billing.PriceDao;
import net.cltech.enterprisent.dao.interfaces.masters.billing.RateDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.FeeScheduleDao;
import net.cltech.enterprisent.dao.interfaces.masters.test.TestDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.FormulaOption;
import net.cltech.enterprisent.domain.masters.billing.PriceAssigment;
import net.cltech.enterprisent.domain.masters.billing.PriceAssignmentBatch;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.billing.TestPrice;
import net.cltech.enterprisent.domain.masters.test.FeeSchedule;
import net.cltech.enterprisent.domain.masters.test.Standardization;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.service.interfaces.masters.billing.PriceAssigmentService;
import net.cltech.enterprisent.service.interfaces.masters.test.CentralSystemService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Tarifas para Enterprise
 * NT
 *
 * @version 1.0.0
 * @author eacuna
 * @see 18/08/2017
 * @see Creación
 */
@Service
public class PriceServiceEnterpriseNT implements PriceAssigmentService
{

    @Autowired
    private PriceDao dao;
    @Autowired
    private RateDao rateDao;
    @Autowired
    private TestDao testDao;
    @Autowired
    private FeeScheduleDao validDao;
    @Autowired
    private CentralSystemService systemService;
    @Autowired
    private ConfigurationDao daoConfig;
    @Autowired
    private TrackingService trackingService;

    @Override
    public int savePrice(PriceAssigment rate) throws Exception
    {
        List<String> errors = validateInsertFields(rate, testDao.list(), validDao.list(), rateDao.list());
        if (errors.isEmpty())
        {
            if (dao.priceExists(rate.getId(), rate.getTest().getId(), rate.getIdValid()))
            {
                PriceAssigment previous = dao.get(rate.getId(), rate.getTest().getId(), rate.getIdValid());
                int insert = dao.priceUpdate(rate);
                trackingService.registerConfigurationTracking(previous, rate, PriceAssigment.class);
                return insert;
            } else
            {
                int insert = dao.priceCreate(rate);
                trackingService.registerConfigurationTracking(null, rate, PriceAssigment.class);
                return insert;
            }
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public int importPrices(PriceAssigment rate) throws Exception
    {
        List<String> errors = validateImport(rate, validDao.list(), rateDao.list());
        if (errors.isEmpty())
        {
            dao.deletePrices(rate.getId(), rate.getIdValid());
            return dao.priceBatchCreate(rate);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<TestPrice> listTestPrice(int valid, int rate, Integer area) throws Exception
    {
        return dao.listTestPrices(valid, rate).stream()
                .filter(test -> (area == -1 || test.getArea().getId().equals(area)))
                .collect(Collectors.toList());
    }

    @Override
    public Integer applyFeeschedule(Integer valid) throws Exception
    {
        List<String> errors = validateFeescheduleActivation(valid, validDao.list());
        if (errors.isEmpty())
        {
            dao.deleteActivePrices();
            return dao.activeFeeschedule(valid);
        } else
        {
            throw new EnterpriseNTException(errors);
        }

    }

    @Override
    public Integer applyFormula(FormulaOption formula) throws Exception
    {

        List<String> errors = validateFormula(formula, validDao.list(), rateDao.list());
        List<TestPrice> fromList = listTestPrice(formula.getFeeSchedule(), formula.getFrom(), -1);
        List<TestPrice> toList;
        List<TestPrice> insertList;

        if (errors.isEmpty())
        {
            PriceAssigment rate = new PriceAssigment(formula.getTo(), formula.getFeeSchedule());

            if (formula.getWriteType() == FormulaOption.WRITE_TOTAL)
            {
                dao.deletePrices(formula.getTo(), formula.getFeeSchedule());
                rate.setImportTests(getCalculatedPrices(fromList, formula));
            } else
            {
                toList = listTestPrice(formula.getFeeSchedule(), formula.getTo(), -1);
                insertList = toList.stream()
                        .filter(test -> BigDecimal.ZERO.equals(test.getPrice()))
                        .map(test -> fromList.get(fromList.indexOf(test)))
                        .collect(Collectors.toList());
                rate.setImportTests(getCalculatedPrices(insertList, formula));

            }
            return dao.priceBatchCreate(rate);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    /**
     * Valida campos enviados
     *
     * @param rate PriceAssigment información a validar
     * @param valids lista de vigencias creadas
     * @param rates lista de tarifas creadas
     *
     * @return lista de errores encontrados
     * @throws Exception Error
     */
    private List<String> validatePriceFields(PriceAssigment rate, List<FeeSchedule> valids, List<Rate> rates) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (rate.getId() == null)
        {
            errors.add("0|rate");
        } else if (rates.stream().noneMatch(record -> record.getId().equals(rate.getId())))
        {
            errors.add("3|rate");
        }
        if (rate.getIdValid() == null)
        {
            errors.add("0|feeschedule");
        } else if (valids.stream().noneMatch(record -> record.getId().equals(rate.getIdValid())))
        {
            errors.add("3|feeschedule");
        }
        return errors;
    }

    /**
     * Valida campos para la inserción de datos
     *
     * @param rate información a validar
     * @param tests lista de examenes creados
     * @param valids lista de vigencias creadas
     * @param rates lista de tarifas creadas
     *
     * @return lista de errores encontrados
     * @throws Exception Error
     */
    private List<String> validateInsertFields(PriceAssigment rate, List<TestBasic> tests, List<FeeSchedule> valids, List<Rate> rates) throws Exception
    {
        List<String> errors = validatePriceFields(rate, valids, rates);

        if (rate.getTest().getId() == null)
        {
            errors.add("0|test");
        } else if (tests.stream().noneMatch(record -> record.getId().equals(rate.getTest().getId())))
        {
            errors.add("3|test");
        }
        return errors;
    }

    /**
     * Valida campos para importar
     *
     * @param rate Campos a validar
     * @param valids lista de vigencais
     * @param rates lista de tarifas
     *
     * @return lista de errores encontrados
     * @throws Exception Error
     */
    private List<String> validateImport(PriceAssigment rate, List<FeeSchedule> valids, List<Rate> rates) throws Exception
    {
        List<String> errors = validatePriceFields(rate, valids, rates);
        boolean isLis = rate.getCentralSystem().equals(0);
        
        if(rate.getCentralSystem() != 0){
            int index = 0;
            List<Standardization> homologate = systemService.standardizationList(rate.getCentralSystem(), true);
            for (TestPrice importTest : rate.getImportTests())
            {
                importTest.setId(getIdTest(homologate, importTest, isLis));
                if (importTest.getId() == 0)
                {
                    errors.add("3|code|" + index);
                }
                index++;
            }
        }
        else{
            List <TestPrice> testLis = testDao.getCodeTest();
            int index = 0;
            for (TestPrice importTest : rate.getImportTests())
            {
                List <TestPrice> testfound = testLis.stream().filter(test -> (test.getCode() == null ? importTest.getCode() == null : test.getCode().equals(importTest.getCode()))).collect(Collectors.toList());
                if (testfound.isEmpty())
                {
                    errors.add("3|code|" + index);
                }
                else{
                    importTest.setId(testfound.get(0).getId());
                }
                index++;
            }
        }
       
        return errors;
    }

    /**
     * Obtiene id id del examen encontrado
     *
     * @param homologate lista con códigos de homologación
     * @param price examen que se desea buscar
     * @param isLisCode Identifica si busca por cógigo de LIS o código de
     * homologacion
     *
     * @return id encontrado, 0 si no encuentra examen
     */
    private int getIdTest(List<Standardization> homologate, TestPrice price, boolean isLisCode)
    {
        for (Standardization standardization : homologate)
        {
            if (isLisCode)
            {
                if (price.getCode() != null && !price.getCode().trim().isEmpty() && standardization.getCode().equalsIgnoreCase(price.getCode()))
                {
                    return standardization.getId();
                }
            } else
            {
                if (price.getCode() != null && !price.getCode().trim().isEmpty() && standardization.getCodes().contains(price.getCode()))
                {
                    return standardization.getId();
                }
            }
        }
        return 0;
    }

    /**
     * Valida campos para aplicar formula
     *
     * @param formula campos a validar
     * @param valids lista de vigencias
     * @param rates lista de tarifas creadas
     *
     * @return lista de errores encontrados
     * @throws Exception Error
     */
    private List<String> validateFormula(FormulaOption formula, List<FeeSchedule> valids, List<Rate> rates) throws Exception
    {
        List<String> errors = new ArrayList<>();
        if (rates.stream().noneMatch(record -> record.getId().equals(formula.getFrom())))
        {
            errors.add("3|from");
        }
        if (rates.stream().noneMatch(record -> record.getId().equals(formula.getTo())))
        {
            errors.add("3|to");
        }
        if (valids.stream().noneMatch(record -> record.getId().equals(formula.getFeeSchedule())))
        {
            errors.add("3|feeschedule");
        }
        if (!FormulaOption.operators.contains(formula.getOperator()))
        {
            errors.add("3|operator");
        }
        if (formula.getRoundMode() != 0 && formula.getRoundMode() != 1)
        {
            errors.add("3|roundMode");
        }
        if (formula.getWriteType() != 0 && formula.getWriteType() != 1)
        {
            errors.add("3|writeType");
        }
        if (formula.getValue() == null)
        {
            errors.add("3|value");
        }

        return errors;
    }

    /**
     * Obtiene Lista de examenes con los precios actualizados deacuerdo a la
     * formula
     *
     * @param priceFromList lista de precios
     * @param option valor de formula que se aplican
     *
     * @return examnes con precios actualizados
     * @throws Exception Error
     */
    public List<TestPrice> getCalculatedPrices(List<TestPrice> priceFromList, FormulaOption option) throws Exception
    {
        Integer decimal = daoConfig.get("ManejoCentavos") == null ? 0 : daoConfig.get("ManejoCentavos").getValue().equalsIgnoreCase("true") ? 2 : 0;
        return priceFromList.stream()
                .map(test -> test.setPrice(getResult(test.getPrice(), option, decimal)))
                .filter(test -> !BigDecimal.ZERO.equals(test.getPrice()))
                .collect(Collectors.toList());

    }

    /**
     * Optiene el resultado de un calculo
     *
     * @param price precio a calcular
     * @param option valores de calculo
     * @param decimal némero de decimales
     *
     * @return precio actualizado
     */
    private BigDecimal getResult(BigDecimal price, FormulaOption option, Integer decimal)
    {

        RoundingMode round = option.getRoundMode() == 0 ? RoundingMode.DOWN : RoundingMode.HALF_UP;
        switch (option.getOperator())
        {
            case "+":
                return price.add(option.getValue()).setScale(decimal, round);
            case "-":
                BigDecimal result = price.subtract(option.getValue()).setScale(decimal, round);
                return result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result;
            case "*":
                return price.multiply(option.getValue()).setScale(decimal, round);
            case "/":
                return price.divide(option.getValue(), decimal, round);
            default:
                return BigDecimal.ZERO;
        }
    }

    /**
     * Valida campos para aplicar la vigencia
     *
     * @param idValid id de la vigencia
     * @param valids lista de vigencias creadas
     *
     * @return Errores encontrados
     * @throws Exception Error
     */
    private List<String> validateFeescheduleActivation(int idValid, List<FeeSchedule> valids) throws Exception
    {

        List<String> errors = new ArrayList<>();
        if (valids.stream().noneMatch(record -> record.getId().equals(idValid)))
        {
            errors.add("3|feeschedule");
        }
        return errors;
    }
    
    @Override
    public int importListPrices(List<PriceAssignmentBatch> rates) throws Exception
    {
        List<String> errors = validateListImport(rates, validDao.list());
        if (errors.isEmpty())
        {            
            List<Integer> idsRates = rates.stream().map(rate -> rate.getIdRate()).distinct().collect(Collectors.toList());
            Integer idValidity = rates.get(0).getIdValid();
            dao.deleteListPrices(idsRates, idValidity);
            return dao.priceBatchCreate(rates);
        } else
        {
            throw new EnterpriseNTException(errors);
        }
    }
    
    /**
     * Valida campos para importar
     *
     * @param list Campos a validar
     * @param valids lista de vigencais
     * @param rates lista de tarifas
     *
     * @return lista de errores encontrados
     * @throws Exception Error
     */
    private List<String> validateListImport(List<PriceAssignmentBatch> list, List<FeeSchedule> valids) throws Exception
    {
        List<String> errors = new ArrayList<>();

        if (list.size() > 0)
        {
            
            Integer[] idsRates = list.stream().map(ra -> ra.getIdRate()).distinct().toArray(Integer[]::new);
            
            Integer idValidity = list.get(0).getIdValid();
                    
            for(int i=0; i < idsRates.length ; i++) {
                if (idsRates[i] == null)
                {
                    errors.add("0|rate");
                }
            }

            if (idValidity == null)
            {
                errors.add("0|feeschedule");
            } else if (valids.stream().noneMatch(record -> record.getId().equals(idValidity)))
            {
                errors.add("3|feeschedule");
            }
            
            boolean isLis = list.get(0).getCentralSystem().equals(0);
            
            Integer idCentralSysmem = list.get(0).getCentralSystem();
            
            List<Standardization> homologate = new LinkedList<>();
            List <TestPrice> testLis = testDao.getCodeTest();
            if(idCentralSysmem != 0) {
                homologate = systemService.standardizationList(idCentralSysmem, true);
            }
            int index = 0;
            
            for (PriceAssignmentBatch rate : list)
            {
                if(rate.getCentralSystem() != 0) {
                    rate.setIdTest(getIdTest(homologate, rate.getCode(), isLis));
                    if (rate.getIdTest() == 0)
                    {
                        errors.add("3|code|" + index);
                    }
                }
                else {
                    List <TestPrice> testfound = testLis.stream().filter(test -> (test.getCode() == null ? rate.getCode() == null : test.getCode().equals(rate.getCode()))).collect(Collectors.toList());
                    if (testfound.isEmpty())
                    {
                        errors.add("3|code|" + index);
                    }
                    else{
                        rate.setIdTest(testfound.get(0).getId());
                    }
                }
                index++;
            }
        } else
        {
            errors.add("0|rates");
        }
        return errors;
    }
    
    /**
     * Obtiene id id del examen encontrado
     *
     * @param homologate lista con códigos de homologación
     * @param price examen que se desea buscar
     * @param isLisCode Identifica si busca por cógigo de LIS o código de
     * homologacion
     *
     * @return id encontrado, 0 si no encuentra examen
     */
    private int getIdTest(List<Standardization> homologate, String code, boolean isLisCode)
    {
        for (Standardization standardization : homologate)
        {
            if (isLisCode)
            {
                if (code != null && !code.trim().isEmpty() && standardization.getCode().equalsIgnoreCase(code))
                {
                    return standardization.getId();
                }
            } else
            {
                if (code != null && !code.trim().isEmpty() && standardization.getCodes().contains(code))
                {
                    return standardization.getId();
                }
            }
        }
        return 0;
    }
}
