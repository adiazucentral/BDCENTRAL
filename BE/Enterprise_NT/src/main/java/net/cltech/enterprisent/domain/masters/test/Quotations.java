/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de una cotizacion
 *
 * @author jrodriguez
 * @version 1.0.0
 * @since 31/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Cotizacion",
        description = "Representa una cotizacion"
)
public class Quotations
{

    @ApiObjectField(name = "quotationHeader", description = "Cabecera de cotizacion", order = 1)
    private QuotationHeader quotationHeader;
    @ApiObjectField(name = "quotationDetail", description = "Cabecera de cotizacion", order = 2)
    private QuotationDetail quotationDetail;

    public QuotationHeader getQuotationHeader()
    {
        return quotationHeader;
    }

    public void setQuotationHeader(QuotationHeader quotationHeader)
    {
        this.quotationHeader = quotationHeader;
    }

    public QuotationDetail getQuotationDetail()
    {
        return quotationDetail;
    }

    public void setQuotationDetail(QuotationDetail quotationDetail)
    {
        this.quotationDetail = quotationDetail;
    }

}
