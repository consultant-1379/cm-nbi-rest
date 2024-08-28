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

import java.io.Serializable;
import java.util.Objects;

public abstract class NbiRequest implements Serializable {
    private String userId;
    private String requestId;
    private String userIpAddress;
    private String ssoToken;
    private int retryNumber;
    private static final String EMPTY = "";

    public NbiRequest(String userId, String requestId, String userIpAddress, String ssoToken) {
        this.userId = userId;
        this.requestId = requestId;
        this.userIpAddress = userIpAddress;
        this.ssoToken = ssoToken;
        this.retryNumber = 0;
    }

    public String getUserId() {
        return userId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUserIpAddress() { return userIpAddress; }

    public String getSsoToken() { return ssoToken;}

    public int getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(int retryNumber) {
        this.retryNumber = retryNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NbiRequest that = (NbiRequest) o;
        return Objects.equals(userId, that.userId) && Objects.equals(requestId, that.requestId) && Objects.equals(userIpAddress, that.userIpAddress) && Objects.equals(ssoToken, that.ssoToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, requestId, userIpAddress, ssoToken);
    }

    @Override
    public String toString() {
        return "NbiRequest{" +
                "userId='" + userId + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }

    public String getRecordingInfo() {
        return EMPTY;
    }
}
