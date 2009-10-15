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
 * of the �cole Normale Sup�rieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.om.io;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * This class define a backend for BioAssayTabularWriter that create a xls file.
 * @author Laurent Jourdren
 */
class BioAssayTabularWriterOOXMLBackend implements BioAssayTabularWriterBackend {

  private BioAssayWriter writer;

  private XSSFWorkbook wb;
  private XSSFSheet sheet;

  /**
   * Set the BioAssayWriter.
   * @param writer Writer to set
   */
  public void setBioAssayWriter(final BioAssayWriter writer) {

    this.writer = writer;
  }

  /**
   * Write the data.
   * @throws NividicIOException if an error occurs while writing data
   */
  public void writeData() throws NividicIOException {

    final BioAssayWriter writer = this.writer;

    final int countCol = writer.getColumnCount();
    final int countRow = writer.getRowColumn();
    final Translator translator = writer.getTranslator();
    final String[] translatorFields;
    final String[] ids =
        writer.getBioAssay() == null ? null : writer.getBioAssay().getIds();

    if (translator == null)
      translatorFields = null;
    else
      translatorFields = translator.getFields();

    try {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < countRow; i++) {

        XSSFRow row = sheet.createRow(i + 1);

        for (int j = 0; j < countCol; j++) {

          String value = writer.getData(i, j);
          if (value == null)
            continue;

          switch (writer.getFieldType(j)) {
          case BioAssay.DATATYPE_STRING:
            row.createCell((short) j).setCellValue(
                new XSSFRichTextString(value));
            break;

          case BioAssay.DATATYPE_INTEGER:
            row.createCell((short) j).setCellValue(Integer.parseInt(value));
            break;

          case BioAssay.DATATYPE_DOUBLE:

            final double vd = Double.parseDouble(value);
            if (!Double.isNaN(vd))
              row.createCell((short) j).setCellValue(vd);
            break;

          // Locations type
          default:
            sb.append(value);
            break;
          }

        }

        if (translatorFields != null)
          for (int j = 0; j < translatorFields.length; j++) {

            final String field = translatorFields[j];
            final String value;

            if (ids == null)
              value = null;
            else
              value = translator.translateField(ids[i], field);

            String link;

            if (value == null || !translator.isLinkInfo(field))
              link = null;
            else
              link = translator.getLinkInfo(value, field);

            if (value != null) {

              final XSSFCell cell = row.createCell(countCol + j);

              if (link == null)
                cell.setCellValue(new XSSFRichTextString(value));
              else
                cell.setCellFormula("HYPERLINK(\""
                    + link + "\",\"" + value + "\")");

            }

          }
      }

      OutputStream os = writer.getOutputStream();
      wb.write(os);
      os.close();

    } catch (IOException e) {
      throw new NividicIOException("Error while writing stream : "
          + e.getMessage());
    }

  }

  /**
   * Write the header.
   * @throws NividicIOException if an error occurs while writing header
   */
  public void writeHeaders() throws NividicIOException {

    final BioAssayWriter writer = this.writer;

    this.wb = new XSSFWorkbook();
    this.sheet = wb.createSheet("new sheet");

    XSSFRow row = sheet.createRow((short) 0);

    // Create a new font and alter it.
    XSSFFont font = wb.createFont();
    font.setItalic(true);

    // Fonts are set into a style so create a new one to use.
    XSSFCellStyle style = wb.createCellStyle();
    style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
    style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
    style.setFont(font);

    int count = 0;

    for (int i = 0; i < writer.getColumnCount(); i++) {

      final XSSFCell cell = row.createCell(count++);
      cell.setCellValue(new XSSFRichTextString(writer.getFieldName(i)));
      cell.setCellStyle(style);
    }

    if (writer.getTranslator() != null) {

      String[] fields = writer.getTranslator().getFields();
      if (fields != null)
        for (int i = 0; i < fields.length; i++) {

          final XSSFCell cell = row.createCell(count++);
          cell.setCellValue(new XSSFRichTextString(fields[i]));
          cell.setCellStyle(style);
        }
    }

  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param writer Writer to set
   */
  public BioAssayTabularWriterOOXMLBackend(final BioAssayWriter writer) {

    setBioAssayWriter(writer);
  }

}
