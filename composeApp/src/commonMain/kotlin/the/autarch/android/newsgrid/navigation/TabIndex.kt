package the.autarch.android.newsgrid.navigation

enum class TabIndex {

    CHANNELS,
    BOOKMARKS,
    ;

    val title: String
        get() = when(this) {
            CHANNELS -> "Channels"
            BOOKMARKS -> "Bookmarks"
        }

    val idxVal: Int
        get() = when(this) {
            CHANNELS -> 0
            BOOKMARKS -> 1
        }
}