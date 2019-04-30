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
import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.service.ModuleService.ModuleWithBestUnits;
import ca.qc.ircm.bio.ChromosomeFormat;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Creates CSV files containing modules' information.
 */
public class ModuleCsvWriter {
  /**
   * Columns of Excel file for modules.
   */
  public static enum Column {
    NAME, CHROMOSOME, SCORE, UPSTREAM_GENE_ID, UPSTREAM_GENE_NAME, UPSTREAM_GENE_POSITION,
    DOWNSTREAM_GENE_ID, DOWNSTREAM_GENE_NAME, DOWNSTREAM_GENE_POSITION, BEST_TAG_1, BEST_TAG_2,
    BEST_TAG_3, BEST_TAG_4, BEST_TAG_5;
    public String getHeader(ResourceBundle bundle) {
      return bundle.getString("header." + this.name());
    }
  }

  private final CSVWriter writer;
  private final ResourceBundle bundle;
  private final DecimalFormat scoreFormat = new DecimalFormat("#");
  private final ChromosomeFormat chromosomeFormat = new ChromosomeFormat();

  /**
   * Creates module CSV writer.
   *
   * @param locale
   *          user's locale
   * @param writer
   *          where to write CSV content
   */
  public ModuleCsvWriter(Writer writer, Locale locale) {
    this.writer = new CSVWriter(writer);
    bundle = ResourceBundle.getBundle(ModuleCsvWriter.class.getName(), locale);
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
   * Writes module in CSV file.
   *
   * @param module
   *          module to write
   * @throws IOException
   *           exception while writing CSV content
   */
  public void writeModule(ModuleWithBestUnits module) throws IOException {
    Module actualModule = module.getModule();
    List<Unit> bestUnits = module.getBestUnits();
    String[] columns = new String[Column.values().length];
    int index = 0;
    for (Column column : Column.values()) {
      String columnValue = null;
      switch (column) {
        case NAME:
          columnValue = actualModule.getName();
          break;
        case CHROMOSOME:
          columnValue = chromosomeFormat.format(actualModule.getChromosomeLocation());
          break;
        case SCORE:
          columnValue = scoreFormat.format(actualModule.getScore());
          break;
        case UPSTREAM_GENE_ID:
          columnValue = actualModule.getUpstreamGeneLocus();
          break;
        case UPSTREAM_GENE_NAME:
          columnValue = actualModule.getUpstreamGeneName();
          break;
        case UPSTREAM_GENE_POSITION:
          columnValue = String.valueOf(actualModule.getUpstreamGenePosition());
          break;
        case DOWNSTREAM_GENE_ID:
          columnValue = actualModule.getDownstreamGeneLocus();
          break;
        case DOWNSTREAM_GENE_NAME:
          columnValue = actualModule.getDownstreamGeneName();
          break;
        case DOWNSTREAM_GENE_POSITION:
          columnValue = String.valueOf(actualModule.getDownstreamGenePosition());
          break;
        case BEST_TAG_1: {
          Unit unit = bestUnits.size() > 0 ? bestUnits.get(0) : null;
          if (unit != null) {
            columnValue = MessageFormat.format(bundle.getString(column.name() + ".value"),
                unit.getId(), unit.getName());
          } else {
            columnValue = "";
          }
          break;
        }
        case BEST_TAG_2: {
          Unit unit = bestUnits.size() > 1 ? bestUnits.get(1) : null;
          if (unit != null) {
            columnValue = MessageFormat.format(bundle.getString(column.name() + ".value"),
                unit.getId(), unit.getName());
          } else {
            columnValue = "";
          }
          break;
        }
        case BEST_TAG_3: {
          Unit unit = bestUnits.size() > 2 ? bestUnits.get(2) : null;
          if (unit != null) {
            columnValue = MessageFormat.format(bundle.getString(column.name() + ".value"),
                unit.getId(), unit.getName());
          } else {
            columnValue = "";
          }
          break;
        }
        case BEST_TAG_4: {
          Unit unit = bestUnits.size() > 3 ? bestUnits.get(3) : null;
          if (unit != null) {
            columnValue = MessageFormat.format(bundle.getString(column.name() + ".value"),
                unit.getId(), unit.getName());
          } else {
            columnValue = "";
          }
          break;
        }
        case BEST_TAG_5: {
          Unit unit = bestUnits.size() > 4 ? bestUnits.get(4) : null;
          if (unit != null) {
            columnValue = MessageFormat.format(bundle.getString(column.name() + ".value"),
                unit.getId(), unit.getName());
          } else {
            columnValue = "";
          }
          break;
        }
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
