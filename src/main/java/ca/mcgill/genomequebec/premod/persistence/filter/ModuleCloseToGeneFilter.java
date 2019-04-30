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
 * Only modules close to specified gene(s) passes filter.
 */
public class ModuleCloseToGeneFilter implements ModuleDatabaseFilter {

  private final Long upstream;
  private final Long downstream;

  public ModuleCloseToGeneFilter(Long upstream, Long downstream) {
    this.upstream = upstream;
    this.downstream = downstream;
  }

  @Override
  public void addParameters(Map<String, Object> parameters) {
    parameters.put("geneUpstream", upstream);
    parameters.put("geneDownstream", downstream);
  }

  @Override
  public Collection<Table> joins() {
    return Collections.nCopies(1, Table.module);
  }

  @Override
  public String where() {
    StringBuilder condition = new StringBuilder();
    condition.append("(");
    if (upstream != null) {
      condition.append("module.upstream_gene <= #{geneUpstream}");
    }
    if (upstream != null && downstream != null) {
      condition.append(" OR ");
    }
    if (downstream != null) {
      condition.append("module.downstream_gene >= (#{geneDownstream} * -1)");
    }
    condition.append(")");
    return condition.toString();
  }
}
