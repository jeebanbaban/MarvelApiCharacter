package marvel.api.marvelcharacters.ui.entry

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import marvel.api.marvelcharacters.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        setUI()
    }

    private fun initUI() {
        navController = (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).findNavController()
    }

    private fun setUI() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.splashFragment -> hideHeader()
                else -> showHeader()
            }
        }

    }


    private fun showHeader() {
        showStatusbar()
        findViewById<RelativeLayout>(R.id.rlSiteHeader).visibility = View.VISIBLE
    }

    private fun hideHeader() {
        hideStatusBar()
        findViewById<RelativeLayout>(R.id.rlSiteHeader).visibility = View.GONE
    }

    private fun showStatusbar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun hideStatusBar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

}