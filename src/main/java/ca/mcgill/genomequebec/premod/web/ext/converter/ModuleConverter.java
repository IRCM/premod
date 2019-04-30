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

import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.sourceforge.stripes.format.Formatter;
import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

/**
 * Converter/formatter for modules.
 */
public class ModuleConverter implements TypeConverter<Module>, Formatter<Module> {

  @Inject
  private ModuleService moduleService;
  private OrganismConverter organismConverter = new OrganismConverter();

  @Override
  public void setLocale(Locale locale) {
  }

  @Override
  public Module convert(String input, Class<? extends Module> targetType,
      Collection<ValidationError> errors) {
    Pattern pattern = Pattern.compile("(\\w+)\\-(\\w+)");
    Matcher matcher = pattern.matcher(input);
    if (matcher.matches()) {
      Organism organism = organismConverter.convert(matcher.group(1), Organism.class, errors);
      String name = matcher.group(2);
      Module module = moduleService.get(name, organism);
      if (module == null) {
        ScopedLocalizableError error = new ScopedLocalizableError("validation.exists", "notExists");
        errors.add(error);
        return null;
      } else {
        return module;
      }
    } else {
      ScopedLocalizableError error = new ScopedLocalizableError("validation.exists", "notExists");
      errors.add(error);
      return null;
    }
  }

  @Override
  public String format(Module input) {
    return input != null ? organismConverter.format(input.getOrganism()) + "-" + input.getName()
        : "";
  }

  @Override
  public void setFormatType(String formatType) {
  }

  @Override
  public void setFormatPattern(String formatPattern) {
  }

  @Override
  public void init() {
  }
}
