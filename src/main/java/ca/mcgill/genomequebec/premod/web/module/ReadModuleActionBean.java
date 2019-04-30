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
import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import ca.mcgill.genomequebec.premod.service.NucleotideService;
import ca.mcgill.genomequebec.premod.service.UnitService;
import ca.mcgill.genomequebec.premod.web.WebConstants;
import ca.mcgill.genomequebec.premod.web.ext.BaseActionBean;
import ca.mcgill.genomequebec.premod.web.ext.resolution.ForwardToJspResolution;
import ca.mcgill.genomequebec.premod.web.image.ModuleContextImage;
import ca.mcgill.genomequebec.premod.web.image.ModuleContextImageFactory;
import ca.mcgill.genomequebec.premod.web.image.ModuleImage;
import ca.mcgill.genomequebec.premod.web.image.ModuleImageFactory;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.StrictBinding;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Show module.
 */
@UrlBinding("/module/{module}")
@StrictBinding
public class ReadModuleActionBean extends BaseActionBean {
  /**
   * Maximum number of additional units that can be shown in graphic.
   */
  public static final int MAX_ADDITIONNAL_UNITS = ModuleImage.MAX_ADDITIONNAL_UNITS;

  private final Logger logger = LoggerFactory.getLogger(ReadModuleActionBean.class);
  /**
   * Module to show.
   */
  @Validate
  public Module module;
  /**
   * Additional units to show in module graphic.
   */
  @Validate
  public List<Unit> units;
  /**
   * Module's sequence.
   */
  private String sequence;
  /**
   * Bests units for module.
   */
  private List<Unit> bestUnits;
  /**
   * Non-best units for module.
   */
  private List<Unit> nonBestUnits;
  /**
   * Extra information for units.
   */
  private Map<Unit, UnitService.Extra> unitExtras;
  /**
   * Nucleotides near module.
   */
  private List<Nucleotide> nucleotides;
  /**
   * Relative position of nucleotides against module.
   */
  private Map<Nucleotide, Long> nucleotidesRelativePosition;
  /**
   * Modules near module.
   */
  private List<Module> modules;
  /**
   * Module image.
   */
  private ModuleImage moduleImage;
  /**
   * Module context image.
   */
  private ModuleContextImage contextImage;
  @Inject
  private ModuleService moduleService;
  @Inject
  private UnitService unitService;
  @Inject
  private NucleotideService nucleotideService;
  @Inject
  private ModuleImageFactory moduleImageFactory;
  @Inject
  private ModuleContextImageFactory moduleContextImageFactory;

  /**
   * Populate units for module.
   */
  @Before(stages = LifecycleStage.ResolutionExecution)
  public void populateUnits() {
    logger.trace("Populate units for module");
    if (module != null) {
      bestUnits = unitService.best(module);
      nonBestUnits = unitService.nonBest(module);
      unitExtras = unitService.extras(module);
    }
  }

  /**
   * Populate nucleotides near module.
   */
  @Before(stages = LifecycleStage.ResolutionExecution)
  public void populateNucleotides() {
    logger.trace("Populate nucleotides near module");
    if (module != null) {
      nucleotides = nucleotideService.inWindow(module);
      nucleotidesRelativePosition = new HashMap<Nucleotide, Long>();
      for (Nucleotide nucleotide : nucleotides) {
        // Get gene relative position against module.
        long relativePosition = nucleotide.getRelativePosition(module.getMiddlePosition());
        nucleotidesRelativePosition.put(nucleotide, relativePosition);
      }
    }
  }

  /**
   * Populate modules near module.
   */
  @Before(stages = LifecycleStage.ResolutionExecution)
  public void populateModules() {
    logger.trace("Populate modules near module");
    if (module != null) {
      modules = moduleService.inWindow(module);
    }
  }

  /**
   * Populate module's sequence.
   */
  @Before(stages = LifecycleStage.ResolutionExecution)
  public void populateSequence() {
    logger.trace("Populate module's sequence");
    if (module != null) {
      sequence = moduleService.sequence(module);
    }
  }

  /**
   * Show module.
   * 
   * @return show module page
   */
  @DefaultHandler
  public Resolution show() {
    logger.debug("Show module {}", module);

    // Generate module's image.
    if (module != null) {
      moduleImage = moduleImageFactory.create(module, units, context.getLocale());
    }
    // Get context image.
    if (module != null) {
      contextImage = moduleContextImageFactory.create(module, context);
    }

    return new ForwardToJspResolution(this.getClass());
  }

  /**
   * Send module image to user.
   * 
   * @return module's image
   */
  public Resolution showImage() {
    logger.debug("Show module image for module {} with units {}", module, units);

    // Generate module's image.
    if (module != null) {
      moduleImage = moduleImageFactory.create(module, units, context.getLocale());
      return new StreamingResolution("image/" + WebConstants.IMAGE_TYPE) {
        @Override
        protected void stream(HttpServletResponse response) throws Exception {
          OutputStream output = response.getOutputStream();
          try {
            moduleImage.printPlotToOutputStream(output, WebConstants.IMAGE_TYPE);
          } finally {
            output.close();
          }
        }
      };
    } else {
      return new StreamingResolution("image/" + WebConstants.IMAGE_TYPE, new StringReader(""));
    }
  }

  /**
   * Send context image to user.
   * 
   * @return context image
   */
  public Resolution showContextImage() {
    logger.trace("Show context image for module {}", module);

    if (module != null) {
      contextImage = moduleContextImageFactory.create(module, context);
      return new StreamingResolution("image/" + WebConstants.IMAGE_TYPE) {
        @Override
        protected void stream(HttpServletResponse response) throws Exception {
          OutputStream output = response.getOutputStream();
          try {
            contextImage.printPlotToOutputStream(output, WebConstants.IMAGE_TYPE);
          } finally {
            output.close();
          }
        }
      };
    } else {
      return new StreamingResolution("image/" + WebConstants.IMAGE_TYPE, new StringReader(""));
    }
  }

  /**
   * Returns sequence's name for module.
   * 
   * @return sequence's name for module
   */
  public String getSequenceName() {
    StringBuilder builder = new StringBuilder();
    builder.append("gi|");
    if (module != null) {
      builder.append(module.getName());
      builder.append(" ");
      builder.append(module.getChromosome());
      builder.append(":");
      builder.append(module.getStart());
      builder.append("-");
      builder.append(module.getEnd());
      builder.append(" (");
      builder.append(module.getAssembly());
      builder.append(")");
    }
    return builder.toString();
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

  public List<Nucleotide> getNucleotides() {
    return nucleotides;
  }

  public Map<Nucleotide, Long> getNucleotidesRelativePosition() {
    return nucleotidesRelativePosition;
  }

  public String getSequence() {
    return sequence;
  }

  public ModuleImage getModuleImage() {
    return moduleImage;
  }

  public Map<Unit, UnitService.Extra> getUnitExtras() {
    return unitExtras;
  }

  public List<Unit> getUnits() {
    return units;
  }

  public void setUnits(List<Unit> units) {
    this.units = units;
  }

  public ModuleContextImage getContextImage() {
    return contextImage;
  }

  public List<Module> getModules() {
    return modules;
  }
}
