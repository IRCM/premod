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
import java.util.LinkedList;
import java.util.Map;

/**
 * Only modules containing all specified best unit(s) passes filter.
 */
public class ModuleGroupByBestUnitFilter
    implements ModuleDatabaseFilter, ModuleGroupByDatabaseFilter {

  private Collection<String> ids;
  private ModuleBestUnitFilter moduleBestUnitFilter;

  public ModuleGroupByBestUnitFilter(Collection<String> ids) {
    this.ids = ids;
    moduleBestUnitFilter = new ModuleBestUnitFilter(ids);
  }

  @Override
  public void addParameters(Map<String, Object> parameters) {
    parameters.put("unitCount", ids.size());
    moduleBestUnitFilter.addParameters(parameters);
  }

  @Override
  public Collection<Table> joins() {
    return moduleBestUnitFilter.joins();
  }

  @Override
  public Collection<Column> columns() {
    return new LinkedList<Column>();
  }

  @Override
  public String where() {
    return moduleBestUnitFilter.where();
  }

  @Override
  public String having() {
    return "COUNT(DISTINCT unit.id) = #{unitCount}";
  }
}
