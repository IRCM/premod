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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test {@link SimpleChromosomeLocation} class.
 */
public class SimpleChromosomeLocationTest {
  @Test
  public void chromosomeOnlyConstructor() throws Throwable {
    SimpleChromosomeLocation location = new SimpleChromosomeLocation("1");
    assertEquals("1", location.getChromosome());
    assertEquals(null, location.getStart());
    assertEquals(null, location.getEnd());
  }

  @Test
  public void chromosomeAndLocationConstructor() throws Throwable {
    SimpleChromosomeLocation location = new SimpleChromosomeLocation("1", 123456L, 456789L);
    assertEquals("1", location.getChromosome());
    assertEquals((Long) 123456L, location.getStart());
    assertEquals((Long) 456789L, location.getEnd());
  }

  @Test
  public void testEquals() throws Throwable {
    SimpleChromosomeLocation location1 = new SimpleChromosomeLocation("1", 123456L, 456789L);
    SimpleChromosomeLocation location2 = new SimpleChromosomeLocation("1", 123456L, 456789L);
    assertEquals(true, location1.equals(location2));

    location1 = new SimpleChromosomeLocation("1", 123456L, 456789L);
    location2 = new SimpleChromosomeLocation("1", 123457L, 456789L);
    assertEquals(false, location1.equals(location2));
    location1 = new SimpleChromosomeLocation("1", 123456L, 456789L);
    location2 = new SimpleChromosomeLocation("1", 123456L, 456788L);
    assertEquals(false, location1.equals(location2));
    location1 = new SimpleChromosomeLocation("1", 123456L, 456789L);
    location2 = new SimpleChromosomeLocation("2", 123456L, 456789L);
    assertEquals(false, location1.equals(location2));

    location1 = new SimpleChromosomeLocation("1");
    location2 = new SimpleChromosomeLocation("1");
    assertEquals(true, location1.equals(location2));

    location1 = new SimpleChromosomeLocation("1");
    location2 = new SimpleChromosomeLocation("2");
    assertEquals(false, location1.equals(location2));

    location1 = new SimpleChromosomeLocation();
    location2 = new SimpleChromosomeLocation();
    assertEquals(true, location1.equals(location2));
  }

  @Test
  public void testHashCode() throws Throwable {
    SimpleChromosomeLocation location1 = new SimpleChromosomeLocation("1", 123456L, 456789L);
    SimpleChromosomeLocation location2 = new SimpleChromosomeLocation("1", 123456L, 456789L);
    assertEquals(location1.hashCode(), location2.hashCode());

    location1 = new SimpleChromosomeLocation("1", 123456L, 456789L);
    location2 = new SimpleChromosomeLocation("1", 123457L, 456789L);
    assertNotSame(location1.hashCode(), location2.hashCode());
    location1 = new SimpleChromosomeLocation("1", 123456L, 456789L);
    location2 = new SimpleChromosomeLocation("1", 123456L, 456788L);
    assertNotSame(location1.hashCode(), location2.hashCode());
    location1 = new SimpleChromosomeLocation("1", 123456L, 456789L);
    location2 = new SimpleChromosomeLocation("2", 123456L, 456789L);
    assertNotSame(location1.hashCode(), location2.hashCode());

    location1 = new SimpleChromosomeLocation("1");
    location2 = new SimpleChromosomeLocation("1");
    assertEquals(location1.hashCode(), location2.hashCode());

    location1 = new SimpleChromosomeLocation("1");
    location2 = new SimpleChromosomeLocation("2");
    assertNotSame(location1.hashCode(), location2.hashCode());

    location1 = new SimpleChromosomeLocation();
    location2 = new SimpleChromosomeLocation();
    assertEquals(location1.hashCode(), location2.hashCode());
  }

  @Test
  public void testToString() throws Throwable {
    SimpleChromosomeLocation location = new SimpleChromosomeLocation("1", 123456L, 456789L);
    assertEquals("chr1:123456-456789", location.toString());

    location = new SimpleChromosomeLocation("1");
    assertEquals("chr1", location.toString());

    location = new SimpleChromosomeLocation();
    try {
      location.toString();
    } catch (Throwable t) {
      fail("Not exception should be thrown on toString()");
    }
  }

  @Test
  public void testLength() throws Throwable {
    SimpleChromosomeLocation location = new SimpleChromosomeLocation("1", 123456L, 456789L);
    assertEquals((Long) 333333L, location.getLength());

    location = new SimpleChromosomeLocation("1");
    assertEquals(null, location.getLength());
  }

  @Test
  public void testMiddle() throws Throwable {
    SimpleChromosomeLocation location = new SimpleChromosomeLocation("1", 123456L, 456790L);
    assertEquals((Long) 290123L, location.getMiddle());

    location = new SimpleChromosomeLocation("1");
    assertEquals(null, location.getMiddle());
  }
}
