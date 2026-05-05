package com.fatec.crud_estoque

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform