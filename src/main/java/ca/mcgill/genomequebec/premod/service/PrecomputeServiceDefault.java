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
import ca.mcgill.genomequebec.premod.persistence.mapper.ModuleMapper;
import ca.mcgill.genomequebec.premod.persistence.mapper.NucleotideMapper;
import java.util.List;
import javax.inject.Inject;

/**
 * MyBatis implementation of {@link PrecomputeService}.
 */
public class PrecomputeServiceDefault implements PrecomputeService {
  private final ModuleMapper moduleMapper;
  private final NucleotideMapper nucleotideMapper;

  @Inject
  protected PrecomputeServiceDefault(ModuleMapper moduleMapper, NucleotideMapper nucleotideMapper) {
    this.moduleMapper = moduleMapper;
    this.nucleotideMapper = nucleotideMapper;
  }

  @Override
  public List<Module> modules() {
    return moduleMapper.selectAll();
  }

  @Override
  public Nucleotide closestUpstreamGene(Module module) {
    Nucleotide minus = nucleotideMapper.selectUpstreamGeneMinusByModule(module);
    Nucleotide plus = nucleotideMapper.selectUpstreamGenePlusByModule(module);
    if (minus != null && plus != null) {
      long plusDiff = Math.abs(plus.getRelativePosition(module.getMiddlePosition()));
      long minusDiff = Math.abs(minus.getRelativePosition(module.getMiddlePosition()));
      if (plusDiff < minusDiff) {
        return plus;
      } else {
        return minus;
      }
    } else if (minus != null) {
      return minus;
    } else if (plus != null) {
      return plus;
    } else {
      return null;
    }
  }

  @Override
  public Nucleotide closestDownstreamGene(Module module) {
    Nucleotide minus = nucleotideMapper.selectDownstreamGeneMinusByModule(module);
    Nucleotide plus = nucleotideMapper.selectDownstreamGenePlusByModule(module);
    if (minus != null && plus != null) {
      long plusDiff = Math.abs(plus.getRelativePosition(module.getMiddlePosition()));
      long minusDiff = Math.abs(minus.getRelativePosition(module.getMiddlePosition()));
      if (plusDiff < minusDiff) {
        return plus;
      } else {
        return minus;
      }
    } else if (minus != null) {
      return minus;
    } else if (plus != null) {
      return plus;
    } else {
      return null;
    }
  }
}
