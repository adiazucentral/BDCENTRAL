package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.PatientNT;
import net.cltech.enterprisent.domain.integration.homebound.DemographicHomeBound;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.tools.mappers.MigrationMapper;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa a un paciente de la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 30/06/2017
 * @see Creacion
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Paciente",
        description = "Representa un paciente dentro de la aplicación"
)
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Patient
{

    @ApiObjectField(name = "id", description = "Identificador", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 2)
    private String patientId;
    @ApiObjectField(name = "name1", description = "Nombre 1", required = true, order = 3)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2", required = false, order = 4)
    private String name2;
    @ApiObjectField(name = "lastName", description = "Apellido 1", required = true, order = 5)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Apellido 2", required = false, order = 6)
    private String surName;
    @ApiObjectField(name = "sex", description = "Sexo", required = false, order = 7)
    private Item sex = new Item();
    @ApiObjectField(name = "birthday", description = "Fecha Nacimiento", required = false, order = 8)
    private Date birthday;
    @ApiObjectField(name = "email", description = "Correo Electronico", required = false, order = 9)
    private String email;
    @ApiObjectField(name = "size", description = "Talla", required = false, order = 10)
    private BigDecimal size;
    @ApiObjectField(name = "weight", description = "Peso", required = false, order = 11)
    private BigDecimal weight;
    @ApiObjectField(name = "dateOfDeath", description = "Fecha Fallecimiento", required = false, order = 12)
    private Date dateOfDeath;
    @ApiObjectField(name = "lastUpdateDate", description = "Fecha Ultima Modificación", required = false, order = 13)
    private Date lastUpdateDate;
    @ApiObjectField(name = "lastUpdateUser", description = "Usuario Ultima Modificación", required = false, order = 14)
    private User lastUpdateUser;
    @ApiObjectField(name = "race", description = "Raza", required = false, order = 15)
    private Race race = new Race();
    @ApiObjectField(name = "documentType", description = "Tipo Documento", required = false, order = 16)
    private DocumentType documentType = new DocumentType();
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 17)
    private List<DemographicValue> demographics = new ArrayList<>(0);
    @ApiObjectField(name = "diagnostic", description = "Diagnostico Permanente", required = false, order = 18)
    private List<CommentOrder> diagnostic = new ArrayList<>();

    @ApiObjectField(name = "orderNumber", description = "Número de la orden", required = false, order = 19)
    private Long orderNumber;
    @ApiObjectField(name = "orders", description = "Ordenes", required = false, order = 20)
    private List<Order> orders = new ArrayList<>();
    @ApiObjectField(name = "photo", description = "Foto del Paciente", required = false, order = 21)
    private String photo;
    @ApiObjectField(name = "phone", description = "Telefono del Paciente", required = false, order = 22)
    private String phone;
    @ApiObjectField(name = "address", description = "Dirección del Paciente", required = false, order = 23)
    private String address;
    @ApiObjectField(name = "firstDate", description = "Fecha creacion", required = false, order = 24)
    private Date firstDate;
    @ApiObjectField(name = "firstUser", description = "Usuario creacion", required = false, order = 25)
    private User firstUser = new User();
    //@ApiObjectField(name = "firstUser", description = "Usuario creacion", required = false, order = 25)
    private List<TestBasic> listAllTestsOfPatient = new ArrayList<>();
    @ApiObjectField(name = "birthDayFormat", description = "fecha de naciemento en formato especifico", required = false, order = 26)
    private String birthDayFormat;
    @ApiObjectField(name = "updatePatient", description = "indicador para saber si se actualiza o no el paciente", required = false, order = 27)
    private Boolean updatePatient;
    @ApiObjectField(name = "passwordWebQuery", description = "contraseña de la consulta web", required = false, order = 27)
    private String passwordWebQuery;
    @ApiObjectField(name = "isHomebound", description = "Me indica si el paciente es de homebound o no", required = false, order = 28)
    private boolean isHomebound;
    @ApiObjectField(name = "demographicsHomebound", description = "Demograficos de homebound", required = false, order = 29)
    private List<DemographicHomeBound> demographicsHomebound = new ArrayList<>(0);
    @ApiObjectField(name = "diagnosis", description = "Diagnostico permanente proveniente de homebound", required = true, order = 30)
    private String diagnosis;
    @ApiObjectField(name = "status", description = "Estado del paciente. 0 -> creado.  1 -> verificado. 2 -> verifico genero inconsistencias. 3 -> no se consulto en el tribunal.", required = false, order = 31)
    private Integer status;
    @ApiObjectField(name = "dataTribunal", description = "Datos del tribunal", required = false, order = 32)
    private String dataTribunal;
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 29)
    private Branch branch;
    @ApiObjectField(name = "sampleprovenance", description = "Proveniencia de la muestra", required = false, order = 30)
    private String sampleprovenance;

    public Patient()
    {

    }

    public Patient(String patientId)
    {
        this.patientId = patientId;
    }

    public Patient(PatientNT patient)
    {
        this.name1 = patient.getName();
        this.lastName = patient.getLastName();
        this.surName = patient.getSecondLastName();
        //this.id = Integer.parseInt(patient.getRecord());
        this.patientId = patient.getRecord();
        SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yy");
        try
        {
            this.birthday = dateParser.parse(patient.getBirthDate());

        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        demographics = MigrationMapper.toDtoDemo(patient.getDemographics());

    }

    public Patient(Integer id)
    {
        this.id = id;
    }

    public Patient(Integer id, String patientId, List<CommentOrder> diagnostic)
    {
        this.id = id;
        this.patientId = patientId;
        this.diagnostic = diagnostic;
        this.sex = null;
        this.race = null;
        this.documentType = null;
    }

    public Patient(String record, String name, String lastName, String secondLastName, int gender, Date birthDate, String comment)
    {

        this.patientId = record;
        this.name1 = name;
        this.lastName = lastName;
        this.surName = secondLastName;
        this.sex = new Item(gender);
        this.birthday = birthDate;

    }

    public Patient setOrders(List<Order> orders)
    {
        this.orders = orders;
        return this;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
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
        final Patient other = (Patient) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}
