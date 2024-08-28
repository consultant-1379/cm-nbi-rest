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

public class NbiCrudActionRequest extends NbiRequest {
    private static final long serialVersionUID = 1L;

    private String xpath;
    private String action;
    private String body;

    @SuppressWarnings({"squid:S107"})
    public NbiCrudActionRequest(String userId, String requestId, String userIpAddress, String ssoToken, String xpath, String action, String body) {
        super(userId, requestId, userIpAddress, ssoToken);
        this.xpath = xpath;
        this.action = action;
        this.body = body;
    }

    public String getXpath() {
        return xpath;
    }

    public String getAction() {
        return action;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "NbiCrudActionRequest{" +
                " userId='" + getUserId() + '\'' +
                ", requestId='" + getRequestId() + '\'' +
                ", xpath='" + xpath + '\'' +
                ", action='" + action + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    @Override
    public String getRecordingInfo() {
        return "NbiCrudActionRequest{" +
                "xpath='" + xpath + '\'' +
                ", action='" + action + '\'' +
                ", body='{ ... }'" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        NbiCrudActionRequest that = (NbiCrudActionRequest) o;

        if (xpath != null ? !xpath.equals(that.xpath) : that.xpath != null) return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        return action.equals(that.action);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (xpath != null ? xpath.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }
}
