package org.gfuzan.common.config;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常处理Controller
 * @author gaofz
 *
 */
@Controller
@ConditionalOnWebApplication
public class ExceptionController extends BasicErrorController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public ExceptionController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }

    @Override
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request,
            HttpServletResponse response) {
        String status = "415";
        PrintWriter pw = null;
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.setCharacterEncoding("UTF-8");
        String page = "<html><title>${title}</title><body><h3>${status}</h3><h4>${msg}<h4></body></html>";
        try {
            pw = response.getWriter();
            page = page.replace("${title}", "非法");
            page = page.replace("${status}",status);
            page = page.replace("${msg}", "连接被拒绝");
            pw.println(page);
        } catch (IOException e) {
            logger.error("IOException", new String[] { "response" }, e);
        }
        return null;
    }

    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> srcbody = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        Map<String, Object> body = new HashMap<>();
        HttpStatus status = getStatus(request);

        body.put("errorCode", status.value());
        body.put("errorMessage", srcbody.get("message"));
        return new ResponseEntity<>(body, status);
    }

}
