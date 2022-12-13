package pi.restaurantapp.ui.activities.management

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import pi.restaurantapp.R
import pi.restaurantapp.databinding.ActivityDiscountsBinding

/**
 * Class responsible for direct communication and displaying information to the user (View layer) for DiscountsActivity
 * @see pi.restaurantapp.viewmodels.activities.management.DiscountsViewModel ViewModel layer
 */
class DiscountsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiscountsBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiscountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentDiscounts) as NavHostFragment

        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayoutDiscounts)

        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView: NavigationView = findViewById(R.id.nav_view_discounts)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragmentDiscounts)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}