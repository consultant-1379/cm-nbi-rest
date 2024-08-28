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
package com.ericsson.oss.presentation.cmnbirest.ejb;

import com.ericsson.oss.presentation.cmnbirest.api.NbiRestApplication;


public class RestNbiSelectJmsSelector {
    private static final String RDN_SEPARATOR = "/";
    private static final String SUBNETWORK = "SubNetwork";
    private static final String BASE_ONLY = "BASE_ONLY";

    public NbiRestApplication getFilter(final String xPath, final String scopeType, final String filter, final String attributes) {

        if (BASE_ONLY.equals(scopeType)) {
            return NbiRestApplication.CRUD;
        }
        if (xPath == null) {
            return NbiRestApplication.CRUD_STRONG;
        }

        final String moType = extractTypeFromFdn(xPath);
        if (SUBNETWORK.equalsIgnoreCase(moType)) {
            if (attributes == null) {
                return NbiRestApplication.CRUD_STRONG;
            }
            return NbiRestApplication.CRUD_MEDIUM;
        }


        if (filter == null) {
            if (attributes == null)  {
                return NbiRestApplication.CRUD_STRONG;
            } else {
                return NbiRestApplication.CRUD_MEDIUM;
            }
        } else {
            if (attributes == null) {
                return NbiRestApplication.CRUD_MEDIUM;
            } else {
                return NbiRestApplication.CRUD;
            }
        }
    }


    private  String extractTypeFromFdn(final String xPath) {
        final int lastIndexOfRdnSeparator = xPath.lastIndexOf(RDN_SEPARATOR);
        final int lastIndexOfNameSeparator = xPath.lastIndexOf('=');
        if (lastIndexOfNameSeparator>lastIndexOfRdnSeparator) {
            return xPath.substring(lastIndexOfRdnSeparator + 1, lastIndexOfNameSeparator);
        }
        return null;
    }
}
