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
import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.service.PrecomputeService;
import ca.mcgill.genomequebec.premod.utils.ExceptionUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import javax.inject.Inject;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Computes closest genes for all modules to improve search speed.
 */
public class PrecomputeClosestGenes {
  private final Logger logger = LoggerFactory.getLogger(PrecomputeClosestGenes.class);
  private final PrecomputeService precomputeService;
  private final SqlSessionFactory sqlSessionFactory;

  @Inject
  protected PrecomputeClosestGenes(PrecomputeService precomputeService,
      SqlSessionFactory sqlSessionFactory) {
    this.precomputeService = precomputeService;
    this.sqlSessionFactory = sqlSessionFactory;
  }

  /**
   * Updates module's closest gene in database.
   *
   * @throws IOException
   *           could not read SQL script
   * @throws SQLException
   *           could not run SQL script
   */
  public void updateClosestGenesDbScript() throws IOException, SQLException {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Connection connection = sqlSession.getConnection();
      ScriptRunner scriptRunner = new ScriptRunner(connection);
      scriptRunner.setStopOnError(true);
      Reader reader = Resources.getResourceAsReader(
          PrecomputeClosestGenes.class.getName().replaceAll("\\.", "/") + ".sql");
      scriptRunner.runScript(reader);
    } finally {
      sqlSession.close();
    }
  }

  public void updateClosestGenes() throws IOException, SQLException {
    File file = findClosestGenes();
    updateClosestGenes(file);
  }

  private void updateClosestGenes(File closestGenesFile) throws SQLException {
    SqlSession sqlSession = sqlSessionFactory.openSession();
    try {
      Connection connection = sqlSession.getConnection();
      logger.trace("Loading file {} into temporary table", closestGenesFile);
      PreparedStatement createTemporaryTable = connection.prepareStatement(
          "CREATE TEMPORARY TABLE _modules (id BIGINT UNSIGNED, downstream_gene int(11), downstream_gene_locus int(10), downstream_gene_name VARCHAR(30), upstream_gene int(11), upstream_gene_locus int(10), upstream_gene_name VARCHAR(30))");
      try {
        createTemporaryTable.execute();
      } finally {
        createTemporaryTable.close();
      }
      PreparedStatement load =
          connection.prepareStatement("LOAD DATA LOCAL INFILE ? INTO TABLE _modules");
      try {
        load.setString(1, closestGenesFile.getPath());
        load.execute();
      } finally {
        load.close();
      }
      logger.trace("Update modules");
      SQL sql = new SQL() {
        {
          UPDATE("module JOIN _modules ON _modules.id = module.id");
          SET("module.downstream_gene = _modules.downstream_gene");
          SET("module.downstream_gene_locus = _modules.downstream_gene_locus");
          SET("module.downstream_gene_name = _modules.downstream_gene_name");
          SET("module.upstream_gene = _modules.upstream_gene");
          SET("module.upstream_gene_locus = _modules.upstream_gene_locus");
          SET("module.upstream_gene_name = _modules.upstream_gene_name");
        }
      };
      PreparedStatement update = connection.prepareStatement(sql.toString());
      try {
        update.execute();
      } finally {
        update.close();
      }
      PreparedStatement dropTemporaryTable =
          connection.prepareStatement("DROP TEMPORARY TABLE _modules");
      try {
        dropTemporaryTable.execute();
      } finally {
        dropTemporaryTable.close();
      }
    } finally {
      sqlSession.close();
    }
  }

  private File findClosestGenes() throws IOException {
    File output = File.createTempFile(PrecomputeClosestGenes.class.getSimpleName(), ".txt");
    final Writer writer =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));
    try {
      Queue<Module> modules = new LinkedList<>(precomputeService.modules());
      int totalCount = modules.size();
      logger.debug("Finding closest genes for {} modules", totalCount);
      int count = 0;
      while (!modules.isEmpty()) {
        Module module = modules.poll();
        count++;
        if (count % 10000 == 0) {
          logger.debug("Processing module {} of {}", count, totalCount);
        }
        Nucleotide downstream = precomputeService.closestDownstreamGene(module);
        Nucleotide upstream = precomputeService.closestUpstreamGene(module);
        try {
          writer.write(String.valueOf(module.getId()));
          writer.write("\t");
          writer.write(downstream != null
              ? String.valueOf(downstream.getRelativePosition(module.getMiddlePosition())) : "\\N");
          writer.write("\t");
          writer.write(downstream != null && downstream.getLocusLink() != null
              ? String.valueOf(downstream.getLocusLink()) : "\\N");
          writer.write("\t");
          writer.write(downstream != null ? downstream.getGene() : "\\N");
          writer.write("\t");
          writer.write(upstream != null
              ? String.valueOf(upstream.getRelativePosition(module.getMiddlePosition())) : "\\N");
          writer.write("\t");
          writer.write(upstream != null && upstream.getLocusLink() != null
              ? String.valueOf(upstream.getLocusLink()) : "\\N");
          writer.write("\t");
          writer.write(upstream != null ? upstream.getGene() : "\\N");
          writer.write("\n");
        } catch (IOException e) {
          throw new RuntimeException("Could not write to writer", e);
        }
      }
    } catch (RuntimeException e) {
      ExceptionUtils.throwExceptionIfMatch(e.getCause(), IOException.class);
      throw e;
    } finally {
      writer.close();
    }
    return output;
  }

  /**
   * Updates module's closest gene in database.
   *
   * @param args
   *          not used
   */
  public static void main(String[] args) throws Throwable {
    try (ConfigurableApplicationContext context = SpringApplication.run(Main.class, args)) {
      PrecomputeClosestGenes precomputeClosestGenes = context.getBean(PrecomputeClosestGenes.class);
      precomputeClosestGenes.updateClosestGenes();
    }
  }
}
