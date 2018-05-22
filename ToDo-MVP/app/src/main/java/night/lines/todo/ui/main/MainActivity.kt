package night.lines.todo.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.LinearLayoutManager
import android.widget.FrameLayout
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_navigation.*
import night.lines.todo.R
import night.lines.todo.presentation.main.MainPresenter
import night.lines.todo.presentation.main.MainView
import night.lines.todo.toothpick.DI
import night.lines.todo.toothpick.main.MainModule
import night.lines.todo.toothpick.main.MainScope
import night.lines.todo.ui.main.addtask.AddTaskFragment
import night.lines.todo.ui.main.navigation.MainNavigationAdapter
import night.lines.todo.ui.main.navigation.TaskIdDialog
import night.lines.todo.ui.main.task.TaskFragment
import night.lines.todo.util.ScreenUtils
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by denisgabyshev on 18/03/2018.
 */

class MainActivity : MvpAppCompatActivity(), MainView {

    private val TAG = "MainActivity"

    @InjectPresenter lateinit var presenter: MainPresenter

    @Inject lateinit var linearLayoutManager: LinearLayoutManager

    @Inject lateinit var adapter: MainNavigationAdapter


    @ProvidePresenter
    fun providePresenter(): MainPresenter =
            Toothpick.openScopes(DI.APP_SCOPE, MainScope::class.java).apply {
                installModules(MainModule())
                Toothpick.inject(this@MainActivity, this)
            }.getInstance(MainPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setMenu()
        setNavMenu()

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, TaskFragment())
                .commitAllowingStateLoss()

        //if(presenter.bottomFrameLayoutId != 0) createFrameLayout()

        fab.setOnClickListener {
            presenter.onFabButtonClicked()
        }

        presenter.onViewPrepared()
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

        constraintSet.clear(coordinator.id, ConstraintSet.BOTTOM)

        constraintSet.connect(coordinator.id, ConstraintSet.BOTTOM, presenter.bottomFrameLayoutId, ConstraintSet.TOP)

        constraintSet.applyTo(parentConstraint)
    }

    override fun showAddTaskFragment() {
        createFrameLayout()

        supportFragmentManager.beginTransaction().replace(presenter.bottomFrameLayoutId, AddTaskFragment(), AddTaskFragment.TAG).commit()

        app_bar.setExpanded(false)
    }

    override fun hideAddTaskFragment() {
        val bottomFragment = supportFragmentManager.findFragmentByTag(AddTaskFragment.TAG)

        if(bottomFragment != null) supportFragmentManager.beginTransaction().remove(bottomFragment).commit()

        parentConstraint.removeView(findViewById(presenter.bottomFrameLayoutId))

        val constraintSet = ConstraintSet()
        constraintSet.clone(parentConstraint)

        constraintSet.clear(coordinator.id, ConstraintSet.BOTTOM)

        constraintSet.connect(coordinator.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        constraintSet.applyTo(parentConstraint)

        presenter.bottomFrameLayoutId = 0

        fab.show()
    }

    override fun setToolbarBackground(background: Int) {
        Log.d(TAG, "$background toolbar new image")
        toolbarBackground.setImageResource(background)
    }

    override fun onBackPressed() {
        if(presenter.bottomFrameLayoutId != 0) {
            hideAddTaskFragment()
            presenter.enumAddTaskFragmentHide()
        }
        else super.onBackPressed()
    }

    private fun setMenu() {
        toolbar.inflateMenu(R.menu.main)

        toolbar.setOnMenuItemClickListener {
            if(it.itemId == R.id.check) presenter.setFinishedTasksVisibility()
            false
        }
    }

    private fun setNavMenu() {
        navigationView.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0)
        adapter.presenter = presenter
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        addTaskIDCardView.setOnClickListener {
            Log.d(TAG, "newListClicked")
            TaskIdDialog().show(supportFragmentManager)
        }
    }

    override fun updateIconCheckFinishedItemsVisibility(drawable: Int) {
        toolbar.menu.getItem(0).icon = resources.getDrawable(drawable, null)
    }

    override fun updateTaskIDArray() {
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isFinishing) Toothpick.closeScope(DI.MAIN_ACTIVITY_SCOPE)
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawers()
    }
}