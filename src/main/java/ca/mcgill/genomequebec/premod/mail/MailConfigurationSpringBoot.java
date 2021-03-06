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

package ca.mcgill.genomequebec.premod.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = MailConfigurationSpringBoot.PREFIX)
public class MailConfigurationSpringBoot implements MailConfiguration {
  public static final String PREFIX = "mail";
  @Value("${spring.application.name}")
  private String applicationName;
  private boolean enabled;
  private String host;
  private String from;
  private String to;

  @Override
  public String getSubject() {
    return applicationName;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  @Override
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  @Override
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }
}
