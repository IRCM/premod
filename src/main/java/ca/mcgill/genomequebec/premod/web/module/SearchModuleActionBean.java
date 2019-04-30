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

package ca.mcgill.genomequebec.premod.web.module;

import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.persistence.Conjunction;
import ca.mcgill.genomequebec.premod.persistence.Limit;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleBestUnitFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleCloseToGeneFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleDatabaseFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleGeneIdFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleGeneNameFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleGroupByBestUnitFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleGroupByUnitFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleLocationFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleMinimalScoreFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleNameFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleOrganismFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleOverlappingCpgFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleUnitFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleVersionFilter;
import ca.mcgill.genomequebec.premod.service.DataBlockingQueue;
import ca.mcgill.genomequebec.premod.service.DataBlockingQueueIterable;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import ca.mcgill.genomequebec.premod.service.ModuleService.ModuleWithBestUnits;
import ca.mcgill.genomequebec.premod.service.ModuleService.Sort;
import ca.mcgill.genomequebec.premod.service.NucleotideService;
import ca.mcgill.genomequebec.premod.service.UnitService;
import ca.mcgill.genomequebec.premod.service.parser.ModuleCsvWriter;
import ca.mcgill.genomequebec.premod.web.WebConstants;
import ca.mcgill.genomequebec.premod.web.ext.BaseActionBean;
import ca.mcgill.genomequebec.premod.web.ext.resolution.ForwardToJspResolution;
import ca.mcgill.genomequebec.premod.web.image.GeneContextImage;
import ca.mcgill.genomequebec.premod.web.image.GeneContextImageFactory;
import ca.qc.ircm.bio.ChromosomeLocation;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.StrictBinding;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.exception.SourcePageNotFoundException;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.OneToManyTypeConverter;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.sourceforge.stripes.validation.ValidationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Search for modules.
 */
@UrlBinding("/search/module")
@StrictBinding
public class SearchModuleActionBean extends BaseActionBean implements ValidationErrorHandler {
  /**
   * Types of search.
   */
  public static enum SearchType {
    /**
     * Do not limit module search.
     */
    ALL, /**
          * By module name(s).
          */
    MODULE, /**
             * By unit id(s).
             */
    UNIT, /**
           * By gene name(s).
           */
    GENE_NAME, /**
                * By gene id(s) on NCBI web site.
                */
    GENE_ID
  }

  /**
   * Output format.
   */
  public static enum Format {
    /**
     * HTML.
     */
    HTML, /**
           * Excel file.
           */
    EXCEL
  }

  /**
   * Overlapping values.
   */
  public static enum Overlapping {
    OVERLAP, NO_OVERLAP
  }

  /**
   * Maximum number of modules to show on a single page.
   */
  public static final int MODULES_PER_PAGE = 500;
  /**
   * Session attribute where search restrictions are stored.
   */
  public static final String SEARCH_ATTRIBUTE = SearchModuleActionBean.class + "#search";
  private static final String MESSAGE_BASE = SearchModuleActionBean.class.getName() + ".";

  private final Logger logger = LoggerFactory.getLogger(SearchModuleActionBean.class);

  /**
   * Type of entered chromosomes.
   */
  public enum ChromosomeType {
    /**
     * Entered chromosomes are in UCSC format.
     */
    UCSC, /**
           * Entered chromosomes are numbers or letters (X and Y).
           */
    NUMBER
  }

