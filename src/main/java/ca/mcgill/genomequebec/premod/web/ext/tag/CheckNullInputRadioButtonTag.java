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

package ca.mcgill.genomequebec.premod.web.ext.tag;

import javax.servlet.jsp.JspException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Same as Stripes {@link net.sourceforge.stripes.tag.InputRadioButtonTag} except that if value in
 * action bean is null, a radio button with null will be checked if present.
 */
public class CheckNullInputRadioButtonTag extends net.sourceforge.stripes.tag.InputRadioButtonTag {

  private Logger logger = LoggerFactory.getLogger(CheckNullInputRadioButtonTag.class);

  @Override
  public int doEndInputTag() throws JspException {
    Object actualChecked = getSingleOverrideValue();

    logger.debug("ActualChecked:{}, Value:{}", actualChecked, this.getValue());

    // Now if the "checked" value matches this tags value, check it!
    if (actualChecked == null && (this.getValue() == null || "".equals(this.getValue()))) {
      getAttributes().put("checked", "checked");
    }

    return super.doEndInputTag();
  }
}
