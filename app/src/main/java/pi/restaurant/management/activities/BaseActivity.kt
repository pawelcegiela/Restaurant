package pi.restaurant.management.activities

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pi.restaurant.management.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startMyData()
                true
            }
            R.id.action_log_out -> {
                logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startMyData() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun logOut() {
        Firebase.auth.signOut()
        startActivity(Intent(this, AuthenticationActivity::class.java))
        finish()
    }
}