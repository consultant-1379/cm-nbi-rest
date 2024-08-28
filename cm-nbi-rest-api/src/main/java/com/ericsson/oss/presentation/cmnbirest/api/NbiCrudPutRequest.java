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

public class NbiCrudPutRequest extends NbiRequest {
    private static final long serialVersionUID = 1L;

    private String xpath;
    private String bodyPut;

    @SuppressWarnings({"squid:S107"})
    public NbiCrudPutRequest(String userId, String requestId, String userIpAddress, String ssoToken, String xpath, String body) {
        super(userId, requestId, userIpAddress, ssoToken);
        this.xpath = xpath;
        this.bodyPut = body;
    }

    public String getXpath() {
        return xpath;
    }

    public String getBody() {
        return bodyPut;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        NbiCrudPutRequest that = (NbiCrudPutRequest) o;

        if (getXpath() != null ? !getXpath().equals(that.getXpath()) : that.getXpath() != null) return false;
        return getBody() != null ? getBody().equals(that.getBody()) : that.getBody() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getXpath() != null ? getXpath().hashCode() : 0);
        result = 31 * result + (getBody() != null ? getBody().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NbiCrudPutRequest{" +
                "userId='" + getUserId() + '\'' +
                ", requestId='" + getRequestId() + '\'' +
                ", xpath='" + xpath + '\'' +
                ", body='" + bodyPut + '\'' +
                '}';
    }

    @Override
    public String getRecordingInfo() {
        return "NbiCrudPutRequest{" +
                "xpath='" + xpath + '\'' +
                ", body='{ ... }'" +
                '}';
    }
}
