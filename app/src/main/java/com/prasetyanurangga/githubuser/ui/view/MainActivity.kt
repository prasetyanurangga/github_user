package com.prasetyanurangga.githubuser.ui.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
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
import com.prasetyanurangga.githubuser.util.Status
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private var userAdapter : UserAdapter? = null

    lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory

    private var visibleThreshold: Int = 20
    private var currentPage: Int = 0
    private var isLoading: Boolean = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount:Int = 0
    private var visibleItemCount:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Please Wait ...")
        injectDagger()
        createViewModel()
        setBinding()


        rv_user.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                if(dy > 0)
                {

                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        currentPage += visibleThreshold;
                        loadMore("a", currentPage, visibleThreshold)
                    }
                }
            }
        })

        ed_search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                onUpdateList(textView.text.toString() ?: "")
                true
            } else {
                false
            }
        })

        ed_search.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0.isNullOrEmpty())
                {
                    userAdapter?.updateUser(null)
                    userAdapter?.notifyDataSetChanged()
                }
            }

        })


    }

    fun Context.hideKeyboard() {
        val view = this@MainActivity.currentFocus
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
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
            userViewModel.getUser(q, page, perPage).observe(this, Observer {
                resource ->
            if (!isDestroyed)
            {
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideKeyboard()
                        resource.data?.let { list ->
                            addList(list)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
            )
    }

    private fun showList(userList: List<UserModel>?)
    {
        if(userAdapter == null && userList != null)
        {
            rv_user.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            userAdapter = UserAdapter(userList)
            rv_user.adapter = userAdapter
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
        currentPage = 0;
        progressDialog.show()
        userViewModel.getUser(q, currentPage, visibleThreshold).observe(this, Observer {
                resource ->
            if (!isDestroyed && q.isNotEmpty())
            {
                progressDialog.hide()
                when (resource.status) {
                    Status.SUCCESS -> {
                        hideKeyboard()
                        resource.data?.let { list ->
                            showList(list)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
                    }
                }


            }
            else
            {
                showList(null)
            }
        })

    }




}