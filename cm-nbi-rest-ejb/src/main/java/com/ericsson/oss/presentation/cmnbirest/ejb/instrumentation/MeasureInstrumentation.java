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
package com.ericsson.oss.presentation.cmnbirest.ejb.instrumentation;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import java.util.HashMap;

/**
 * Created by enmadmin on 11/24/21.
 */
@Singleton
public class MeasureInstrumentation {

    HashMap<String, MeasureData> measureStorage = new HashMap<>();

    @Lock(LockType.WRITE)
    public void stopInstrumentationMeasure(final String requestType, final long startTimeMeasure, final boolean failure) {
        MeasureData measure = measureStorage.get(requestType);
        if (measure == null) {
            measure = new MeasureData();
        }
        final long deltaTime = System.currentTimeMillis() - startTimeMeasure;
        measure.incrementIntervalTotalTime(deltaTime, failure);
        measureStorage.put(requestType, measure);
    }

    @Lock(LockType.WRITE)
    public long getSavedDailyVisits(final String requestType) {
        MeasureData measure = measureStorage.get(requestType);
        if (measure == null) {
            return 0;
        } else {
            return measure.getSavedDailyVisits();
        }
    }

    @Lock(LockType.WRITE)
    public long getSavedAverageExecutionTime(final String requestType) {
        MeasureData measure = measureStorage.get(requestType);
        if (measure == null) {
            return 0;
        } else {
            return measure.getSavedIntervalAvgTime();
        }
    }

    @Lock(LockType.WRITE)
    public long getSavedMaxExecutionTime(final String requestType) {
        MeasureData measure = measureStorage.get(requestType);
        if (measure == null) {
            return 0;
        } else {
            return measure.getSavedIntervalMaxTime();
        }
    }

    @Lock(LockType.WRITE)
    public long getSavedNumberOfFailedResponses(final String requestType) {
        MeasureData measure = measureStorage.get(requestType);
        if (measure == null) {
            return 0;
        } else {
            return measure.getSavedNumberOfFailedResponses();
        }
    }

    @Lock(LockType.WRITE)
    public void freezeMeasureData(final String requestType) {
        MeasureData measure = measureStorage.get(requestType);
        if (measure != null) {
            measure.freezeDailyVisits();
            measure.freezeIntervalAvgTime();
            measure.freezeIntervalMaxTime();
            measure.freezeNumberOfFailedResponses();
        }
    }
}
