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

package ca.mcgill.genomequebec.premod.web;

import ca.mcgill.genomequebec.premod.service.error.ErrorSaver;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Send errors at regular intervals.
 */
public class ErrorSenderListener implements ServletContextListener {
  @Inject
  private ErrorSaver errorSaver;
  private Timer timer;

  @Override
  public void contextInitialized(ServletContextEvent event) {
    WebApplicationContext context =
        WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
    context.getAutowireCapableBeanFactory().autowireBean(this);
    errorSaver.sendEmail();
    Date senderStartMoment =
        new DateTime().withHourOfDay(23).withMinuteOfHour(50).withSecondOfMinute(0).toDate();
    timer = new Timer(true);
    timer.scheduleAtFixedRate(new ErrorSenderTask(), senderStartMoment,
        Days.ONE.toStandardDuration().getMillis());
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    timer.cancel();
  }

  private class ErrorSenderTask extends TimerTask {
    @Override
    public void run() {
      errorSaver.sendEmail();
    }
  }
}
