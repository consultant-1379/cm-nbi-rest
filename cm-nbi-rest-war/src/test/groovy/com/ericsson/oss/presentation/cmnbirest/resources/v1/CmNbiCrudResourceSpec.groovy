package com.ericsson.oss.presentation.cmnbirest.resources.v1

import com.ericsson.cds.cdi.support.rule.MockedImplementation
import com.ericsson.cds.cdi.support.rule.ObjectUnderTest
import com.ericsson.cds.cdi.support.spock.CdiSpecification
import com.ericsson.oss.itpf.sdk.eventbus.Channel
import com.ericsson.oss.itpf.sdk.eventbus.Event
import com.ericsson.oss.presentation.cmnbirest.api.NbiResponse
import com.ericsson.oss.presentation.cmnbirest.api.NbiRestResponseHandler
import com.ericsson.oss.presentation.cmnbirest.ejb.instrumentation.InstrumentationBean
import com.ericsson.oss.presentation.cmnbirest.license.LicenseValidator
import spock.lang.Unroll

import javax.ws.rs.core.Cookie
import javax.ws.rs.core.MultivaluedHashMap
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.UriInfo

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR
import static javax.ws.rs.core.Response.Status.SEE_OTHER
import static javax.ws.rs.core.Response.Status.OK
import static javax.ws.rs.core.Response.Status.FORBIDDEN
import static javax.ws.rs.core.Response.Status.NO_CONTENT

import static com.ericsson.oss.presentation.cmnbirest.api.NbiRestApplication.CRUD_STRONG
import static com.ericsson.oss.presentation.cmnbirest.api.NbiRestApplication.CRUD_MEDIUM
import static com.ericsson.oss.presentation.cmnbirest.api.NbiRestApplication.CRUD
import static com.ericsson.oss.presentation.cmnbirest.ejb.instrumentation.InstrumentationBean.GET_BASE_ONLY
import static com.ericsson.oss.presentation.cmnbirest.ejb.instrumentation.InstrumentationBean.GET_BASE_OTHER_ALL

import javax.inject.Inject

class CmNbiCrudResourceSpec extends CdiSpecification {

    @ObjectUnderTest
    CmNbiCrudResource objUnderTest

    @Inject
    NbiRestResponseHandler nbiRestResponseHandler;

    @MockedImplementation
    InstrumentationBean instrumentationBean;

    @Inject
    Channel mockedScriptEngineRequestChannel

    @MockedImplementation
    UriInfo uriInfoMock

    @MockedImplementation
    LicenseValidator licenseValidatorMock

    @MockedImplementation
    final Cookie iPlanetDirectoryProMock

    def mockedEvent = Mock(Event)
    def static BASE_ONLY = 'BASE_ONLY'
    def static BASE_ALL = 'BASE_ALL'
    def static xpath = "/SubNetwork=Ireland"
    def fields = "somefields"
    def attributes = "someattributes"
    def scopeType = "BASE_ONLY"
    def scopeLevel = 0
    def filter = "somefilter"
    def userId = "Administrator"
    def body = "SomeJsonBody"
    def ipAddress = "1.2.3.4"

    def static requestId = "someRequestId"

    def static nbiResponse = new NbiResponse(OK.getStatusCode(), "SomeJsonResponse")
    def static nbi204Response = new NbiResponse(NO_CONTENT.getStatusCode(), "SomeJsonResponse")

    private static final String DUMMY_COOKIE = "CookieAABBCCDD";

    def setup() {
        uriInfoMock.getBaseUri() >> { new URI("https://enmapache.athtem.eei.ericsson.se/enm-nbi/cm")}
        iPlanetDirectoryProMock.getValue() >> DUMMY_COOKIE
    }


