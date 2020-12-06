package pt.brunoponte.satisfyingcalorietracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import pt.brunoponte.satisfyingcalorietracker.data.Food


class FoodsListViewAdapter(ctx: Context, foods: List<Food>) : BaseAdapter() {

    // Declare Variables
    private val mContext = ctx
    private val inflater = LayoutInflater.from(mContext)
    private val foodsList: MutableList<Food> = foods.toMutableList()

    class ViewHolder {
        var name: TextView? = null
    }

    override fun getCount() = foodsList.size

    override fun getItem(position: Int) = foodsList[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val holder: ViewHolder
        val viewToReturn: View

        if (convertView == null) {
            holder = ViewHolder()
            viewToReturn = inflater.inflate(R.layout.listview_food_item, null)
            // Locate the TextViews in listview_item.xml
            holder.name = viewToReturn.findViewById(R.id.name)
            viewToReturn.tag = holder
        } else {
            viewToReturn = convertView
            holder = viewToReturn.tag as ViewHolder
        }
        // Set the results into TextViews
        // Set the results into TextViews
        holder.name?.text = foodsList[position].name
        return viewToReturn
    }

    fun setFoods(foods: List<Food>) {
        foodsList.clear()
        foodsList.addAll(foods)
        notifyDataSetChanged()
    }

}
