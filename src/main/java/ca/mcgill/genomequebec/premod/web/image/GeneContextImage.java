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

package ca.mcgill.genomequebec.premod.web.image;

import ca.mcgill.genomequebec.premod.Constants;
import ca.mcgill.genomequebec.premod.MainConfiguration;
import ca.mcgill.genomequebec.premod.business.Exon;
import ca.mcgill.genomequebec.premod.business.Intron;
import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import ca.mcgill.genomequebec.premod.service.NucleotideService;
import ca.mcgill.genomequebec.premod.web.WebContext;
import ca.qc.ircm.bio.ChromosomeLocation;
import ca.qc.ircm.bio.SimpleChromosomeLocation;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Generates an image of genomic context around a module.
 */
public class GeneContextImage extends ContextImage {
  /**
   * Width of image.
   */
  private static final int IMAGE_WIDTH = 800;

  /**
   * Resource bundle used to get text.
   */
  private final ResourceBundle bundle;
  /**
   * Version of modules.
   */
  private final Version version;
  /**
   * Nucleotide for which to create context image.
   */
  private final Nucleotide nucleotide;
  /**
   * Modules located near our module.
   */
  private final List<Module> modulesInWindow;
  /**
   * Nucleotides near module.
   */
  private final List<Nucleotide> nucleotidesInWindow;
  /**
   * Nucleotides' exons.
   */
  private final Map<Nucleotide, List<Exon>> nucleotideExons;
  /**
   * Nucleotides' introns.
   */
  private final Map<Nucleotide, List<Intron>> nucleotideIntrons;
  /**
   * Location of the window.
   */
  private final ChromosomeLocation window;

  /**
   * Creates context image for Gene.
   *
   * @param moduleService
   *          module service
   * @param nucleotideService
   *          nucleotide service
   * @param mainConfiguration
   *          main configuration
   * @param nucleotide
   *          nucleotide
   * @param version
   *          version
   * @param webContext
   *          web context
   */
  public GeneContextImage(ModuleService moduleService, NucleotideService nucleotideService,
      MainConfiguration mainConfiguration, Nucleotide nucleotide, Version version,
      WebContext webContext) {
    super(webContext, mainConfiguration);
    this.bundle =
        ResourceBundle.getBundle(GeneContextImage.class.getName(), webContext.getLocale());
    this.nucleotide = nucleotide;
    this.version = version;
    modulesInWindow = moduleService.inWindow(nucleotide, version);
    nucleotidesInWindow = nucleotideService.inWindow(nucleotide);
    nucleotideExons = new HashMap<>();
    for (Nucleotide nucleotideInWindow : nucleotidesInWindow) {
      nucleotideExons.put(nucleotideInWindow, nucleotideService.exons(nucleotideInWindow));
    }
    nucleotideIntrons = new HashMap<>();
    for (Nucleotide nucleotideInWindow : nucleotidesInWindow) {
      nucleotideIntrons.put(nucleotideInWindow, nucleotideService.introns(nucleotideInWindow));
    }
    // Computes window location.
    long start = nucleotide.getStart() - Constants.MODULE_GENE_WINDOW / 2;
    start = Math.max(start, 0);
    long end = nucleotide.getEnd() + Constants.MODULE_GENE_WINDOW / 2;
    window = new SimpleChromosomeLocation(nucleotide.getChromosome(), start, end);
  }

  @Override
  public int getWidth() {
    return IMAGE_WIDTH;
  }

  @Override
  protected Organism organism() {
    return nucleotide.getOrganism();
  }

  @Override
  protected Version version() {
    return version;
  }

  @Override
  protected ChromosomeLocation window() {
    return window;
  }

  @Override
  protected Collection<Module> modulesInWindow() {
    return modulesInWindow;
  }

  @Override
  protected Collection<Nucleotide> nucleotidesInWindow() {
    return nucleotidesInWindow;
  }

  @Override
  protected Map<Nucleotide, List<Exon>> nucleotideExons() {
    return nucleotideExons;
  }

  @Override
  protected Map<Nucleotide, List<Intron>> nucleotideIntrons() {
    return nucleotideIntrons;
  }

  @Override
  protected String title() {
    return MessageFormat.format(bundle.getString("title.module"), nucleotide.getGene(),
        window.getChromosome(), window.getStart(), window.getEnd());
  }
}
