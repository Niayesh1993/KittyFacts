package com.zozi.kittyfacts

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityLaunchTest {

    @Test
    fun mainActivity_launches() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // If the activity fails to start (e.g., theme/resource issues), this test will fail.
        }
    }
}

