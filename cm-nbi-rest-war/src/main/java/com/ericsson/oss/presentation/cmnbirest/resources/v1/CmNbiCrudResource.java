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

package com.ericsson.oss.presentation.cmnbirest.resources.v1;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;

import com.ericsson.oss.presentation.cmnbirest.api.NbiCrudActionRequest;
import com.ericsson.oss.presentation.cmnbirest.api.NbiCrudGetRequest;
import com.ericsson.oss.presentation.cmnbirest.api.NbiCrudDeleteRequest;
import com.ericsson.oss.presentation.cmnbirest.api.NbiCrudPutRequest;
import com.ericsson.oss.presentation.cmnbirest.api.NbiCrudPostRequest;
import com.ericsson.oss.presentation.cmnbirest.api.NbiCrudPatchRequest;
import com.ericsson.oss.presentation.cmnbirest.api.PatchContentType;
import com.ericsson.oss.presentation.cmnbirest.api.NbiResponse;
import com.ericsson.oss.presentation.cmnbirest.api.NbiRestApplication;
import com.ericsson.oss.presentation.cmnbirest.api.NbiRestResponseHandler;
import com.ericsson.oss.presentation.cmnbirest.ejb.RestNbiSelectJmsSelector;
import com.ericsson.oss.presentation.cmnbirest.statistics.CmNbiRestCallsStatistics;
import com.ericsson.oss.presentation.cmnbirest.ejb.instrumentation.InstrumentationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.ericsson.oss.presentation.cmnbirest.CmRestNbiUtility;
import com.ericsson.oss.presentation.cmnbirest.ejb.RestNbiMessageSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static javax.ws.rs.core.Response.*;
import com.ericsson.oss.presentation.cmnbirest.license.LicenseValidator;

@SuppressWarnings("PMD.AvoidCatchingThrowable")
@Path("/v1")
public class CmNbiCrudResource {

    @Inject
    private CmRestNbiUtility cmRestNbiUtility;

    @Inject
    private RestNbiMessageSender restNbiMessageSender;

    @Inject
    private CmNbiRestCallsStatistics cmNbiRestCallsStatistics;

    @Inject
    InstrumentationBean instrumentationBean;

    @EServiceRef
    private NbiRestResponseHandler nbiRestResponseHandler;

    @Inject
    private RestNbiSelectJmsSelector restNbiSelectJmsSelector;

    @Context
    private UriInfo uriInfo;

    @Inject
    private LicenseValidator licenseValidator;

    private final Logger logger = LoggerFactory.getLogger(CmNbiCrudResource.class);

    private static final int HTTP_CODE_NO_CONTENT = 204;
    private static final int WAIT_INTERVAL = 200;

    private static final String QUERY_PARAM_FIELDS = "fields";
    private static final String QUERY_PARAM_ATTRIBUTES = "attributes";
    private static final String QUERY_PARAM_SCOPE_TYPE = "scopeType";
    private static final String QUERY_PARAM_SCOPE_LEVEL = "scopeLevel";
    private static final String QUERY_PARAM_FILTER = "filter";
    private static final String QUERY_PARAM_ACTION = "action";

