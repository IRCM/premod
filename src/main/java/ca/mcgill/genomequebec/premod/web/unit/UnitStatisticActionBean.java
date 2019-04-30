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

package ca.mcgill.genomequebec.premod.web.unit;

import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.service.UnitService;
import ca.mcgill.genomequebec.premod.service.UnitService.UnitWithExtra;
import ca.mcgill.genomequebec.premod.service.parser.UnitCsvWriter;
import ca.mcgill.genomequebec.premod.web.ext.BaseActionBean;
import ca.mcgill.genomequebec.premod.web.ext.resolution.ForwardToJspResolution;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.StrictBinding;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Show all unit and their frequencies.
 */
@UrlBinding("/unit/statistic/{organism}/{version}")
@StrictBinding
public class UnitStatisticActionBean extends BaseActionBean {
  /**
   * How to sort units.
   */
  public static enum Sort {
    ID, NAME, TAG, OTHER, TOTAL
  }

  private static final String MESSAGE_BASE = UnitStatisticActionBean.class.getName() + ".";

  private final Logger logger = LoggerFactory.getLogger(UnitStatisticActionBean.class);
  /**
   * Organism of modules to show.
   */
  @Validate
  public Organism organism;
  /**
   * Version of modules to show.
   */
  @Validate
  public Version version;
  /**
   * How to sort units.
   */
  @Validate
  public Sort sort;
  /**
   * True if units are to be reverted after sort.
   */
  @Validate
  public boolean descending;
  /**
   * Units with extra information.
   */
  private List<UnitWithExtra> unitsWithExtra;
  @Inject
  private UnitService unitService;

  /**
   * Populate default values.
   */
  @Before(stages = LifecycleStage.BindingAndValidation)
  public void populateDefault() {
    logger.trace("Populate default values");
    organism = Organism.HUMAN;
    version = Version.LATEST;
    sort = Sort.ID;
  }

  /**
   * Show all unit frequencies.
   *
   * @return unit statistic page
   */
  @DefaultHandler
  public Resolution show() {
    logger.debug("Show all unit frequencies for {} version {}", organism, version);

    if (organism != null && version != null) {
      unitsWithExtra = unitService.statistics(organism, version);
      if (sort == null) {
        sort = Sort.ID;
      }
      Collections.sort(unitsWithExtra, new Comparator<UnitWithExtra>() {
        @Override
        public int compare(UnitWithExtra o1, UnitWithExtra o2) {
          switch (sort) {
            case ID:
              return o1.getUnit().getId().compareTo(o2.getUnit().getId());
            case NAME:
              return o1.getUnit().getName().compareTo(o2.getUnit().getName());
            case TAG:
              return o2.getExtra().getTag().compareTo(o1.getExtra().getTag());
            case OTHER:
              return o2.getExtra().getNoTag().compareTo(o1.getExtra().getNoTag());
            case TOTAL:
              return o2.getExtra().getAny().compareTo(o1.getExtra().getAny());
            default:
              throw new AssertionError("sort " + sort + " not covered in switch");
          }
        }
      });
      if (descending) {
        Collections.reverse(unitsWithExtra);
      }
    }

    return new ForwardToJspResolution(this.getClass());
  }

  /**
   * Create an Excel file containing all unit frequencies.
   *
   * @return Excel file containing all unit frequencies
   */
  public Resolution excel() {
    logger.debug("Create an Excel file containing all unit frequencies for {} version {}", organism,
        version);

    if (organism != null && version != null) {
      unitsWithExtra = unitService.statistics(organism, version);
      if (sort == null) {
        sort = Sort.ID;
      }
      Collections.sort(unitsWithExtra, new Comparator<UnitWithExtra>() {
        @Override
        public int compare(UnitWithExtra o1, UnitWithExtra o2) {
          switch (sort) {
            case ID:
              return o1.getUnit().getId().compareTo(o2.getUnit().getId());
            case NAME:
              return o1.getUnit().getName().compareTo(o2.getUnit().getName());
            case TAG:
              return o2.getExtra().getTag().compareTo(o1.getExtra().getTag());
            case OTHER:
              return o2.getExtra().getNoTag().compareTo(o1.getExtra().getNoTag());
            case TOTAL:
              return o2.getExtra().getAny().compareTo(o1.getExtra().getAny());
            default:
              throw new AssertionError("sort " + sort + " not covered in switch");
          }
        }
      });
      if (descending) {
        Collections.reverse(unitsWithExtra);
      }
    }

    String filename =
        new LocalizableMessage(MESSAGE_BASE + "excel.filename").getMessage(context.getLocale());
    StreamingResolution resolution = new StreamingResolution("application/csv") {
      @Override
      protected void stream(HttpServletResponse response) throws Exception {
        UnitCsvWriter writer = new UnitCsvWriter(
            new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8")),
            context.getLocale());
        try {
          writer.writeHeader();
          for (UnitWithExtra unitWithExtra : unitsWithExtra) {
            writer.writeUnit(unitWithExtra);
          }
        } finally {
          writer.close();
        }
      }
    };
    resolution.setCharacterEncoding("UTF-8");
    resolution.setFilename(filename);
    return resolution;
  }

  public Organism getOrganism() {
    return organism;
  }

  public void setOrganism(Organism organism) {
    this.organism = organism;
  }

  public Version getVersion() {
    return version;
  }

  public void setVersion(Version version) {
    this.version = version;
  }

  public Sort getSort() {
    return sort;
  }

  public void setSort(Sort sort) {
    this.sort = sort;
  }

  public boolean isDescending() {
    return descending;
  }

  public void setDescending(boolean descending) {
    this.descending = descending;
  }

  public List<UnitWithExtra> getUnitsWithExtra() {
    return unitsWithExtra;
  }
}
