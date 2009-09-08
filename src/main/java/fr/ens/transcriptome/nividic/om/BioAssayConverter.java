/*
 *                      Nividic development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the microarray platform
 * of the École Normale Supérieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.om;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements methods to convert BioAssay.
 * @author Laurent Jourdren
 */
public class BioAssayConverter {

  private static final int SPOT_DIAMETER = 60;

  private static void GPRizeAnnotation(final BioAssay bioAssay) {

    Annotation aOut = bioAssay.getAnnotation();
    aOut.setProperty("Type", "GenePix Results 3");
    aOut.setProperty("PMTGain", "800\t800");
    aOut.setProperty("ScanPower", "100\t100");
    aOut.setProperty("LaserPower", "1.71\t3.74");
    aOut.setProperty("ScanRegion", "80,3499,4312,6187");
    aOut.setProperty("PixelSize", "5");
    // aOut.setProperty("ImageOrigin", "400, 17495");
    // aOut.setProperty("JpegOrigin", "840, 18080");
    aOut.setProperty("ImageOrigin", "0, 0");
    aOut.setProperty("JpegOrigin", "0, 0");
    aOut.setProperty("Creator", "GenePix Pro 6.1.0.4");
  }

  /**
   * Convert a BioAssay from an Agilent to a GPR usable by Goulphar.
   * @param ba BioAssay to convert
   * @return a new bioAssay object
   */
  public final static BioAssay convertAgilentToGPR(final BioAssay ba) {

    // Output BioAssay
    BioAssay out = BioAssayFactory.createBioAssay();

    Annotation aIn = ba.getAnnotation();

    // Get the number of channels
    final int nChannels = Integer.parseInt(aIn.getProperty("Scan_NumChannels"));

    // Set Ids and descriptions
    out.setIds(ba.getIds().clone());
    out.setDescriptions(ba.getDescriptions().clone());

    GPRizeLocationsAndFlags(ba, out, nChannels == 1);
    GPRizeAnnotation(out);

    // Add Red and Greens foregrounds
    bioAssayFieldDoubleToInt(ba, out, BioAssay.FIELD_NAME_GREEN,
        BioAssay.FIELD_NAME_GREEN, 1);
    bioAssayFieldDoubleToInt(ba, out, nChannels == 1
        ? BioAssay.FIELD_NAME_GREEN : BioAssay.FIELD_NAME_RED,
        BioAssay.FIELD_NAME_RED, 1);

    // Add Red and Greens backgrounds
    bioAssayFieldDoubleToInt(ba, out, nChannels == 1
        ? "gBGMedianSignal" : "rBGMedianSignal", "B635 Median", 1); // red
    bioAssayFieldDoubleToInt(ba, out, "gBGMedianSignal", "B532 Median", 1);

    // bioAssayFieldDoubleToInt(ba, out, "PositionX", "X", 100);
    // bioAssayFieldDoubleToInt(ba, out, "PositionY", "Y", 100);

    // Set diameters
    int[] dia = new int[ba.size()];
    for (int j = 0; j < dia.length; j++)
      dia[j] = SPOT_DIAMETER;
    out.setDataFieldInt("Dia.", dia);

    return insertMissingSpots(out);
  }

  private static void bioAssayFieldDoubleToInt(final BioAssay in, BioAssay out,
      String inField, String outField, final double factor) {

    final double[] before = in.getDataFieldDouble(inField).clone();

    final int[] after = new int[before.length];

    for (int i = 0; i < before.length; i++)
      after[i] = (int) (before[i] * factor);

    out.setDataFieldInt(outField, after);
  }

