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
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.Version;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Services for {@link Unit}.
 */
public interface UnitService {
  /**
   * Unit's additional information when linked to a module.
   */
  public static class Extra implements Serializable {
    private static final long serialVersionUID = -7482419605705941789L;
    private Integer tagNumber;
    private Integer minScore;
    private Integer nbOccurrence;
    private Integer tag;
    private Integer noTag;
    private Integer any;

    public Integer getTagNumber() {
      return tagNumber;
    }

    public void setTagNumber(Integer tagNumber) {
      this.tagNumber = tagNumber;
    }

    public Integer getMinScore() {
      return minScore;
    }

    public void setMinScore(Integer minScore) {
      this.minScore = minScore;
    }

    public Integer getNbOccurrence() {
      return nbOccurrence;
    }

    public void setNbOccurrence(Integer nbOccurrence) {
      this.nbOccurrence = nbOccurrence;
    }

    public Integer getTag() {
      return tag;
    }

    public void setTag(Integer tag) {
      this.tag = tag;
    }

    public Integer getNoTag() {
      return noTag;
    }

    public void setNoTag(Integer noTag) {
      this.noTag = noTag;
    }

    public Integer getAny() {
      return any;
    }

    public void setAny(Integer any) {
      this.any = any;
    }
  }

  /**
   * Units with extra information.
   */
  public static interface UnitWithExtra {
    public static class Default implements UnitWithExtra, Serializable {
      private static final long serialVersionUID = 8160538140690571022L;
      private Unit unit;
      private Extra extra;

      protected Default() {
      }

      protected Default(Unit unit, Extra extra) {
        this.unit = unit;
        this.extra = extra;
      }

      @Override
      public Unit getUnit() {
        return unit;
      }

      @Override
      public Extra getExtra() {
        return extra;
      }
    }

    public Unit getUnit();

    public Extra getExtra();
  }

  /**
   * Selects unit based on it's database identifier.
   * 
   * @param id
   *          unit's database identifier
   * @return unit
   */
  public Unit get(String id);

  /**
   * Returns true if a unit id exists.
   * 
   * @param id
   *          unit's id
   * @return true if a unit id exists
   */
  public boolean exists(String id);

  /**
   * Selects best units for module.
   * 
   * @param module
   *          module
   * @return best units for module
   */
  public List<Unit> best(Module module);

  /**
   * Selects non-best units for module.
   * 
   * @param module
   *          module
   * @return non-best units for module
   */
  public List<Unit> nonBest(Module module);

  /**
   * Selects units for module with extra information.
   * 
   * @param module
   *          module
   * @return units for module with extra information
   */
  public Map<Unit, Extra> extras(Module module);

  /**
   * Selects all units for statistics page.
   *
   * @param organism
   *          organism
   * @param version
   *          modules' version
   * @return all units for statistics page
   */
  public List<UnitWithExtra> statistics(Organism organism, Version version);
}
