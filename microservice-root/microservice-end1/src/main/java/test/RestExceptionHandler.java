package test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	public Map<String, String> defaultErrorHandler(HttpServletRequest req, Exception ex) throws Exception {
		Map<String, String> r = new HashMap<String, String>();
		if (ex instanceof org.springframework.web.servlet.NoHandlerFoundException) {
			r.put("status", "404");
		} else {
			r.put("status", "500");
		}
		r.put("message", ex.getMessage());
		r.put("path", req.getRequestURI());
		r.put("error", ex.getClass().getSimpleName());
		r.put("timestamp", "" + System.currentTimeMillis());
		r.put("customErrorInfo", "true");
		return r;
	}

	@ExceptionHandler(RuntimeException.class)
	public String runtimeExceptionHandler(HttpServletRequest req, RuntimeException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}

	@ExceptionHandler(NoSuchMethodException.class)
	public String noSuchMethodExceptionHandler(HttpServletRequest req, NoSuchMethodException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public String requestNotReadable(HttpServletRequest req, HttpMessageNotReadableException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}

	@ExceptionHandler({ TypeMismatchException.class })
	public String requestTypeMismatch(HttpServletRequest req, TypeMismatchException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public String requestMissingServletRequest(HttpServletRequest req, MissingServletRequestParameterException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}

	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public String request405(HttpServletRequest req, HttpRequestMethodNotSupportedException ex) {
		return ex.getClass().getSimpleName() + "[405]:" + ex.getMessage();
	}

	@ExceptionHandler({ HttpMediaTypeNotAcceptableException.class })
	public String request406(HttpServletRequest req, HttpMediaTypeNotAcceptableException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}

	@ExceptionHandler({ ConversionNotSupportedException.class, HttpMessageNotWritableException.class })
	public String server500(HttpServletRequest req, RuntimeException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}
}