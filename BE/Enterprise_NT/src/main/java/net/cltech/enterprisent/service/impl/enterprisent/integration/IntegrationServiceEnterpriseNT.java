package net.cltech.enterprisent.service.impl.enterprisent.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.exception.WebException;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.BranchService;
import net.cltech.enterprisent.service.interfaces.masters.user.UserService;
import net.cltech.enterprisent.tools.log.integration.SecurityLog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Implementacion de integración para Enterprise NT.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/08/2018
 * @see Creacion
 */
@Service
public class IntegrationServiceEnterpriseNT implements IntegrationService
{

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private BranchService branchService;

    @Override
    public List<User> listUser() throws Exception
    {
        List<User> users = userService.list(true);
        users.forEach((user) ->
        {
            //Ocultar información innesaria para la integración
            users.set(users.indexOf(user), new User(user.getId(), user.getName(), user.getLastName(), user.getUserName(), user.getPhoto()));
        });
        return users;
    }

    @Override
    public List<User> listUserSimple() throws Exception
    {
        List<User> users = userService.SimpleUserList();
        return users;
    }

    @Override
    public List<Branch> listBranches() throws Exception
    {
        List<Branch> branches = branchService.list(true);
        branches.forEach((branch) ->
        {
            branches.set(branches.indexOf(branch), new Branch(branch.getId(), branch.getCode(), branch.getName()));
        });
        return branches;
    }

    @Override
    public void processResponseError(Response response) throws IOException, EnterpriseNTException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String responseAsString = response.body().string();
        try
        {
            throw new EnterpriseNTException(mapper.readValue(responseAsString, WebException.class).getErrorFields());
        } catch (IOException ex)
        {
            ex.printStackTrace();
            SecurityLog.error(ex);
            throw new EnterpriseNTException(responseAsString);
        }
    }

    @Override
    public <T> T post(String data, Class<T> valueTypeReturn, String url) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", getToken())
                .post(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        if (response.code() == HttpStatus.OK.value())
        {
            return mapper.readValue(response.body().string(), mapper.getTypeFactory().constructType(valueTypeReturn));
        } else if (response.code() == HttpStatus.NO_CONTENT.value())
        {
            return null;
        } else
        {
            processResponseError(response);
        }
        return null;
    }

    @Override
    public <T> T put(String data, Class<T> valueTypeReturn, String url) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", getToken())
                .put(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        
        if (response.code() == HttpStatus.OK.value())
        {
            return mapper.readValue(response.body().string(), mapper.getTypeFactory().constructType(valueTypeReturn));
        } else if (response.code() == HttpStatus.NO_CONTENT.value())
        {
            return null;
        } else
        {
            processResponseError(response);
        }
        return null;
        
        
    }

    @Override
    public <T> void putVoid(String data, String url) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", getToken())
                .put(body)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        if (response.code() != HttpStatus.OK.value() || response.code() == HttpStatus.NO_CONTENT.value())
        {
            processResponseError(response);
        }
    }

    @Override
    public <T> T get(Class<T> valueTypeReturn, String url) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", getToken())
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        if (response.code() == HttpStatus.OK.value())
        {
            return mapper.readValue(response.body().string(), mapper.getTypeFactory().constructType(valueTypeReturn));
        } else if (response.code() == HttpStatus.NO_CONTENT.value())
        {
            return null;
        } else
        {
            processResponseError(response);
        }
        return null;
    }

    @Override
    public <T> List<T> getList(Class<T> valueTypeReturn, String url) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", getToken())
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        if (response.code() == HttpStatus.OK.value())
        {
            return mapper.readValue(response.body().string(), mapper.getTypeFactory().constructCollectionType(List.class, valueTypeReturn));
        } else if (response.code() == HttpStatus.NO_CONTENT.value())
        {
            return new ArrayList<>();
        } else
        {
            processResponseError(response);
        }
        return new ArrayList<>();
    }

    @Override
    public void delete(String url, String token) throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        Request requestRest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token == null ? getToken() : token)
                .delete()
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(requestRest).execute();
        if (response.code() != HttpStatus.OK.value() || response.code() == HttpStatus.NO_CONTENT.value())
        {
            processResponseError(response);
        }
    }

    private String getToken()
    {
        String token = "";
        try
        {
            token = request.getHeader("Authorization");
            return token == null ? "" : token;
        } catch (Exception ex)
        {
        }
        return token;
    }
}
