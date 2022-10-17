package dmytruek.swayconnectandroid

import okhttp3.*
import okio.IOException


fun sendEvent(client: OkHttpClient, addr: String) {
    val request = Request.Builder()
        .url(addr)
        .build()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            //Log.i("mytag", "onFailure")
            e.printStackTrace()
        }
        override fun onResponse(call: Call, response: Response) {
            //Log.i("mytag", "onResponse")
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
            }
        }
    })
}
