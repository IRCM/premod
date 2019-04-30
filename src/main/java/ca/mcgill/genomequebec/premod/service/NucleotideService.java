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

package ca.mcgill.genomequebec.premod.service;

import ca.mcgill.genomequebec.premod.business.Exon;
import ca.mcgill.genomequebec.premod.business.Intron;
import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.business.Organism;
import java.util.List;

/**
 * Services for {@link Nucleotide}.
 */
public interface NucleotideService {
  /**
   * Selects longest nucleotide linked to gene.
   *
   * @param geneName
   *          gene's name
   * @param organism
   *          gene's organism
   * @return longest nucleotide linked to gene
   */
  public Nucleotide longest(String geneName, Organism organism);

  /**
   * Selects longest nucleotide linked to gene.
   *
   * @param geneId
   *          gene's identifier
   * @param organism
   *          gene's organism
   * @return longest nucleotide linked to gene
   */
  public Nucleotide longest(Long geneId, Organism organism);

  /**
   * Selects true if gene exists in database.
   *
   * @param geneName
   *          gene's name
   * @param organism
   *          gene's organism
   * @return true if gene exists
   */
  public boolean existsGeneName(String geneName, Organism organism);

  /**
   * Selects true if gene exists in database.
   *
   * @param geneId
   *          gene's identifier
   * @param organism
   *          gene's organism
   * @return true if gene exists
   */
  public boolean existsGeneId(Long geneId, Organism organism);

  /**
   * Selects all nucleotides that are close to module.
   *
   * @param module
   *          module
   * @return all nucleotides that are close to module
   */
  public List<Nucleotide> inWindow(Module module);

  /**
   * Selects all nucleotides that are close to nucleotide.
   *
   * @param nucleotide
   *          nucleotide
   * @return all nucleotides that are close to nucleotide
   */
  public List<Nucleotide> inWindow(Nucleotide nucleotide);

  /**
   * Selects nucleotide's exons.
   *
   * @param nucleotide
   *          nucleotide
   * @return nucleotide's exons
   */
  public List<Exon> exons(Nucleotide nucleotide);

  /**
   * Selects nucleotide's introns.
   *
   * @param nucleotide
   *          nucleotide
   * @return nucleotide's introns
   */
  public List<Intron> introns(Nucleotide nucleotide);
}
