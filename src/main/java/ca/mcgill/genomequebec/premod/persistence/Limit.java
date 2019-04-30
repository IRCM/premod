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

package ca.mcgill.genomequebec.premod.persistence;

/**
 * Limits number of rows returned by database.
 */
public interface Limit {
  public static class Default implements Limit {
    public Integer offset;
    public Integer rowCount;

    public Default() {
    }

    public Default(Integer offset, Integer rowCount) {
      this.offset = offset;
      this.rowCount = rowCount;
    }

    @Override
    public Integer getOffset() {
      return offset;
    }

    public void setOffset(Integer offset) {
      this.offset = offset;
    }

    @Override
    public Integer getRowCount() {
      return rowCount;
    }

    public void setRowCount(Integer rowCount) {
      this.rowCount = rowCount;
    }
  }

  public Integer getOffset();

  public Integer getRowCount();
}