  /**
   * Organism on which to search.
   */
  @Validate(required = true, on = "search")
  public Organism organism;
  /**
   * Version of modules on which to search.
   */
  @Validate(required = true, on = "search")
  public Version version;
  /**
   * Type of search.
   */
  @Validate(required = true, on = "search")
  public SearchType searchType;
  /**
   * Name(s) of modules if search by module name(s).
   */
  @Validate(converter = OneToManyTypeConverter.class)
  public List<String> moduleNames;
  /**
   * Id(s) of units if search by unit id(s).
   */
  @Validate(converter = OneToManyTypeConverter.class)
  public List<String> unitIds;
  /**
   * Conjunction to use if multiple unit ids.
   */
  @Validate
  public Conjunction unitConjunction;
  /**
   * Are unit id(s) tags of module or not.
   */
  @Validate
  public Unit.TagType unitTag;
  /**
   * Name(s) of genes if search by genes name(s).
   */
  @Validate(converter = OneToManyTypeConverter.class)
  public List<String> geneNames;
  /**
   * Id(s) of genes if search by genes id(s).
   */
  @Validate(converter = OneToManyTypeConverter.class)
  public List<Long> geneIds;
  /**
   * Chromosome(s) of modules.
   */
  @Validate(converter = OneToManyTypeConverter.class)
  public List<ChromosomeLocation> chromosomes;
  /**
   * Minimal score of modules.
   */
  @Validate
  public Integer score;
  /**
   * Modules that are within geneUp bases upstream of gene.
   */
  @Validate
  public Long geneUp;
  /**
   * Modules that are within geneDown bases downstream of gene.
   */
  @Validate
  public Long geneDown;
  /**
   * Overlapping CpG island.
   */
  @Validate
  public Overlapping overlapping;
  /**
   * Sort order of modules.
   */
  @Validate(required = true, on = "search")
  public ModuleService.Sort sort;
  /**
   * True if modules are to be reverted after sort.
   */
  @Validate
  public boolean descending;
  /**
   * Output format.
   */
  @Validate(required = true, on = "search")
  public Format format;
  /**
   * Page of modules to show when search finds to many modules.
   */
  @Validate(minvalue = 1, required = true, on = "search")
  public Integer page;
  /**
   * Gene for which to get context image.
   */
  @Validate
  public String geneName;
  /**
   * Gene for which to get context image.
   */
  @Validate
  public Long geneId;
  /**
   * Number of modules that pass filters.
   */
  private Integer modulesCount;
  /**
   * Modules found by search.
   */
  private DataBlockingQueue<ModuleWithBestUnits> modules;
  /**
   * Images for gene contexts.
   */
  private Map<Object, GeneContextImage> geneContextImages;
  @Inject
  private ModuleService moduleService;
  @Inject
  private UnitService unitService;
  @Inject
  private NucleotideService nucleotideService;
  @Inject
  private GeneContextImageFactory geneContextImageFactory;

  /**
   * Populate default values.
   */
  @Before(stages = LifecycleStage.BindingAndValidation)
  public void populateDefault() {
    logger.trace("Populate default values");
    organism = Organism.HUMAN;
    version = Version.LATEST;
    searchType = SearchType.ALL;
    unitConjunction = Conjunction.AND;
    unitTag = null;
    sort = ModuleService.Sort.SCORE;
    descending = false;
    format = Format.HTML;
    page = 1;
  }

  /**
   * Mouse version is always latest.
   */
  @Before(stages = LifecycleStage.CustomValidation)
  public void replaceMouseVersion() {
    if (organism == Organism.MOUSE) {
      logger.trace("Set mouse version to latest");
      version = Version.LATEST;
    }
  }

  /**
   * Generate images for selected genes.
   */
  @Before(stages = LifecycleStage.ResolutionExecution)
  public void populateGeneContextImages() {
    geneContextImages = new HashMap<Object, GeneContextImage>();
    if (searchType == SearchType.GENE_NAME && geneNames != null) {
      for (String geneName : geneNames) {
        Nucleotide gene = nucleotideService.longest(geneName, organism);
        if (gene != null && version != null) {
          GeneContextImage image = geneContextImageFactory.create(gene, version, context);
          geneContextImages.put(geneName, image);
        }
      }
    }
    if (searchType == SearchType.GENE_ID && geneIds != null) {
      for (Long geneId : geneIds) {
        Nucleotide gene = nucleotideService.longest(geneId, organism);
        GeneContextImage image = geneContextImageFactory.create(gene, version, context);
        geneContextImages.put(geneId, image);
      }
    }
  }

  /**
   * Go to search page.
   *
   * @return search page
   */
  @DefaultHandler
  public Resolution input() {
    logger.debug("Go to search page");

    return new ForwardToJspResolution(this.getClass(), "Search");
  }

