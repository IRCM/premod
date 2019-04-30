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
import ca.mcgill.genomequebec.premod.service.UnitService;
import ca.mcgill.genomequebec.premod.service.UnitService.UnitWithExtra;
import ca.mcgill.genomequebec.premod.test.config.ServiceTestAnnotations;
import ca.mcgill.genomequebec.premod.test.utils.ExcelUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for {@link UnitCsvWriter2}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ServiceTestAnnotations
public class UnitCsvWriterTest {
  @Inject
  private UnitService unitService;
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void write() throws Throwable {
    File actual = temporaryFolder.newFile("UnitCsvWriterTest.csv");
    UnitCsvWriter writer = new UnitCsvWriter(
        new OutputStreamWriter(new FileOutputStream(actual), "UTF-8"), Locale.CANADA);
    try {
      writer.writeHeader();
      List<UnitWithExtra> units = unitService.statistics(Organism.HUMAN, Version.LATEST);
      for (UnitWithExtra unit : units) {
        writer.writeUnit(unit);
      }
    } finally {
      writer.close();
    }

    File expected =
        new File(this.getClass().getResource("/service/parser/human_statistic.csv").toURI());
    ExcelUtils.assertEqualsCsv(expected, actual);
  }
}
