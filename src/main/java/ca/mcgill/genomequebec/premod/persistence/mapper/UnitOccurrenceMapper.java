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

import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.UnitOccurrence;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

/**
 * Mapper for {@link UnitOccurrence}.
 */
@Mapper
public interface UnitOccurrenceMapper {
  /**
   * Selects all occurrences of unit in module.
   *
   * @param module
   *          module
   * @param unit
   *          unit
   * @return all occurrences of unit in module
   */
  public List<UnitOccurrence> selectByModuleAndUnit(@Param("module") Module module,
      @Param("unit") Unit unit);

  /**
   * Selects all occurrences of all units in module.
   *
   * @param module
   *          module
   * @return all occurrences of all units in module
   */
  public List<UnitOccurrence> selectByModule(Module module);

  /**
   * Selects all occurrences of all units in module.
   *
   * @param module
   *          module
   * @param handler
   *          handles unit occurrences
   */
  public void selectByModule(Module module, ResultHandler<UnitOccurrence> handler);

  public void selectAll(ResultHandler<UnitOccurrence> resultHandler);
}
