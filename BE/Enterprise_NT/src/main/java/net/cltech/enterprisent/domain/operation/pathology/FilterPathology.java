/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa filtros para las consultas en patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 28/04/2021
 * @see Creacion
 */
@ApiObject(
        group = "Patología",
        name = "Filtros",
        description = "Representa filtros para las consultas en patologia"
)
public class FilterPathology 
{
    @ApiObjectField(name = "initDate", description = "Fecha Inicial", required = true, order = 1)
    private Date initDate;
    @ApiObjectField(name = "endDate", description = "Fecha Final", required = true, order = 2)
    private Date endDate;
    @ApiObjectField(name = "firstCase", description = "Número de caso inicial", required = false, order = 3)
    private long firstCase;
    @ApiObjectField(name = "lastCase", description = "Número de caso final", required = false, order = 4)
    private long lastCase;
    @ApiObjectField(name = "areas", description = "Lista de identificadores de las areas", required = false, order = 5)
    private List<Integer> areas = new ArrayList<>();
    @ApiObjectField(name = "studyTypeList", description = "Lista de identificadores de tipos de estudios", required = false, order = 5)
    private List<Integer> studyTypeList = new ArrayList<>();
    @ApiObjectField(name = "init", description = "Fecha Inicial en Formato yyyymmdd", required = true, order = 6)
    private Integer init;
    @ApiObjectField(name = "end", description = "Fecha Final en Formato yyyymmdd", required = true, order = 7)
    private Integer end;
    @ApiObjectField(name = "status", description = "Lista de estados de los casetes", required = false, order = 8)
    private List<Integer> status = new ArrayList<>();

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getFirstCase() {
        return firstCase;
    }

    public void setFirstCase(long firstCase) {
        this.firstCase = firstCase;
    }

    public long getLastCase() {
        return lastCase;
    }

    public void setLastCase(long lastCase) {
        this.lastCase = lastCase;
    }

    public List<Integer> getStudyTypeList() {
        return studyTypeList;
    }

    public void setStudyTypeList(List<Integer> studyTypeList) {
        this.studyTypeList = studyTypeList;
    }

    public Integer getInit() {
        return init;
    }

    public void setInit(Integer init) {
        this.init = init;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public List<Integer> getAreas() {
        return areas;
    }

    public void setAreas(List<Integer> areas) {
        this.areas = areas;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }
}
