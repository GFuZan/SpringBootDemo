package org.gfuzan.common.config.filter.requestwrapper;

import java.io.IOException;
import java.util.Enumeration;

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
 * 
 * @author GFuZan
 *
 */
public class RequestWrapperFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(RequestWrapperFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest requestWrapper = new RequestWrapper((HttpServletRequest) request);
			request = requestWrapper;

			// 打印请求详情
			{
				StringBuilder sb = new StringBuilder();
				sb.append("请求详情");

				// 请求URL
				sb.append('\n').append("RequestURL:");
				sb.append('\n').append('\t');
				sb.append(requestWrapper.getRequestURI());

				// 请求头
				sb.append('\n').append("RequestHead:");
				Enumeration<String> headerNames = requestWrapper.getHeaderNames();
				while (headerNames.hasMoreElements()) {
					sb.append('\n').append('\t');
					String headName = headerNames.nextElement();
					sb.append(headName).append(':').append(requestWrapper.getHeader(headName));
				}
				logger.debug(sb.toString());
			}

		}
		chain.doFilter(request, response);

	}

}
