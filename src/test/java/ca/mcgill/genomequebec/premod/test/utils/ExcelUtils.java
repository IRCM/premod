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

package ca.mcgill.genomequebec.premod.test.utils;

import static org.junit.Assert.assertEquals;

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utilities to compare Excel files.
 */
public class ExcelUtils {
  /**
   * Validates that 2 CSV files are identical.
   * 
   * @param expected
   *          expected CSV files
   * @param actual
   *          actual CSV files
   * @throws IOException
   *           could not read one of the files
   */
  public static void assertEqualsCsv(File expected, File actual) throws IOException {
    CSVReader expectedReader = null;
    CSVReader actualReader = null;
    try {
      expectedReader = new CSVReader(new InputStreamReader(new FileInputStream(expected)));
      actualReader = new CSVReader(new InputStreamReader(new FileInputStream(actual)));

      String[] expectedColumns = expectedReader.readNext();
      String[] actualColumns = actualReader.readNext();
      int lineNumber = 1;
      while (expectedColumns != null && actualColumns != null) {
        for (int i = 0; i < expectedColumns.length; i++) {
          if (actualColumns.length > i) {
            assertEquals("Column " + i + " at line " + lineNumber, expectedColumns[i],
                actualColumns[i]);
          } else {
            assertEquals("Column " + i + " at line " + lineNumber, expectedColumns[i], "");
          }
        }
        for (int i = expectedColumns.length; i < actualColumns.length; i++) {
          assertEquals("Column " + i + " at line " + lineNumber, "", actualColumns[i]);
        }

        expectedColumns = expectedReader.readNext();
        actualColumns = actualReader.readNext();
        lineNumber++;
      }
      lineNumber++;

      while (expectedColumns != null) {
        for (int i = 0; i < expectedColumns.length; i++) {
          assertEquals("Column " + i + " at line " + lineNumber, expectedColumns[i], "");
        }

        expectedColumns = expectedReader.readNext();
        lineNumber++;
      }
      while (actualColumns != null) {
        for (int i = 0; i < actualColumns.length; i++) {
          assertEquals("Column " + i + " at line " + lineNumber, "", actualColumns[i]);
        }

        actualColumns = actualReader.readNext();
        lineNumber++;
      }
    } finally {
      if (expectedReader != null) {
        expectedReader.close();
      }
      if (actualReader != null) {
        actualReader.close();
      }
    }
  }
}