    private static final List<String> GET_QUERY_PARAMS = Arrays.asList(QUERY_PARAM_FIELDS,QUERY_PARAM_ATTRIBUTES,QUERY_PARAM_SCOPE_TYPE,QUERY_PARAM_SCOPE_LEVEL,QUERY_PARAM_FILTER);
    private static final List<String> DELETE_QUERY_PARAMS = Arrays.asList(QUERY_PARAM_SCOPE_TYPE,QUERY_PARAM_SCOPE_LEVEL,QUERY_PARAM_FILTER);
    private static final List<String> ACTION_QUERY_PARAMS = Collections.singletonList(QUERY_PARAM_ACTION);


    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(
                        @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                        @QueryParam(QUERY_PARAM_FIELDS) final String fields,
                        @QueryParam(QUERY_PARAM_ATTRIBUTES) final String attributes,
                        @DefaultValue("BASE_ONLY") @QueryParam(QUERY_PARAM_SCOPE_TYPE) final String scopeType,
                        @QueryParam(QUERY_PARAM_SCOPE_LEVEL) final int scopeLevel,
                        @QueryParam(QUERY_PARAM_FILTER) final String filter,
                        @HeaderParam("X-Tor-UserID") final String userId,
                        @HeaderParam("x-forwarded-for") String ipAddress) {

        if (!licenseValidator.isValidLicense()) {
            return createForbiddenResponse();
        }
        final String xpath = null;
        final long startTime = instrumentationBean.startInstrumentationMeasure();
        logger.debug("GET xpath=null REST MESSAGE  xpath: {}, fields: {}, attributes: {}, scopeType: {}, scopeLevel={}, filter: {}",
                xpath, fields, attributes, scopeType, scopeLevel, filter);

        final Response response = manageNbiGetRequest(xpath,fields,attributes,scopeType,scopeLevel,filter,userId,ipAddress,iPlanetDirectoryPro);
        instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.GET_BASE_OTHER_ALL,startTime,response.getStatus());
        return response;
    }


    @GET
    @Path("/data/{xpath:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(
                        @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                        @PathParam("xpath") final String xpath,
                        @QueryParam(QUERY_PARAM_FIELDS) final String fields,
                        @QueryParam(QUERY_PARAM_ATTRIBUTES) final String attributes,
                        @DefaultValue("BASE_ONLY") @QueryParam(QUERY_PARAM_SCOPE_TYPE) final String scopeType,
                        @QueryParam(QUERY_PARAM_SCOPE_LEVEL) final int scopeLevel,
                        @QueryParam(QUERY_PARAM_FILTER) final String filter,
                        @HeaderParam("X-Tor-UserID") final String userId,
                        @HeaderParam("x-forwarded-for") String ipAddress) {

        if (!licenseValidator.isValidLicense()) {
            return createForbiddenResponse();
        }
        final long startTime = instrumentationBean.startInstrumentationMeasure();
        logger.debug("GET REST MESSAGE  xpath: {}, fields: {}, attributes: {}, scopeType: {}, scopeLevel={}, filter: {}",
                xpath, fields, attributes, scopeType, scopeLevel, filter);

        final Response response = manageNbiGetRequest(xpath,fields,attributes,scopeType,scopeLevel,filter,userId,ipAddress,iPlanetDirectoryPro);
        if ( "BASE_ONLY".equals(scopeType)) {
            instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.GET_BASE_ONLY, startTime, response.getStatus());
        } else {
            instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.GET_BASE_OTHER_ALL, startTime, response.getStatus());
        }
        return response;
    }

    @GET
    @Path("/polling/{requestid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response responsePolling(@PathParam("requestid") final String nbiRequestId,
                                    @HeaderParam("X-Tor-UserID") final String userId) {

        if (nbiRequestId == null) {
            logger.error(" GET Polling REST requestId  NULL" );
            return createInternalErrorResponse("requestId NULL");
        }
        try {
            logger.debug(" GET Polling REST requestId  {}",nbiRequestId );
            NbiResponse nbiResponse = nbiRestResponseHandler.retrieveOperationResponse(nbiRequestId,WAIT_INTERVAL);
            return createHttpResponse(nbiResponse, nbiRequestId);
        } catch (Exception e) {
            logger.error(" POLLING REST exception message = {}", e.getMessage());
            return createInternalErrorResponse(e.getMessage());
        }
    }

    @DELETE
    @Path("/data/{xpath:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(
                           @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                           @PathParam("xpath") final String xpath,
                           @DefaultValue("BASE_ONLY") @QueryParam(QUERY_PARAM_SCOPE_TYPE) final String scopeType,
                           @QueryParam(QUERY_PARAM_SCOPE_LEVEL) final int scopeLevel,
                           @QueryParam(QUERY_PARAM_FILTER) final String filter,
                           @HeaderParam("X-Tor-UserID") final String userId,
                           @HeaderParam("x-forwarded-for") String ipAddress) {

        if (!licenseValidator.isValidLicense()) {
            return createForbiddenResponse();
        }

        final long startTime = instrumentationBean.startInstrumentationMeasure();

        Response response = createInternalUnexpectedError();
        try {
            validateQueryParamsForDeleteRequest();
            logger.debug("DELETE REST MESSAGE  operation: {}, xpath: {} scopeType: {}, scopeLevel={}, filter: {}",
                "HTTP DELETE", xpath, scopeType, scopeLevel, filter);
            final String nbiRequestId = cmRestNbiUtility.generateNbiRequestId(NbiRestApplication.CRUD);
            cmNbiRestCallsStatistics.incrementNbiCrudDeleteActiveRequests();

            NbiCrudDeleteRequest nbiCrudDeleteRequest = new NbiCrudDeleteRequest(userId, nbiRequestId, ipAddress, getSsoToken(iPlanetDirectoryPro), xpath, scopeType, scopeLevel, filter);
            restNbiMessageSender.sendMessage(nbiCrudDeleteRequest, NbiRestApplication.CRUD);
            NbiResponse nbiResponse = nbiRestResponseHandler.retrieveOperationResponse(nbiRequestId, WAIT_INTERVAL);
            response = createHttpResponse(nbiResponse, nbiRequestId);
        } catch (Exception e) {
                logger.error("DELETE REST exception message = {}", e.getMessage());
                response = createInternalErrorResponse(e.getMessage());
        } finally {
            cmNbiRestCallsStatistics.decrementNbiCrudDeleteActiveRequests();
            instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.DELETE,startTime,response.getStatus());
        }
        return response;
    }

    @PUT
    @Path("/data/{xpath:.*}")
    @Consumes("application/merge-patch+json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response putmpj(
                        @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                        @PathParam("xpath") final String xpath,
                        @HeaderParam("X-Tor-UserID") final String userId,
                        @HeaderParam("x-forwarded-for") String ipAddress,
                        final String body) {

        return manageNbiPutRequest(xpath, userId, body, ipAddress, iPlanetDirectoryPro);
    }

    @PUT
    @Path("/data/{xpath:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putj(
                           @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                           @PathParam("xpath") final String xpath,
                           @HeaderParam("X-Tor-UserID") final String userId,
                           @HeaderParam("x-forwarded-for") String ipAddress,
                           final String body) {

        return manageNbiPutRequest(xpath, userId, body, ipAddress, iPlanetDirectoryPro);
    }

    @POST
    @Path("/data")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postj(
                          @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                          @HeaderParam("X-Tor-UserID") final String userId,
                          @HeaderParam("x-forwarded-for") String ipAddress,
                          final String body) {

        if (!licenseValidator.isValidLicense()) {
            return createForbiddenResponse();
        }
        final long startTime = instrumentationBean.startInstrumentationMeasure();
        final String xpath = null;
        logger.debug("POST xpath=null REST MESSAGE - 'application/json'  xpath: {}, body: {}", xpath, body);
        final Response response = manageNbiPostRequest(xpath, userId, body, ipAddress, iPlanetDirectoryPro);
        instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.POST,startTime,response.getStatus());
        return response;
    }

    @POST
    @Path("/data/{xpath:.*}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postj(
                         @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                         @PathParam("xpath") final String xpath,
                         @HeaderParam("X-Tor-UserID") final String userId,
                         @HeaderParam("x-forwarded-for") String ipAddress,
                         final String body) {

        if (!licenseValidator.isValidLicense()) {
            return createForbiddenResponse();
        }
        final long startTime = instrumentationBean.startInstrumentationMeasure();
        logger.debug("POST REST MESSAGE - 'application/json'  xpath: {}, body: {}", xpath, body);
        final Response response =  manageNbiPostRequest(xpath, userId, body, ipAddress, iPlanetDirectoryPro);
        instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.POST,startTime,response.getStatus());
        return response;
    }

    @MYJSONPATCH
    @Path("/data")
    @Consumes("application/3gpp-json-patch+json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response my3gppjsonpatch(
                                    @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                                    @HeaderParam("X-Tor-UserID") final String userId,
                                    @HeaderParam("x-forwarded-for") String ipAddress,
                                    final String body) {

        if (!licenseValidator.isValidLicense()) {
            return createForbiddenResponse();
        }
        final long startTime = instrumentationBean.startInstrumentationMeasure();
        final String xpath = null;
        logger.debug("MYJ3GPPSONPATCH xpath=null REST MESSAGE - 'application/3gpp-json-patch+json'  xpath: {}, body: {}", xpath, body);
        final Response response =   manageNbiPatchRequest(xpath, userId, body, PatchContentType.THREE_GPP_JSON_PATCH,ipAddress,iPlanetDirectoryPro);
        instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.PATCH_3GPP_JSON_PATCH ,startTime,response.getStatus());
        return response;

    }

    @MYJSONPATCH
    @Path("/data/{xpath:.*}")
    @Consumes("application/3gpp-json-patch+json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response my3gppjsonpatch(
                                    @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                                    @PathParam("xpath") final String xpath,
                                    @HeaderParam("X-Tor-UserID") final String userId,
                                    @HeaderParam("x-forwarded-for") String ipAddress,
                                    final String body) {
        if (!licenseValidator.isValidLicense()) {
            return createForbiddenResponse();
        }
        final long startTime = instrumentationBean.startInstrumentationMeasure();
        logger.debug("MYJ3GPPSONPATCH REST MESSAGE - 'application/3gpp-json-patch+json'  xpath: {}, body: {}", xpath, body);
        final Response response =    manageNbiPatchRequest(xpath, userId, body, PatchContentType.THREE_GPP_JSON_PATCH,ipAddress,iPlanetDirectoryPro);
        instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.PATCH_3GPP_JSON_PATCH ,startTime,response.getStatus());
        return response;
    }

    @MYJSONPATCH
    @Path("/data/{xpath:.*}")
    @Consumes("application/json-patch+json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response myjsonpatch(
                               @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                               @PathParam("xpath") final String xpath,
                               @HeaderParam("X-Tor-UserID") final String userId,
                               @HeaderParam("x-forwarded-for") String ipAddress,
                               final String body) {
        if (!licenseValidator.isValidLicense()) {
            return createForbiddenResponse();
        }
        final long startTime = instrumentationBean.startInstrumentationMeasure();
        logger.debug("MYJSONPATCH REST MESSAGE - 'application/json-patch+json'  xpath: {}, body: {}", xpath, body);
        final Response response = manageNbiPatchRequest(xpath, userId, body, PatchContentType.JSON_PATCH,ipAddress,iPlanetDirectoryPro);
        instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.PATCH_JSON_PATCH ,startTime,response.getStatus());
        return response;

    }

    @MYJSONPATCH
    @Path("/action/{xpath:.*}")
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response myjsonpatchaction(
                                      @CookieParam("iPlanetDirectoryPro") final Cookie iPlanetDirectoryPro,
                                      @PathParam("xpath") final String xpath,
                                      @QueryParam(QUERY_PARAM_ACTION) final String action,
                                      @HeaderParam("X-Tor-UserID") final String userId,
                                      @HeaderParam("x-forwarded-for") String ipAddress,
                                      final String body) {
        if (!licenseValidator.isValidLicense()) {
            return createForbiddenResponse();
        }
        Response response;
        try {
            validateQueryParamsForActionRequest();
            logger.debug("PATCH (ACTION) REST MESSAGE - 'application/json'  xpath: {}, action: {}, body: '{}'", xpath, action, body);
            final String nbiRequestId = cmRestNbiUtility.generateNbiRequestId(NbiRestApplication.CRUD);

            NbiCrudActionRequest nbiCrudActionRequest = new NbiCrudActionRequest(userId, nbiRequestId, ipAddress, getSsoToken(iPlanetDirectoryPro), xpath, action, body);
            restNbiMessageSender.sendMessage(nbiCrudActionRequest, NbiRestApplication.CRUD);
            NbiResponse nbiResponse = nbiRestResponseHandler.retrieveOperationResponse(nbiRequestId, WAIT_INTERVAL);

            response = createHttpResponse(nbiResponse, nbiRequestId);
        } catch (Exception e) {
            logger.error("PATCH (ACTION) REST exception message = {}", e.getMessage());
            response = createInternalErrorResponse(e.getMessage());
        }
        return response;
    }

    private Response manageNbiGetRequest(final String xpath, final String fields, final String attributes, String scopeType, final int scopeLevel,
        final String filter, final String userId,
        final String ipAddress, final Cookie iPlanetDirectoryPro) {

        Response response;
        try {
            validateQueryParamsForGetRequest();
            final String nbiRequestId = cmRestNbiUtility.generateNbiRequestId(NbiRestApplication.CRUD);

            cmNbiRestCallsStatistics.incrementNbiCrudGetActiveRequests();

            NbiCrudGetRequest nbiCrudGetRequest = new NbiCrudGetRequest(userId, nbiRequestId, ipAddress, getSsoToken(iPlanetDirectoryPro), xpath, scopeType, fields, attributes, scopeLevel, filter);
            restNbiMessageSender.sendMessage(nbiCrudGetRequest, restNbiSelectJmsSelector.getFilter(xpath,scopeType, filter,attributes));
            NbiResponse nbiResponse = nbiRestResponseHandler.retrieveOperationResponse(nbiRequestId, WAIT_INTERVAL);
            response = createHttpResponse(nbiResponse, nbiRequestId);
        } catch (Exception e) {
            logger.error("GET REST exception message = {}", e.getMessage());
            response = createInternalErrorResponse(e.getMessage());
        } finally {
            cmNbiRestCallsStatistics.decrementNbiCrudGetActiveRequests();
        }
        return response;
    }

    private Response manageNbiPostRequest(String xpath, String userId, String body,
                                          final String ipAddress, final Cookie iPlanetDirectoryPro) {
        Response response;
        try {
            validateQueryParamsForRequestWithoutParam();
            final String nbiRequestId = cmRestNbiUtility.generateNbiRequestId(NbiRestApplication.CRUD);
            cmNbiRestCallsStatistics.incrementNbiCrudPostActiveRequests();

            NbiCrudPostRequest nbiCrudPostRequest = new NbiCrudPostRequest(userId, nbiRequestId, ipAddress, getSsoToken(iPlanetDirectoryPro), xpath, body);
            restNbiMessageSender.sendMessage(nbiCrudPostRequest, NbiRestApplication.CRUD);
            NbiResponse nbiResponse = nbiRestResponseHandler.retrieveOperationResponse(nbiRequestId, WAIT_INTERVAL);

            response = createHttpResponse(nbiResponse, nbiRequestId);
        } catch (Exception e) {
            logger.error("POST REST exception message = {}", e.getMessage());
            response = createInternalErrorResponse(e.getMessage());
        } finally {
            cmNbiRestCallsStatistics.decrementNbiCrudPostActiveRequests();
        }
        return response;
    }

    private Response manageNbiPatchRequest(String xpath, String userId, String body, PatchContentType patchContentType,
                                           final String ipAddress, final Cookie iPlanetDirectoryPro) {
        Response response;
        try {
            validateQueryParamsForRequestWithoutParam();
            final String nbiRequestId = cmRestNbiUtility.generateNbiRequestId(NbiRestApplication.CRUD);
            cmNbiRestCallsStatistics.incrementNbiCrudPatchActiveRequests();

            NbiCrudPatchRequest nbiCrudPatchRequest = new NbiCrudPatchRequest(userId, nbiRequestId, ipAddress, getSsoToken(iPlanetDirectoryPro), xpath, body, patchContentType);
            restNbiMessageSender.sendMessage(nbiCrudPatchRequest, NbiRestApplication.CRUD);
            NbiResponse nbiResponse = nbiRestResponseHandler.retrieveOperationResponse(nbiRequestId, WAIT_INTERVAL);

            response = createHttpResponse(nbiResponse, nbiRequestId);
        } catch (Exception e) {
            logger.error("MYJSONPATCH REST exception message = {}", e.getMessage());
            response = createInternalErrorResponse(e.getMessage());
        } finally {
            cmNbiRestCallsStatistics.decrementNbiCrudPatchActiveRequests();
        }
        return response;
    }

    private Response manageNbiPutRequest(String xpath, String userId, String body,
                                         final String ipAddress, final Cookie iPlanetDirectoryPro) {
        if (!licenseValidator.isValidLicense()) {
            return createForbiddenResponse();
        }
        final long startTime = instrumentationBean.startInstrumentationMeasure();
        Response response = createInternalUnexpectedError();
        try {
            validateQueryParamsForRequestWithoutParam();
            logger.debug("PUT REST MESSAGE - 'application/merge-patch+json'  xpath: {}, body: {}", xpath, body);
            final String nbiRequestId = cmRestNbiUtility.generateNbiRequestId(NbiRestApplication.CRUD);
            cmNbiRestCallsStatistics.incrementNbiCrudPutActiveRequests();

            NbiCrudPutRequest nbiCrudPutRequest = new NbiCrudPutRequest(userId, nbiRequestId, ipAddress, getSsoToken(iPlanetDirectoryPro), xpath, body);
            restNbiMessageSender.sendMessage(nbiCrudPutRequest, NbiRestApplication.CRUD);
            NbiResponse nbiResponse = nbiRestResponseHandler.retrieveOperationResponse(nbiRequestId, WAIT_INTERVAL);
            response = createHttpResponse(nbiResponse, nbiRequestId);
        } catch (Exception e) {
            logger.error("PUT REST exception message = {}", e.getMessage());
            response = createInternalErrorResponse(e.getMessage());
        } finally {
            cmNbiRestCallsStatistics.decrementNbiCrudPutActiveRequests();
            instrumentationBean.stopInstrumentationMeasure(InstrumentationBean.PUT_MODIFY ,startTime,response.getStatus());
        }
        return response;
    }

    private Response createHttpResponse(final NbiResponse nbiResponse, final String nbiRequestId) {

        if (nbiResponse != null) {
            if (nbiResponse.getHttpCode() == HTTP_CODE_NO_CONTENT) {
                return status(nbiResponse.getHttpCode())
                        .build();
            } else {
                return status(nbiResponse.getHttpCode())
                        .entity(nbiResponse.getJsonContent())
                        .build();
            }
        } else {
            final String retryUri = uriInfo.getBaseUri().toString() + "v1/polling/"+nbiRequestId;
            logger.debug(" Response Not Yet Available RequestId = {}", retryUri);
            return status(Status.SEE_OTHER).header("Location",retryUri)
                    .entity(nbiRequestId)
                    .build();
        }

    }

    private Response createInternalErrorResponse(final String errorMessage) {
        String bodyErrorMessage = createErrorBody(errorMessage);
        Status errorCode = Status.INTERNAL_SERVER_ERROR;
        if (errorMessage != null && errorMessage.contains("ScriptEngineRequestQueue") && errorMessage.contains("is full")) {
            errorCode = Status.TOO_MANY_REQUESTS;
            bodyErrorMessage = createErrorBody("Rate limiting - Too many requests in progress. Retry later");
        }
        return status(errorCode).entity(bodyErrorMessage).build();
    }

    private Response createInternalUnexpectedError()  {
        final String bodyErrorMessage = createErrorBody("Unexpected Error");
        return status(Status.INTERNAL_SERVER_ERROR)
                .entity(bodyErrorMessage)
                .build();
    }
    private Response createForbiddenResponse()  {
        logger.info(" No license active for this feature");
        final String bodyErrorMessage = createErrorBody("No license active for this feature, please contact the system administrator.");
        return status(Status.FORBIDDEN)
                .entity(bodyErrorMessage)
                .build();
    }

    private boolean validateQueryParamsForGetRequest() {
        boolean valid = true;
        MultivaluedMap<String,String> queryParams = uriInfo.getQueryParameters();
        if (queryParams != null) {
            for (final String key : queryParams.keySet()) {
                if (!GET_QUERY_PARAMS.contains(key)) {
                    valid = false;
                    logger.error("GET Unexpected query Parameter: invalid parameter= {} with value= {}",key,queryParams.getFirst(key));
                }
            }
        }
        return valid;
    }

    private boolean validateQueryParamsForDeleteRequest() {
        boolean valid = true;
        MultivaluedMap<String,String> queryParams = uriInfo.getQueryParameters();
        if (queryParams != null) {
            for (final String key : queryParams.keySet()) {
                if (!DELETE_QUERY_PARAMS.contains(key)) {
                    valid = false;
                    logger.error("DELETE Unexpected query Parameter: invalid parameter= {} with value= {}",key,queryParams.getFirst(key));
                }
            }
        }
        return valid;
    }

    private boolean validateQueryParamsForActionRequest() {
        boolean valid = true;
        MultivaluedMap<String,String> queryParams = uriInfo.getQueryParameters();
        if (queryParams != null) {
            for (final String key : queryParams.keySet()) {
                if (!ACTION_QUERY_PARAMS.contains(key)) {
                    valid = false;
                    logger.error("PATCH (ACTION) Unexpected query Parameter: invalid parameter= {} with value= {}", key, queryParams.getFirst(key));
                }
            }
        }
        return valid;
    }

    private boolean validateQueryParamsForRequestWithoutParam() {
        boolean valid = true;
        MultivaluedMap<String,String> queryParams = uriInfo.getQueryParameters();
        if (queryParams != null) {
            for (final String key : queryParams.keySet()) {
                valid = false;
                logger.error("Unexpected query Parameter: invalid parameter= {} with value= {}",key,queryParams.getFirst(key));
            }
        }
        return valid;
    }

    private String createErrorBody(final String errorMessage) {
        final StringBuilder builder = new StringBuilder();
        builder.append("{\"error\":{\"errorInfo\":\"");
        builder.append(errorMessage);
        builder.append("\"}}");
        return builder.toString();
    }

    private String getSsoToken(final Cookie iPlanetDirectoryPro) {
        if (iPlanetDirectoryPro != null) {
            return iPlanetDirectoryPro.getValue();
        }
        return null;
    }
}
