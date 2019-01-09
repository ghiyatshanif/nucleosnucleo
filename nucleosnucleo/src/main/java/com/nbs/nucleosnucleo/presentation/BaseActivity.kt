package com.nbs.nucleosnucleo.presentation

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.nbs.nucleosnucleo.R


abstract class BaseActivity : AppCompatActivity(), BaseView, BaseFragment.Callback {

    private var mProgressDialog: ProgressDialog? = null

    //    pass Layout here
    protected abstract val layoutResource: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        setContentView(layoutResource)

        onViewReady()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun setupToolbar(toolbar: Toolbar?, title: String, isChild: Boolean) {
        toolbar?.let {
            setSupportActionBar(toolbar)
        }

        if (supportActionBar != null) {
            supportActionBar!!.title = title
            supportActionBar!!.setDisplayHomeAsUpEnabled(isChild)
        }
    }

    override fun setupToolbar(toolbar: Toolbar?, isChild: Boolean) {
        toolbar?.let {
            setSupportActionBar(toolbar)
        }

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(isChild)
        }
    }

    override fun setupToolbar(title: String, isChild: Boolean) {
        if (supportActionBar != null) {
            supportActionBar!!.title = title
            supportActionBar!!.setDisplayHomeAsUpEnabled(isChild)
        }
    }

    override fun showLoading() {
        hideLoading()

        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setMessage(getString(R.string.please_wait))
            mProgressDialog!!.isIndeterminate = true
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.setCanceledOnTouchOutside(false)
        }
        mProgressDialog!!.show()
    }

    override fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    override fun finishActivity() {
        finish()
    }

    override fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        var iBinder: IBinder? = null
        try {
            iBinder = currentFocus!!.windowToken
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        if (iBinder != null) {
            inputManager.hideSoftInputFromWindow(
                iBinder,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    fun setFragment(viewRes: Int, fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(viewRes, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        // Commit the transaction
        transaction.commit()
    }

    private fun onViewReady() {
        initLib()
        initIntent()
        initUI()
        initAction()
        initProcess()
    }

    //    Init Presenter and Component Injection here
    protected abstract fun initLib()

    //    Extract desired intent here
    protected abstract fun initIntent()

    //    initialize the UI, setup toolbar, setText etc here
    protected abstract fun initUI()

    //    initialize UI interaction here
    protected abstract fun initAction()

    //    initialize main Process here e.g call presenter to load data
    protected abstract fun initProcess()

}