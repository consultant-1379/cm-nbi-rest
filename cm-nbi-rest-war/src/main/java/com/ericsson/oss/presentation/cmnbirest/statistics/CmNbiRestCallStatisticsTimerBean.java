/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@Singleton
@LocalBean
@Startup
public class CmNbiRestCallStatisticsTimerBean {

    @Resource
    private TimerService timerService;

    @Inject
    private CmNbiRestCallsStatistics cmNbiRestCallsStatistics;

    @Inject
    private Logger logger;

    private static final long ATTEMPT_INTERVAL = TimeUnit.SECONDS.toMillis(10);

    @PostConstruct
    public void setupTimer() {
        logger.debug("CmNbiRestCallStatisticsTimerBean STARTED");
        setAttemptsTimer(ATTEMPT_INTERVAL);
    }

    @Timeout
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void showStatistics(final Timer timer) {
        try {
            final StringBuilder stringBuilder = new StringBuilder();
            long nbiCrudMaxActiveRequests = cmNbiRestCallsStatistics.getNbiCrudMaxActiveRequests();

            if (nbiCrudMaxActiveRequests!= 0) {
                stringBuilder.append("CmNbiRestCallsStatistics info:");
                stringBuilder.append("[ ");
                stringBuilder.append(" active=");
                stringBuilder.append(cmNbiRestCallsStatistics.getNbiCrudActiveRequests());
                stringBuilder.append(", active get=");
                stringBuilder.append(cmNbiRestCallsStatistics.getNbiCrudGetActiveRequests());
                stringBuilder.append(", active put=");
                stringBuilder.append(cmNbiRestCallsStatistics.getNbiCrudPutActiveRequests());
                stringBuilder.append(", active post=");
                stringBuilder.append(cmNbiRestCallsStatistics.getNbiCrudPostActiveRequests());
                stringBuilder.append(", active delete=");
                stringBuilder.append(cmNbiRestCallsStatistics.getNbiCrudDeleteActiveRequests());
                stringBuilder.append(", active patch=");
                stringBuilder.append(cmNbiRestCallsStatistics.getNbiCrudPatchActiveRequests());
                stringBuilder.append(", active max=");
                stringBuilder.append(nbiCrudMaxActiveRequests);
                stringBuilder.append(", total=");
                stringBuilder.append(cmNbiRestCallsStatistics.getNbiCrudTotalRequests());
                stringBuilder.append(" ]");
                logger.debug("{}", stringBuilder);
            }

            //reset getNbiCrudMaxActiveRequests
            cmNbiRestCallsStatistics.resetNbiCrudMaxActiveRequests();
        } finally {
            setAttemptsTimer(ATTEMPT_INTERVAL);
        }
    }

    private void setAttemptsTimer(final long duration) {
        logger.debug("CmNbiRestCallStatisticsTimerBean started again");
        final TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerService.createSingleActionTimer(duration, timerConfig);
    }

}
