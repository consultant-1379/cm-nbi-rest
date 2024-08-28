package com.ericsson.oss.presentation.cmnbirest.license

import com.ericsson.cds.cdi.support.rule.MockedImplementation
import com.ericsson.cds.cdi.support.rule.ObjectUnderTest
import com.ericsson.cds.cdi.support.spock.CdiSpecification
import com.ericsson.oss.itpf.sdk.licensing.LicensingService
import static com.ericsson.oss.itpf.sdk.licensing.Permission.ALLOWED
import static com.ericsson.oss.itpf.sdk.licensing.Permission.DENIED_NO_VALID_LICENSE

import spock.lang.Unroll

class LicenseValidatorSpec extends CdiSpecification {
    @ObjectUnderTest
    LicenseValidator objUnderTest

    private static final String LICENSE_KEY_5MHzSC = "FAT1023443";
    private static final String LICENSE_KEY_ONOFFSCOPE_RADIO = "FAT1023988";

    @MockedImplementation
    private LicensingService licensingServiceMock;

    @Unroll
    def 'validate licenses starting from previouslyValidLicense = null' () {
        given: "previouslyValidLicense setup"
            objUnderTest.previouslyValidLicense = null
        when:
            objUnderTest.validatePermissionsOnExpiryOfTimeout()

        then:
            valTimes_5MHzSC * licensingServiceMock.validatePermission(LICENSE_KEY_5MHzSC) >> license_5MHzSC
            valTimes_RADIO  * licensingServiceMock.validatePermission(LICENSE_KEY_ONOFFSCOPE_RADIO) >> license_RADIO
            valTimes_other  * licensingServiceMock.validatePermission( _ as String) >> license_other
        and: 'Expected license validation result'
            objUnderTest.previouslyValidLicense == expectedValidLicense
            objUnderTest.licenseIsValid == expectedValid

        where: ""
        license_5MHzSC           |   license_RADIO            | license_other          || expectedValidLicense         | expectedValid   | valTimes_5MHzSC | valTimes_RADIO | valTimes_other
        ALLOWED                  |   ALLOWED                  | ALLOWED                || LICENSE_KEY_5MHzSC           |  true           |      1          |         0      |    0
        DENIED_NO_VALID_LICENSE  |   DENIED_NO_VALID_LICENSE  | DENIED_NO_VALID_LICENSE|| null                         |  false          |      1          |         1      |    4
        DENIED_NO_VALID_LICENSE  |   ALLOWED                  | DENIED_NO_VALID_LICENSE|| LICENSE_KEY_ONOFFSCOPE_RADIO |  true           |      1          |         1      |    2
    }

    @Unroll
    def 'validate licenses starting from previouslyValidLicense = LICENSE_KEY_ONOFFSCOPE_RADIO' () {
        given: "previouslyValidLicense setup"
        objUnderTest.previouslyValidLicense = LICENSE_KEY_ONOFFSCOPE_RADIO
        when:
        objUnderTest.validatePermissionsOnExpiryOfTimeout()

        then:
        valTimes_5MHzSC * licensingServiceMock.validatePermission(LICENSE_KEY_5MHzSC) >> license_5MHzSC
        valTimes_RADIO  * licensingServiceMock.validatePermission(LICENSE_KEY_ONOFFSCOPE_RADIO) >> license_RADIO
        valTimes_other  * licensingServiceMock.validatePermission( _ as String) >> license_other
        and: 'Expected license validation result'
        objUnderTest.previouslyValidLicense == expectedValidLicense
        objUnderTest.licenseIsValid == expectedValid

        where: ""
        license_5MHzSC           |   license_RADIO            | license_other          || expectedValidLicense         | expectedValid   | valTimes_5MHzSC | valTimes_RADIO | valTimes_other
        ALLOWED                  |   ALLOWED                  | ALLOWED                || LICENSE_KEY_ONOFFSCOPE_RADIO |  true           |      0          |         1      |    0
        DENIED_NO_VALID_LICENSE  |   DENIED_NO_VALID_LICENSE  | DENIED_NO_VALID_LICENSE|| null                         |  false          |      1          |         1      |    4
        ALLOWED                  |   DENIED_NO_VALID_LICENSE  | DENIED_NO_VALID_LICENSE|| LICENSE_KEY_5MHzSC           |  true           |      1          |         1      |    0
    }

    @Unroll
    def 'isValidLicense  test' () {
        given: "previouslyValidLicense setup"
        objUnderTest.licenseIsValid = validLicenseSetUp
        when:
            def result = objUnderTest.isValidLicense()

        then:
        valTimes_5MHzSC * licensingServiceMock.validatePermission(LICENSE_KEY_5MHzSC) >> license_5MHzSC
        valTimes_other  * licensingServiceMock.validatePermission( _ as String) >> license_other
        and: 'Expected license validation result'
            result == expectedValid

        where: ""
        validLicenseSetUp   | license_5MHzSC            | license_other          || expectedValid   | valTimes_5MHzSC | valTimes_other
            true            |  ALLOWED                  | ALLOWED                ||  true           |      0          |    0
            false           |  DENIED_NO_VALID_LICENSE  | DENIED_NO_VALID_LICENSE||  false          |      1          |    5
            false           |  ALLOWED                  | DENIED_NO_VALID_LICENSE||  true           |      1          |    0
    }

}
