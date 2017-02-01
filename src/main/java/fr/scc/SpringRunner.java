package fr.scc;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import fr.scc.elo.controller.EloConfig;
import fr.scc.elo.filter.ConnectionFilter;
import fr.scc.elo.filter.EloFilter;
import fr.scc.elo.service.ConnectionService;
import io.swagger.jaxrs.config.BeanConfig;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * Copyright (C) 2015 CHERRIER-COSTE Sébastien

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * @author Static
 *
 */

@SpringBootApplication
@Log4j2
@EnableAutoConfiguration
@ComponentScan
public class SpringRunner extends SpringBootServletInitializer{
	
	@Autowired
	@Setter
	private ApplicationContext applicationContext;
	
	@Autowired
	private ConnectionService connectionService;
	
    public static void main(String[] args) {
        SpringApplication.run(SpringRunner.class, args);
    }
    
	
	@Bean
	public ServletRegistrationBean jerseyServlet() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/rest/v1/*");
		registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, EloConfig.class.getName());
		registration.addInitParameter("jersey.config.server.provider.classnames", 
				"org.glassfish.jersey.server.mvc.jsp.JspMvcFeature");
		registration.addInitParameter("jersey.config.server.mvc.templateBasePath.jsp", 
        		"/WEB-INF/jsp");
		registration.addInitParameter("jersey.config.server.tracing", 
        		"ALL");
		registration.addInitParameter("jersey.config.server.provider.packages", 
        		"fr.scc.elo.controller");
		registration.setLoadOnStartup(1);
		return registration;
	}
	
	
	
	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		log.info("Lancement SpringRunner en mode war");
		return application.sources(SpringRunner.class);
	}
	
	
	@Bean
	public FilterRegistrationBean exceptionFilter(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new EloFilter());
        registrationBean.addUrlPatterns("/rest/v1/*");
        registrationBean.setOrder(2);
        return registrationBean;
	}
	
	
	@Bean
	public FilterRegistrationBean connectionFilter(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new ConnectionFilter());
        registrationBean.addUrlPatterns("/*");
        ConnectionFilter.setConnectionService(applicationContext.getBean(ConnectionService.class));
        registrationBean.setOrder(1);
        return registrationBean;
	}
	
	
	
    @PostConstruct
	public void postConstruct(){
		configureSwagger();
		configureAuthorizedMembers();
		
	}
    

    
    private void configureAuthorizedMembers() {
    	connectionService.setContext(
    			Arrays.asList(applicationContext.getEnvironment().getProperty("ip.authorized.update").split(",")),
    			Arrays.asList(applicationContext.getEnvironment().getProperty("ip.authorized.delete").split(","))
    			);	
}


	private void configureSwagger() {
		if (applicationContext != null){
			configureSwaggerBean(
					applicationContext.getEnvironment().getProperty("swagger.version"),
					applicationContext.getEnvironment().getProperty("swagger.basePath"),
					applicationContext.getEnvironment().getProperty("swagger.resourcePackage"));
		}
	}
    private static void configureSwaggerBean(String version, String path, String resourcePackage){
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion(version);
		beanConfig.setBasePath(path);
		beanConfig.setResourcePackage(resourcePackage);
		beanConfig.setScan(true);
	}
    
    
    
    
    
    
   /*
    * Previous: avec spring MVC 
    */
    
//	@Override
//  public void addInterceptors(InterceptorRegistry registry) {
//      registry.addInterceptor(localeChangeInterceptor());
//  }
    

//  @Bean
//  public ServletRegistrationBean elo() {
//      return buildBeanForController(EloConfig.class, "elo", "/elo/*");
//  }
  
    
    
    
//  private ServletRegistrationBean buildBeanForController(Class<?> className, String name, String path) {
//  DispatcherServlet dispatcherServlet = new DispatcherServlet();
//  AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
//  applicationContext.register(className);
//  dispatcherServlet.setApplicationContext(applicationContext);
//  ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet, path);
//  servletRegistrationBean.setName(name);
//  return servletRegistrationBean;
//}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}