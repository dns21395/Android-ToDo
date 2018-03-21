package night.lines.todo.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_main.*
import night.lines.todo.R
import night.lines.todo.presentation.global.MainActivityController
import night.lines.todo.presentation.main.MainPresenter
import night.lines.todo.presentation.main.MainView
import night.lines.todo.toothpick.DI
import night.lines.todo.toothpick.module.MainActivityModule
import night.lines.todo.ui.main.addtask.AddTaskFragment
import night.lines.todo.ui.main.task.TaskFragment
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by denisgabyshev on 18/03/2018.
 */
class MainActivity : MvpAppCompatActivity(), MainView {

    private val TAG = "MainActivity"

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        return Toothpick
                .openScopes(DI.MAIN_SCOPE)
                .getInstance(MainPresenter::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, TaskFragment())
                .commitAllowingStateLoss()

        if(presenter.bottomFrameLayoutId != 0) createFrameLayout()

        fab.setOnClickListener {
            presenter.onFabButtonClicked()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        if(isFinishing) Toothpick.closeScope(DI.MAIN_SCOPE)
    }

    private fun createFrameLayout() {
        fab.hide()

        val bottomFrameLayout = FrameLayout(applicationContext)

        val params = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)

        bottomFrameLayout.layoutParams = params

        if(presenter.bottomFrameLayoutId == 0) presenter.bottomFrameLayoutId = View.generateViewId()

        bottomFrameLayout.id = presenter.bottomFrameLayoutId

        parentConstraint.addView(bottomFrameLayout)

        val constraintSet = ConstraintSet()

        constraintSet.clone(parentConstraint)

        constraintSet.connect(presenter.bottomFrameLayoutId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM )

        constraintSet.connect(presenter.bottomFrameLayoutId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)

        constraintSet.connect(presenter.bottomFrameLayoutId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        constraintSet.clear(frameLayout.id, ConstraintSet.BOTTOM)

        constraintSet.connect(frameLayout.id, ConstraintSet.BOTTOM, presenter.bottomFrameLayoutId, ConstraintSet.TOP)

        constraintSet.applyTo(parentConstraint)
    }

    override fun showAddTaskFragment() {
        createFrameLayout()

        supportFragmentManager.beginTransaction().replace(presenter.bottomFrameLayoutId, AddTaskFragment(), AddTaskFragment.TAG).commit()
    }

    override fun hideAddTaskFragment() {
        val bottomFragment = supportFragmentManager.findFragmentByTag(AddTaskFragment.TAG)

        if(bottomFragment != null) supportFragmentManager.beginTransaction().remove(bottomFragment).commit()

        parentConstraint.removeView(findViewById(presenter.bottomFrameLayoutId))

        val constraintSet = ConstraintSet()
        constraintSet.clone(parentConstraint)

        constraintSet.clear(frameLayout.id, ConstraintSet.BOTTOM)

        constraintSet.connect(frameLayout.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        constraintSet.applyTo(parentConstraint)

        presenter.bottomFrameLayoutId = 0

        fab.show()
    }
}