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

import ca.mcgill.genomequebec.premod.business.Version;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Only modules having specified version passes filter.
 */
public class ModuleVersionFilter implements ModuleDatabaseFilter {

  private final Version version;

  public ModuleVersionFilter(Version version) {
    this.version = version;
  }

  @Override
  public void addParameters(Map<String, Object> parameters) {
    parameters.put("version", version);
  }

  @Override
  public Collection<Table> joins() {
    return Collections.nCopies(1, Table.module);
  }

  @Override
  public String where() {
    return "module.version_id = #{version}";
  }
}
