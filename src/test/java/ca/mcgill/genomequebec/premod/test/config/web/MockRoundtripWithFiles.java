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
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.mock.MockHttpSession;
import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.mock.MockServletContext;

/**
 * MockRoundtrip that supports files.
 */
public class MockRoundtripWithFiles extends MockRoundtrip {
  /**
   * Sets file parameter.
   *
   * @param name
   *          file name
   * @param fileBean
   *          file
   * @throws IOException
   *           could not set file parameter
   */
  public void setFileParameter(String name, FileBean fileBean) throws IOException {
    if (this.getRequest()
        .getAttribute(MockMultipartWrapperFactoryWithFiles.FILE_BEAN_REQUEST_ATTRIBUTE) == null) {
      this.getRequest().setAttribute(
          MockMultipartWrapperFactoryWithFiles.FILE_BEAN_REQUEST_ATTRIBUTE,
          new HashMap<String, FileBean>());
    }

    @SuppressWarnings("unchecked")
    Map<String, FileBean> fileBeanParameters = (Map<String, FileBean>) this.getRequest()
        .getAttribute(MockMultipartWrapperFactoryWithFiles.FILE_BEAN_REQUEST_ATTRIBUTE);
    fileBeanParameters.put(name, fileBean);
  }

  public MockRoundtripWithFiles(MockServletContext context, Class<? extends ActionBean> beanType,
      MockHttpSession session) {
    super(context, beanType, session);
  }

  public MockRoundtripWithFiles(MockServletContext context, Class<? extends ActionBean> beanType) {
    super(context, beanType);
  }

  public MockRoundtripWithFiles(MockServletContext context, String actionBeanUrl,
      MockHttpSession session) {
    super(context, actionBeanUrl, session);
  }

  public MockRoundtripWithFiles(MockServletContext context, String actionBeanUrl) {
    super(context, actionBeanUrl);
  }
}
