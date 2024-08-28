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

import com.ericsson.oss.itpf.sdk.instrument.annotation.InstrumentedBean;
import com.ericsson.oss.itpf.sdk.instrument.annotation.MonitoredAttribute;
import com.ericsson.oss.itpf.sdk.instrument.annotation.MonitoredAttribute.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@InstrumentedBean(description = "CM NBI CRUD Operation", displayName = "CM NBI CRUD")
@ApplicationScoped
public class InstrumentationBean {

    public static final String GET_BASE_ONLY = "GET_BASE_ONLY";
    public static final String GET_BASE_OTHER_ALL = "GET_BASE_OTHER_ALL";
    public static final String PUT_MODIFY = "PUT_MODIFY";
    public static final String PUT_CREATE = "PUT_CREATE";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String PATCH_JSON_PATCH = "PATCH_JSON_PATCH";
    public static final String PATCH_3GPP_JSON_PATCH = "PATCH_3GPP_JSON_PATCH";
    public static final Set<Integer> SUCCESS_HTTP_CODES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(200,201,202,203,204,303)));

    @Inject
    private MeasureInstrumentation measureInstrumentation;

    public long startInstrumentationMeasure() {
        return System.currentTimeMillis();
    }

    public void stopInstrumentationMeasure(final String requestType, final long startTimeMeasure, final int httpStatus) {
        final boolean failure = isFailureHttpStatus(httpStatus);
        measureInstrumentation.stopInstrumentationMeasure(requestType,startTimeMeasure, failure);
    }

    /*
     * GET_BASE_ONLY
     */
    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.NONE,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.TRENDSUP)
    public long getGetBaseOnlyCount() {
        return measureInstrumentation.getSavedDailyVisits(GET_BASE_ONLY);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getGetBaseOnlyAsyncResponses() {
        return measureInstrumentation.getSavedNumberOfFailedResponses(GET_BASE_ONLY);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getGetBaseOnlyTotalAvgTime() {
        return measureInstrumentation.getSavedAverageExecutionTime(GET_BASE_ONLY);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getGetBaseOnlyTotalMaxTime() {
        return measureInstrumentation.getSavedMaxExecutionTime(GET_BASE_ONLY);
    }

    /*
     * GET_BASE_OTHER_ALL
     */
    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.NONE,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.TRENDSUP)
    public long getGetBaseOtherAllCount() {
        return measureInstrumentation.getSavedDailyVisits(GET_BASE_OTHER_ALL);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getGetBaseOtherAllAsyncResponses() {
        return measureInstrumentation.getSavedNumberOfFailedResponses(GET_BASE_OTHER_ALL);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getGetBaseOtherAllTotalAvgTime() {
        return measureInstrumentation.getSavedAverageExecutionTime(GET_BASE_OTHER_ALL);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getGetBaseOtherAllTotalMaxTime() {
        return measureInstrumentation.getSavedMaxExecutionTime(GET_BASE_OTHER_ALL);
    }

    /*
     * PUT_MODIFY
     */
    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.NONE,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.TRENDSUP)
    public long getPutModifyCount() {
        return measureInstrumentation.getSavedDailyVisits(PUT_MODIFY);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPutModifyAsyncResponses() {
        return measureInstrumentation.getSavedNumberOfFailedResponses(PUT_MODIFY);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPutModifyTotalAvgTime() {
        return measureInstrumentation.getSavedAverageExecutionTime(PUT_MODIFY);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPutModifyTotalMaxTime() {
        return measureInstrumentation.getSavedMaxExecutionTime(PUT_MODIFY);
    }

    /*
     * PUT_CREATE
     */
    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.NONE,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.TRENDSUP)
    public long getPutCreateCount() {
        return measureInstrumentation.getSavedDailyVisits(PUT_CREATE);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPutCreateAsyncResponses() {
        return measureInstrumentation.getSavedNumberOfFailedResponses(PUT_CREATE);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPutCreateTotalAvgTime() {
        return measureInstrumentation.getSavedAverageExecutionTime(PUT_CREATE);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPutCreateTotalMaxTime() {
        return measureInstrumentation.getSavedMaxExecutionTime(PUT_CREATE);
    }

    /*
     * POST
     */
    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.NONE,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.TRENDSUP)
    public long getPostCount() {
        return measureInstrumentation.getSavedDailyVisits(POST);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPostAsyncResponses() {
        return measureInstrumentation.getSavedNumberOfFailedResponses(POST);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPostTotalAvgTime() {
        return measureInstrumentation.getSavedAverageExecutionTime(POST);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPostTotalMaxTime() {
        return measureInstrumentation.getSavedMaxExecutionTime(POST);
    }

    /*
     * DELETE
     */
    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.NONE,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.TRENDSUP)
    public long getDeleteCount() {
        return measureInstrumentation.getSavedDailyVisits(DELETE);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getDeleteAsyncResponses() {
        return measureInstrumentation.getSavedNumberOfFailedResponses(DELETE);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getDeleteTotalAvgTime() {
        return measureInstrumentation.getSavedAverageExecutionTime(DELETE);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getDeleteTotalMaxTime() {
        return measureInstrumentation.getSavedMaxExecutionTime(DELETE);
    }

    /*
     * PATCH_JSON_PATCH
     */
    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.NONE,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.TRENDSUP)
    public long getPatchJPatchCount() {
        return measureInstrumentation.getSavedDailyVisits(PATCH_JSON_PATCH);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPatchJPatchAsyncResponses() {
        return measureInstrumentation.getSavedNumberOfFailedResponses(PATCH_JSON_PATCH);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPatchJPatchTotalAvgTime() {
        return measureInstrumentation.getSavedAverageExecutionTime(PATCH_JSON_PATCH);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPatchJPatchTotalMaxTime() {
        return measureInstrumentation.getSavedMaxExecutionTime(PATCH_JSON_PATCH);
    }

    /*
     * PATCH_3GPP_JSON_PATCH
     */
    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.NONE,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.TRENDSUP)
    public long getPatch3gppJPatchCount() {
        return measureInstrumentation.getSavedDailyVisits(PATCH_3GPP_JSON_PATCH);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPatch3gppJPatchAsyncResponses() {
        return measureInstrumentation.getSavedNumberOfFailedResponses(PATCH_3GPP_JSON_PATCH);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPatch3gppJPatchTotalAvgTime() {
        return measureInstrumentation.getSavedAverageExecutionTime(PATCH_3GPP_JSON_PATCH);
    }

    @MonitoredAttribute(visibility = Visibility.ALL,
            units = Units.MILLISECONDS,
            category = Category.PERFORMANCE,
            interval = Interval.ONE_MIN,
            collectionType = CollectionType.DYNAMIC)
    public long getPatch3gppJPatchTotalMaxTime() {
        return measureInstrumentation.getSavedMaxExecutionTime(PATCH_3GPP_JSON_PATCH);
    }

    private boolean isFailureHttpStatus(int httpStatus) {
        return !SUCCESS_HTTP_CODES.contains(httpStatus);
    }
}
