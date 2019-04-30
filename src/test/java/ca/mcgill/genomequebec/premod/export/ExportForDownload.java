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

package ca.mcgill.genomequebec.premod.export;

import au.com.bytecode.opencsv.CSVWriter;
import ca.mcgill.genomequebec.premod.Main;
import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.UnitOccurrence;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.persistence.Limit;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleDatabaseFilter;
import ca.mcgill.genomequebec.premod.persistence.mapper.ModuleMapper;
import ca.mcgill.genomequebec.premod.persistence.mapper.ModuleMapper.ModuleWithSequence;
import ca.mcgill.genomequebec.premod.persistence.mapper.UnitMapper;
import ca.mcgill.genomequebec.premod.persistence.mapper.UnitMapper.UnitForExport;
import ca.mcgill.genomequebec.premod.persistence.mapper.UnitOccurrenceMapper;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import ca.mcgill.genomequebec.premod.service.ModuleService.ModuleForSearchHandler;
import ca.mcgill.genomequebec.premod.service.ModuleService.ModuleWithBestUnits;
import ca.mcgill.genomequebec.premod.service.ModuleService.SearchParameters;
import ca.mcgill.genomequebec.premod.service.ModuleService.Sort;
import ca.mcgill.genomequebec.premod.service.UnitService.Extra;
import ca.mcgill.genomequebec.premod.utils.ExceptionUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.inject.Inject;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Export data in old format.
 */
public class ExportForDownload {
  private static class CreateWriterListener {
    public void writerCreated(Writer writer) throws IOException {
    }

    public void writerCreated(CSVWriter writer) throws IOException {
    }
  }

  private class Writers {
    private Map<Organism, Map<Version, Writer>> regulars = new HashMap<>();
    private Map<Organism, Map<Version, CSVWriter>> csvs = new HashMap<>();
    private Map<Organism, Map<Version, Map<String, Writer>>> chromosomes = new HashMap<>();
    private Collection<Writer> writers = new ArrayList<>();
    private Collection<CSVWriter> csvWriters = new ArrayList<>();
    private String filename;
    private String chromosomeFilename;
    private CreateWriterListener listener;

    private Writers(String filename) {
      this.filename = filename;
    }

    private Writers(String filename, String chromosomeFilename) {
      this.filename = filename;
      this.chromosomeFilename = chromosomeFilename;
    }

    private Writer get(Organism organism, Version version) throws IOException {
      if (!regulars.containsKey(organism)) {
        regulars.put(organism, new HashMap<Version, Writer>());
      }
      Map<Version, Writer> versionMap = regulars.get(organism);
      if (!versionMap.containsKey(version)) {
        Writer writer = createWriter(organism, version, filename);
        versionMap.put(version, writer);
      }
      return versionMap.get(version);
    }

    private Writer get(Organism organism, Version version, String chromosome) throws IOException {
      if (!chromosomes.containsKey(organism)) {
        chromosomes.put(organism, new HashMap<Version, Map<String, Writer>>());
      }
      Map<Version, Map<String, Writer>> versionMap = chromosomes.get(organism);
      if (!versionMap.containsKey(version)) {
        versionMap.put(version, new HashMap<String, Writer>());
      }
      Map<String, Writer> chromosomeMap = versionMap.get(version);
      if (!chromosomeMap.containsKey(chromosome)) {
        String filename = chromosomeFilename.replaceAll("\\{0\\}", chromosome);
        Writer writer = createWriter(organism, version, filename);
        chromosomeMap.put(chromosome, writer);
      }
      return chromosomeMap.get(chromosome);
    }

