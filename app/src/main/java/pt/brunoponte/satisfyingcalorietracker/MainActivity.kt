package pt.brunoponte.satisfyingcalorietracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val TAG = "MainActivity"

    // Reference to self
    private lateinit var mActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this

        // In start, app has no saved state
        if (savedInstanceState == null) {
            openDashboardFragment()
        }
    }

    fun openDashboardFragment() {
        replaceFragment(DashboardFragment.newInstance())
    }

    fun openFoodsFragment() {
        replaceFragment(FoodsFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, fragment)
        }
    }


}