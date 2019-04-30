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

package ca.mcgill.genomequebec.premod.web.ext.handler;

import ca.mcgill.genomequebec.premod.service.error.ErrorSaver;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.exception.DefaultExceptionHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Handler for exception that are unspecific.
 */
public class ExceptionHandler extends DefaultExceptionHandler {
  @Inject
  private ErrorSaver errorSaver;

  /**
   * Log exception.
   *
   * @param exception
   *          Throwable.
   * @param request
   *          Request.
   * @param response
   *          Response
   * @return General error page.
   */
  public Resolution logger(Throwable exception, HttpServletRequest request,
      HttpServletResponse response) {
    if (errorSaver == null) {
      WebApplicationContext context =
          WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
      context.getAutowireCapableBeanFactory().autowireBean(this);
    }
    errorSaver.saveError(exception, request);
    return new ExceptionHandlerResolution();
  }
}
