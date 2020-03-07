package xyz.icoding168.flyboot.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UncheckedException extends RuntimeException {

    private static final Logger logger = LogManager.getLogger();

    private Integer code = 5000;
    private String msg = "失败";

    public UncheckedException(){

    }

    public UncheckedException(Throwable cause) {
        super(cause);
        logger.error("error",cause);
    }

    public UncheckedException(String msg){
        this.msg = msg;
        logger.error("error",msg);
    }

    public UncheckedException(Integer code,String msg){
        this.code = code;
        this.msg = msg;
        logger.error("error",msg);
    }

    public UncheckedException(Integer code,String msg,Throwable cause){
        this.code = code;
        this.msg = msg;
        logger.error(msg,cause);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
