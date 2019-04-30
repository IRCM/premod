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

package ca.mcgill.genomequebec.premod.web;

import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.web.ext.BaseActionBean;
import ca.mcgill.genomequebec.premod.web.ext.resolution.ForwardToJspResolution;
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
 * Just forward to download page.
 */
@UrlBinding("/download/{organism}/{version}")
@StrictBinding
public class DownloadActionBean extends BaseActionBean {
  private final Logger logger = LoggerFactory.getLogger(DownloadActionBean.class);
  /**
   * Organism of modules.
   */
  @Validate
  public Organism organism;
  /**
   * Version of modules.
   */
  @Validate
  public Version version;

  /**
   * Populate default values.
   */
  @Before(stages = LifecycleStage.BindingAndValidation)
  public void populateDefault() {
    logger.trace("Populate default values");
    organism = Organism.HUMAN;
    version = Version.LATEST;
  }

  /**
   * Go to download page.
   * 
   * @return download page
   */
  @DefaultHandler
  public Resolution input() {
    logger.debug("Go to download page for organism {} and version {}", organism, version);
    return new ForwardToJspResolution(this.getClass());
  }

  public Organism getOrganism() {
    return organism;
  }

  public void setOrganism(Organism organism) {
    this.organism = organism;
  }

  public Version getVersion() {
    return version;
  }

  public void setVersion(Version version) {
    this.version = version;
  }
}
