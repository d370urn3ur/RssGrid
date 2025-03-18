package the.autarch.android.newsgrid

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform