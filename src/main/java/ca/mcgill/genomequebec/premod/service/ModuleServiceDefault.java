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

import ca.mcgill.genomequebec.premod.Constants;
import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.persistence.Limit;
import ca.mcgill.genomequebec.premod.persistence.mapper.ModuleMapper;
import ca.mcgill.genomequebec.premod.persistence.mapper.ModuleMapper.ModuleWithUnit;
import ca.qc.ircm.bio.ChromosomeLocation;
import ca.qc.ircm.bio.SimpleChromosomeLocation;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of services for {@link Module}.
 */
@Service
@Transactional
public class ModuleServiceDefault implements ModuleService {
  private static final int TOTAL_NUMBER_OF_MODULE = 123510;
  private static final int MAX_MODULES_IN_SELECT = 10000;

  private static class SearchResultHandler implements ResultHandler<ModuleWithUnit> {
    private final ModuleForSearchHandler handler;
    private Module module;
    private List<Unit> bestUnits;

    private SearchResultHandler(ModuleForSearchHandler handler) {
      this.handler = handler;
    }

    @Override
    public void handleResult(ResultContext<? extends ModuleWithUnit> context) {
      ModuleMapper.ModuleWithUnit moduleWithUnit = context.getResultObject();
      if (module == null) {
        module = moduleWithUnit.getModule();
        bestUnits = new ArrayList<>(5);
        if (moduleWithUnit.getUnit() != null) {
          bestUnits.add(moduleWithUnit.getUnit());
        }
      } else if (!module.getId().equals(moduleWithUnit.getModule().getId())) {
        // Another module is being read.
        handler.handleResult(new ModuleWithBestUnits.Default(module, bestUnits));
        module = moduleWithUnit.getModule();
        bestUnits = new ArrayList<>(5);
        if (moduleWithUnit.getUnit() != null) {
          bestUnits.add(moduleWithUnit.getUnit());
        }
      } else {
        if (moduleWithUnit.getUnit() != null) {
          bestUnits.add(moduleWithUnit.getUnit());
        }
      }
    }

    protected void queryCompleted() {
      if (module != null) {
        handler.handleResult(new ModuleWithBestUnits.Default(module, bestUnits));
      }
    }
  }

  @Inject
  private ModuleMapper moduleMapper;
  private final Random random = new Random();

  @Override
  public Module get(String name, Organism organism) {
    return moduleMapper.selectByNameAndOrganism(name, organism);
  }

  @Override
  public boolean exists(String name, Organism organism) {
    return moduleMapper.existsByNameAndOrganism(name, organism);
  }

  @Override
  public Module random() {
    // Get a random number.
    int id = random.nextInt(TOTAL_NUMBER_OF_MODULE);

    return moduleMapper.selectModuleByIndex(Organism.HUMAN, Version.LATEST, id);
  }

  @Override
  public String sequence(Module module) {
    return moduleMapper.selectSequence(module);
  }

  @Override
  public List<Module> inWindow(Module module) {
    long windowSize = Constants.MODULE_GENE_WINDOW;
    long start = module.getStart() - windowSize / 2;
    long end = module.getEnd() + windowSize / 2;
    if (start < 0) {
      end -= start;
      start = 0;
    }
    ChromosomeLocation window = new SimpleChromosomeLocation(module.getChromosome(), start, end);
    return moduleMapper.selectInWindow(module.getOrganism(), module.getVersion(), window);
  }

  @Override
  public List<Module> inWindow(Nucleotide nucleotide, Version version) {
    long windowSize = Constants.MODULE_GENE_WINDOW;
    long start = nucleotide.getStart() - windowSize / 2;
    long end = nucleotide.getEnd() + windowSize / 2;
    if (start < 0) {
      end -= start;
      start = 0;
    }
    ChromosomeLocation window =
        new SimpleChromosomeLocation(nucleotide.getChromosome(), start, end);
    return moduleMapper.selectInWindow(nucleotide.getOrganism(), version, window);
  }

  @Override
  public SearchResults search(SearchParameters parameters) {
    moduleMapper.createTemporaryTable();
    try {
      moduleMapper.insertByFilters(parameters.filters());
      moduleMapper.createLimitTemporaryTable();
      try {
        moduleMapper.insertLimitTemporaryTable(parameters.sort(), parameters.descending(),
            parameters.limit());
        moduleMapper.updateLimitInTemporaryTable();
        List<ModuleMapper.ModuleWithBestUnits> databaseModules =
            moduleMapper.selectForSearch(parameters.sort(), parameters.descending());
        final List<ModuleWithBestUnits> modules = new ArrayList<>(databaseModules.size());
        for (ModuleMapper.ModuleWithBestUnits databaseModule : databaseModules) {
          modules.add(new ModuleWithBestUnits.Default(databaseModule.getModule(),
              databaseModule.getBestUnits()));
        }
        final int count = moduleMapper.countForSearch();
        return new SearchResults() {
          @Override
          public List<ModuleWithBestUnits> modules() {
            return modules;
          }

          @Override
          public int count() {
            return count;
          }
        };
      } finally {
        moduleMapper.dropLimitTemporaryTable();
      }
    } finally {
      moduleMapper.dropTemporaryTable();
    }
  }

  @Override
  public void searchLowMemory(final SearchParameters parameters,
      final ModuleForSearchHandler handler) {
    moduleMapper.createTemporaryTable();
    try {
      moduleMapper.insertByFilters(parameters.filters());
      moduleMapper.createLimitTemporaryTable();
      try {
        final int count = moduleMapper.countForSearch();
        handler.handleCount(count);

        SearchResultHandler selectHandler = new SearchResultHandler(handler);
        if (parameters.limit() != null) {
          moduleMapper.insertLimitTemporaryTable(parameters.sort(), parameters.descending(),
              parameters.limit());
          moduleMapper.updateLimitInTemporaryTable();
          moduleMapper.selectForSearchLowRam(parameters.sort(), parameters.descending(),
              selectHandler);
        } else {
          for (int i = 0; i < count; i += MAX_MODULES_IN_SELECT) {
            final int offset = i;
            moduleMapper.deleteLimitTemporaryTable();
            moduleMapper.resetLimitInTemporaryTable();
            moduleMapper.insertLimitTemporaryTable(parameters.sort(), parameters.descending(),
                new Limit() {
                  @Override
                  public Integer getOffset() {
                    return offset;
                  }

                  @Override
                  public Integer getRowCount() {
                    return MAX_MODULES_IN_SELECT;
                  }
                });
            moduleMapper.updateLimitInTemporaryTable();
            moduleMapper.selectForSearchLowRam(parameters.sort(), parameters.descending(),
                selectHandler);
          }
        }
        selectHandler.queryCompleted();
      } finally {
        moduleMapper.dropLimitTemporaryTable();
      }
    } finally {
      moduleMapper.dropTemporaryTable();
    }
  }
}