  private static void GPRizeLocationsAndFlags(final BioAssay in,
      final BioAssay out, final boolean oneColor) {

    // Set the
    final int[] flags = new int[in.size()];
    final int[] locs = new int[in.size()];
    final int[] positionsX = new int[in.size()];
    final int[] positionsY = new int[in.size()];

    out.setLocations(locs);
    out.setFlags(flags);
    out.setDataFieldInt("X", positionsX);
    out.setDataFieldInt("Y", positionsY);

    SpotIterator si = in.iterator();

    while (si.hasNext()) {

      si.next();
      final int i = si.getIndex();
      int loc = si.getLocation();
      locs[i] = BioAssayUtils.setMetaColumn(loc, 1);
      positionsX[i] = si.getColumn() * 100;
      positionsY[i] = si.getRow() * 100;

      // Test If the spot is standard spot
      final int controlType = si.getDataFieldInt("ControlType");

      if (controlType != 0) {
        flags[i] = -75;
        continue;
      }

      // Test if the spot was manually flag
      final int isManualFlag = si.getDataFieldInt("IsManualFlag");
      if (isManualFlag == 1) {
        flags[i] = -100;
        continue;
      }

      final int gIsFeatPopnOL = si.getDataFieldInt("gIsFeatPopnOL");
      final int rIsFeatPopnOL =
          si.getDataFieldInt(oneColor ? "gIsFeatPopnOL" : "rIsFeatPopnOL");

      final int gIsFeatNonUnifOL = si.getDataFieldInt("gIsFeatNonUnifOL");
      final int rIsFeatNonUnifOL =
          si
              .getDataFieldInt(oneColor
                  ? "gIsFeatNonUnifOL" : "rIsFeatNonUnifOL");

      if (gIsFeatPopnOL == 1
          || rIsFeatPopnOL == 1 || gIsFeatNonUnifOL == 1
          || rIsFeatNonUnifOL == 1) {
        flags[i] = -50;
        continue;
      }
    }

  }

  private static BioAssay insertMissingSpots(final BioAssay ba) {

    final BioAssay out = BioAssayFactory.createBioAssay();

    // Set Annotation
    out.getAnnotation().addProperties(ba.getAnnotation());

    final Map<String, Integer> spotsLocations = new HashMap<String, Integer>();
    int maxColumn = -1;
    int maxRow = -1;

    // Find the dimensions of the array
    SpotIterator si = ba.iterator();
    while (si.hasNext()) {
      si.next();
      final int column = si.getColumn();
      final int row = si.getRow();

      if (column > maxColumn)
        maxColumn = column;
      if (row > maxRow)
        maxRow = row;

      spotsLocations.put(column + "_" + row, si.getIndex());
    }

    // Transform coordinates
    final Map<String, Integer> translatedSpotsLocations =
        changeCoordinates(spotsLocations);

    maxColumn = -1;
    maxRow = -1;

    // Find dimension of the translated array
    for (Map.Entry<String, Integer> e : translatedSpotsLocations.entrySet()) {

      final String key = e.getKey();
      final String[] coord = key.split("_");
      final int col = Integer.parseInt(coord[0]);
      final int row = Integer.parseInt(coord[1]);

      if (col > maxColumn)
        maxColumn = col;
      if (row > maxRow)
        maxRow = row;
    }

    final int newSize = maxRow * maxColumn;
    final String[] fields = ba.getFields();

    // Resize all the fields
    for (int i = 0; i < fields.length; i++) {

      final String field = fields[i];
      switch (ba.getFieldType(fields[i])) {

      case BioAssayBase.DATATYPE_DOUBLE:

        final double[] newArrayDouble = new double[newSize];
        // System.arraycopy(ba.getDataFieldDouble(field), 0, newArrayDouble, 0,
        // oldSize);
        out.setDataFieldDouble(field, newArrayDouble);
        break;

      case BioAssayBase.DATATYPE_INTEGER:

        final int[] newArrayInteger = new int[newSize];
        // System.arraycopy(ba.getDataFieldInt(field), 0, newArrayInteger, 0,
        // oldSize);
        out.setDataFieldInt(field, newArrayInteger);
        break;

      case BioAssayBase.DATATYPE_STRING:

        final String[] newArrayString = new String[newSize];
        // System.arraycopy(ba.getDataFieldString(field), 0, newArrayString, 0,
        // oldSize);
        out.setDataFieldString(field, newArrayString);
        break;

      default:
        break;
      }
    }

    int count = 0;

    // Insert the missing spots

    for (int i = 1; i <= maxRow; i++)
      for (int j = 1; j <= maxColumn; j++) {

        final String key = j + "_" + i;

        // Test if the spot doesn't exist
        if (!translatedSpotsLocations.containsKey(key)) {

          for (int k = 0; k < fields.length; k++) {

            final String field = fields[k];
            int intValue = 0;
            double doubleValue = 0.0;
            String stringValue = "";

            if (BioAssay.FIELD_NAME_ID.equals(field)) {

              stringValue = "NA";

            } else if (BioAssayBase.FIELD_NAME_LOCATION.equals(field)) {

              // The field is the location field
              int loc = 0;
              loc = BioAssayUtils.setMetaColumn(loc, 1);
              loc = BioAssayUtils.setRow(loc, i);
              loc = BioAssayUtils.setColumn(loc, j);
              intValue = loc;

            } else if (BioAssay.FIELD_NAME_FLAG.equals(field)) {

              // The the flag as absent
              intValue = -75;

            } else if ("X".equals(field)) {

              // The X position
              intValue = j * 100;

            } else if ("Y".equals(field)) {

              // The Y position
              intValue = i * 100;
            } else if ("Dia.".equals(field)) {

              // The Y position
              intValue = SPOT_DIAMETER;
            }

            switch (out.getFieldType(fields[k])) {

            case BioAssayBase.DATATYPE_DOUBLE:
              out.getDataFieldDouble(field)[count] = doubleValue;
              break;

            case BioAssayBase.DATATYPE_INTEGER:
              out.getDataFieldInt(field)[count] = intValue;
              break;

            case BioAssayBase.DATATYPE_STRING:
              out.getDataFieldString(field)[count] = stringValue;
              break;

            default:
              break;
            }
          }
        } else
          for (int k = 0; k < fields.length; k++) {

            final String field = fields[k];
            switch (out.getFieldType(fields[k])) {

            case BioAssayBase.DATATYPE_DOUBLE:
              out.getDataFieldDouble(field)[count] =
                  ba.getDataFieldDouble(field)[translatedSpotsLocations
                      .get(key)];
              break;

            case BioAssayBase.DATATYPE_INTEGER:

              if (BioAssayBase.FIELD_NAME_LOCATION.equals(field)) {

                // The field is the location field
                int loc = 0;
                loc = BioAssayUtils.setMetaColumn(loc, 1);
                loc = BioAssayUtils.setRow(loc, i);
                loc = BioAssayUtils.setColumn(loc, j);
                out.getDataFieldInt(field)[count] = loc;
              } else if ("X".equals(field)) {

                // The X position
                out.getDataFieldInt(field)[count] = j * 100;

              } else if ("Y".equals(field)) {

                // The Y position
                out.getDataFieldInt(field)[count] = i * 100;
              } else
                out.getDataFieldInt(field)[count] =
                    ba.getDataFieldInt(field)[translatedSpotsLocations.get(key)];
              break;

            case BioAssayBase.DATATYPE_STRING:
              out.getDataFieldString(field)[count] =
                  ba.getDataFieldString(field)[translatedSpotsLocations
                      .get(key)];
              break;

            default:
              break;
            }

          }

        count++;
      }

    return out;
  }

