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

package ca.mcgill.genomequebec.premod.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for {@link ExceptionUtils}.
 */
public class ExceptionUtilsTest {
  @Test
  public void optionallyPackageRuntimeException_NotRuntime() {
    ClassNotFoundException expectedCause = new ClassNotFoundException();
    Exception exception = ExceptionUtils.optionallyPackageRuntimeException(expectedCause, "Test");
    assertEquals(PackagedRuntimeException.class, exception.getClass());
    assertEquals("Test", exception.getMessage());
    assertEquals(expectedCause, exception.getCause());
    assertEquals(true, expectedCause == exception.getCause());
  }

  @Test
  public void optionallyPackageRuntimeException_Runtime() {
    RuntimeException expectedException = new RuntimeException("Original message");
    Exception exception =
        ExceptionUtils.optionallyPackageRuntimeException(expectedException, "Test");
    assertSame(expectedException, exception);
    assertEquals("Original message", exception.getMessage());
  }

  @Test
  public void throwExceptionIfMatch_Null() {
    try {
      ExceptionUtils.throwExceptionIfMatch(null, ClassNotFoundException.class);
    } catch (ClassNotFoundException e) {
      fail("ClassNotFoundException unexcepted");
    }
  }

  @Test
  public void throwExceptionIfMatch_Match() {
    ClassNotFoundException expectedException = new ClassNotFoundException();
    try {
      ExceptionUtils.throwExceptionIfMatch(expectedException, ClassNotFoundException.class);
      fail("Excepted ClassNotFoundException");
    } catch (ClassNotFoundException e) {
      assertEquals(e, expectedException);
    }
  }

  @Test
  public void throwExceptionIfMatch_NoMatch() {
    ClassNotFoundException expectedException = new ClassNotFoundException();
    try {
      ExceptionUtils.throwExceptionIfMatch(expectedException, InterruptedException.class);
    } catch (InterruptedException e) {
      fail("InterruptedException unexpected");
    }
  }

  @Test
  public void throwExceptionIfMatch_Assignable() {
    NumberFormatException expectedException = new NumberFormatException();
    try {
      ExceptionUtils.throwExceptionIfMatch(expectedException, IllegalArgumentException.class);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Success.
    }
  }

  @Test
  public void throwIfInterrupted() throws Throwable {
    Thread thread = new Thread() {
      @Override
      public void run() {
        try {
          ExceptionUtils.throwIfInterrupted("Test");
        } catch (InterruptedException e) {
          fail("unexpected InterruptedException");
        }

        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          // Ignore.
        }

        try {
          ExceptionUtils.throwIfInterrupted("Test");
          fail("Expected InterruptedException");
        } catch (InterruptedException e) {
          assertEquals("Test", e.getMessage());
        }
      }
    };
    Thread.sleep(1000);
    thread.interrupt();
  }

  @Test
  public void getCause() {
    NullPointerException expectedCause0 = new NullPointerException("test_0");
    IllegalArgumentException expectedCause1 =
        new IllegalArgumentException("test_1", expectedCause0);
    Exception exception = new Exception(expectedCause1);
    assertEquals(expectedCause1,
        ExceptionUtils.getCause(exception, IllegalArgumentException.class));
    assertEquals(expectedCause0, ExceptionUtils.getCause(exception, NullPointerException.class));
    assertEquals(null, ExceptionUtils.getCause(exception, ClassNotFoundException.class));
  }

  @Test
  public void getCause_MultipleCauseSameClass() {
    IllegalStateException expectedCause0 = new IllegalStateException("test_0");
    IllegalArgumentException expectedCause1 =
        new IllegalArgumentException("test_1", expectedCause0);
    IllegalStateException expectedCause2 = new IllegalStateException("test_2", expectedCause1);
    Exception exception = new Exception(expectedCause2);
    assertEquals(expectedCause1,
        ExceptionUtils.getCause(exception, IllegalArgumentException.class));
    assertEquals(expectedCause2, ExceptionUtils.getCause(exception, IllegalStateException.class));
    assertEquals(null, ExceptionUtils.getCause(exception, ClassNotFoundException.class));
  }
}
