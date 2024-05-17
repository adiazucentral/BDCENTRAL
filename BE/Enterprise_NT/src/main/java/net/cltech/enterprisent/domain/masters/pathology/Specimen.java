/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import net.cltech.enterprisent.domain.masters.test.Container;
import net.cltech.enterprisent.domain.operation.pathology.Case;
import net.cltech.enterprisent.domain.operation.pathology.SamplePathology;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Clase que representa la informacion de las muestras con tipo de laboratorio Patologia
*
* @version 1.0.0
* @author omendez
* @since 08/04/2021
* @see Creación
*/
@ApiObject(
        group = "Patología",
        name = "Especimen",
        description = "Muestra informacion de las muestras con tipo de laboratorio Patologia"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Specimen extends PathologyAudit 
{

    @ApiObjectField(name = "id", description = "Id del especimen", order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de la muestra", order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "Estado de la muestra", order = 3)
    private boolean state;
    @ApiObjectField(name = "container", description = "Id del recipiente", order = 4)
    private Container container = new Container();
    @ApiObjectField(name = "code", description = "Codigo de la muestra", order = 5)
    private String code;
    @ApiObjectField(name = "subSamples", description = "Lista de sub-muestras asignadas ", order = 6)
    private List<Specimen> subSamples = new ArrayList<>(); 
    @ApiObjectField(name = "selected", description = "Si esta asignada la muestra", order = 7)
    private boolean selected;
    @ApiObjectField(name = "casePat", description = "Caso", required = true, order = 8)
    private Case casePat = new Case();
    @ApiObjectField(name = "samples", description = "Muestras", required = false, order = 10)
    private List<SamplePathology> samples = new ArrayList<>();
    @ApiObjectField(name = "studies", description = "Estudios", required = true, order = 11)
    private List<Study> studies = new ArrayList<>();
    @ApiObjectField(name = "organ", description = "Organo", required = true, order = 12)
    private Organ organ = new Organ();
    @ApiObjectField(name = "studyType", description = "Tipo de estudio", required = true, order = 13)
    private StudyType studyType = new StudyType();
    @ApiObjectField(name = "subSample", description = "Id de la submuestra", order = 14)
    private Integer subSample; 
    @ApiObjectField(name = "sample", description = "Id de la muestra patologica", order = 15)
    private Integer sample;
    @ApiObjectField(name = "subSampleName", description = "Nombre de la submuestra", order = 16)
    private String subSampleName;

    public Specimen() {
    }
    
    public Specimen(Integer id)
    {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
    
    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Specimen> getSubSamples() {
        return subSamples;
    }

    public void setSubSamples(List<Specimen> subSamples) {
        this.subSamples = subSamples;
    }
  
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Case getCasePat() {
        return casePat;
    }

    public void setCasePat(Case casePat) {
        this.casePat = casePat;
    }

    public List<SamplePathology> getSamples() {
        return samples;
    }

    public void setSamples(List<SamplePathology> samples) {
        this.samples = samples;
    }

    public List<Study> getStudies() {
        return studies;
    }

    public void setStudies(List<Study> studies) {
        this.studies = studies;
    }

    public Organ getOrgan() {
        return organ;
    }

    public void setOrgan(Organ organ) {
        this.organ = organ;
    }

    public StudyType getStudyType() {
        return studyType;
    }

    public void setStudyType(StudyType studyType) {
        this.studyType = studyType;
    }

    public Integer getSubSample() {
        return subSample;
    }

    public void setSubSample(Integer subSample) {
        this.subSample = subSample;
    }

    public Integer getSample() {
        return sample;
    }

    public void setSample(Integer sample) {
        this.sample = sample;
    }

    public String getSubSampleName() {
        return subSampleName;
    }

    public void setSubSampleName(String subSampleName) {
        this.subSampleName = subSampleName;
    }
}
