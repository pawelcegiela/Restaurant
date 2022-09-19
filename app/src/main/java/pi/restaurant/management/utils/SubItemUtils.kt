package pi.restaurant.management.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import pi.restaurant.management.R
import pi.restaurant.management.data.IngredientItem

class SubItemUtils {
    companion object {
        fun setRecyclerSize(recyclerView: RecyclerView, size: Int, context: Context) {
            val itemSize = 60
            val layoutParams = LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                (itemSize * context.resources.displayMetrics.density * size).toInt()
            )
            recyclerView.layoutParams = layoutParams
        }

        fun addChangeIngredientItemAmountDialog(recyclerView: RecyclerView, item: IngredientItem, context: Context) {
            val dialog = Dialog(context)

            dialog.setContentView(R.layout.dialog_dish_ingredient_amount)
            dialog.window!!.setLayout(1000, 1000)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            val editText = dialog.findViewById<EditText>(R.id.editTextAmount)
            val button = dialog.findViewById<Button>(R.id.buttonEnter)

            editText.setText(item.amount.toString())
            button.setOnClickListener {
                item.amount = if (editText.text.isEmpty()) 0.0 else editText.text.toString().toDouble()
                dialog.dismiss()
                recyclerView.adapter?.notifyDataSetChanged() //TODO Zła praktyka
            }
        }

        fun removeIngredientItem(
            list: MutableList<IngredientItem>,
            recyclerView: RecyclerView,
            item: IngredientItem,
            context: Context
        ) {
            list.remove(item)
            recyclerView.adapter?.notifyDataSetChanged() //TODO Zła praktyka
            setRecyclerSize(recyclerView, list.size, context)
        }

        fun addIngredientItem(
            list: MutableList<IngredientItem>,
            recyclerView: RecyclerView,
            item: IngredientItem,
            context: Context
        ) {
            list.add(item)
            recyclerView.adapter?.notifyDataSetChanged() //TODO Zła praktyka
            setRecyclerSize(recyclerView, list.size, context)
        }
    }
}