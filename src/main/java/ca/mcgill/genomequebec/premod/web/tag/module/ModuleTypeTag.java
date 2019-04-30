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

import ca.mcgill.genomequebec.premod.business.Module.Type;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Write module type to JSP.
 */
public class ModuleTypeTag extends SimpleTagSupport {
  private static final String BUNDLE = ModuleTypeTag.class.getName();
  /**
   * Module type.
   */
  private Object type;

  @Override
  public void doTag() throws JspException, IOException {
    PageContext pageContext = (PageContext) this.getJspContext();

    Type type = this.convert();
    String message;
    if (type != null) {
      message = LocaleSupport.getLocalizedMessage(pageContext, type.name(), BUNDLE);
    } else {
      message = LocaleSupport.getLocalizedMessage(pageContext, "NULL", BUNDLE);
    }
    pageContext.getOut().write(message);
  }

  private Type convert() {
    if (type instanceof Type) {
      return (Type) type;
    } else {
      try {
        return Type.valueOf(String.valueOf(type));
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
  }

  public Object getType() {
    return type;
  }

  public void setType(Object type) {
    this.type = type;
  }
}
