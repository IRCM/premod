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

import static org.junit.Assert.assertEquals;

import ca.mcgill.genomequebec.premod.test.config.NonTransactionalTestAnnotations;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@NonTransactionalTestAnnotations
public class MailConfigurationTest {
  @Inject
  private MailConfiguration mailConfiguration;
  @Inject
  private JavaMailSenderImpl mailSender;

  @Test
  public void defaultValues() {
    assertEquals(true, mailConfiguration.isEnabled());
    assertEquals("mailserver.example.com", mailConfiguration.getHost());
    assertEquals("christian.poitras@example.com", mailConfiguration.getFrom());
    assertEquals("christian.poitras@example.com", mailConfiguration.getTo());
    assertEquals("premod", mailConfiguration.getSubject());
    assertEquals("mailserver.example.com", mailSender.getHost());
  }
}
