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

package ca.mcgill.genomequebec.premod.service;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link LinkedBlockingQueue} with a terminated flag to inform that no more objects will be added
 * to queue.
 */
public class DataBlockingQueue<E extends Object> extends LinkedBlockingQueue<E> {
  private static final long serialVersionUID = -7690121015392398567L;
  private final AtomicBoolean terminated = new AtomicBoolean(false);

  public DataBlockingQueue() {
    super(1000);
  }

  public boolean isTerminated() {
    return terminated.get();
  }

  public void setTerminated(boolean terminated) {
    this.terminated.set(terminated);
  }

  /**
   * Blocks until results are available in queue.
   */
  public void waitForResults() {
    while (!terminated.get() && this.isEmpty()) {
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        throw new RuntimeException("Thread interrupted", e);
      }
    }
  }
}
