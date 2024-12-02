package com.example.weatherapplication

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpRequest {
    companion object {
        public fun executeGet(targetUrl: String): String? {
            var url: URL
            var connection: HttpURLConnection? = null

            try {
                url = URL(targetUrl)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                var inStream: InputStream =
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        connection.inputStream
                    } else {
                        connection.errorStream
                    }

                val reader = BufferedReader(InputStreamReader(inStream))

                var line: String?
                val response: StringBuffer = StringBuffer()

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                    response.append("\r")
                }
                reader.close()
                return response.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
            }
        }
    }
}