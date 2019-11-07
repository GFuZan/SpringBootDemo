package org.gfuzan.common.config.filter.requestwrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.util.StringUtils;

/**
 * 自定义Request,让Json数据可以读多次
 * 
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private byte[] postBody;

	public RequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		if (StringUtils.hasLength(this.getContentType()) && this.getContentType().toLowerCase().indexOf("json") >= 0) {
			if (postBody == null) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				IOUtils.copy(super.getInputStream(), bos);
				postBody = bos.toByteArray();
			}

			return new CustomServletInputStream(postBody);
		}

		return super.getInputStream();
	}

	/**
	 * 自定义ServletInputStream
	 *
	 */
	private static class CustomServletInputStream extends ServletInputStream {

		private final InputStream is;

		CustomServletInputStream(byte[] postBody) {
			if (postBody == null) {
				postBody = new byte[0];
			}
			this.is = new ByteArrayInputStream(postBody);
		}

		@Override
		public int read() throws IOException {
			return is.read();
		}

		@Override
		public boolean isFinished() {
			return false;
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