package be.condictum.move_up.util

import java.util.concurrent.atomic.AtomicInteger

object RandomIntUtil {
    private val seed = AtomicInteger()

    fun getRandomInt()=seed.getAndIncrement()+ System.currentTimeMillis().toInt()

}