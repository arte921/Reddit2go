package arte921.reddit2go

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.postcard.view.*


class PostCardAdapter : RecyclerView.Adapter<PostCardAdapter.MainViewHolder>() {

    class MainViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder = MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.postcard,parent,false))

    override fun getItemCount(): Int = posts.length()

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val jo = posts.getJSONObject(position).getJSONObject("data")

        Picasso.get().load(jo.getString("url")).into(holder.view.postImage)
        holder.view.postTitle.text = jo.getString("title")

    }
}