  /**
   * Validate that search restrictions are correct.
   */
  @ValidationMethod(on = "search", when = ValidationState.ALWAYS)
  public void validateSearch() {
    logger.trace("Validate that search restrictions are correct");

    // Check required fields linked to search type.
    if (searchType != null) {
      switch (searchType) {
        case ALL:
          break;
        case MODULE: {
          if (moduleNames == null || moduleNames.isEmpty()) {
            logger.debug("moduleNames is required");
            LocalizableError error = new LocalizableError("validation.required.valueNotPresent");
            context.getValidationErrors().add("moduleNames", error);
          }
          break;
        }
        case UNIT: {
          if (unitIds == null || unitIds.isEmpty()) {
            logger.debug("unitIds is required");
            LocalizableError error = new LocalizableError("validation.required.valueNotPresent");
            context.getValidationErrors().add("unitIds", error);
          }
          if (unitConjunction == null) {
            logger.debug("unitConjunction is required");
            LocalizableError error = new LocalizableError("validation.required.valueNotPresent");
            context.getValidationErrors().add("unitConjunction", error);
          }
          break;
        }
        case GENE_NAME: {
          if (geneNames == null || geneNames.isEmpty()) {
            logger.debug("geneNames is required");
            LocalizableError error = new LocalizableError("validation.required.valueNotPresent");
            context.getValidationErrors().add("geneNames", error);
          }
          break;
        }
        case GENE_ID: {
          if (geneIds == null || geneIds.isEmpty()) {
            logger.debug("geneIds is required");
            LocalizableError error = new LocalizableError("validation.required.valueNotPresent");
            context.getValidationErrors().add("geneIds", error);
          }
          break;
        }
        default:
      }
    }

    // Validate module names.
    if (organism != null && SearchType.MODULE == searchType && moduleNames != null) {
      for (String moduleName : moduleNames) {
        if (!moduleService.exists(moduleName, organism)) {
          logger.debug("module {} does not exists", moduleName);
          LocalizableError error = new LocalizableError("validation.exists.notExists", moduleName);
          context.getValidationErrors().add("moduleNames", error);
        }
      }
    }

    // Validate unit identifiers.
    if (SearchType.UNIT == searchType && unitIds != null) {
      for (String unitId : unitIds) {
        if (!unitService.exists(unitId)) {
          logger.debug("unit {} does not exists", unitId);
          LocalizableError error = new LocalizableError("validation.exists.notExists", unitId);
          context.getValidationErrors().add("unitIds", error);
        }
      }
    }

    // Validate gene names.
    if (organism != null && SearchType.GENE_NAME == searchType && geneNames != null) {
      for (String geneName : geneNames) {
        if (!nucleotideService.existsGeneName(geneName, organism)) {
          logger.debug("gene {} does not exists", geneName);
          LocalizableError error = new LocalizableError("validation.exists.notExists", geneName);
          context.getValidationErrors().add("geneNames", error);
        }
      }
    }

    // Validate gene identifiers.
    if (organism != null && SearchType.GENE_ID == searchType && geneIds != null) {
      for (Long geneId : geneIds) {
        if (!nucleotideService.existsGeneId(geneId, organism)) {
          logger.debug("gene {} does not exists", geneId);
          LocalizableError error = new LocalizableError("validation.exists.notExists", geneId);
          context.getValidationErrors().add("geneIds", error);
        }
      }
    }
  }

