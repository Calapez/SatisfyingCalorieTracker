package pt.brunoponte.satisfyingcalorietracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import pt.brunoponte.satisfyingcalorietracker.data.Food
import kotlin.random.Random

class FoodsFragment : Fragment(R.layout.fragment_foods), SearchView.OnQueryTextListener {

    private val TAG = "FoodsFragment"

    // Declare Variables
    private lateinit var mActivity : MainActivity
    private lateinit var listViewFoods: ListView
    private lateinit var searchFoods: SearchView
    private lateinit var adapterFoods: FoodsListViewAdapter
    private lateinit var foods: MutableList<Food>

    companion object {
        fun newInstance() = FoodsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = context as MainActivity
        foods = mutableListOf(
            Food("Carbonara", 500),
            Food("Amatriciana", 550),
            Food("Honey Pasta", 650),
            Food("Pesto", 800),
            Food("Pesto1", 800),
            Food("Pesto2", 800),
            Food("Pesto3", 800),
            Food("Pesto4", 800),
            Food("Pesto5", 800),
            Food("Pesto6", 800),
            Food("Pesto7", 800),
            Food("Pesto8", 800),
            Food("Pesto9", 800),
            Food("Pesto10", 800),
            Food("Pesto11", 800),
            Food("Pesto12", 800),
            Food("Pesto13", 800),
            Food("Pesto14", 800)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_foods, container, false)

        listViewFoods = viewRoot.findViewById(R.id.listFoods)
        adapterFoods = FoodsListViewAdapter(mActivity, foods)
        listViewFoods.adapter = adapterFoods

        searchFoods = viewRoot.findViewById(R.id.searchFoods)
        searchFoods.setOnQueryTextListener(this);

        return viewRoot
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapterFoods.setFoods(getRandomFoods())
        return false
    }

    private fun getRandomFoods() : List<Food> {
        val randomFoods = mutableListOf<Food>()

        foods.forEach {
            if (Random.nextInt() % 2 == 0) {
                randomFoods.add(it)
            }
        }

        return randomFoods
    }


}