/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.access;

import java.util.List;

/**
 * Representa un modulo de la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 03/04/2017
 * @see Creacion
 */
public class Module
{

    private int code;
    private boolean canAccess;
    private List<SubModule> subModules;

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public boolean isCanAccess()
    {
        return canAccess;
    }

    public void setCanAccess(boolean canAccess)
    {
        this.canAccess = canAccess;
    }

    public List<SubModule> getSubModules()
    {
        return subModules;
    }

    public void setSubModules(List<SubModule> subModules)
    {
        this.subModules = subModules;
    }

}
