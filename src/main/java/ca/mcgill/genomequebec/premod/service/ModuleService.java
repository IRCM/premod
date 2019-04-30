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
import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.persistence.Limit;
import ca.mcgill.genomequebec.premod.persistence.filter.ModuleDatabaseFilter;
import java.util.Collection;
import java.util.List;

/**
 * Services for {@link Module}.
 */
public interface ModuleService {
  /**
   * Sort orders for modules.
   */
  public static enum Sort {
    /**
     * Sort modules by name.
     */
    NAME, /**
           * Sort modules by position on chromosome.
           */
    POSITION, /**
               * Sort modules by score.
               */
    SCORE;
  }

  /**
   * A module with it's best units.
   */
  public static interface ModuleWithBestUnits {
    public static class Default implements ModuleWithBestUnits {
      private final Module module;
      private final List<Unit> bestUnits;

      protected Default(Module module, List<Unit> bestUnits) {
        this.module = module;
        this.bestUnits = bestUnits;
      }

      @Override
      public Module getModule() {
        return module;
      }

      @Override
      public List<Unit> getBestUnits() {
        return bestUnits;
      }
    }

    public Module getModule();

    public List<Unit> getBestUnits();
  }

  /**
   * Handles modules found in search.
   */
  public static interface ModuleForSearchHandler {
    public void handleCount(int count);

    public void handleResult(ModuleWithBestUnits module);
  }

  /**
   * Parameters for module search.
   */
  public static interface SearchParameters {
    public Collection<ModuleDatabaseFilter> filters();

    public Sort sort();

    public boolean descending();

    public Limit limit();
  }

  /**
   * Search results.
   */
  public static interface SearchResults {
    public List<ModuleWithBestUnits> modules();

    public int count();
  }

  /**
   * Selects module based on it's name and organism.
   * 
   * @param name
   *          module's name
   * @param organism
   *          module's organism
   * @return module
   */
  public Module get(String name, Organism organism);

  /**
   * Returns true if module with exists.
   * 
   * @param name
   *          module's name
   * @param organism
   *          module's organism
   * @return true if module with exists
   */
  public boolean exists(String name, Organism organism);

  /**
   * Selects a random module.
   * 
   * @return random module
   */
  public Module random();

  /**
   * Selects module's sequence.
   * 
   * @param module
   *          module
   * @return module's sequence
   */
  public String sequence(Module module);

  /**
   * Returns all modules which are located near specified module.
   * 
   * @param module
   *          module
   * @return all modules which are located near specified module
   */
  public List<Module> inWindow(Module module);

  /**
   * Returns all modules which are located near specified nucleotide.
   * 
   * @param nucleotide
   *          nucleotide
   * @param version
   *          version of modules to search for
   * @return all modules which are located near specified nucleotide
   */
  public List<Module> inWindow(Nucleotide nucleotide, Version version);

  /**
   * Selects all modules that pass filters.
   * 
   * @param parameters
   *          search parameters
   * @return all modules that pass filters
   */
  public SearchResults search(SearchParameters parameters);

  /**
   * Selects all modules that pass filters - uses few memory.
   * 
   * @param parameters
   *          search parameters
   * @param handler
   *          handles modules found during search
   */
  public void searchLowMemory(SearchParameters parameters, ModuleForSearchHandler handler);
}
