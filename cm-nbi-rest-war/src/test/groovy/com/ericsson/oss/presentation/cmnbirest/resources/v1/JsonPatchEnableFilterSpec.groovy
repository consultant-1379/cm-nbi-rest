package com.ericsson.oss.presentation.cmnbirest.resources.v1

import com.ericsson.cds.cdi.support.rule.ObjectUnderTest
import com.ericsson.cds.cdi.support.spock.CdiSpecification
import com.ericsson.oss.itpf.sdk.eventbus.Channel
import com.ericsson.oss.itpf.sdk.eventbus.Event
import com.ericsson.oss.presentation.cmnbirest.api.NbiResponse
import com.ericsson.oss.presentation.cmnbirest.api.NbiRestResponseHandler
import spock.lang.Unroll

import javax.inject.Inject
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.core.MediaType

import static com.ericsson.oss.presentation.cmnbirest.api.NbiRestApplication.CRUD
import static javax.ws.rs.core.Response.Status.*

class JsonPatchEnableFilterSpec extends CdiSpecification {

    @ObjectUnderTest
    JsonPatchEnableFilter objUnderTest

    ContainerRequestContext mockedContainerRequestContext = Mock(ContainerRequestContext.class)

    @Unroll
    def 'filter with PATCH request'() {
        given:
        mockedContainerRequestContext.getMethod() >> request
        mockedContainerRequestContext.getMediaType() >>  mediaType

        when:
        objUnderTest.filter(mockedContainerRequestContext)

        then:
        1 == 1

        where:
        request   | mediaType                                      | _
        "PATCH"  | MediaType.APPLICATION_JSON_PATCH_JSON_TYPE | _
        "GET"    | MediaType.APPLICATION_JSON_PATCH_JSON_TYPE | _
        "GET"    | MediaType.APPLICATION_XML                     | _
    }

    def 'dummy test to force maven to run test class with all other test methods annotated with @Unroll'() {
        when:
        objUnderTest.toString()
        then:
        1 == 1
    }

}