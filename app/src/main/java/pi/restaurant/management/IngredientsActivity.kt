package pi.restaurant.management

import android.os.Bundle
import pi.restaurant.management.databinding.ActivityIngredientsBinding

class IngredientsActivity : BaseActivity() {
    private lateinit var binding: ActivityIngredientsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIngredientsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.ingredients)
    }
}