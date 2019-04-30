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

package ca.qc.ircm.bio;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares 2 chromosome locations. <br>
 * Locations are compared by chromosome first, then by start and finally by end. <br>
 * <strong>This comparator does not support any null values.</strong>
 */
public class ChromosomeLocationComparator implements Comparator<ChromosomeLocation>, Serializable {
  private static final long serialVersionUID = 1913588967480635206L;
  private final ChromosomeComparator chromosomeComparator = new ChromosomeComparator();

  @Override
  public int compare(ChromosomeLocation o1, ChromosomeLocation o2) {
    int compare = chromosomeComparator.compare(o1, o2);
    compare = compare == 0 ? o1.getStart().compareTo(o2.getStart()) : compare;
    compare = compare == 0 ? o1.getEnd().compareTo(o2.getEnd()) : compare;
    return compare;
  }
}
