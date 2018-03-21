package night.lines.todo.ui.main.addtask

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_add_task.*
import night.lines.todo.R
import night.lines.todo.model.system.KeyboardUtils
import night.lines.todo.presentation.main.addtask.AddTaskPresenter
import night.lines.todo.presentation.main.addtask.AddTaskView
import night.lines.todo.toothpick.DI
import night.lines.todo.ui.global.BaseFragment
import org.jetbrains.anko.support.v4.toast
import toothpick.Toothpick

/**
 * Created by denisgabyshev on 20/03/2018.
 */
class AddTaskFragment : BaseFragment(), AddTaskView {

    @InjectPresenter lateinit var presenter: AddTaskPresenter

    companion object {
        val TAG = "AddTaskFragment"
    }

    @ProvidePresenter
    fun providePresenter(): AddTaskPresenter =
            Toothpick
                    .openScope(DI.MAIN_SCOPE)
                    .getInstance(AddTaskPresenter::class.java)

    override val layoutRes = R.layout.fragment_add_task

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        context?.let { KeyboardUtils.showSoftInput(textTask, it) }

        addButton.setOnClickListener {
            if(textTask.text.toString().isNotEmpty()) {
                presenter.onAddTaskButtonClicked(textTask.text.toString())
                KeyboardUtils.hideSoftInput(activity as FragmentActivity)
            } else {
                toast(R.string.text_add_task_empty)
            }
        }
    }
}