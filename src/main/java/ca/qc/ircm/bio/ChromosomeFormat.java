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

package ca.qc.ircm.bio;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Formats and parses chromosome the UCSC standards.
 */
public class ChromosomeFormat {
  /**
   * UCSC pattern for chromosome only.
   */
  public static final String UCSC_PATTERN = "chr(\\w+)";
  /**
   * UCSC pattern for chromosome and location.
   */
  public static final String UCSC_PATTERN_LOCATION =
      "chr(\\w+)\\s*[\\:\\s]\\s*(\\d+)\\s*[\\-\\s]\\s*(\\d+)";

  /**
   * Formats chromosome and location in UCSC format.
   * <p>
   * Location is added only if chromosome is an instance of {@link ChromosomeLocation}.
   * </p>
   *
   * @param chromosome
   *          chromosome
   * @return chromosome and location in UCSC format
   */
  public String format(Chromosome chromosome) {
    StringBuilder builder = new StringBuilder();
    builder.append("chr");
    builder.append(chromosome.getChromosome());
    if (chromosome instanceof ChromosomeLocation) {
      ChromosomeLocation chromosomeLocation = (ChromosomeLocation) chromosome;
      if (chromosomeLocation.getStart() != null && chromosomeLocation.getEnd() != null) {
        builder.append(":");
        builder.append(chromosomeLocation.getStart());
        builder.append("-");
        builder.append(chromosomeLocation.getEnd());
      } else if (chromosomeLocation.getStart() != null && chromosomeLocation.getEnd() == null) {
        throw new IllegalArgumentException("end cannot be null unless start is null");
      } else if (chromosomeLocation.getStart() == null && chromosomeLocation.getEnd() != null) {
        throw new IllegalArgumentException("start cannot be null unless end is null");
      }
    }
    return builder.toString();
  }

  /**
   * Parse chromosome based on UCSC format.
   *
   * @param chromosome
   *          chromosome to parse
   * @return parsed chromosome and it's location, if supplied
   * @throws ParseException
   *           chromosome does not match {@link #UCSC_PATTERN} or {@link #UCSC_PATTERN_LOCATION}
   *           pattern
   */
  public ChromosomeLocation parse(String chromosome) throws ParseException {
    if (chromosome == null) {
      // Nothing to parser.
      throw new NullPointerException("Cannot parse null chromosome.");
    }

    SimpleChromosomeLocation parsed = new SimpleChromosomeLocation();

    // Try to match with location.
    Pattern patternWithLocation = Pattern.compile(UCSC_PATTERN_LOCATION);
    Matcher matcherWithLocation = patternWithLocation.matcher(chromosome);
    if (matcherWithLocation.matches()) {
      parsed.setChromosome(matcherWithLocation.group(1));
      parsed.setStart(new Long(matcherWithLocation.group(2)));
      parsed.setEnd(new Long(matcherWithLocation.group(3)));
    } else {
      // Try to match without location.
      Pattern patternWithoutLocation = Pattern.compile(UCSC_PATTERN);
      Matcher matcherWithoutLocation = patternWithoutLocation.matcher(chromosome);
      if (matcherWithoutLocation.matches()) {
        parsed.setChromosome(matcherWithoutLocation.group(1));
      } else {
        throw new ParseException("Chromosome does not match " + UCSC_PATTERN + " or "
            + UCSC_PATTERN_LOCATION + "pattern", 0);
      }
    }
    return parsed;
  }
}
