/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.masters.demographic;

import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de demografico consulta web
 * 
 * @version 1.0.0
 * @author adiaz
 * @since 03/02/2020
 * @see Creación
 */
public class DemographicWebQuery {
    @ApiObjectField(name = "id", description = "Id demografico consulta web", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "user", description = "Usuario demografico consulta web", required = true, order = 2)
    private String user;
    @ApiObjectField(name = "password", description = "Contraseña usuario demografico consulta web", required = true, order = 3)
    private String password;
    @ApiObjectField(name = "passwordExpirationDate", description = "Fecha de expiración de la contraseña", required = true, order = 4)
    private Date passwordExpirationDate;
    @ApiObjectField(name = "dateOfLastEntry", description = "Fecha del ultimo ingreso", required = true, order = 5)
    private Date dateOfLastEntry;
    @ApiObjectField(name = "numberFailedAttempts", description = "Cantidad de intentos fallidos", required = true, order = 6)
    private int numberFailedAttempts;
    @ApiObjectField(name = "demographic", description = "Demografico", required = true, order = 7)
    private Integer demographic;
    @ApiObjectField(name = "idDemographicItem", description = "Id item demografico", required = true, order = 8)
    private Integer idDemographicItem;
    @ApiObjectField(name = "state", description = "Estado activo(1) o inactivo(0)", required = true, order = 9)
    private boolean state;

    public DemographicWebQuery() {
    }

    public DemographicWebQuery(Integer id, String user, String password, Date passwordExpirationDate, Date dateOfLastEntry, int numberFailedAttempts, Integer demographic, Integer idDemographicItem) {
        this.id = id;
        this.user = user;
        this.password = password;
        this.passwordExpirationDate = passwordExpirationDate;
        this.dateOfLastEntry = dateOfLastEntry;
        this.numberFailedAttempts = numberFailedAttempts;
        this.demographic = demographic;
        this.idDemographicItem = idDemographicItem;
    }

    public Integer getIdDemographicItem() {
        return idDemographicItem;
    }

    public void setIdDemographicItem(Integer idDemographicItem) {
        this.idDemographicItem = idDemographicItem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getPasswordExpirationDate() {
        return passwordExpirationDate;
    }

    public void setPasswordExpirationDate(Date passwordExpirationDate) {
        this.passwordExpirationDate = passwordExpirationDate;
    }

    public Date getDateOfLastEntry() {
        return dateOfLastEntry;
    }

    public void setDateOfLastEntry(Date dateOfLastEntry) {
        this.dateOfLastEntry = dateOfLastEntry;
    }

    public int getNumberFailedAttempts() {
        return numberFailedAttempts;
    }

    public void setNumberFailedAttempts(int numberFailedAttempts) {
        this.numberFailedAttempts = numberFailedAttempts;
    }

    public Integer getDemographic() {
        return demographic;
    }

    public void setDemographic(Integer demographic) {
        this.demographic = demographic;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
