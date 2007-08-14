/**
 * SlideDescriptionImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package fr.ens.transcriptome.nividic.sgdb.lims.old.ws;

public class SlideDescription  implements java.io.Serializable {
    private java.lang.String serialNumber;
    private java.lang.String slideOrder;
    private java.lang.String batchName;
    private int listId;
    private java.util.Date spottingDate;
    private java.lang.String spottingBy;
    private int tipNumber;
    private int topMargin;
    private int bottomMargin;
    private int rightMargin;
    private int leftMargin;
    private int slideWidth;
    private int slideHeight;
    private java.lang.String slideImage;
    private int spottingOrder;
    private java.lang.String slideImageURL;

    public SlideDescription() {
    }

    public SlideDescription(
           java.lang.String serialNumber,
           java.lang.String slideOrder,
           java.lang.String batchName,
           int listId,
           java.util.Date spottingDate,
           java.lang.String spottingBy,
           int tipNumber,
           int topMargin,
           int bottomMargin,
           int rightMargin,
           int leftMargin,
           int slideWidth,
           int slideHeight,
           java.lang.String slideImage,
           int spottingOrder,
           java.lang.String slideImageURL) {
           this.serialNumber = serialNumber;
           this.slideOrder = slideOrder;
           this.batchName = batchName;
           this.listId = listId;
           this.spottingDate = spottingDate;
           this.spottingBy = spottingBy;
           this.tipNumber = tipNumber;
           this.topMargin = topMargin;
           this.bottomMargin = bottomMargin;
           this.rightMargin = rightMargin;
           this.leftMargin = leftMargin;
           this.slideWidth = slideWidth;
           this.slideHeight = slideHeight;
           this.slideImage = slideImage;
           this.spottingOrder = spottingOrder;
           this.slideImageURL = slideImageURL;
    }


    /**
     * Gets the serialNumber value for this SlideDescriptionImpl.
     * 
     * @return serialNumber
     */
    public java.lang.String getSerialNumber() {
        return serialNumber;
    }


    /**
     * Sets the serialNumber value for this SlideDescriptionImpl.
     * 
     * @param serialNumber
     */
    public void setSerialNumber(java.lang.String serialNumber) {
        this.serialNumber = serialNumber;
    }


    /**
     * Gets the slideOrder value for this SlideDescriptionImpl.
     * 
     * @return slideOrder
     */
    public java.lang.String getSlideOrder() {
        return slideOrder;
    }


    /**
     * Sets the slideOrder value for this SlideDescriptionImpl.
     * 
     * @param slideOrder
     */
    public void setSlideOrder(java.lang.String slideOrder) {
        this.slideOrder = slideOrder;
    }


    /**
     * Gets the batchName value for this SlideDescriptionImpl.
     * 
     * @return batchName
     */
    public java.lang.String getBatchName() {
        return batchName;
    }


    /**
     * Sets the batchName value for this SlideDescriptionImpl.
     * 
     * @param batchName
     */
    public void setBatchName(java.lang.String batchName) {
        this.batchName = batchName;
    }


    /**
     * Gets the listId value for this SlideDescriptionImpl.
     * 
     * @return listId
     */
    public int getListId() {
        return listId;
    }


    /**
     * Sets the listId value for this SlideDescriptionImpl.
     * 
     * @param listId
     */
    public void setListId(int listId) {
        this.listId = listId;
    }


    /**
     * Gets the spottingDate value for this SlideDescriptionImpl.
     * 
     * @return spottingDate
     */
    public java.util.Date getSpottingDate() {
        return spottingDate;
    }


    /**
     * Sets the spottingDate value for this SlideDescriptionImpl.
     * 
     * @param spottingDate
     */
    public void setSpottingDate(java.util.Date spottingDate) {
        this.spottingDate = spottingDate;
    }


    /**
     * Gets the spottingBy value for this SlideDescriptionImpl.
     * 
     * @return spottingBy
     */
    public java.lang.String getSpottingBy() {
        return spottingBy;
    }


    /**
     * Sets the spottingBy value for this SlideDescriptionImpl.
     * 
     * @param spottingBy
     */
    public void setSpottingBy(java.lang.String spottingBy) {
        this.spottingBy = spottingBy;
    }


    /**
     * Gets the tipNumber value for this SlideDescriptionImpl.
     * 
     * @return tipNumber
     */
    public int getTipNumber() {
        return tipNumber;
    }


    /**
     * Sets the tipNumber value for this SlideDescriptionImpl.
     * 
     * @param tipNumber
     */
    public void setTipNumber(int tipNumber) {
        this.tipNumber = tipNumber;
    }


    /**
     * Gets the topMargin value for this SlideDescriptionImpl.
     * 
     * @return topMargin
     */
    public int getTopMargin() {
        return topMargin;
    }


    /**
     * Sets the topMargin value for this SlideDescriptionImpl.
     * 
     * @param topMargin
     */
    public void setTopMargin(int topMargin) {
        this.topMargin = topMargin;
    }


    /**
     * Gets the bottomMargin value for this SlideDescriptionImpl.
     * 
     * @return bottomMargin
     */
    public int getBottomMargin() {
        return bottomMargin;
    }


    /**
     * Sets the bottomMargin value for this SlideDescriptionImpl.
     * 
     * @param bottomMargin
     */
    public void setBottomMargin(int bottomMargin) {
        this.bottomMargin = bottomMargin;
    }


    /**
     * Gets the rightMargin value for this SlideDescriptionImpl.
     * 
     * @return rightMargin
     */
    public int getRightMargin() {
        return rightMargin;
    }


    /**
     * Sets the rightMargin value for this SlideDescriptionImpl.
     * 
     * @param rightMargin
     */
    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }


    /**
     * Gets the leftMargin value for this SlideDescriptionImpl.
     * 
     * @return leftMargin
     */
    public int getLeftMargin() {
        return leftMargin;
    }


    /**
     * Sets the leftMargin value for this SlideDescriptionImpl.
     * 
     * @param leftMargin
     */
    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }


    /**
     * Gets the slideWidth value for this SlideDescriptionImpl.
     * 
     * @return slideWidth
     */
    public int getSlideWidth() {
        return slideWidth;
    }


    /**
     * Sets the slideWidth value for this SlideDescriptionImpl.
     * 
     * @param slideWidth
     */
    public void setSlideWidth(int slideWidth) {
        this.slideWidth = slideWidth;
    }


    /**
     * Gets the slideHeight value for this SlideDescriptionImpl.
     * 
     * @return slideHeight
     */
    public int getSlideHeight() {
        return slideHeight;
    }


    /**
     * Sets the slideHeight value for this SlideDescriptionImpl.
     * 
     * @param slideHeight
     */
    public void setSlideHeight(int slideHeight) {
        this.slideHeight = slideHeight;
    }


    /**
     * Gets the slideImage value for this SlideDescriptionImpl.
     * 
     * @return slideImage
     */
    public java.lang.String getSlideImage() {
        return slideImage;
    }


    /**
     * Sets the slideImage value for this SlideDescriptionImpl.
     * 
     * @param slideImage
     */
    public void setSlideImage(java.lang.String slideImage) {
        this.slideImage = slideImage;
    }


    /**
     * Gets the spottingOrder value for this SlideDescriptionImpl.
     * 
     * @return spottingOrder
     */
    public int getSpottingOrder() {
        return spottingOrder;
    }


    /**
     * Sets the spottingOrder value for this SlideDescriptionImpl.
     * 
     * @param spottingOrder
     */
    public void setSpottingOrder(int spottingOrder) {
        this.spottingOrder = spottingOrder;
    }


    /**
     * Gets the slideImageURL value for this SlideDescriptionImpl.
     * 
     * @return slideImageURL
     */
    public java.lang.String getSlideImageURL() {
        return slideImageURL;
    }


    /**
     * Sets the slideImageURL value for this SlideDescriptionImpl.
     * 
     * @param slideImageURL
     */
    public void setSlideImageURL(java.lang.String slideImageURL) {
        this.slideImageURL = slideImageURL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SlideDescription)) return false;
        SlideDescription other = (SlideDescription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.serialNumber==null && other.getSerialNumber()==null) || 
             (this.serialNumber!=null &&
              this.serialNumber.equals(other.getSerialNumber()))) &&
            ((this.slideOrder==null && other.getSlideOrder()==null) || 
             (this.slideOrder!=null &&
              this.slideOrder.equals(other.getSlideOrder()))) &&
            ((this.batchName==null && other.getBatchName()==null) || 
             (this.batchName!=null &&
              this.batchName.equals(other.getBatchName()))) &&
            this.listId == other.getListId() &&
            ((this.spottingDate==null && other.getSpottingDate()==null) || 
             (this.spottingDate!=null &&
              this.spottingDate.equals(other.getSpottingDate()))) &&
            ((this.spottingBy==null && other.getSpottingBy()==null) || 
             (this.spottingBy!=null &&
              this.spottingBy.equals(other.getSpottingBy()))) &&
            this.tipNumber == other.getTipNumber() &&
            this.topMargin == other.getTopMargin() &&
            this.bottomMargin == other.getBottomMargin() &&
            this.rightMargin == other.getRightMargin() &&
            this.leftMargin == other.getLeftMargin() &&
            this.slideWidth == other.getSlideWidth() &&
            this.slideHeight == other.getSlideHeight() &&
            ((this.slideImage==null && other.getSlideImage()==null) || 
             (this.slideImage!=null &&
              this.slideImage.equals(other.getSlideImage()))) &&
            this.spottingOrder == other.getSpottingOrder() &&
            ((this.slideImageURL==null && other.getSlideImageURL()==null) || 
             (this.slideImageURL!=null &&
              this.slideImageURL.equals(other.getSlideImageURL())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getSerialNumber() != null) {
            _hashCode += getSerialNumber().hashCode();
        }
        if (getSlideOrder() != null) {
            _hashCode += getSlideOrder().hashCode();
        }
        if (getBatchName() != null) {
            _hashCode += getBatchName().hashCode();
        }
        _hashCode += getListId();
        if (getSpottingDate() != null) {
            _hashCode += getSpottingDate().hashCode();
        }
        if (getSpottingBy() != null) {
            _hashCode += getSpottingBy().hashCode();
        }
        _hashCode += getTipNumber();
        _hashCode += getTopMargin();
        _hashCode += getBottomMargin();
        _hashCode += getRightMargin();
        _hashCode += getLeftMargin();
        _hashCode += getSlideWidth();
        _hashCode += getSlideHeight();
        if (getSlideImage() != null) {
            _hashCode += getSlideImage().hashCode();
        }
        _hashCode += getSpottingOrder();
        if (getSlideImageURL() != null) {
            _hashCode += getSlideImageURL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SlideDescription.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Lims", "SlideDescriptionImpl"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serialNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SerialNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("slideOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SlideOrder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BatchName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ListId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spottingDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SpottingDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spottingBy");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SpottingBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TipNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("topMargin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TopMargin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bottomMargin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BottomMargin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rightMargin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "RightMargin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("leftMargin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LeftMargin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("slideWidth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SlideWidth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("slideHeight");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SlideHeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("slideImage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SlideImage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spottingOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SpottingOrder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("slideImageURL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SlideImageURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
