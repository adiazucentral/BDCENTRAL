/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.ingreso;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la respuesta de muestras 
 * de la busqueda por su id de orden, id de la prueba
 * 
 * @version 1.0.0
 * @author javila
 * @since 17/02/2020
 * @see Creación
 */
@ApiObject(
    group = "Integración",
    name = "Item muestras",
    description = "respuesta de muestras de la busqueda por id de orden, y id de la prueba"
)
public class ResponsItemSample
{
    @ApiObjectField(name = "idSample", description = "Id de la muestra", required = true, order = 1)
    private int idSample;
    @ApiObjectField(name = "lab24c9", description = "Codigo de la muestra", required = true, order = 2)
    private String lab24c9;
    @ApiObjectField(name = "lab24c2", description = "Nombre de la muestra", required = true, order = 3)
    private String lab24c2;
    @ApiObjectField(name = "lab56c2", description = "Nombre del recipiente (Tubo)", required = true, order = 4)
    private String lab56c2;
    @ApiObjectField(name = "lab24c3", description = "El número de las etiquetas para imprimir", required = true, order = 5)
    private int lab24c3;
    @ApiObjectField(name = "lab43c4", description = "Abreviatura de los examenes por muestra", required = true, order = 6)
    private String lab43c4;
    @ApiObjectField(name = "ExamenesEtiquetaAdicional", description = "Abreviatura de todos los examenes de la orden", required = true, order = 7)
    private String ExamenesEtiquetaAdicional;
    @ApiObjectField(name = "AbreviaturaExamen", description = "Abreviatura examen", required = true, order = 8)
    private String AbreviaturaExamen;
    
    public ResponsItemSample()
    {
    }

    public ResponsItemSample(int idSample, String lab24c9, String lab24c2, String lab56c2, int lab24c3, String lab43c4, String ExamenesEtiquetaAdicional, String AbreviaturaExamen)
    {
        this.idSample = idSample;
        this.lab24c9 = lab24c9;
        this.lab24c2 = lab24c2;
        this.lab56c2 = lab56c2;
        this.lab24c3 = lab24c3;
        this.lab43c4 = lab43c4;
        this.ExamenesEtiquetaAdicional = ExamenesEtiquetaAdicional;
        this.AbreviaturaExamen = AbreviaturaExamen;
    }

    public int getIdSample()
    {
        return idSample;
    }

    public void setIdSample(int idSample)
    {
        this.idSample = idSample;
    }

    public String getLab24c9()
    {
        return lab24c9;
    }

    public void setLab24c9(String lab24c9)
    {
        this.lab24c9 = lab24c9;
    }

    public String getLab24c2()
    {
        return lab24c2;
    }

    public void setLab24c2(String lab24c2)
    {
        this.lab24c2 = lab24c2;
    }

    public String getLab56c2()
    {
        return lab56c2;
    }

    public void setLab56c2(String lab56c2)
    {
        this.lab56c2 = lab56c2;
    }

    public int getLab24c3()
    {
        return lab24c3;
    }

    public void setLab24c3(int lab24c3)
    {
        this.lab24c3 = lab24c3;
    }

    public String getLab43c4()
    {
        return lab43c4;
    }

    public void setLab43c4(String lab43c4)
    {
        this.lab43c4 = lab43c4;
    }

    public String getExamenesEtiquetaAdicional()
    {
        return ExamenesEtiquetaAdicional;
    }

    public void setExamenesEtiquetaAdicional(String ExamenesEtiquetaAdicional)
    {
        this.ExamenesEtiquetaAdicional = ExamenesEtiquetaAdicional;
    }

    public String getAbreviaturaExamen()
    {
        return AbreviaturaExamen;
    }

    public void setAbreviaturaExamen(String AbreviaturaExamen)
    {
        this.AbreviaturaExamen = AbreviaturaExamen;
    }
}
