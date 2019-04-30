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
 * Comparator for chromosomes. <br>
 * <strong>This comparator does not support any null values.</strong>
 */
public class ChromosomeComparator implements Comparator<Chromosome>, Serializable {
  private static final long serialVersionUID = 1896655709128714395L;

  /**
   * Compare chromosomes as if they where numbers. <br>
   * <strong>Chromosomes that are numbers are always "less" then non-number chromosomes.</strong>
   *
   * @param o1
   *          First chromosome.
   * @param o2
   *          Second chromosome.
   * @return A negative integer, zero, or a positive integer as the first chromosome is less than,
   *         equal to, or greater than the second.
   */
  protected int compareNumber(Chromosome o1, Chromosome o2) {
    // Compare chromosomes number.
    int chr1;
    try {
      if (o1.getChromosome().startsWith("chr")) {
        chr1 = Integer.parseInt(o1.getChromosome().substring(3));
      } else {
        chr1 = Integer.parseInt(o1.getChromosome());
      }
    } catch (NumberFormatException e) {
      chr1 = Integer.MAX_VALUE;
    }
    int chr2;
    try {
      if (o2.getChromosome().startsWith("chr")) {
        chr2 = Integer.parseInt(o2.getChromosome().substring(3));
      } else {
        chr2 = Integer.parseInt(o2.getChromosome());
      }
    } catch (NumberFormatException e) {
      chr2 = Integer.MAX_VALUE;
    }
    return Integer.valueOf(chr1).compareTo(chr2);
  }

  @Override
  public int compare(Chromosome o1, Chromosome o2) {
    int compare = this.compareNumber(o1, o2);
    compare = compare == 0 ? o1.getChromosome().compareToIgnoreCase(o2.getChromosome()) : compare;
    return compare;
  }
}
