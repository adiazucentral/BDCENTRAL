package net.cltech.securitynt.service.impl.securitynt.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cltech.securitynt.dao.interfaces.masters.demographic.BranchDao;
import net.cltech.securitynt.domain.common.AuthenticationSession;
import net.cltech.securitynt.domain.common.License;
import net.cltech.securitynt.domain.common.LicenseResponse;
import net.cltech.securitynt.domain.exception.EnterpriseNTException;
import net.cltech.securitynt.domain.masters.demographic.Branch;
import net.cltech.securitynt.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.securitynt.service.interfaces.security.LicenseService;
import net.cltech.securitynt.service.interfaces.security.SessionService;
import net.cltech.securitynt.tools.JWT;
import net.cltech.securitynt.tools.log.LicenseLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Implementacion de licencias para Enterprise NT
 *
 * @version 1.0.0
 * @author equijano
 * @since 25/10/2019
 * @see Creacion
 */
@Service
public class LicenseServiceSecurityNT implements LicenseService
{

    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private BranchDao branchDao;
    private static final String PRODUCT_CODE_LICENSE = "ENT";
    private static final String PRODUCT_CODE_LICENSE_BOARD = "TAB";
    private static final String PRODUCT_CODE_LICENSE_INTERFACE = "HIS";
    private static final String PATH_LICENSE = "c:\n";
    
    @Override
    public LicenseResponse getLicenses(License license) throws Exception
    {
        try
        {
            final String url = configurationService.getValue("UrlDischarge") + "/api/responses";
            try
            {
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                final HttpEntity<License> httpEntity = new HttpEntity<>(license, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                if (responseEntity.getBody() != null)
                {
                    
                    return JWT.decodeLicense(responseEntity.getBody());
                }
            } catch (IllegalArgumentException | HttpClientErrorException ex)
            {
                LicenseLog.error("URL not found - Url: " + url + " - Method: POST");
            }
        } catch (Exception ex)
        {
            LicenseLog.error(ex);
            Logger.getLogger(LicenseServiceSecurityNT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public HashMap<String, Boolean> licences(String branchCode) throws Exception
    {
        String language = "es-CO";
        HashMap<String, Boolean> hash = new HashMap<>();
        hash.put("product", true);
        hash.put("branch", true);
        hash.put("user", true);
        hash.put("LIS", true);
        hash.put("MMB", true);
        hash.put("PAT", true);
        hash.put("FAC", true);
        hash.put("NUM", true);
        hash.put("CWB", true);
        
        if (validateLicense(new License(PRODUCT_CODE_LICENSE, PATH_LICENSE, null, null, language)))
        {
            hash.replace("product", Boolean.TRUE);
            if (validateLicense(new License(PRODUCT_CODE_LICENSE, PATH_LICENSE, branchCode, null, language)))
            {
                hash.replace("LIS", validateLicense(new License(PRODUCT_CODE_LICENSE, PATH_LICENSE, "LIS", null, language)));
                hash.replace("MMB", validateLicense(new License(PRODUCT_CODE_LICENSE, PATH_LICENSE, "MMB", null, language)));
                hash.replace("PAT", validateLicense(new License(PRODUCT_CODE_LICENSE, PATH_LICENSE, "PAT", null, language)));
                hash.replace("FAC", validateLicense(new License(PRODUCT_CODE_LICENSE, PATH_LICENSE, "FAC", null, language)));
                hash.replace("NUM", validateLicense(new License(PRODUCT_CODE_LICENSE, PATH_LICENSE, "NUM", null, language)));
                hash.replace("CWB", validateLicense(new License("CWB", PATH_LICENSE, "", null, language)));   
                hash.replace("branch", validateLicense(new License(PRODUCT_CODE_LICENSE, PATH_LICENSE, branchCode, null, language)));
                Branch branch = branchDao.get(null, null, null, branchCode);
                if (branch != null)
                {
                    List<AuthenticationSession> sessions = sessionService.list();
                    String usersByBranch = Long.toString(sessions.stream().filter(s -> Objects.equals(s.getBranch(), branch.getId())).count());
                    if (validateLicense(new License(PRODUCT_CODE_LICENSE, PATH_LICENSE, branchCode, usersByBranch, language)))
                    {
                        hash.replace("user", Boolean.TRUE);
                    }
                }
            }
        }
        return hash;
    }

    private boolean validateLicense(License license) throws Exception
    {
        return true;
//        LicenseResponse licenseProduct = getLicenses(license);
//        if (licenseProduct != null && licenseProduct.getErrorCode().equals("Success"))
//        {
//            return true;
//        } else if (licenseProduct != null && licenseProduct.getErrorCode().equals("Err04"))
//        {
//            throw new EnterpriseNTException(Arrays.asList("La licencia registrada ha expirado."));
//        } else
//        {
//            return false;
//        }
    }
    
    @Override
    public Boolean validateLicenseInterface(String llave) throws Exception
    {
        String language = "es-CO";
        return validateLicense(new License(PRODUCT_CODE_LICENSE_INTERFACE, PATH_LICENSE, llave, null, language));
    }
    
    /**
     * Obtener las licencias de los tableros
     *
     * @return Licencias de los tableros
     * @throws Exception Error en la base de datos.
     */
    @Override
    public HashMap<String, Boolean> boardLicenses() throws Exception
    {
        String language = "es-CO";
        HashMap<String, Boolean> hash = new HashMap<>();
        hash.put("TableroTomaDeMuestra", true);
        hash.put("TableroProductividadPorSeccion", true);
        hash.put("TableroTiempoDeOportunidad", true);
        hash.put("TableroValidacion", true);
        hash.put("TableroCalificacionDelServicio", true);
        hash.put("TableroSeguimientoDePruebas", true);
        
        /*if (validateLicense(new License(PRODUCT_CODE_LICENSE_BOARD, PATH_LICENSE, null, null, language)))
        {
            hash.replace("TableroTomaDeMuestra", validateLicense(new License(PRODUCT_CODE_LICENSE_BOARD, PATH_LICENSE, "TTM", null, language)));
            hash.replace("TableroProductividadPorSeccion", validateLicense(new License(PRODUCT_CODE_LICENSE_BOARD, PATH_LICENSE, "TPS", null, language)));
            hash.replace("TableroTiempoDeOportunidad", validateLicense(new License(PRODUCT_CODE_LICENSE_BOARD, PATH_LICENSE, "TTO", null, language)));
            hash.replace("TableroValidacion", validateLicense(new License(PRODUCT_CODE_LICENSE_BOARD, PATH_LICENSE, "TV", null, language)));
            hash.replace("TableroCalificacionDelServicio", validateLicense(new License(PRODUCT_CODE_LICENSE_BOARD, PATH_LICENSE, "TCS", null, language)));
            hash.replace("TableroSeguimientoDePruebas", validateLicense(new License(PRODUCT_CODE_LICENSE_BOARD, PATH_LICENSE, "TSP", null, language)));
        }*/
        return hash;
    }
}
