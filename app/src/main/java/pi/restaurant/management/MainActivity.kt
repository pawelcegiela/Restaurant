package pi.restaurant.management

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.WindowCompat
import pi.restaurant.management.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false) //TODO: Co z innymi klasami?
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setButtonListeners()
    }

    private fun setButtonListeners() {
        binding.buttonRestaurantData.setOnClickListener {
            startActivity(Intent(this, RestaurantDataActivity::class.java))
        }

        binding.buttonWorkers.setOnClickListener {
            startActivity(Intent(this, WorkersActivity::class.java))
        }

        binding.buttonMenu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        binding.buttonOrders.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }

        binding.buttonIngredients.setOnClickListener {
            startActivity(Intent(this, IngredientsActivity::class.java))
        }

        binding.buttonDiscounts.setOnClickListener {
            startActivity(Intent(this, DiscountsActivity::class.java))
        }
    }
}