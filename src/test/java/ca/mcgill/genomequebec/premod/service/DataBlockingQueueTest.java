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

import ca.mcgill.genomequebec.premod.test.config.NonTransactionalTestAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@NonTransactionalTestAnnotations
public class DataBlockingQueueTest {
  private class QueueFiller extends Thread {
    private final List<Integer> values;

    private QueueFiller(List<Integer> values) {
      this.values = values;
    }

    @Override
    public void run() {
      try {
        for (Integer value : values) {
          queue.put(value);
        }
      } catch (InterruptedException e) {
        // Ignore.
      } finally {
        queue.setTerminated(true);
      }
    }
  }

  private class BlockingQueueFiller extends Thread {
    private final List<Integer> values;

    private BlockingQueueFiller(List<Integer> values) {
      this.values = values;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(1000);
        for (Integer value : values) {
          queue.put(value);
        }
      } catch (InterruptedException e) {
        // Ignore.
      } finally {
        queue.setTerminated(true);
      }
    }
  }

  private final Random random = new Random();
  private DataBlockingQueue<Integer> queue;

  @Before
  public void beforeTest() {
    queue = new DataBlockingQueue<>();
  }

  @Test
  public void iterate() throws Throwable {
    final List<Integer> values = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      values.add(random.nextInt());
    }

    new QueueFiller(values).start();

    for (Integer expected : values) {
      Integer actual = queue.poll(2, TimeUnit.SECONDS);
      assertEquals(expected, actual);
    }
  }

  @Test
  public void iterate_NeverStopRead() throws Throwable {
    final List<Integer> values = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      values.add(random.nextInt());
    }

    new QueueFiller(values).start();

    for (Integer expected : values) {
      Integer actual = queue.poll(300, TimeUnit.MILLISECONDS);
      assertEquals(expected, actual);
    }
    assertEquals(null, queue.poll(300, TimeUnit.MILLISECONDS));
  }

  @Test
  public void terminated() throws Throwable {
    final List<Integer> values = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      values.add(random.nextInt());
    }

    QueueFiller filler = new QueueFiller(values);
    assertEquals(false, queue.isTerminated());

    filler.start();
    filler.join();

    assertEquals(true, queue.isTerminated());
  }

  @Test(timeout = 5000)
  public void waitForResults_Empty() throws Throwable {
    final List<Integer> values = new ArrayList<>();

    new BlockingQueueFiller(values).start();
    Thread.sleep(100);
    long before = System.currentTimeMillis();
    assertEquals(null, queue.peek());
    queue.waitForResults();
    long after = System.currentTimeMillis();
    assertEquals(true, after - before > 500);
    assertEquals(true, after - before < 1100);
  }

  @Test(timeout = 5000)
  public void waitForResults_NotEmpty() throws Throwable {
    final List<Integer> values = new ArrayList<>();
    for (int i = 0; i < 2; i++) {
      values.add(random.nextInt());
    }

    new BlockingQueueFiller(values).start();
    Thread.sleep(100);
    long before = System.currentTimeMillis();
    assertEquals(null, queue.peek());
    queue.waitForResults();
    long after = System.currentTimeMillis();
    assertEquals(true, after - before > 500);
    assertEquals(true, after - before < 1100);

    assertEquals(values.get(0), queue.peek());
  }
}
