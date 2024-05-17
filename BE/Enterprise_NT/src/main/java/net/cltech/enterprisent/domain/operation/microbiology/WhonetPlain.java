package net.cltech.enterprisent.domain.operation.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los datos para la generacion del whonet
 *
 * @version 1.0.0
 * @author eacuna
 * @since 04/04/2018
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas",
        name = "Whonet",
        description = "Representa informacion para plano whonet"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WhonetPlain
{

    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 1)
    private String patientId;
    @ApiObjectField(name = "name1", description = "Nombre 1", required = true, order = 2)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2", required = false, order = 3)
    private String name2;
    @ApiObjectField(name = "lastName", description = "Apellido 1", required = true, order = 4)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Apellido 2", required = false, order = 5)
    private String surName;
    @ApiObjectField(name = "sex", description = "Sexo", required = false, order = 6)
    private String gender;
    @ApiObjectField(name = "documentType", description = "Tipo Documento", required = false, order = 7)
    private Integer documentType;
    @ApiObjectField(name = "documentTypeCode", description = "Código Tipo Documento", required = false, order = 8)
    private String documentTypeCode;
    @ApiObjectField(name = "documentTypeName", description = "Nombre Tipo Documento", required = false, order = 9)
    private String documentTypeName;
    @ApiObjectField(name = "dob", description = "Fecha de nacimiento", required = false, order = 10)
    private Date dob;
    @ApiObjectField(name = "age", description = "Edad paciente", required = false, order = 11)
    private Integer age;
    @ApiObjectField(name = "ageUnit", description = "Unidad de la edad dias(1), meses(2), años(3)", required = false, order = 12)
    private Integer ageUnit;
    @ApiObjectField(name = "department", description = "Nombre de la sede", required = false, order = 13)
    private String department;
    @ApiObjectField(name = "wardType", description = "Servicio ", required = false, order = 14)
    private String wardType;
    @ApiObjectField(name = "specNum", description = "Número de orden", required = false, order = 15)
    private String specNum;
    @ApiObjectField(name = "specDate", description = "Fecha de la orden", required = false, order = 16)
    private Date specDate;
    @ApiObjectField(name = "specType", description = "Código de sitio anatomico o submuestra segun configuracion general", required = false, order = 17)
    private String specType;
    @ApiObjectField(name = "organism", description = "Microorganismo", required = false, order = 18)
    private String organism;
    @ApiObjectField(name = "abx", description = "Antibiotico", required = false, order = 19)
    private String abx;
    @ApiObjectField(name = "mic", description = "CMI", required = false, order = 20)
    private String mic;
    @ApiObjectField(name = "thm", description = "THM", required = false, order = 21)
    private String thm;
    @ApiObjectField(name = "edta", description = "EDTA", required = false, order = 22)
    private String edta;
    @ApiObjectField(name = "apb", description = "APB", required = false, order = 23)
    private String apb;

    public WhonetPlain()
    {
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public String getName1()
    {
        return name1;
    }

    public void setName1(String name1)
    {
        this.name1 = name1;
    }

    public String getName2()
    {
        return name2;
    }

    public void setName2(String name2)
    {
        this.name2 = name2;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getSurName()
    {
        return surName;
    }

    public void setSurName(String surName)
    {
        this.surName = surName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public Integer getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType(Integer documentType)
    {
        this.documentType = documentType;
    }

    public String getDocumentTypeCode()
    {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode)
    {
        this.documentTypeCode = documentTypeCode;
    }

    public String getDocumentTypeName()
    {
        return documentTypeName;
    }

    public void setDocumentTypeName(String documentTypeName)
    {
        this.documentTypeName = documentTypeName;
    }

    public Date getDob()
    {
        return dob;
    }

    public void setDob(Date dob)
    {
        this.dob = dob;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getWardType()
    {
        return wardType;
    }

    public void setWardType(String wardType)
    {
        this.wardType = wardType;
    }

    public String getSpecNum()
    {
        return specNum;
    }

    public void setSpecNum(String specNum)
    {
        this.specNum = specNum;
    }

    public Date getSpecDate()
    {
        return specDate;
    }

    public void setSpecDate(Date specDate)
    {
        this.specDate = specDate;
    }

    public String getOrganism()
    {
        return organism;
    }

    public void setOrganism(String organism)
    {
        this.organism = organism;
    }

    public String getAbx()
    {
        return abx;
    }

    public void setAbx(String abx)
    {
        this.abx = abx;
    }

    public String getMic()
    {
        return mic;
    }

    public void setMic(String mic)
    {
        this.mic = mic;
    }

    public String getThm()
    {
        return thm;
    }

    public void setThm(String thm)
    {
        this.thm = thm;
    }

    public String getEdta()
    {
        return edta;
    }

    public void setEdta(String edta)
    {
        this.edta = edta;
    }

    public String getApb()
    {
        return apb;
    }

    public void setApb(String apb)
    {
        this.apb = apb;
    }

    public Integer getAgeUnit()
    {
        return ageUnit;
    }

    public void setAgeUnit(Integer ageUnit)
    {
        this.ageUnit = ageUnit;
    }

    public String getSpecType()
    {
        return specType;
    }

    public void setSpecType(String specType)
    {
        this.specType = specType;
    }

}
