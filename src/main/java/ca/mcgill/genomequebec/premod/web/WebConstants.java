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

package ca.mcgill.genomequebec.premod.web;

/**
 * <p>
 * Contains the web constants for this program.
 * </p>
 */
public class WebConstants {
  /**
   * Type of image the program (java classes) generates.
   */
  public static final String IMAGE_TYPE = "png";

  /**
   * Base hyperlink to create a link to Entrez Gene database (NCBI). Note that the parameter
   * list_uids must be added to hyperlink with the Protein Gene database id.
   */
  public static final String GENE_BASE_LINK =
      "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=gene&amp;cmd=Retrieve&amp;dopt=Graphics";
  /**
   * Use with GENE_BASE_LINK as parameter identifier.
   */
  public static final String GENE_PARAMETER = "list_uids";

  /**
   * Base hyperlink to create a link to Entrez Nucleotide database (NCBI). Note that the parameter
   * val must be added to hyperlink with the Nucleotide database id.
   */
  public static final String NUCLEOTIDE_BASE_LINK =
      "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=Nucleotide";
  /**
   * Use with NUCLEOTIDE_BASE_LINK as parameter identifier.
   */
  public static final String NUCLEOTIDE_PARAMETER = "val";

  /**
   * UCSC base link to go to Genome browser for Human.
   */
  public static final String UCSC_HUMAN_GENOME_LINK =
      "http://www.genome.ucsc.edu/cgi-bin/hgTracks?clade=vertebrate&amp;org=Human&amp;db=hg17&amp;pix=620&amp;hgsid=61477757&amp;Submit=submit";
  /**
   * UCSC base link to go to Genome browser for Mouse.
   */
  public static final String UCSC_MOUSE_GENOME_LINK =
      "http://www.genome.ucsc.edu/cgi-bin/hgTracks?clade=vertebrate&amp;org=Mouse&amp;db=mm6&amp;pix=620&amp;hgsid=74149282&amp;Submit=submit";
  /**
   * UCSC parameter to add to show a specific window.
   */
  public static final String UCSC_POSITION_PARAMETER = "position";
}