  private static Map<String, Integer> changeCoordinates(Map<String, Integer> in) {

    // Output result
    final Map<String, Integer> out = new HashMap<String, Integer>(in.size());

    int maxCol = -1;
    int maxRow = -1;

    // Find dimension of the array
    for (Map.Entry<String, Integer> e : in.entrySet()) {

      final String key = e.getKey();
      final String[] coord = key.split("_");
      final int col = Integer.parseInt(coord[0]);
      final int row = Integer.parseInt(coord[1]);

      if (col > maxCol)
        maxCol = col;
      if (row > maxRow)
        maxRow = row;
    }

    maxCol++;
    maxRow++;

    // Change coordinates
    for (Map.Entry<String, Integer> e : in.entrySet()) {

      final String key = e.getKey();
      final String[] coord = key.split("_");
      final int col1 = Integer.parseInt(coord[0]);
      final int row1 = Integer.parseInt(coord[1]);

      // Do the translation
      final int col3 = maxRow - row1;
      final int row3 = maxCol - col1;

      // Change staggered rows
      final int row4 = row3 * 2 + (col3 % 2 == 0 ? 0 : -1);
      final int col4 = col3 / 2 + (col3 % 2 == 0 ? 0 : 1);

      out.put(col4 + "_" + row4, in.get(col1 + "_" + row1));
    }

    return out;
  }

}
