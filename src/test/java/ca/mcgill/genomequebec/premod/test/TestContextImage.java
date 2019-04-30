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

package ca.mcgill.genomequebec.premod.test;

import ca.mcgill.genomequebec.premod.Main;
import ca.mcgill.genomequebec.premod.MainConfiguration;
import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.service.ModuleService;
import ca.mcgill.genomequebec.premod.service.NucleotideService;
import ca.mcgill.genomequebec.premod.web.WebContext;
import ca.mcgill.genomequebec.premod.web.image.ModuleContextImage;
import java.io.File;
import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Tests for module context image generation.
 */
public class TestContextImage {
  /**
   * Creates a module context image on disk.
   *
   * @param args
   *          not used
   */
  public static void main(String[] args) throws Throwable {
    try (ConfigurableApplicationContext context = SpringApplication.run(Main.class, args)) {
      ModuleService moduleService = context.getBean(ModuleService.class);
      Module module = moduleService.get("mod022434", Organism.HUMAN);
      NucleotideService nucleotideService = context.getBean(NucleotideService.class);
      MainConfiguration mainConfiguration = context.getBean(MainConfiguration.class);
      ModuleContextImage moduleContextImage = new ModuleContextImage(moduleService,
          nucleotideService, mainConfiguration, module, new WebContext() {
            @Override
            public Locale getLocale() {
              return Locale.ENGLISH;
            }

            @Override
            public String getContextPath() {
              return "/premod";
            }
          });
      File imageFile = new File("c:/temp/module_context.png");
      moduleContextImage.printPlotToFile(imageFile, "png");
      System.out.println("finished");
    }
  }
}
