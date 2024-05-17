/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.tools;

import net.cltech.outreach.service.interfaces.masters.configuration.ConfigurationService;
import okhttp3.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author cmartin
 */
public class RestClient
{

    @Autowired
    private ConfigurationService configService;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static String urlBase;

//    public static <T> T jsonObject(String content, Class<T> valueType)
//    {
//        try
//        {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//            String jsonContent = mapper.writeValueAsString(valueType);
//            RequestBody body = RequestBody.create(JSON, jsonContent);
//            Request request = new Request.Builder()
//                    .url(urlBase + "/api/authentication")
//                    .post(body)
//                    //                .addHeader("Authorization", token)
//                    .build();
//            OkHttpClient client = new OkHttpClient();
//            Response response = client.newCall(request).execute();
//            if (response.code() == HttpStatus.OK.value())
//            {
//                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                return mapper.readValue(response.body().string(), valueType);
//            } else
//            {
//                return null;
//            }
//        } catch (IOException ex)
//        {
//            ex.printStackTrace();
//            return null;
//        }
//    }
//
//    public static String getUrlBase()
//    {
//        return configService.getValue("urlLIS");
//    }
//
//    public static void setUrlBase(String urlBase)
//    {
//        try
//        {
//            RestClient.urlBase = configService.getValue("urlLIS");
//        } catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
//    }
}
