package arte921.reddit2go

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

const val SUCCESS = 0
const val FAILED = SUCCESS + 1
const val LOADING = FAILED + 1
const val STORAGE_ERROR = LOADING + 1

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
const val ISPROPERTY = ISHEADER+1

class NamedStringIndented(val op: String, val comment: String, val Indentation: Int)

var nsi: MutableList<NamedStringIndented> = mutableListOf()

fun examineComment(cjo: JSONObject, indentation: Int){
    if(indentation > 0) nsi.add(NamedStringIndented("u/${cjo.get("author")}",cjo.get("body").toString(),indentation))
    val children = cjo.getJSONObject("data").getJSONArray("children")
    for(i in 0 until children.length()){
        examineComment(children.getJSONObject(i),indentation+1)
    }
}

fun examineThread(cjo: JSONObject){

}

