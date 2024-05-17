/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.access;

/**
 * Representa un submodulo de un modulo mayor de la solucion.
 *
 * @version 1.0.0
 * @author dcortes
 * @since 03/04/2017
 * @see Creación
 */
public class SubModule
{

    private int id;
    private String module;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getModule()
    {
        return module;
    }

    public void setModule(String module)
    {
        this.module = module;
    }

}
