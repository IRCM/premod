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

package ca.mcgill.genomequebec.premod.web.ext.converter;

import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.service.UnitService;
import java.util.Collection;
import java.util.Locale;
import javax.inject.Inject;
import net.sourceforge.stripes.format.Formatter;
import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

/**
 * Convert unit id into unit.
 */
public class UnitConverter implements TypeConverter<Unit>, Formatter<Unit> {

  @Inject
  private UnitService unitService;

  @Override
  public void setLocale(Locale locale) {
  }

  @Override
  public Unit convert(String input, Class<? extends Unit> targetType,
      Collection<ValidationError> errors) {
    Unit unit = unitService.get(input);
    if (unit == null) {
      ScopedLocalizableError error = new ScopedLocalizableError("validation.exists", "notExists");
      errors.add(error);
    }
    return unit;
  }

  @Override
  public String format(Unit input) {
    return input.getId();
  }

  @Override
  public void init() {
  }

  @Override
  public void setFormatPattern(String formatPattern) {
  }

  @Override
  public void setFormatType(String formatType) {
  }
}
