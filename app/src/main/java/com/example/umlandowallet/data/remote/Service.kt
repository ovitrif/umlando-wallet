package com.example.umlandowallet.data.remote

import com.example.umlandowallet.data.MerkleProof
import com.example.umlandowallet.data.OutputSpent
import com.example.umlandowallet.data.Tx
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


interface Service {

    suspend fun getLatestBlockHash() : String

    suspend fun getLatestBlockHeight() : Int

    suspend fun broadcastTx(tx: ByteArray) : String

    suspend fun getTx(txid: String) : Tx

    suspend fun getTxHex(txid: String): String

    suspend fun getHeader(hash: String) : String

    suspend fun getMerkleProof(txid: String) : MerkleProof

    suspend fun getOutputSpent(txid: String, outputIndex: Int) : OutputSpent

    suspend fun connectPeer(pubkeyHex: String, hostname: String, port: Int)

    companion object {
        fun create() : Service {
            return ServiceImpl(
                client = HttpClient {
                    install(Logging) {
                        logger = Logger.DEFAULT
                        level = LogLevel.HEADERS
                    }
                    install(ContentNegotiation) {
                        json(
                            json = Json {
                                prettyPrint = true
                                isLenient = true
                                ignoreUnknownKeys = true
                            }
                        )
                    }
                }
            )
        }
    }
}