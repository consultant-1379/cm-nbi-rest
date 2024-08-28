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

public class NbiCrudPatchRequest extends NbiRequest {
    private static final long serialVersionUID = 1L;

    private String xpath;
    private String body;

    private PatchContentType patchContentType;

    @SuppressWarnings({"squid:S107"})
    public NbiCrudPatchRequest(String userId, String requestId, String userIpAddress, String ssoToken, String xpath, String body, PatchContentType patchContentType) {
        super(userId, requestId, userIpAddress, ssoToken);
        this.xpath = xpath;
        this.body = body;
        this.patchContentType = patchContentType;
    }

    public String getXpath() {
        return xpath;
    }

    public String getBody() {
        return body;
    }

    public PatchContentType getPatchContentType() {
        return patchContentType;
    }

    @Override
    public String toString() {
        return "NbiCrudPatchRequest{" +
                "userId='" + getUserId() + '\'' +
                ", requestId='" + getRequestId() + '\'' +
                ", xpath='" + xpath + '\'' +
                ", body='" + body + '\'' +
                ", patchContentType='" + patchContentType + '\'' +
                '}';
    }

    @Override
    public String getRecordingInfo() {
        return "NbiCrudPatchRequest{" +
                "xpath='" + xpath + '\'' +
                ", body='{ ... }'" +
                ", patchContentType='" + patchContentType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        NbiCrudPatchRequest that = (NbiCrudPatchRequest) o;

        if (xpath != null ? !xpath.equals(that.xpath) : that.xpath != null) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        return patchContentType == that.patchContentType;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (xpath != null ? xpath.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (patchContentType != null ? patchContentType.hashCode() : 0);
        return result;
    }
}
