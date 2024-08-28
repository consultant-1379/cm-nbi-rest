/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.presentation.cmnbirest.license;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.ericsson.oss.itpf.sdk.licensing.LicensingService;
import com.ericsson.oss.itpf.sdk.licensing.Permission;


/**
 * Allows controlled checking of cm-crud-nbi license state via the {@code LicensingService}.
 *
 * @since 1.3.3
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class LicenseValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseValidator.class);
    private static final String VP_DYNAMIC_CM_NBI_5MHZSC = "FAT1023443";
    private static final String VP_DYNAMIC_CM_NBI_CELL_CARRIER = "FAT1023603";
    private static final String VP_DYNAMIC_CM_NBI_ONOFFSCOPE_GSM_TRX = "FAT1023653";
    private static final String VP_DYNAMIC_CM_NBI_ONOFFSCOPE_RADIO = "FAT1023988";
    private static final String VP_DYNAMIC_CM_NBI_ONOFFSCOPE_CORE = "FAT1023989";
    private static final String VP_DYNAMIC_CM_NBI_ONOFFSCOPE_TRANSPORT = "FAT1023990";
    private static final Collection<String> licenseKeys = new ArrayList<>(Arrays.asList(VP_DYNAMIC_CM_NBI_5MHZSC,
            VP_DYNAMIC_CM_NBI_CELL_CARRIER, VP_DYNAMIC_CM_NBI_ONOFFSCOPE_GSM_TRX, VP_DYNAMIC_CM_NBI_ONOFFSCOPE_RADIO,
            VP_DYNAMIC_CM_NBI_ONOFFSCOPE_CORE, VP_DYNAMIC_CM_NBI_ONOFFSCOPE_TRANSPORT));

    private boolean licenseIsValid;
    private String previouslyValidLicense;

    @Inject
    private LicensingService licensingService;

    @Inject
    private TimerService timerService;

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * Schedule a job to be executed via the {@code TimerService}.
     */
    @PostConstruct
    public void scheduleJob() {
        final ScheduleExpression schedule = new ScheduleExpression();
        schedule.hour(0).minute(15).second(1);
        final TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerService.createCalendarTimer(schedule, timerConfig);
    }

    /**
     * Callback method to be called on expiry of timeout registered in {#scheduleJob} method.
     */
    @Timeout
    public void validatePermissionsOnExpiryOfTimeout() {
        validatePermissionWithLicenseManagementService();
    }

    private void validatePermissionWithLicenseManagementService() {
        try {
            LOGGER.debug("Validate current licence, previouslyValidLicense {}", previouslyValidLicense);
            readWriteLock.writeLock().lock();
            if (previouslyValidLicense != null && licensingService.validatePermission(previouslyValidLicense) == Permission.ALLOWED) {
                licenseIsValid = true;
                return;
            }
            for (final String licenseKey : licenseKeys) {
                if (!licenseKey.equals(previouslyValidLicense) && licensingService.validatePermission(licenseKey) == Permission.ALLOWED) {
                    LOGGER.info("New licence validated, licenseKey {}", licenseKey);
                    licenseIsValid = true;
                    previouslyValidLicense = licenseKey;
                    return;
                }
            }
            licenseIsValid = false;
            previouslyValidLicense = null;
        } finally {
            if (readWriteLock.isWriteLockedByCurrentThread()) {
                readWriteLock.writeLock().unlock();
            }
        }
    }

    /**
     * Check if the license is invalid.
     *
     * @return true if the license is invalid; false otherwise
     */
    public boolean isValidLicense() {
        if (!licenseIsValid) {
            validatePermissionWithLicenseManagementService();
        }
        return licenseIsValid;
    }

}