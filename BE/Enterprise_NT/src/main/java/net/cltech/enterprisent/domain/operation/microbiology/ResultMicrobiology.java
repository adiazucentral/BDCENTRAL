/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un resultado estadisticas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 12/12/2017
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Microbiologia",
        name = "Resultados de Microbiologia",
        description = "Representa resultados para estadisticas"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultMicrobiology
{

    @ApiObjectField(name = "idSensitivity", description = "Id antibiograma", required = false, order = 1)
    private Integer idSensitivity;
    @ApiObjectField(name = "idAntibiotic", description = "Id Antibiotico", required = true, order = 2)
    private Integer idAntibiotic;
    @ApiObjectField(name = "nameAntibiotic", description = "Nombre Antibiotico", required = true, order = 3)
    private String nameAntibiotic;
    @ApiObjectField(name = "line", description = "Linea de supresión", required = true, order = 4)
    private Integer line;
    @ApiObjectField(name = "referenceValue", description = "Valor de referencia del CMI", required = false, order = 5)
    private String referenceValue;
    @ApiObjectField(name = "unidad", description = "Unidad de medida", required = false, order = 6)
    private Integer unit;
    @ApiObjectField(name = "cmi", description = "CMI", required = false, order = 7)
    private String cmi;
    @ApiObjectField(name = "interpretationCMI", description = "Interpretación CMI", required = false, order = 8)
    private String interpretationCMI;
    @ApiObjectField(name = "cmiM", description = "CMI Manual", required = false, order = 9)
    private String cmiM;
    @ApiObjectField(name = "interpretationCMIM", description = "Interpretion CMI Manual", required = false, order = 10)
    private String interpretationCMIM;
    @ApiObjectField(name = "cmiMPrint", description = "Imprimir Interpretación CMI Manual en Resultados", required = true, order = 11)
    private boolean cmiMPrint;
    @ApiObjectField(name = "disk", description = "Disco", required = false, order = 12)
    private String disk;
    @ApiObjectField(name = "interpretationDisk", description = "Interpretion Disco", required = false, order = 13)
    private String interpretationDisk;
    @ApiObjectField(name = "diskPrint", description = "Imprimir Interpretación de Disco en Resultados", required = true, order = 14)
    private boolean diskPrint;
    @ApiObjectField(name = "dateCMI", description = "Fecha de actualización CMI", required = true, order = 15)
    private Date dateCMI;
    @ApiObjectField(name = "userCMI", description = "Usuario CMI", required = true, order = 17)
    private AuthorizedUser userCMI = new AuthorizedUser();
    @ApiObjectField(name = "dateCMIM", description = "Fecha de actualización CMI Manual", required = true, order = 15)
    private Date dateCMIM;
    @ApiObjectField(name = "userCMIM", description = "Usuario CMI", required = true, order = 17)
    private AuthorizedUser userCMIM = new AuthorizedUser();
    @ApiObjectField(name = "dateDisk", description = "Fecha de actualización por Disco", required = true, order = 15)
    private Date dateDisk;
    @ApiObjectField(name = "userDisk", description = "Usuario Disco", required = true, order = 17)
    private AuthorizedUser userDisk = new AuthorizedUser();
    @ApiObjectField(name = "selected", description = "Esta seleccionado", required = true, order = 16)
    private boolean selected;

    public Integer getIdSensitivity()
    {
        return idSensitivity;
    }

    public void setIdSensitivity(Integer idSensitivity)
    {
        this.idSensitivity = idSensitivity;
    }

    public Integer getLine()
    {
        return line;
    }

    public void setLine(Integer line)
    {
        this.line = line;
    }

    public Integer getIdAntibiotic()
    {
        return idAntibiotic;
    }

    public void setIdAntibiotic(Integer idAntibiotic)
    {
        this.idAntibiotic = idAntibiotic;
    }

    public String getNameAntibiotic()
    {
        return nameAntibiotic;
    }

    public void setNameAntibiotic(String nameAntibiotic)
    {
        this.nameAntibiotic = nameAntibiotic;
    }

    public String getReferenceValue()
    {
        return referenceValue;
    }

    public void setReferenceValue(String referenceValue)
    {
        this.referenceValue = referenceValue;
    }

    public Integer getUnit()
    {
        return unit;
    }

    public void setUnit(Integer unit)
    {
        this.unit = unit;
    }

    public String getCmi()
    {
        return cmi;
    }

    public void setCmi(String cmi)
    {
        this.cmi = cmi;
    }

    public String getInterpretationCMI()
    {
        return interpretationCMI;
    }

    public void setInterpretationCMI(String interpretationCMI)
    {
        this.interpretationCMI = interpretationCMI;
    }

    public String getCmiM()
    {
        return cmiM;
    }

    public void setCmiM(String cmiM)
    {
        this.cmiM = cmiM;
    }

    public String getInterpretationCMIM()
    {
        return interpretationCMIM;
    }

    public void setInterpretationCMIM(String interpretationCMIM)
    {
        this.interpretationCMIM = interpretationCMIM;
    }

    public boolean isCmiMPrint()
    {
        return cmiMPrint;
    }

    public void setCmiMPrint(boolean cmiMPrint)
    {
        this.cmiMPrint = cmiMPrint;
    }

    public String getDisk()
    {
        return disk;
    }

    public void setDisk(String disk)
    {
        this.disk = disk;
    }

    public String getInterpretationDisk()
    {
        return interpretationDisk;
    }

    public void setInterpretationDisk(String interpretationDisk)
    {
        this.interpretationDisk = interpretationDisk;
    }

    public boolean isDiskPrint()
    {
        return diskPrint;
    }

    public void setDiskPrint(boolean diskPrint)
    {
        this.diskPrint = diskPrint;
    }

    public Date getDateCMI()
    {
        return dateCMI;
    }

    public void setDateCMI(Date dateCMI)
    {
        this.dateCMI = dateCMI;
    }

    public AuthorizedUser getUserCMI()
    {
        return userCMI;
    }

    public void setUserCMI(AuthorizedUser userCMI)
    {
        this.userCMI = userCMI;
    }

    public Date getDateCMIM()
    {
        return dateCMIM;
    }

    public void setDateCMIM(Date dateCMIM)
    {
        this.dateCMIM = dateCMIM;
    }

    public AuthorizedUser getUserCMIM()
    {
        return userCMIM;
    }

    public void setUserCMIM(AuthorizedUser userCMIM)
    {
        this.userCMIM = userCMIM;
    }

    public Date getDateDisk()
    {
        return dateDisk;
    }

    public void setDateDisk(Date dateDisk)
    {
        this.dateDisk = dateDisk;
    }

    public AuthorizedUser getUserDisk()
    {
        return userDisk;
    }

    public void setUserDisk(AuthorizedUser userDisk)
    {
        this.userDisk = userDisk;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}
