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

package com.ericsson.oss.presentation.cmnbirest;

import java.util.Locale;
import java.util.UUID;
import com.ericsson.oss.presentation.cmnbirest.api.NbiRestApplication;

/**
 * Created by enmadmin on 9/23/21.
 */
public class CmRestNbiUtility {


    public String generateNbiRequestId(final NbiRestApplication application) {
            return application.getName().toLowerCase(Locale.ENGLISH) + ":"+ UUID.randomUUID().toString();
    }

}
