package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Bank;
import net.cltech.enterprisent.domain.masters.billing.TaxPrinter;

/**
 * Representa los métodos de acceso a base de datos para la información de los Bancos.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/06/2017
 * @see Creación
 */
public interface TaxPrinterDao
{
    /**
     * Lista los bancos desde la base de datos.
     *
     * @return Lista de bancos.
     * @throws Exception Error en la base de datos.
     */
    public List<TaxPrinter> list() throws Exception;

    /**
     * Registra un nuevo banco en la base de datos.
     *
     * @param taxPrinter
     * @return Instancia con los datos del banco.
     * @throws Exception Error en la base de datos.
     */
    public TaxPrinter create(TaxPrinter taxPrinter) throws Exception;
    
    /**
     * Obtener información de un banco por un campo especifico.
     *
     * @param id ID del banco a ser consultado.
     * @param name Nombre del banco a ser consultado.
     * @return Instancia con los datos del banco.
     * @throws Exception Error en la base de datos.
     */
    public TaxPrinter get(Integer id, String name, String code) throws Exception;

    /**
     * Actualiza la información de un banco en la base de datos.
     *
     * @param taxPrinter
     * @return Objeto del banco modificada.
     * @throws Exception Error en la base de datos.
     */
    public TaxPrinter update(TaxPrinter taxPrinter) throws Exception;


}
