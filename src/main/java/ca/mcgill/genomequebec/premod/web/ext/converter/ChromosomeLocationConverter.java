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

import ca.qc.ircm.bio.ChromosomeLocation;
import ca.qc.ircm.bio.SimpleChromosomeLocation;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.stripes.format.Formatter;
import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convert/formatter for chromosome and location.
 */
public class ChromosomeLocationConverter
    implements TypeConverter<ChromosomeLocation>, Formatter<ChromosomeLocation> {

  /**
   * Pattern for chromosome location only.
   */
  public static final String CHROMOSOME_PATTERN =
      "(?:chr)?(\\w+)(\\s*[\\:\\s]\\s*(\\d+)\\s*[\\-\\s]\\s*(\\d+))?";

  private final Logger logger = LoggerFactory.getLogger(ChromosomeLocationConverter.class);

  @Override
  public void setLocale(Locale locale) {
  }

  // Converter.
  @Override
  public ChromosomeLocation convert(String input, Class<? extends ChromosomeLocation> targetType,
      Collection<ValidationError> errors) {
    if (input == null) {
      return null;
    }
    Pattern pattern = Pattern.compile(CHROMOSOME_PATTERN);
    Matcher matcher = pattern.matcher(input.trim());
    if (matcher.matches()) {
      SimpleChromosomeLocation location = new SimpleChromosomeLocation();
      location.setChromosome(matcher.group(1));
      location.setStart(matcher.group(3) != null ? new Long(matcher.group(3)) : null);
      location.setEnd(matcher.group(4) != null ? new Long(matcher.group(4)) : null);

      // Validate that end is after start.
      if (location.getStart() != null && location.getEnd() != null
          && location.getEnd().compareTo(location.getStart()) < 0) {
        logger.debug("Chromosome {} has end before start", location.getChromosome());
        ScopedLocalizableError error = new ScopedLocalizableError("validation.chromosomeLocation",
            "endBeforeStart", input, location.getStart(), location.getEnd());
        errors.add(error);
        return null;
      } else {
        return location;
      }
    } else {
      logger.debug("Chromosome does not match pattern {}", input);
      // Input does not match required pattern.
      ScopedLocalizableError error =
          new ScopedLocalizableError("validation.chromosomeLocation", "valueDoesNotMatch", input);
      errors.add(error);
      return null;
    }
  }

  // Formatter.
  @Override
  public String format(ChromosomeLocation chromosomeLocation) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("chr");
    buffer.append(chromosomeLocation.getChromosome());
    if (chromosomeLocation.getStart() != null && chromosomeLocation.getEnd() != null) {
      buffer.append(":");
      buffer.append(chromosomeLocation.getStart());
      buffer.append("-");
      buffer.append(chromosomeLocation.getEnd());
    }
    return buffer.toString();
  }

  @Override
  public void init() {
  }

  @Override
  public void setFormatPattern(String pattern) {
  }

  @Override
  public void setFormatType(String type) {
  }
}
