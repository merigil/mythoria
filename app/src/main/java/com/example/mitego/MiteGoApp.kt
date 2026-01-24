package com.example.mitego

import android.app.Application
import org.osmdroid.config.Configuration
import java.io.File

class MiteGoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize OSMDroid configuration
        Configuration.getInstance().userAgentValue = packageName
        // Set storage path for OSMDroid cache
        Configuration.getInstance().osmdroidBasePath = File(cacheDir, "osmdroid")
        Configuration.getInstance().osmdroidTileCache = File(Configuration.getInstance().osmdroidBasePath, "tile")
    }
}
