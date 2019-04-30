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

package ca.mcgill.genomequebec.premod.database;

import ca.mcgill.genomequebec.premod.Main;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Class that temporarily contains some algorithms to test.
 */
public class UpdateNuclotides {
  private static enum Tables {
    gene_info("ftp://ftp.ncbi.nih.gov/gene/DATA/gene_info.gz", 1),
    gene2accession("ftp://ftp.ncbi.nih.gov/gene/DATA/gene2accession.gz", 1),
    refflat_human("ftp://hgdownload.cse.ucsc.edu/goldenPath/hg17/database/refFlat.txt.gz", 0),
    refflat_mouse("ftp://hgdownload.cse.ucsc.edu/goldenPath/mm7/database/refFlat.txt.gz", 0);
    private final URL downloadUrl;
    private final int ignoreLines;

    Tables(String downloadUrl, int ignoreLines) {
      try {
        this.downloadUrl = new URL(downloadUrl);
      } catch (MalformedURLException e) {
        throw new IllegalStateException("Could not convert " + downloadUrl + " to java.net.URL", e);
      }
      this.ignoreLines = ignoreLines;
    }
  }

  private final Logger logger = LoggerFactory.getLogger(UpdateNuclotides.class);
  private final SqlSessionFactory sqlSessionFactory;

  @Inject
  protected UpdateNuclotides(SqlSessionFactory sqlSessionFactory) {
    this.sqlSessionFactory = sqlSessionFactory;
  }

  /**
   * Updates nucleotides.
   *
   * @throws IOException
   *           could not download nucleotides
   * @throws SQLException
   *           could not run SQL script
   */
  public void updateNucleotides() throws IOException, SQLException {
    Map<Tables, File> files = downloadFiles();
    files = uncompressFiles(files);
    File geneInfo = File.createTempFile(Tables.gene_info.name(), ".txt");
    convertGeneInfoToMysql(files.get(Tables.gene_info), geneInfo);
    files.put(Tables.gene_info, geneInfo).delete();
    File gene2accession = File.createTempFile(Tables.gene2accession.name(), ".txt");
    convertGeneToAccessionToMysql(files.get(Tables.gene2accession), gene2accession);
    files.put(Tables.gene2accession, gene2accession).delete();
    createTables();
    loadTables(files);
    for (File file : files.values()) {
      file.delete();
    }
    loadNucleotide();
    dropTables();
  }

