package pt.brunoponte.satisfyingcalorietracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class FoodsFragment : Fragment(R.layout.fragment_foods) {

    private val TAG = "FoodsFragment"

    private lateinit var mActivity : MainActivity

    companion object {
        fun newInstance() = FoodsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = context as MainActivity

        //val someInt = requireArguments().getInt("some_int")

        //Toast.makeText(mActivity, "Int is $someInt", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_foods, container, false)
    }



}