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

package ca.mcgill.genomequebec.premod.web.ext.resolution;

import ca.mcgill.genomequebec.premod.Main;
import java.util.regex.Pattern;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ForwardResolution;

/**
 * Forwards request to a JSP in the same package as the {@link ActionBean} class.
 */
public class ForwardToJspResolution extends ForwardResolution {
  private static String[] IGNORE_PACKAGES =
      new String[] { Main.class.getPackage().getName() + ".", "web." };

  private static String path(Class<? extends ActionBean> beanType) {
    String path = beanType.getName();
    for (String ignore : IGNORE_PACKAGES) {
      path = path.replaceAll(Pattern.quote(ignore), "");
    }
    return path.replaceAll("\\.", "/");
  }

  /**
   * Forwards request to a JSP having the same name as the {@link ActionBean action bean}'s class.
   * For examples, if class is <code>ca.qc.ircm.user.web.IndexActionBean</code>, the request is
   * forwarded to <code>/WEB-INF/pages/user/IndexActionBean.jsp</code>.
   *
   * @param beanType
   *          action bean's class
   */
  public ForwardToJspResolution(Class<? extends ActionBean> beanType) {
    super("/WEB-INF/pages/" + path(beanType) + ".jsp");
  }

  /**
   * Forwards request to a JSP with a name similar to {@link ActionBean action bean}'s class name.
   * <p>
   * The JSP to which the request is forwarded is computed like this.
   * </p>
   *
   * <pre>
   * "/WEB-INF/pages/" + beanType.getName().replaceAll("\\.", "/") + "_" + classNameExtension
   *     + ".jsp"
   * </pre>
   *
   * <p>
   * For examples, if class is <code>ca.qc.ircm.web.IndexActionBean</code> and classNameExtension is
   * <code>abc</code>, the request is forwarded to
   * <code>/WEB-INF/classes/ca/qc/ircm/web/IndexActionBean_abc.jsp</code>.
   * </p>
   *
   * @param beanType
   *          action bean's class
   * @param classNameExtension
   *          extension to add to action bean's class name
   */
  public ForwardToJspResolution(Class<? extends ActionBean> beanType, String classNameExtension) {
    super("/WEB-INF/pages/" + path(beanType) + "_" + classNameExtension + ".jsp");
  }
}
