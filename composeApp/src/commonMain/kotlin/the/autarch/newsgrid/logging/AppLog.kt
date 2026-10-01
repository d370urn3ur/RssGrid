package the.autarch.newsgrid.logging

import com.diamondedge.logging.KmLog
import com.diamondedge.logging.logging

object AppLog {

    enum class Category {

        APP, NETWORK, UI, DB
        ;

        val logger: KmLog
            get() = when (this) {
                APP -> logging("app")
                NETWORK -> logging("network")
                UI -> logging("ui")
                DB -> logging("db")
            }
    }

    fun verbose(msg: () -> Any?, category: Category = Category.APP) {
        category.logger.verbose(msg)
    }

    fun debug(msg: () -> Any?, category: Category = Category.APP) {
        category.logger.d(msg = msg)
    }

    fun info(msg: () -> Any?, category: Category = Category.APP) {
        category.logger.i(msg = msg)
    }

    fun warning(error: Throwable?, msg: () -> Any?, category: Category = Category.APP) {
        category.logger.w(err = error, msg = msg)
    }

    fun error(error: Throwable?, msg: () -> Any?, category: Category = Category.APP) {
        category.logger.e(err = error, msg = msg)
    }
}