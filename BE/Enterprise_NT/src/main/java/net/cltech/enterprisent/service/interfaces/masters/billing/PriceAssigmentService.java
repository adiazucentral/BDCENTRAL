package net.cltech.enterprisent.service.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.FormulaOption;
import net.cltech.enterprisent.domain.masters.billing.PriceAssigment;
import net.cltech.enterprisent.domain.masters.billing.PriceAssignmentBatch;
import net.cltech.enterprisent.domain.masters.billing.TestPrice;

/**
 * Interfaz de servicios a la informacion del maestro Vigencia
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/08/2017
 * @see Creación
 */
public interface PriceAssigmentService
{

    /**
     * Almacena precio de examen
     *
     * @param rate Información con el preceio
     *
     * @return registros afectados
     * @throws java.lang.Exception Error
     */
    public int savePrice(PriceAssigment rate) throws Exception;

    /**
     * Importa los precios de la tarifa
     *
     * @param rate tarifas
     *
     * @return Registros afectados
     * @throws java.lang.Exception Error
     */
    public int importPrices(PriceAssigment rate) throws Exception;

    /**
     * Lista examenes con su precio
     *
     * @param valid id de la vigencia
     * @param rate id de la tarifa
     * @param area id del area
     *
     * @return Lista de examenes con precio
     * @throws java.lang.Exception Error
     */
    public List<TestPrice> listTestPrice(int valid, int rate, Integer area) throws Exception;

    /**
     * Actualiza precios mediante formula
     *
     * @param formula
     *
     * @return registros afectados
     * @throws Exception Error
     */
    public Integer applyFormula(FormulaOption formula) throws Exception;

    /**
     * Aplica los precios de la vigencia
     *
     * @param valid id de la vigencia
     *
     * @return Numero de registros afectados
     * @throws java.lang.Exception
     */
    public Integer applyFeeschedule(Integer valid) throws Exception;
    
    /**
     * Importa los precios de una o mas tarifas
     *
     * @param rates Lista de tarifas con sus precios
     *
     * @return Registros afectados
     * @throws java.lang.Exception Error
     */
    public int importListPrices(List<PriceAssignmentBatch> rates) throws Exception;

}
