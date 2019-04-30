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

package ca.mcgill.genomequebec.premod.persistence.filter;

import java.util.Collection;
import java.util.HashSet;

/**
 * Filter for modules that applies a GROUP BY clause and potential condition.
 */
public interface ModuleGroupByDatabaseFilter extends ModuleDatabaseFilter {
  /**
   * Temporary table column that should be set.
   */
  public static enum Column {
    ;
    public final Collection<ModuleDatabaseFilter.Table> requirements;

    Column(ModuleDatabaseFilter.Table... requirements) {
      this.requirements = new HashSet<ModuleDatabaseFilter.Table>();
      for (ModuleDatabaseFilter.Table table : requirements) {
        this.requirements.add(table);
        this.requirements.addAll(table.requirements);
      }
    }
  }

  /**
   * Returns temporary columns to include in SQL query. Returned collection should contain at least
   * one {@link Column}.
   * 
   * @return temporary columns to include in SQL query.
   */
  public Collection<Column> columns();

  /**
   * Returns condition(s) to include in SQL query or <code>null</code> if no conditions are
   * necessary. Condition(s) are included using HAVING/AND.
   * 
   * @return condition(s) to include in SQL query
   */
  public String having();
}
