package xyz.icoding168.flyboot.common.helper;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.icoding168.flyboot.common.jackson.MyBeanSerializerModifier;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public final class HttpHelper {


    private static final Logger logger = LogManager.getLogger();

    public static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 统一设置日期格式、根据字段类型将 json 中的 null 统一转为空字符串""或空集合"[]"
     */
    static{
        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(myDateFormat);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new MyBeanSerializerModifier()));
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    public static String getJsonString(Object object){
        try {
            if(object == null){
                return null;
            }

            String str = objectMapper.writeValueAsString(object);
            return str;
        } catch (Exception e) {
            logger.error("error",e);
            return null;
        }
    }

    public static JsonNode getJsonNode(Object object) {
        try {
            if(object == null){
                return null;
            }

            if(object instanceof String){
                String jsonString = object.toString();
                if(StringUtils.isBlank(jsonString)){
                    return null;
                }

                JsonNode jsonNode = objectMapper.readTree(jsonString);
                return jsonNode;
            }

            JsonNode jsonNode = objectMapper.valueToTree(object);
            return jsonNode;
        } catch (Exception e) {
            logger.error("error",e);
            return null;
        }

    }

    public static boolean checkConvertToMap(Object o){
        try{
            Map map = objectMapper.convertValue(o, HashMap.class);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    public static JsonNode callApi(String url, Map<String,Object> parameters) throws Exception{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("content-type","application/json;charset=UTF-8");

        if(parameters != null){
            String params = HttpHelper.getJsonString(parameters);
            StringEntity stringEntity = new StringEntity(params,"UTF-8");
            httpPost.setEntity(stringEntity);
        }

        return post(httpPost);
    }

    public static JsonNode callApi(String url, Map<String,Object> parameters, Map<String,String> headers) throws Exception{
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("content-type","application/json;charset=UTF-8");

        if(parameters != null){
            String params = HttpHelper.getJsonString(parameters);
            StringEntity stringEntity = new StringEntity(params,"UTF-8");
            httpPost.setEntity(stringEntity);
        }

        if(headers != null && headers.size() > 0){
            for(String key:headers.keySet()){
                httpPost.setHeader(key,headers.get(key));
            }
        }

        return post(httpPost);
    }


    public static JsonNode callApiWithForm(String url, Map<String,Object> parameters) throws Exception{
        HttpPost httpPost;
        if(parameters == null){
            httpPost = new HttpPost(url);
        }else{
            URIBuilder builder = new URIBuilder(url);
            for(String key:parameters.keySet()){
                Object object = parameters.get(key);
                if(object == null){
                    continue;
                }
                builder.setParameter(key, parameters.get(key).toString());
            }
            httpPost = new HttpPost(builder.build());
        }
        return post(httpPost);
    }

    public static JsonNode callApiWithForm(String url, Map<String,Object> parameters, Map<String,String> headers) throws Exception{
        HttpPost httpPost;
        if(parameters == null){
            httpPost = new HttpPost(url);
        }else{
            URIBuilder builder = new URIBuilder(url);
            for(String key:parameters.keySet()){
                Object object = parameters.get(key);
                if(object == null){
                    continue;
                }
                builder.setParameter(key, parameters.get(key).toString());
            }
            httpPost = new HttpPost(builder.build());
        }

        if(headers != null && headers.size() > 0){
            for(String key:headers.keySet()){
                httpPost.setHeader(key,headers.get(key));
            }
        }
        return post(httpPost);
    }


    private static JsonNode post(HttpPost httpPost){
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            StatusLine statusLine = httpResponse.getStatusLine();

            if(statusLine.getStatusCode() != 200){
                return null;
            }

            HttpEntity entity = httpResponse.getEntity();
            if (entity == null) {
                return null;
            }

            ContentType contentType = ContentType.getOrDefault(entity);
            Charset charset = contentType.getCharset() == null ? StandardCharsets.UTF_8 : contentType.getCharset();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), charset));

            StringBuffer sb = new StringBuffer();
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                sb.append(inputLine);
            }

            JsonNode jsonNode = HttpHelper.getJsonNode(sb.toString());

            logger.info("response is " + sb.toString());

            return jsonNode;
        } catch (Exception e) {
            logger.error("error",e);
            return null;
        }

    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if(ip.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip= inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }


    public static void main(String[] args) throws Exception{
        Map<String,Object> map = new HashMap<>();
        map.put("3","3");
        JsonNode jsonNode = HttpHelper.getJsonNode(HttpHelper.getJsonString(map));

        System.out.println(jsonNode.toString());

    }


}
