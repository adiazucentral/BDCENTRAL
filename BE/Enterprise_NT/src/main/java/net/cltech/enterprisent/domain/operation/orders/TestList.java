/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import net.cltech.enterprisent.domain.operation.test.SuperArea;
import net.cltech.enterprisent.domain.operation.test.SuperLaboratory;
import net.cltech.enterprisent.domain.operation.test.SuperSample;
import net.cltech.enterprisent.domain.operation.test.SuperTechnique;
import net.cltech.enterprisent.domain.operation.test.SuperUnit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que represent un resultado
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 5/09/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Listados",
        name = "Examen de Laboratorio",
        description = "Representa un Examen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestList extends SuperTest
{

    @ApiObjectField(name = "area", description = "Area ", required = true, order = 2)
    private SuperArea area;
    @ApiObjectField(name = "unit", description = "Unidad", required = false, order = 7)
    private SuperUnit unit;
    @ApiObjectField(name = "sample", description = "Muestra", required = true, order = 8)
    private SuperSample sample;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 9)
    private SuperResult result;
    @ApiObjectField(name = "panel", description = "Perfil del examen", required = true, order = 10)
    private SuperTest panel;
    @ApiObjectField(name = "pack", description = "Paquete", required = true, order = 11)
    private SuperTest pack;
    @ApiObjectField(name = "confidential", description = "Identifica si es exámen confidencial", required = true, order = 12)
    private boolean confidential;
    @ApiObjectField(name = "laboratory", description = "Laboratorio de Referencia", required = false, order = 9)
    private SuperLaboratory laboratory;
    @ApiObjectField(name = "worklist", description = "Hoja de trabajo", required = false, order = 11)
    private Worklist worklist;
    @ApiObjectField(name = "technique", description = "Tecnica", required = false, order = 18)
    private SuperTechnique technique;
    @ApiObjectField(name = "historicGraphic", description = "Se muestran grafica de historicos en el reporte", required = true, order = 19)
    private boolean historicGraphic;
    @ApiObjectField(name = "gender", description = "Genero de la prueba", required = true, order = 9)
    private Item gender;
    @ApiObjectField(name = "testState", description = "Estado del examen para la orden Estado: 0 -> Ordenado, 1 -> Repeticion, 2 -> Reportado, 3 -> Preliminar, 4 -> Validado, 5 -> Impreso.", required = true, order = 13)
    private int testState;
    @ApiObjectField(name = "sampleState", description = "Estado Muestra:  0 -> Rechazado, 1 -> Nueva Muestra, 2 -> Ordenado, 3 -> Tomado, 4 -> Verificado.", required = true, order = 14)
    private int sampleState;
    @ApiObjectField(name = "viewquery", description = "Ver en consultas", required = true, order = 14)
    private int viewquery;
    @ApiObjectField(name = "resultType", description = "Tipo de Resultado: 1->Numerico, 2->Texto", required = false, order = 18)
    private Integer resultType;
    @ApiObjectField(name = "rackStore", description = "Nombre de la gradilla donde se almaceno", required = false, order = 37)
    private String rackStore;
    @ApiObjectField(name = "positionStore", description = "posicion donde fue almacenada", required = false, order = 38)
    private String positionStore;
    @ApiObjectField(name = "profile", description = "perfil", required = true, order = 41)
    private Integer profile;
    @ApiObjectField(name = "excluideTestProfile", description = "Excluir analistos del perfil", required = true, order = 21)
    private int excluideTestProfile;
    @ApiObjectField(name = "remission", description = "Remision", required = true, order = 23)
    private int remission;
    @ApiObjectField(name = "originRemission", description = "Laboratorio de Orgien Remisiones", required = false, order = 24)
    private SuperLaboratory originRemission;
    @ApiObjectField(name = "homologationCodes", description = "Códigos de homologación", required = true, order = 26)
    private String homologationCodes;
    @ApiObjectField(name = "resultComment", description = "Comentario del resultado", required = false, order = 27)
    private ResultTestComment resultComment;

    @ApiObjectField(name = "testPrice", description = "testPrice test", required = false, order = 27)
    private TestPrice testPrice;

    public TestList()
    {
        area = new SuperArea();
        unit = new SuperUnit();
        sample = new SuperSample();
        result = new SuperResult();
        laboratory = new SuperLaboratory();
        originRemission = new SuperLaboratory();
        resultComment = new ResultTestComment();
    }

    public TestList(TestBasic testBasic)
    {
        super(testBasic.getId(), testBasic.getCode(), testBasic.getAbbr(), testBasic.getName());
    }

    public TestList(Integer id)
    {
        super();
        area = new SuperArea();
        unit = new SuperUnit();
        sample = new SuperSample();
        result = new SuperResult();
        laboratory = new SuperLaboratory();
    }

    public SuperArea getArea()
    {
        return area;
    }

    public void setArea(SuperArea area)
    {
        this.area = area;
    }

    public SuperUnit getUnit()
    {
        return unit;
    }

    public void setUnit(SuperUnit unit)
    {
        this.unit = unit;
    }

    public SuperSample getSample()
    {
        return sample;
    }

    public void setSample(SuperSample sample)
    {
        this.sample = sample;
    }

    public SuperResult getResult()
    {
        return result;
    }

    public void setResult(SuperResult result)
    {
        this.result = result;
    }

    public SuperTest getPanel()
    {
        return panel;
    }

    public void setPanel(SuperTest panel)
    {
        this.panel = panel;
    }

    public SuperTest getPack()
    {
        return pack;
    }

    public void setPack(SuperTest pack)
    {
        this.pack = pack;
    }

    public boolean isConfidential()
    {
        return confidential;
    }

    public void setConfidential(boolean confidential)
    {
        this.confidential = confidential;
    }

    public SuperLaboratory getLaboratory()
    {
        return laboratory;
    }

    public void setLaboratory(SuperLaboratory laboratory)
    {
        this.laboratory = laboratory;
    }

    public Worklist getWorklist()
    {
        return worklist;
    }

    public void setWorklist(Worklist worklist)
    {
        this.worklist = worklist;
    }

    public SuperTechnique getTechnique()
    {
        return technique;
    }

    public void setTechnique(SuperTechnique technique)
    {
        this.technique = technique;
    }

    public boolean getHistoricGraphic()
    {
        return historicGraphic;
    }

    public void setHistoricGraphic(boolean historicGraphic)
    {
        this.historicGraphic = historicGraphic;
    }

    public Item getGender()
    {
        return gender;
    }

    public TestPrice getTestPrice()
    {
        return testPrice;
    }

    public void setTestPrice(TestPrice testPrice)
    {
        this.testPrice = testPrice;
    }

    public void setGender(Item gender)
    {
        this.gender = gender;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        return hash;
    }

    public int getTestState()
    {
        return testState;
    }

    public void setTestState(int testState)
    {
        this.testState = testState;
    }

    public int getSampleState()
    {
        return sampleState;
    }

    public void setSampleState(int sampleState)
    {
        this.sampleState = sampleState;
    }

    public Integer getResultType()
    {
        return resultType;
    }

    public void setResultType(Integer resultType)
    {
        this.resultType = resultType;
    }

    public String getRackStore()
    {
        return rackStore;
    }

    public void setRackStore(String rackStore)
    {
        this.rackStore = rackStore;
    }

    public String getPositionStore()
    {
        return positionStore;
    }

    public void setPositionStore(String positionStore)
    {
        this.positionStore = positionStore;
    }

    public Integer getProfile()
    {
        return profile;
    }

    public void setProfile(Integer profile)
    {
        this.profile = profile;
    }

    public int getExcluideTestProfile()
    {
        return excluideTestProfile;
    }

    public void setExcluideTestProfile(int excluideTestProfile)
    {
        this.excluideTestProfile = excluideTestProfile;
    }

    public int getRemission()
    {
        return remission;
    }

    public void setRemission(int remission)
    {
        this.remission = remission;
    }

    public SuperLaboratory getOriginRemission()
    {
        return originRemission;
    }

    public void setOriginRemission(SuperLaboratory originRemission)
    {
        this.originRemission = originRemission;
    }

    public int getViewquery()
    {
        return viewquery;
    }

    public void setViewquery(int viewquery)
    {
        this.viewquery = viewquery;
    }

    public String getHomologationCodes()
    {
        return homologationCodes;
    }

    public void setHomologationCodes(String homologationCodes)
    {
        this.homologationCodes = homologationCodes;
    }

    public ResultTestComment getResultComment()
    {
        return resultComment;
    }

    public void setResultComment(ResultTestComment resultComment)
    {
        this.resultComment = resultComment;
    }
}
