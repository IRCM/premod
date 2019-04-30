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
import java.util.Collections;
import java.util.Map;

/**
 * Only modules containing specified unit(s) passes filter.
 */
public class ModuleUnitFilter implements ModuleDatabaseFilter {

  private final Collection<String> ids;

  public ModuleUnitFilter(Collection<String> ids) {
    this.ids = ids;
  }

  @Override
  public void addParameters(Map<String, Object> parameters) {
    int index = -1;
    for (String id : ids) {
      index++;
      parameters.put("unit_" + index, id);
    }
  }

  @Override
  public Collection<Table> joins() {
    return Collections.nCopies(1, Table.unit);
  }

  @Override
  public String where() {
    StringBuilder condition = new StringBuilder();
    condition.append("(");
    for (int index = 0; index < ids.size(); index++) {
      if (index > 0) {
        condition.append(" OR ");
      }
      condition.append("unit.id = #{unit_");
      condition.append(index);
      condition.append("}");
    }
    condition.append(")");
    return condition.toString();
  }
}
