package test.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = RootErrorController.ERROR_PATH)
public class RootErrorController implements ErrorController {
	public static final String ERROR_PATH = "/error";

	@RequestMapping
	@ResponseBody
	public ResponseEntity<Map<String, String>> getServerLink(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException {
		Map<String, String> r = new HashMap<String, String>();
		r.put("status", "" + HttpStatus.INTERNAL_SERVER_ERROR);
		r.put("message", HttpStatus.INTERNAL_SERVER_ERROR.name());
		r.put("path", MarkUriFilter.getRequestUri());
		r.put("timestamp", "" + System.currentTimeMillis());
		r.put("customErrorInfo", "rooterr");
		r.put("threadId", "" + Thread.currentThread().getId());
		HttpHeaders header = new HttpHeaders();
		header.add("err-header", "rooterr");
		ResponseEntity<Map<String, String>> rep = new ResponseEntity<Map<String, String>>(r, header, HttpStatus.INTERNAL_SERVER_ERROR);

		MarkUriFilter.removeRequestUri();
		return rep;
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
}
