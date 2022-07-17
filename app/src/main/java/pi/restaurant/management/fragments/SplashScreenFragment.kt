package pi.restaurant.management.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import pi.restaurant.management.R

@SuppressLint("CustomSplashScreen")
abstract class SplashScreenFragment : Fragment() {
    var keepSplashScreen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val splashScreen = activity?.installSplashScreen()
        splashScreen?.setKeepOnScreenCondition(SplashScreen.KeepOnScreenCondition {
            return@KeepOnScreenCondition keepSplashScreen
        })
    }

}