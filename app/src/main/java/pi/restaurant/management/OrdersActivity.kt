package pi.restaurant.management

import android.os.Bundle
import pi.restaurant.management.databinding.ActivityOrdersBinding

class OrdersActivity : BaseActivity() {
    private lateinit var binding: ActivityOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.orders)
    }
}