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

package ca.mcgill.genomequebec.premod.web.tag.module;

import ca.mcgill.genomequebec.premod.Constants;
import ca.mcgill.genomequebec.premod.MainConfiguration;
import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.qc.ircm.bio.ChromosomeFormat;
import ca.qc.ircm.bio.SimpleChromosomeLocation;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import net.sourceforge.stripes.util.UrlBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Generates link to UCSC genome browser pointing to module location.
 */
public class UcscGenomeUrlTag<E extends Object> extends BodyTagSupport {
  private static final long serialVersionUID = 8416442716050232799L;
  private static final String BUNDLE = UcscGenomeUrlTag.class.getName();
  /**
   * Module.
   */
  private Module module;
  /**
   * Page attribute under which link to UCSC genome browser is saved.
   */
  private String var;

  /**
   * <p>
   * Generates link to UCSC genome browser.
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
    if (module == null) {
      return Tag.EVAL_PAGE;
    }

    WebApplicationContext context = WebApplicationContextUtils
        .getRequiredWebApplicationContext(pageContext.getServletContext());
    final MainConfiguration mainConfiguration = context.getBean(MainConfiguration.class);

    // Generate URL.
    UrlBuilder urlBuilder = new UrlBuilder(pageContext.getRequest().getLocale(),
        "http://www.genome.ucsc.edu/cgi-bin/hgTracks", true);
    urlBuilder.addParameter("clade", "vertebrate");
    urlBuilder.addParameter("pix", "620");
    urlBuilder.addParameter("Submit", "submit");
    if (Organism.MOUSE == module.getOrganism()) {
      urlBuilder.addParameter("org", "Mouse");
      urlBuilder.addParameter("db", "mm6");
      urlBuilder.addParameter("hgsid", "74149282");
    } else {
      urlBuilder.addParameter("org", "Human");
      urlBuilder.addParameter("db", "hg17");
      urlBuilder.addParameter("hgsid", "61477757");
    }

    // Add file parameter.
    String fileParam = getFileParameterValue(mainConfiguration);
    urlBuilder.addParameter("hgt.customText", fileParam);

    // Compute UCSC window location.
    long windowStart;
    long windowEnd;
    if (module.getStart() < Constants.MODULE_GENE_WINDOW / 2) {
      windowStart = 0;
    } else {
      windowStart = module.getStart() - (Constants.MODULE_GENE_WINDOW / 2);
    }
    windowEnd = windowStart + Constants.MODULE_GENE_WINDOW;
    // Add position parameter.
    ChromosomeFormat chromosomeFormat = new ChromosomeFormat();
    String positionParam = chromosomeFormat
        .format(new SimpleChromosomeLocation(module.getChromosome(), windowStart, windowEnd));
    urlBuilder.addParameter("position", positionParam);

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

  private String getFileParameterValue(MainConfiguration mainConfiguration) {
    String versionValue = LocaleSupport.getLocalizedMessage(pageContext,
        "version." + module.getVersion().name(), BUNDLE);
    String chromosomeValue;
    if (module.getChromosome() == null || module.getChromosome().equals("")) {
      chromosomeValue = LocaleSupport.getLocalizedMessage(pageContext, "all", BUNDLE);
    } else {
      chromosomeValue = LocaleSupport.getLocalizedMessage(pageContext, "chromosome",
          new Object[] { module.getChromosome() }, BUNDLE);
    }
    String fileLocation =
        LocaleSupport.getLocalizedMessage(pageContext, module.getOrganism().name() + ".url",
            new Object[] { versionValue, chromosomeValue }, BUNDLE);

    String url = mainConfiguration.serverUrl();
    url += "/" + fileLocation;
    return url;
  }

  @Override
  public void release() {
    super.release();
    module = null;
    var = null;
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }
}
