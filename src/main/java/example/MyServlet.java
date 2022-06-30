package example;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet implements Servlet {

	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
		AsyncContext asyncContext = servletRequest.startAsync();
		MyWriteListener writeListener = new MyWriteListener(asyncContext);
		servletResponse.getOutputStream().setWriteListener(writeListener);
	}

	@Override
	public void init(ServletConfig servletConfig) {
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void destroy() {
	}

	private static final class MyWriteListener implements WriteListener {

		private final byte[] bytes = new byte[]{'F', 'O', 'O'};

		private final AsyncContext asyncContext;

		public MyWriteListener(AsyncContext asyncContext) {
			this.asyncContext = asyncContext;
		}

		@Override
		public void onWritePossible() throws IOException {
			try {
				HttpServletRequest request = (HttpServletRequest) this.asyncContext.getRequest();
				HttpServletResponse response = (HttpServletResponse) this.asyncContext.getResponse();
				response.setHeader("Content-Length", "3");
				if ("GET".equals(request.getMethod())) {
					response.getOutputStream().write(this.bytes);
				}
			}
			finally {
				this.asyncContext.complete();
			}
		}

		@Override
		public void onError(Throwable throwable) {
		}
	}
}
