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

import java.util.HashMap;
import java.util.Map;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.ActionResolver;
import net.sourceforge.stripes.controller.NameBasedActionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ActionResolver} that return specified instance of ActionBean.
 */
public class TestActionResolver extends NameBasedActionResolver {
  private final Logger logger = LoggerFactory.getLogger(TestActionResolver.class);
  private Map<Class<? extends ActionBean>, ActionBean> registeredBeans =
      new HashMap<Class<? extends ActionBean>, ActionBean>();

  @Override
  protected ActionBean makeNewActionBean(Class<? extends ActionBean> type,
      ActionBeanContext context) throws Exception {
    ActionBean actionBean;
    if (registeredBeans.containsKey(type)) {
      actionBean = registeredBeans.get(type);
      logger.debug("Use registered action bean {} for type {}", actionBean, type);
    } else {
      actionBean = super.makeNewActionBean(type, context);
      logger.debug("Use super action bean {} for type {}", actionBean, type);
    }
    return actionBean;
  }

  public void clear() {
    logger.debug("Clear register action beans");
    registeredBeans.clear();
  }

  public void registerActionBean(ActionBean action) {
    logger.debug("Register action bean {} for type {}", action, action.getClass().getName());
    registeredBeans.put(action.getClass(), action);
  }
}
