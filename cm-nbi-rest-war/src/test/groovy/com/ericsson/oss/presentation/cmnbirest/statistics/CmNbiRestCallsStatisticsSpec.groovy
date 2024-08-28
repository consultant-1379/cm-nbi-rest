package com.ericsson.oss.presentation.cmnbirest.statistics


import com.ericsson.cds.cdi.support.rule.ObjectUnderTest
import com.ericsson.cds.cdi.support.spock.CdiSpecification
import spock.lang.Unroll

class CmNbiRestCallsStatisticsSpec extends CdiSpecification {
    @ObjectUnderTest
    CmNbiRestCallsStatistics objUnderTest

    @Unroll
    def 'invoke showStatistics' () {
        given:
        objUnderTest.nbiCrudActiveRequests    = nbiCrudActiveRequests
        objUnderTest.nbiCrudMaxActiveRequests = nbiCrudMaxActiveRequests
        objUnderTest.nbiCrudGetActiveRequests = nbiCrudGetActiveRequests
        objUnderTest.nbiCrudTotalRequests     = nbiCrudTotalRequests
        objUnderTest.nbiCrudPutActiveRequests = nbiCrudPutActiveRequests
        objUnderTest.nbiCrudPostActiveRequests = nbiCrudPostActiveRequests
        objUnderTest.nbiCrudDeleteActiveRequests = nbiCrudDeleteActiveRequests
        objUnderTest.nbiCrudPatchActiveRequests = nbiCrudPatchActiveRequests

        when:
        objUnderTest.incrementNbiCrudGetActiveRequests()
        objUnderTest.incrementNbiCrudPutActiveRequests()
        objUnderTest.incrementNbiCrudPostActiveRequests()
        objUnderTest.incrementNbiCrudDeleteActiveRequests()
        objUnderTest.incrementNbiCrudPatchActiveRequests()

        then:
        objUnderTest.getNbiCrudActiveRequests() == expectednbiCrudActiveRequests
        objUnderTest.getNbiCrudMaxActiveRequests() == expectednbiCrudMaxActiveRequests
        objUnderTest.getNbiCrudGetActiveRequests() == expectednbiCrudGetActiveRequests
        objUnderTest.getNbiCrudTotalRequests() == expectednbiCrudTotalRequests
        objUnderTest.getNbiCrudPutActiveRequests() == expectednbiCrudPutActiveRequests
        objUnderTest.getNbiCrudPostActiveRequests() == expectednbiCrudPostActiveRequests
        objUnderTest.getNbiCrudDeleteActiveRequests() == expectednbiCrudDeleteActiveRequests
        objUnderTest.getNbiCrudPatchActiveRequests() == expectednbiCrudPatchActiveRequests

        where:
        nbiCrudActiveRequests | expectednbiCrudActiveRequests | nbiCrudMaxActiveRequests | expectednbiCrudMaxActiveRequests | nbiCrudGetActiveRequests  | expectednbiCrudGetActiveRequests | nbiCrudPutActiveRequests | expectednbiCrudPutActiveRequests | nbiCrudPostActiveRequests | expectednbiCrudPostActiveRequests | nbiCrudDeleteActiveRequests | expectednbiCrudDeleteActiveRequests | nbiCrudPatchActiveRequests | expectednbiCrudPatchActiveRequests | nbiCrudTotalRequests | expectednbiCrudTotalRequests | _
        0                     | 5                             | 0                        | 5                                | 0                         | 1                                |  0                       | 1                                | 0                         | 1                                 | 0                           | 1                                   | 0                          | 1                                  | 0                    | 5                            | _
        0                     | 5                             | 10                       | 10                               | 0                         | 1                                |  0                       | 1                                | 0                         | 1                                 | 0                           | 1                                   | 0                          | 1                                  | 0                    | 5                            | _
        0                     | 5                             | 10                       | 10                               | 0                         | 1                                |  0                       | 1                                | 0                         | 1                                 | 0                           | 1                                   | 0                          | 1                                  | Long.MAX_VALUE -1   | 5                            | _
    }

    def 'invoke resetNbiCrudTotalRequests' () {
        given:
        objUnderTest.nbiCrudTotalRequests  = 1000
        when:
        objUnderTest.resetNbiCrudTotalRequests()
        then:
        objUnderTest.getNbiCrudTotalRequests() == 0

  }

    def 'invoke resetNbiCrudMaxActiveRequests' () {
        given:
        objUnderTest.nbiCrudTotalRequests  = 1000
        when:
        objUnderTest.resetNbiCrudMaxActiveRequests()
        then:
        objUnderTest.getNbiCrudMaxActiveRequests() == 0

    }

}
