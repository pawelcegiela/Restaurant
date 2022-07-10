package pi.restaurant.management

import android.os.Bundle
import pi.restaurant.management.databinding.ActivityDiscountsBinding

class DiscountsActivity : BaseActivity() {
    private lateinit var binding: ActivityDiscountsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiscountsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.discounts)
    }
}