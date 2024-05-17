package net.cltech.enterprisent.domain.integration.ingresoLIH;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Interface para integracion de interfaz de ingreso
 *
 * @version 1.0.0
 * @author BValero
 * @since 23/04/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Items",
        description = "DatosGenerales del un examen"
)
public class Items {

    @ApiObjectField(name = "cups", description = "Codigo CUPS", required = true, order = 1)
    private String cups;
    @ApiObjectField(name = "descripcion", description = "Descripcion del examen", required = true, order = 2)
    private String descripcion;
    @ApiObjectField(name = "Secuencia", description = "Secuencia del examen orden", required = true, order = 3)
    private String secuencia;
    @ApiObjectField(name = "idTest", description = "Id del examen", required = true, order = 4)
    private Integer idTest;

    public Items()
    {
    }

    public Items(String cups, String descripcion, String secuencia, int idTest)
    {
        this.cups = cups;
        this.descripcion = descripcion;
        this.secuencia = secuencia;
        this.idTest = idTest;
    }

    public String getCups()
    {
        return cups;
    }

    public void setCups(String cups)
    {
        this.cups = cups;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getSecuencia()
    {
        return secuencia;
    }

    public void setSecuencia(String secuencia)
    {
        this.secuencia = secuencia;
    }

    public Integer getIdTest()
    {
        return idTest;
    }

    public void setIdTest(Integer idTest)
    {
        this.idTest = idTest;
    }
}
