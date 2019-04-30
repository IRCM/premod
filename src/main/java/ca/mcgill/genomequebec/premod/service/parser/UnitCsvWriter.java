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

import au.com.bytecode.opencsv.CSVWriter;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.service.UnitService.Extra;
import ca.mcgill.genomequebec.premod.service.UnitService.UnitWithExtra;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Creates CSV files containing units' information.
 */
public class UnitCsvWriter {
  /**
   * Columns of Excel file for units.
   */
  public static enum Column {
    ID, NAME, DESCRIPTION, TAG, NO_TAG, ALL_TAG;
    public String getHeader(ResourceBundle bundle) {
      return bundle.getString("header." + this.name());
    }
  }

  private final CSVWriter writer;
  private final ResourceBundle bundle;

  /**
   * Creates unit CSV writer.
   *
   * @param writer
   *          where to write CSV content
   * @param locale
   *          user's locale
   */
  public UnitCsvWriter(Writer writer, Locale locale) {
    this.writer = new CSVWriter(writer);
    bundle = ResourceBundle.getBundle(UnitCsvWriter.class.getName(), locale);
  }

  /**
   * Write CSV file header.
   *
   * @throws IOException
   *           exception while writing CSV content
   */
  public void writeHeader() throws IOException {
    String[] headers = new String[Column.values().length];
    int index = 0;
    for (Column column : Column.values()) {
      headers[index++] = column.getHeader(bundle);
    }
    writer.writeNext(headers);
  }

  /**
   * Writes unit in CSV file.
   *
   * @param unit
   *          unit to write
   * @throws IOException
   *           exception while writing CSV content
   */
  public void writeUnit(UnitWithExtra unit) throws IOException {
    Unit actualUnit = unit.getUnit();
    Extra extra = unit.getExtra();
    String[] columns = new String[Column.values().length];
    int index = 0;
    for (Column column : Column.values()) {
      String columnValue = null;
      switch (column) {
        case ID:
          columnValue = actualUnit.getId();
          break;
        case NAME:
          columnValue = actualUnit.getName();
          break;
        case DESCRIPTION:
          columnValue = actualUnit.getDescription();
          break;
        case TAG:
          columnValue = String.valueOf(extra.getTag());
          break;
        case NO_TAG:
          columnValue = String.valueOf(extra.getNoTag());
          break;
        case ALL_TAG:
          columnValue = String.valueOf(extra.getAny());
          break;
        default:
          throw new AssertionError("column " + column + " not covered in switch");
      }
      columns[index++] = columnValue;
    }
    writer.writeNext(columns);
  }

  /**
   * Closes the writer.
   *
   * @throws IOException
   *           If an I/O error occurs
   */
  public void close() throws IOException {
    writer.close();
  }
}
