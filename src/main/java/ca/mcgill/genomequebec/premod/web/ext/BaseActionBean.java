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
import net.sourceforge.stripes.action.ActionBeanContext;

/**
 * Abstract base class for action beans.
 */
public abstract class BaseActionBean implements ActionBean {
  protected BaseActionBeanContext context;

  @Override
  public BaseActionBeanContext getContext() {
    return context;
  }

  @Override
  public void setContext(ActionBeanContext context) {
    if (context instanceof BaseActionBeanContext) {
      this.context = (BaseActionBeanContext) context;
    } else {
      throw new IllegalStateException("context must be of type " + BaseActionBeanContext.class);
    }
  }
}
