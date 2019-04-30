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

/**
 * Simple implementation of {@link ChromosomeLocation}.
 */
public class SimpleChromosomeLocation implements ChromosomeLocation {
  public String chromosome;
  public Long start;
  public Long end;

  public SimpleChromosomeLocation() {
  }

  public SimpleChromosomeLocation(String chromosome) {
    this.chromosome = chromosome;
  }

  /**
   * Creates a {@link ChromosomeLocation} with chromosome, start and end.
   *
   * @param chromosome
   *          chromosome
   * @param start
   *          start location on chromosome
   * @param end
   *          end location on chromosome
   * @throws IllegalArgumentException
   *           thrown when end &lt; start
   */
  public SimpleChromosomeLocation(String chromosome, Long start, Long end)
      throws IllegalArgumentException {
    if (end < start) {
      throw new IllegalArgumentException("end must be >= start");
    }
    this.chromosome = chromosome;
    this.start = start;
    this.end = end;
  }

  /**
   * Creates a copy of chromosome location.
   *
   * @param chromosomeLocation
   *          chromosome location
   */
  public SimpleChromosomeLocation(ChromosomeLocation chromosomeLocation) {
    this.chromosome = chromosomeLocation.getChromosome();
    this.start = chromosomeLocation.getStart();
    this.end = chromosomeLocation.getEnd();
  }

  /**
   * Returns length of chromosome location.
   *
   * @return length of chromosome location
   */
  public Long getLength() {
    if (start != null && end != null) {
      return end - start;
    } else {
      return null;
    }
  }

  /**
   * Returns the middle of chromosome location.
   *
   * @return the middle of chromosome location
   */
  public Long getMiddle() {
    if (start != null && end != null) {
      return start + (end - start) / 2;
    } else {
      return null;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((chromosome == null) ? 0 : chromosome.hashCode());
    result = prime * result + ((end == null) ? 0 : end.hashCode());
    result = prime * result + ((start == null) ? 0 : start.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    SimpleChromosomeLocation other = (SimpleChromosomeLocation) obj;
    if (chromosome == null) {
      if (other.chromosome != null) {
        return false;
      }
    } else if (!chromosome.equals(other.chromosome)) {
      return false;
    }
    if (end == null) {
      if (other.end != null) {
        return false;
      }
    } else if (!end.equals(other.end)) {
      return false;
    }
    if (start == null) {
      if (other.start != null) {
        return false;
      }
    } else if (!start.equals(other.start)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    if (start != null || end != null) {
      return "chr" + chromosome + ":" + start + "-" + end;
    } else {
      return "chr" + chromosome;
    }
  }

  @Override
  public String getChromosome() {
    return chromosome;
  }

  public void setChromosome(String chromosome) {
    this.chromosome = chromosome;
  }

  @Override
  public Long getStart() {
    return start;
  }

  public void setStart(Long start) {
    this.start = start;
  }

  @Override
  public Long getEnd() {
    return end;
  }

  public void setEnd(Long end) {
    this.end = end;
  }
}
