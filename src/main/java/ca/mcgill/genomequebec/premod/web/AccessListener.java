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

import ca.mcgill.genomequebec.premod.service.access.AccessKeeper;
import ca.mcgill.genomequebec.premod.service.access.AccessSaver;
import ca.mcgill.genomequebec.premod.service.access.AccessSender;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Keep information about user accessing program.
 */
public class AccessListener
    implements ServletContextListener, HttpSessionListener, ServletRequestListener {
  @Autowired
  private AccessKeeper accessKeeper;
  @Autowired
  private AccessSaver accessSaver;
  @Autowired
  private AccessSender accessSender;
  private Timer timer;

  @Override
  public void contextInitialized(ServletContextEvent event) {
    WebApplicationContext context =
        WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());
    context.getAutowireCapableBeanFactory().autowireBean(this);
    Date saverStartMoment =
        new DateTime().withHourOfDay(23).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
    Date senderStartMoment =
        new DateTime().withDayOfWeek(1).withHourOfDay(8).withMinuteOfHour(0).toDate();
    timer = new Timer(true);
    timer.scheduleAtFixedRate(new AccessSaverTask(), saverStartMoment,
        Days.ONE.toStandardDuration().getMillis());
    timer.scheduleAtFixedRate(new AccesSenderTask(), senderStartMoment,
        Days.days(7).toStandardDuration().getMillis());
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    timer.cancel();
    new AccessSaverTask().run();
  }

  /**
   * Save information that a new session is created.
   */
  @Override
  public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    accessKeeper.incrementSession();
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
  }

  /**
   * Save information that a download has been requested.
   */
  @Override
  public void requestInitialized(ServletRequestEvent event) {
    if (event.getServletRequest() instanceof HttpServletRequest) {
      HttpServletRequest request = (HttpServletRequest) event.getServletRequest();
      if (request.getRequestURI().startsWith("/data/publication")
          || request.getRequestURI().startsWith("/data/UCSC")) {
        accessKeeper.incrementDownload();
      }
    }
  }

  @Override
  public void requestDestroyed(ServletRequestEvent event) {
  }

  private class AccessSaverTask extends TimerTask {
    @Override
    public void run() {
      accessSaver.saveAccess();
    }
  }

  private class AccesSenderTask extends TimerTask {
    @Override
    public void run() {
      accessSender.emailAccess();
    }
  }
}
