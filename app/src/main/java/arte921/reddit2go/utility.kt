package arte921.reddit2go

const val SUCCESS = 0
const val FAILED = SUCCESS + 1
const val LOADING = FAILED + 1
const val STORAGE_ERROR = LOADING + 1

const val MEDIA_ONLY = 0
const val THREAD = MEDIA_ONLY + 1
const val MEDIA_OR_THREAD = THREAD + 1
