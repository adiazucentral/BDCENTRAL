/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la Detección Microbial de un examen
 *
 * @version 1.0.0
 * @author cmartin
 * @since 26/01/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Microbiologia",
        name = "Microbiologia - Siembra",
        description = "Representa una siembra de microbiologia de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MicrobialDetection
{

    @ApiObjectField(name = "order", description = "Orden", required = false, order = 1)
    private Long order;
    @ApiObjectField(name = "test", description = "Examen", required = false, order = 3)
    private Integer test;
    @ApiObjectField(name = "microorganisms", description = "Microorganismo", required = false, order = 4)
    private List<Microorganism> microorganisms = new ArrayList<>();
    @ApiObjectField(name = "user", description = "Usuarios", required = false, order = 5)
    private AuthorizedUser user = new AuthorizedUser();

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Integer getTest()
    {
        return test;
    }

    public void setTest(Integer test)
    {
        this.test = test;
    }

    public List<Microorganism> getMicroorganisms()
    {
        return microorganisms;
    }

    public void setMicroorganisms(List<Microorganism> microorganisms)
    {
        this.microorganisms = microorganisms;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

}
