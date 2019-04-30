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

import ca.mcgill.genomequebec.premod.web.ext.BaseActionBean;
import ca.mcgill.genomequebec.premod.web.ext.resolution.ForwardToJspResolution;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StrictBinding;
import net.sourceforge.stripes.action.UrlBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Just forward to contact page.
 */
@UrlBinding("/contact")
@StrictBinding
public class ContactActionBean extends BaseActionBean {
  private final Logger logger = LoggerFactory.getLogger(ContactActionBean.class);

  /**
   * Go to contact page.
   * 
   * @return contact page
   */
  @DefaultHandler
  public Resolution input() {
    logger.debug("Go to contact page");
    return new ForwardToJspResolution(this.getClass());
  }
}
