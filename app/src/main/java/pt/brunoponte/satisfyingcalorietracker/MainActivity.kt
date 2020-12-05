package pt.brunoponte.satisfyingcalorietracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import pt.brunoponte.satisfyingcalorietracker.data.AppData

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val TAG = "MainActivity"

    lateinit var appData: AppData

    // Reference to self
    private lateinit var mActivity: MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this
        appData = AppData()
        appData.load(mActivity)

        // In start, app has no saved state
        if (savedInstanceState == null) {
            openDashboardFragment()
        }
    }

    override fun onPause() {
        super.onPause()
        appData.save(mActivity)
    }

    override fun onResume() {
        super.onResume()
        appData.load(mActivity)
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