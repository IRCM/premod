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

package ca.mcgill.genomequebec.premod.web.image;

import ca.mcgill.genomequebec.premod.MainConfiguration;
import ca.mcgill.genomequebec.premod.business.Exon;
import ca.mcgill.genomequebec.premod.business.Intron;
import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Nucleotide;
import ca.mcgill.genomequebec.premod.business.Organism;
import ca.mcgill.genomequebec.premod.business.Version;
import ca.mcgill.genomequebec.premod.web.WebConstants;
import ca.mcgill.genomequebec.premod.web.WebContext;
import ca.mcgill.genomequebec.premod.web.module.ReadModuleActionBean;
import ca.qc.ircm.bio.ChromosomeFormat;
import ca.qc.ircm.bio.ChromosomeLocation;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import net.sourceforge.stripes.util.UrlBuilder;

/**
 * Generates an image of genomic context around a location.
 */
public abstract class ContextImage {
  /**
   * An URL with an it's location on image.
   */
  public static interface UrlLocation {
    public static class Default implements UrlLocation {
      private final String url;
      private final String alt;
      private final Rectangle location;

      /**
       * Creates URL location.
       *
       * @param url
       *          URL
       * @param alt
       *          alternate text
       * @param location
       *          location
       */
      public Default(String url, String alt, Rectangle location) {
        this.url = url;
        this.alt = alt;
        this.location = location;
      }

      @Override
      public String getUrl() {
        return url;
      }

      @Override
      public String getAlt() {
        return alt;
      }

      @Override
      public Rectangle getLocation() {
        return location;
      }
    }

    /**
     * Returns URL.
     *
     * @return URL
     */
    public String getUrl();

    /**
     * Returns alternate text for URL.
     *
     * @return alternate text for URL
     */
    public String getAlt();

    /**
     * Returns URL location on image.
     *
     * @return URL location on image
     */
    public Rectangle getLocation();
  }

  /**
   * Left margin that contains text information before the information line is drawn.
   */
  private static final int LEFT_MARGIN = 100;
  /**
   * Padding to use between elements.
   */
  private static final int PADDING = 10;
  /**
   * Color of the left margin separator.
   */
  private static final Color LEFT_SEPARATOR_COLOR = ColorHelper.getInternetColor("FF88BB");
  /**
   * Color of the separators in the main window.
   */
  private static final Color MAIN_SEPARATOR_COLOR = ColorHelper.getInternetColor("AAF0FF");
  /**
   * Module's title height.
   */
  private static final int MODULE_TITLE_HEIGHT = 25;
  /**
   * UCSC's title height.
   */
  private static final int UCSC_TITLE_HEIGHT = 25;
  /**
   * Height of the DNA positions of image.
   */
  private static final int DNA_POSITIONS_HEIGHT = 15;
  /**
   * Color of the written base positions.
   */
  private static final Color DNA_POSITIONS_COLOR = Color.BLACK;
  /**
   * Height of the nucleotide header of image.
   */
  private static final int NUCLEOTIDE_HEADER_HEIGHT = 25;
  /**
   * Height of a Gene information.
   */
  private static final int NUCLEOTIDE_HEIGHT = 15;
  /**
   * Color of the written nucleotides.
   */
  private static final Color NUCLEOTIDE_COLOR = ColorHelper.getInternetColor("2A5CA5");
  /**
   * Height of a nucleotide non-coding transcriptionnal section.
   */
  private static final int NUCLEOTIDE_NON_CODING_HEIGHT = 6;
  /**
   * Height of exons.
   */
  private static final int NUCLEOTIDE_EXON_HEIGHT = 10;
  /**
   * Height of introns.
   */
  private static final int NUCLEOTIDE_INTRON_HEIGHT = 1;
  /**
   * Color of things written into GENE_COLOR.
   */
  private static final Color NUCLEOTIDE_OPPOSITE_COLOR = Color.WHITE;

  /**
   * Direction of an arrow.
   */
  private static enum ArrowDirection {
    RIGHT, LEFT
  }

