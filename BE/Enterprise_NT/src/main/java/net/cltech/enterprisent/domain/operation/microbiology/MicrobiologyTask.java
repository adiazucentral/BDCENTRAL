/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.microbiology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.masters.microbiology.MicrobiologyDestination;
import net.cltech.enterprisent.domain.masters.microbiology.Task;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa las tareas de medios de cultivo y procedimientos en microbiologia
 *
 * @version 1.0.0
 * @author cmartin
 * @since 15/02/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Microbiologia",
        name = "Microbiologia - Tareas",
        description = "Representa las tareas de microbiologia de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MicrobiologyTask extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "order", description = "Orden", required = false, order = 2)
    private Long order;
    @ApiObjectField(name = "patient", description = "Orden", required = false, order = 3)
    private Patient patient = new Patient();
    @ApiObjectField(name = "idTest", description = "Id del Examen", required = false, order = 4)
    private Integer idTest;
    @ApiObjectField(name = "abbrTest", description = "Abreviatura del Examen", required = false, order = 5)
    private String abbrTest;
    @ApiObjectField(name = "nameTest", description = "Nombre del Examen", required = false, order = 6)
    private String nameTest;
    @ApiObjectField(name = "idRecord", description = "Id del Medio de Cultivo o Procedimiento", required = false, order = 7)
    private Integer idRecord;
    @ApiObjectField(name = "destination", description = "Destino de Microbiologia", required = false, order = 8)
    private MicrobiologyDestination destination;
    @ApiObjectField(name = "comment", description = "Comentario", required = false, order = 9)
    private String comment;
    @ApiObjectField(name = "type", description = "Indica si el registro de tareas es para el medio de cultivo o el procedimiento. \n 1 -> Medio de Cultivo, 2 -> Procedimiento", required = true, order = 10)
    private Short type;
    @ApiObjectField(name = "tasks", description = "Tareas", required = true, order = 11)
    private List<Task> tasks = new ArrayList<>();
    @ApiObjectField(name = "reported", description = "Reportado", required = true, order = 12)
    private boolean reported;
    @ApiObjectField(name = "dateRegister", description = "Fecha de registro", required = true, order = 13)
    private Date dateRegister;
    @ApiObjectField(name = "userRegister", description = "Usuario registra", required = true, order = 14)
    private AuthorizedUser userRegister = new AuthorizedUser();

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Patient getPatient()
    {
        return patient;
    }

    public void setPatient(Patient patient)
    {
        this.patient = patient;
    }

    public Integer getIdTest()
    {
        return idTest;
    }

    public void setIdTest(Integer idTest)
    {
        this.idTest = idTest;
    }

    public String getAbbrTest()
    {
        return abbrTest;
    }

    public void setAbbrTest(String abbrTest)
    {
        this.abbrTest = abbrTest;
    }

    public String getNameTest()
    {
        return nameTest;
    }

    public void setNameTest(String nameTest)
    {
        this.nameTest = nameTest;
    }
    
    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Integer getIdRecord()
    {
        return idRecord;
    }

    public void setIdRecord(Integer idRecord)
    {
        this.idRecord = idRecord;
    }

    public MicrobiologyDestination getDestination()
    {
        return destination;
    }

    public void setDestination(MicrobiologyDestination destination)
    {
        this.destination = destination;
    }

    public Short getType()
    {
        return type;
    }

    public void setType(Short type)
    {
        this.type = type;
    }

    public List<Task> getTasks()
    {
        return tasks;
    }

    public void setTasks(List<Task> tasks)
    {
        this.tasks = tasks;
    }

    public boolean isReported()
    {
        return reported;
    }

    public void setReported(boolean reported)
    {
        this.reported = reported;
    }

    public Date getDateRegister()
    {
        return dateRegister;
    }

    public void setDateRegister(Date dateRegister)
    {
        this.dateRegister = dateRegister;
    }

    public AuthorizedUser getUserRegister()
    {
        return userRegister;
    }

    public void setUserRegister(AuthorizedUser userRegister)
    {
        this.userRegister = userRegister;
    }

}
