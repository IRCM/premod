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

import ca.qc.ircm.bio.ChromosomeLocation;
import ca.qc.ircm.bio.SimpleChromosomeLocation;
import java.io.Serializable;

/**
 * DNA module.
 */
public class Module implements Serializable {
  /**
   * Type of modules.
   */
  public static enum Type {
    PREDICTED
  }

  private static final long serialVersionUID = 84276964577805365L;

  /**
   * Module database identifier.
   */
  private Long id;
  /**
   * Module name.
   */
  private String name;
  /**
   * Data version.
   */
  private Version version;
  /**
   * Type of module.
   */
  private Type type;
  /**
   * Chromosome where this module was found.
   */
  private String chromosome;
  /**
   * Position of the start of the module on the chromosome.
   */
  private Long start;
  /**
   * Position of the end of the module on the chromosome.
   */
  private Long end;
  /**
   * The length of the Module.
   */
  private Long length;
  /**
   * Name of the assembly of the data.
   */
  private String assembly;
  /**
   * Score of this module at the specified location.
   */
  private Double score;
  /**
   * Raw score (before GC content is evaluated) of this module at the specified location.
   */
  private Double rawScore;
  /**
   * The GC Content of the module.
   */
  private Integer gcContent;
  /**
   * Position of the gene that is downstream of this module.
   */
  private Long downstreamGenePosition;
  /**
   * Gene (NCBI) id of the downstream gene.
   */
  private String downstreamGeneLocus;
  /**
   * Name of the downstream gene.
   */
  private String downstreamGeneName;
  /**
   * Position of the gene that is upstream of this module.
   */
  private Long upstreamGenePosition;
  /**
   * Gene (NCBI) id of the downstream gene.
   */
  private String upstreamGeneLocus;
  /**
   * Name of the upstream gene.
   */
  private String upstreamGeneName;
  /**
   * <p>
   * Indicate if a cpg island is present at the place of this module.
   * </p>
   * <p>
   * - if no cpg island computation was made for this module.
   * </p>
   * <p>
   * 0 if no cpg island overlap this module.
   * </p>
   * <p>
   * 1 if at least one cpg island overlap this module.
   * </p>
   */
  private boolean cpgIsland;
  /**
   * <p>
   * Indicate if a TRRD matrix is present at the place of this module.
   * </p>
   * <p>
   * - if TRRD matrix island computation was made for this module.
   * </p>
   * <p>
   * 0 if TRRD matrix island overlap this module.
   * </p>
   * <p>
   * 1 if at least one TRRD matrix overlap this module.
   * </p>
   * <p>
   * This property is not used.
   * </p>
   */
  private String trrdModule;
  /**
   * Organism where this module is found.
   */
  private Organism organism;

  /**
   * Returns the middle position of this Module.
   *
   * @return The middle position of this Module.
   */
  public long getMiddlePosition() {
    long toAdd = Math.abs(end - start) / 2;
    if (start < end) {
      return start + toAdd;
    } else {
      return end + toAdd;
    }
  }

  public ChromosomeLocation getChromosomeLocation() {
    return new SimpleChromosomeLocation(this.getChromosome(), this.getStart(), this.getEnd());
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other instanceof Module) {
      Module otherModule = (Module) other;
      boolean equals = true;
      equals &= getName().equalsIgnoreCase(otherModule.getName());
      equals &= getOrganism() == otherModule.getOrganism();
      return equals;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return getOrganism().hashCode() + getName().toUpperCase().hashCode();
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

  public String getDownstreamGeneLocus() {
    return downstreamGeneLocus;
  }

  public void setDownstreamGeneLocus(String downstreamGeneLocus) {
    this.downstreamGeneLocus = downstreamGeneLocus;
  }

  public String getDownstreamGeneName() {
    return downstreamGeneName;
  }

  public void setDownstreamGeneName(String downstreamGeneName) {
    this.downstreamGeneName = downstreamGeneName;
  }

  public Long getEnd() {
    return end;
  }

  public void setEnd(Long end) {
    this.end = end;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Long getStart() {
    return start;
  }

  public void setStart(Long start) {
    this.start = start;
  }

  public String getTrrdModule() {
    return trrdModule;
  }

  public void setTrrdModule(String trrdModule) {
    this.trrdModule = trrdModule;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getUpstreamGeneLocus() {
    return upstreamGeneLocus;
  }

  public void setUpstreamGeneLocus(String upstreamGeneLocus) {
    this.upstreamGeneLocus = upstreamGeneLocus;
  }

  public String getUpstreamGeneName() {
    return upstreamGeneName;
  }

  public void setUpstreamGeneName(String upstreamGeneName) {
    this.upstreamGeneName = upstreamGeneName;
  }

  public Long getDownstreamGenePosition() {
    return downstreamGenePosition;
  }

  public void setDownstreamGenePosition(Long downstreamGenePosition) {
    this.downstreamGenePosition = downstreamGenePosition;
  }

  public Long getUpstreamGenePosition() {
    return upstreamGenePosition;
  }

  public void setUpstreamGenePosition(Long upstreamGenePosition) {
    this.upstreamGenePosition = upstreamGenePosition;
  }

  public Integer getGcContent() {
    return gcContent;
  }

  public void setGcContent(Integer gcContent) {
    this.gcContent = gcContent;
  }

  public Double getRawScore() {
    return rawScore;
  }

  public void setRawScore(Double rawScore) {
    this.rawScore = rawScore;
  }

  public void setLength(Long length) {
    this.length = length;
  }

  public Organism getOrganism() {
    return organism;
  }

  public void setOrganism(Organism organism) {
    this.organism = organism;
  }

  public Long getLength() {
    return length;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isCpgIsland() {
    return cpgIsland;
  }

  public void setCpgIsland(boolean cpgIsland) {
    this.cpgIsland = cpgIsland;
  }

  public Version getVersion() {
    return version;
  }

  public void setVersion(Version version) {
    this.version = version;
  }
}
