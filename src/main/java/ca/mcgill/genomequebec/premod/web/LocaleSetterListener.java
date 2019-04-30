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

import java.util.Locale;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.jsp.jstl.core.Config;

/**
 * Set Locale information on new session.
 */
public class LocaleSetterListener implements HttpSessionListener {

  /**
   * Visited attribute.
   */
  public static final String VISITED = "visited";

  /**
   * Default Locale for the program.
   */
  public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

  @Override
  public void sessionCreated(HttpSessionEvent event) {
    setLocale(event.getSession(), DEFAULT_LOCALE);
  }

  protected void setLocale(HttpSession session, Locale locale) {
    Config.set(session, Config.FMT_LOCALE, locale);
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent event) {
  }
}
