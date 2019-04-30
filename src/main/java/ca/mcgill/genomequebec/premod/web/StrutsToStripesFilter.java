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

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.StripesFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Redirect request made from Struts to Stripes equivalent.
 */
public class StrutsToStripesFilter implements Filter {
  public static final String BEAN_NAME = "strutsToStripesFilter";
  private Logger logger = LoggerFactory.getLogger(StrutsToStripesFilter.class);

  /**
   * Mapping of Struts actions to Stripes action beans.
   */
  private Map<String, String> redirections;

  /**
   * Redirect request made to struts to stripes equivalent.
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      HttpServletResponse httpResponse = (HttpServletResponse) response;
      if (redirections.containsKey(httpRequest.getServletPath())) {
        String className = redirections.get(httpRequest.getServletPath());
        try {
          Class<?> clazz = Class.forName(className);
          if (clazz == null || !ActionBean.class.isAssignableFrom(clazz)) {
            logger.warn("Redirect destination {} is not an action bean", className);
            filterChain.doFilter(request, response);
          }
          logger.info("Redirect request from {} to {}", httpRequest.getServletPath(), className);
          @SuppressWarnings("unchecked")
          Class<? extends ActionBean> actionBean = (Class<? extends ActionBean>) clazz;
          String url =
              StripesFilter.getConfiguration().getActionResolver().getUrlBinding(actionBean);
          httpResponse.sendRedirect(httpRequest.getContextPath() + url);
        } catch (ClassNotFoundException e) {
          logger.warn("Redirect destination {} is not a class", className);
          filterChain.doFilter(request, response);
        }
      } else {
        // Filter does not apply.
        filterChain.doFilter(request, response);
      }
    } else {
      // Filter does not apply.
      filterChain.doFilter(request, response);
    }
  }

  /**
   * Find all redirections to Stripes.
   */
  @Override
  public void init(FilterConfig config) throws ServletException {
    redirections = new HashMap<>();
    Enumeration<String> names = config.getInitParameterNames();
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      String value = config.getInitParameter(name);
      logger.info("Found redirection from {} to {}", name, value);
      redirections.put(name, value);
    }
  }

  @Override
  public void destroy() {
  }
}
