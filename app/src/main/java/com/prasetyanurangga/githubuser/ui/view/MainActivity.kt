package com.prasetyanurangga.githubuser.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.prasetyanurangga.githubuser.GithubUserApplication
import com.prasetyanurangga.githubuser.R
import com.prasetyanurangga.githubuser.data.di.factory.UserViewModelFactory
import com.prasetyanurangga.githubuser.data.model.UserModel
import com.prasetyanurangga.githubuser.databinding.ActivityMainBinding
import com.prasetyanurangga.githubuser.ui.adapter.UserAdapter
import com.prasetyanurangga.githubuser.ui.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private var userAdapter : UserAdapter? = null

    lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory

    private var visibleThreshold: Int = 10
    private var currentPage: Int = 0
    private var isLoading: Boolean = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount:Int = 0
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please Wait ...")

        mLayoutManager = LinearLayoutManager(this)

        injectDagger()
        createViewModel()
        setBinding()


        recycler_view_books.layoutManager = mLayoutManager






        recycler_view_books.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                totalItemCount = userAdapter?.itemCount ?: 0

                lastVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (lastVisibleItem == totalItemCount - 1) {
                    currentPage += visibleThreshold
                    loadMore("angga",currentPage, visibleThreshold)
                }
            }
        })


    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }


    fun Context.hideKeyboard() {
        val view = this@MainActivity.currentFocus
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView? = searchItem?.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {// do your logic here                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                onUpdateList(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText == "")
                {
                    userAdapter?.updateUser(null)
                    userAdapter?.notifyDataSetChanged()
                }
                return true
            }
        })

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }

    private fun createViewModel()
    {
        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)
    }

    private fun injectDagger()
    {
        GithubUserApplication.instance.appComponent.inject(this)
    }

    private fun loadMore(q: String, page: Int, perPage: Int)
    {
        progressDialog.show()
            userViewModel.getUser(q, page, perPage).observe(this, Observer {
                list ->
            if (!isDestroyed)
            {
                Log.e("eee", list.toString())
                progressDialog.hide()
                hideKeyboard()
                showList(list)
            }
        })
    }

    private fun showList(userList: List<UserModel>?)
    {
        if(userAdapter == null && userList != null)
        {
            recycler_view_books.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            userAdapter = UserAdapter(userList)
            recycler_view_books.adapter = userAdapter
        }
        else
        {
            userAdapter?.updateUser(userList)
            userAdapter?.notifyDataSetChanged()
        }
    }

    private fun addList(userList: List<UserModel>?)
    {
        userAdapter?.addUser(userList)
        userAdapter?.notifyDataSetChanged()
    }

    private fun setBinding()
    {
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = userViewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }


    private var simpleItemCallback: ItemTouchHelper.SimpleCallback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (direction == ItemTouchHelper.LEFT)
            {
                if (viewHolder is UserAdapter.UserViewHolder)
                {


                }
            }
        }
    }

    fun onUpdateList(q: String)
    {

        progressDialog.show()
        userViewModel.getUser(q, currentPage, visibleThreshold).observe(this, Observer {
                list ->
            if (!isDestroyed && q.isNotEmpty())
            {
                Log.e("eee", list.toString())
                progressDialog.hide()
                hideKeyboard()
                showList(list)
            }
            else
            {
                showList(null)
            }
        })

    }




}