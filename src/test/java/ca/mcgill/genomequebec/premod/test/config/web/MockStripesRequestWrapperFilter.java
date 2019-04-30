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

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import net.sourceforge.stripes.controller.StripesRequestWrapper;

/**
 * Request filter that envelops request into a {@link StripesRequestWrapper} that is multipart.
 */
public class MockStripesRequestWrapperFilter implements Filter {
  private class MockStripesRequestWrapper extends HttpServletRequestWrapper {
    public MockStripesRequestWrapper(HttpServletRequest request) {
      super(request);
    }

    @Override
    public String getMethod() {
      return "POST";
    }

    @Override
    public String getContentType() {
      return "multipart/form-data";
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    request = new MockStripesRequestWrapper((HttpServletRequest) request);
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }
}
