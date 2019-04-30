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

package ca.mcgill.genomequebec.premod;

import java.io.File;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration by spring boot.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = MainConfigurationSpringBoot.PREFIX)
public class MainConfigurationSpringBoot implements MainConfiguration {
  public static final String PREFIX = "main";
  private File home;
  private String serverUrl;

  @Override
  public File home() {
    return home;
  }

  @Override
  public String serverUrl() {
    return serverUrl;
  }

  public File getHome() {
    return home;
  }

  public void setHome(File home) {
    this.home = home;
  }

  public String getServerUrl() {
    return serverUrl;
  }

  public void setServerUrl(String serverUrl) {
    this.serverUrl = serverUrl;
  }
}
