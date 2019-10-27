package org.gfuzan.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reuqest替换Filter
 * @author GFuZan
 *
 */
public class RequestWrapperFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestWrapperFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(request instanceof HttpServletRequest) {
			HttpServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) request);
			request = requestWrapper;
			
			StringBuilder sb = new StringBuilder();
			sb.append("RequestURL: '").append(requestWrapper.getRequestURI()).append("' ");
			sb.append("ContentType: '").append(request.getContentType()).append("'");
			logger.debug(sb.toString());
		}
		chain.doFilter(request, response);
		
	}

}
