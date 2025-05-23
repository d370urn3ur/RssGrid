package the.autarch.newsgrid

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform