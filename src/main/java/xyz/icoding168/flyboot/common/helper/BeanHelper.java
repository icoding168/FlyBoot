package xyz.icoding168.flyboot.common.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import xyz.icoding168.flyboot.common.UncheckedException;


public class BeanHelper {

    private static Logger logger = LoggerFactory.getLogger(BeanHelper.class);


    public static <T> T clone(Object source, Class<T> destinationClass) {

        try {
            Object obj = destinationClass.newInstance();
            BeanUtils.copyProperties(source,obj);

            return ((T) obj);
        } catch (Exception e) {
            logger.error("error",e);
            throw new UncheckedException();
        }
    }

    public static void main(String[] args) {


    }

}
