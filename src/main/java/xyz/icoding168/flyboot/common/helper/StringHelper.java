package xyz.icoding168.flyboot.common.helper;

import org.apache.commons.lang3.StringUtils;

public class StringHelper {

    public static boolean isNullOrEmptyOrBlank(String string){
        if(StringUtils.isBlank(string)){
            return true;
        }

        string = StringUtils.lowerCase(string);

        if(StringUtils.equals(string,"null")){
            return true;
        }

        if(StringUtils.equals(string,"undefined")){
            return true;
        }

        if(StringUtils.equals(string,"nil")){
            return true;
        }

        return false;
    }
}