    @Unroll
    def 'GET ROOT test'() {
        given:
            if (failure) {
              nbiRestResponseHandler.retrieveOperationResponse(*_) >> {throw new RuntimeException("SomeErrorMessage")}
            } else {
              nbiRestResponseHandler.retrieveOperationResponse(*_) >> responseMock
            }

            licenseValidatorMock.isValidLicense() >> validLicenseValue

        when:
            def response = objUnderTest.get(iPlanetDirectoryProMock, fields, attributes, scope, scopeLevel, filter, userId, ipAddress)

        then:
            response.status == status.getStatusCode()
            response.entity != null
            executeCommand * mockedScriptEngineRequestChannel.createEvent(*_) >> mockedEvent
            executeCommand * mockedScriptEngineRequestChannel.send(mockedEvent,
                    {eventConfiguration -> eventConfiguration.eventProperties.get("application") == applicationProp.name()})
            and: "the appropriate event counter is updated as expected"
            executeCommand * instrumentationBean.startInstrumentationMeasure()
            executeCommand * instrumentationBean.stopInstrumentationMeasure(GET_BASE_OTHER_ALL,_ , status.statusCode);

        where: "  "
        validLicenseValue       |   failure     |  responseMock  | scope       ||  status                  |   executeCommand      | applicationProp
            true                |   false       |   nbiResponse  | BASE_ONLY   ||   OK                     |         1             |  CRUD
            true                |   false       |   nbiResponse  | BASE_ALL    ||   OK                     |         1             |  CRUD_STRONG
            true                |   false       |   null         | BASE_ONLY   ||  SEE_OTHER               |         1             |  CRUD
            true                |   false       |   null         | BASE_ALL    ||  SEE_OTHER               |         1             |  CRUD_STRONG
            true                |   true        |   null         | BASE_ONLY   ||  INTERNAL_SERVER_ERROR   |         1             |  CRUD
            true                |   true        |   null         | BASE_ALL    ||  INTERNAL_SERVER_ERROR   |         1             |  CRUD_STRONG
            false               |   true        |   null         | "N/A"       ||  FORBIDDEN               |         0             |  CRUD_STRONG
    }

    @Unroll
    def 'GET wit xPath  test'() {
        given:
            if (failure) {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> {throw new RuntimeException("SomeErrorMessage")}
            } else {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> responseMock
            }

            licenseValidatorMock.isValidLicense() >> validLicenseValue

        when:
            def response = objUnderTest.get(iPlanetDirectoryProMock, xpath, fields, attributes, scope, scopeLevel, filter, userId, ipAddress)

        then:
            response.status == status.getStatusCode()
            response.entity != null
            executeCommand * mockedScriptEngineRequestChannel.createEvent(*_) >> mockedEvent
            executeCommand * mockedScriptEngineRequestChannel.send(mockedEvent,
                    {eventConfiguration -> eventConfiguration.eventProperties.get("application") == applicationProp.name()})
        and: "the appropriate event counter is updated as expected"
            executeCommand * instrumentationBean.startInstrumentationMeasure()
            executeCommand * instrumentationBean.stopInstrumentationMeasure(instrFlag,_ , status.statusCode);

        where: " test parameter "
        validLicenseValue   |   failure     |  responseMock  | scope       ||  status                  |   executeCommand      | applicationProp | instrFlag
        true                |   false       |   nbiResponse  | BASE_ONLY   ||   OK                     |         1             |  CRUD           |  GET_BASE_ONLY
        true                |   false       |   nbiResponse  | BASE_ALL    ||   OK                     |         1             |  CRUD_MEDIUM    |  GET_BASE_OTHER_ALL
        true                |   false       |   null         | BASE_ONLY   ||  SEE_OTHER               |         1             |  CRUD           |  GET_BASE_ONLY
        true                |   false       |   null         | BASE_ALL    ||  SEE_OTHER               |         1             |  CRUD_MEDIUM    |  GET_BASE_OTHER_ALL
        true                |   true        |   null         | BASE_ONLY   ||  INTERNAL_SERVER_ERROR   |         1             |  CRUD           |  GET_BASE_ONLY
        true                |   true        |   null         | BASE_ALL    ||  INTERNAL_SERVER_ERROR   |         1             |  CRUD_MEDIUM    |  GET_BASE_OTHER_ALL
        false               |   true        |   null         | "N/A"       ||  FORBIDDEN               |         0             |  CRUD_STRONG    |  GET_BASE_OTHER_ALL
    }


    @Unroll
    def 'GET POLLING test'() {
        given:
            if (failure) {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> {throw new RuntimeException("SomeErrorMessage")}
            } else {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> responseMock
            }

        when:
            def response = objUnderTest.responsePolling(requestIdentifier, userId)

        then:
            response.status == status.getStatusCode()
            response.entity != null

        where: "test parameter  "
        requestIdentifier   |   failure     |  responseMock  ||  status
           requestId        |   false       |   nbiResponse  ||   OK
           requestId        |   false       |   null         ||  SEE_OTHER
           null             |   true        |   null         ||  INTERNAL_SERVER_ERROR
           requestId        |   true        |   null         ||  INTERNAL_SERVER_ERROR
    }

