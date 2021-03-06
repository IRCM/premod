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

package ca.mcgill.genomequebec.premod.business;

import java.util.ResourceBundle;

/**
 * Versions of modules.
 */
public enum Version {
  /**
   * Current version.
   */
  LATEST, /**
           * Version of article.
           */
  ARTICLE;
  public String ucscName() {
    ResourceBundle bundle = ResourceBundle.getBundle(Version.class.getName());
    return bundle.getString(name());
  }
}
