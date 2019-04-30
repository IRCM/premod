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

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test {@link ChromosomeFormat} class.
 */
public class ChromosomeFormatTest {
  private static ChromosomeFormat chromosomeFormat;

  @BeforeClass
  public static void init() {
    chromosomeFormat = new ChromosomeFormat();
  }

  @Test
  public void formatWithLocation() throws Throwable {
    SimpleChromosomeLocation chromosome = new SimpleChromosomeLocation();
    chromosome.setChromosome("1");
    chromosome.setStart(123456L);
    chromosome.setEnd(456789L);
    assertEquals("chr1:123456-456789", chromosomeFormat.format(chromosome));
  }

  @Test
  public void formatWithoutLocation() throws Throwable {
    SimpleChromosomeLocation chromosome = new SimpleChromosomeLocation();
    chromosome.setChromosome("1");
    assertEquals("chr1", chromosomeFormat.format(chromosome));
  }

  @Test(expected = IllegalArgumentException.class)
  public void formatNoEnd() throws Throwable {
    SimpleChromosomeLocation chromosome = new SimpleChromosomeLocation();
    chromosome.setChromosome("1");
    chromosome.setStart(123456L);
    chromosomeFormat.format(chromosome);
  }

  @Test(expected = IllegalArgumentException.class)
  public void formatNoStart() throws Throwable {
    SimpleChromosomeLocation chromosome = new SimpleChromosomeLocation();
    chromosome.setChromosome("1");
    chromosome.setEnd(456789L);
    chromosomeFormat.format(chromosome);
  }

  @Test
  public void parseWithLocation() throws Throwable {
    ChromosomeLocation chromosomeLocation = chromosomeFormat.parse("chr1:123456-456789");
    assertEquals("1", chromosomeLocation.getChromosome());
    assertEquals((Long) 123456L, chromosomeLocation.getStart());
    assertEquals((Long) 456789L, chromosomeLocation.getEnd());
  }

  @Test
  public void parseWithLocationSpacesOnly() throws Throwable {
    ChromosomeLocation chromosomeLocation = chromosomeFormat.parse("chr1 123456 456789");
    assertEquals("1", chromosomeLocation.getChromosome());
    assertEquals((Long) 123456L, chromosomeLocation.getStart());
    assertEquals((Long) 456789L, chromosomeLocation.getEnd());
  }

  @Test
  public void parseWithLocationAndMultipleSpaces() throws Throwable {
    ChromosomeLocation chromosomeLocation = chromosomeFormat.parse("chr1 : 123456 - 456789");
    assertEquals("1", chromosomeLocation.getChromosome());
    assertEquals((Long) 123456L, chromosomeLocation.getStart());
    assertEquals((Long) 456789L, chromosomeLocation.getEnd());
  }

  @Test
  public void parseWithLocationSpacesOnlyAndMultipleSpaces() throws Throwable {
    ChromosomeLocation chromosomeLocation = chromosomeFormat.parse("chr1  123456  456789");
    assertEquals("1", chromosomeLocation.getChromosome());
    assertEquals((Long) 123456L, chromosomeLocation.getStart());
    assertEquals((Long) 456789L, chromosomeLocation.getEnd());
  }

  @Test
  public void parseWithoutLocation() throws Throwable {
    ChromosomeLocation chromosomeLocation = chromosomeFormat.parse("chr1");
    assertEquals("1", chromosomeLocation.getChromosome());
    assertEquals(null, chromosomeLocation.getStart());
    assertEquals(null, chromosomeLocation.getEnd());
  }

  @Test(expected = ParseException.class)
  public void parseError() throws Throwable {
    ChromosomeLocation chromosomeLocation = chromosomeFormat.parse("chr1:abc-def");
    assertEquals("1", chromosomeLocation.getChromosome());
    assertEquals(null, chromosomeLocation.getStart());
    assertEquals(null, chromosomeLocation.getEnd());
  }
}
