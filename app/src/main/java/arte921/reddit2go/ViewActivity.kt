package arte921.reddit2go

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.viewactivity.*
import org.json.JSONArray

var posts = JSONArray("[]")

class ViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewactivity)

        val subname = intent.getStringExtra(SUBNAME.toString())
        getjson("https://www.reddit.com/r/$subname/hot/.json?limit=$DefaultViewAmount") { response, responseFlag ->
            flag = responseFlag
            when(flag){
                SUCCESS -> {
                    posts = response.getJSONObject("data").getJSONArray("children")
                    postsRv.adapter?.notifyDataSetChanged()
                }
                LOADING,PLEASE_ENTER -> null
                else -> Toast.makeText(this, when(flag){
                    LOADING -> "Loading..."
                    FAILED -> "Search failed. Check your connection."
                    JSON_ERROR, NO_RESULTS -> "No results."
                    else -> throw(IllegalArgumentException(flag.toString()))
                }, Toast.LENGTH_LONG).show()
            }
        }

        val viewManager = LinearLayoutManager(this)
        postsRv.apply{
            layoutManager = viewManager
            adapter = PostCardAdapter()
        }

    }
}