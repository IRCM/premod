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

package ca.mcgill.genomequebec.premod.web.ext;

import ca.mcgill.genomequebec.premod.Main;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

/**
 * Interceptor that sets action bean's class simple name into <code>actionBeanClassName</code>
 * request attribute.
 */
@Intercepts(LifecycleStage.ActionBeanResolution)
public class FmtBundleNameSetterInterceptor implements Interceptor {
  private static String[] IGNORE_PACKAGES =
      new String[] { Main.class.getPackage().getName() + ".", "web." };

  private static String path(Class<? extends ActionBean> beanType) {
    String path = beanType.getName();
    for (String ignore : IGNORE_PACKAGES) {
      path = path.replaceAll(Pattern.quote(ignore), "");
    }
    return path.replaceAll("\\.", "/");
  }

  @Override
  public Resolution intercept(ExecutionContext executionContext) throws Exception {
    Resolution resolution = executionContext.proceed();
    HttpServletRequest request = executionContext.getActionBeanContext().getRequest();
    if (request.getAttribute("fmtBundle") == null) {
      ActionBean actionBean = executionContext.getActionBean();
      request.setAttribute("fmtBundle", "WEB-INF.pages." + path(actionBean.getClass()));
    }
    return resolution;
  }
}
