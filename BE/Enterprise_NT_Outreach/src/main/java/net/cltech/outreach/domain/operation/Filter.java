package net.cltech.outreach.domain.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un tipo de usuario de la aplicación
 *
 * @version 1.0.0
 * @author cmartin
 * @since 23/04/2018
 * @see Creacion
 */
@ApiObject(
        group = "Filtros",
        name = "Filtro - Lista de Ordenes",
        description = "Representa un filtro de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter
{
    @ApiObjectField(name = "order", description = "Orden", order = 1)
    private Long order;
    @ApiObjectField(name = "dateNumber", description = "Fecha YYYYmmDD", order = 2)
    private Integer dateNumber;
    @ApiObjectField(name = "dateNumberInit", description = "Fecha YYYYmmDD", order = 2)
    private Integer dateNumberInit;
    @ApiObjectField(name = "dateNumberEnd", description = "Fecha YYYYmmDD", order = 2)
    private Integer dateNumberEnd;
    @ApiObjectField(name = "documentType", description = "Tipo de Documento", order = 3)
    private Integer documentType;
    @ApiObjectField(name = "patientId", description = "Historia", order = 4)
    private String patientId;
    @ApiObjectField(name = "name1", description = "Nombre 1", order = 5)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2", order = 6)
    private String name2;
    @ApiObjectField(name = "lastName", description = "Primer Apellido", order = 7)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Segundo Apellido", order = 8)
    private String surName;
    @ApiObjectField(name = "year", description = "Año", order = 9)
    private Integer year;
    @ApiObjectField(name = "area", description = "Area", order = 10)
    private Integer area;
    @ApiObjectField(name = "onlyValidated", description = "Indica si el tipo de usuario solo puede ver pruebas validadas", required = true, order = 7)
    private boolean onlyValidated;

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Integer getDateNumber()
    {
        return dateNumber;
    }

    public void setDateNumber(Integer dateNumber)
    {
        this.dateNumber = dateNumber;
    }

    public Integer getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType(Integer documentType)
    {
        this.documentType = documentType;
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

    public Integer getYear()
    {
        return year;
    }

    public void setYear(Integer year)
    {
        this.year = year;
    }

    public Integer getDateNumberInit() {
        return dateNumberInit;
    }

    public void setDateNumberInit(Integer dateNumberInit) {
        this.dateNumberInit = dateNumberInit;
    }

    public Integer getDateNumberEnd() {
        return dateNumberEnd;
    }

    public void setDateNumberEnd(Integer dateNumberEnd) {
        this.dateNumberEnd = dateNumberEnd;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public boolean isOnlyValidated() {
        return onlyValidated;
    }

    public void setOnlyValidated(boolean onlyValidated) {
        this.onlyValidated = onlyValidated;
    }
    
}
