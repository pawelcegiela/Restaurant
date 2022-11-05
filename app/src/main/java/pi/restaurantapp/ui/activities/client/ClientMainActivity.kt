package pi.restaurantapp.ui.activities.client

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ActivityClientMainBinding

class ClientMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityClientMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentClientMain) as NavHostFragment

        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayoutClientMain)

        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView: NavigationView = findViewById(R.id.nav_view_client_main)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragmentClientMain)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}