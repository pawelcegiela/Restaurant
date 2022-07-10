package pi.restaurant.management

import android.os.Bundle
import pi.restaurant.management.databinding.ActivityMenuBinding

class MenuActivity : BaseActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.menu)
    }
}