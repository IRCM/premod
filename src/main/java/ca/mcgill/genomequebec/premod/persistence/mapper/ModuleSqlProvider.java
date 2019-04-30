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

package ca.mcgill.genomequebec.premod.persistence.mapper;

import ca.mcgill.genomequebec.premod.persistence.filter.ModuleDatabaseFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleDatabaseFilter.Table;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleGroupByDatabaseFilter;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleGroupByDatabaseFilter.Column;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

/**
 * Provides SQL code for module searches.
 */
public class ModuleSqlProvider {
  private static class Filters {
    private final Collection<ModuleDatabaseFilter> filters;
    private final Collection<ModuleGroupByDatabaseFilter> groupByFilters;

    private Filters(Collection<ModuleDatabaseFilter> filters,
        Collection<ModuleGroupByDatabaseFilter> groupByFilters) {
      this.filters = filters;
      this.groupByFilters = groupByFilters;
    }
  }

  private Filters getFilters(Map<String, Object> parameters) {
    Collection<ModuleDatabaseFilter> filters;
    if (parameters.get("filters") != null) {
      @SuppressWarnings("unchecked")
      Collection<ModuleDatabaseFilter> uncheckedFilters =
          (Collection<ModuleDatabaseFilter>) parameters.get("filters");
      filters = uncheckedFilters;
    } else {
      filters = new LinkedList<ModuleDatabaseFilter>();
    }
    Collection<ModuleGroupByDatabaseFilter> groupByFilters =
        new LinkedList<ModuleGroupByDatabaseFilter>();
    for (ModuleDatabaseFilter filter : filters) {
      if (filter instanceof ModuleGroupByDatabaseFilter) {
        groupByFilters.add((ModuleGroupByDatabaseFilter) filter);
      }
    }
    return new Filters(filters, groupByFilters);
  }

  /**
   * Returns SQL to use to insert modules in temporary table.
   * 
   * @param parameters
   *          parameters
   * @return SQL to use to insert modules in temporary table
   */
  public String insertByFilters(Map<String, Object> parameters) {
    final Filters filters = this.getFilters(parameters);
    for (ModuleDatabaseFilter filter : filters.filters) {
      filter.addParameters(parameters);
    }
    final Collection<Column> groupByColumns = new HashSet<Column>();
    for (ModuleGroupByDatabaseFilter filter : filters.groupByFilters) {
      if (filter.columns() != null) {
        groupByColumns.addAll(filter.columns());
      }
    }

    // Select part.
    final String selectSql = new SQL() {
      {
        StringBuilder selectColumnsBuilder = new StringBuilder();
        selectColumnsBuilder.append("module.id");
        SELECT_DISTINCT(selectColumnsBuilder.toString());
        FROM("module");
        Collection<Table> tables = new HashSet<Table>();
        for (ModuleDatabaseFilter filter : filters.filters) {
          if (filter.joins() != null) {
            for (Table table : filter.joins()) {
              tables.add(table);
              tables.addAll(table.requirements);
            }
          }
        }
        for (ModuleGroupByDatabaseFilter filter : filters.groupByFilters) {
          if (filter.columns() != null) {
            for (Column column : filter.columns()) {
              tables.addAll(column.requirements);
            }
          }
        }
        if (tables.contains(Table.module_to_unit)) {
          INNER_JOIN("module_to_unit ON module.id = module_to_unit.module_id");
        }
        if (tables.contains(Table.unit)) {
          INNER_JOIN("unit ON module_to_unit.unit_id = unit.id");
        }
        if (tables.contains(Table.nucleotide)) {
          StringBuilder join = new StringBuilder();
          join.append("nucleotide");
          join.append(" ON module.organism = nucleotide.organism");
          join.append(" AND module.chromosome = nucleotide.chromosome");
          join.append(" AND module.end + 10000 >= nucleotide.start");
          join.append(" AND module.start - 10000 <= nucleotide.end");
          INNER_JOIN(join.toString());
        }
        for (ModuleDatabaseFilter filter : filters.filters) {
          if (filter.where() != null) {
            WHERE(filter.where());
          }
        }
        if (!filters.groupByFilters.isEmpty()) {
          StringBuilder groupByBuilder = new StringBuilder();
          groupByBuilder.append("module.id");
          GROUP_BY(groupByBuilder.toString());
        }
        for (ModuleGroupByDatabaseFilter filter : filters.groupByFilters) {
          if (filter.having() != null) {
            HAVING(filter.having());
          }
        }
      }
    }.toString();

    // Insert part.
    return new SQL() {
      {
        INSERT_INTO("_modules (id) " + selectSql);
      }
    }.toString();
  }
}
