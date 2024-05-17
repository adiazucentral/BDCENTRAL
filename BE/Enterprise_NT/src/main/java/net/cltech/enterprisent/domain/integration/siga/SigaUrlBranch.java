/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un servicio para el Siga.
 *
 * @version 1.0.0
 * @author Jrodriguez
 * @since 18/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Servicios",
        description = "Representa el objeto de la url del siga y las sedes"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaUrlBranch
{

    @ApiObjectField(name = "url", description = "url del siga", order = 1)
    private String url;
    @ApiObjectField(name = "id", description = "id de las sedes", order = 2)
    private int id;

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

}
