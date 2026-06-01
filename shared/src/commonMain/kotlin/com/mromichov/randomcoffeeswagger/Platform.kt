package com.mromichov.randomcoffeeswagger

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform