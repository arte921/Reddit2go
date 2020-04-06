package arte921.reddit2go

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.downloadactivity.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class DownloadActivity : AppCompatActivity() {
    private lateinit var json: JSONArray
    private var status = LOADING
    private lateinit var backupWhileRequesting: JSONArray
    private var handled = 0
    private var total = 0
    private var sortTime = -1
    private var sort = -1
    private val whatContent = MEDIA_ONLY

    private fun buildUrl(sub: String, amount: Int): String {
        var optTime = ""
        val sortUrl = when (sort) {
            NEW -> "new"
            HOT -> "hot"
            TOP -> {
                optTime = "&t=" + when (sortTime) {
                    HOUR -> "hour"
                    DAY -> "day"
                    WEEK -> "week"
                    MONTH -> "month"
                    YEAR -> "year"
                    ALL -> "all"
                    else -> throw(IllegalArgumentException(optTime))
                }
                "top"
            }
            else -> "hot"
        }
        Log.i("url", "https://www.reddit.com/r/$sub/$sortUrl/.json?limit=$amount$optTime")
        return "https://www.reddit.com/r/$sub/$sortUrl/.json?limit=$amount$optTime"
    }



    fun download(view: View) {
        var amount = postsamount.text.toString()
        var subname = subnameinput.text.toString()
        Log.i(subname, amount)
        if (subname != "" && amount != "") {
            totalprogress.visibility = View.VISIBLE
            statusView.text = getString(R.string.loading)

            totalprogress.max = amount.toInt()
            total += amount.toInt()

            getjson(buildUrl(subname,amount.toInt())) {response,flag -> processAll(response.getJSONObject("data").getJSONArray("children")) }
        } else {
            Toast.makeText(this, getString(R.string.noinputtoast), Toast.LENGTH_LONG).show()
            statusView.text = getString(R.string.noinput)
        }

    }

    fun onTimeChanged(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.id) {
                R.id.hourradio -> if (checked) sortTime = HOUR
                R.id.dayradio -> if (checked) sortTime = DAY
                R.id.weekradio -> if (checked) sortTime = WEEK
                R.id.monthradio -> if (checked) sortTime = MONTH
                R.id.yearradio -> if (checked) sortTime = YEAR
                R.id.allradio -> if (checked) sortTime = ALL
            }
        }
    }

    fun onSortChanged(view: View) {
        Log.i("onSortChanged", "lol")
        if (view is RadioButton) {
            val checked = view.isChecked
            when (view.id) {
                R.id.newradio -> if (checked) {
                    sort = NEW
                    TimeRadioGroup.visibility = View.INVISIBLE
                }
                R.id.hotradio -> if (checked) {
                    sort = HOT
                    TimeRadioGroup.visibility = View.INVISIBLE
                }
                R.id.topradio -> if (checked) {
                    sort = TOP
                    TimeRadioGroup.visibility = View.VISIBLE
                }
            }
        }
    }

    fun handle() {
        handled++
        if (handled == total) {
            statusView.text = getString(R.string.done)
            totalprogress.progress = 0
            totalprogress.visibility = View.GONE
        }
    }

    fun logError() {
        totalprogress.max -= 1
        handle()
    }

    fun logSuccess() {
        totalprogress.incrementProgressBy(1)
        handle()
    }

    val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            logSuccess()
        }
    }

    fun processAll(ja: JSONArray) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            backupWhileRequesting = ja
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_PERMISSION_CODE)
        } else {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                for (i in 0 until ja.length()) downloadOne(ja.getJSONObject(i).getJSONObject("data"))
            } else {
                status = STORAGE_ERROR
                Log.e("", "Storage error")
            }
        }
    }

    fun downloadOne(jo: JSONObject) {
        when (whatContent) {
            MEDIA_ONLY -> {
                try {
                    val url = jo.getString("url")
                    val filename = jo.getString("title")
                        .replace(Regex("/[^a-z0-9]/i"), "_") + " | " + url.substringAfterLast("/")
                    val file = File(
                        Environment.getExternalStorageDirectory().path + "/Reddit2go/" + jo.getString("subreddit"),
                        filename.replace(Regex("/[^a-z0-9]/i"), "_")
                    )
                    if (!file.exists()) {
                        val mediaRequest = DownloadManager.Request(Uri.parse(url)).apply {
                            setTitle(filename)
                            setDescription(filename)
                            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                            setDestinationUri(Uri.fromFile(file))
                            setAllowedOverMetered(true)
                            setAllowedOverRoaming(true)
                        }
                        val downloadManager =
                            getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        downloadManager.enqueue(mediaRequest)
                    } else {
                        logError()
                    }
                } catch (_: IllegalArgumentException) {
                    logError()
                }

            }
            THREAD -> null

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_PERMISSION_CODE -> processAll(backupWhileRequesting)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.downloadactivity)

        onTimeChanged(findViewById(TimeRadioGroup.checkedRadioButtonId))
        onSortChanged(findViewById(sortRadioGroup.checkedRadioButtonId))

        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
}