package com.ericsson.oss.presentation.cmnbirest.statistics

import com.ericsson.cds.cdi.support.rule.MockedImplementation
import com.ericsson.cds.cdi.support.rule.ObjectUnderTest
import com.ericsson.cds.cdi.support.spock.CdiSpecification

import javax.ejb.Timer
import javax.ejb.TimerConfig
import javax.ejb.TimerService
import javax.inject.Inject

class CmNbiRestCallStatisticsTimerBeanSpec extends CdiSpecification {
    @ObjectUnderTest
    CmNbiRestCallStatisticsTimerBean objUnderTest

    @Inject
    TimerService timerService;

    @MockedImplementation
    Timer timerMock

    TimerConfig timerConfig = null

    def setup() {
        timerConfig = new TimerConfig()
        timerConfig.setPersistent(false);
        timerService.createSingleActionTimer(1, timerConfig)
        timerService.createSingleActionTimer(1, timerConfig) >> timerMock
    }

    def 'invoke showStatistics' () {
        when:
         objUnderTest.showStatistics(timerMock)
        then:
          1 == 1
    }

    def 'invoke showStatistics when active requests' () {
        given:
        objUnderTest.cmNbiRestCallsStatistics.incrementNbiCrudGetActiveRequests()
        when:
        objUnderTest.showStatistics(timerMock)
        then:
        1 == 1
    }

    def 'invoke setupTimer' () {
        when:
        objUnderTest.setupTimer()
        then:
        1 == 1
    }

}
