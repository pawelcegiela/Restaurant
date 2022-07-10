package pi.restaurant.management

import android.os.Bundle
import pi.restaurant.management.databinding.ActivityWorkersBinding

class WorkersActivity : BaseActivity() {
    private lateinit var binding: ActivityWorkersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.workers)
    }
}