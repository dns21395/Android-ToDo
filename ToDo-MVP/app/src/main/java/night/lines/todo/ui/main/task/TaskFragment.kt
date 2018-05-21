package night.lines.todo.ui.main.task

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_task.*
import night.lines.todo.R
import night.lines.todo.domain.model.Task
import night.lines.todo.presentation.main.task.TaskPresenter
import night.lines.todo.presentation.main.task.TaskView
import night.lines.todo.toothpick.DI
import night.lines.todo.toothpick.task.TaskModule
import night.lines.todo.toothpick.task.TaskScope
import night.lines.todo.ui.base.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by denisgabyshev on 20/03/2018.
 */
class TaskFragment : BaseFragment(), TaskView {

    private val TAG = "TaskFragment"

    private val layoutManager = LinearLayoutManager(context)

    @InjectPresenter
    lateinit var presenter: TaskPresenter

    @Inject
    lateinit var adapter: TaskAdapter


    @ProvidePresenter
    fun providePresenter(): TaskPresenter
        =  Toothpick
            .openScopes(DI.MAIN_ACTIVITY_SCOPE, TaskScope::class.java).apply {
                TaskModule()
                Toothpick.inject(this@TaskFragment, this)
            }.getInstance(TaskPresenter::class.java)

    override val layoutRes = R.layout.fragment_task

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter.setRecyclerView(recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, layoutManager.orientation))

        presenter.onViewPrepared()
    }

    override fun updateTaskArray(array: ArrayList<Task>) {
        adapter.updateArray(array)
    }

    override fun scrollToEnd() {
        adapter.smoothScrollToPosition()
    }
}