  /**
   * Search modules.
   *
   * @return modules page
   */
  public Resolution search() {
    logger.debug("Search modules");

    // Search modules.
    final Collection<ModuleDatabaseFilter> filters = new LinkedList<ModuleDatabaseFilter>();
    filters.add(new ModuleOrganismFilter(organism));
    filters.add(new ModuleVersionFilter(version));
    switch (searchType) {
      case ALL:
        break;
      case MODULE:
        filters.add(new ModuleNameFilter(moduleNames));
        break;
      case UNIT:
        switch (unitConjunction) {
          case AND:
            if (unitTag != null) {
              switch (unitTag) {
                case TAG:
                  filters.add(new ModuleGroupByBestUnitFilter(unitIds));
                  break;
                case NO_TAG:
                  break;
                default:
              }
            } else {
              filters.add(new ModuleGroupByUnitFilter(unitIds));
            }
            break;
          case OR:
            if (unitTag != null) {
              switch (unitTag) {
                case TAG:
                  filters.add(new ModuleBestUnitFilter(unitIds));
                  break;
                case NO_TAG:
                  break;
                default:
              }
            } else {
              filters.add(new ModuleUnitFilter(unitIds));
            }
            break;
          default:
        }
        break;
      case GENE_NAME:
        filters.add(new ModuleGeneNameFilter(geneNames));
        break;
      case GENE_ID:
        filters.add(new ModuleGeneIdFilter(geneIds));
        break;
      default:
    }
    if (chromosomes != null && !chromosomes.isEmpty()) {
      filters.add(new ModuleLocationFilter(chromosomes));
    }
    if (score != null) {
      filters.add(new ModuleMinimalScoreFilter(score));
    }
    if (geneUp != null || geneDown != null) {
      filters.add(new ModuleCloseToGeneFilter(geneUp, geneDown));
    }
    if (overlapping != null) {
      filters.add(new ModuleOverlappingCpgFilter(overlapping == Overlapping.OVERLAP));
    }
    final ModuleService.SearchParameters parameters = new ModuleService.SearchParameters() {
      @Override
      public Collection<ModuleDatabaseFilter> filters() {
        return filters;
      }

      @Override
      public Sort sort() {
        return sort;
      }

      @Override
      public boolean descending() {
        return descending;
      }

      @Override
      public Limit limit() {
        switch (format) {
          case HTML:
            return new Limit.Default((page - 1) * MODULES_PER_PAGE, MODULES_PER_PAGE);
          case EXCEL:
            return null;
          default:
            throw new AssertionError("format " + format + " not covered in switch");
        }
      }
    };
    class Handler implements ModuleService.ModuleForSearchHandler, Runnable {
      @Override
      public void handleCount(int count) {
        modulesCount = count;
      }

      @Override
      public void handleResult(ModuleWithBestUnits module) {
        try {
          if (!modules.offer(module, 1, TimeUnit.MINUTES)) {
            // Probably that the reader thread is dead, return.
            throw new IllegalStateException(
                "Waited too long for reader to take a value, thread stopped");
          }
        } catch (InterruptedException e) {
          return;
        }
      }

      @Override
      public void run() {
        try {
          moduleService.searchLowMemory(parameters, this);
        } catch (Throwable e) {
          logger.error("Could not obtain modules", e);
          LocalizableError error = new LocalizableError("error");
          context.getValidationErrors().addGlobalError(error);
        } finally {
          modules.setTerminated(true);
        }
      }
    }

    modules = new DataBlockingQueue<ModuleWithBestUnits>();
    new Thread(new Handler()).start();
    modules.waitForResults();

    // Go to web page or send Excel file.
    switch (format) {
      case HTML:
        return new ForwardToJspResolution(this.getClass());
      case EXCEL: {
        // Send excel file.
        String filename = (new LocalizableMessage(MESSAGE_BASE + "excel.filename"))
            .getMessage(context.getLocale());
        StreamingResolution resolution = new StreamingResolution("application/csv") {
          @Override
          protected void stream(HttpServletResponse response) throws Exception {
            Iterable<ModuleWithBestUnits> iterableModules =
                new DataBlockingQueueIterable<ModuleWithBestUnits>(modules);
            ModuleCsvWriter writer = new ModuleCsvWriter(
                new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8")),
                context.getLocale());
            try {
              writer.writeHeader();
              for (ModuleWithBestUnits module : iterableModules) {
                writer.writeModule(module);
              }
            } finally {
              writer.close();
            }
          }
        };
        resolution.setCharacterEncoding("UTF-8");
        resolution.setFilename(filename);
        return resolution;
      }
      default:
        throw new AssertionError("format " + format + " not covered in switch");
    }
  }

  /**
   * Send gene image to user.
   *
   * @return gene's image
   */
  public Resolution geneNameImage() {
    logger.debug("Show gene image for gene {}", geneName);

    // Generate gene's image.
    Nucleotide gene = nucleotideService.longest(geneName, organism);
    if (gene != null && version != null) {
      final GeneContextImage image = geneContextImageFactory.create(gene, version, context);
      return new StreamingResolution("image/" + WebConstants.IMAGE_TYPE) {
        @Override
        protected void stream(HttpServletResponse response) throws Exception {
          OutputStream output = response.getOutputStream();
          try {
            image.printPlotToOutputStream(output, WebConstants.IMAGE_TYPE);
          } finally {
            output.close();
          }
        }
      };
    } else {
      return new StreamingResolution("image/" + WebConstants.IMAGE_TYPE, new StringReader(""));
    }
  }

