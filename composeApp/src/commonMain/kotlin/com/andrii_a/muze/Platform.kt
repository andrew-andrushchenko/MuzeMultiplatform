package com.andrii_a.muze

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform