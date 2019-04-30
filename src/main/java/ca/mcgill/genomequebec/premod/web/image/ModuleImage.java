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

import ca.mcgill.genomequebec.premod.business.Module;
import ca.mcgill.genomequebec.premod.business.Unit;
import ca.mcgill.genomequebec.premod.business.UnitOccurrence;
import ca.mcgill.genomequebec.premod.service.UnitOccurrenceService;
import ca.mcgill.genomequebec.premod.service.UnitService;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 * Generates an image of units' occurrences inside a module.
 */
public class ModuleImage {
  /**
   * Width of image.
   */
  private static final int IMAGE_WIDTH = 515;
  /**
   * Height of the title of image.
   */
  private static final int TITLE_HEIGHT = 30;
  /**
   * Left margin that contains text information before the information line is drawn.
   */
  private static final int LEFT_MARGIN = 115;
  /**
   * Right margin that contains text information before the information line is drawn.
   */
  private static final int RIGHT_MARGIN = 75;
  /**
   * Height of the Unit header of image.
   */
  private static final int UNIT_HEADER_HEIGHT = 35;
  /**
   * Number of additionnal Units to show in graphic. The Units that can be added must be non-tagging
   * Units for the Module.
   */
  public static final int MAX_ADDITIONNAL_UNITS = 5;
  /**
   * Height of a Unit information.
   */
  private static final int UNIT_HEIGHT = 30;
  /**
   * Height of a UnitOccurrence scare.
   */
  private static final int UNIT_OCCURENCE_HEIGHT = 10;
  /**
   * Padding to use between elements.
   */
  private static final int PADDING = 10;
  /**
   * Colors for the Unit squares.
   */
  private static final List<Color> UNIT_COLORS;

  static {
    List<Color> colors = new ArrayList<>();
    // Pink
    colors.add(ColorHelper.getInternetColor("FF6699"));
    // Blue
    colors.add(ColorHelper.getInternetColor("0099CC"));
    // Yellow
    colors.add(ColorHelper.getInternetColor("FFCC33"));
    // Green
    colors.add(ColorHelper.getInternetColor("66CC66"));
    // Red
    colors.add(ColorHelper.getInternetColor("FF0033"));
    UNIT_COLORS = colors;
  }

  /**
   * Colors for the additionnal Units square.
   */
  private static final List<Color> ADDITIONNAL_UNIT_COLORS;

  static {
    List<Color> colors = new ArrayList<>();
    // Dark blue
    colors.add(ColorHelper.getInternetColor("3264AD"));
    // Orange
    colors.add(ColorHelper.getInternetColor("F1582F"));
    // Purple
    colors.add(ColorHelper.getInternetColor("8491BE"));
    // Light green (turquoise)
    colors.add(ColorHelper.getInternetColor("9AD1BB"));
    // Light brown
    colors.add(ColorHelper.getInternetColor("A18577"));
    ADDITIONNAL_UNIT_COLORS = colors;
  }

  /**
   * Resource bundle used to get text.
   */
  private final ResourceBundle bundle;
  /**
   * Module for which to create image.
   */
  private final Module module;
  /**
   * Module's best units.
   */
  private final List<Unit> bestUnits;
  /**
   * Additional unit to add to image.
   */
  private final List<Unit> additionalUnits;
  /**
   * Extra unit information.
   */
  private final Map<Unit, UnitService.Extra> unitExtras;
  /**
   * Occurrences of all units.
   */
  private final Map<Unit, List<UnitOccurrence>> unitOccurrences;
  /**
   * Used to get occurrences of unit in module.
   */
  private final UnitOccurrenceService unitOccurrenceService;
  /**
   * The generated image.
   */
  private BufferedImage image;

