package pt.brunoponte.satisfyingcalorietracker

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.json.JSONException
import org.json.JSONObject
import pt.brunoponte.satisfyingcalorietracker.data.Api
import pt.brunoponte.satisfyingcalorietracker.data.Food
import java.io.IOException
import java.text.ParseException
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class FoodsFragment : Fragment(R.layout.fragment_foods), SearchView.OnQueryTextListener,
    AdapterView.OnItemClickListener {

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
        foods = mutableListOf()

        /* For testing only
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
        )*/

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_foods, container, false)

        listViewFoods = viewRoot.findViewById(R.id.listFoods)
        listViewFoods.onItemClickListener = this
        adapterFoods = FoodsListViewAdapter(mActivity, foods)
        listViewFoods.adapter = adapterFoods

        searchFoods = viewRoot.findViewById(R.id.searchFoods)
        searchFoods.setOnQueryTextListener(this);

        return viewRoot
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        TaskGetFoods(query, "<APP_CODE>", "<APP_ID>")
            .execute()
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        return false
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            val selectedFood = parent.adapter.getItem(position) as Food
            mActivity.appData.currentCals += selectedFood.calories
        }

        mActivity.openDashboardFragment()
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

    private inner class TaskGetFoods(
        private val query: String,
        private val appId: String,
        private val appKey: String
    ) : AsyncTask<Void?, Void?, Void?>()
    {
        private var resultCode: Int = -1
        private var resultBody: String = ""

        override fun doInBackground(vararg params: Void?): Void? {
            val httpClient = OkHttpClient()
            httpClient.setConnectTimeout(3000, TimeUnit.MILLISECONDS)
            httpClient.setReadTimeout(3000, TimeUnit.MILLISECONDS)
            httpClient.setWriteTimeout(3000, TimeUnit.MILLISECONDS)

            val getRequest: Request = Request.Builder()
                .url(Api.constructUrl(query, appId, appKey))
                .addHeader("Content-type", "application/json")
                .build()

            try {
                val response = httpClient.newCall(getRequest).execute()
                resultCode = response.code()
                resultBody = response.body().string()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)

            when (resultCode) {
                200 -> { // Request succeeded

                    foods.clear()

                    try {
                        val root = JSONObject(resultBody)
                        val jsonHints = root.getJSONArray("hints")

                        for (i in 0 until jsonHints.length()) {
                            val jsonHint = jsonHints.getJSONObject(i)
                            val jsonFood = jsonHint.getJSONObject("food")

                            val name = jsonFood.getString("label")
                            val jsonNutrients = jsonFood.getJSONObject("nutrients")
                            val calories =
                                if (jsonNutrients.has("ENERC_KCAL")) {
                                    jsonNutrients.getDouble("ENERC_KCAL").toInt()
                                } else { 0 }

                            // Add Theme to list
                            foods.add(Food(name, calories))
                        }

                        adapterFoods.setFoods(foods)
                    } catch (e: JSONException) {
                        // Failed to parse data, set Enterprise data as empty
                        e.printStackTrace()
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                }
                400 -> {
                    foods.clear()
                    adapterFoods.setFoods(foods)
                }
                401 -> { }
                500 -> { }
                else -> { }
            }
        }

    }
}