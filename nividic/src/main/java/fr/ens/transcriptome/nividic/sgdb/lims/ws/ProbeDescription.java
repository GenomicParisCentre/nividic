/**
 * ProbeDescription.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package fr.ens.transcriptome.nividic.sgdb.lims.ws;

public class ProbeDescription  implements java.io.Serializable {
    private int probeId;
    private java.lang.String probeName;
    private java.lang.String probeDescription;
    private int organismId;
    private boolean control;

    public ProbeDescription() {
    }

    public ProbeDescription(
           int probeId,
           java.lang.String probeName,
           java.lang.String probeDescription,
           int organismId,
           boolean control) {
           this.probeId = probeId;
           this.probeName = probeName;
           this.probeDescription = probeDescription;
           this.organismId = organismId;
           this.control = control;
    }


    /**
     * Gets the probeId value for this ProbeDescription.
     * 
     * @return probeId
     */
    public int getProbeId() {
        return probeId;
    }


    /**
     * Sets the probeId value for this ProbeDescription.
     * 
     * @param probeId
     */
    public void setProbeId(int probeId) {
        this.probeId = probeId;
    }


    /**
     * Gets the probeName value for this ProbeDescription.
     * 
     * @return probeName
     */
    public java.lang.String getProbeName() {
        return probeName;
    }


    /**
     * Sets the probeName value for this ProbeDescription.
     * 
     * @param probeName
     */
    public void setProbeName(java.lang.String probeName) {
        this.probeName = probeName;
    }


    /**
     * Gets the probeDescription value for this ProbeDescription.
     * 
     * @return probeDescription
     */
    public java.lang.String getProbeDescription() {
        return probeDescription;
    }


    /**
     * Sets the probeDescription value for this ProbeDescription.
     * 
     * @param probeDescription
     */
    public void setProbeDescription(java.lang.String probeDescription) {
        this.probeDescription = probeDescription;
    }


    /**
     * Gets the organismId value for this ProbeDescription.
     * 
     * @return organismId
     */
    public int getOrganismId() {
        return organismId;
    }


    /**
     * Sets the organismId value for this ProbeDescription.
     * 
     * @param organismId
     */
    public void setOrganismId(int organismId) {
        this.organismId = organismId;
    }


    /**
     * Gets the control value for this ProbeDescription.
     * 
     * @return control
     */
    public boolean isControl() {
        return control;
    }


    /**
     * Sets the control value for this ProbeDescription.
     * 
     * @param control
     */
    public void setControl(boolean control) {
        this.control = control;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProbeDescription)) return false;
        ProbeDescription other = (ProbeDescription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.probeId == other.getProbeId() &&
            ((this.probeName==null && other.getProbeName()==null) || 
             (this.probeName!=null &&
              this.probeName.equals(other.getProbeName()))) &&
            ((this.probeDescription==null && other.getProbeDescription()==null) || 
             (this.probeDescription!=null &&
              this.probeDescription.equals(other.getProbeDescription()))) &&
            this.organismId == other.getOrganismId() &&
            this.control == other.isControl();
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
        _hashCode += getProbeId();
        if (getProbeName() != null) {
            _hashCode += getProbeName().hashCode();
        }
        if (getProbeDescription() != null) {
            _hashCode += getProbeDescription().hashCode();
        }
        _hashCode += getOrganismId();
        _hashCode += (isControl() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProbeDescription.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Lims", "ProbeDescription"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("probeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProbeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("probeName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProbeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("probeDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProbeDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organismId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OrganismId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("control");
        elemField.setXmlName(new javax.xml.namespace.QName("", "control"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
