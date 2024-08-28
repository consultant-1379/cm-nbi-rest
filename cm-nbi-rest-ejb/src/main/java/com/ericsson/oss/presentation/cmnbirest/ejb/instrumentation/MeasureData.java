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

/**
 * Created by enmadmin on 11/24/21.
 */
public class MeasureData {
    private long dailyVisits;
    private long intervalTotalTime;
    private long intervalOperationNumber;
    private long intervalMaxTime;
    private long numberOfFailedResponses;
    private long savedDailyVisits;
    private long savedIntervalAvgTime;
    private long savedIntervalMaxTime;
    private long savedNumberOfFailedResponses;

    public MeasureData() {
        dailyVisits = 0;
        intervalTotalTime = 0;
        intervalOperationNumber = 0;
        intervalMaxTime = 0;
        numberOfFailedResponses = 0;
        savedDailyVisits = 0;
        savedIntervalAvgTime = 0;
        savedIntervalMaxTime = 0;
        savedNumberOfFailedResponses = 0;
    }

    public void incrementIntervalTotalTime(final long incrementValue, final boolean failure) {
        dailyVisits +=1;
        intervalTotalTime += incrementValue;
        intervalOperationNumber+=1;

        if (incrementValue > intervalMaxTime) {
            intervalMaxTime = incrementValue;
        }
        if (failure) {
            numberOfFailedResponses++;
        }
    }

    public void freezeDailyVisits() {
        savedDailyVisits = dailyVisits;
    }

    public void freezeIntervalAvgTime() {
        long value = 0;
        if (intervalOperationNumber != 0) {
            value = intervalTotalTime / intervalOperationNumber;
        }
        intervalOperationNumber = 0;
        intervalTotalTime = 0;

        savedIntervalAvgTime = value;
    }

    public void freezeIntervalMaxTime() {
        savedIntervalMaxTime = intervalMaxTime;
        intervalMaxTime = 0;
    }

    public void freezeNumberOfFailedResponses() {
        savedNumberOfFailedResponses = numberOfFailedResponses;
        numberOfFailedResponses = 0;
    }

    public long getSavedDailyVisits() {
        return savedDailyVisits;
    }

    public long getSavedIntervalAvgTime() {
        return savedIntervalAvgTime;
    }

    public long getSavedIntervalMaxTime() {
        return savedIntervalMaxTime;
    }

    public long getSavedNumberOfFailedResponses() {
        return savedNumberOfFailedResponses;
    }
}
