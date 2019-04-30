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
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.service.UnitService;
import ca.mcgill.genomequebec.premod.service.UnitService.UnitWithExtra;
import java.io.Serializable;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

/**
 * Mapper for {@link Unit}.
 */
@Mapper
public interface UnitMapper {
  public static class UnitLinkedToModule implements Serializable {
    private static final long serialVersionUID = -1237606346310011012L;
    private String id;
    private Unit unit;
    private UnitService.Extra extra;

    public final String getId() {
      return id;
    }

    public final void setId(String id) {
      this.id = id;
    }

    public final Unit getUnit() {
      return unit;
    }

    public final void setUnit(Unit unit) {
      this.unit = unit;
    }

    public final UnitService.Extra getExtra() {
      return extra;
    }

    public final void setExtra(UnitService.Extra extra) {
      this.extra = extra;
    }
  }

  public static class UnitForExport extends UnitWithExtra.Default {
    private static final long serialVersionUID = 1853771848662022512L;
    private Organism organism;
    private Version version;

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
  }

  /**
   * Returns a Unit read from database.
   *
   * @param id
   *          The id of the Unit.
   * @return The Unit corresponding to this id.
   */
  public Unit getUnitById(String id);

  /**
   * Returns true if a unit id exists.
   *
   * @param id
   *          unit's id
   * @return true if a unit id exists
   */
  public boolean existsById(String id);

  /**
   * Selects best units for module.
   *
   * @param module
   *          module
   * @return best units for module
   */
  public List<Unit> selectBestByModule(Module module);

  /**
   * Selects non-best units for module.
   *
   * @param module
   *          module
   * @return non-best units for module
   */
  public List<Unit> selectNonBestByModule(Module module);

  /**
   * Selects best units for module.
   *
   * @param module
   *          module
   * @return best units for module
   */
  public List<UnitLinkedToModule> selectWithExtrasByModule(Module module);

  /**
   * Selects all units in temporary tables for statistics page.
   *
   * @param organism
   *          organism
   * @param version
   *          modules' version
   * @return all units in temporary tables for statistics page
   */
  public List<UnitWithExtra> selectForStatistics(@Param("organism") Organism organism,
      @Param("version") Version version);

  public void selectAllForStatistics(ResultHandler<UnitForExport> resultHandler);
}
