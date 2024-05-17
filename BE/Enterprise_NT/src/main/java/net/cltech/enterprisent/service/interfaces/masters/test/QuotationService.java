/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.QuotationHeader;

/**
 * Servicios sobre las cotizaciones
 *
 * @version 1.0.0
 * @author jrodriguez
 * @since 31/10/2018
 * @see Creacion
 */
public interface QuotationService
{

    /**
     * Realiza el registro del tipo de entrega de los resultados
     *
     * @param quotationHeader@return
     * @throws java.lang.Exception
     */
    public int insertQuotations(QuotationHeader quotationHeader) throws Exception;

    /**
     * Busca paciente por nombres, apellidos y fechas de nacimiento
     *
     * @param name
     * @return Lista de cotizaciones
     * @throws Exception Error en el servicio
     */
    public List<QuotationHeader> getPatientBy(String name) throws Exception;

    /**
     * Lista las cotizaciones registrados en un rango de fechas.
     *
     * @param init Fecha Inicial
     * @param end Fecha Final
     * @return Retorna la lista de inconsistencias.
     */
    public List<QuotationHeader> listQuotationHeader(Date init, Date end);
}
