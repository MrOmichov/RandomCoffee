package com.mromichov.randomcoffeeswagger.di

import com.mromichov.randomcoffeeswagger.data.api.RandomCoffeeApi
import com.mromichov.randomcoffeeswagger.data.api.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single { RandomCoffeeApi(get()) }
}