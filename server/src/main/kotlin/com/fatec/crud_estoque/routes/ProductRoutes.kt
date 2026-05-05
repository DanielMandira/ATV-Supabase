package com.fatec.crud_estoque.routes

import com.fatec.crud_estoque.Product
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.productRoutes(supabase: SupabaseClient) {
    route("/products") {
        get {
            try {
                val products = supabase.postgrest["products"]
                    .select()
                    .decodeList<Product>()
                call.respond(HttpStatusCode.OK, products)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing id")
            try {
                val product = supabase.postgrest["products"]
                    .select {
                        filter {
                            eq("id", id)
                        }
                    }.decodeSingleOrNull<Product>()
                
                if (product != null) {
                    call.respond(HttpStatusCode.OK, product)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Product not found")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        post {
            try {
                val product = call.receive<Product>()
                val newProduct = supabase.postgrest["products"]
                    .insert(product) {
                        select()
                    }.decodeSingle<Product>()
                call.respond(HttpStatusCode.Created, newProduct)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Missing id")
            try {
                val product = call.receive<Product>()
                val updatedProduct = supabase.postgrest["products"]
                    .update(product) {
                        filter {
                            eq("id", id)
                        }
                        select()
                    }.decodeSingleOrNull<Product>()
                
                if (updatedProduct != null) {
                    call.respond(HttpStatusCode.OK, updatedProduct)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Product not found")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing id")
            try {
                // To maintain referential integrity, we should maybe check if stock items exist, but Supabase can handle it via foreign key constraints (e.g., ON DELETE CASCADE or RESTRICT).
                supabase.postgrest["products"].delete {
                    filter {
                        eq("id", id)
                    }
                }
                call.respond(HttpStatusCode.NoContent)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }
    }
}
