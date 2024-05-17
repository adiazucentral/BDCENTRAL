/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;

/**
 * Entidad de un paciente
 *
 * @author oarango
 * @since 2022-04-14
 * @see Creacion
 */
@Data
public class PatientIngresoOrder
{
    private int id;
    private String record;
    private String name1;
    private String name2;
    private String lastName;
    private String secondLastName;
    private int gender;
    private String genderSp;
    private String genderEn;
    private Date birthDate;
    private String email;
    private BigDecimal size;
    private BigDecimal weight;
    private Date deathDate;
    private DemographicIngresoOrder race;
    private DemographicIngresoOrder documentType;
    private List<DemographicIngresoOrder> demographics;
    private List<CommentIngresoOrder> comments;
    private String phone;
    private String address;
    private String passwordOutreach;
    private Date creationDate;
    private int userId;

    public PatientIngresoOrder()
    {
        demographics = new LinkedList<>();
    }

    public PatientIngresoOrder(int id)
    {
        this();
        this.id = id;
    }

    public PatientIngresoOrder(String record, String name1, String lastName, String secondLastName, int gender, Date birthDate, String comment)
    {
        this();
        this.record = record;
        this.name1 = name1;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.comments.add(new CommentIngresoOrder(2, comment));
    }
}