  /**
   * Creates module image.
   *
   * @param unitService
   *          unit service
   * @param unitOccurrenceService
   *          unit occurrence service
   * @param module
   *          module
   * @param additionalUnits
   *          additional units
   * @param locale
   *          locale
   */
  public ModuleImage(UnitService unitService, UnitOccurrenceService unitOccurrenceService,
      Module module, Collection<Unit> additionalUnits, Locale locale) {
    this.bundle = ResourceBundle.getBundle(this.getClass().getName(), locale);
    this.module = module;
    this.unitOccurrenceService = unitOccurrenceService;
    this.bestUnits = unitService.best(module);
    Set<Unit> uniqueUnits = new HashSet<>();
    for (Unit unit : bestUnits) {
      uniqueUnits.add(unit);
    }
    if (additionalUnits == null) {
      additionalUnits = new LinkedList<>();
    } else {
      additionalUnits = new LinkedList<>(additionalUnits);
      if (additionalUnits.size() > MAX_ADDITIONNAL_UNITS) {
        additionalUnits = ((LinkedList<Unit>) additionalUnits).subList(0, MAX_ADDITIONNAL_UNITS);
      }
    }
    this.additionalUnits = new LinkedList<>(additionalUnits);
    this.unitExtras = unitService.extras(module);
    this.unitOccurrences = unitOccurrenceService.all(module);
    this.makePlot();
  }

  /**
   * Create graphic for a module.
   */
  private void makePlot() {
    // Initialising buffered image.
    int imageHeight = this.getBaseHeight() + 2;
    int imageWidth = IMAGE_WIDTH + 2;
    image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

    // Set a white background and black border.
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(Color.WHITE);
    graphic.setPaint(Color.BLACK);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Title to graphic.
    int titleTop = 1;
    graphic.drawImage(this.createTitle(), 1, titleTop, null);

    // Add summary for module.
    int moduleSummaryTop = titleTop + TITLE_HEIGHT;
    graphic.drawImage(this.createModuleSummary(), 1, moduleSummaryTop, null);

    // Add unit header before printing unit information.
    int unitHeaderTop = moduleSummaryTop + this.getModuleSummaryHeight();
    graphic.drawImage(this.createUnitHeader(), 1, unitHeaderTop, null);

    // Add all unit information.
    int unitsTop = unitHeaderTop + UNIT_HEADER_HEIGHT;
    graphic.drawImage(this.createUnits(), 1, unitsTop, null);

    // Add a black border.
    graphic.setPaint(Color.BLACK);
    graphic.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);

