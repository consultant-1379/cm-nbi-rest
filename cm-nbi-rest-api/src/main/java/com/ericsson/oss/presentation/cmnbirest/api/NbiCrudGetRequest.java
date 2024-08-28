/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2021
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.presentation.cmnbirest.api;

public class NbiCrudGetRequest extends NbiRequest {
    private static final long serialVersionUID = 1L;

    private String xpath;
    private String scopeType;
    private String fields;
    private String attributes;
    private int scopeLevel;
    private String filter;

    @SuppressWarnings({"squid:S107"})
    public NbiCrudGetRequest(String userId, String requestId, String userIpAddress, String ssoToken, String xpath, String scopeType, String fields, String attributes, int scopeLevel, String filter) {
        super(userId, requestId, userIpAddress, ssoToken);
        this.xpath = xpath;
        this.scopeType = scopeType;
        this.fields = fields;
        this.attributes = attributes;
        this.scopeLevel = scopeLevel;
        this.filter = filter;
    }

    public String getXpath() {
        return xpath;
    }

    public String getScopeType() {
        return scopeType;
    }

    public String getFields() {
        return fields;
    }

    public String getAttributes() {
        return attributes;
    }


    public int getScopeLevel() {
        return scopeLevel;
    }

    public String getFilter() {
        return filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        NbiCrudGetRequest that = (NbiCrudGetRequest) o;

        if (scopeLevel != that.scopeLevel) return false;
        if (xpath != null ? !xpath.equals(that.xpath) : that.xpath != null) return false;
        if (scopeType != null ? !scopeType.equals(that.scopeType) : that.scopeType != null) return false;
        if (fields != null ? !fields.equals(that.fields) : that.fields != null) return false;
        if (attributes != null ? !attributes.equals(that.attributes) : that.attributes != null) return false;
        return filter != null ? filter.equals(that.filter) : that.filter == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (xpath != null ? xpath.hashCode() : 0);
        result = 31 * result + (scopeType != null ? scopeType.hashCode() : 0);
        result = 31 * result + (fields != null ? fields.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + scopeLevel;
        result = 31 * result + (filter != null ? filter.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NbiCrudGetRequest{" +
                "userId='" + getUserId() + '\'' +
                ", requestId='" + getRequestId() + '\'' +
                ", xpath='" + xpath + '\'' +
                ", scopeType='" + scopeType + '\'' +
                ", fields='" + fields + '\'' +
                ", attributes='" + attributes + '\'' +
                ", scopeLevel=" + scopeLevel +
                ", filter='" + filter + '\'' +
                '}';
    }

    @Override
    public String getRecordingInfo() {
        return "NbiCrudGetRequest{" +
                "xpath='" + xpath + '\'' +
                ", scopeType='" + scopeType + '\'' +
                ", fields='" + fields + '\'' +
                ", attributes='" + attributes + '\'' +
                ", scopeLevel=" + scopeLevel +
                ", filter='" + filter + '\'' +
                '}';
    }
}
