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

package ca.mcgill.genomequebec.premod.web.image;

import java.awt.Color;

/**
 * Helper for Java colors.
 */
public class ColorHelper {

  /**
   * Returns a Color object with the color defined by 6 characters from 0 to F as Internet color are
   * defined.
   * 
   * @param internetColor
   *          String of 6 characters.
   * @return The Java Color corresponding to the Internet Color.
   */
  public static Color getInternetColor(String internetColor) {
    if (internetColor.length() != 6) {
      return null;
    } else {
      String redText = internetColor.substring(0, 2);
      String greenText = internetColor.substring(2, 4);
      String blueText = internetColor.substring(4, 6);

      int red = Integer.parseInt(redText, 16);
      int green = Integer.parseInt(greenText, 16);
      int blue = Integer.parseInt(blueText, 16);

      return new Color(red, green, blue);
    }
  }

}
