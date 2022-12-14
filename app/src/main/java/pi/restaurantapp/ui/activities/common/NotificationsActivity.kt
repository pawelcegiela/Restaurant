package pi.restaurantapp.ui.activities.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ActivityNotificationsBinding

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for NotificationsActivity
 * @see pi.restaurantapp.viewmodels.activities.common.NotificationsViewModel ViewModel layer
 */
class NotificationsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragmentNotifications)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}