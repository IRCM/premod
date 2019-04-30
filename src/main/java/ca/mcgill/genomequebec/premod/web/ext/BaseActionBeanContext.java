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

import ca.mcgill.genomequebec.premod.web.RootActionBean;
import ca.mcgill.genomequebec.premod.web.WebContext;
import ca.mcgill.genomequebec.premod.web.ext.resolution.ForwardToJspResolution;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.exception.SourcePageNotFoundException;
import net.sourceforge.stripes.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base action bean context class to use with {@link BaseActionBean}.
 */
public class BaseActionBeanContext extends ActionBeanContext implements WebContext {
  private final Logger logger = LoggerFactory.getLogger(BaseActionBeanContext.class);

  @Override
  public String getContextPath() {
    return this.getRequest().getContextPath();
  }

  /**
   * Log errors.
   */
  public void logErrors() {
    logger.debug("logErrors");
    for (String key : this.getValidationErrors().keySet()) {
      for (ValidationError error : this.getValidationErrors().get(key)) {
        logger.debug("{}: {}", key, error.getMessage(this.getLocale()));
      }
    }
  }

  @Override
  public Resolution getSourcePageResolution() throws SourcePageNotFoundException {
    try {
      return super.getSourcePageResolution();
    } catch (SourcePageNotFoundException e) {
      return new ForwardToJspResolution(RootActionBean.class);
    }
  }
}
