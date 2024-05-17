/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author User
 */
@ApiObject(
        group = "Operación - Listados",
        name = "Examen de Laboratorio",
        description = "Representa un Examen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperTest {
    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 3)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura", required = true, order = 4)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 5)
    private String name;
    @ApiObjectField(name = "nameTestEnglish", description = "Nombre del examen en ingles", required = false, order = 6)
    private String nameTestEnglish;
    @ApiObjectField(name = "testType", description = "Tipo de la prueba: 0 -> Examen, 1 -> Perfil, 2 -> Paquete", required = true, order = 7)
    private Short testType;
    
    
    public SuperTest(){}
    
    
    public SuperTest(Integer id)
    {
        this.id = id;
    }
    
    
    public SuperTest(Integer id, String code, String abbr, String name)
    {
        this.id = id;
        this.code = code;
        this.abbr = abbr;
        this.name = name;
        this.nameTestEnglish = nameTestEnglish;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final SuperTest other = (SuperTest) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}
