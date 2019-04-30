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

package ca.mcgill.genomequebec.premod;

import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.sourceforge.stripes.vfs.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * Stripes VFS that supports Spring Boot. <br>
 * This version was modified by Christian Poitras to support unit testing.
 *
 * @author Hans Westerbeek from Stripes project.
 * @author poitrac
 */
public class SpringBootExecutableJarVfs extends VFS {
  private static final Logger logger = LoggerFactory.getLogger(SpringBootExecutableJarVfs.class);

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public List<String> list(final URL url, final String path) throws IOException {
    ClassLoader cl = this.getClass().getClassLoader();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
    try {
      Resource[] resources = resolver.getResources(url + "/**/*.class");
      final List<String> resourcePaths = Arrays.asList(resources).stream()
          .filter(r -> r.toString().contains(path)).map(resource -> {
            try {
              return preserveSubpackageName(resource.getURI(), path);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }).collect(Collectors.toList());
      return resourcePaths;
    } catch (IllegalArgumentException e) {
      logger.warn(
          "Caught IllegalArgumentException when listing resources for URL {} and path {}, {}", url,
          path, e.getMessage());
      return Collections.emptyList();
    } catch (MalformedURLException e) {
      logger.warn("Caught MalformedURLException when listing resources for URL {} and path {}, {}",
          url, path, e.getMessage());
      return Collections.emptyList();
    } catch (Throwable e) {
      logger.warn("Caught exception when listing resources for URL {} and path {}", url, path, e);
      return Collections.emptyList();
    }
  }

  @VisibleForTesting
  protected static String preserveSubpackageName(final URI uri, final String rootPath) {
    final String uriStr = uri.toString();
    // we must return the uri with everything before the rootpath stripped off
    final int start = uriStr.indexOf(rootPath);
    return uriStr.substring(start, uriStr.length());
  }
}
