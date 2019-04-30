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

package ca.mcgill.genomequebec.premod.test;

import java.util.Random;

/**
 * Simply generates a random int and a random long and write them to System.out.
 */
public class RandomGenerator {
  /**
   * Generates random numbers.
   *
   * @param args
   *          not used
   */
  public static void main(String[] args) {
    Random random = new Random();
    System.out.println(random.nextInt());
    System.out.println(random.nextLong());
  }
}
