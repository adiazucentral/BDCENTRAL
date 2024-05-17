package net.cltech.enterprisent.domain.integration.skl;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa las pruebas de una orden con consentimiento
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 09/06/2020
 * @see Creación
 */
@ApiObject(
        group = "SKL",
        name = "Prueba con base64",
        description = "Prueba con consentimiento informado en la orden con base 64"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestConsentBase64
{

    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la prueba", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 3)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre de la prueba", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "userId", description = "Id del usuario que registro el examen", required = true, order = 5)
    private Integer userId;
    @ApiObjectField(name = "userName", description = "Nombre del usuario que registro el examen", required = true, order = 6)
    private String userName;
    @ApiObjectField(name = "registrationDate", description = "Fecha de registro del examen", order = 7)
    private Timestamp registrationDate;
    @ApiObjectField(name = "document", description = "Documento del examen en base 64", required = true, order = 8)
    private String document;

    public TestConsentBase64()
    {
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

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public Timestamp getRegistrationDate()
    {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate)
    {
        this.registrationDate = registrationDate;
    }

    public String getDocument()
    {
        return document;
    }

    public void setDocument(String document)
    {
        this.document = document;
    }

}
