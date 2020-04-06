package arte921.reddit2go

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    fun launchSub(view: View) = this.startActivity(Intent(this,SubscriptionActivity()::class.java))
    fun launchDownload(view: View) = this.startActivity(Intent(this, DownloadActivity()::class.java))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queue = RequestQueue(DiskBasedCache(cacheDir,1024*1024), BasicNetwork(HurlStack())).apply{start()}


        //TESTONLY
        launchSub(search)//TESTONLY
        //TESTONLY
    }
}
