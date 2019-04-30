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

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Filter modules during a search.
 */
public interface ModuleDatabaseFilter {
  /**
   * Tables that can be used by filter. Table name to use in where condition is the
   * {@link Enum#name() enum's name}.
   */
  public static enum Table {
    module(), module_to_unit(module), unit(module_to_unit, module), nucleotide(module);
    public Collection<Table> requirements;

    Table(Table... requirements) {
      this.requirements = Arrays.asList(requirements);
    }
  }

  /**
   * Add parameters for current query.
   * 
   * @param parameters
   *          parameters for current query
   */
  public void addParameters(Map<String, Object> parameters);

  /**
   * Returns table to join to SQL query. Returned collection should contain at least one
   * {@link Table}.
   * 
   * @return table to join to SQL query
   */
  public Collection<Table> joins();

  /**
   * Returns condition(s) to include in SQL query or <code>null</code> if no conditions are
   * necessary. Condition(s) are included using WHERE/AND.
   * 
   * @return condition(s) to include in SQL query
   */
  public String where();
}
