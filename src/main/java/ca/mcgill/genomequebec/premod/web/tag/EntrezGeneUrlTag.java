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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import net.sourceforge.stripes.util.UrlBuilder;

/**
 * Generates link to NCBI Entrez Gene for gene.
 */
public class EntrezGeneUrlTag<E extends Object> extends BodyTagSupport {
  private static final long serialVersionUID = -2007452801583657333L;
  /**
   * Entrez gene id of gene.
   */
  private Long geneId;
  /**
   * Page attribute under which link to Entrez Gene is saved.
   */
  private String var;

  /**
   * <p>
   * Generates link to NCBI Entrez Gene for gene.
   * </p>
   * <p>
   * Link is saved into page attribute var if var is specified.
   * </p>
   * <p>
   * Otherwise, an anchor tag is create with link.
   * </p>
   */
  @Override
  public int doEndTag() throws JspException {
    // Generate URL.
    UrlBuilder urlBuilder = new UrlBuilder(pageContext.getRequest().getLocale(),
        "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi", true);
    urlBuilder.addParameter("db", "gene");
    urlBuilder.addParameter("cmd", "Retrieve");
    urlBuilder.addParameter("dopt", "Graphics");

    if (geneId != null) {
      // Add geneId parameter.
      urlBuilder.addParameter("list_uids", geneId);
    }

    if (var != null) {
      // Save URL in page.
      pageContext.setAttribute(var, urlBuilder.toString());
    } else {
      // Write anchor tag.
      try {
        this.pageContext.getOut().write("<a href=\"");
        this.pageContext.getOut().write(urlBuilder.toString());
        this.pageContext.getOut().write("\">");
        // Add body content of tag to jsp.
        if (this.bodyContent != null) {
          this.pageContext.getOut().write(this.bodyContent.getString());
        }
        this.pageContext.getOut().write("</a>");
      } catch (IOException e) {
        throw new JspException(e);
      }
    }

    return super.doEndTag();
  }

  @Override
  public void release() {
    geneId = null;
    var = null;
    super.release();
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public Long getGeneId() {
    return geneId;
  }

  public void setGeneId(Long geneId) {
    this.geneId = geneId;
  }
}
