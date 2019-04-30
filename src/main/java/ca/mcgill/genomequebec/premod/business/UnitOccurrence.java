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

import java.io.Serializable;

/**
 * <p>
 * This class represents an occurrence (match) of a matrix on a chromosome.
 * </p>
 */
public class UnitOccurrence implements Serializable {
  private static final long serialVersionUID = -8730476829930063932L;

  /**
   * The Unit (matrix) of this occurrence.
   */
  private Unit unit;

  /**
   * Organism.
   */
  private Organism organism;

  /**
   * Data version.
   */
  private Version version;

  /**
   * Chromosome where the occurrence was found.
   */
  private String chromosome;

  /**
   * Position of the beginning of the matrix occurrence.
   */
  private long start;

  /**
   * Position of the end of the matrix occurrence.
   */
  private long end;

  /**
   * <p>
   * The strand where the matrix is found.
   * </p>
   * <p>
   * It may be the positive or the negative strand (+ or -).
   * </p>
   */
  private String strand;

  /**
   * Name of the assembly of the data.
   */
  private String assembly;

  /**
   * <p>
   * Score of the matrix occurrence.
   * </p>
   * <p>
   * A high score indicates that the chromosome respects the defined matrix very well.
   * </p>
   */
  private Double occScore;

  /**
   * <p>
   * Score of the matrix for the module in property module.
   * </p>
   * <p>
   * The score correspond to the total score of all occurrences of the matrix for the Module.
   * </p>
   */
  private Double totalScore;

  /**
   * <p>
   * The Module where the matrix occurrence was found.
   * </p>
   * <p>
   * This should not be used.
   * </p>
   */
  private Module module;

  /**
   * <p>
   * Shows the "interesting" value of the matrix occurrence for the Module.
   * </p>
   * <p>
   * Note that this value is 0 if it is not in the first 5 matrix with best score for this module.
   * </p>
   * <p>
   * The value is the position of this matrix in the best scoring matrix for this module. The value
   * 1 is the best scoring matrix.
   * </p>
   */
  private String tagNumber;

  /**
   * Returns the length of this UnitOccurrence.
   * 
   * @return The length of this UnitOccurrence.
   */
  public long getLength() {
    if (start < end) {
      return end - start;
    } else {
      return start - end;
    }
  }

  public String getAssembly() {
    return assembly;
  }

  public void setAssembly(String assembly) {
    this.assembly = assembly;
  }

  public String getChromosome() {
    return chromosome;
  }

  public void setChromosome(String chromosome) {
    this.chromosome = chromosome;
  }

  public String getStrand() {
    return strand;
  }

  public void setStrand(String strand) {
    this.strand = strand;
  }

  public Version getVersion() {
    return version;
  }

  public void setVersionId(Version version) {
    this.version = version;
  }

  public long getEnd() {
    return end;
  }

  public void setEnd(long end) {
    this.end = end;
  }

  public long getStart() {
    return start;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }

  public Double getOccScore() {
    return occScore;
  }

  public void setOccScore(Double occScore) {
    this.occScore = occScore;
  }

  public Double getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(Double totalScore) {
    this.totalScore = totalScore;
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public String getTagNumber() {
    return tagNumber;
  }

  public void setTagNumber(String tagNumber) {
    this.tagNumber = tagNumber;
  }

  public Organism getOrganism() {
    return organism;
  }

  public void setOrganism(Organism organism) {
    this.organism = organism;
  }

  public Unit getUnit() {
    return unit;
  }
}
