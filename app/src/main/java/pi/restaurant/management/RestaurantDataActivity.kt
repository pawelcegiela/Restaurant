package pi.restaurant.management

import android.os.Bundle
import pi.restaurant.management.databinding.ActivityRestaurantDataBinding

class RestaurantDataActivity : BaseActivity() {
    private lateinit var binding: ActivityRestaurantDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestaurantDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.restaurant_data)
    }
}