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

import ca.mcgill.genomequebec.premod.Constants;
import ca.mcgill.genomequebec.premod.business.Exon;
import ca.mcgill.genomequebec.premod.business.Intron;
import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.persistence.mapper.NucleotideMapper;
import ca.qc.ircm.bio.ChromosomeLocation;
import ca.qc.ircm.bio.SimpleChromosomeLocation;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of services for {@link Nucleotide}.
 */
@Service
@Transactional
public class NucleotideServiceDefault implements NucleotideService {
  @Inject
  private NucleotideMapper nucleotideMapper;

  @Override
  public Nucleotide longest(String geneName, Organism organism) {
    List<Nucleotide> nucleotides = nucleotideMapper.selectByGeneName(geneName, organism);
    Nucleotide longest = null;
    for (Nucleotide nucleotide : nucleotides) {
      if (longest == null || longest.getLength() < nucleotide.getLength()) {
        longest = nucleotide;
      }
    }
    return longest;
  }

  @Override
  public Nucleotide longest(Long geneId, Organism organism) {
    List<Nucleotide> nucleotides = nucleotideMapper.selectByGeneId(geneId, organism);
    Nucleotide longest = null;
    for (Nucleotide nucleotide : nucleotides) {
      if (longest == null || longest.getLength() < nucleotide.getLength()) {
        longest = nucleotide;
      }
    }
    return longest;
  }

  @Override
  public boolean existsGeneName(String geneName, Organism organism) {
    return nucleotideMapper.existsByGeneName(geneName, organism);
  }

  @Override
  public boolean existsGeneId(Long geneId, Organism organism) {
    return nucleotideMapper.existsByGeneId(geneId, organism);
  }

  @Override
  public List<Nucleotide> inWindow(Module module) {
    long windowSize = Constants.MODULE_GENE_WINDOW;
    long start = module.getStart() - windowSize / 2;
    long end = module.getEnd() + windowSize / 2;
    if (start < 0) {
      end -= start;
      start = 0;
    }
    ChromosomeLocation window = new SimpleChromosomeLocation(module.getChromosome(), start, end);
    return nucleotideMapper.selectInWindow(module.getOrganism(), window);
  }

  @Override
  public List<Nucleotide> inWindow(Nucleotide nucleotide) {
    long windowSize = Constants.MODULE_GENE_WINDOW;
    long start = nucleotide.getStart() - windowSize / 2;
    long end = nucleotide.getEnd() + windowSize / 2;
    if (start < 0) {
      end -= start;
      start = 0;
    }
    ChromosomeLocation window =
        new SimpleChromosomeLocation(nucleotide.getChromosome(), start, end);
    return nucleotideMapper.selectInWindow(nucleotide.getOrganism(), window);
  }

  @Override
  public List<Exon> exons(Nucleotide nucleotide) {
    NucleotideMapper.Exons databaseExons = nucleotideMapper.selectExonsByNucleotide(nucleotide);
    List<Exon> exons = new ArrayList<>();
    for (int i = 0; i < databaseExons.getCount(); i++) {
      Exon exon = new Exon();
      exon.setStart(databaseExons.getExonStarts().get(i));
      exon.setEnd(databaseExons.getExonEnds().get(i));
      exons.add(exon);
    }
    return exons;
  }

  @Override
  public List<Intron> introns(Nucleotide nucleotide) {
    NucleotideMapper.Exons databaseExons = nucleotideMapper.selectExonsByNucleotide(nucleotide);
    List<Intron> introns = new ArrayList<>();
    for (int i = 1; i < databaseExons.getCount(); i++) {
      Intron intron = new Intron();
      intron.setStart(databaseExons.getExonEnds().get(i - 1));
      intron.setEnd(databaseExons.getExonStarts().get(i));
      introns.add(intron);
    }
    return introns;
  }
}
