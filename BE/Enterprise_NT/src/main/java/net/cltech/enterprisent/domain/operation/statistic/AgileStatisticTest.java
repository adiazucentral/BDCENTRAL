package net.cltech.enterprisent.domain.operation.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la información de estadisticas rapidas: Sede - Pruebas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 04/04/2018
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas Rapidas",
        name = "Sede - Prueba",
        description = "Representa información de estadisticas rapidas: Sede - Pruebas"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgileStatisticTest
{

    @ApiObjectField(name = "date", description = "Fecha: MMDD", required = false, order = 1)
    private Integer date;
    @ApiObjectField(name = "idBranch", description = "Id Sede", required = false, order = 2)
    private Integer idBranch;
    @ApiObjectField(name = "codeBranch", description = "Codigo Sede", required = false, order = 3)
    private String codeBranch;
    @ApiObjectField(name = "nameBranch", description = "Nombre Sede", required = false, order = 4)
    private String nameBranch;
    @ApiObjectField(name = "idTest", description = "Id Prueba", required = false, order = 5)
    private Integer idTest;
    @ApiObjectField(name = "codeTest", description = "Codigo Prueba", required = false, order = 6)
    private String codeTest;
    @ApiObjectField(name = "nameTest", description = "Nombre Prueba", required = false, order = 7)
    private String nameTest;
    @ApiObjectField(name = "idArea", description = "Id Area", required = false, order = 8)
    private Integer idArea;
    @ApiObjectField(name = "codeArea", description = "Codigo Area", required = false, order = 9)
    private String codeArea;
    @ApiObjectField(name = "nameArea", description = "Nombre Area", required = false, order = 10)
    private String nameArea;
    @ApiObjectField(name = "orderEntry", description = "Ordenes Ingresadas", required = false, order = 11)
    private Integer orderEntry;
    @ApiObjectField(name = "entry", description = "Examenes Ingresados", required = false, order = 12)
    private Integer entry;
    @ApiObjectField(name = "validate", description = "Examenes Validados", required = false, order = 13)
    private Integer validate;
    @ApiObjectField(name = "print", description = "Examenes Impresos", required = false, order = 14)
    private Integer print;
    @ApiObjectField(name = "pathology", description = "Examenes Patologicos", required = false, order = 15)
    private Integer pathology;

    public AgileStatisticTest()
    {
    }

    public AgileStatisticTest(Integer date, Integer idBranch, String codeBranch, String nameBranch, Integer idTest, String codeTest, String nameTest, Integer entry, Integer validate, Integer print, Integer pathologic)
    {
        this.date = date;
        this.idBranch = idBranch;
        this.codeBranch = codeBranch;
        this.nameBranch = nameBranch;
        this.idTest = idTest;
        this.codeTest = codeTest;
        this.nameTest = nameTest;
        this.entry = entry;
        this.validate = validate;
        this.print = print;
        this.pathology = pathologic;
    }

    public Integer getDate()
    {
        return date;
    }

    public void setDate(Integer date)
    {
        this.date = date;
    }

    public Integer getIdBranch()
    {
        return idBranch;
    }

    public void setIdBranch(Integer idBranch)
    {
        this.idBranch = idBranch;
    }

    public String getCodeBranch()
    {
        return codeBranch;
    }

    public void setCodeBranch(String codeBranch)
    {
        this.codeBranch = codeBranch;
    }

    public String getNameBranch()
    {
        return nameBranch;
    }

    public void setNameBranch(String nameBranch)
    {
        this.nameBranch = nameBranch;
    }

    public Integer getIdTest()
    {
        return idTest;
    }

    public void setIdTest(Integer idTest)
    {
        this.idTest = idTest;
    }

    public String getCodeTest()
    {
        return codeTest;
    }

    public void setCodeTest(String codeTest)
    {
        this.codeTest = codeTest;
    }

    public String getNameTest()
    {
        return nameTest;
    }

    public void setNameTest(String nameTest)
    {
        this.nameTest = nameTest;
    }

    public Integer getOrderEntry()
    {
        return orderEntry;
    }

    public void setOrderEntry(Integer orderEntry)
    {
        this.orderEntry = orderEntry;
    }

    public Integer getEntry()
    {
        return entry;
    }

    public void setEntry(Integer entry)
    {
        this.entry = entry;
    }

    public Integer getValidate()
    {
        return validate;
    }

    public void setValidate(Integer validate)
    {
        this.validate = validate;
    }

    public Integer getPrint()
    {
        return print;
    }

    public void setPrint(Integer print)
    {
        this.print = print;
    }

    public Integer getPathology()
    {
        return pathology;
    }

    public void setPathology(Integer pathology)
    {
        this.pathology = pathology;
    }

    public Integer getIdArea()
    {
        return idArea;
    }

    public void setIdArea(Integer idArea)
    {
        this.idArea = idArea;
    }

    public String getCodeArea()
    {
        return codeArea;
    }

    public void setCodeArea(String codeArea)
    {
        this.codeArea = codeArea;
    }

    public String getNameArea()
    {
        return nameArea;
    }

    public void setNameArea(String nameArea)
    {
        this.nameArea = nameArea;
    }

}
