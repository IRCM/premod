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

import org.junit.Test;

/**
 * Test for chromosome comparator.
 */
public class ChromosomeComparatorTest {

  private ChromosomeComparator chromosomeComparator = new ChromosomeComparator();

  @Test
  public void testCompareChromosomeAsNumber() {
    Chromosome chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "1";
      }
    };
    Chromosome chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "1";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) == 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "11";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "2";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "2";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "11";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) < 0;

    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chr1";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chr1";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) == 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chr11";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chr2";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chr2";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chr11";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) < 0;
  }

  @Test
  public void testCompareChromosomeAsText() {
    Chromosome chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "A";
      }
    };
    Chromosome chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "A";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) == 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "B";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "AA";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "AA";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "B";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) < 0;

    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chrA";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chrA";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) == 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chrB";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chrAA";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chrAA";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chrB";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) < 0;
  }

  @Test
  public void testCompareChromosomeAsNumberAndTextMix() {
    Chromosome chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "1";
      }
    };
    Chromosome chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "A";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) < 0 : "Expected negative, got "
        + chromosomeComparator.compare(chromosome1, chromosome2);
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "A";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "1";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "456789";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "A";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) < 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "A";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "456789";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) > 0;

    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chr1";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chrA";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) < 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chrA";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chr1";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chr456789";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chrA";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) < 0;
    chromosome1 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chrA";
      }
    };
    chromosome2 = new Chromosome() {
      @Override
      public String getChromosome() {
        return "chr456789";
      }
    };
    assert chromosomeComparator.compare(chromosome1, chromosome2) > 0;
  }
}
