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

public class NbiResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private int httpCode;
    private String jsonContent;

    public NbiResponse(int httpCode, String jsonContent) {
        this.httpCode = httpCode;
        this.jsonContent = jsonContent;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NbiResponse that = (NbiResponse) o;
        return httpCode == that.httpCode && Objects.equals(jsonContent, that.jsonContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonContent, httpCode);
    }

    @Override
    public String toString() {
        return "NbiResponse{" +
                "jsonContent='" + jsonContent + '\'' +
                ", httpCode=" + httpCode +
                '}';
    }
}
