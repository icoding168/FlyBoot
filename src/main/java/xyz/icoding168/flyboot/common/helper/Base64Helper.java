package xyz.icoding168.flyboot.common.helper;



import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;

public class Base64Helper {


    public static String encode(String string) throws Exception{
        if(StringUtils.isBlank(string)){
            return null;
        }
        byte[] bytes = string.getBytes();
        Base64 base64 = new Base64();
        bytes = base64.encode(bytes);
        String result = new String(bytes);
        return result;
    }


    public static String decode(String string) throws Exception{
        if(StringUtils.isBlank(string)){
            return null;
        }
        String result = null;
        byte[] bytes = string.getBytes();
        Base64 base64 = new Base64();
        bytes = base64.decode(bytes);
        result = new String(bytes);
        return result;
    }


    public static String encodeWithUrlSafety(String string) throws Exception{
        if(StringUtils.isBlank(string)){
            return null;
        }
        String finalResult = null;
        byte[] bytes = string.getBytes();
        Base64 base64 = new Base64();
        bytes = base64.encode(bytes);
        String result = new String(bytes);
        URLCodec urlCodec = new URLCodec();
        finalResult = urlCodec.encode(result);
        return finalResult;


    }


    public static String decodeWithUrlSafety(String string) throws Exception{
        if(StringUtils.isBlank(string)){
            return null;
        }
        String finalResult = null;
        URLCodec urlCodec = new URLCodec();
        String result = urlCodec.decode(string);
        byte[] bytes = result.getBytes();
        Base64 base64 = new Base64();
        bytes = base64.decode(bytes);
        finalResult = new String(bytes);
        return finalResult;
    }


}
