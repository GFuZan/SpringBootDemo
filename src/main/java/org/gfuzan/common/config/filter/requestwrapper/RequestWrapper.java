package org.gfuzan.common.config.filter.requestwrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

/**
 * 自定义Request,让Json数据可以读多次
 * 
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private static final Logger logger = LoggerFactory.getLogger(RequestWrapper.class);

	/**
	 * 需要缓存的Mime列表
	 */
	private final static List<String> MIME_TYPES = Arrays.asList(MimeTypeUtils.APPLICATION_JSON_VALUE);

	/**
	 * 请求体缓存
	 */
	private final byte[] cacheBody;

	/**
	 * 是否需要缓存
	 */
	private final boolean isCache;

	public RequestWrapper(HttpServletRequest request) {
		super(request);

		if (cacheJudge(this.getContentType())) {
			byte[] body = null;
			try {
				body = StreamUtils.copyToByteArray(super.getInputStream());
				logger.debug("缓存请求体. body:\n" + new String(body));
			} catch (IOException e) {
				logger.error("缓存请求体出错", e);
			}
			isCache = true;
			cacheBody = body;
		} else {
			isCache = false;
			cacheBody = null;
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		if (isCache) {
			return new CustomServletInputStream(cacheBody);
		}

		return super.getInputStream();
	}

	/**
	 * 缓存body判断
	 * 
	 * @param contentType
	 * @return true 缓存,false 不缓存
	 */
	private boolean cacheJudge(String contentType) {
		String mimeType = StringUtils.hasLength(contentType) ? contentType.split(";")[0] : null;
		if (StringUtils.hasLength(mimeType) && MIME_TYPES.contains(mimeType)) {
			return true;
		}
		return false;
	}

	/**
	 * 自定义ServletInputStream
	 *
	 */
	private static class CustomServletInputStream extends ServletInputStream {

		private final ByteArrayInputStream inputStream;

		CustomServletInputStream(byte[] cacheBody) {
			if (cacheBody == null) {
				cacheBody = new byte[0];
			}
			this.inputStream = new ByteArrayInputStream(cacheBody);
		}

		@Override
		public int read() throws IOException {
			return inputStream.read();
		}

		@Override
		public boolean isFinished() {
			return inputStream.available() == 0 ? true : false;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener listener) {
		}

	}
}