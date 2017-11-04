package com.example.denisgabyshev.getdisciplined.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import com.example.denisgabyshev.getdisciplined.R
import com.example.denisgabyshev.getdisciplined.data.db.model.ListId
import com.example.denisgabyshev.getdisciplined.ui.base.BaseActivity
import com.example.denisgabyshev.getdisciplined.ui.main.task.todo.ToDoListFragment
import javax.inject.Inject
import com.example.denisgabyshev.getdisciplined.ui.main.task.today.TaskFragment
import com.example.denisgabyshev.getdisciplined.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_main.*
/**
 * Created by denisgabyshev on 18/09/2017.
 */
class MainActivity : BaseActivity(), MainMvpView, ListAdapter.Callback {



    private val TAG = "MainActivity"

    @Inject lateinit var presenter: MainMvpPresenter<MainMvpView>

    lateinit var drawerToggle: ActionBarDrawerToggle

    var adapter: ListAdapter? = null


    companion object {
        fun getStartIntent(context: Context): Intent =
                Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityComponent.inject(this)

        presenter.onAttach(this)

        setUp()
    }

    override fun setTaskFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
        closeDrawer()
        
    }

    override fun setUp() {
        transparentStatusBar()

        setupNavMenu()

        addList.setOnClickListener {
            presenter.onAddListId()

        }
    }

    override fun openDrawer() {
        drawerLayout.openDrawer(Gravity.START)
    }

    override fun closeDrawer() {
        drawerLayout.closeDrawers()
    }

    private fun setupNavMenu() {
        navigationView.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0)
        setupNavRecyclerView()
        presenter.navigationListIds()
    }

    private fun setupNavRecyclerView() {
        listsRecycler.layoutManager = LinearLayoutManager(this)
        adapter = ListAdapter(applicationContext)
        adapter?.callback = this
        listsRecycler.adapter = adapter

    }

    override fun updateNavigationArray(array: ArrayList<ListId>) {
        adapter?.updateArray(array)
    }

    override fun clickedTodayOrToDoItem(position: Int) {
        when(position) {
            0 -> {
                setTaskFragment(TaskFragment())
            }
            1 -> {
                setTaskFragment(ToDoListFragment())
            }
        }
    }

    override fun clickedNavigationItem(listId: ListId) {

    }
}