package com.fatec.crud_estoque.routes

import com.fatec.crud_estoque.StockItem
import com.fatec.crud_estoque.StockSummary
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.rpc
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

fun Route.stockRoutes(supabase: SupabaseClient) {
    route("/stock") {
        get {
            try {
                val stockItems = supabase.postgrest["stock_items"]
                    .select()
                    .decodeList<StockItem>()
                call.respond(HttpStatusCode.OK, stockItems)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        get("/summary") {
            try {
                // Assuming there is a view named "stock_summary" created in Supabase
                // that groups by product_id and joins with products to get product_name
                // and sums the quantity.
                val summary = supabase.postgrest["stock_summary"]
                    .select()
                    .decodeList<StockSummary>()
                call.respond(HttpStatusCode.OK, summary)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing id")
            try {
                val stockItem = supabase.postgrest["stock_items"]
                    .select {
                        filter {
                            eq("id", id)
                        }
                    }.decodeSingleOrNull<StockItem>()
                
                if (stockItem != null) {
                    call.respond(HttpStatusCode.OK, stockItem)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Stock item not found")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        post {
            try {
                val stockItem = call.receive<StockItem>()
                val newStockItem = supabase.postgrest["stock_items"]
                    .insert(stockItem) {
                        select()
                    }.decodeSingle<StockItem>()
                call.respond(HttpStatusCode.Created, newStockItem)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Missing id")
            try {
                val stockItem = call.receive<StockItem>()
                val updatedStockItem = supabase.postgrest["stock_items"]
                    .update(stockItem) {
                        filter {
                            eq("id", id)
                        }
                        select()
                    }.decodeSingleOrNull<StockItem>()
                
                if (updatedStockItem != null) {
                    call.respond(HttpStatusCode.OK, updatedStockItem)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Stock item not found")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing id")
            try {
                supabase.postgrest["stock_items"].delete {
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
