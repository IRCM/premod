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

package ca.mcgill.genomequebec.premod.web.image;

import static org.mockito.Mockito.when;

import ca.mcgill.genomequebec.premod.MainConfiguration;
import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import ca.mcgill.genomequebec.premod.service.NucleotideService;
import ca.mcgill.genomequebec.premod.test.config.ServiceTestAnnotations;
import ca.mcgill.genomequebec.premod.test.config.web.MockServletContextRule;
import ca.mcgill.genomequebec.premod.web.WebContext;
import java.io.OutputStream;
import java.util.Locale;
import javax.inject.Inject;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests {@link GeneContextImage}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ServiceTestAnnotations
public class GeneContextImageTest {
  @ClassRule
  public static MockServletContextRule mockServletContextRule = new MockServletContextRule();
  @Inject
  private ModuleService moduleService;
  @Inject
  private NucleotideService nucleotideService;
  @Inject
  private MainConfiguration mainConfiguration;
  @Mock
  private WebContext webContext;
  @Mock
  private OutputStream output;

  @Test
  public void noModule() throws Throwable {
    when(webContext.getLocale()).thenReturn(Locale.CANADA);
    when(webContext.getContextPath()).thenReturn("premod");
    Nucleotide nucleotide = nucleotideService.longest("TREM1", Organism.HUMAN);
    GeneContextImage image = new GeneContextImage(moduleService, nucleotideService,
        mainConfiguration, nucleotide, Version.LATEST, webContext);
    image.printPlotToOutputStream(output, "png");
    image.getUrlLocations();
  }
}
