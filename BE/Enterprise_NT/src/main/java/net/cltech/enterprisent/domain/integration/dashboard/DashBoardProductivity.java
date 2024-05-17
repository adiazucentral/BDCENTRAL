/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una productividad para tableros.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 11/07/2018
 * @see Creación
 */
@ApiObject(
        group = "DashBoard",
        name = "Productividad",
        description = "Representa el objeto de productividad para los tableros."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashBoardProductivity
{

    @ApiObjectField(name = "idBranch", description = "Id la sede", order = 1)
    private Integer idBranch;
    @ApiObjectField(name = "idSection", description = "Id la sección", order = 2)
    private Integer idSection;
    @ApiObjectField(name = "branchName", description = "Nombre de la sede", order = 3)
    private String branchName;
    @ApiObjectField(name = "sectionName", description = "Nombre de la seccion", order = 4)
    private String sectionName;
    @ApiObjectField(name = "printed", description = "Cantidad examenes impresos", order = 5)
    private Integer printed = 0;
    @ApiObjectField(name = "results", description = "Cantidad examenes con resultado", order = 6)
    private Integer results = 0;
    @ApiObjectField(name = "validated", description = "Cantidad examenes validados", order = 7)
    private Integer validated = 0;
    @ApiObjectField(name = "verified", description = "Cantidad examenes verificados", order = 8)
    private Integer verified = 0;

    public DashBoardProductivity()
    {
    }

    public DashBoardProductivity(Integer idBranch, Integer idSection)
    {
        this.idBranch = idBranch;
        this.idSection = idSection;
    }

    public Integer getIdBranch()
    {
        return idBranch;
    }

    public void setIdBranch(Integer idBranch)
    {
        this.idBranch = idBranch;
    }

    public Integer getIdSection()
    {
        return idSection;
    }

    public void setIdSection(Integer idSection)
    {
        this.idSection = idSection;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    public String getSectionName()
    {
        return sectionName;
    }

    public void setSectionName(String sectionName)
    {
        this.sectionName = sectionName;
    }

    public Integer getPrinted()
    {
        return printed;
    }

    public void setPrinted(Integer printed)
    {
        this.printed = printed;
    }

    public Integer getResults()
    {
        return results;
    }

    public void setResults(Integer results)
    {
        this.results = results;
    }

    public Integer getValidated()
    {
        return validated;
    }

    public void setValidated(Integer validated)
    {
        this.validated = validated;
    }

    public Integer getVerified()
    {
        return verified;
    }

    public void setVerified(Integer verified)
    {
        this.verified = verified;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.idBranch);
        hash = 97 * hash + Objects.hashCode(this.idSection);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final DashBoardProductivity other = (DashBoardProductivity) obj;
        if (!Objects.equals(this.idBranch, other.idBranch))
        {
            return false;
        }
        if (!Objects.equals(this.idSection, other.idSection))
        {
            return false;
        }
        return true;
    }

}
