/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.MacroscopyTemplate;
import net.cltech.enterprisent.domain.masters.user.User;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa las descripciones macroscopias de los casos de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 26/05/2021
 * @see Creacion
 */
@ApiObject(
        group = "Patología",
        name = "Macroscopia",
        description = "Representa las descripciones macroscopias de los casos de patologia"
)
public class Macroscopy 
{
    @ApiObjectField(name = "id", description = "Identificador", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "casePat", description = "Caso", required = false, order = 2)
    private Integer casePat;
    @ApiObjectField(name = "pathologist", description = "Patólogo", required = true, order = 3)
    private User pathologist = new User();
    @ApiObjectField(name = "createdAt", description = "Fecha de creación", required = true, order = 4)
    private Date createdAt;
    @ApiObjectField(name = "transcription", description = "Identifica si el caso necesita transcripción", order = 5)
    private Integer transcription;
    @ApiObjectField(name = "audio", description = "Audio", required = false, order = 6)
    private Audio audio = new Audio();
    @ApiObjectField(name = "templates", description = "Plantillas", required = true, order = 8)
    private List<MacroscopyTemplate> templates = new ArrayList<>();
    @ApiObjectField(name = "numberCase", description = "Identificador del caso", required = false, order = 9)
    private Long numberCase;
    @ApiObjectField(name = "studyTypeName", description = "Nombre del tipo de estudio", required = true, order = 10)
    private String studyTypeName;
    @ApiObjectField(name = "transcriber", description = "Transcriptor", required = true, order = 11)
    private User transcriber = new User();
    @ApiObjectField(name = "transcribedAt", description = "Fecha de transcripción", required = true, order = 12)
    private Date transcribedAt;
    @ApiObjectField(name = "numberOrder", description = "Identificador de la orden", required = false, order = 13)
    private Long numberOrder;
    @ApiObjectField(name = "draft", description = "Borrador", required = false, order = 14)
    private Integer draft;
    @ApiObjectField(name = "authorization", description = "Identifica si la transcripción necesita autorización", order = 15)
    private Integer authorization;
    @ApiObjectField(name = "authorizer", description = "Persona que autoriza la transcripción", required = true, order = 16)
    private User authorizer = new User();
    @ApiObjectField(name = "authorizedAt", description = "Fecha de autorización", required = true, order = 17)
    private Date authorizedAt;
    @ApiObjectField(name = "studyTypeCode", description = "Codigo del tipo de estudio", required = true, order = 11)
    private String studyTypeCode;
    
   
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCasePat() {
        return casePat;
    }

    public void setCasePat(Integer casePat) {
        this.casePat = casePat;
    }

    public User getPathologist() {
        return pathologist;
    }

    public void setPathologist(User pathologist) {
        this.pathologist = pathologist;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTranscription() {
        return transcription;
    }

    public void setTranscription(Integer transcription) {
        this.transcription = transcription;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public List<MacroscopyTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(List<MacroscopyTemplate> templates) {
        this.templates = templates;
    }

    public Long getNumberCase() {
        return numberCase;
    }

    public void setNumberCase(Long numberCase) {
        this.numberCase = numberCase;
    }

    public String getStudyTypeName() {
        return studyTypeName;
    }

    public void setStudyTypeName(String studyTypeName) {
        this.studyTypeName = studyTypeName;
    }

    public User getTranscriber() {
        return transcriber;
    }

    public void setTranscriber(User transcriber) {
        this.transcriber = transcriber;
    }

    public Date getTranscribedAt() {
        return transcribedAt;
    }

    public void setTranscribedAt(Date transcribedAt) {
        this.transcribedAt = transcribedAt;
    }

    public Long getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(Long numberOrder) {
        this.numberOrder = numberOrder;
    }

    public Integer getDraft() {
        return draft;
    }

    public void setDraft(Integer draft) {
        this.draft = draft;
    }

    public Integer getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Integer authorization) {
        this.authorization = authorization;
    }

    public User getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(User authorizer) {
        this.authorizer = authorizer;
    }

    public Date getAuthorizedAt() {
        return authorizedAt;
    }

    public void setAuthorizedAt(Date authorizedAt) {
        this.authorizedAt = authorizedAt;
    }

    public String getStudyTypeCode() {
        return studyTypeCode;
    }

    public void setStudyTypeCode(String studyTypeCode) {
        this.studyTypeCode = studyTypeCode;
    }
}
