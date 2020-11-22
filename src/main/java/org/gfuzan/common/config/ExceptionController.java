package org.gfuzan.common.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.gfuzan.common.result.RawResponse;
import org.gfuzan.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 异常处理Controller
 * 
 * @author gfuzan
 *
 */
@Controller
@ConditionalOnWebApplication
public class ExceptionController extends BasicErrorController {

	@Autowired
	private ServerProperties serverProperties;

	@Autowired
	private WebMvcProperties webMvcProperties;

	private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

	public ExceptionController(ServerProperties serverProperties) {
		super(new DefaultErrorAttributes(), serverProperties.getError());
	}

//	@Override
//	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
//	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
//		String status = "415";
//		PrintWriter pw = null;
//		response.setContentType(MediaType.TEXT_HTML_VALUE);
//		response.setCharacterEncoding("UTF-8");
//		String page = "<html><title>${title}</title><body><h3>${status}</h3><h4>${msg}<h4></body></html>";
//		try {
//			pw = response.getWriter();
//			page = page.replace("${title}", "非法");
//			page = page.replace("${status}", status);
//			page = page.replace("${msg}", "连接被拒绝");
//			pw.println(page);
//		} catch (IOException e) {
//			log.error("IOException", new String[] { "response" }, e);
//		}
//		return null;
//	}

	@Override
	@RequestMapping
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> srcbody = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
		HttpStatus status = getStatus(request);

		RawResponse<Object> res = new RawResponse<>();
		res.setErrorCode(status.value());
		res.setErrorMessage((String) srcbody.get("message"));
		if (res.getErrorCode() == 404) {
			res.addOtherField("rootPath", serverProperties.getServlet().getContextPath() + webMvcProperties.getServlet().getPath());
		}
		ObjectMapper objectMapper = CommonUtil.getObjectMapper();
		Map<String, Object> body = new HashMap<>();
		try {
			body = objectMapper.readValue(objectMapper.writeValueAsBytes(res),new TypeReference<Map<String, Object>>() {});
		} catch (Exception e) {
			log.warn("转换错误消息失败!", e);
			body = srcbody;
		}

		return new ResponseEntity<>(body, status);
	}

}
