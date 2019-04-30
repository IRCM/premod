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
 * Test for chromosome location comparator.
 */
public class ChromosomeLocationComparatorTest {

  private ChromosomeLocationComparator chromosomeLocationComparator =
      new ChromosomeLocationComparator();

  @Test
  public void testCompareChromosomeAsNumber() {
    ChromosomeLocation chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    ChromosomeLocation chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) == 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "11";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "2";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "2";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "11";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;

    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chr1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chr1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) == 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chr11";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chr2";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chr2";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chr11";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;
  }

  @Test
  public void testCompareChromosomeAsText() {
    ChromosomeLocation chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "A";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    ChromosomeLocation chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "A";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) == 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "B";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "AA";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "AA";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "B";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;

    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chrA";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chrA";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) == 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chrB";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chrAA";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chrAA";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chrB";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;
  }

  @Test
  public void testCompareChromosomeAsNumberAndTextMix() {
    ChromosomeLocation chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    ChromosomeLocation chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "A";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "A";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "456789";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "A";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "A";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "456789";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;

    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chr1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chrA";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chrA";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chr1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chr456789";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chrA";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chrA";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "chr456789";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 2L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;
  }

  @Test
  public void testCompareStart() {
    ChromosomeLocation chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    ChromosomeLocation chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) == 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 2L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 2L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 2L;
      }

      @Override
      public Long getEnd() {
        return 10L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 2L;
      }

      @Override
      public Long getEnd() {
        return 10L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;
  }

  @Test
  public void testCompareEnd() {
    ChromosomeLocation chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    ChromosomeLocation chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) == 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 200L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) < 0;
    chromosome1 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 200L;
      }
    };
    chromosome2 = new ChromosomeLocation() {
      @Override
      public String getChromosome() {
        return "1";
      }

      @Override
      public Long getStart() {
        return 1L;
      }

      @Override
      public Long getEnd() {
        return 100L;
      }
    };
    assert chromosomeLocationComparator.compare(chromosome1, chromosome2) > 0;
  }
}
