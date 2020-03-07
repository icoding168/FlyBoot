package xyz.icoding168.flyboot.configuration.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import xyz.icoding168.flyboot.common.CommonResponse;
import xyz.icoding168.flyboot.common.UncheckedException;
import xyz.icoding168.flyboot.common.helper.HttpHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class ResponseBodyHandler extends DefaultErrorAttributes implements ResponseBodyAdvice {

    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);

        logger.error(errorAttributes);
        if(errorAttributes != null && errorAttributes.size() != 0){
            Integer status = (Integer)errorAttributes.get("status");
            String message = (String)errorAttributes.get("message");

            String path = (String)errorAttributes.get("path");

            logger.error(message + ":" + path);

            throw new UncheckedException(status, message + ":" + path);
        }
        return errorAttributes;
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public CommonResponse handleHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        logger.error("error",e);
        UncheckedException uncheckedException = new UncheckedException();
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(uncheckedException.getCode());
        commonResponse.setMsg("请求参数解析异常，请检查数据格式和内容是否正确。");
        return commonResponse;
    }

    /**
     * 结合 @Valid 处理 javax validation 异常
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public CommonResponse handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        logger.error("error",e);
        BindingResult bindingResult = e.getBindingResult();
        logger.info(bindingResult);
        FieldError fieldError = bindingResult.getFieldError();
        String message = fieldError.getDefaultMessage();

        UncheckedException uncheckedException = new UncheckedException();
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(uncheckedException.getCode());
        commonResponse.setMsg(fieldError.getField() + message);
        return commonResponse;
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    public CommonResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("error",e);
        UncheckedException uncheckedException = new UncheckedException();
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(uncheckedException.getCode());
        commonResponse.setMsg(e.getMessage());
        return commonResponse;
    }

    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public CommonResponse handleHttpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        logger.error("error",e);
        UncheckedException uncheckedException = new UncheckedException();
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(uncheckedException.getCode());
        commonResponse.setMsg(e.getMessage());
        return commonResponse;
    }


    @ExceptionHandler(value = {UncheckedException.class})
    @ResponseBody
    public Object handleUncheckedException(UncheckedException e) {
        logger.error("error",e);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(e.getCode());
        commonResponse.setMsg(e.getMsg());
        return commonResponse;
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleException(Exception e) {
        logger.error("error",e);
        UncheckedException uncheckedException;
        if (e instanceof UncheckedException) {
            uncheckedException = (UncheckedException) e;
        }else{
            uncheckedException = new UncheckedException();
        }

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(uncheckedException.getCode());
        commonResponse.setMsg(uncheckedException.getMsg());
        return commonResponse;
    }


    @Override
    public boolean supports(MethodParameter methodParameter, Class clazz) {
        boolean b = true;
        return b;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class clazz, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if(o == null){
            return new CommonResponse();
        }

        if(o instanceof CommonResponse){
            return o;
        }

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setBody(o);

        if(mediaType != MediaType.APPLICATION_JSON){
            serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return HttpHelper.getJsonString(commonResponse);
        }

        return commonResponse;
    }


}
