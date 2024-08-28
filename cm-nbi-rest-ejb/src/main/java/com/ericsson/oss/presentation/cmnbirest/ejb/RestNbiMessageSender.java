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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.ericsson.oss.itpf.sdk.eventbus.Channel;
import com.ericsson.oss.itpf.sdk.eventbus.Event;
import com.ericsson.oss.itpf.sdk.eventbus.annotation.Endpoint;
import com.ericsson.oss.itpf.sdk.eventbus.EventConfiguration;
import com.ericsson.oss.itpf.sdk.eventbus.EventConfigurationBuilder;
import com.ericsson.oss.presentation.cmnbirest.api.NbiRequest;
import com.ericsson.oss.presentation.cmnbirest.api.NbiRestApplication;

/**
 * Created by enmadmin on 9/23/21.
 */

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class RestNbiMessageSender {

        private final Logger logger = LoggerFactory.getLogger(RestNbiMessageSender.class);

        @Inject
        @Endpoint(value = "jms:/queue/scriptengine/request", timeToLive = 60000)
        private Channel scriptengineRequestChannel;

        public void sendMessage(final NbiRequest nbiRequest, final NbiRestApplication applicationId) {
            logger.debug("jms:/queue/scriptengine/request operation=send, requestId={} applicationId={}",nbiRequest.getRequestId(), applicationId);

            final EventConfigurationBuilder eventConfigurationBuilder = new EventConfigurationBuilder();
            eventConfigurationBuilder.addEventProperty("application", applicationId.getName());
            final EventConfiguration additionalMessageProperties = eventConfigurationBuilder.build();

            final Event event = scriptengineRequestChannel.createEvent(nbiRequest, nbiRequest.getRequestId());
            scriptengineRequestChannel.send(event,additionalMessageProperties);
        }

    }
