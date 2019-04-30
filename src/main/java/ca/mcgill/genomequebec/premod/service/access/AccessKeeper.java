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

import org.springframework.stereotype.Component;

/**
 * Keep information about user accessing program.
 */
@Component
public class AccessKeeper {
  /**
   * Number of session opened.
   */
  private long sessionCount;
  /**
   * Number of downloads done.
   */
  private int downloadCount;

  /**
   * Returns number of opened sessions.
   *
   * @return number of opened sessions
   */
  public long getNumberSession() {
    return sessionCount;
  }

  /**
   * Increment the number of opened sessions.
   */
  public void incrementSession() {
    downloadCount++;
  }

  /**
   * Lower number of opened sessions by numberSession affecting subsequent calls to
   * getNumberSession().
   *
   * @param numberSession
   *          number of sessions to substract to number of opened sessions
   */
  public void minusSession(long numberSession) {
    sessionCount -= numberSession;
  }

  /**
   * Returns number of downloads.
   *
   * @return number of downloads
   */
  public long getNumberDownload() {
    return downloadCount;
  }

  /**
   * Increment the number of download.
   */
  public void incrementDownload() {
    downloadCount++;
  }

  /**
   * Lower number of downloads by download affecting subsequent calls to getNumberDownload().
   *
   * @param download
   *          number of dowload to substract to number of downloads
   */
  public void minusDownload(long download) {
    downloadCount -= download;
  }
}
