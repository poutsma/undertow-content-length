package example;

import javax.servlet.ServletException;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

public class Driver {

	public static void main(String[] args) throws ServletException {
		DeploymentInfo servletBuilder = Servlets.deployment()
		        .setClassLoader(MyServlet.class.getClassLoader())
		        .setContextPath("/")
		        .setDeploymentName("test.war")
		        .addServlets(
		                Servlets.servlet("myServlet", MyServlet.class)
				                .setAsyncSupported(true)
		                        .addMapping("/*"));

		DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
		manager.deploy();
		PathHandler path = Handlers.path(Handlers.redirect("/"))
		        .addPrefixPath("/", manager.start());

		Undertow server = Undertow.builder()
		        .addHttpListener(8080, "localhost")
		        .setHandler(path)
		        .build();
		server.start();
	}

}
