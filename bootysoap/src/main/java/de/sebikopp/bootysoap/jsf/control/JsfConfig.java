package de.sebikopp.bootysoap.jsf.control;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;

@Configuration
public class JsfConfig implements ServletContextAware {

	@Bean
	public static CustomScopeConfigurer viewScope() {
		CustomScopeConfigurer configurer = new CustomScopeConfigurer();
		configurer.addScope("view", new ViewScope());
		return configurer;
	}

	@Bean
	public ServletRegistrationBean<FacesServlet> facesServletRegistrationBean() {
		ServletRegistrationBean<FacesServlet> registrationBean = new ServletRegistrationBean<>(new FacesServlet(), "*.jsf");
		registrationBean.setLoadOnStartup(1);
		return registrationBean;
	}
	
	
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
		servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");
	}
}
