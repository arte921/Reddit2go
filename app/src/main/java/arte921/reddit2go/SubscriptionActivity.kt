package arte921.reddit2go

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.subscriptionactivity.*
import org.json.JSONArray

var subs = JSONArray("[]")
var flag = PLEASE_ENTER
lateinit var SubSearchContext: Context

class SubscriptionActivity : AppCompatActivity() {
    private fun buildUrl(q: String, limit: Int): String = "https://www.reddit.com/search/.json?q=$q&type=sr&limit=$limit"

    fun search(view: View) {
        flag = LOADING
        goButton.visibility = GONE
        pbSpinning.visibility = VISIBLE

        resultsRv.adapter?.notifyDataSetChanged()
        getjson(buildUrl(SearchBox.text.toString(), DefaultSearchAmount)) { response, responseFlag ->
            flag = responseFlag
            Log.i("returned",flag.toString())

            goButton.visibility = VISIBLE
            pbSpinning.visibility = GONE

            when(flag){
                SUCCESS -> {
                    subs = response.getJSONObject("data").getJSONArray("children")
                    resultsRv.adapter?.notifyDataSetChanged()
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subscriptionactivity)

        val viewManager = LinearLayoutManager(this)
        resultsRv.apply {
            adapter = SubredditSearchAdapter()
            layoutManager = viewManager
        }

        SubSearchContext = this
    }
}