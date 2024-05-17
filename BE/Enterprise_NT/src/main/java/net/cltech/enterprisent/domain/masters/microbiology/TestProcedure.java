/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.microbiology;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro relación prueba -
 * procedimiento
 *
 * @version 1.0.0
 * @author mmunoz
 * @since 15/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Microbiología",
        name = "Relación prueba-procedimiento",
        description = "Muestra la información del maestro relación prueba- procedimiento"
)

public class TestProcedure extends MasterAudit
{

    @ApiObjectField(name = "procedure", description = "Procedimiento", required = true, order = 1)
    private Procedure procedure;
    @ApiObjectField(name = "test", description = "Prueba", required = true, order = 2)
    private TestBasic test;

    public TestProcedure()
    {
        procedure = new Procedure();
        test = new TestBasic();
    }

    public Procedure getProcedure()
    {
        return procedure;
    }

    public void setProcedure(Procedure procedure)
    {
        this.procedure = procedure;
    }

    public TestBasic getTest()
    {
        return test;
    }

    public void setTest(TestBasic test)
    {
        this.test = test;
    }

}
