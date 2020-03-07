package xyz.icoding168.flyboot.common;


import java.util.HashMap;

public class CommonResponse {

    private Integer code = 2000;

    private String msg = "成功";

    private Object body = new HashMap<>();

    public CommonResponse() {
    }

    public CommonResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
