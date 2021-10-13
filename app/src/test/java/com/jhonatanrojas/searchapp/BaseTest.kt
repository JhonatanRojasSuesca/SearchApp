package com.jhonatanrojas.searchapp

import org.junit.Rule
import org.mockito.Mockito

open class BaseTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

    inline fun <reified T : Any> getClassReference() = T::class.java

    fun assertAll(vararg conditions: Boolean) {
        conditions.forEach { assert(it) }
    }
}
