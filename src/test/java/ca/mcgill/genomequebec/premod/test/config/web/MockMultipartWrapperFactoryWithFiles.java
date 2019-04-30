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

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.FileUploadLimitExceededException;
import net.sourceforge.stripes.controller.multipart.MultipartWrapper;
import net.sourceforge.stripes.controller.multipart.MultipartWrapperFactory;

/**
 * {@link MultipartWrapperFactory} that supports files.
 */
public class MockMultipartWrapperFactoryWithFiles implements MultipartWrapperFactory {
  public static final String FILE_BEAN_REQUEST_ATTRIBUTE =
      MockMultipartWrapperFactoryWithFiles.class.getName() + "#fileBeanParameters";

  @Override
  public void init(Configuration configuration) throws Exception {
  }

  @Override
  public MultipartWrapper wrap(HttpServletRequest request)
      throws IOException, FileUploadLimitExceededException {
    if (request.getAttribute(FILE_BEAN_REQUEST_ATTRIBUTE) != null
        && !(request.getAttribute(FILE_BEAN_REQUEST_ATTRIBUTE) instanceof Map)) {
      throw new IllegalStateException(
          FILE_BEAN_REQUEST_ATTRIBUTE + " attribute must be a Map<String, FileBean>");
    }

    @SuppressWarnings("unchecked")
    Map<String, FileBean> fileBeanParameters =
        (Map<String, FileBean>) request.getAttribute(FILE_BEAN_REQUEST_ATTRIBUTE);
    return new MockMultipartWrapperWithFiles(fileBeanParameters);
  }
}
