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

import ca.mcgill.genomequebec.premod.business.Version;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Write version to JSP.
 */
public class VersionTag extends SimpleTagSupport {
  private static final String BUNDLE = VersionTag.class.getName();
  /**
   * Version.
   */
  private Object version;

  @Override
  public void doTag() throws JspException, IOException {
    PageContext pageContext = (PageContext) this.getJspContext();

    Version version = this.convert();
    String message;
    if (version != null) {
      message = LocaleSupport.getLocalizedMessage(pageContext, version.name(), BUNDLE);
    } else {
      message = LocaleSupport.getLocalizedMessage(pageContext, "NULL", BUNDLE);
    }
    pageContext.getOut().write(message);
  }

  private Version convert() {
    if (version instanceof Version) {
      return (Version) version;
    } else {
      try {
        return Enum.valueOf(Version.class, String.valueOf(version));
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
  }

  public Object getVersion() {
    return version;
  }

  public void setVersion(Object version) {
    this.version = version;
  }
}
