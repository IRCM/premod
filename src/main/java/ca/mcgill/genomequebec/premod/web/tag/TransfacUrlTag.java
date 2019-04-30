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

import ca.mcgill.genomequebec.premod.business.Unit;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import net.sourceforge.stripes.util.UrlBuilder;

/**
 * Generates link to Transfac for unit.
 */
public class TransfacUrlTag extends BodyTagSupport {
  private static final long serialVersionUID = -6418490800186580856L;
  /**
   * Unit.
   */
  private Unit unit;
  /**
   * Page attribute under which link to Transfac is saved.
   */
  private String var;

  /**
   * <p>
   * Generates link to Transfac for unit.
   * </p>
   * <p>
   * Link is saved into page attribute var if var is specified.
   * </p>
   * <p>
   * Otherwise, an anchor tag is create with link.
   * </p>
   */
  @Override
  public int doEndTag() throws JspException {
    // Generate URL.
    UrlBuilder urlBuilder = new UrlBuilder(pageContext.getRequest().getLocale(),
        "http://www.gene-regulation.com/cgi-bin/pub/databases/transfac/getTF.cgi", true);

    // Add matrix parameter.
    if (unit != null) {
      urlBuilder.addParameter("AC", unit.getId());
    }

    if (var != null) {
      // Save URL in page.
      pageContext.setAttribute(var, urlBuilder.toString());
    } else {
      // Write anchor tag.
      try {
        this.pageContext.getOut().write("<a href=\"");
        this.pageContext.getOut().write(urlBuilder.toString());
        this.pageContext.getOut().write("\">");
        // Add body content of tag to jsp.
        if (this.bodyContent != null) {
          this.pageContext.getOut().write(this.bodyContent.getString());
        }
        this.pageContext.getOut().write("</a>");
      } catch (IOException e) {
        throw new JspException(e);
      }
    }

    return super.doEndTag();
  }

  @Override
  public void release() {
    unit = null;
    var = null;
    super.release();
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }
}
