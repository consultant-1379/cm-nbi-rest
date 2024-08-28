package com.ericsson.oss.presentation.cmnbirest.ejb

import com.ericsson.cds.cdi.support.rule.ObjectUnderTest
import com.ericsson.cds.cdi.support.spock.CdiSpecification
import com.ericsson.oss.presentation.cmnbirest.ejb.instrumentation.InstrumentationBean
import com.ericsson.oss.presentation.cmnbirest.ejb.instrumentation.InstrumentationTimerBean

import javax.inject.Inject

class InstrumentationBeanSpec extends CdiSpecification {

    @ObjectUnderTest
    InstrumentationBean instrumentationBean;

    @ObjectUnderTest
    InstrumentationBean instrumentationBean2;

    @Inject
    InstrumentationTimerBean instrumentationTimerBean;

    def 'measures have a consistent value after start and stop'() {
        given:
        def measureType = "GET_BASE_ONLY"
        instrumentationTimerBean.setupTimer()
        def startTime = instrumentationBean.startInstrumentationMeasure()
        and: 'some time elapses'
        Thread.sleep(100L)
        when: 'measure time is stopped'
        instrumentationBean.stopInstrumentationMeasure(measureType, startTime, 200)
        instrumentationTimerBean.freezeMeasures()
        then:
        instrumentationBean.getGetBaseOnlyCount() == 1
        instrumentationBean.getGetBaseOnlyTotalAvgTime() > 0
        instrumentationBean.getGetBaseOnlyTotalMaxTime() > 0
        instrumentationBean.getGetBaseOnlyAsyncResponses() == 0
    }

    def 'measures have a consistent value after double start and stop'() {
        given:
        def measureType = "GET_BASE_OTHER_ALL"
        def startTime = instrumentationBean.startInstrumentationMeasure()
        and: 'some time elapses'
        Thread.sleep(100L);
        when: 'measure time is stopped'
        instrumentationBean.stopInstrumentationMeasure(measureType, startTime, 200)
        instrumentationTimerBean.freezeMeasures()
        then:
        instrumentationBean.getGetBaseOtherAllCount() == 1
        instrumentationBean.getGetBaseOtherAllAsyncResponses() == 0
        def max1 = instrumentationBean.getGetBaseOtherAllTotalMaxTime()
        max1 > 0
        and: 'measure time is stopped and started again'
        def startTime2 = instrumentationBean.startInstrumentationMeasure()
        and: 'some time elapses again'
        Thread.sleep(200L);
        when: 'measure time is stopped again'
        instrumentationBean.stopInstrumentationMeasure(measureType, startTime2, 200)
        instrumentationTimerBean.freezeMeasures()
        def avg2 = instrumentationBean.getGetBaseOtherAllTotalAvgTime()
        def max2 = instrumentationBean.getGetBaseOtherAllTotalMaxTime()
        then:
        instrumentationBean.getGetBaseOtherAllCount() == 2
        instrumentationBean.getGetBaseOtherAllAsyncResponses() == 0
        avg2 > 0
        max2 > 0
//        avg2 == ((max1+max2) / 2).toLong()
    }

    def 'measures have a consistent value after some start and stop'() {
        given:
        def measureType = "GET_BASE_OTHER_ALL"
        def startTime = instrumentationBean.startInstrumentationMeasure()
        and: 'some time elapses'
        Thread.sleep(100L);
        when: 'measure time is stopped'
        instrumentationBean.stopInstrumentationMeasure(measureType, startTime, 200)
        instrumentationTimerBean.freezeMeasures()
        then:
        instrumentationBean.getGetBaseOtherAllCount() == 1
        instrumentationBean.getGetBaseOtherAllAsyncResponses() == 0
        def max1 = instrumentationBean.getGetBaseOtherAllTotalMaxTime()
        max1 > 0
        and: 'measure time is started and stopped again'
        def startTime2 = instrumentationBean.startInstrumentationMeasure()
        and: 'some time elapses again'
        Thread.sleep(200L);
        when: 'measure time is stopped again'
        instrumentationBean.stopInstrumentationMeasure(measureType, startTime2, 500)
        instrumentationTimerBean.freezeMeasures()
        def max2 = instrumentationBean.getGetBaseOtherAllTotalMaxTime()
        then:
        instrumentationBean.getGetBaseOtherAllCount() == 2
        instrumentationBean.getGetBaseOtherAllAsyncResponses() == 1
        max2 > 0
        and: 'measure time is started and stopped a third time'
        def startTime3 = instrumentationBean.startInstrumentationMeasure()
        and: 'some other time elapses'
        Thread.sleep(300L);
        when: 'measure time is stopped again'
        instrumentationBean.stopInstrumentationMeasure(measureType, startTime3, 200)
        instrumentationTimerBean.freezeMeasures()
        def avg3 = instrumentationBean.getGetBaseOtherAllTotalAvgTime()
        def max3 = instrumentationBean.getGetBaseOtherAllTotalMaxTime()
        then:
        instrumentationBean.getGetBaseOtherAllCount() == 3
        instrumentationBean.getGetBaseOtherAllAsyncResponses() == 0
        avg3 > 0
        max3 > 0
///        avg3 == ((max1 + max2 + max3) / 3).toLong()
    }
/*
    def 'measures have consistent values when ................. if called '() {
        given:
        def measureType1 = "GET_BASE_ONLY"
        def measureType2 = "POST"
        def startTime = instrumentationBean.startInstrumentationMeasure()
        and: 'some time elapses'
        Thread.sleep(100L);
        when: 'one measure time is stopped'
        instrumentationBean.stopInstrumentationMeasure(measureType1, startTime, 200)
        instrumentationTimerBean.freezeMeasures()
        Thread.sleep(100L)
        and: 'the other measure time is stopped'
        instrumentationBean2.stopInstrumentationMeasure(measureType2, startTime, 303)
        instrumentationTimerBean.freezeMeasures()
        then:
        instrumentationBean.getGetBaseOnlyCount() == 1
        instrumentationBean.getGetBaseOnlyTotalAvgTime() > 0
        instrumentationBean.getGetBaseOnlyTotalMaxTime() > 0
        instrumentationBean.getGetBaseOnlyAsyncResponses() == 0

        instrumentationBean.getPostCount() == 1
        instrumentationBean.getPostTotalAvgTime() > 0
        instrumentationBean.getPostTotalMaxTime() > 0
        instrumentationBean.getPostAsyncResponses() == 1
    }*/
}
