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

package ca.mcgill.genomequebec.premod.service.parser;

import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.persistence.Limit;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleDatabaseFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleGeneNameFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleOrganismFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleVersionFilter;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import ca.mcgill.genomequebec.premod.service.ModuleService.ModuleForSearchHandler;
import ca.mcgill.genomequebec.premod.service.ModuleService.ModuleWithBestUnits;
import ca.mcgill.genomequebec.premod.service.ModuleService.SearchParameters;
import ca.mcgill.genomequebec.premod.service.ModuleService.Sort;
import ca.mcgill.genomequebec.premod.test.config.ServiceTestAnnotations;
import ca.mcgill.genomequebec.premod.test.utils.ExcelUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for {@link ModuleCsvWriter2}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ServiceTestAnnotations
public class ModuleCsvWriterTest {
  @Inject
  private ModuleService moduleService;
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void write() throws Throwable {
    File actual = temporaryFolder.newFile("ModuleCsvWriter.csv");
    final ModuleCsvWriter writer = new ModuleCsvWriter(
        new OutputStreamWriter(new FileOutputStream(actual), "UTF-8"), Locale.CANADA);
    try {
      writer.writeHeader();
      final Collection<ModuleDatabaseFilter> filters = new ArrayList<>();
      filters.add(new ModuleGeneNameFilter(Collections.nCopies(1, "POLR2A")));
      filters.add(new ModuleOrganismFilter(Organism.HUMAN));
      filters.add(new ModuleVersionFilter(Version.LATEST));
      moduleService.searchLowMemory(new SearchParameters() {
        @Override
        public Collection<ModuleDatabaseFilter> filters() {
          return filters;
        }

        @Override
        public Sort sort() {
          return Sort.SCORE;
        }

        @Override
        public boolean descending() {
          return true;
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
        public void handleResult(ModuleWithBestUnits module) {
          try {
            writer.writeModule(module);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      });
    } finally {
      writer.close();
    }

    File expected =
        new File(this.getClass().getResource("/service/parser/POLR2A_modules.csv").toURI());
    ExcelUtils.assertEqualsCsv(expected, actual);
  }
}
