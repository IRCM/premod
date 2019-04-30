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
 * mRNA.
 */
public class Nucleotide implements Serializable {
  private static final long serialVersionUID = -5776525426879191218L;
  /**
   * Organism where this module is found.
   */
  private Organism organism;
  /**
   * Acceession number of the mRNA corresponding to this nucleotide.
   */
  private String mrna;
  /**
   * Gene name.
   */
  private String gene;
  /**
   * Chromosome corresponding to the Gene location.
   */
  private String chromosome;
  /**
   * Strand of the chromosome corresponding to the Gene location.
   */
  private String strand;
  /**
   * Gene start location on the chromosome.
   */
  private Long start;
  /**
   * Gene end location on the chromosome.
   */
  private Long end;
  /**
   * Coding start location on chromosome.
   */
  private Long codingStart;
  /**
   * Coding end location on chromosome.
   */
  private Long codingEnd;
  /**
   * Entrez Gene number.
   */
  private Long locusLink;

  /**
   * Returns the length of this Gene (transcriptionnal length).
   *
   * @return The length of this Gene (transcriptionnal length).
   */
  public Long getLength() {
    return end - start;
  }

  /**
   * Returns the middle position of this Gene (transcriptionnal middle).
   *
   * @return The middle position of this Gene (transcriptionnal middle).
   */
  public long getMiddlePosition() {
    return start + (end - start) / 2;
  }

  /**
   * Returns the relative position of this Gene against the position.
   *
   * @param position
   *          The position to use to get the Gene relative position. This position must be in the
   *          same chromosome or results may not be significative.
   * @return The relative position of this Gene against the position.
   */
  public long getRelativePosition(long position) {
    if (strand.equals("+")) {
      return position - start;
    } else if (strand.equals("-")) {
      return end - position;
    }
    return 0;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other instanceof Nucleotide) {
      Nucleotide otherNucleotide = (Nucleotide) other;
      getMrna().equals(otherNucleotide.getMrna());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return mrna.hashCode();
  }

  public String getMrna() {
    return mrna;
  }

  public void setMrna(String mrna) {
    this.mrna = mrna;
  }

  public String getGene() {
    return gene;
  }

  public void setGene(String gene) {
    this.gene = gene;
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

  public Long getStart() {
    return start;
  }

  public void setStart(Long start) {
    this.start = start;
  }

  public Long getEnd() {
    return end;
  }

  public void setEnd(Long end) {
    this.end = end;
  }

  public Long getLocusLink() {
    return locusLink;
  }

  public void setLocusLink(Long locusLink) {
    this.locusLink = locusLink;
  }

  public Long getCodingStart() {
    return codingStart;
  }

  public void setCodingStart(Long codingStart) {
    this.codingStart = codingStart;
  }

  public Long getCodingEnd() {
    return codingEnd;
  }

  public void setCodingEnd(Long codingEnd) {
    this.codingEnd = codingEnd;
  }

  public Organism getOrganism() {
    return organism;
  }

  public void setOrganism(Organism organism) {
    this.organism = organism;
  }
}