  /**
   * Height of the Module header of image.
   */
  private static final int MODULE_HEADER_HEIGHT = 25;
  /**
   * Height of a Module information.
   */
  private static final int MODULE_HEIGHT = 15;
  /**
   * Height of module location rectangle.
   */
  private static final int MODULE_SQUARE_HEIGHT = 10;
  /**
   * Color of the written modules.
   */
  private static final Color MODULE_COLOR = ColorHelper.getInternetColor("A00000");

  /**
   * Resource bundle used to get text.
   */
  private final ResourceBundle bundle;
  /**
   * Context of web request.
   */
  private final WebContext webContext;
  /**
   * Application's properties.
   */
  private final MainConfiguration mainConfiguration;

  /**
   * Creates context image.
   *
   * @param webContext
   *          web context
   * @param mainConfiguration
   *          main configuration
   */
  public ContextImage(WebContext webContext, MainConfiguration mainConfiguration) {
    this.bundle = ResourceBundle.getBundle(ContextImage.class.getName(), webContext.getLocale());
    this.webContext = webContext;
    this.mainConfiguration = mainConfiguration;
  }

  /**
   * Returns image height.
   *
   * @return image height
   */
  public int getHeight() {
    int height = MODULE_TITLE_HEIGHT;
    height += UCSC_TITLE_HEIGHT;
    height += DNA_POSITIONS_HEIGHT;
    height += NUCLEOTIDE_HEADER_HEIGHT;
    height += nucleotidesInWindow().size() * NUCLEOTIDE_HEIGHT;
    height += MODULE_HEADER_HEIGHT;
    height += modulesInWindow().size() * MODULE_HEIGHT;
    return height;
  }

  public abstract int getWidth();

  /**
   * Returns context's organism.
   *
   * @return context's organism
   */
  protected abstract Organism organism();

  /**
   * Returns module's version to use for this context.
   *
   * @return module's version to use for this context.
   */
  protected abstract Version version();

  /**
   * Returns context's DNA window.
   *
   * @return context's DNA window
   */
  protected abstract ChromosomeLocation window();

  /**
   * Returns modules inside window.
   *
   * @return modules inside window
   */
  protected abstract Collection<Module> modulesInWindow();

  /**
   * Returns nucleotides inside window.
   *
   * @return nucleotides inside window
   */
  protected abstract Collection<Nucleotide> nucleotidesInWindow();

  /**
   * Returns exons of nucleotides inside window.
   *
   * @return exons of nucleotides inside window
   */
  protected abstract Map<Nucleotide, List<Exon>> nucleotideExons();

  /**
   * Returns introns of nucleotides inside window.
   *
   * @return introns of nucleotides inside window
   */
  protected abstract Map<Nucleotide, List<Intron>> nucleotideIntrons();

  /**
   * Create context image for module.
   */
  private BufferedImage makePlot() {
    // Initialising the buffered image
    BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

    // Set a white background and black border.
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(Color.WHITE);
    graphic.setPaint(Color.BLACK);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Add left separator and indicator lines on graphic.
    graphic.drawImage(this.createSeparator(), 1, 1, null);

    // Add title.
    int titleTop = 1;
    graphic.drawImage(this.createTitle(), 1, titleTop, null);

    // Add UCSC title.
    int ucscTitleTop = titleTop + MODULE_TITLE_HEIGHT;
    graphic.drawImage(this.createUcscTitle(), 1, ucscTitleTop, null);

    // Add nucleotide positions.
    int dnaPositionsTop = ucscTitleTop + UCSC_TITLE_HEIGHT;
    graphic.drawImage(this.createDnaPositions(), 1, dnaPositionsTop, null);

    // Add nucleotide header.
    int nucleotideHeaderTop = dnaPositionsTop + DNA_POSITIONS_HEIGHT;
    graphic.drawImage(this.createNucleotideHeader(), 1, nucleotideHeaderTop, null);

    // Add nucleotides.
    int nucleotidesTop = nucleotideHeaderTop + NUCLEOTIDE_HEADER_HEIGHT;
    graphic.drawImage(this.createNucleotides(), 1, nucleotidesTop, null);

    // Add module header.
    int moduleHeaderTop = nucleotidesTop + nucleotidesInWindow().size() * NUCLEOTIDE_HEIGHT;
    graphic.drawImage(this.createModuleHeader(), 1, moduleHeaderTop, null);

    // Add Modules in window.
    int modulesTop = moduleHeaderTop + MODULE_HEADER_HEIGHT;
    graphic.drawImage(this.createModules(), 1, modulesTop, null);

    // Add a black border.
    graphic.setPaint(Color.BLACK);
    graphic.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);

