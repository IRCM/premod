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
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.UnitOccurrence;
import ca.mcgill.genomequebec.premod.service.UnitOccurrenceService;
import ca.mcgill.genomequebec.premod.service.UnitService;
import ca.mcgill.genomequebec.premod.web.ext.BaseActionBean;
import ca.mcgill.genomequebec.premod.web.ext.resolution.ForwardToJspResolution;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StrictBinding;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Show module's units and their occurrence locations.
 */
@UrlBinding("/module/unit/{module}")
@StrictBinding
public class ReadModuleUnitActionBean extends BaseActionBean {
  private final Logger logger = LoggerFactory.getLogger(ReadModuleUnitActionBean.class);
  /**
   * Module to show.
   */
  @Validate
  public Module module;
  /**
   * Best units inside module.
   */
  private List<Unit> bestUnits;
  /**
   * Non-best units inside module.
   */
  private List<Unit> nonBestUnits;
  /**
   * Occurrences of units inside module.
   */
  private Map<Unit, List<UnitOccurrence>> unitOccurrences;
  @Inject
  private UnitService unitService;
  @Inject
  private UnitOccurrenceService unitOccurrenceService;

  /**
   * Populate module's best units.
   */
  @Before(stages = LifecycleStage.ResolutionExecution)
  public void populateBestUnits() {
    logger.trace("Populate module's best units for module {}", module);
    if (module != null) {
      bestUnits = unitService.best(module);
    }
  }

  /**
   * Populate module's non-best units.
   */
  @Before(stages = LifecycleStage.ResolutionExecution)
  public void populateNonBestUnits() {
    logger.trace("Populate module's non-best units for module {}", module);
    if (module != null) {
      nonBestUnits = unitService.nonBest(module);
    }
  }

  /**
   * Populate module's unit occurrences.
   */
  @Before(stages = LifecycleStage.ResolutionExecution)
  public void populateUnitOccurrences() {
    logger.trace("Populate module's unit occurrences for module {}", module);
    if (module != null) {
      unitOccurrences = unitOccurrenceService.all(module);
    }
  }

  /**
   * Show module's units and occurrences.
   * 
   * @return show module's units and occurrences page
   */
  @DefaultHandler
  public Resolution show() {
    logger.trace("Show module's units and occurrences for module {}", module);

    return new ForwardToJspResolution(this.getClass());
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public List<Unit> getBestUnits() {
    return bestUnits;
  }

  public List<Unit> getNonBestUnits() {
    return nonBestUnits;
  }

  public Map<Unit, List<UnitOccurrence>> getUnitOccurrences() {
    return unitOccurrences;
  }
}