    @Unroll
    def 'PUT Modify test'() {
        given:
            if (failure) {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> {throw new RuntimeException("SomeErrorMessage")}
            } else {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> responseMock
            }
            licenseValidatorMock.isValidLicense() >> validLicenseValue
        when:
            def response = objUnderTest.putmpj(iPlanetDirectoryProMock, xpath, userId, ipAddress, body)

        then:
            response.status == status.getStatusCode()
            response.entity != null
        and:
            executeOper * mockedScriptEngineRequestChannel.createEvent(*_) >> mockedEvent
            executeOper * mockedScriptEngineRequestChannel.send(mockedEvent,
                {eventConfiguration -> eventConfiguration.eventProperties.get("application") == CRUD.name()})
        and: "the appropriate event counter is updated as expected"
            executeOper * instrumentationBean.startInstrumentationMeasure()
            executeOper * instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.PUT_MODIFY,_ , status.statusCode);

        where: "test parameter  "
        validLicenseValue   |   failure     |  responseMock  ||  status                 | executeOper
            true            |   false       |   nbiResponse  ||   OK                    |     1
            true            |   false       |   null         ||  SEE_OTHER              |     1
            true            |   true        |   null         ||  INTERNAL_SERVER_ERROR  |     1
            false           |   true        |   null         ||  FORBIDDEN              |     0
    }

    @Unroll
    def 'PUT Create test'() {
        given:
            if (failure) {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> {throw new RuntimeException("SomeErrorMessage")}
            } else {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> responseMock
            }
            licenseValidatorMock.isValidLicense() >> validLicenseValue
        when:
            def response = objUnderTest.putj(iPlanetDirectoryProMock, xpath, userId, ipAddress, body)

        then:
            response.status == status.getStatusCode()
            response.entity != null
        and:
            executeOper * mockedScriptEngineRequestChannel.createEvent(*_) >> mockedEvent
            executeOper * mockedScriptEngineRequestChannel.send(mockedEvent,
                {eventConfiguration -> eventConfiguration.eventProperties.get("application") == CRUD.name()})
        and: "the appropriate event counter is updated as expected"
            executeOper * instrumentationBean.startInstrumentationMeasure()
            executeOper * instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.PUT_MODIFY,_ , status.statusCode);

        where: "test parameter  "
        validLicenseValue   |   failure     |  responseMock  ||  status                 | executeOper
        true                |   false       |   nbiResponse  ||   OK                    |     1
        true                |   false       |   null         ||  SEE_OTHER              |     1
        true                |   true        |   null         ||  INTERNAL_SERVER_ERROR  |     1
        false               |   false       |   null         ||  FORBIDDEN              |     0
    }

    @Unroll
    def 'POST Create test'() {
        given:
            if (failure) {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> {throw new RuntimeException("SomeErrorMessage")}
            } else {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> responseMock
            }
            licenseValidatorMock.isValidLicense() >> validLicenseValue
        when:
            def response
            if (baseObject == null) {
                response = objUnderTest.postj(iPlanetDirectoryProMock, userId, ipAddress, body)
            } else {
                response = objUnderTest.postj(iPlanetDirectoryProMock, baseObject, userId, ipAddress, body)
            }

        then:
            response.status == status.getStatusCode()
            response.entity != null
        and:
            executeOper * mockedScriptEngineRequestChannel.createEvent(*_) >> mockedEvent
            executeOper * mockedScriptEngineRequestChannel.send(mockedEvent,
                {eventConfiguration -> eventConfiguration.eventProperties.get("application") == CRUD.name()})
        and: "the appropriate event counter is updated as expected"
            executeOper * instrumentationBean.startInstrumentationMeasure()
            executeOper * instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.POST,_ , status.statusCode);

        where: "test parameter  "
        validLicenseValue   |   baseObject  |   failure     |  responseMock  ||  status                 | executeOper
        true                |   null        |   false       |   nbiResponse  ||   OK                    |     1
        true                |   null        |   false       |   null         ||  SEE_OTHER              |     1
        true                |   null        |   true        |   null         ||  INTERNAL_SERVER_ERROR  |     1
        false               |   null        |   false       |   null         ||  FORBIDDEN              |     0
        true                |   xpath       |   false       |   nbiResponse  ||   OK                    |     1
        true                |   xpath       |   false       |   null         ||  SEE_OTHER              |     1
        true                |   xpath       |   true        |   null         ||  INTERNAL_SERVER_ERROR  |     1
        false               |   xpath       |   false       |   null         ||  FORBIDDEN              |     0
    }

    @Unroll
    def 'DELETE test'() {
        given:
            if (failure) {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> {throw new RuntimeException("SomeErrorMessage")}
            } else {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> responseMock
            }
            licenseValidatorMock.isValidLicense() >> validLicenseValue
        when:
            def response = objUnderTest.delete(iPlanetDirectoryProMock, xpath, scopeType, scopeLevel, filter, userId, ipAddress)

        then:
            response.status == status.getStatusCode()

        and:
            executeOper * mockedScriptEngineRequestChannel.createEvent(*_) >> mockedEvent
            executeOper * mockedScriptEngineRequestChannel.send(mockedEvent,
                {eventConfiguration -> eventConfiguration.eventProperties.get("application") == CRUD.name()})
        and: "the appropriate event counter is updated as expected"
            executeOper * instrumentationBean.startInstrumentationMeasure()
            executeOper * instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.DELETE,_ , status.statusCode);

        where: "test parameter  "
        validLicenseValue   |   failure     |  responseMock  ||  status                 | executeOper
        true                |   false       |   nbiResponse  ||   OK                    |     1
        true                |   false       | nbi204Response ||   NO_CONTENT            |     1
        true                |   false       |   null         ||  SEE_OTHER              |     1
        true                |   true        |   null         ||  INTERNAL_SERVER_ERROR  |     1
        false               |   false       |   null         ||  FORBIDDEN              |     0
    }

    @Unroll
    def '3GPP Json Patch test'() {
        given:
            if (failure) {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> {throw new RuntimeException("SomeErrorMessage")}
            } else {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> responseMock
            }
            licenseValidatorMock.isValidLicense() >> validLicenseValue
        when:
            def response
            if (baseObject == null) {
                response = objUnderTest.my3gppjsonpatch(iPlanetDirectoryProMock, userId, ipAddress, body)
            } else {
                response = objUnderTest.my3gppjsonpatch(iPlanetDirectoryProMock, baseObject, userId, ipAddress, body)
            }

        then:
            response.status == status.getStatusCode()
            response.entity != null
        and:
            executeOper * mockedScriptEngineRequestChannel.createEvent(*_) >> mockedEvent
            executeOper * mockedScriptEngineRequestChannel.send(mockedEvent,
                {eventConfiguration -> eventConfiguration.eventProperties.get("application") == CRUD.name()})
        and: "the appropriate event counter is updated as expected"
            executeOper * instrumentationBean.startInstrumentationMeasure()
            executeOper * instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.PATCH_3GPP_JSON_PATCH,_ , status.statusCode);

        where: "test parameter  "
        validLicenseValue   |   baseObject  |   failure     |  responseMock  ||  status                 | executeOper
        true                |   null        |   false       |   nbiResponse  ||   OK                    |     1
        true                |   null        |   false       |   null         ||  SEE_OTHER              |     1
        true                |   null        |   true        |   null         ||  INTERNAL_SERVER_ERROR  |     1
        false               |   null        |   false       |   null         ||  FORBIDDEN              |     0
        true                |   xpath       |   false       |   nbiResponse  ||   OK                    |     1
        true                |   xpath       |   false       |   null         ||  SEE_OTHER              |     1
        true                |   xpath       |   true        |   null         ||  INTERNAL_SERVER_ERROR  |     1
        false               |   xpath       |   false       |   null         ||  FORBIDDEN              |     0
    }

    @Unroll
    def 'Json Patch test'() {
        given:
            if (failure) {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> {throw new RuntimeException("SomeErrorMessage")}
            } else {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> responseMock
            }
            licenseValidatorMock.isValidLicense() >> validLicenseValue
        when:
            def response = objUnderTest.myjsonpatch(iPlanetDirectoryProMock, xpath, userId, ipAddress, body)

        then:
            response.status == status.getStatusCode()

        and:
            executeOper * mockedScriptEngineRequestChannel.createEvent(*_) >> mockedEvent
            executeOper * mockedScriptEngineRequestChannel.send(mockedEvent,
                {eventConfiguration -> eventConfiguration.eventProperties.get("application") == CRUD.name()})
        and: "the appropriate event counter is updated as expected"
            executeOper * instrumentationBean.startInstrumentationMeasure()
            executeOper * instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.PATCH_JSON_PATCH,_ , status.statusCode);

        where: "test parameter  "
        validLicenseValue   |   failure     |  responseMock  ||  status                 | executeOper
        true                |   false       |   nbiResponse  ||   OK                    |     1
        true                |   false       |   null         ||  SEE_OTHER              |     1
        true                |   true        |   null         ||  INTERNAL_SERVER_ERROR  |     1
        false               |   false       |   null         ||  FORBIDDEN              |     0
    }


    @Unroll
    def 'Patch fo action test'() {
        given:
            if (failure) {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> {throw new RuntimeException("SomeErrorMessage")}
            } else {
                nbiRestResponseHandler.retrieveOperationResponse(*_) >> responseMock
            }
            licenseValidatorMock.isValidLicense() >> validLicenseValue
        when:
            def response = objUnderTest.myjsonpatchaction(iPlanetDirectoryProMock, xpath, "action", userId, ipAddress, body)


        then:
            response.status == status.getStatusCode()

        and:
            executeOper * mockedScriptEngineRequestChannel.createEvent(*_) >> mockedEvent
            executeOper * mockedScriptEngineRequestChannel.send(mockedEvent,
                {eventConfiguration -> eventConfiguration.eventProperties.get("application") == CRUD.name()})

        where: "test parameter  "
        validLicenseValue   |   failure     |  responseMock  ||  status                 | executeOper
        true                |   false       |   nbiResponse  ||   OK                    |     1
        true                |   false       |   null         ||  SEE_OTHER              |     1
        true                |   true        |   null         ||  INTERNAL_SERVER_ERROR  |     1
        false               |   false       |   null         ||  FORBIDDEN              |     0
    }

    @Unroll
    def 'ScriptEngineRequestQueue Full tests / valid for all REST'() {
        given:

            licenseValidatorMock.isValidLicense() >> true
            mockedScriptEngineRequestChannel.createEvent(*_) >> mockedEvent
            mockedScriptEngineRequestChannel.send(mockedEvent,_) >> { throw  new RuntimeException("ScriptEngineRequestQueue is full")}
        when:
            def response = objUnderTest.myjsonpatchaction(iPlanetDirectoryProMock, xpath, "action", userId, ipAddress, body)
        then:
            response.status == 429

    }

    def 'validateQueryParamsForGetRequest wrong parameter'() {
        MultivaluedMap<String, String> mapParam = new MultivaluedHashMap<>()
        mapParam.add("copeType", "someScopeType")
        given:
        uriInfoMock.getQueryParameters() >> mapParam
        when:
        def response = objUnderTest.validateQueryParamsForGetRequest()
        then:
        response.booleanValue() == false
    }

    def 'validateQueryParamsForDeleteRequest wrong parameter'() {
        MultivaluedMap<String, String> mapParam = new MultivaluedHashMap<>()
        mapParam.add("copeType", "someScopeType")
        mapParam.add("filter", "someFilter")
        given:
        uriInfoMock.getQueryParameters() >> mapParam
        when:
        def response = objUnderTest.validateQueryParamsForDeleteRequest()
        then:
        response.booleanValue() == false
    }

    def 'validateQueryParamsForActionRequest wrong parameter'() {
        MultivaluedMap<String, String> mapParam = new MultivaluedHashMap<>()
        mapParam.add("action", "someAction")
        mapParam.add("filter", "someFilter")
        given:
        uriInfoMock.getQueryParameters() >> mapParam
        when:
        def response = objUnderTest.validateQueryParamsForActionRequest()
        then:
        response.booleanValue() == false
    }

    def 'validateQueryParamsForRequestWithoutParam unexpected parameter'() {
        MultivaluedMap<String, String> mapParam = new MultivaluedHashMap<>()
        mapParam.add("scopeType", "someScopeType")
        mapParam.add("filter", "someFilter")
        given:
        uriInfoMock.getQueryParameters() >> mapParam
        when:
        def response = objUnderTest.validateQueryParamsForRequestWithoutParam()
        then:
        response.booleanValue() == false
    }

    @Unroll
    def 'getSsoToken coverage only'() {
        when:
            def value = objUnderTest.getSsoToken(ssoToken)
        then:
            value == expectedValue
        where:
        ssoToken                | expectedValue |_
        null                    | null          |_
    }

}