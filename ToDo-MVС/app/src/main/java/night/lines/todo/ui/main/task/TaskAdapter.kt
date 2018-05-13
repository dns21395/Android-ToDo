package night.lines.todo.ui.main.task

import android.content.Context
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_task.view.*
import night.lines.todo.R
import night.lines.todo.data.database.db.model.TaskModel
import night.lines.todo.domain.model.Task
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.imageResource

/**
 * Created by denisgabyshev on 20/03/2018.
 */
class TaskAdapter(context: Context, private val recyclerView: RecyclerView) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    var array = ArrayList<Task>()
    lateinit var taskFragment: TaskFragment

    private val swipeCallback = TaskAdapterSwipeCallback(context, this)
    private val itemTouchHelper = ItemTouchHelper(swipeCallback)

    init {
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder
        = TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))

    override fun getItemCount(): Int = array.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(array[position])
    }

    fun updateArray(array: ArrayList<Task>) {
        this.array = array
        notifyDataSetChanged()
        if(taskFragment.isAddTaskFragmentVisible) smoothScrollToPosition()
    }

    fun removeAt(position: Int) {
        taskFragment.onItemSwiped(array[position], {notifyItemRemoved(position)})
    }

    fun smoothScrollToPosition() {
        recyclerView.smoothScrollToPosition(array.size)
    }

    inner class TaskViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView?.setOnLongClickListener {
                itemTouchHelper.startSwipe(this)
                false
            }
        }

        fun bind(task: Task) = with(itemView){
            taskName.text = task.taskName

            when(task.isDone) {
                false -> {
                    taskName.paintFlags = taskName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    statusButton.imageResource = R.drawable.checkbox_blank_circle_outline
                }
                true -> {
                    taskName.paintFlags = taskName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    statusButton.imageResource = R.drawable.checkbox_marked_circle
                }
            }

            statusButton.setOnClickListener {
                task.isDone = !task.isDone
                taskFragment.onStatusButtonClick(task)
            }
        }

        private fun changeBackground(color: Int) = with(itemView)  {
            itemView.backgroundColor = ContextCompat.getColor(context, color)
        }

        fun onItemSelected() {
            changeBackground(R.color.item_touch)
        }

        fun onItemClear() {
            changeBackground(android.R.color.white)
        }
    }
}