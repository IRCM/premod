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

package ca.mcgill.genomequebec.premod.web.tag;

import ca.mcgill.genomequebec.premod.business.Organism;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Write organism to JSP.
 */
public class OrganismTag extends SimpleTagSupport {
  private static final String BUNDLE = OrganismTag.class.getName();
  /**
   * Organism.
   */
  private Object organism;

  @Override
  public void doTag() throws JspException, IOException {
    PageContext pageContext = (PageContext) this.getJspContext();

    Organism organism = this.convert();
    String message;
    if (organism != null) {
      message = LocaleSupport.getLocalizedMessage(pageContext, organism.name(), BUNDLE);
    } else {
      message = LocaleSupport.getLocalizedMessage(pageContext, "NULL", BUNDLE);
    }
    pageContext.getOut().write(message);
  }

  private Organism convert() {
    if (organism instanceof Organism) {
      return (Organism) organism;
    } else {
      try {
        return Organism.valueOf(String.valueOf(organism));
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
  }

  public Object getOrganism() {
    return organism;
  }

  public void setOrganism(Object organism) {
    this.organism = organism;
  }
}
