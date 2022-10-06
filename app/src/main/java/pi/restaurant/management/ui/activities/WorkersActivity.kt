package pi.restaurant.management.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import pi.restaurant.management.R
import pi.restaurant.management.databinding.ActivityWorkersBinding


class WorkersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkersBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentWorkers) as NavHostFragment

        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayoutWorkers)

        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView: NavigationView = findViewById(R.id.nav_view_workers)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragmentWorkers)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}