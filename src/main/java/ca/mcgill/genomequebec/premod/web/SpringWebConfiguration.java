/*
 * Copyright (c) 2013 Institut de recherches cliniques de Montreal (IRCM)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.mcgill.genomequebec.premod.web;

import ca.mcgill.genomequebec.premod.SpringBootExecutableJarVfs;
import javax.annotation.PostConstruct;
import javax.servlet.Servlet;
import net.sourceforge.stripes.controller.DynamicMappingFilter;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.vfs.VFS;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Enable Spring Web MVC for REST services.
 */
@Configuration
@EnableWebMvc
public class SpringWebConfiguration extends WebMvcConfigurerAdapter {
  @SuppressWarnings("checkstyle:linelength")
  private static final String STRIPES_ENCRYPTION_KEY =
      "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";
  public static final String STRIPES_FILTER_NAME = "StripesFilter";
  public static final String DYNAMIC_MAPPING_FILTER_NAME = "DynamicMappingFilter";
  public static final String STRIPES_SERVLET_NAME = "StripesServlet";

  /**
   * Registers Stripes VFS.
   */
  @PostConstruct
  public void initialize() {
    VFS.addImplClass(SpringBootExecutableJarVfs.class);
  }

  /**
   * Returns Struts to Stripes URL filter configuration.
   *
   * @return Struts to Stripes URL filter configuration
   */
  @Bean
  public FilterRegistrationBean strutsToStripesFilterFilterRegistration() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(strutsToStripesFilter());
    registration.addUrlPatterns("/*");
    registration.addInitParameter("/welcome.do",
        "ca.mcgill.genomequebec.premod.web.RootActionBean");
    registration.addInitParameter("/contact.do",
        "ca.mcgill.genomequebec.premod.web.ContactActionBean");
    registration.addInitParameter("/welcome/download.do",
        "ca.mcgill.genomequebec.premod.web.DownloadActionBean");
    registration.addInitParameter("/explore/initmodule.do",
        "ca.mcgill.genomequebec.premod.web.module.SearchModuleActionBean");
    registration.setOrder(0);
    return registration;
  }

  @Bean(name = StrutsToStripesFilter.BEAN_NAME)
  public StrutsToStripesFilter strutsToStripesFilter() {
    return new StrutsToStripesFilter();
  }

  /**
   * Returns Struts to Stripes URL filter configuration.
   *
   * @return Struts to Stripes URL filter configuration
   */
  @Bean
  public FilterRegistrationBean redirectOldUrlFilterFilterRegistration() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(redirectOldUrlFilter());
    registration.addUrlPatterns("/*");
    registration.setOrder(1);
    return registration;
  }

  @Bean(name = RedirectOldUrlFilter.BEAN_NAME)
  public RedirectOldUrlFilter redirectOldUrlFilter() {
    return new RedirectOldUrlFilter();
  }

  /**
   * Returns Stripes filter configuration.
   *
   * @return Stripes filter configuration
   */
  @Bean
  public FilterRegistrationBean stripesFilterRegistration() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(stripesFilter());
    registration.addUrlPatterns("*.jsp");
    registration.addInitParameter("Extension.Packages", "ca.mcgill.genomequebec.premod");
    registration.addInitParameter("ActionResolver.Packages", "ca.mcgill.genomequebec.premod");
    registration.addInitParameter("LocalePicker.Locales", "en");
    registration.addInitParameter("LocalizationBundleFactory.ErrorMessageBundle",
        "MessageResources");
    registration.addInitParameter("LocalizationBundleFactory.FieldNameBundle", "MessageResources");
    registration.addInitParameter("Stripes.EncryptionKey", STRIPES_ENCRYPTION_KEY);
    registration.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
    return registration;
  }

  @Bean(name = STRIPES_FILTER_NAME)
  public StripesFilter stripesFilter() {
    return new StripesFilter();
  }

  /**
   * Returns Stripes dynamic filter configuration.
   *
   * @return Stripes dynamic filter configuration
   */
  @Bean
  public FilterRegistrationBean dynamicMappingFilterRegistration() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(dynamicMappingFilter());
    registration.addUrlPatterns("/*");
    registration.setOrder(Ordered.LOWEST_PRECEDENCE);
    return registration;
  }

  @Bean(name = DYNAMIC_MAPPING_FILTER_NAME)
  public DynamicMappingFilter dynamicMappingFilter() {
    return new DynamicMappingFilter();
  }

  @Bean
  public Servlet dispatcherServlet() {
    return new DispatcherServlet();
  }

  /**
   * Returns Spring filter configuration.
   *
   * @return Spring filter configuration
   */
  @Bean
  public ServletRegistrationBean dispatcherServletRegistration() {
    ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet());
    registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
    registration.addUrlMappings("/rest/*");
    registration.addUrlMappings("/actuator/*");
    registration.addUrlMappings("/actuator.json");
    registration.addUrlMappings("/beans/*");
    registration.addUrlMappings("/beans.json");
    registration.addUrlMappings("/autoconfig/*");
    registration.addUrlMappings("/autoconfig.json");
    registration.addUrlMappings("/configprops/*");
    registration.addUrlMappings("/configprops.json");
    registration.addUrlMappings("/dump/*");
    registration.addUrlMappings("/dump.json");
    registration.addUrlMappings("/env/*");
    registration.addUrlMappings("/env.json");
    registration.addUrlMappings("/health/*");
    registration.addUrlMappings("/health.json");
    registration.addUrlMappings("/info/*");
    registration.addUrlMappings("/info.json");
    registration.addUrlMappings("/logfile/*");
    registration.addUrlMappings("/logfile.json");
    registration.addUrlMappings("/mappings/*");
    registration.addUrlMappings("/mappings.json");
    registration.addUrlMappings("/metrics/*");
    registration.addUrlMappings("/metrics.json");
    registration.addUrlMappings("/trace/*");
    registration.addUrlMappings("/trace.json");
    return registration;
  }

  @Bean
  public ServletListenerRegistrationBean<RequestContextListener> requestContextListener() {
    return new ServletListenerRegistrationBean<>(new RequestContextListener());
  }

  @Bean
  public LocaleSetterListener localeSetterListener() {
    return new LocaleSetterListener();
  }
}
