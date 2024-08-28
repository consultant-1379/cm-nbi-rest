package com.ericsson.oss.presentation.cmnbirest.ejb

import com.ericsson.cds.cdi.support.rule.ObjectUnderTest;
import com.ericsson.cds.cdi.support.spock.CdiSpecification
import com.ericsson.oss.presentation.cmnbirest.api.NbiRestApplication
import spock.lang.Unroll;


class RestNbiSelectJmsSelectorSpec extends CdiSpecification {

    @ObjectUnderTest
    RestNbiSelectJmsSelector objUnderTest;

    @Unroll
    def 'validate getFilter with different xPath #xPath , scopeType #scopeType - filter #filter and attributes = #attributes'(xPath,scopeType, filter, attributes, expectedSelector) {
        when: ' start method'
            def result = objUnderTest.getFilter(xPath,scopeType,filter, attributes)
        then:
            result == expectedSelector
        where: 'different input xpath'
            xPath                                | scopeType        |  filter      |    attributes  ||  expectedSelector
            null                                 |  "BASE_ONLY"     |  null        |    null        ||  NbiRestApplication.CRUD
            null                                 |  "BASE_ONLY"     |  "not null"  |    null        ||  NbiRestApplication.CRUD
            null                                 |  "NOT_BASE_ONLY" |  null        |    null        ||  NbiRestApplication.CRUD_STRONG
            null                                 |  "NOT_BASE_ONLY" |  "not null"  |    null        ||  NbiRestApplication.CRUD_STRONG
            "subnetwork=test"                    | "NOT_BASE_ONLY"  |  null        |    null        ||  NbiRestApplication.CRUD_STRONG
            "subnetwork=test"                    | "NOT_BASE_ONLY"  |  "not null"  |    null        ||  NbiRestApplication.CRUD_STRONG
            "subnetwork=test"                    | "BASE_ONLY"      |  null        |    null        ||  NbiRestApplication.CRUD
            "subnetwork=test"                    | "BASE_ONLY"      |  "not null"  |    null        ||  NbiRestApplication.CRUD
            "/subnetwork=test"                   | "BASE_ONLY"      |  null        |    null        ||  NbiRestApplication.CRUD
            "/subnetwork=test"                   | "NOT_BASE_ONLY"  |  "not null"  |    null        ||   NbiRestApplication.CRUD_STRONG
            "/subnetwork=test/SUBNETWORK=a"      | "NOT_BASE_ONLY"  |  "not null"  |    null        ||  NbiRestApplication.CRUD_STRONG
            "/subnetwork=test/SUBNETWORK=a"      | "NOT_BASE_ONLY"  |  "not null"  |  "not null"    ||  NbiRestApplication.CRUD_MEDIUM
            "/subnetwork=test/MeContext=a"       | "NOT_BASE_ONLY"  |  "not null"  |    null        ||  NbiRestApplication.CRUD_MEDIUM
            "/subnetwork=test/MeContext=a"       | "NOT_BASE_ONLY"  |   null       |  "not null"    ||  NbiRestApplication.CRUD_MEDIUM
            "/subnetwork=test/MeContext=a"       | "NOT_BASE_ONLY"  |   null       |    null        ||  NbiRestApplication.CRUD_STRONG
            "/MeContext=a"                       | "BASE_ONLY"      |    null      |    null        || NbiRestApplication.CRUD
            "MeContext=a"                        | "BASE_ONLY"      |  "not null"  |    null        ||  NbiRestApplication.CRUD
            "MeContext=a"                        | "NOT_BASE_ONLY"  |  "not null"  |    null        ||  NbiRestApplication.CRUD_MEDIUM
            "MeContext=a"                        | "NOT_BASE_ONLY"  |  "not null"  |  "not null"    ||  NbiRestApplication.CRUD
            "MeContext=a"                        | "NOT_BASE_ONLY"  |  null        |  "not null"    ||  NbiRestApplication.CRUD_MEDIUM
            "MeContext=a"                        | "NOT_BASE_ONLY"  |  null        |    null        ||  NbiRestApplication.CRUD_STRONG
            "/meContext"                         | "NOT_BASE_ONLY"  |  null        |    null        ||   NbiRestApplication.CRUD_STRONG
            "/meContext"                         | "NOT_BASE_ONLY"  |  null        |  "not null"    ||   NbiRestApplication.CRUD_MEDIUM
            "meContext"                          | "NOT_BASE_ONLY"  |   null       |    null        ||  NbiRestApplication.CRUD_STRONG
            ""                                   |  "BASE_ONLY"     |    null      |    null        || NbiRestApplication.CRUD
            " "                                  |  "NOT_BASE_ONLY" |   null       |    null        ||  NbiRestApplication.CRUD_STRONG
    }
}
