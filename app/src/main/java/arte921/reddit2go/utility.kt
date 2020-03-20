package arte921.reddit2go

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