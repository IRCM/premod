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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.mcgill.genomequebec.premod.test.config.NonTransactionalTestAnnotations;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@NonTransactionalTestAnnotations
public class DataBlockingQueueIterableTest {
  private Iterator<Integer> iterator;
  @Mock
  private DataBlockingQueue<Integer> queue;

  @Before
  public void beforeTest() {
    iterator = new DataBlockingQueueIterable<>(queue).iterator();
  }

  @Test
  public void hashNext() {
    when(queue.peek()).thenReturn(20).thenReturn(null);
    assertEquals(true, iterator.hasNext());
    assertEquals(false, iterator.hasNext());

    verify(queue, times(2)).peek();
    verify(queue, never()).poll();
  }

  @Test
  public void next() {
    when(queue.poll()).thenReturn(20).thenReturn(null);
    assertEquals((Integer) 20, iterator.next());
    assertEquals(null, iterator.next());
    verify(queue, times(2)).poll();
  }

  @Test
  public void iteration() {
    when(queue.peek()).thenReturn(20).thenReturn(12).thenReturn(25).thenReturn(null);
    when(queue.poll()).thenReturn(20).thenReturn(12).thenReturn(25).thenReturn(null);

    int index = 0;
    while (iterator.hasNext()) {
      index++;
      if (index == 1) {
        assertEquals((Integer) 20, iterator.next());
      } else if (index == 2) {
        assertEquals((Integer) 12, iterator.next());
      } else if (index == 3) {
        assertEquals((Integer) 25, iterator.next());
      }
    }
    assertEquals(3, index);

    verify(queue, times(4)).peek();
    verify(queue, times(3)).poll();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void remove() {
    iterator.remove();
  }
}
