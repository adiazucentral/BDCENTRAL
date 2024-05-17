package net.cltech.enterprisent.domain.integration.ingreso;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Esta clase representa la el item de la peticion con los datos necesarios
 * para la impresion del codigo de barras
 * @author javila
 */
@ApiObject(
        group = "Integración",
        name = "Codigo de barras",
        description = "Este el item enviado con los datos necesarios para la impresion del codigo de barras"
)
public class RequestItemBarCode
{
    @ApiObjectField(name = "lab24C9", description = "Codigo de la muestra", required = true, order = 1)
    private String lab24C9;
    @ApiObjectField(name = "lab24C2", description = "Nombre de la muestra", required = true, order = 2)
    private String lab24C2;
    @ApiObjectField(name = "lab56C2", description = "Nombre del recipiente (tubo)", required = true, order = 3)
    private String lab56C2;
    @ApiObjectField(name = "lab24C3", description = "Número de etiquetas a imprimir", required = true, order = 4)
    private int lab24C3;
    @ApiObjectField(name = "lab43C4", description = "Abreviatura de area", required = true, order = 5)
    private String lab43C4;
    @ApiObjectField(name = "examenesEtiquetaAdicional", description = "Abreviatura de las muestras", required = true, order = 6)
    private String examenesEtiquetaAdicional;

    public RequestItemBarCode()
    {
    }

    public RequestItemBarCode(String lab24C9, String lab24C2, String lab56C2, int lab24C3, String lab43C4, String examenesEtiquetaAdicional)
    {
        this.lab24C9 = lab24C9;
        this.lab24C2 = lab24C2;
        this.lab56C2 = lab56C2;
        this.lab24C3 = lab24C3;
        this.lab43C4 = lab43C4;
        this.examenesEtiquetaAdicional = examenesEtiquetaAdicional;
    }

    public String getLab24C9()
    {
        return lab24C9;
    }

    public void setLab24C9(String lab24C9)
    {
        this.lab24C9 = lab24C9;
    }

    public String getLab24C2()
    {
        return lab24C2;
    }

    public void setLab24C2(String lab24C2)
    {
        this.lab24C2 = lab24C2;
    }

    public String getLab56C2()
    {
        return lab56C2;
    }

    public void setLab56C2(String lab56C2)
    {
        this.lab56C2 = lab56C2;
    }

    public int getLab24C3()
    {
        return lab24C3;
    }

    public void setLab24C3(int lab24C3)
    {
        this.lab24C3 = lab24C3;
    }

    public String getLab43C4()
    {
        return lab43C4;
    }

    public void setLab43C4(String lab43C4)
    {
        this.lab43C4 = lab43C4;
    }

    public String getExamenesEtiquetaAdicional()
    {
        return examenesEtiquetaAdicional;
    }

    public void setExamenesEtiquetaAdicional(String examenesEtiquetaAdicional)
    {
        this.examenesEtiquetaAdicional = examenesEtiquetaAdicional;
    }
    
    
}
