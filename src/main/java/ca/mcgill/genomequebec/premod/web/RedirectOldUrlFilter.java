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

import ca.mcgill.genomequebec.premod.web.unit.UnitStatisticActionBean;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.RedirectResolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Redirects request made using old URLs.
 */
public class RedirectOldUrlFilter implements Filter {
  public static final String BEAN_NAME = "redirectOldUrlFilter";
  private final Logger logger = LoggerFactory.getLogger(RedirectOldUrlFilter.class);

  /**
   * Redirect request made to struts to stripes equivalent.
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    if (redirectRequest(httpRequest) != null) {
      logger.trace("Redirect user from {} to {}", httpRequest.getServletPath(),
          redirectRequest(httpRequest));
      httpResponse.sendRedirect(httpRequest.getContextPath() + redirectRequest(httpRequest));
    } else {
      filterChain.doFilter(request, response);
    }
  }

  /**
   * Returns redirect URL based on request.
   *
   * @param request
   *          request
   * @return redirect URL based on request
   */
  public String redirectRequest(HttpServletRequest request) {
    if (Pattern.matches("/welcome", request.getServletPath())) {
      return "/";
    } else if (Pattern.matches("/module/context/\\w+/\\w+", request.getServletPath())) {
      Matcher matcher =
          Pattern.compile("/module/context/(\\w+)/(\\w+)").matcher(request.getServletPath());
      matcher.matches();
      return "/module/" + matcher.group(1) + "-" + matcher.group(2) + "#context";
    } else if (Pattern.matches("/module/unit/\\w+/\\w+", request.getServletPath())) {
      Matcher matcher =
          Pattern.compile("/module/unit/(\\w+)/(\\w+)").matcher(request.getServletPath());
      matcher.matches();
      return "/module/unit/" + matcher.group(1) + "-" + matcher.group(2);
    } else if (Pattern.matches("/module/\\w+/\\w+", request.getServletPath())) {
      Matcher matcher = Pattern.compile("/module/(\\w+)/(\\w+)").matcher(request.getServletPath());
      matcher.matches();
      if (!matcher.group(1).equals("unit") && !matcher.group(1).equals("context")) {
        return "/module/" + matcher.group(1) + "-" + matcher.group(2);
      }
    } else if (Pattern.matches("/unit/statistic/excel/\\w+/\\w+", request.getServletPath())) {
      Matcher matcher =
          Pattern.compile("/unit/statistic/excel/(\\w+)/(\\w+)").matcher(request.getServletPath());
      matcher.matches();
      RedirectResolution resolution =
          new RedirectResolution(UnitStatisticActionBean.class, "excel");
      resolution.addParameter("organism", matcher.group(1).toUpperCase());
      resolution.addParameter("version", matcher.group(2));
      return resolution.getUrl(request.getLocale());
    }
    return null;
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
  }

  @Override
  public void destroy() {
  }
}
