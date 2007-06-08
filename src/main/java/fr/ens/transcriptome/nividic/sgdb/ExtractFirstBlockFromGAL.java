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

package fr.ens.transcriptome.nividic.sgdb;

import java.util.Arrays;

import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.ArrayBlock;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.GenepixArrayList;
import fr.ens.transcriptome.nividic.om.filters.BioAssayFilter;
import fr.ens.transcriptome.nividic.om.filters.BioAssayGenericIntegerFieldFilter;

public class ExtractFirstBlockFromGAL {

  private static final String DESIGN_ID_ANNOTATION = "DesignID";

  public static final BioAssay extractFirstGALBlock(final BioAssay bioAssay) {

    BioAssayFilter baf = new BioAssayGenericIntegerFieldFilter() {

      @Override
      public String getFieldToFilter() {

        return BioAssay.FIELD_NAME_LOCATION;
      }

      @Override
      public boolean test(final int value) {

        return BioAssayUtils.getMetaColumn(value) == 1;
      }

      public String getParameterInfo() {

        return "Filter first block";
      }

    };

    BioAssay ba1 = bioAssay.filter(baf);
    BioAssay result = BioAssayFactory.createBioAssay();

    GenepixArrayList gpal1 = new GenepixArrayList(bioAssay);
    GenepixArrayList gpal2 = new GenepixArrayList(result);

    gpal2.setType(gpal1.getType());
    gpal2.setBlockType(gpal1.getBlockType());
    gpal2.setUrl(gpal1.getUrl());
    gpal2.setSupplier(gpal1.getSupplier());
    gpal2.setArrayName(gpal1.getArrayName());
    result.getAnnotation().setProperty(DESIGN_ID_ANNOTATION,
        bioAssay.getAnnotation().getProperty(DESIGN_ID_ANNOTATION));

    ArrayBlock[] blocks = gpal1.getBlocks();

    gpal2.setBlocks(new ArrayBlock[] {blocks[0]});

    String[] fields = ba1.getFields();

    for (int i = 0; i < fields.length; i++) {
      final String field = fields[i];
      int type = ba1.getFieldType(field);

      switch (type) {

      case BioAssay.DATATYPE_INTEGER:
        result.setDataFieldInt(field, ba1.getDataFieldInt(field));
        break;

      case BioAssay.DATATYPE_DOUBLE:
        result.setDataFieldDouble(field, ba1.getDataFieldDouble(field));
        break;

      case BioAssay.DATATYPE_STRING:
        result.setDataFieldString(field, ba1.getDataFieldString(field));
        break;

      default:
        break;

      }

    }

    return result;
  }
}