  /**
   * Send gene image to user.
   *
   * @return gene's image
   */
  public Resolution geneIdImage() {
    logger.debug("Show gene image for gene {}", geneId);

    // Generate gene's image.
    Nucleotide gene = nucleotideService.longest(geneId, organism);
    if (gene != null && version != null) {
      final GeneContextImage image = geneContextImageFactory.create(gene, version, context);
      return new StreamingResolution("image/" + WebConstants.IMAGE_TYPE) {
        @Override
        protected void stream(HttpServletResponse response) throws Exception {
          OutputStream output = response.getOutputStream();
          try {
            image.printPlotToOutputStream(output, WebConstants.IMAGE_TYPE);
          } finally {
            output.close();
          }
        }
      };
    } else {
      return new StreamingResolution("image/" + WebConstants.IMAGE_TYPE, new StringReader(""));
    }
  }

  @Override
  public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
    try {
      return context.getSourcePageResolution();
    } catch (SourcePageNotFoundException e) {
      return new ForwardToJspResolution(this.getClass(), "Search");
    }
  }

  public Organism getOrganism() {
    return organism;
  }

  public void setOrganism(Organism organism) {
    this.organism = organism;
  }

  public Iterator<ModuleWithBestUnits> getModulesIterator() {
    return modules.iterator();
  }

  public Integer getModulesCount() {
    return modulesCount;
  }

  public int getModulesPerPage() {
    return MODULES_PER_PAGE;
  }

  public Version getVersion() {
    return version;
  }

  public void setVersion(Version version) {
    this.version = version;
  }

  public SearchType getSearchType() {
    return searchType;
  }

  public void setSearchType(SearchType searchType) {
    this.searchType = searchType;
  }

  public List<String> getModuleNames() {
    return moduleNames;
  }

  public void setModuleNames(List<String> moduleNames) {
    this.moduleNames = moduleNames;
  }

  public List<String> getUnitIds() {
    return unitIds;
  }

  public void setUnitIds(List<String> unitIds) {
    this.unitIds = unitIds;
  }

  public Conjunction getUnitConjunction() {
    return unitConjunction;
  }

  public void setUnitConjunction(Conjunction unitConjunction) {
    this.unitConjunction = unitConjunction;
  }

  public Unit.TagType getUnitTag() {
    return unitTag;
  }

  public void setUnitTag(Unit.TagType unitTag) {
    this.unitTag = unitTag;
  }

  public List<String> getGeneNames() {
    return geneNames;
  }

  public void setGeneNames(List<String> geneNames) {
    this.geneNames = geneNames;
  }

  public List<Long> getGeneIds() {
    return geneIds;
  }

  public void setGeneIds(List<Long> geneIds) {
    this.geneIds = geneIds;
  }

  public List<ChromosomeLocation> getChromosomes() {
    return chromosomes;
  }

  public void setChromosomes(List<ChromosomeLocation> chromosomes) {
    this.chromosomes = chromosomes;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public Long getGeneUp() {
    return geneUp;
  }

  public void setGeneUp(Long geneUp) {
    this.geneUp = geneUp;
  }

  public Long getGeneDown() {
    return geneDown;
  }

  public void setGeneDown(Long geneDown) {
    this.geneDown = geneDown;
  }

  public Overlapping getOverlapping() {
    return overlapping;
  }

  public void setOverlapping(Overlapping overlapping) {
    this.overlapping = overlapping;
  }

  public ModuleService.Sort getSort() {
    return sort;
  }

  public void setSort(ModuleService.Sort sort) {
    this.sort = sort;
  }

  public boolean isDescending() {
    return descending;
  }

  public void setDescending(boolean descending) {
    this.descending = descending;
  }

  public Format getFormat() {
    return format;
  }

  public void setFormat(Format format) {
    this.format = format;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Map<Object, GeneContextImage> getGeneContextImages() {
    return geneContextImages;
  }

  public final DataBlockingQueue<ModuleWithBestUnits> getModules() {
    return modules;
  }
}
