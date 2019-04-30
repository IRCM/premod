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

package ca.mcgill.genomequebec.premod.service;

import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.UnitOccurrence;
import ca.mcgill.genomequebec.premod.persistence.mapper.UnitOccurrenceMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of services for {@link UnitOccurrence}.
 */
@Service
@Transactional
public class UnitOccurrenceServiceDefault implements UnitOccurrenceService {
  @Inject
  private UnitOccurrenceMapper unitOccurrenceMapper;

  @Override
  public List<UnitOccurrence> all(Module module, Unit unit) {
    return unitOccurrenceMapper.selectByModuleAndUnit(module, unit);
  }

  @Override
  public Map<Unit, List<UnitOccurrence>> all(Module module) {
    class UnitOccurrenceHandler implements ResultHandler<UnitOccurrence> {
      Map<Unit, List<UnitOccurrence>> unitOccurrences = new HashMap<>();

      @Override
      public void handleResult(ResultContext<? extends UnitOccurrence> context) {
        UnitOccurrence unitOccurrence = context.getResultObject();
        Unit unit = unitOccurrence.getUnit();
        if (!unitOccurrences.containsKey(unit)) {
          unitOccurrences.put(unit, new ArrayList<UnitOccurrence>());
        }
        unitOccurrences.get(unit).add(unitOccurrence);
      }
    }

    UnitOccurrenceHandler handler = new UnitOccurrenceHandler();
    unitOccurrenceMapper.selectByModule(module, handler);
    return handler.unitOccurrences;
  }
}
