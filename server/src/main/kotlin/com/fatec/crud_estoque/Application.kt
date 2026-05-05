package com.fatec.crud_estoque

import com.fatec.crud_estoque.routes.productRoutes
import com.fatec.crud_estoque.routes.stockRoutes
import io.github.cdimascio.dotenv.dotenv
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val env = dotenv {
        ignoreIfMissing = true
    }

    val supabaseUrl = env["SUPABASE_URL"] ?: System.getenv("SUPABASE_URL") ?: throw IllegalArgumentException("Missing SUPABASE_URL environment variable")
    val supabaseKey = env["SUPABASE_KEY"] ?: System.getenv("SUPABASE_KEY") ?: throw IllegalArgumentException("Missing SUPABASE_KEY environment variable")

    val supabase = createSupabaseClient(
        supabaseUrl = supabaseUrl,
        supabaseKey = supabaseKey
    ) {
        install(Postgrest)
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        })
    }

    routing {
        get("/") {
            call.respondText("API de Controle de Estoque (Estocadão) - Ktor")
        }
        
        productRoutes(supabase)
        stockRoutes(supabase)
    }
}