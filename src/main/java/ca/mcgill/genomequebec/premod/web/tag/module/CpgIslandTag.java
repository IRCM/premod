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

package ca.mcgill.genomequebec.premod.web.tag.module;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Write if module overlap a CpG island to JSP.
 */
public class CpgIslandTag extends SimpleTagSupport {
  private static final String BUNDLE = CpgIslandTag.class.getName();
  /**
   * Organism.
   */
  private Boolean cpgIsland;

  @Override
  public void doTag() throws JspException, IOException {
    PageContext pageContext = (PageContext) this.getJspContext();

    String message;
    if (cpgIsland != null) {
      message = LocaleSupport.getLocalizedMessage(pageContext, String.valueOf(cpgIsland), BUNDLE);
    } else {
      message = LocaleSupport.getLocalizedMessage(pageContext, "NULL", BUNDLE);
    }
    pageContext.getOut().write(message);
  }

  public Boolean getCpgIsland() {
    return cpgIsland;
  }

  public void setCpgIsland(Boolean cpgIsland) {
    this.cpgIsland = cpgIsland;
  }
}
