package com.mnclimbingcoop

import com.mnclimbingcoop.service.*

import spock.lang.Specification

class SurveyorSpec extends Specification {

    Surveyor surveyor
    AlertService alertService
    CardFormatService cardFormatService
    CardholderSurveyService cardholderSurveyService
    CredentialSurveyService credentialSurveyService
    DoorService doorService
    HidService hidService
    ParameterService parameterService
    ReaderService readerService
    ScheduleService scheduleService
    SystemService systemService

    void setup() {
        alertService = Mock()
        cardFormatService = Mock()
        cardholderSurveyService = Mock()
        credentialSurveyService = Mock()
        doorService = Mock()
        hidService = Mock()
        parameterService = Mock()
        readerService = Mock()
        scheduleService = Mock()
        systemService = Mock()

        surveyor = new Surveyor(
            alertService,
            cardFormatService,
            cardholderSurveyService,
            credentialSurveyService,
            doorService,
            hidService,
            parameterService,
            readerService,
            scheduleService,
            systemService
        )
    }

    /* From Stacktrace:
     * com.mnclimbingcoop.service.HidRemoteErrorException: null
     *     at com.mnclimbingcoop.service.HidService.get(HidService.groovy:101)
     *     at com.mnclimbingcoop.service.HidService$_getAll_closure3.doCall(HidService.groovy:85)
     *     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     *     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
     *     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
     *     at java.lang.reflect.Method.invoke(Method.java:483)
     *     at org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:90)
     *     at groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:324)
     *     at org.codehaus.groovy.runtime.metaclass.ClosureMetaClass.invokeMethod(ClosureMetaClass.java:292)
     *     at groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1016)
     *     at groovy.lang.Closure.call(Closure.java:423)
     *     at groovy.lang.Closure.call(Closure.java:439)
     *     at org.codehaus.groovy.runtime.DefaultGroovyMethods.each(DefaultGroovyMethods.java:2027)
     *     at org.codehaus.groovy.runtime.DefaultGroovyMethods.each(DefaultGroovyMethods.java:2012)
     *     at org.codehaus.groovy.runtime.DefaultGroovyMethods.each(DefaultGroovyMethods.java:2065)
     *     at com.mnclimbingcoop.service.HidService.getAll(HidService.groovy:84)
     *     at com.mnclimbingcoop.service.HidService.getAll(HidService.groovy:79)
     *     at com.mnclimbingcoop.service.ParameterService.get(ParameterService.groovy:28)
     *     at com.mnclimbingcoop.Surveyor.survey(Surveyor.groovy:67)
     *     at com.mnclimbingcoop.Surveyor$$FastClassBySpringCGLIB$$be87c403.invoke(<generated>)
     *     at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204)
     *     at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:717)
     *     at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157)
     *     at org.springframework.aop.interceptor.AsyncExecutionInterceptor$1.call(AsyncExecutionInterceptor.java:110)
     *     at java.util.concurrent.FutureTask.run(FutureTask.java:266)
     *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
     *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
     *     at java.lang.Thread.run(Thread.java:744)
     */
    void 'can retry and continue on exceptions'() {
        when:
        surveyor.survey()

        then:
        1 * alertService.list()
        1 * cardFormatService.list()
        3 * parameterService.get() >> { throw new HidRemoteErrorException('TEST ERROR') }
        1 * doorService.list()
        1 * readerService.list()
        1 * scheduleService.list()
        1 * systemService.get()
        1 * hidService.sync()
        1 * cardholderSurveyService.survey()
        1 * credentialSurveyService.survey()
        0 * _

    }

}
