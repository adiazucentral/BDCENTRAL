package net.cltech.enterprisent.domain.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Hojas de trabajo
 *
 * @version 1.0.0
 * @author eacuna
 * @since 25/10/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Filtro Examenes",
        description = "Muestra informacion de FIltro de Examenes"
)
public class TestFilter extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del grupo", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código para busquedas", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del filtro", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "tests", description = "Pruebas", required = true, order = 4)
    private List<TestBasic> tests;
    @ApiObjectField(name = "state", description = "Estado del filtro", required = true, order = 3)
    private Boolean state;
    @ApiObjectField(name = "idTest", description = "Id del examen", required = true, order = 1)
    private int idTest;

    public Boolean getState()
    {
        return state;
    }

    public void setState(Boolean state)
    {
        this.state = state;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<TestBasic> getTests()
    {
        return tests;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }
    
    public TestFilter setTests(List<TestBasic> tests)
    {
        this.tests = tests;
        return this;
    }

}
