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

package ca.mcgill.genomequebec.premod.test.config.web;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.controller.FileUploadLimitExceededException;
import net.sourceforge.stripes.controller.multipart.MultipartWrapper;

/**
 * {@link MultipartWrapper} that supports files.
 */
public class MockMultipartWrapperWithFiles implements MultipartWrapper {
  private final Map<String, FileBean> fileBeanParameters;

  public MockMultipartWrapperWithFiles(Map<String, FileBean> fileBeanParameters) {
    this.fileBeanParameters = fileBeanParameters;
  }

  @Override
  public void build(HttpServletRequest request, File tempDir, long maxPostSize)
      throws IOException, FileUploadLimitExceededException {
  }

  @Override
  public Enumeration<String> getParameterNames() {
    return null;
  }

  @Override
  public String[] getParameterValues(String name) {
    return null;
  }

  @Override
  public Enumeration<String> getFileParameterNames() {
    Vector<String> names = new Vector<String>();
    if (fileBeanParameters != null) {
      for (String name : fileBeanParameters.keySet()) {
        names.add(name);
      }
    }
    return names.elements();
  }

  @Override
  public FileBean getFileParameterValue(String name) {
    if (fileBeanParameters != null) {
      return fileBeanParameters.get(name);
    } else {
      return null;
    }
  }
}