    private Writer createWriter(Organism organism, Version version, String filename)
        throws IOException {
      File file = getFile(organism, version, filename);
      logger.trace("Create writer for file {}", file);
      Writer writer = new BufferedWriter(
          new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file)), "UTF-8"));
      writers.add(writer);
      if (listener != null) {
        listener.writerCreated(writer);
      }
      return writer;
    }

    private CSVWriter getCsv(Organism organism, Version version) throws IOException {
      if (!csvs.containsKey(organism)) {
        csvs.put(organism, new HashMap<Version, CSVWriter>());
      }
      Map<Version, CSVWriter> versionMap = csvs.get(organism);
      if (!versionMap.containsKey(version)) {
        CSVWriter writer = createCsvWriter(organism, version, filename);
        versionMap.put(version, writer);
      }
      return versionMap.get(version);
    }

    private CSVWriter createCsvWriter(Organism organism, Version version, String filename)
        throws IOException {
      File file = getFile(organism, version, filename);
      logger.trace("Create CSV writer for file {} for organism {} version {}", file, organism,
          version);
      CSVWriter writer = new CSVWriter(new BufferedWriter(
          new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file)), "UTF-8")));
      csvWriters.add(writer);
      if (listener != null) {
        listener.writerCreated(writer);
      }
      return writer;
    }

    private void close() {
      for (Writer writer : writers) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      for (CSVWriter writer : csvWriters) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private final Logger logger = LoggerFactory.getLogger(ExportForDownload.class);
  @Inject
  private ModuleMapper moduleMapper;
  @Inject
  private UnitMapper unitMapper;
  @Inject
  private UnitOccurrenceMapper unitOccurrenceMapper;
  @Inject
  private ModuleService moduleService;
  private Map<Organism, Map<Version, File>> folders = new HashMap<>();
  private boolean appendOrganismToFilename;

  private void write(Collection<Writer> writers, String content) throws IOException {
    for (Writer writer : writers) {
      writer.write(content);
    }
  }

  /**
   * Export modules.
   *
   * @throws IOException
   *           could not write modules
   */
  public void exportModules() throws IOException {
    logger.debug("exportModules");
    final Writers writers = new Writers("module_database.txt.gz");

    moduleMapper.selectAll(new ResultHandler<Module>() {
      @Override
      public void handleResult(ResultContext<? extends Module> context) {
        Module module = context.getResultObject();
        Organism organism = module.getOrganism();
        Version version = module.getVersion();
        try {
          Writer writer = writers.get(organism, version);
          writer.write(String.valueOf(module.getName()));
          writer.write("\t");
          writer.write(String.valueOf(module.getType()));
          writer.write("\t");
          writer.write("chr");
          writer.write(String.valueOf(module.getChromosome()));
          writer.write("\t");
          writer.write(String.valueOf(module.getStart()));
          writer.write("\t");
          writer.write(String.valueOf(module.getEnd()));
          writer.write("\t");
          writer.write(String.valueOf(module.getLength()));
          writer.write("\t");
          writer.write(String.valueOf(module.getAssembly()));
          writer.write("\t");
          writer.write(module.getScore() != null ? String.valueOf(module.getScore()) : "\\N");
          writer.write("\t");
          writer.write(module.getDownstreamGenePosition() != null
              ? String.valueOf(module.getDownstreamGenePosition()) : "\\N");
          writer.write("\t");
          writer.write(module.getDownstreamGeneLocus() != null
              ? String.valueOf(module.getDownstreamGeneLocus()) : "\\N");
          writer.write("\t");
          writer.write(module.getDownstreamGeneName() != null
              ? String.valueOf(module.getDownstreamGeneName()) : "\\N");
          writer.write("\t");
          writer.write(module.getUpstreamGenePosition() != null
              ? String.valueOf(module.getUpstreamGenePosition()) : "\\N");
          writer.write("\t");
          writer.write(module.getUpstreamGeneLocus() != null
              ? String.valueOf(module.getUpstreamGeneLocus()) : "\\N");
          writer.write("\t");
          writer.write(module.getUpstreamGeneName() != null
              ? String.valueOf(module.getUpstreamGeneName()) : "\\N");
          writer.write("\t");
          writer.write(module.isCpgIsland() ? "1" : "0");
          writer.write("\n");
        } catch (IOException e) {
          ExceptionUtils.optionallyPackageRuntimeException(e, "Could not write to writer");
        }
      }
    });

    writers.close();
  }

  /**
   * Export modules' sequence.
   *
   * @throws IOException
   *           could not write modules' sequence
   */
  public void exportModuleSequences() throws IOException {
    logger.debug("exportModuleSequences");
    final Writers writers = new Writers("module_sequence_database.txt.gz");

    moduleMapper.selectAllWithSequence(new ResultHandler<ModuleWithSequence>() {
      @Override
      public void handleResult(ResultContext<? extends ModuleWithSequence> context) {
        ModuleWithSequence module = context.getResultObject();
        Organism organism = module.getOrganism();
        Version version = module.getVersion();
        try {
          Writer writer = writers.get(organism, version);
          writer.write(String.valueOf(module.getName()));
          writer.write("\t");
          writer.write(String.valueOf(module.getSequence()));
          writer.write("\n");
        } catch (IOException e) {
          ExceptionUtils.optionallyPackageRuntimeException(e, "Could not write to writer");
        }
      }
    });

    writers.close();
  }

  /**
   * Export units.
   *
   * @throws IOException
   *           could not write units
   */
  public void exportUnits() throws IOException {
    logger.debug("exportUnits");
    final Writers writers = new Writers("matrix_database.txt.gz");

    unitMapper.selectAllForStatistics(new ResultHandler<UnitForExport>() {
      @Override
      public void handleResult(ResultContext<? extends UnitForExport> context) {
        UnitForExport unitForExport = context.getResultObject();
        Organism organism = unitForExport.getOrganism();
        Version version = unitForExport.getVersion();
        Unit unit = unitForExport.getUnit();
        Extra extra = unitForExport.getExtra();
        try {
          Writer writer = writers.get(organism, version);
          writer.write(String.valueOf(unit.getId()));
          writer.write("\t");
          writer.write(String.valueOf(unit.getName()));
          writer.write("\t");
          writer
              .write(unit.getDescription() != null ? String.valueOf(unit.getDescription()) : "\\N");
          writer.write("\t");
          writer.write(extra.getTag() != null ? String.valueOf(extra.getTag()) : "\\N");
          writer.write("\t");
          writer.write(extra.getNoTag() != null ? String.valueOf(extra.getNoTag()) : "\\N");
          writer.write("\t");
          writer.write(extra.getAny() != null ? String.valueOf(extra.getAny()) : "\\N");
          writer.write("\n");
        } catch (IOException e) {
          ExceptionUtils.optionallyPackageRuntimeException(e, "Could not write to writer");
        }
      }
    });

    writers.close();
  }

  /**
   * Export unit occurrences.
   *
   * @throws IOException
   *           could not write unit occurrences
   */
  public void exportUnitOccurrences() throws IOException {
    logger.debug("exportUnitOccurrences");
    final Writers writers = new Writers("matrix_occurence_database.txt.gz");

    unitOccurrenceMapper.selectAll(new ResultHandler<UnitOccurrence>() {
      @Override
      public void handleResult(ResultContext<? extends UnitOccurrence> context) {
        UnitOccurrence unitOccurrence = context.getResultObject();
        Organism organism = unitOccurrence.getOrganism();
        Version version = unitOccurrence.getVersion();
        try {
          Writer writer = writers.get(organism, version);
          writer.write(String.valueOf(unitOccurrence.getUnit().getId()));
          writer.write("\t");
          writer.write("chr");
          writer.write(String.valueOf(unitOccurrence.getChromosome()));
          writer.write("\t");
          writer.write(String.valueOf(unitOccurrence.getStart()));
          writer.write("\t");
          writer.write(String.valueOf(unitOccurrence.getEnd()));
          writer.write("\t");
          writer.write(String.valueOf(unitOccurrence.getStrand()));
          writer.write("\t");
          writer.write(String.valueOf(unitOccurrence.getAssembly()));
          writer.write("\t");
          writer.write(unitOccurrence.getOccScore() != null
              ? String.valueOf(unitOccurrence.getOccScore()) : "\\N");
          writer.write("\t");
          writer.write(unitOccurrence.getTotalScore() != null
              ? String.valueOf(unitOccurrence.getTotalScore()) : "\\N");
          writer.write("\t");
          writer.write(String.valueOf(unitOccurrence.getModule().getName()));
          writer.write("\t");
          writer.write(unitOccurrence.getTagNumber() != null
              ? String.valueOf(Integer.parseInt(unitOccurrence.getTagNumber()) - 1) : "\\N");
          writer.write("\n");
        } catch (IOException e) {
          ExceptionUtils.optionallyPackageRuntimeException(e, "Could not write to writer");
        }
      }
    });

    writers.close();
  }

  private String sequenceToFastaLikeFormat(String sequence) {
    StringBuffer result = new StringBuffer();

    if (sequence != null) {
      // Add sequence with 80 chars by line.
      String tmp = sequence;
      while (tmp.length() > 80) {
        result.append(tmp.substring(0, 80));
        result.append("\n");
        tmp = tmp.substring(80);
      }
      result.append(tmp);
    }

    return result.toString();
  }

  /**
   * Export modules in CSV format.
   *
   * @throws IOException
   *           could not write modules
   */
  public void exportCsv() throws IOException {
    logger.debug("exportCsv");
    final Writers writers = new Writers("module_comma.txt.gz");
    writers.listener = new CreateWriterListener() {
      @Override
      public void writerCreated(CSVWriter writer) throws IOException {
        writer.writeNext(
            new String[] { "Name", "Chromosome", "Length", "Score", "Upstream Entrez Gene Id",
                "Upstream Gene Name", "Upstream Gene Position", "Downstream Entrez Gene Id",
                "Downstream Gene Name", "Downstream Gene Position", "Tag Matrices" });
      }
    };

    moduleService.searchLowMemory(new SearchParameters() {
      @Override
      public Collection<ModuleDatabaseFilter> filters() {
        return null;
      }

      @Override
      public Sort sort() {
        return null;
      }

      @Override
      public boolean descending() {
        return false;
      }

      @Override
      public Limit limit() {
        return null;
      }
    }, new ModuleForSearchHandler() {
      @Override
      public void handleCount(int count) {
      }

      @Override
      public void handleResult(ModuleWithBestUnits moduleWithBestUnits) {
        Module module = moduleWithBestUnits.getModule();
        Organism organism = module.getOrganism();
        Version version = module.getVersion();
        try {
          final CSVWriter writer = writers.getCsv(organism, version);
          String[] columns = new String[10 + moduleWithBestUnits.getBestUnits().size()];
          columns[0] = module.getName();
          columns[1] = "chr" + module.getChromosome();
          columns[2] = String.valueOf(module.getLength());
          columns[3] = module.getScore() != null ? String.valueOf(module.getScore()) : "\\N";
          columns[4] = module.getUpstreamGeneLocus() != null
              ? String.valueOf(module.getUpstreamGeneLocus()) : "\\N";
          columns[5] = module.getUpstreamGeneName() != null
              ? String.valueOf(module.getUpstreamGeneName()) : "\\N";
          columns[6] = module.getUpstreamGenePosition() != null
              ? String.valueOf(module.getUpstreamGenePosition()) : "\\N";
          columns[7] = module.getDownstreamGeneLocus() != null
              ? String.valueOf(module.getDownstreamGeneLocus()) : "\\N";
          columns[8] = module.getDownstreamGeneName() != null
              ? String.valueOf(module.getDownstreamGeneName()) : "\\N";
          columns[9] = module.getDownstreamGenePosition() != null
              ? String.valueOf(module.getDownstreamGenePosition()) : "\\N";
          int index = 10;
          for (Unit unit : moduleWithBestUnits.getBestUnits()) {
            columns[index++] = unit.getId() + " (" + unit.getName() + ")";
          }
          writer.writeNext(columns);
        } catch (IOException e) {
          ExceptionUtils.optionallyPackageRuntimeException(e, "Could not write to writer");
        }
      }
    });

    writers.close();
  }

  /**
   * Export modules in tabs format.
   *
   * @throws IOException
   *           could not write modules
   */
  public void exportTabs() throws IOException {
    logger.debug("exportTabs");
    final Writers writers = new Writers("module_tab.txt.gz");
    writers.listener = new CreateWriterListener() {
      @Override
      public void writerCreated(Writer writer) throws IOException {
        writer.write(
            "Name\tChromosome\tLength\tScore\tUpstream Entrez Gene Id\tUpstream Gene Name\tUpstream Gene Position\tDownstream Entrez Gene Id\tDownstream Gene Name\tDownstream Gene Position\tTag Matrices");
        writer.write("\n");
      }
    };

    moduleService.searchLowMemory(new SearchParameters() {
      @Override
      public Collection<ModuleDatabaseFilter> filters() {
        return null;
      }

      @Override
      public Sort sort() {
        return null;
      }

      @Override
      public boolean descending() {
        return false;
      }

      @Override
      public Limit limit() {
        return null;
      }
    }, new ModuleForSearchHandler() {
      @Override
      public void handleCount(int count) {
      }

      @Override
      public void handleResult(ModuleWithBestUnits moduleWithBestUnits) {
        Module module = moduleWithBestUnits.getModule();
        Organism organism = module.getOrganism();
        Version version = module.getVersion();
        try {
          Writer writer = writers.get(organism, version);
          writer.write(module.getName());
          writer.write("\t");
          writer.write("chr");
          writer.write(module.getChromosome());
          writer.write("\t");
          writer.write(String.valueOf(module.getLength()));
          writer.write("\t");
          writer.write(module.getScore() != null ? String.valueOf(module.getScore()) : "\\N");
          writer.write("\t");
          writer.write(module.getUpstreamGeneLocus() != null
              ? String.valueOf(module.getUpstreamGeneLocus()) : "\\N");
          writer.write("\t");
          writer.write(module.getUpstreamGeneName() != null
              ? String.valueOf(module.getUpstreamGeneName()) : "\\N");
          writer.write("\t");
          writer.write(module.getUpstreamGenePosition() != null
              ? String.valueOf(module.getUpstreamGenePosition()) : "\\N");
          writer.write("\t");
          writer.write(module.getDownstreamGeneLocus() != null
              ? String.valueOf(module.getDownstreamGeneLocus()) : "\\N");
          writer.write("\t");
          writer.write(module.getDownstreamGeneName() != null
              ? String.valueOf(module.getDownstreamGeneName()) : "\\N");
          writer.write("\t");
          writer.write(module.getDownstreamGenePosition() != null
              ? String.valueOf(module.getDownstreamGenePosition()) : "\\N");
          for (Unit unit : moduleWithBestUnits.getBestUnits()) {
            writer.write("\t");
            writer.write(unit.getId() + " (" + unit.getName() + ")");
          }
          writer.write("\n");
        } catch (IOException e) {
          ExceptionUtils.optionallyPackageRuntimeException(e, "Could not write to writer");
        }
      }
    });

    writers.close();
  }

  /**
   * Export modules' sequence in FASTA format.
   *
   * @throws IOException
   *           could not write modules
   */
  public void exportFastas() throws IOException {
    logger.debug("exportFastas");
    final Writers writers = new Writers("all.fas.gz", "chr{0}.fas.gz");

    moduleMapper.selectAllWithSequence(new ResultHandler<ModuleWithSequence>() {
      @Override
      public void handleResult(ResultContext<? extends ModuleWithSequence> context) {
        ModuleWithSequence module = context.getResultObject();
        Organism organism = module.getOrganism();
        Version version = module.getVersion();
        String chromosome = module.getChromosome();
        try {
          Collection<Writer> currentWriters = new ArrayList<>();
          currentWriters.add(writers.get(organism, version));
          currentWriters.add(writers.get(organism, version, chromosome));
          write(currentWriters, ">gi|");
          write(currentWriters, module.getName());
          write(currentWriters, " ");
          write(currentWriters, "chr");
          write(currentWriters, module.getChromosome());
          write(currentWriters, ":");
          write(currentWriters, String.valueOf(module.getStart()));
          write(currentWriters, "-");
          write(currentWriters, String.valueOf(module.getEnd()));
          write(currentWriters, "\n");
          write(currentWriters, sequenceToFastaLikeFormat(module.getSequence()));
          write(currentWriters, "\n");
        } catch (IOException e) {
          ExceptionUtils.optionallyPackageRuntimeException(e, "Could not write to writer");
        }
      }
    });

    writers.close();
  }

  /**
   * Export modules' location as UCSC track.
   *
   * @throws IOException
   *           could not write modules
   */
  public void exportTracks() throws IOException {
    logger.debug("exportTracks");
    final Writers writers = new Writers("all.gff.gz", "chr{0}.gff.gz");
    writers.listener = new CreateWriterListener() {
      @Override
      public void writerCreated(Writer writer) throws IOException {
        writer.write("track name=module description=\"PReMod Predicted Regulatory Modules\"");
        writer.write("\n");
      }
    };

    moduleMapper.selectAllWithSequence(new ResultHandler<ModuleWithSequence>() {
      @Override
      public void handleResult(ResultContext<? extends ModuleWithSequence> context) {
        ModuleWithSequence module = context.getResultObject();
        Organism organism = module.getOrganism();
        Version version = module.getVersion();
        String chromosome = module.getChromosome();
        try {
          Collection<Writer> currentWriters = new ArrayList<>();
          currentWriters.add(writers.get(organism, version));
          currentWriters.add(writers.get(organism, version, chromosome));

          write(currentWriters, "chr");
          write(currentWriters, module.getChromosome());
          write(currentWriters, "\t");
          write(currentWriters, "PReMod");
          write(currentWriters, "\t");
          write(currentWriters, "Module");
          write(currentWriters, "\t");
          write(currentWriters, String.valueOf(module.getStart()));
          write(currentWriters, "\t");
          write(currentWriters, String.valueOf(module.getEnd()));
          write(currentWriters, "\t");
          write(currentWriters, module.getScore() != null
              ? String.valueOf(module.getScore().intValue() * 10) : "\\N");
          write(currentWriters, "\t");
          write(currentWriters, ".");
          write(currentWriters, "\t");
          write(currentWriters, ".");
          write(currentWriters, "\t");
          write(currentWriters, "module");
          write(currentWriters, version.ucscName());
          write(currentWriters, "_");
          write(currentWriters, organism.ucscName());
          write(currentWriters, "_");
          write(currentWriters, module.getName());
          write(currentWriters, "\n");
        } catch (IOException e) {
          ExceptionUtils.optionallyPackageRuntimeException(e, "Could not write to writer");
        }
      }
    });

    writers.close();
  }

  protected void setFolder(Organism organism, Version version, File folder) {
    if (!folders.containsKey(organism)) {
      folders.put(organism, new HashMap<Version, File>());
    }
    folders.get(organism).put(version, folder);
  }

  private void createAndSetFolders(File root) {
    File humanLatest = new File(root, "human/1");
    File humanArticle = new File(root, "human/0");
    File mouseLatest = new File(root, "mouse/1");
    humanLatest.mkdirs();
    humanArticle.mkdirs();
    mouseLatest.mkdirs();
    setFolder(Organism.HUMAN, Version.LATEST, humanLatest);
    setFolder(Organism.HUMAN, Version.ARTICLE, humanArticle);
    setFolder(Organism.MOUSE, Version.LATEST, mouseLatest);
  }

  private File getFile(Organism organism, Version version, String baseFilename) {
    File folder = folders.get(organism).get(version);
    StringBuilder builder = new StringBuilder(baseFilename);
    if (appendOrganismToFilename) {
      builder.insert(0, "_");
      builder.insert(0, organism.name().toLowerCase());
    }
    return new File(folder, builder.toString());
  }

  /**
   * Export database.
   *
   * @throws IOException
   *           could not write database
   */
  public void exportDatabase(File data) throws IOException {
    File database = new File(data, "database");
    database.mkdir();
    appendOrganismToFilename = true;
    createAndSetFolders(database);
    File file = getFile(Organism.HUMAN, Version.LATEST, "module_database.txt.gz");
    if (!file.exists()) {
      logger.info("exportModules since file {} does not exists", file);
      exportModules();
    } else {
      logger.info("Skipping exportModules since file {} exists", file);
    }
    file = getFile(Organism.HUMAN, Version.LATEST, "module_sequence_database.txt.gz");
    if (!file.exists()) {
      logger.info("exportModuleSequences since file {} does not exists", file);
      exportModuleSequences();
    } else {
      logger.info("Skipping exportModuleSequences since file {} exists", file);
    }
    file = getFile(Organism.HUMAN, Version.LATEST, "matrix_database.txt.gz");
    if (!file.exists()) {
      logger.info("exportUnits since file {} does not exists", file);
      exportUnits();
    } else {
      logger.info("Skipping exportUnits since file {} exists", file);
    }
    file = getFile(Organism.HUMAN, Version.LATEST, "matrix_occurence_database.txt.gz");
    if (!file.exists()) {
      logger.info("exportUnitOccurrences since file {} does not exists", file);
      exportUnitOccurrences();
    } else {
      logger.info("Skipping exportUnits since file {} exists", file);
    }
  }

  /**
   * Export modules.
   *
   * @throws IOException
   *           could not write modules
   */
  public void exportModule(File data) throws IOException {
    File module = new File(data, "module");
    module.mkdir();
    appendOrganismToFilename = true;
    createAndSetFolders(module);
    File file = getFile(Organism.HUMAN, Version.LATEST, "module_comma.txt.gz");
    if (!file.exists()) {
      logger.info("exportCsv since file {} does not exists", file);
      exportCsv();
    } else {
      logger.info("Skipping exportCsv since file {} exists", file);
    }
    file = getFile(Organism.HUMAN, Version.LATEST, "module_tab.txt.gz");
    if (!file.exists()) {
      logger.info("exportTabs since file {} does not exists", file);
      exportTabs();
    } else {
      logger.info("Skipping exportTabs since file {} exists", file);
    }
  }

  /**
   * Export modules' sequence.
   *
   * @throws IOException
   *           could not write modules' sequence
   */
  public void exportSequence(File data) throws IOException {
    File sequence = new File(data, "sequence");
    sequence.mkdir();
    appendOrganismToFilename = false;
    createAndSetFolders(sequence);
    File file = getFile(Organism.HUMAN, Version.LATEST, "all.fas.gz");
    if (!file.exists()) {
      logger.info("exportFastas since file {} does not exists", file);
      exportFastas();
    } else {
      logger.info("Skipping exportFastas since file {} exists", file);
    }
  }

  /**
   * Export modules as UCSC track.
   *
   * @throws IOException
   *           could not write modules
   */
  public void exportUcsc(File data) throws IOException {
    File ucsc = new File(data, "UCSC");
    ucsc.mkdir();
    appendOrganismToFilename = false;
    createAndSetFolders(ucsc);
    File file = getFile(Organism.HUMAN, Version.LATEST, "all.gff.gz");
    if (!file.exists()) {
      logger.info("exportTracks since file {} does not exists", file);
      exportTracks();
    } else {
      logger.info("Skipping exportTracks since file {} exists", file);
    }
  }

  /**
   * Export database.
   *
   * @param args
   *          not used
   */
  public static void main(String[] args) throws Throwable {
    Logger logger = LoggerFactory.getLogger(ExportForDownload.class);
    try (ConfigurableApplicationContext context = SpringApplication.run(Main.class, args)) {
      final ExportForDownload exportOldFormat = context.getBean(ExportForDownload.class);
      File root;
      if (args.length > 0) {
        root = new File(args[0]);
        logger.info("Using directory {} for export", root);
      } else {
        root = new File("f:/temp/premod");
        logger.warn("Using default directory {} for export", root);
      }
      root.mkdirs();
      File data = new File(root, "data");
      data.mkdir();
      exportOldFormat.exportDatabase(data);
      exportOldFormat.exportModule(data);
      exportOldFormat.exportSequence(data);
      exportOldFormat.exportUcsc(data);
    }
  }
}
