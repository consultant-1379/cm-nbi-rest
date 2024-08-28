/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2018
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.presentation.cmnbirest.statistics;

import org.slf4j.Logger;

import javax.ejb.*;
import javax.inject.Inject;

/**
 * Created by enmadmin on 1/19/21.
 */
@Singleton
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class CmNbiRestCallsStatistics {

    @Inject
    private Logger logger;

    // active requests
    private long nbiCrudActiveRequests = 0;

    private long nbiCrudMaxActiveRequests = 0;

    // get active requests
    private long nbiCrudGetActiveRequests = 0;

    // delete active requests
    private long nbiCrudDeleteActiveRequests = 0;

    // put active requests
    private long nbiCrudPutActiveRequests = 0;

    // post active requests
    private long nbiCrudPostActiveRequests = 0;

    // patch active requests
    private long nbiCrudPatchActiveRequests = 0;


    // total requests
    private long nbiCrudTotalRequests = 0;

    @Lock(LockType.WRITE)
    public void incrementNbiCrudGetActiveRequests() {
        nbiCrudGetActiveRequests++;
        incrementActiveRequests();
        incrementNbiCrudTotalRequests();
    }

    @Lock(LockType.WRITE)
    public void decrementNbiCrudGetActiveRequests() {
        nbiCrudGetActiveRequests--;
        decrementActiveRequests();
    }

    @Lock(LockType.WRITE)
    public void incrementNbiCrudPutActiveRequests() {
        nbiCrudPutActiveRequests++;
        incrementActiveRequests();
        incrementNbiCrudTotalRequests();
    }

    @Lock(LockType.WRITE)
    public void decrementNbiCrudPutActiveRequests() {
        nbiCrudPutActiveRequests--;
        decrementActiveRequests();
    }

    @Lock(LockType.WRITE)
    public void incrementNbiCrudPostActiveRequests() {
        nbiCrudPostActiveRequests++;
        incrementActiveRequests();
        incrementNbiCrudTotalRequests();
    }

    @Lock(LockType.WRITE)
    public void decrementNbiCrudPostActiveRequests() {
        nbiCrudPostActiveRequests--;
        decrementActiveRequests();
    }

    @Lock(LockType.WRITE)
    public void incrementNbiCrudDeleteActiveRequests() {
        nbiCrudDeleteActiveRequests++;
        incrementActiveRequests();
        incrementNbiCrudTotalRequests();
    }

    @Lock(LockType.WRITE)
    public void decrementNbiCrudDeleteActiveRequests() {
        nbiCrudDeleteActiveRequests--;
        decrementActiveRequests();
    }

    @Lock(LockType.WRITE)
    public void incrementNbiCrudPatchActiveRequests() {
        nbiCrudPatchActiveRequests++;
        incrementActiveRequests();
        incrementNbiCrudTotalRequests();
    }

    @Lock(LockType.WRITE)
    public void decrementNbiCrudPatchActiveRequests() {
        nbiCrudPatchActiveRequests--;
        decrementActiveRequests();
    }

    private void incrementActiveRequests() {
        nbiCrudActiveRequests++;
        if (nbiCrudActiveRequests > nbiCrudMaxActiveRequests) {
            nbiCrudMaxActiveRequests = nbiCrudActiveRequests;
        }
    }

    private void decrementActiveRequests() {
        nbiCrudActiveRequests--;
    }


    //  TOTAL
    private void incrementNbiCrudTotalRequests() {
        if (nbiCrudTotalRequests == Long.MAX_VALUE -1) {
            nbiCrudTotalRequests = 0;
        }
        nbiCrudTotalRequests++;
    }

    @Lock(LockType.WRITE)
    @SuppressWarnings("unused")
    public void resetNbiCrudTotalRequests() {
      nbiCrudTotalRequests = 0;
    }

    @Lock(LockType.WRITE)
    @SuppressWarnings("unused")
    public void resetNbiCrudMaxActiveRequests() {
        nbiCrudMaxActiveRequests = 0;
    }

    /**
        GETTER
     */

    @Lock(LockType.READ)
    public long getNbiCrudActiveRequests() { return nbiCrudActiveRequests; }

    @Lock(LockType.READ)
    public long getNbiCrudGetActiveRequests() {
        return nbiCrudGetActiveRequests;
    }

    @Lock(LockType.READ)
    public long getNbiCrudPutActiveRequests() {
        return nbiCrudPutActiveRequests;
    }

    @Lock(LockType.READ)
    public long getNbiCrudPostActiveRequests() { return nbiCrudPostActiveRequests;
    }

    @Lock(LockType.READ)
    public long getNbiCrudDeleteActiveRequests() { return nbiCrudDeleteActiveRequests;
    }

    @Lock(LockType.READ)
    public long getNbiCrudPatchActiveRequests() {
        return nbiCrudPatchActiveRequests;
    }

    @Lock(LockType.READ)
    public long getNbiCrudMaxActiveRequests() {
        return nbiCrudMaxActiveRequests;
    }

    @Lock(LockType.READ)
    public long getNbiCrudTotalRequests() { return nbiCrudTotalRequests; }

}
