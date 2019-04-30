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

import java.util.Iterator;

/**
 * Allows iteration on all current and future elements of a {@link DataBlockingQueue}.
 * <p>
 * The {@link Iterator} returned by {@link #iterator()} will block on {@link Iterator#hasNext()} and
 * {@link Iterator#next()} operations until elements are available or when {@link DataBlockingQueue}
 * is terminated.
 * </p>
 */
public class DataBlockingQueueIterable<E> implements Iterable<E> {
  private static class DataBlockingQueueIterator<E> implements Iterator<E> {
    private final DataBlockingQueue<E> queue;

    private DataBlockingQueueIterator(DataBlockingQueue<E> queue) {
      this.queue = queue;
    }

    @Override
    public boolean hasNext() {
      queue.waitForResults();
      return queue.peek() != null;
    }

    @Override
    public E next() {
      queue.waitForResults();
      return queue.poll();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove is not supported");
    }
  }

  private final DataBlockingQueue<E> queue;

  public DataBlockingQueueIterable(DataBlockingQueue<E> queue) {
    this.queue = queue;
  }

  @Override
  public Iterator<E> iterator() {
    return new DataBlockingQueueIterator<E>(queue);
  }
}
