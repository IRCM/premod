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

package ca.mcgill.genomequebec.premod.service.error;

import ca.mcgill.genomequebec.premod.MainConfiguration;
import ca.mcgill.genomequebec.premod.mail.EmailService;
import ca.mcgill.genomequebec.premod.mail.MailConfiguration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.SocketException;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Save number of access.
 */
@Component
public class ErrorSaver {
  private final Logger logger = LoggerFactory.getLogger(ErrorSaver.class);
  private EmailService emailService;
  private MainConfiguration mainConfiguration;
  private MailConfiguration mailConfiguration;

  @Inject
  protected ErrorSaver(EmailService emailService, MainConfiguration mainConfiguration,
      MailConfiguration mailConfiguration) {
    this.emailService = emailService;
    this.mainConfiguration = mainConfiguration;
    this.mailConfiguration = mailConfiguration;
  }

  private File file() {
    return new File(mainConfiguration.home(), "error.txt");
  }

  /**
   * Saves error to file.
   *
   * @param exception
   *          error
   * @param request
   *          request that generated the error
   */
  public void saveError(Throwable exception, HttpServletRequest request) {
    if (!(exception instanceof IOException) || !(exception.getCause() instanceof SocketException)) {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      StringBuilder message = new StringBuilder();
      message.append(format.format(new Date()));
      message.append(" ");
      message.append(request.getRequestURL());
      if (request.getQueryString() != null) {
        message.append("?");
        message.append(request.getQueryString());
      }
      File file = file();
      try {
        FileOutputStream output = new FileOutputStream(file, true);
        try {
          FileLock lock = output.getChannel().lock();
          try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"));
            writer.write(message.toString());
            writer.write(System.getProperty("line.separator"));
            writer.flush();
            exception.printStackTrace(writer);
            writer.flush();
          } finally {
            lock.release();
          }
        } finally {
          output.close();
        }
      } catch (IOException ioe) {
        logger.error("Could not save error in file {}", file, ioe);
      }
      logger.error(message.toString(), exception);
    } else {
      logger.error(exception.getMessage(), exception);
    }
  }

  /**
   * Send errors by email to administrator.
   */
  public void sendEmail() {
    File file = file();
    if (file.exists() && file.length() > 0) {
      try {
        // Send file.
        MimeMessageHelper email = emailService.htmlEmail();
        email.addTo(mailConfiguration.getTo());
        email.setSubject("Errors");
        email.setText("Errors for premod", "Errors for premod");
        email.addAttachment("error.txt", file);
        emailService.send(email);
      } catch (MessagingException e) {
        logger.error("Could not send email containing errors", e);
      }

      // Clear content file.
      try {
        FileOutputStream output = new FileOutputStream(file);
        try {
          FileLock lock = output.getChannel().lock();
          try {
            output.write(new byte[0]);
          } finally {
            lock.release();
          }
        } finally {
          output.close();
        }
      } catch (IOException ioe) {
        logger.error("Could not clear file {}", file, ioe);
      }
    }
  }
}
