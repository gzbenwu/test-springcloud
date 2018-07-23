package test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.ConversionNotSupportedException;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@ResponseBody
public class RestExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, String> defaultErrorHandler(HttpServletRequest req, Exception ex) throws Exception {
		return buildErrInfo(HttpStatus.INTERNAL_SERVER_ERROR, req, ex);
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class })
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> badRequest(HttpServletRequest req, Exception ex) {
		return buildErrInfo(HttpStatus.BAD_REQUEST, req, ex);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public Map<String, String> methodArgumentNotValid(HttpServletRequest req, MethodArgumentNotValidException ex) {
		Map<String, String> res = buildErrInfo(HttpStatus.BAD_REQUEST, req, ex);
		String msg = handleArgumentNotValid(ex.getBindingResult());
		if (msg != null) {
			res.put("message", "MethodArgumentNotValid:" + msg);
		}
		return res;
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

	@ExceptionHandler({ NoHandlerFoundException.class, NoSuchMethodException.class })
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public Map<String, String> request404(HttpServletRequest req, Exception ex) {
		return buildErrInfo(HttpStatus.NOT_FOUND, req, ex);
	}

	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
	public Map<String, String> request405(HttpServletRequest req, Exception ex) {
		return buildErrInfo(HttpStatus.METHOD_NOT_ALLOWED, req, ex);
	}

	@ExceptionHandler({ HttpMediaTypeNotAcceptableException.class, HttpMediaTypeNotSupportedException.class })
	@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public Map<String, String> request415(HttpServletRequest req, Exception ex) {
		return buildErrInfo(HttpStatus.UNSUPPORTED_MEDIA_TYPE, req, ex);
	}

	@ExceptionHandler({ ConversionNotSupportedException.class, HttpMessageNotWritableException.class })
	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
	public Map<String, String> server503(HttpServletRequest req, Exception ex) {
		return buildErrInfo(HttpStatus.SERVICE_UNAVAILABLE, req, ex);
	}

	private static Map<String, String> buildErrInfo(HttpStatus hs, HttpServletRequest req, Exception ex) {
		Map<String, String> r = new HashMap<String, String>();
		r.put("status", "" + hs.value());
		r.put("message", ex.getMessage());
		r.put("path", req.getRequestURI());
		r.put("error", ex.getClass().getSimpleName());
		r.put("timestamp", "" + System.currentTimeMillis());
		r.put("customErrorInfo", "true");
		return r;
	}
}