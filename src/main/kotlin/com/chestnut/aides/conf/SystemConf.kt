package com.chestnut.aides.conf
enum class Env {
    MAC,WINDOWS,LINUX,UNKNOWN;
}
object SystemConf {
    val env = getOperatingSystem() 
}

fun getOperatingSystem(): Env {
    val os: String = System.getProperty("os.name").toLowerCase()
    return when {
        os.contains("win") -> Env.WINDOWS
        os.contains("mac") -> Env.MAC
        os.contains("nix") || os.contains("nux") || os.contains("aix") -> Env.LINUX
        else -> Env.UNKNOWN
    }
}