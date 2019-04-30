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

package ca.mcgill.genomequebec.premod.service.access;

import ca.mcgill.genomequebec.premod.MainConfiguration;
import ca.mcgill.genomequebec.premod.mail.EmailService;
import ca.mcgill.genomequebec.premod.mail.MailConfiguration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.inject.Inject;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Email number of access to administrator.
 */
public class AccessSender {
  private final Logger logger = LoggerFactory.getLogger(AccessSender.class);
  @Inject
  private EmailService emailService;
  @Inject
  private MailConfiguration mailConfiguration;
  @Inject
  private MainConfiguration mainConfiguration;

  protected AccessSender() {
  }

  protected AccessSender(EmailService emailService, MailConfiguration mailConfiguration,
      MainConfiguration mainConfiguration) {
    this.emailService = emailService;
    this.mailConfiguration = mailConfiguration;
    this.mainConfiguration = mainConfiguration;
  }

  /**
   * Send number of access by email to administrator.
   */
  public void emailAccess() {
    try {
      File location = new File(mainConfiguration.home(), "premod_access.txt");
      if (location.exists()) {
        // Send file.
        MimeMessageHelper email = emailService.htmlEmail();
        email.addTo(mailConfiguration.getTo());
        email.setSubject("Statistics");
        email.setText("Statistics for premod", "Statistics for premod");
        email.addAttachment("statistics.txt", location);
        emailService.send(email);

        // Reset access files content.
        StringBuffer content = new StringBuffer();
        content.append("Date\tSession\tDownload\n");
        Writer writer = new OutputStreamWriter(new FileOutputStream(location), "UTF-8");
        try {
          writer.append(content);
        } finally {
          writer.close();
        }
      }
    } catch (MessagingException e) {
      logger.error("Could not send email containing statistics", e);
    } catch (IOException e) {
      logger.error("Could not overwrite file containing statistics", e);
    }
  }
}
