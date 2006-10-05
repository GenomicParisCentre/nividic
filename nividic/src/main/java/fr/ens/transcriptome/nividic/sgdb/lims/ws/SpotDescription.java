/**
 * SpotDescription.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package fr.ens.transcriptome.nividic.sgdb.lims.ws;

public class SpotDescription  implements java.io.Serializable {
    private int spotId;
    private java.lang.String spotDescription;

    public SpotDescription() {
    }

    public SpotDescription(
           int spotId,
           java.lang.String spotDescription) {
           this.spotId = spotId;
           this.spotDescription = spotDescription;
    }


    /**
     * Gets the spotId value for this SpotDescription.
     * 
     * @return spotId
     */
    public int getSpotId() {
        return spotId;
    }


    /**
     * Sets the spotId value for this SpotDescription.
     * 
     * @param spotId
     */
    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }


    /**
     * Gets the spotDescription value for this SpotDescription.
     * 
     * @return spotDescription
     */
    public java.lang.String getSpotDescription() {
        return spotDescription;
    }


    /**
     * Sets the spotDescription value for this SpotDescription.
     * 
     * @param spotDescription
     */
    public void setSpotDescription(java.lang.String spotDescription) {
        this.spotDescription = spotDescription;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SpotDescription)) return false;
        SpotDescription other = (SpotDescription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.spotId == other.getSpotId() &&
            ((this.spotDescription==null && other.getSpotDescription()==null) || 
             (this.spotDescription!=null &&
              this.spotDescription.equals(other.getSpotDescription())));
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
        _hashCode += getSpotId();
        if (getSpotDescription() != null) {
            _hashCode += getSpotDescription().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SpotDescription.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Lims", "SpotDescription"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spotId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SpotId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spotDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SpotDescription"));
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
