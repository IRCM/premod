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

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.ConfigurableComponent;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Inject Spring dependencies in action beans.
 */
@Intercepts(LifecycleStage.ActionBeanResolution)
public class SpringInjectionEnricher implements Interceptor, ConfigurableComponent {
  private WebApplicationContext context;

  @Override
  public Resolution intercept(ExecutionContext ctx) throws Exception {
    Resolution resolution = ctx.proceed();
    ActionBean bean = ctx.getActionBean();

    inject(context, bean);

    return resolution;
  }

  /**
   * Inject beans into object instance.
   *
   * @param context
   *          Spring's context
   * @param instance
   *          the object instance
   */
  private void inject(ApplicationContext context, Object instance) {
    context.getAutowireCapableBeanFactory().autowireBean(instance);
  }

  @Override
  public void init(Configuration configuration) {
    context = WebApplicationContextUtils
        .getRequiredWebApplicationContext(configuration.getServletContext());
  }
}