    return image;
  }

  /**
   * Returns location of clickable URLs.
   *
   * @return location of clickable URLs
   */
  public Collection<UrlLocation> getUrlLocations() {
    int moduleTitleTop = 1;
    int ucscTitleTop = moduleTitleTop + MODULE_TITLE_HEIGHT;
    int dnaPositionsTop = ucscTitleTop + UCSC_TITLE_HEIGHT;
    int nucleotideHeaderTop = dnaPositionsTop + DNA_POSITIONS_HEIGHT;
    int nucleotidesTop = nucleotideHeaderTop + NUCLEOTIDE_HEADER_HEIGHT;
    int moduleHeaderTop = nucleotidesTop + nucleotidesInWindow().size() * NUCLEOTIDE_HEIGHT;
    int modulesTop = moduleHeaderTop + MODULE_HEADER_HEIGHT;

    Collection<UrlLocation> urlLocations = new LinkedList<>();

    // UCSC area.
    {
      UrlBuilder urlBuilder = new UrlBuilder(webContext.getLocale(),
          "http://www.genome.ucsc.edu/cgi-bin/hgTracks", true);
      urlBuilder.addParameter("clade", "vertebrate");
      urlBuilder.addParameter("pix", "620");
      urlBuilder.addParameter("Submit", "submit");
      if (Organism.MOUSE == organism()) {
        urlBuilder.addParameter("org", "Mouse");
        urlBuilder.addParameter("db", "mm6");
        urlBuilder.addParameter("hgsid", "74149282");
      } else {
        urlBuilder.addParameter("org", "Human");
        urlBuilder.addParameter("db", "hg17");
        urlBuilder.addParameter("hgsid", "61477757");
      }
      urlBuilder.addParameter("hgt.customText", getFileParameterValue());
      ChromosomeFormat chromosomeFormat = new ChromosomeFormat();
      urlBuilder.addParameter("position", chromosomeFormat.format(window()));
      String url = urlBuilder.toString();
      Rectangle area = new Rectangle(0, ucscTitleTop, getWidth(), UCSC_TITLE_HEIGHT);
      urlLocations.add(new UrlLocation.Default(url, bundle.getString("title.ucsc.alt"), area));
    }

    // Nucleotide areas.
    int currentNucleotideTop = nucleotidesTop;
    for (Nucleotide nucleotide : nucleotidesInWindow()) {
      // Area for gene.
      UrlBuilder geneUrlBuilder =
          new UrlBuilder(webContext.getLocale(), WebConstants.GENE_BASE_LINK, true);
      geneUrlBuilder.addParameter(WebConstants.GENE_PARAMETER, nucleotide.getLocusLink());
      String geneUrl = geneUrlBuilder.toString();
      Rectangle geneArea = new Rectangle(0, currentNucleotideTop, LEFT_MARGIN, NUCLEOTIDE_HEIGHT);
      urlLocations.add(new UrlLocation.Default(geneUrl, nucleotide.getGene(), geneArea));

      // Area for nucleotide.
      UrlBuilder nucleotideUrlBuilder =
          new UrlBuilder(webContext.getLocale(), WebConstants.NUCLEOTIDE_BASE_LINK, true);
      nucleotideUrlBuilder.addParameter(WebConstants.NUCLEOTIDE_PARAMETER, nucleotide.getMrna());
      String nucleotideUrl = nucleotideUrlBuilder.toString();
      Rectangle nucleotideArea = new Rectangle(LEFT_MARGIN, currentNucleotideTop,
          getWidth() - LEFT_MARGIN, NUCLEOTIDE_HEIGHT);
      urlLocations
          .add(new UrlLocation.Default(nucleotideUrl, nucleotide.getMrna(), nucleotideArea));

      currentNucleotideTop += NUCLEOTIDE_HEIGHT;
    }

    // Modules area.
    int currentModuleTop = modulesTop;
    for (Module module : modulesInWindow()) {
      UrlBuilder urlBuilder =
          new UrlBuilder(webContext.getLocale(), ReadModuleActionBean.class, true);
      urlBuilder.addParameter("module", module);
      String url = (webContext.getContextPath() != null ? webContext.getContextPath() : "")
          + urlBuilder.toString();
      Rectangle area = new Rectangle(0, currentModuleTop, getWidth(), MODULE_HEIGHT);
      urlLocations.add(new UrlLocation.Default(url, module.getName(), area));

      currentModuleTop += MODULE_HEIGHT;
    }
    return urlLocations;
  }

  private String getFileParameterValue() {
    String versionValue = bundle.getString("url.version." + version().name());
    String chromosomeValue;
    if (window().getChromosome() == null || window().getChromosome().equals("")) {
      chromosomeValue = bundle.getString("url.all");
    } else {
      chromosomeValue =
          MessageFormat.format(bundle.getString("url.chromosome"), window().getChromosome());
    }
    String fileLocation = MessageFormat.format(bundle.getString("url." + organism().name()),
        versionValue, chromosomeValue);

    String url = mainConfiguration.serverUrl();
    url += "/" + fileLocation;
    return url;
  }

  /**
   * Draw left separator and indicator lines.
   */
  private BufferedImage createSeparator() {
    BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 11);
    graphic.setFont(font);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Add a separator at left margin.
    graphic.setPaint(LEFT_SEPARATOR_COLOR);
    graphic.fillRect(LEFT_MARGIN - 1, 0, 1, this.getHeight());

    // Add a line at each 20 pixels.
    graphic.setPaint(MAIN_SEPARATOR_COLOR);
    int rightWidth = image.getWidth() - LEFT_MARGIN;
    for (int i = 20; i < rightWidth; i += 20) {
      graphic.fillRect(LEFT_MARGIN + i, 0, 1, this.getHeight());
    }

    return image;
  }

  /**
   * Draw main title for image.
   */
  private BufferedImage createTitle() {
    BufferedImage image =
        new BufferedImage(getWidth(), MODULE_TITLE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(Color.LIGHT_GRAY);
    graphic.setPaint(Color.BLACK);
    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    graphic.setFont(font);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Print module text at center of title.
    String title = title();
    LineMetrics titleMetrics = font.getLineMetrics(title, graphic.getFontRenderContext());
    Rectangle2D titleBounds = font.getStringBounds(title, graphic.getFontRenderContext());
    float titleX = (float) (image.getWidth() - titleBounds.getWidth()) / 2;
    float titleY = (MODULE_TITLE_HEIGHT - titleMetrics.getHeight()) / 2 + titleMetrics.getAscent();
    graphic.drawString(title, titleX, titleY);

    return image;
  }

  /**
   * Returns image title.
   *
   * @return image title
   */
  protected String title() {
    return MessageFormat.format(bundle.getString("title"), window().getChromosome(),
        window().getStart(), window().getEnd());
  }

  /**
   * Draw UCSC title for image.
   */
  private BufferedImage createUcscTitle() {
    BufferedImage image =
        new BufferedImage(getWidth(), UCSC_TITLE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(Color.LIGHT_GRAY);
    graphic.setPaint(Color.BLACK);
    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    graphic.setFont(font);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Print UCSC text at center of title.
    String title = bundle.getString("title.ucsc");
    LineMetrics titleMetrics = font.getLineMetrics(title, graphic.getFontRenderContext());
    Rectangle2D titleBounds = font.getStringBounds(title, graphic.getFontRenderContext());
    float titleX = (float) (image.getWidth() - titleBounds.getWidth()) / 2;
    float titleY = (UCSC_TITLE_HEIGHT - titleMetrics.getHeight()) / 2 + titleMetrics.getAscent();
    graphic.drawString(title, titleX, titleY);

    return image;
  }

  /**
   * Draw DNA positions.
   */
  private BufferedImage createDnaPositions() {
    BufferedImage image =
        new BufferedImage(getWidth(), DNA_POSITIONS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    graphic.setPaint(DNA_POSITIONS_COLOR);
    Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
    graphic.setFont(font);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Print header in left margin.
    String header = bundle.getString("nucleotide.position.header");
    LineMetrics headerMetrics = font.getLineMetrics(header, graphic.getFontRenderContext());
    Rectangle2D headerBounds = font.getStringBounds(header, graphic.getFontRenderContext());
    int headerX = (int) (LEFT_MARGIN - PADDING - headerBounds.getWidth());
    int headerY = (int) (headerMetrics.getAscent());
    graphic.drawString(header, headerX, headerY);

    // Add nucleotide positions with an indicator.
    int rightWidth = image.getWidth() - LEFT_MARGIN;
    long nucleotidePositionStart = window().getStart() - window().getStart() % 5000 + 20000;
    double nuclotideLength = (double) (rightWidth) / (window().getEnd() - window().getStart());
    for (long nucleotidePosition = nucleotidePositionStart; nucleotidePosition < window().getStart()
        + (window().getEnd() - window().getStart()); nucleotidePosition += 20000) {
      // Add indicator
      int indicatorX =
          (int) ((nucleotidePosition - window().getStart()) * nuclotideLength + LEFT_MARGIN);
      graphic.fillRect(indicatorX, 0, 1, DNA_POSITIONS_HEIGHT);
      // Add base position text on left.
      String nucleotidePositionString = Long.toString(nucleotidePosition);
      LineMetrics nucleotidePositionMetrics =
          font.getLineMetrics(nucleotidePositionString, graphic.getFontRenderContext());
      Rectangle2D nucleotidePositionBounds =
          font.getStringBounds(nucleotidePositionString, graphic.getFontRenderContext());
      int nucleotidePositionX = (int) (indicatorX - nucleotidePositionBounds.getWidth());
      int nucleotidePositionY = (int) (nucleotidePositionMetrics.getAscent());
      graphic.drawString(nucleotidePositionString, nucleotidePositionX, nucleotidePositionY);
    }

    return image;
  }

  /**
   * Draw header for nucleotides.
   */
  private BufferedImage createNucleotideHeader() {
    BufferedImage image =
        new BufferedImage(getWidth(), NUCLEOTIDE_HEADER_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    graphic.setPaint(NUCLEOTIDE_COLOR);
    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 11);
    graphic.setFont(font);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Print text at center of header.
    String header = bundle.getString("gene.header");
    LineMetrics headerMetrics = font.getLineMetrics(header, graphic.getFontRenderContext());
    Rectangle2D headerBounds = font.getStringBounds(header, graphic.getFontRenderContext());
    float headerX = (float) (image.getWidth() - headerBounds.getWidth()) / 2;
    float headerY =
        (NUCLEOTIDE_HEADER_HEIGHT - headerMetrics.getHeight()) / 2 + headerMetrics.getAscent();
    graphic.drawString(header, headerX, headerY);

    return image;
  }

  /**
   * Draw all genes that are in the window.
   */
  private BufferedImage createNucleotides() {
    BufferedImage image;
    if (!nucleotidesInWindow().isEmpty()) {
      image = new BufferedImage(getWidth(),
          Math.max(nucleotidesInWindow().size(), 1) * NUCLEOTIDE_HEIGHT,
          BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphic = image.createGraphics();
      graphic.setBackground(new Color(255, 255, 255, 0));
      graphic.setPaint(NUCLEOTIDE_COLOR);
      Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
      graphic.setFont(font);
      graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

      int top = 0;
      for (Nucleotide nucleotide : nucleotidesInWindow()) {
        graphic.drawImage(this.createNucleotide(nucleotide), 0, top, null);
        top += NUCLEOTIDE_HEIGHT;
      }
    } else {
      image = new BufferedImage(getWidth(), 1, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphic = image.createGraphics();
      graphic.setBackground(new Color(255, 255, 255, 0));
      graphic.clearRect(0, 0, image.getWidth(), image.getHeight());
    }

    return image;
  }

  private BufferedImage createNucleotide(Nucleotide nucleotide) {
    BufferedImage image =
        new BufferedImage(getWidth(), NUCLEOTIDE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    graphic.setPaint(NUCLEOTIDE_COLOR);
    Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
    graphic.setFont(font);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Draw nucleotide name.
    graphic.drawImage(this.createNucleotideName(nucleotide), 0, 0, null);
    // Draw nucleotide introns/exons.
    graphic.drawImage(this.createNucleotideDetail(nucleotide), LEFT_MARGIN, 0, null);

    return image;
  }

  /**
   * Draw nucleotide name.
   *
   * @param nucleotide
   *          nucleotide
   * @return image containing nucleotide name
   */
  private BufferedImage createNucleotideName(Nucleotide nucleotide) {
    BufferedImage image =
        new BufferedImage(LEFT_MARGIN, NUCLEOTIDE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    graphic.setPaint(NUCLEOTIDE_COLOR);
    Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
    graphic.setFont(font);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    LineMetrics nameMetrics =
        font.getLineMetrics(nucleotide.getGene(), graphic.getFontRenderContext());
    Rectangle2D nameBounds =
        font.getStringBounds(nucleotide.getGene(), graphic.getFontRenderContext());
    int nameX = (int) (LEFT_MARGIN - PADDING - nameBounds.getWidth());
    int nameY =
        (int) (NUCLEOTIDE_HEIGHT / 2 - nameMetrics.getHeight() / 2 + nameMetrics.getAscent());
    graphic.drawString(nucleotide.getGene(), nameX, nameY);

    return image;
  }

  /**
   * Draw nucleotide introns and exons.
   *
   * @param nucleotide
   *          nucleotide
   * @return image containing nucleotide introns and exons
   */
  private BufferedImage createNucleotideDetail(Nucleotide nucleotide) {
    BufferedImage image =
        new BufferedImage(getWidth() - LEFT_MARGIN, NUCLEOTIDE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
    graphic.setFont(font);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    final List<Intron> introns = nucleotideIntrons().get(nucleotide);
    final List<Exon> exons = nucleotideExons().get(nucleotide);
    int middle = NUCLEOTIDE_HEIGHT / 2;
    double dnaLength = (double) (image.getWidth()) / (window().getEnd() - window().getStart());

    // Draw nucleotide base line.
    int nucleotideX = (int) ((nucleotide.getStart() - window().getStart()) * dnaLength);
    int nucleotideWidth = (int) ((nucleotide.getEnd() - nucleotide.getStart()) * dnaLength);
    graphic.setPaint(NUCLEOTIDE_COLOR);
    graphic.fillRect(nucleotideX, middle, nucleotideWidth, 1);

    // Draw regions outside exons and introns.
    if (!exons.isEmpty()) {
      Exon firstExon = exons.get(0);
      int startNonCodingX = (int) ((nucleotide.getStart() - window().getStart()) * dnaLength);
      int startNonCodingY = middle - NUCLEOTIDE_NON_CODING_HEIGHT / 2;
      int startNonCodingWidth = (int) ((firstExon.getStart() - nucleotide.getStart()) * dnaLength);
      graphic.setPaint(NUCLEOTIDE_COLOR);
      graphic.fillRect(startNonCodingX, startNonCodingY, startNonCodingWidth,
          NUCLEOTIDE_NON_CODING_HEIGHT);
      if (nucleotide.getStrand().equals("+")) {
        for (int i = 3; i < startNonCodingWidth - 1; i += 7) {
          graphic.drawImage(this.createArrow(NUCLEOTIDE_OPPOSITE_COLOR, 3, ArrowDirection.RIGHT),
              startNonCodingX + i, middle - 2, null);
        }
      } else {
        for (int i = startNonCodingWidth - 4; i > 0; i -= 7) {
          graphic.drawImage(this.createArrow(NUCLEOTIDE_OPPOSITE_COLOR, 3, ArrowDirection.LEFT),
              startNonCodingX + i, middle - 2, null);
        }
      }
      Exon lastExon = exons.get(exons.size() - 1);
      int endNonCodingX = (int) ((lastExon.getEnd() - window().getStart()) * dnaLength);
      int endNonCodingY = middle - NUCLEOTIDE_NON_CODING_HEIGHT / 2;
      int endNonCodingWidth = (int) ((nucleotide.getEnd() - lastExon.getEnd()) * dnaLength);
      graphic.setPaint(NUCLEOTIDE_COLOR);
      graphic.fillRect(endNonCodingX, endNonCodingY, endNonCodingWidth,
          NUCLEOTIDE_NON_CODING_HEIGHT);
      if (nucleotide.getStrand().equals("+")) {
        for (int i = 3; i < endNonCodingWidth - 1; i += 7) {
          graphic.drawImage(this.createArrow(NUCLEOTIDE_OPPOSITE_COLOR, 3, ArrowDirection.RIGHT),
              endNonCodingX + i, middle - 2, null);
        }
      } else {
        for (int i = endNonCodingWidth - 4; i > 0; i -= 7) {
          graphic.drawImage(this.createArrow(NUCLEOTIDE_OPPOSITE_COLOR, 3, ArrowDirection.LEFT),
              endNonCodingX + i, middle - 2, null);
        }
      }
    }

    // Draw introns.
    for (Intron intron : introns) {
      int intronX = (int) ((intron.getStart() - window().getStart()) * dnaLength);
      int intronY = middle - NUCLEOTIDE_INTRON_HEIGHT / 2;
      int intronWidth = (int) ((intron.getEnd() - intron.getStart()) * dnaLength);
      graphic.setPaint(NUCLEOTIDE_COLOR);
      graphic.fillRect(intronX, intronY, intronWidth, NUCLEOTIDE_INTRON_HEIGHT);
      if (nucleotide.getStrand().equals("+")) {
        for (int i = 3; i < intronWidth - 1; i += 7) {
          graphic.drawImage(this.createArrow(NUCLEOTIDE_COLOR, 3, ArrowDirection.RIGHT),
              intronX + i, middle - 2, null);
        }
      } else {
        for (int i = intronWidth - 4; i > 0; i -= 7) {
          graphic.drawImage(this.createArrow(NUCLEOTIDE_COLOR, 3, ArrowDirection.LEFT), intronX + i,
              middle - 2, null);
        }
      }
    }

    // Draw exons.
    for (Exon exon : exons) {
      int exonX = (int) ((exon.getStart() - window().getStart()) * dnaLength);
      int exonY = middle - NUCLEOTIDE_EXON_HEIGHT / 2;
      int exonWidth = Math.max((int) ((exon.getEnd() - exon.getStart()) * dnaLength), 1);
      graphic.setPaint(NUCLEOTIDE_COLOR);
      graphic.fillRect(exonX, exonY, exonWidth, NUCLEOTIDE_EXON_HEIGHT);
      if (nucleotide.getStrand().equals("+")) {
        for (int i = 3; i < exonWidth - 1; i += 7) {
          graphic.drawImage(this.createArrow(NUCLEOTIDE_OPPOSITE_COLOR, 3, ArrowDirection.RIGHT),
              exonX + i, middle - 2, null);
        }
      } else {
        for (int i = exonWidth - 4; i > 0; i -= 7) {
          graphic.drawImage(this.createArrow(NUCLEOTIDE_OPPOSITE_COLOR, 3, ArrowDirection.LEFT),
              exonX + i, middle - 2, null);
        }
      }
    }

    return image;
  }

  /**
   * Draws an arrow of the specified color and length pointing in the specified direction.
   *
   * @param color
   *          arrow's color
   * @param length
   *          arrow's length
   * @param direction
   *          arrow's direction
   * @return image containing arrow
   */
  private BufferedImage createArrow(Color color, int length, ArrowDirection direction) {
    BufferedImage image = new BufferedImage(length, length * 2 - 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());
    graphic.setPaint(color);

    switch (direction) {
      case RIGHT:
        graphic.drawLine(0, 0, length - 1, length - 1);
        graphic.drawLine(0, image.getHeight() - 1, length - 1, length - 1);
        break;
      case LEFT:
        graphic.drawLine(0, length - 1, length - 1, 0);
        graphic.drawLine(0, length - 1, length - 1, image.getHeight() - 1);
        break;
      default:
    }

    return image;
  }

  /**
   * Draws header for modules inside window.
   *
   * @return image containing header
   */
  private BufferedImage createModuleHeader() {
    BufferedImage image =
        new BufferedImage(getWidth(), MODULE_HEADER_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());
    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 11);
    graphic.setFont(font);
    graphic.setPaint(MODULE_COLOR);

    // Print text at center of header.
    String header = bundle.getString("module.header");
    LineMetrics headerMetrics = font.getLineMetrics(header, graphic.getFontRenderContext());
    Rectangle2D headerBounds = font.getStringBounds(header, graphic.getFontRenderContext());
    float headerX = (float) (image.getWidth() - headerBounds.getWidth()) / 2;
    float headerY =
        (NUCLEOTIDE_HEADER_HEIGHT - headerMetrics.getHeight()) / 2 + headerMetrics.getAscent();
    graphic.drawString(header, headerX, headerY);

    return image;
  }

  /**
   * Draws modules in an image.
   *
   * @return image containing modules
   */
  private BufferedImage createModules() {
    BufferedImage image = new BufferedImage(getWidth(),
        Math.max(modulesInWindow().size(), 1) * MODULE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());
    graphic.setPaint(MODULE_COLOR);

    int top = 0;
    for (Module module : modulesInWindow()) {
      graphic.drawImage(this.createModule(module), 0, top, null);
      top += MODULE_HEIGHT;
    }

    return image;
  }

  /**
   * Draws module in an image.
   *
   * @param module
   *          module
   * @return image containing module
   */
  private BufferedImage createModule(Module module) {
    BufferedImage image = new BufferedImage(getWidth(), MODULE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Draw module's name.
    graphic.drawImage(this.createModuleName(module), 0, 0, null);
    // Draw module's location.
    graphic.drawImage(this.createModuleLocation(module), LEFT_MARGIN, 0, null);

    return image;
  }

  /**
   * Draw module's name.
   *
   * @param module
   *          module
   * @return image containing module's name
   */
  private BufferedImage createModuleName(Module module) {
    BufferedImage image =
        new BufferedImage(LEFT_MARGIN, MODULE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    graphic.setPaint(this.getModuleColor(module));
    Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
    graphic.setFont(font);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    LineMetrics nameMetrics = font.getLineMetrics(module.getName(), graphic.getFontRenderContext());
    Rectangle2D nameBounds = font.getStringBounds(module.getName(), graphic.getFontRenderContext());
    int nameX = (int) (LEFT_MARGIN - PADDING - nameBounds.getWidth());
    int nameY = (int) (MODULE_HEIGHT / 2 - nameMetrics.getHeight() / 2 + nameMetrics.getAscent());
    graphic.drawString(module.getName(), nameX, nameY);

    return image;
  }

  /**
   * Returns color used to draw module.
   *
   * @param module
   *          module
   * @return color used to draw module
   */
  protected Color getModuleColor(Module module) {
    return MODULE_COLOR;
  }

  /**
   * Draw module's location.
   *
   * @param module
   *          module
   * @return image containing module's location
   */
  private BufferedImage createModuleLocation(Module module) {
    BufferedImage image =
        new BufferedImage(getWidth() - LEFT_MARGIN, MODULE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(new Color(255, 255, 255, 0));
    graphic.setPaint(this.getModuleColor(module));
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    int middle = MODULE_HEIGHT / 2;
    double dnaLength = (double) (image.getWidth()) / (window().getEnd() - window().getStart());

    // Draw module location.
    int moduleX = (int) ((module.getStart() - window().getStart()) * dnaLength);
    int moduleY = middle - MODULE_SQUARE_HEIGHT / 2;
    int moduleWidth = Math.max((int) ((module.getEnd() - module.getStart()) * dnaLength), 1);
    graphic.fillRect(moduleX, moduleY, moduleWidth, MODULE_SQUARE_HEIGHT);

    return image;
  }

  /**
   * Save module's image into a file.
   *
   * @param file
   *          file where to save image
   * @param fileType
   *          image type (eg. png, gif, jpeg)
   * @throws IOException
   *           could not write image to file
   */
  public void printPlotToFile(File file, String fileType) throws IOException {
    BufferedImage image = this.makePlot();
    ImageIO.write(image, fileType, file);
  }

  /**
   * Save module's image into an {@link OutputStream}.
   *
   * @param output
   *          {@link OutputStream} where to save image
   * @param fileType
   *          image type (eg. png, gif, jpeg)
   * @throws IOException
   *           could not write image to file
   */
  public void printPlotToOutputStream(OutputStream output, String fileType) throws IOException {
    BufferedImage image = this.makePlot();
    ImageIO.write(image, fileType, output);
  }
}
