/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.user;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa el filtro por sede y areas para los usuarios
 *
 * @version 1.0.0
 * @author equijano
 * @since 22/10/2019
 * @see Creacion
 */
@ApiObject(
        group = "Usuario",
        name = "Uusuario por sede y areas",
        description = "Representa el filtro por sede y areas para los usuarios"
)
public class UserByBranchByAreas
{

    @ApiObjectField(name = "areas", description = "Id's de las areas en las que debe estar asociado un usuario", required = true, order = 1)
    private List<Integer> areas;
    @ApiObjectField(name = "idbranch", description = "Id de la sede", required = true, order = 2)
    private Integer idbranch;

    public List<Integer> getAreas()
    {
        return areas;
    }

    public void setAreas(List<Integer> areas)
    {
        this.areas = areas;
    }

    public Integer getIdbranch()
    {
        return idbranch;
    }

    public void setIdbranch(Integer idbranch)
    {
        this.idbranch = idbranch;
    }

}
