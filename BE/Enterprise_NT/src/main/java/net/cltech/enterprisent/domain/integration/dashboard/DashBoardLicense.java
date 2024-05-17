/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.dashboard;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de las licencias.
 *
 * @version 1.0.0
 * @author equijano
 * @since 10/12/2018
 * @see Creación
 */
@ApiObject(
        name = "Lincecias",
        group = "Integracion",
        description = "Servicios de licencias"
)
public class DashBoardLicense
{

    @ApiObjectField(name = "accessTab", description = "Acceso al tablero", required = true, order = 1)
    public boolean accessTab;
    @ApiObjectField(name = "idTab", description = "Id del tablero", required = true, order = 2)
    public String idTab;
    @ApiObjectField(name = "product", description = "Nombre del producto", required = true, order = 3)
    public String product;
    @ApiObjectField(name = "totalTab", description = "Tableros totales", required = true, order = 4)
    public String totalTab;

    public boolean isAccessTab()
    {
        return accessTab;
    }

    public void setAccessTab(boolean accessTab)
    {
        this.accessTab = accessTab;
    }

    public String getIdTab()
    {
        return idTab;
    }

    public void setIdTab(String idTab)
    {
        this.idTab = idTab;
    }

    public String getProduct()
    {
        return product;
    }

    public void setProduct(String product)
    {
        this.product = product;
    }

    public String getTotalTab()
    {
        return totalTab;
    }

    public void setTotalTab(String totalTab)
    {
        this.totalTab = totalTab;
    }

}
