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

package ca.mcgill.genomequebec.premod.web.unit;

import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.persistence.Conjunction;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import ca.mcgill.genomequebec.premod.web.ext.BaseActionBean;
import ca.mcgill.genomequebec.premod.web.module.SearchModuleActionBean;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StrictBinding;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Show all modules having unit as tag.
 */
@UrlBinding("/unit/module/{unit}/{organism}/{version}")
@StrictBinding
public class ModuleByUnitActionBean extends BaseActionBean {
  private final Logger logger = LoggerFactory.getLogger(ModuleByUnitActionBean.class);
  /**
   * Tag unit of modules to show.
   */
  @Validate
  public Unit unit;
  /**
   * Organism of modules to show.
   */
  @Validate
  public Organism organism;
  /**
   * Version of modules to show.
   */
  @Validate
  public Version version;

  /**
   * Execute a search modules by selected unit.
   * 
   * @return redirect to search module
   */
  @DefaultHandler
  public Resolution input() {
    logger.debug("Search modules of tag unit {}", unit);
    RedirectResolution resolution = new RedirectResolution(SearchModuleActionBean.class, "search");
    resolution.addParameter("searchType", SearchModuleActionBean.SearchType.UNIT);
    resolution.addParameter("organism", organism);
    resolution.addParameter("version", version);
    resolution.addParameter("unitIds", unit.getId());
    resolution.addParameter("unitConjunction", Conjunction.AND);
    resolution.addParameter("unitTag", Unit.TagType.TAG);
    resolution.addParameter("sort", ModuleService.Sort.SCORE);
    resolution.addParameter("descending", true);
    resolution.addParameter("format", SearchModuleActionBean.Format.HTML);
    resolution.addParameter("page", 1);
    return resolution;
  }
}
