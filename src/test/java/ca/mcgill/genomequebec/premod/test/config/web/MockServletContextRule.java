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

package ca.mcgill.genomequebec.premod.test.config.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.mock.MockServletContext;
import net.sourceforge.stripes.validation.TypeConverter;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider for MockServletContext that should be used in most action bean tests.
 */
public class MockServletContextRule implements TestRule {
  private final Logger logger = LoggerFactory.getLogger(MockServletContextRule.class);
  private MockServletContext servletContext;
  private Collection<ServletContextListener> servletContextListeners;

  @Override
  public Statement apply(final Statement base, final Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        servletContext = get();
        try {
          base.evaluate();
        } finally {
          destroy(servletContext);
        }
      }
    };
  }

  private MockServletContext get() {
    logger.trace("Configuring Stripes and creating MockServletContext");
    MockServletContext context = new MockServletContext("test");
    context.addFilter(MockStripesRequestWrapperFilter.class, "MockStripesRequestWrapperFilter",
        null);
    // Init parameters.
    // Initialise listeners.
    servletContextListeners = new ArrayList<>();
    ServletContextEvent initEvent = new ServletContextEvent(context);
    for (ServletContextListener listener : servletContextListeners) {
      listener.contextInitialized(initEvent);
    }
    // Add the Stripes Filter
    Map<String, String> filterParams = new HashMap<>();
    filterParams.put("Extension.Packages", "ca.mcgill.genomequebec.premod");
    filterParams.put("ActionResolver.Packages", "ca.mcgill.genomequebec.premod");
    filterParams.put("ActionResolver.Class", TestActionResolver.class.getName());
    filterParams.put("LocalizationBundleFactory.ErrorMessageBundle", "StripesResources");
    filterParams.put("LocalizationBundleFactory.FieldNameBundle", "StripesResources");
    filterParams.put("MultipartWrapperFactory.Class",
        MockMultipartWrapperFactoryWithFiles.class.getName());
    // Non guice extension.
    filterParams.put("TypeConverterFactory.Class", MockTypeConverterFactory.class.getName());
    context.addFilter(StripesFilter.class, "StripesFilter", filterParams);
    // Add the Stripes Dispatcher
    context.setServlet(DispatcherServlet.class, "StripesDispatcher", null);

    return context;
  }

  private void destroy(MockServletContext context) {
    List<Filter> filters = context.getFilters();
    for (Filter filter : filters) {
      filter.destroy();
    }
    @SuppressWarnings("unchecked")
    Enumeration<Servlet> servlets = (Enumeration<Servlet>) context.getServlets();
    while (servlets.hasMoreElements()) {
      servlets.nextElement().destroy();
    }

    // Destroy listeners.
    ServletContextEvent event = new ServletContextEvent(context);
    for (ServletContextListener listener : servletContextListeners) {
      listener.contextDestroyed(event);
    }
  }

  public MockServletContext getServletContext() {
    return servletContext;
  }

  /**
   * Registers action bean.
   *
   * @param action
   *          action bean
   */
  public void registerActionBean(ActionBean action) {
    TestActionResolver resolver =
        (TestActionResolver) StripesFilter.getConfiguration().getActionResolver();
    resolver.registerActionBean(action);
  }

  /**
   * Registers type converter.
   *
   * @param converter
   *          type converter
   * @param forType
   *          type of objects supported by converter
   */
  public <T> void setTypeConverter(TypeConverter<? extends T> converter, Class<T> forType) {
    MockTypeConverterFactory factory =
        (MockTypeConverterFactory) StripesFilter.getConfiguration().getTypeConverterFactory();
    factory.setTypeConverter(converter, forType);
  }
}
