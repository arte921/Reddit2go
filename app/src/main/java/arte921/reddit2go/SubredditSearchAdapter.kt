package arte921.reddit2go

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.subredditsearchentry.view.*


class SubredditSearchAdapter : RecyclerView.Adapter<SubredditSearchAdapter.MainViewHolder>() {

    class MainViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder = MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.subredditsearchentry,parent,false))

    override fun getItemCount(): Int{
        Log.i("counted",flag.toString())
        return when(flag){
            SUCCESS -> subs.length()
            PLEASE_ENTER, JSON_ERROR, FAILED, LOADING, NO_RESULTS -> 0
            else -> throw(IllegalArgumentException(flag.toString()))
        }
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val jo = subs.getJSONObject(position).getJSONObject("data")

        holder.view.setOnClickListener{
            val intent = Intent(SubSearchContext,ViewActivity()::class.java)
            intent.putExtra(SUBNAME.toString(),jo.getString("display_name"))
            SubSearchContext.startActivity(intent)
        }

        holder.view.subname.text = jo.getString("display_name")
    }
}