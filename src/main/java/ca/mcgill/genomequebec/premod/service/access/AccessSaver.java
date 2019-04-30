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

package ca.mcgill.genomequebec.premod.service.access;

import ca.mcgill.genomequebec.premod.MainConfiguration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Save number of access.
 */
public class AccessSaver {
  private final Logger logger = LoggerFactory.getLogger(AccessSaver.class);
  @Inject
  private AccessKeeper accessKeeper;
  @Inject
  private MainConfiguration mainConfiguration;

  @Inject
  protected AccessSaver(AccessKeeper accessKeeper, MainConfiguration mainConfiguration) {
    this.accessKeeper = accessKeeper;
    this.mainConfiguration = mainConfiguration;
  }

  /**
   * Saves access to file.
   */
  public void saveAccess() {
    try {
      File location = new File(mainConfiguration.home(), "premod_access.txt");
      Writer writer = new OutputStreamWriter(new FileOutputStream(location, true), "UTF-8");
      try {
        if (location.length() == 0) {
          StringBuffer content = new StringBuffer();
          content.append("Date\tSession\tDownload\n");
          writer.write(content.toString());
        }
        long copySessionNumber = accessKeeper.getNumberSession();
        long copyDownloadNumber = accessKeeper.getNumberDownload();
        StringBuffer content = new StringBuffer();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        content.append(fmt.print(new DateTime()));
        content.append("\t");
        content.append(copySessionNumber);
        content.append("\t");
        content.append(copyDownloadNumber);
        content.append("\n");
        writer.write(content.toString());
        accessKeeper.minusSession(copySessionNumber);
        accessKeeper.minusDownload(copyDownloadNumber);
      } finally {
        writer.close();
      }
    } catch (IOException e) {
      logger.error("Could not save statistics", e);
    }
  }
}
