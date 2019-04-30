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

package ca.mcgill.genomequebec.premod.persistence.mapper;

import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.qc.ircm.bio.ChromosomeLocation;
import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper for {@link Nucleotide}.
 */
@Mapper
public interface NucleotideMapper {
  public static class Exons implements Serializable {
    private static final long serialVersionUID = -6643101002812829129L;
    private int count;
    private List<Long> exonStarts;
    private List<Long> exonEnds;

    public final int getCount() {
      return count;
    }

    public final void setCount(int count) {
      this.count = count;
    }

    public final List<Long> getExonStarts() {
      return exonStarts;
    }

    public final void setExonStarts(List<Long> exonStarts) {
      this.exonStarts = exonStarts;
    }

    public final List<Long> getExonEnds() {
      return exonEnds;
    }

    public final void setExonEnds(List<Long> exonEnds) {
      this.exonEnds = exonEnds;
    }
  }

  /**
   * Selects all nucleotides linked to gene.
   *
   * @param geneName
   *          gene's name
   * @param organism
   *          gene's organism
   * @return all nucleotides linked to gene
   */
  public List<Nucleotide> selectByGeneName(@Param("geneName") String geneName,
      @Param("organism") Organism organism);

  /**
   * Selects true if gene exists in database.
   *
   * @param geneName
   *          gene's name
   * @param organism
   *          gene's organism
   * @return true if gene exists
   */
  public boolean existsByGeneName(@Param("geneName") String geneName,
      @Param("organism") Organism organism);

  /**
   * Selects all nucleotides linked to gene.
   *
   * @param geneId
   *          gene's identifier
   * @param organism
   *          gene's organism
   * @return all nucleotides linked to gene
   */
  public List<Nucleotide> selectByGeneId(@Param("geneId") Long geneId,
      @Param("organism") Organism organism);

  /**
   * Selects true if gene exists in database.
   *
   * @param geneId
   *          gene's identifier
   * @param organism
   *          gene's organism
   * @return true if gene exists
   */
  public boolean existsByGeneId(@Param("geneId") Long geneId, @Param("organism") Organism organism);

  /**
   * Selects all nucleotides in specified window.
   *
   * @param organism
   *          organism
   * @param window
   *          window
   * @return all nucleotides in specified window
   */
  public List<Nucleotide> selectInWindow(@Param("organism") Organism organism,
      @Param("window") ChromosomeLocation window);

  /**
   * Selects nucleotide's exons.
   *
   * @param nucleotide
   *          nucleotide
   * @return nucleotide's exons
   */
  public Exons selectExonsByNucleotide(Nucleotide nucleotide);

  public Nucleotide selectUpstreamGenePlusByModule(Module module);

  public Nucleotide selectUpstreamGeneMinusByModule(Module module);

  public Nucleotide selectDownstreamGenePlusByModule(Module module);

  public Nucleotide selectDownstreamGeneMinusByModule(Module module);
}
