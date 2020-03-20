package arte921.reddit2go

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.lang.Math.random

lateinit var queue: RequestQueue

class MainActivity : AppCompatActivity() {
    lateinit var json: JSONArray
    var status = LOADING
    var context = this



    fun processAll(ja: JSONArray){
        val whatContent = MEDIA_ONLY //TODO

        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            for(i in 0 until ja.length()){
                downloadOne(ja.getJSONObject(i).getJSONObject("data"),whatContent)
            }
        }else status = STORAGE_ERROR
    }

    fun downloadOne(jo: JSONObject,whatContent: Int){

        val mediaRequest = DownloadManager.Request(Uri.parse(jo.getJSONObject("preview").getJSONArray("images").getJSONObject(0).getJSONObject("source").get("url").toString())).apply{
            setTitle(random().toString())
            setDescription("download")
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
            setDestinationUri(Uri.fromFile(File(context.getExternalFilesDir(null), random().toString())))
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
        }
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(mediaRequest)
    }

    fun getjson(sub: String, amount: Int){
        val url = "https://www.reddit.com/r/$sub/top/.json?count=$amount"
        val missionsRequest = JsonObjectRequest(Request.Method.GET,url,null, Response.Listener { response ->
            json = response.getJSONObject("data").getJSONArray("children")
            status = SUCCESS

            processAll(json)
        }, Response.ErrorListener {error ->
            status = FAILED
        })
        queue.add(missionsRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        queue = RequestQueue(DiskBasedCache(cacheDir,1024*1024), BasicNetwork(HurlStack())).apply{start()}
        getjson("memes",1)


    }
}
