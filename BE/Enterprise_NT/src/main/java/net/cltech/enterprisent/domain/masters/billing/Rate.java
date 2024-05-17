package net.cltech.enterprisent.domain.masters.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Bancos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 13/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Tarifa",
        description = "Muestra informacion del maestro Tarifa que usa el API"
)
@JsonInclude(Include.NON_NULL)
public class Rate extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la tarifa", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la tarifa", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la tarifa", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "address", description = "Dirección", required = true, order = 4)
    private String address;
    @ApiObjectField(name = "addAddress", description = "Dirección Adicional", required = true, order = 5)
    private String addAddress;
    @ApiObjectField(name = "city", description = "Ciudad del pagador", required = true, order = 6)
    private String city;
    @ApiObjectField(name = "department", description = "Departamento del pagador", required = true, order = 7)
    private String department;
    @ApiObjectField(name = "phone", description = "Telefono del pagador", required = true, order = 8)
    private String phone;
    @ApiObjectField(name = "email", description = "Correo del pagador", required = true, order = 9)
    private String email;
    @ApiObjectField(name = "postalCode", description = "Codigo Postal del pagador", required = true, order = 10)
    private String postalCode;
    @ApiObjectField(name = "webPage", description = "Pagina Web del pagador", required = true, order = 11)
    private String webPage;
    @ApiObjectField(name = "showPriceInEntry", description = "Ver Precio en Ingreso de Ordenes", required = true, order = 12)
    private boolean showPriceInEntry;
    @ApiObjectField(name = "checkPaid", description = "Verificar Pago", required = true, order = 12)
    private boolean checkPaid;
    @ApiObjectField(name = "typePayer", description = "Tipo del pagador: Aseguradora del estado -> 1, Aseguradora privada -> 2, Cuenta Privada -> 3, Particular -> 4, Otros -> 5", required = true, order = 13)
    private Integer typePayer;
    @ApiObjectField(name = "assingAllAccounts", description = "Asiganr a todas las cuentas", required = true, order = 14)
    private boolean assingAllAccounts;
    @ApiObjectField(name = "applyDiagnostics", description = "Aplicar Diagnosticos", required = true, order = 15)
    private boolean applyDiagnostics;
    @ApiObjectField(name = "checkCPTRelation", description = "Verificar Relacion Diagnosticos", required = true, order = 16)
    private boolean checkCPTRelation;
    @ApiObjectField(name = "homebound", description = "Aplica Homebound", required = true, order = 17)
    private boolean homebound;
    @ApiObjectField(name = "venipunture", description = "Aplica Venipunture", required = true, order = 18)
    private boolean venipunture;
    @ApiObjectField(name = "applyTypePayer", description = "Aplica a Tipo de Pagador", required = true, order = 19)
    private boolean applyTypePayer;
    @ApiObjectField(name = "claimCode", description = "Codigo de reclamacion", required = true, order = 20)
    private String claimCode;
    @ApiObjectField(name = "eligibility", description = "Aplica Elegibilidad", required = true, order = 21)
    private boolean eligibility;
    @ApiObjectField(name = "interchangeSender", description = "Interchange Sender", required = true, order = 22)
    private String interchangeSender;
    @ApiObjectField(name = "interchangeQualifier", description = "Interchange Qualifier", required = true, order = 23)
    private String interchangeQualifier;
    @ApiObjectField(name = "applicationSendCode", description = "Application Send Code", required = true, order = 24)
    private String applicationSendCode;
    @ApiObjectField(name = "labSubmitter", description = "Lab Submitter", required = true, order = 25)
    private String labSubmitter;
    @ApiObjectField(name = "identificationPayer", description = "Identificación del pagador", required = true, order = 26)
    private String identificationPayer;
    @ApiObjectField(name = "formatMemberId", description = "Formato Member ID", required = true, order = 27)
    private String formatMemberId;
    @ApiObjectField(name = "receiver", description = "Receptor", required = true, order = 28)
    private Integer receiver;
    @ApiObjectField(name = "consecutive", description = "Consecutivo del pagador", required = true, order = 29)
    private String consecutive;
    @ApiObjectField(name = "outputFileName", description = "Nombre de archivo de salida", required = true, order = 30)
    private String outputFileName;
    @ApiObjectField(name = "claimType", description = "Tipo de reclamación: Original -> 1, Corregido -> 2, Reemplazo -> 3 o Vacio", required = true, order = 31)
    private Integer claimType;
    @ApiObjectField(name = "transactionType", description = "Tipo de transacion: Prueba -> 1, Produccion -> 2", required = true, order = 32)
    private Integer transactionType;
    @ApiObjectField(name = "supplierSignature", description = "Firma del proveedor en archivo", required = true, order = 33)
    private boolean supplierSignature;
    @ApiObjectField(name = "assingBenefits", description = "Asignar Beneficios", required = true, order = 34)
    private boolean assingBenefits;
    @ApiObjectField(name = "electronicClaim", description = "Reclamacion Electronica", required = true, order = 35)
    private boolean electronicClaim;
    @ApiObjectField(name = "defaultItem", description = "Item por defecto", required = true, order = 36)
    private boolean defaultItem;
    @ApiObjectField(name = "state", description = "Estado del pagador", required = true, order = 37)
    private boolean state;
    @ApiObjectField(name = "observation", description = "observation", required = true, order = 38)
    private String observation;
    @ApiObjectField(name = "nameReceiver", description = "Nombre del receptor", required = false, order = 39)
    private String nameReceiver;

    public Rate()
    {
    }

    public Rate(Integer id)
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

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddAddress()
    {
        return addAddress;
    }

    public void setAddAddress(String addAddress)
    {
        this.addAddress = addAddress;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getWebPage()
    {
        return webPage;
    }

    public void setWebPage(String webPage)
    {
        this.webPage = webPage;
    }

    public boolean isShowPriceInEntry()
    {
        return showPriceInEntry;
    }

    public void setShowPriceInEntry(boolean showPriceInEntry)
    {
        this.showPriceInEntry = showPriceInEntry;
    }

    public boolean isCheckPaid()
    {
        return checkPaid;
    }

    public void setCheckPaid(boolean checkPaid)
    {
        this.checkPaid = checkPaid;
    }

    public Integer getTypePayer()
    {
        return typePayer;
    }

    public void setTypePayer(Integer typePayer)
    {
        this.typePayer = typePayer;
    }

    public boolean isAssingAllAccounts()
    {
        return assingAllAccounts;
    }

    public void setAssingAllAccounts(boolean assingAllAccounts)
    {
        this.assingAllAccounts = assingAllAccounts;
    }

    public boolean isApplyDiagnostics()
    {
        return applyDiagnostics;
    }

    public void setApplyDiagnostics(boolean applyDiagnostics)
    {
        this.applyDiagnostics = applyDiagnostics;
    }

    public boolean isCheckCPTRelation()
    {
        return checkCPTRelation;
    }

    public void setCheckCPTRelation(boolean checkCPTRelation)
    {
        this.checkCPTRelation = checkCPTRelation;
    }

    public boolean isHomebound()
    {
        return homebound;
    }

    public void setHomebound(boolean homebound)
    {
        this.homebound = homebound;
    }

    public boolean isVenipunture()
    {
        return venipunture;
    }

    public void setVenipunture(boolean venipunture)
    {
        this.venipunture = venipunture;
    }

    public boolean isApplyTypePayer()
    {
        return applyTypePayer;
    }

    public void setApplyTypePayer(boolean applyTypePayer)
    {
        this.applyTypePayer = applyTypePayer;
    }

    public String getClaimCode()
    {
        return claimCode;
    }

    public void setClaimCode(String claimCode)
    {
        this.claimCode = claimCode;
    }

    public boolean isEligibility()
    {
        return eligibility;
    }

    public void setEligibility(boolean eligibility)
    {
        this.eligibility = eligibility;
    }

    public String getInterchangeSender()
    {
        return interchangeSender;
    }

    public void setInterchangeSender(String interchangeSender)
    {
        this.interchangeSender = interchangeSender;
    }

    public String getInterchangeQualifier()
    {
        return interchangeQualifier;
    }

    public void setInterchangeQualifier(String interchangeQualifier)
    {
        this.interchangeQualifier = interchangeQualifier;
    }

    public String getApplicationSendCode()
    {
        return applicationSendCode;
    }

    public void setApplicationSendCode(String applicationSendCode)
    {
        this.applicationSendCode = applicationSendCode;
    }

    public String getLabSubmitter()
    {
        return labSubmitter;
    }

    public void setLabSubmitter(String labSubmitter)
    {
        this.labSubmitter = labSubmitter;
    }

    public String getIdentificationPayer()
    {
        return identificationPayer;
    }

    public void setIdentificationPayer(String identificationPayer)
    {
        this.identificationPayer = identificationPayer;
    }

    public String getFormatMemberId()
    {
        return formatMemberId;
    }

    public void setFormatMemberId(String formatMemberId)
    {
        this.formatMemberId = formatMemberId;
    }

    public Integer getReceiver()
    {
        return receiver;
    }

    public void setReceiver(Integer receiver)
    {
        this.receiver = receiver;
    }

    public String getConsecutive()
    {
        return consecutive;
    }

    public void setConsecutive(String consecutive)
    {
        this.consecutive = consecutive;
    }

    public String getOutputFileName()
    {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName)
    {
        this.outputFileName = outputFileName;
    }

    public Integer getClaimType()
    {
        return claimType;
    }

    public void setClaimType(Integer claimType)
    {
        this.claimType = claimType;
    }

    public Integer getTransactionType()
    {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType)
    {
        this.transactionType = transactionType;
    }

    public boolean isSupplierSignature()
    {
        return supplierSignature;
    }

    public void setSupplierSignature(boolean supplierSignature)
    {
        this.supplierSignature = supplierSignature;
    }

    public boolean isAssingBenefits()
    {
        return assingBenefits;
    }

    public void setAssingBenefits(boolean assingBenefits)
    {
        this.assingBenefits = assingBenefits;
    }

    public boolean isElectronicClaim()
    {
        return electronicClaim;
    }

    public void setElectronicClaim(boolean electronicClaim)
    {
        this.electronicClaim = electronicClaim;
    }

    public boolean isDefaultItem()
    {
        return defaultItem;
    }

    public void setDefaultItem(boolean defaultItem)
    {
        this.defaultItem = defaultItem;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public String getObservation()
    {
        return observation;
    }

    public void setObservation(String observation)
    {
        this.observation = observation;
    }

    public String getNameReceiver()
    {
        return nameReceiver;
    }

    public void setNameReceiver(String nameReceiver)
    {
        this.nameReceiver = nameReceiver;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final Rate other = (Rate) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}
