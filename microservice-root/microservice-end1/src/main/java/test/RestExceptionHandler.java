package test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	public Map<String, String> defaultErrorHandler(HttpServletRequest req, HttpServletResponse res, Exception ex) throws Exception {
		Map<String, String> r = new HashMap<String, String>();
		if (ex instanceof org.springframework.web.servlet.NoHandlerFoundException) {
			r.put("status", "" + HttpStatus.NOT_FOUND.value());
			res.setStatus(HttpStatus.NOT_FOUND.value());
		} else {
			r.put("status", "" + HttpStatus.INTERNAL_SERVER_ERROR.value());
			res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		r.put("message", ex.getMessage());
		r.put("path", req.getRequestURI());
		r.put("error", ex.getClass().getSimpleName());
		r.put("timestamp", "" + System.currentTimeMillis());
		r.put("customErrorInfo", "true");
		return r;
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class, HttpMessageNotReadableException.class, TypeMismatchException.class })
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public String requestNotReadable(HttpServletRequest req, RuntimeException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public String methodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex) {
		String msg = handleArgumentNotValid(ex.getBindingResult());
		if (msg == null) {
			return ex.getClass().getSimpleName() + ":" + ex.getMessage();
		} else {
			return "[Build By RestExceptionHandler] " + msg;
		}
	}

	public static String handleArgumentNotValid(BindingResult validResult) {
		if (validResult != null && validResult.hasErrors()) {
			return validResult.getAllErrors().stream().map((or) -> {
				StringBuilder codes = new StringBuilder();
				for (Object o : or.getArguments()) {
					if (o instanceof MessageSourceResolvable && !(o instanceof DefaultMessageSourceResolvable)) {
						MessageSourceResolvable msr = (MessageSourceResolvable) o;
						codes.append(msr.getDefaultMessage() + ",");
					} else if (o instanceof Boolean || o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Float || o instanceof Short) {
						codes.append(o + ",");
					}
				}

				String info;
				if (or instanceof FieldError) {
					FieldError fe = (FieldError) or;
					info = "[" + fe.getObjectName() + "|" + fe.getField() + "|" + fe.getRejectedValue() + "]";
				} else {
					info = "[" + or.getObjectName() + "]";
				}

				return info + " {" + codes + "} " + or.getCode() + ":" + or.getDefaultMessage() + "\n";
			}).collect(Collectors.toList()).toString();
		} else {
			return null;
		}
	}

	@ExceptionHandler(NoSuchMethodException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String noSuchMethodExceptionHandler(HttpServletRequest req, NoSuchMethodException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}

	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
	public String request405(HttpServletRequest req, HttpRequestMethodNotSupportedException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}

	@ExceptionHandler({ HttpMediaTypeNotAcceptableException.class, HttpMediaTypeNotSupportedException.class })
	@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public String request406(HttpServletRequest req, RuntimeException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}

	@ExceptionHandler({ ConversionNotSupportedException.class, HttpMessageNotWritableException.class })
	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
	public String server500(HttpServletRequest req, RuntimeException ex) {
		return ex.getClass().getSimpleName() + ":" + ex.getMessage();
	}
}