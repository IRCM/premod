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

package ca.mcgill.genomequebec.premod.web.tag;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag to write a DNA or protein sequence.
 */
public class SequenceTag extends SimpleTagSupport {
  /**
   * Name of the sequence.
   */
  private String sequenceName = null;
  /**
   * Property where the name of the sequence can be found.
   */
  private String sequence = null;

  /**
   * Render sequence.
   *
   * @exception JspException
   *              if a JSP exception has occurred
   */
  @Override
  public void doTag() throws JspException, IOException {
    Writer writer = getJspContext().getOut();
    writer.write(stringToFastaLikeFormat(sequenceName, sequence));
  }

  private String stringToFastaLikeFormat(String name, String sequence) {
    StringBuffer result = new StringBuffer();

    if (name != null) {
      // Add the name of the sequence in a format: >name
      result.append(">" + name + "\n");
    }

    if (sequence != null) {
      // Add sequence with 80 chars by line.
      String tmp = sequence;
      while (tmp.length() > 80) {
        result.append(tmp.substring(0, 80));
        result.append("<br/>");
        tmp = tmp.substring(80);
      }
      result.append(tmp);
    } else {
      // If sequence is empty, add only a line feed.
      result.append("\n");
    }

    return result.toString();
  }

  public String getSequenceName() {
    return sequenceName;
  }

  public void setSequenceName(String sequenceName) {
    this.sequenceName = sequenceName;
  }

  public String getSequence() {
    return sequence;
  }

  public void setSequence(String sequence) {
    this.sequence = sequence;
  }
}
