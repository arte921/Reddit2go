package arte921.reddit2go

import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject


class NamedStringIndented(val op: String, val comment: String, val Indentation: Int)

var nsi: MutableList<NamedStringIndented> = mutableListOf()

lateinit var queue: RequestQueue //filled in mainactivity

fun getjson(url: String, callback: (result: JSONObject, flag: Int) -> Unit) {
    // JSON_ERROR, FAILED, NO_RESULTS, SUCCESS

    Log.i("url",url)
    val missionsRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
        if(response.getJSONObject("data").getInt("dist") > 0){
            callback(response, SUCCESS)
        } else callback(JSONObject("{}"), NO_RESULTS)
        }, Response.ErrorListener { error ->
        Log.e("JSON request error", error.toString())
        when(error){
            is ParseError -> callback(JSONObject("{}"), JSON_ERROR)
            is NoConnectionError -> callback(JSONObject("{}"), FAILED)
            else -> callback(JSONObject(), FAILED)
        }


        })
    queue.add(missionsRequest)
}

val DefaultSearchAmount = 10
val DefaultViewAmount = 100

/*
class ImageViewModel(override val coroutineContext: CoroutineContext) : ViewModel(), CoroutineScope {
    lateinit var bmp: Bitmap

    fun startHere(url: String){
        viewModelScope.launch{
            getOne(url)
        }
    }

    private suspend fun getOne(url: String) = withContext(Dispatchers.IO){
        bmp = BitmapFactory.decodeStream( URL(url).openConnection().getInputStream())

    }
}*/







/*
fun examineComment(cjo: JSONObject, indentation: Int){
    if(indentation > 0) nsi.add(NamedStringIndented("u/${cjo.get("author")}",cjo.get("body").toString(),indentation))
    val children = cjo.getJSONObject("data").getJSONArray("children")
    for(i in 0 until children.length()){
        examineComment(children.getJSONObject(i),indentation+1)
    }
}

fun examineThread(cjo: JSONObject){

}
*/

const val SUCCESS = 0
const val FAILED = SUCCESS + 1
const val LOADING = FAILED + 1
const val STORAGE_ERROR = LOADING + 1
const val PLEASE_ENTER = STORAGE_ERROR + 1
const val JSON_ERROR = PLEASE_ENTER + 1
const val NO_RESULTS = JSON_ERROR + 1

const val MEDIA_ONLY = 0
const val THREAD = MEDIA_ONLY + 1
const val MEDIA_OR_THREAD = THREAD + 1

const val WRITE_PERMISSION_CODE = 0

const val NEW = 0
const val TOP = NEW + 1
const val HOT = TOP + 1

const val HOUR = 0
const val DAY = HOUR + 1
const val WEEK = DAY + 1
const val MONTH = WEEK + 1
const val YEAR = MONTH + 1
const val ALL = YEAR + 1

const val ISHEADER = 0
const val ISPROPERTY = ISHEADER + 1

const val SUBNAME = 0


