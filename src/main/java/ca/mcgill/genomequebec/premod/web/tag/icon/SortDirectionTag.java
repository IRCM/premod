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

package ca.mcgill.genomequebec.premod.web.tag.icon;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import net.sourceforge.stripes.tag.HtmlTagSupport;

/**
 * Adds a sort direction icon to JSP.
 */
public class SortDirectionTag extends HtmlTagSupport {
  private static final String BUNDLE = SortDirectionTag.class.getName();
  /**
   * True if sorting's order is descending.
   */
  private boolean descending;

  @Override
  public int doStartTag() throws JspException {
    return SKIP_BODY;
  }

  @Override
  public int doEndTag() throws JspException {
    String src = pageContext.getServletContext().getContextPath();
    if (!descending) {
      src += "/img/sort/up.gif";
    } else {
      src += "/img/sort/down.gif";
    }
    this.set("src", src);
    String alt = LocaleSupport.getLocalizedMessage(pageContext, "alt." + descending, BUNDLE);
    this.set("alt", alt);
    this.set("width", "10");
    this.set("height", "9");

    JspWriter writer = this.pageContext.getOut();
    this.writeSingletonTag(writer, "img");

    return EVAL_PAGE;
  }

  public boolean isDescending() {
    return descending;
  }

  public void setDescending(boolean descending) {
    this.descending = descending;
  }
}
