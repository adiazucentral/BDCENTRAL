package net.cltech.enterprisent.domain.integration.resultados;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Esta clase representa la respuesta de los detalles de microorganismos,
 * antibiogramas y antibioticos
 * 
 * @version 1.0.0
 * @author javila
 * @since 03/03/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Detalle de microorganismos",
        description = "Este tendra el detalle de microorganismos, antibiogramas, y antibioticos"
)
public class ResponseDetailMicroorganisms
{
    @ApiObjectField(name = "mic", description = "Nombre del microorganismo", required = true, order = 1)
    private String mic;
    @ApiObjectField(name = "ant", description = "Nombre del antibiotico", required = true, order = 2)
    private String ant;
    @ApiObjectField(name = "res", description = "Interpretacion", required = true, order = 3)
    private String res;
    @ApiObjectField(name = "abName", description = "Nombre del antibiograma", required = true, order = 4)
    private String abName;
    @ApiObjectField(name = "comment", description = "Comentario de deteccion microbiana", required = true, order = 5)
    private String comment;
    @ApiObjectField(name = "cmi", description = "Resultado del antibiotico", required = true, order = 6)
    private String cmi;
    @ApiObjectField(name = "micId", description = "Id del microorganismo", required = true, order = 7)
    private int micId;

    public ResponseDetailMicroorganisms()
    {
    }

    public ResponseDetailMicroorganisms(String mic, String ant, String res, String abName, String comment, String cmi, int micId)
    {
        this.mic = mic;
        this.ant = ant;
        this.res = res;
        this.abName = abName;
        this.comment = comment;
        this.cmi = cmi;
        this.micId = micId;
    }
    
    public String getMic()
    {
        return mic;
    }

    public void setMic(String mic)
    {
        this.mic = mic;
    }

    public String getAnt()
    {
        return ant;
    }

    public void setAnt(String ant)
    {
        this.ant = ant;
    }

    public String getRes()
    {
        return res;
    }

    public void setRes(String res)
    {
        this.res = res;
    }

    public String getAbName()
    {
        return abName;
    }

    public void setAbName(String abName)
    {
        this.abName = abName;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getCmi()
    {
        return cmi;
    }

    public void setCmi(String cmi)
    {
        this.cmi = cmi;
    }

    public int getMicId()
    {
        return micId;
    }

    public void setMicId(int micId)
    {
        this.micId = micId;
    }
}