    graphic.dispose();
    image.flush();
  }

  private int getBaseHeight() {
    int height = TITLE_HEIGHT;
    height += getModuleSummaryHeight();
    height += UNIT_HEADER_HEIGHT;
    height += Math.max(bestUnits.size() + additionalUnits.size(), 1) * UNIT_HEIGHT;
    height += PADDING;
    return height;
  }

  private int getModuleSummaryHeight() {
    return 10 + (bestUnits.size() + additionalUnits.size()) * UNIT_OCCURENCE_HEIGHT;
  }

  /**
   * Creates title image to include in module image.
   */
  private BufferedImage createTitle() {
    BufferedImage image = new BufferedImage(IMAGE_WIDTH, TITLE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(Color.LIGHT_GRAY);
    graphic.setPaint(Color.BLACK);
    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    graphic.setFont(font);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Print text at center of title.
    String title = bundle.getString("title");
    title = MessageFormat.format(title, module.getName(), module.getLength());
    LineMetrics titleMetrics = font.getLineMetrics(title, graphic.getFontRenderContext());
    Rectangle2D titleBounds = font.getStringBounds(title, graphic.getFontRenderContext());
    int textX = IMAGE_WIDTH / 2 - (int) titleBounds.getWidth() / 2;
    int textY =
        TITLE_HEIGHT / 2 - (int) titleMetrics.getHeight() / 2 + (int) titleMetrics.getAscent();
    graphic.drawString(title, textX, textY);
    return image;
  }

  /**
   * Create an image containing all occurrences of module's units.
   */
  private BufferedImage createModuleSummary() {
    BufferedImage image =
        new BufferedImage(IMAGE_WIDTH, this.getModuleSummaryHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(Color.WHITE);
    graphic.setPaint(Color.BLACK);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Actual valid positions.
    Insets occurencesInsets =
        new Insets(0, LEFT_MARGIN, this.getModuleSummaryHeight(), image.getWidth() - RIGHT_MARGIN);

    // Print module line in middle.
    graphic.setPaint(Color.BLACK);
    graphic.fillRect(occurencesInsets.left,
        (occurencesInsets.bottom - occurencesInsets.top) / 2 + occurencesInsets.top,
        occurencesInsets.right - occurencesInsets.left, 1);

    // Add occurrences.
    Map<UnitOccurrence, Insets> positions = computePositions(occurencesInsets);
    int index = -1;
    for (Unit unit : bestUnits) {
      index++;
      graphic.setColor(UNIT_COLORS.get(index));
      for (UnitOccurrence occurrence : this.unitOccurrences.get(unit)) {
        Insets occurenceInsets = positions.get(occurrence);
        if (occurenceInsets != null) {
          graphic.draw3DRect(occurenceInsets.left, occurenceInsets.top,
              occurenceInsets.right - occurenceInsets.left,
              occurenceInsets.bottom - occurenceInsets.top, true);
          graphic.fill3DRect(occurenceInsets.left + 1, occurenceInsets.top + 1,
              occurenceInsets.right - occurenceInsets.left - 1,
              occurenceInsets.bottom - occurenceInsets.top - 1, true);
        }
      }
    }

    index = -1;
    for (Unit unit : additionalUnits) {
      index++;
      graphic.setColor(ADDITIONNAL_UNIT_COLORS.get(index));
      for (UnitOccurrence occurrence : this.unitOccurrences.get(unit)) {
        Insets occurenceInsets = positions.get(occurrence);
        if (occurenceInsets != null) {
          graphic.draw3DRect(occurenceInsets.left, occurenceInsets.top,
              occurenceInsets.right - occurenceInsets.left,
              occurenceInsets.bottom - occurenceInsets.top, true);
          graphic.fill3DRect(occurenceInsets.left + 1, occurenceInsets.top + 1,
              occurenceInsets.right - occurenceInsets.left - 1,
              occurenceInsets.bottom - occurenceInsets.top - 1, true);
        }
      }
    }

    return image;
  }

  /**
   * Computes position of all occurrence of all units to show in module summary.
   *
   * @param insets
   *          module's summary location in image
   * @return position of all occurrence of all units to show in module summary
   */
  private Map<UnitOccurrence, Insets> computePositions(Insets insets) {
    Map<UnitOccurrence, Insets> positions = new HashMap<>();
    List<Unit> units = new LinkedList<>();
    units.addAll(bestUnits);
    units.addAll(additionalUnits);
    int middleHeight = (insets.bottom - insets.top) / 2 + insets.top;
    double nuclotideLength = (double) (insets.right - insets.left) / module.getLength();

    if (!units.isEmpty()) {
      // Unit that is in the middle.
      int middleIndex = (units.size() - 1) / 2;
      Unit middleUnit = units.get(middleIndex);
      List<UnitOccurrence> middleOccurrences = unitOccurrences.get(middleUnit);
      for (UnitOccurrence unitOccurrence : middleOccurrences) {
        int left =
            (int) ((unitOccurrence.getStart() - module.getStart()) * nuclotideLength) + insets.left;
        int right =
            (int) ((unitOccurrence.getEnd() - module.getStart()) * nuclotideLength) + insets.left;
        int top = middleHeight - UNIT_OCCURENCE_HEIGHT / 2;
        int bottom = top + UNIT_OCCURENCE_HEIGHT;
        Insets occurrenceInsets = new Insets(top, left, bottom, right);
        positions.put(unitOccurrence, occurrenceInsets);
      }

      // Units on top.
      List<Unit> topUnits = new LinkedList<>(units.subList(0, middleIndex));
      Collections.reverse(topUnits);
      for (Unit unit : topUnits) {
        for (UnitOccurrence unitOccurrence : unitOccurrences.get(unit)) {
          // Position if there is no overlap.
          int left = (int) ((unitOccurrence.getStart() - module.getStart()) * nuclotideLength)
              + insets.left;
          int right =
              (int) ((unitOccurrence.getEnd() - module.getStart()) * nuclotideLength) + insets.left;
          int top = middleHeight - UNIT_OCCURENCE_HEIGHT / 2;
          int bottom = top + UNIT_OCCURENCE_HEIGHT;
          Insets occurrenceInsets = new Insets(top, left, bottom, right);
          while (overlap(occurrenceInsets, positions.values())) {
            // Move occurrence up.
            occurrenceInsets.top -= UNIT_OCCURENCE_HEIGHT;
            occurrenceInsets.bottom -= UNIT_OCCURENCE_HEIGHT;
          }
          positions.put(unitOccurrence, occurrenceInsets);
        }
      }

      // Units on bottom.
      List<Unit> bottomUnits = new LinkedList<>(units.subList(middleIndex + 1, units.size()));
      for (Unit unit : bottomUnits) {
        for (UnitOccurrence unitOccurrence : unitOccurrences.get(unit)) {
          // Position if there is no overlap.
          int left = (int) ((unitOccurrence.getStart() - module.getStart()) * nuclotideLength)
              + insets.left;
          int right =
              (int) ((unitOccurrence.getEnd() - module.getStart()) * nuclotideLength) + insets.left;
          int top = middleHeight - UNIT_OCCURENCE_HEIGHT / 2;
          int bottom = top + UNIT_OCCURENCE_HEIGHT;
          Insets occurrenceInsets = new Insets(top, left, bottom, right);
          while (overlap(occurrenceInsets, positions.values())) {
            // Move occurrence down.
            occurrenceInsets.top += UNIT_OCCURENCE_HEIGHT;
            occurrenceInsets.bottom += UNIT_OCCURENCE_HEIGHT;
          }
          positions.put(unitOccurrence, occurrenceInsets);
        }
      }
    }

    return positions;
  }

  /**
   * Returns true if insets overlap any of the insets inside collection, false otherwise.
   *
   * @param o1
   *          first insets
   * @param o2
   *          second insets
   * @return true if insets overlap any of the insets inside collection, false otherwise
   */
  private boolean overlap(Insets insets, Collection<Insets> collection) {
    for (Insets test : collection) {
      if (overlap(insets, test)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns true if both insets overlap, false otherwise.
   *
   * @param o1
   *          first insets
   * @param o2
   *          second insets
   * @return true if both insets overlap, false otherwise
   */
  private boolean overlap(Insets o1, Insets o2) {
    boolean overlap = true;
    overlap &= o2.right > o1.left && o2.left < o1.right;
    overlap &= o2.bottom > o1.top && o2.top < o1.bottom;
    return overlap;
  }

  /**
   * Draw a header for units.
   */
  private BufferedImage createUnitHeader() {
    BufferedImage image =
        new BufferedImage(IMAGE_WIDTH, UNIT_HEADER_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(Color.LIGHT_GRAY);
    Font headerFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    graphic.setFont(headerFont);
    graphic.setPaint(Color.BLACK);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    // Add column header for unit name.
    String nameHeader = bundle.getString("unit.header.unit");
    Rectangle2D nameHeaderBounds =
        headerFont.getStringBounds(nameHeader, graphic.getFontRenderContext());
    int nameHeaderX = LEFT_MARGIN - PADDING - (int) nameHeaderBounds.getWidth();
    int nameHeaderY = (int) nameHeaderBounds.getHeight();
    graphic.drawString(nameHeader, nameHeaderX, nameHeaderY);

    // Add column header for unit total score.
    String totalScoreHeader = bundle.getString("unit.header.score");
    {
      String[] words = totalScoreHeader.split("\\s");
      int lineHeight = 0;
      int index = 0;
      while (index < words.length) {
        int width = RIGHT_MARGIN - PADDING * 2;
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(words[index++]);
        while (index < words.length
            && headerFont.getStringBounds(lineBuilder.toString() + " " + words[index],
                graphic.getFontRenderContext()).getWidth() < width) {
          lineBuilder.append(" ");
          lineBuilder.append(words[index++]);
        }
        String line = lineBuilder.toString();
        Rectangle2D lineBounds = headerFont.getStringBounds(line, graphic.getFontRenderContext());
        int lineX = IMAGE_WIDTH - RIGHT_MARGIN + PADDING;
        int lineY = lineHeight + (int) lineBounds.getHeight();
        graphic.drawString(line, lineX, lineY);
        lineHeight += (int) lineBounds.getHeight();
      }
    }

    return image;
  }

  /**
   * Draw a line for each unit and places it's occurrences on the line.
   */
  private BufferedImage createUnits() {
    BufferedImage image = new BufferedImage(IMAGE_WIDTH,
        Math.max(bestUnits.size() + additionalUnits.size(), 1) * UNIT_HEIGHT,
        BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(Color.WHITE);
    graphic.setPaint(Color.BLACK);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    int unitTop = 0;
    int count = -1;
    for (Unit unit : bestUnits) {
      count++;
      graphic.drawImage(this.createUnit(unit, UNIT_COLORS.get(count)), 0, unitTop, null);
      unitTop += UNIT_HEIGHT;
    }
    count = -1;
    for (Unit unit : additionalUnits) {
      count++;
      graphic.drawImage(this.createUnit(unit, ADDITIONNAL_UNIT_COLORS.get(count)), 0, unitTop,
          null);
      unitTop += UNIT_HEIGHT;
    }

    return image;
  }

  /**
   * Draws unit and it's occurrences in an image.
   * <p>
   * This method will:
   * </p>
   * <ol>
   * <li>Write unit id in the left margin.</li>
   * <li>Draw a line and places unit's occurrences on it.</li>
   * <li>Write total unit score in right margin.</li>
   * </ol>
   *
   * @param unit
   *          unit to add to image
   * @param occurrenceColor
   *          color of the occurrences
   */
  private BufferedImage createUnit(Unit unit, Color occurrenceColor) {
    BufferedImage image = new BufferedImage(IMAGE_WIDTH, UNIT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphic = image.createGraphics();
    graphic.setBackground(Color.WHITE);
    graphic.setPaint(Color.BLACK);
    Font unitFont = new Font(Font.SANS_SERIF, Font.PLAIN, 9);
    graphic.setFont(unitFont);
    graphic.clearRect(0, 0, image.getWidth(), image.getHeight());

    int middleHeight = UNIT_HEIGHT / 2;

    // Print unit name and id in left margin.
    String unitName = MessageFormat.format(bundle.getString("unit.name"), unit.getName());
    Rectangle2D unitNameBounds = unitFont.getStringBounds(unitName, graphic.getFontRenderContext());
    int unitNameX = LEFT_MARGIN - PADDING - (int) unitNameBounds.getWidth();
    int unitNameY = middleHeight;
    graphic.drawString(unitName, unitNameX, unitNameY);
    String unitId = MessageFormat.format(bundle.getString("unit.id"), unit.getId());
    Rectangle2D unitIdBounds = unitFont.getStringBounds(unitId, graphic.getFontRenderContext());
    int unitIdX = LEFT_MARGIN - PADDING - (int) unitIdBounds.getWidth();
    int unitIdY = middleHeight + (int) unitNameBounds.getHeight();
    graphic.drawString(unitId, unitIdX, unitIdY);

    // Print score in right margin.
    UnitService.Extra unitExtra = unitExtras.get(unit);
    int score = unitExtra.getMinScore() != null ? unitExtra.getMinScore() : 0;
    String scoreText = String.valueOf(score);
    Rectangle2D scoreBounds = unitFont.getStringBounds(scoreText, graphic.getFontRenderContext());
    graphic.drawString(scoreText, IMAGE_WIDTH - RIGHT_MARGIN / 2 - (int) scoreBounds.getWidth() / 2,
        middleHeight + (int) scoreBounds.getHeight() / 2);

    // Print main line at center of header in black.
    int centerWidth = IMAGE_WIDTH - LEFT_MARGIN - RIGHT_MARGIN;
    graphic.fillRect(LEFT_MARGIN, middleHeight, centerWidth, 1);

    // Get length of a nucleotide base in pixel.
    double nuclotideLength = (double) (centerWidth) / module.getLength();

    // Print occurrences on line.
    List<UnitOccurrence> unitOccurrences = unitOccurrenceService.all(module, unit);
    for (UnitOccurrence unitOccurrence : unitOccurrences) {
      // Position the occurrence on line.
      int left =
          (int) ((unitOccurrence.getStart() - module.getStart()) * nuclotideLength) + LEFT_MARGIN;
      int right =
          (int) ((unitOccurrence.getEnd() - module.getStart()) * nuclotideLength) + LEFT_MARGIN;
      int top = middleHeight - UNIT_OCCURENCE_HEIGHT / 2;
      int bottom = top + UNIT_OCCURENCE_HEIGHT;
      graphic.setPaint(occurrenceColor);
      graphic.draw3DRect(left, top, right - left, bottom - top, true);
      graphic.fill3DRect(left + 1, top + 1, right - left - 1, bottom - top - 1, true);
    }

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
    ImageIO.write(image, fileType, output);
  }

  public int getHeight() {
    return image.getHeight();
  }

  public int getWidth() {
    return image.getWidth();
  }
}