  private Map<Tables, File> downloadFiles() throws IOException {
    Map<Tables, File> downloadedFiles = new HashMap<>();
    for (Tables table : Tables.values()) {
      URLConnection connection = table.downloadUrl.openConnection();
      logger.trace("Downloading table content {} from {}", table, table.downloadUrl);
      BufferedInputStream input = new BufferedInputStream(connection.getInputStream());
      try {
        File outputFile = File.createTempFile(table.name(), ".gz");
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile));
        try {
          IOUtils.copyLarge(input, output);
          downloadedFiles.put(table, outputFile);
        } finally {
          output.close();
        }
      } finally {
        input.close();
      }
    }
    return downloadedFiles;
  }

  private Map<Tables, File> uncompressFiles(Map<Tables, File> files) throws IOException {
    Map<Tables, File> uncompressedFiles = new HashMap<>();
    for (Map.Entry<Tables, File> fileEntry : files.entrySet()) {
      Tables table = fileEntry.getKey();
      File inputFile = fileEntry.getValue();
      logger.trace("Uncompressing file {} for table {}", inputFile, table);
      GZIPInputStream input = new GZIPInputStream(new FileInputStream(inputFile));
      try {
        File outputFile = File.createTempFile(table.name(), ".txt");
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(outputFile));
        try {
          IOUtils.copyLarge(input, output);
          uncompressedFiles.put(table, outputFile);
        } finally {
          output.close();
        }
      } finally {
        input.close();
      }
    }
    return uncompressedFiles;
  }

  private void convertGeneInfoToMysql(File input, File output) throws IOException {
    logger.trace("Convert {} file {} to MySQL import format", Tables.gene_info, input);
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF-8"));
    try {
      Writer writer =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));
      try {
        String line;
        while ((line = reader.readLine()) != null) {
          String[] columns = line.split("\t");
          for (int i = 0; i < columns.length; i++) {
            // Adapt date.
            if (i == 14 && !columns[i].equals("-")) {
              StringBuilder builder = new StringBuilder(columns[i]);
              builder.insert(4, "-");
              builder.insert(7, "-");
              columns[i] = builder.toString();
            }
            // Convert null.
            if (columns[i].equals("-")) {
              columns[i] = "\\N";
            }
            // Write converted columns.
            if (i != 0) {
              writer.write("\t");
            }
            writer.write(columns[i]);
          }
          writer.write("\n");
        }
      } finally {
        writer.close();
      }
    } finally {
      reader.close();
    }
  }

  private void convertGeneToAccessionToMysql(File input, File output) throws IOException {
    logger.trace("Convert {} file {} to MySQL import format", Tables.gene2accession, input);
    BufferedReader reader =
        new BufferedReader(new InputStreamReader(new FileInputStream(input), "UTF-8"));
    try {
      Writer writer =
          new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));
      try {
        String line;
        while ((line = reader.readLine()) != null) {
          String[] columns = line.split("\t");
          for (int i = 0; i < columns.length; i++) {
            // Adapt accessions.
            if ((i == 3 || i == 5 || i == 7) && columns[i].contains(".")) {
              columns[i] = columns[i].substring(0, columns[i].indexOf("."));
            }
            // Convert null.
            if (i != 11 && columns[i].equals("-")) {
              columns[i] = "\\N";
            } else if (i == 11 && columns[i].equals("?")) {
              columns[i] = "\\N";
            }
            // Write converted columns.
            if (i != 0) {
              writer.write("\t");
            }
            writer.write(columns[i]);
          }
          writer.write("\n");
        }
      } finally {
        writer.close();
      }
    } finally {
      reader.close();
    }
  }

  private void createTables() throws IOException, SQLException {
    logger.trace("Create tables");
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Connection connection = sqlSession.getConnection();
      ScriptRunner scriptRunner = new ScriptRunner(connection);
      scriptRunner.setStopOnError(true);
      Reader reader = Resources.getResourceAsReader(
          UpdateNuclotides.class.getName().replaceAll("\\.", "/") + "_CreateTables.sql");
      try {
        scriptRunner.runScript(reader);
      } finally {
        reader.close();
      }
    } finally {
      sqlSession.close();
    }
  }

  private void loadTables(Map<Tables, File> files) throws SQLException {
    for (Map.Entry<Tables, File> fileEntry : files.entrySet()) {
      Tables table = fileEntry.getKey();
      File file = fileEntry.getValue();
      logger.trace("Loading file {} into table {}", file, table);
      SqlSession sqlSession = sqlSessionFactory.openSession();
      try {
        Connection connection = sqlSession.getConnection();
        PreparedStatement delete = connection.prepareStatement("DELETE FROM " + table.name());
        try {
          delete.execute();
        } finally {
          delete.close();
        }
        PreparedStatement load = connection.prepareStatement("LOAD DATA INFILE ? INTO TABLE "
            + table.name() + " IGNORE " + table.ignoreLines + " LINES");
        try {
          load.setString(1, file.getPath());
          load.execute();
        } finally {
          load.close();
        }
      } finally {
        sqlSession.close();
      }
    }
  }

  private void loadNucleotide() throws IOException, SQLException {
    logger.trace("Loading nucleotide table");
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Connection connection = sqlSession.getConnection();
      ScriptRunner scriptRunner = new ScriptRunner(connection);
      scriptRunner.setStopOnError(true);
      Reader reader = Resources.getResourceAsReader(
          UpdateNuclotides.class.getName().replaceAll("\\.", "/") + "_Nucleotide.sql");
      try {
        scriptRunner.runScript(reader);
      } finally {
        reader.close();
      }
    } finally {
      sqlSession.close();
    }
  }

  private void dropTables() throws IOException, SQLException {
    logger.trace("Drop tables");
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Connection connection = sqlSession.getConnection();
      ScriptRunner scriptRunner = new ScriptRunner(connection);
      scriptRunner.setStopOnError(true);
      Reader reader = Resources.getResourceAsReader(
          UpdateNuclotides.class.getName().replaceAll("\\.", "/") + "_DropTables.sql");
      try {
        scriptRunner.runScript(reader);
      } finally {
        reader.close();
      }
    } finally {
      sqlSession.close();
    }
  }

  /**
   * Updates nucleotides.
   *
   * @param args
   *          not used
   */
  public static void main(String[] args) throws Throwable {
    try (ConfigurableApplicationContext context = SpringApplication.run(Main.class, args)) {
      UpdateNuclotides updateNuclotides = context.getBean(UpdateNuclotides.class);
      updateNuclotides.updateNucleotides();
    }
  }
}
