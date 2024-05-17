package net.cltech.enterprisent.domain.masters.microbiology;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import net.cltech.enterprisent.domain.operation.microbiology.ResultMicrobiology;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Microorganismo
 *
 * @version 1.0.0
 * @author eacuna
 * @since 20/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Microbiología",
        name = "Microorganismo",
        description = "Muestra informacion del maestro Microorganismo que usa el API"
)
public class Microorganism extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id ", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Estado de la tarea", required = true, order = 3)
    private boolean state;
    @ApiObjectField(name = "selected", description = "Si esta seleccionado para un antibiograma", required = true, order = 4)
    private boolean selected;
    @ApiObjectField(name = "sensitivityAssing", description = "Si tiene un antibiograma configurado", required = true, order = 4)
    private boolean sensitivityAssing;
    @ApiObjectField(name = "sensitivity", description = "Antibiograma", required = true, order = 5)
    private Sensitivity sensitivity;
    @ApiObjectField(name = "test", description = "Id del examen", required = true, order = 5)
    private Integer test;
    @ApiObjectField(name = "comment", description = "Comentario de la Detección Microbial", required = true, order = 7)
    private String comment;
    @ApiObjectField(name = "idMicrobialDetection", description = "Id de la Detección Microbiana", required = true, order = 1)
    private Integer idMicrobialDetection;
    @ApiObjectField(name = "resultsMicrobiology", description = "Resultado de antibioticos de microbiologia", required = true, order = 1)
    private List<ResultMicrobiology> resultsMicrobiology = new ArrayList<>();
    @ApiObjectField(name = "antibiotics", description = "Lista de Antibioticos", required = true, order = 1)
    private List<Antibiotic> antibiotics;
    @ApiObjectField(name = "recount", description = "Comentario de recuentos", required = false, order = 8)
    private String recount;
    @ApiObjectField(name = "complementations", description = "Comentario de complementaciones", required = false, order = 8)
    private String complementations;
    @ApiObjectField(name = "countResultMicrobiology", description = "cantida de antibioticos relacionados al microorganismo", required = true, order = 5)
    private Integer countResultMicrobiology;
    
    public Microorganism()
    {
        sensitivity = new Sensitivity();
    }

    public String getRecount()
    {
        return recount;
    }

    public void setRecount(String recount)
    {
        this.recount = recount;
    }

    public String getComplementations()
    {
        return complementations;
    }

    public void setComplementations(String complementations)
    {
        this.complementations = complementations;
    }

    public Microorganism(Integer id, String name)
    {
        this.id = id;
        this.name = name;
        sensitivity = new Sensitivity();
    }

    public Microorganism(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public boolean isSensitivityAssing()
    {
        return sensitivityAssing;
    }

    public void setSensitivityAssing(boolean sensitivityAssing)
    {
        this.sensitivityAssing = sensitivityAssing;
    }

    public Sensitivity getSensitivity()
    {
        return sensitivity;
    }

    public void setSensitivity(Sensitivity sensitivity)
    {
        this.sensitivity = sensitivity;
    }

    public Integer getTest()
    {
        return test;
    }

    public void setTest(Integer test)
    {
        this.test = test;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Integer getIdMicrobialDetection()
    {
        return idMicrobialDetection;
    }

    public void setIdMicrobialDetection(Integer idMicrobialDetection)
    {
        this.idMicrobialDetection = idMicrobialDetection;
    }

    public List<ResultMicrobiology> getResultsMicrobiology()
    {
        return resultsMicrobiology;
    }

    public void setResultsMicrobiology(List<ResultMicrobiology> resultsMicrobiology)
    {
        this.resultsMicrobiology = resultsMicrobiology;
    }

    public List<Antibiotic> getAntibiotics()
    {
        return antibiotics;
    }

    public void setAntibiotics(List<Antibiotic> antibiotics)
    {
        this.antibiotics = antibiotics;
    }

    public Integer getCountResultMicrobiology() {
        return countResultMicrobiology;
    }

    public void setCountResultMicrobiology(Integer countResultMicrobiology) {
        this.countResultMicrobiology = countResultMicrobiology;
    }
    
    

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.name == null ? "" : this.name.toLowerCase());
        return hash;
    }

//    @Override
//    public boolean equals(Object obj)
//    {
//        if (this == obj)
//        {
//            return true;
//        }
//        if (obj == null)
//        {
//            return false;
//        }
//        if (getClass() != obj.getClass())
//        {
//            return false;
//        }
//        final Microorganism other = (Microorganism) obj;
//        if (Objects.equals(this.getName(), other.getName()))
//        {
//            return true;
//        }
//        return this.name.equalsIgnoreCase(other.name);
//    }
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
        final Microorganism other = (Microorganism) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}
