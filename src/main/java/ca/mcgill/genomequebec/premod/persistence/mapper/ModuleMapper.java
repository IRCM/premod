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
import ca.mcgill.genomequebec.premod.persistence.Limit;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleDatabaseFilter;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import ca.qc.ircm.bio.ChromosomeLocation;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Options.FlushCachePolicy;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;

/**
 * Mapper for {@link Module}.
 */
@Mapper
public interface ModuleMapper {
  public static class ModuleWithBestUnits {
    private Long id;
    private Module module;
    private List<Unit> bestUnits;

    public final Long getId() {
      return id;
    }

    public final void setId(Long id) {
      this.id = id;
    }

    public final Module getModule() {
      return module;
    }

    public final void setModule(Module module) {
      this.module = module;
    }

    public final List<Unit> getBestUnits() {
      return bestUnits;
    }

    public final void setBestUnits(List<Unit> bestUnits) {
      this.bestUnits = bestUnits;
    }
  }

  public static class ModuleWithUnit {
    private Long id;
    private Module module;
    private Unit unit;

    public final Long getId() {
      return id;
    }

    public final void setId(Long id) {
      this.id = id;
    }

    public final Module getModule() {
      return module;
    }

    public final void setModule(Module module) {
      this.module = module;
    }

    public final Unit getUnit() {
      return unit;
    }

    public final void setUnit(Unit unit) {
      this.unit = unit;
    }
  }

  public static class ModuleWithSequence extends Module {
    private static final long serialVersionUID = -7602615640600892915L;
    private String sequence;

    public String getSequence() {
      return sequence;
    }

    public void setSequence(String sequence) {
      this.sequence = sequence;
    }

    @Override
    public boolean equals(Object other) {
      return super.equals(other);
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }
  }

  /**
   * Selects module based on it's database identifier.
   *
   * @param id
   *          module's database identifier
   * @return module
   */
  public Module selectModuleById(Long id);

  /**
   * Selects module's sequence.
   *
   * @param module
   *          module
   * @return module's sequence
   */
  public String selectSequence(Module module);

  /**
   * Selects module based on it's name and organism.
   *
   * @param organism
   *          module's organism
   * @param name
   *          module's name
   * @return module
   */
  public Module selectByNameAndOrganism(@Param("name") String name,
      @Param("organism") Organism organism);

  /**
   * Returns true if module with name exists.
   *
   * @param name
   *          module's name
   * @param organism
   *          module's organism
   * @return true if module with name exists
   */
  public boolean existsByNameAndOrganism(@Param("name") String name,
      @Param("organism") Organism organism);

  /**
   * Selects module based on it's name and organism.
   *
   * @param organism
   *          modules' organism
   * @param version
   *          modules' version
   * @param index
   *          index
   * @return module
   */
  public Module selectModuleByIndex(@Param("organism") Organism organism,
      @Param("version") Version version, @Param("index") Integer index);

  /**
   * Returns all modules which are located in the specified window.
   *
   * @param organism
   *          modules' organism
   * @param version
   *          modules' version
   * @param chromosomeLocation
   *          window in which to search for modules
   * @return all modules which are located in the specified window
   */
  public List<Module> selectInWindow(@Param("organism") Organism organism,
      @Param("version") Version version,
      @Param("chromosomeLocation") ChromosomeLocation chromosomeLocation);

  /**
   * Creates a temporary table where to store module identifiers.
   */
  public void createTemporaryTable();

  /**
   * Drops temporary table used to store module identifiers.
   */
  public void dropTemporaryTable();

  /**
   * Creates a temporary table where modules to return can be limited.
   */
  public void createLimitTemporaryTable();

  /**
   * Drops temporary table used to limit modules.
   */
  public void dropLimitTemporaryTable();

  /**
   * Inserts module identifiers into temporary table.
   *
   * @param filters
   *          filters module to search for
   */
  @InsertProvider(type = ModuleSqlProvider.class, method = "insertByFilters")
  @Options(flushCache = FlushCachePolicy.FALSE)
  public void insertByFilters(@Param("filters") Collection<ModuleDatabaseFilter> filters);

  /**
   * Inserts module identifiers that are in limits into limit temporary table.
   *
   * @param sort
   *          how to sort modules
   * @param descending
   *          true if module order is to be reversed
   * @param limit
   *          limits number of modules to select
   */
  public void insertLimitTemporaryTable(@Param("sort") ModuleService.Sort sort,
      @Param("descending") boolean descending, @Param("limit") Limit limit);

  /**
   * Sets flag in temporary table indicating if modules are inside limits.
   */
  public void updateLimitInTemporaryTable();

  /**
   * Resets flag in temporary table indicating if modules are inside limits.
   */
  public void resetLimitInTemporaryTable();

  /**
   * Deleted content of temporary table where modules to return can be limited.
   */
  public void deleteLimitTemporaryTable();

  /**
   * Selects number of modules that pass filters.
   *
   * @return number of modules that pass filters
   */
  public int countForSearch();

  /**
   * Selects all modules (inside limits) in temporary tables for search page.
   *
   * @param sort
   *          how to sort modules
   * @param descending
   *          true if module order is to be reversed
   * @return all modules (inside limits) in temporary tables for search page
   */
  public List<ModuleWithBestUnits> selectForSearch(@Param("sort") ModuleService.Sort sort,
      @Param("descending") boolean descending);

  /**
   * Selects all modules (inside limits) in temporary tables for search page.
   *
   * @param sort
   *          how to sort modules
   * @param descending
   *          true if module order is to be reversed
   * @param resultHandler
   *          handles modules
   */
  public void selectForSearchLowRam(@Param("sort") ModuleService.Sort sort,
      @Param("descending") boolean descending, ResultHandler<ModuleWithUnit> resultHandler);

  public List<Module> selectAll();

  public void selectAll(ResultHandler<Module> resultHandler);

  public void selectAllWithSequence(ResultHandler<ModuleWithSequence> resultHandler);
}
