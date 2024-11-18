package com.andrii_a.muze.domain

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class PlatformParcelize()

expect interface PlatformParcelable
