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

package ca.mcgill.genomequebec.premod.web.module;

import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import ca.mcgill.genomequebec.premod.web.ext.BaseActionBean;
import javax.inject.Inject;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StrictBinding;
import net.sourceforge.stripes.action.UrlBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Show a random module.
 */
@UrlBinding("/module/random")
@StrictBinding
public class ReadRandomModuleActionBean extends BaseActionBean {
  private final Logger logger = LoggerFactory.getLogger(ReadRandomModuleActionBean.class);
  @Inject
  private ModuleService moduleService;

  /**
   * Show a random module.
   * 
   * @return redirect to {@link ReadModuleActionBean}.
   */
  @DefaultHandler
  public Resolution show() {
    logger.debug("Show a random module");

    // Get a random module.
    Module module = moduleService.random();

    // Redirect to show module with random module.
    RedirectResolution resolution = new RedirectResolution(ReadModuleActionBean.class);
    resolution.addParameter("module", module);
    return resolution;
  }
}
