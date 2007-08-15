/**
 * Scan.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.ens.transcriptome.nividic.sgdb.lims.ws;

public class Scan  implements java.io.Serializable {
    private int hybridizationId;

    private java.lang.String creationDate;

    private java.lang.String modificationDate;

    private int id;

    private java.lang.String comment;

    private int channel1;

    private int pmt1;

    private int power1;

    private int channel2;

    private int pmt2;

    private int power2;

    public Scan() {
    }

    public Scan(
           int hybridizationId,
           java.lang.String creationDate,
           java.lang.String modificationDate,
           int id,
           java.lang.String comment,
           int channel1,
           int pmt1,
           int power1,
           int channel2,
           int pmt2,
           int power2) {
           this.hybridizationId = hybridizationId;
           this.creationDate = creationDate;
           this.modificationDate = modificationDate;
           this.id = id;
           this.comment = comment;
           this.channel1 = channel1;
           this.pmt1 = pmt1;
           this.power1 = power1;
           this.channel2 = channel2;
           this.pmt2 = pmt2;
           this.power2 = power2;
    }


    /**
     * Gets the hybridizationId value for this Scan.
     * 
     * @return hybridizationId
     */
    public int getHybridizationId() {
        return hybridizationId;
    }


    /**
     * Sets the hybridizationId value for this Scan.
     * 
     * @param hybridizationId
     */
    public void setHybridizationId(int hybridizationId) {
        this.hybridizationId = hybridizationId;
    }


    /**
     * Gets the creationDate value for this Scan.
     * 
     * @return creationDate
     */
    public java.lang.String getCreationDate() {
        return creationDate;
    }


    /**
     * Sets the creationDate value for this Scan.
     * 
     * @param creationDate
     */
    public void setCreationDate(java.lang.String creationDate) {
        this.creationDate = creationDate;
    }


    /**
     * Gets the modificationDate value for this Scan.
     * 
     * @return modificationDate
     */
    public java.lang.String getModificationDate() {
        return modificationDate;
    }


    /**
     * Sets the modificationDate value for this Scan.
     * 
     * @param modificationDate
     */
    public void setModificationDate(java.lang.String modificationDate) {
        this.modificationDate = modificationDate;
    }


    /**
     * Gets the id value for this Scan.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this Scan.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Gets the comment value for this Scan.
     * 
     * @return comment
     */
    public java.lang.String getComment() {
        return comment;
    }


    /**
     * Sets the comment value for this Scan.
     * 
     * @param comment
     */
    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }


    /**
     * Gets the channel1 value for this Scan.
     * 
     * @return channel1
     */
    public int getChannel1() {
        return channel1;
    }


    /**
     * Sets the channel1 value for this Scan.
     * 
     * @param channel1
     */
    public void setChannel1(int channel1) {
        this.channel1 = channel1;
    }


    /**
     * Gets the pmt1 value for this Scan.
     * 
     * @return pmt1
     */
    public int getPmt1() {
        return pmt1;
    }


    /**
     * Sets the pmt1 value for this Scan.
     * 
     * @param pmt1
     */
    public void setPmt1(int pmt1) {
        this.pmt1 = pmt1;
    }


    /**
     * Gets the power1 value for this Scan.
     * 
     * @return power1
     */
    public int getPower1() {
        return power1;
    }


    /**
     * Sets the power1 value for this Scan.
     * 
     * @param power1
     */
    public void setPower1(int power1) {
        this.power1 = power1;
    }


    /**
     * Gets the channel2 value for this Scan.
     * 
     * @return channel2
     */
    public int getChannel2() {
        return channel2;
    }


    /**
     * Sets the channel2 value for this Scan.
     * 
     * @param channel2
     */
    public void setChannel2(int channel2) {
        this.channel2 = channel2;
    }


    /**
     * Gets the pmt2 value for this Scan.
     * 
     * @return pmt2
     */
    public int getPmt2() {
        return pmt2;
    }


    /**
     * Sets the pmt2 value for this Scan.
     * 
     * @param pmt2
     */
    public void setPmt2(int pmt2) {
        this.pmt2 = pmt2;
    }


    /**
     * Gets the power2 value for this Scan.
     * 
     * @return power2
     */
    public int getPower2() {
        return power2;
    }


    /**
     * Sets the power2 value for this Scan.
     * 
     * @param power2
     */
    public void setPower2(int power2) {
        this.power2 = power2;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Scan)) return false;
        Scan other = (Scan) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.hybridizationId == other.getHybridizationId() &&
            ((this.creationDate==null && other.getCreationDate()==null) || 
             (this.creationDate!=null &&
              this.creationDate.equals(other.getCreationDate()))) &&
            ((this.modificationDate==null && other.getModificationDate()==null) || 
             (this.modificationDate!=null &&
              this.modificationDate.equals(other.getModificationDate()))) &&
            this.id == other.getId() &&
            ((this.comment==null && other.getComment()==null) || 
             (this.comment!=null &&
              this.comment.equals(other.getComment()))) &&
            this.channel1 == other.getChannel1() &&
            this.pmt1 == other.getPmt1() &&
            this.power1 == other.getPower1() &&
            this.channel2 == other.getChannel2() &&
            this.pmt2 == other.getPmt2() &&
            this.power2 == other.getPower2();
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
        _hashCode += getHybridizationId();
        if (getCreationDate() != null) {
            _hashCode += getCreationDate().hashCode();
        }
        if (getModificationDate() != null) {
            _hashCode += getModificationDate().hashCode();
        }
        _hashCode += getId();
        if (getComment() != null) {
            _hashCode += getComment().hashCode();
        }
        _hashCode += getChannel1();
        _hashCode += getPmt1();
        _hashCode += getPower1();
        _hashCode += getChannel2();
        _hashCode += getPmt2();
        _hashCode += getPower2();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Scan.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Lims", "Scan"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hybridizationId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hybridizationId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "creationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modificationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "modificationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comment");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("channel1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "channel1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pmt1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pmt1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("power1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "power1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("channel2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "channel2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pmt2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pmt2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("power2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "power2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
