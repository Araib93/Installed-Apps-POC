package com.poc.installedapps.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.poc.installedapps.R
import com.poc.installedapps.base.BaseFragment
import com.poc.installedapps.model.App
import com.poc.installedapps.utils.FileUtils
import com.poc.installedapps.utils.PackageHelper
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : BaseFragment(), AppActions {
    private lateinit var packageHelper: PackageHelper
    private lateinit var fileUtils: FileUtils
    private val appAdapter by lazy { AppAdapter(this) }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        packageHelper = PackageHelper(context)
        fileUtils = FileUtils(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            rv_apps.layoutManager = LinearLayoutManager(requireContext())
            rv_apps.adapter = appAdapter
            appAdapter.addApps(fetchApps().also { pb_loading.visibility = View.GONE })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_allApps -> {
                setApps(null)
                true
            }
            R.id.action_systemApps -> {
                setApps(true)
                true
            }
            R.id.action_userApps -> {
                setApps(false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setApps(systemApps: Boolean? = null) {
        lifecycleScope.launch {
            appAdapter.clear().also { pb_loading.visibility = View.VISIBLE }
            appAdapter.addApps(fetchApps(systemApps).also { pb_loading.visibility = View.GONE })
        }
    }

    private suspend fun fetchApps(systemApps: Boolean? = null): List<App> =
        withContext(Dispatchers.Default) {
            packageHelper.getInstalledPackages(systemApps).map {
                App(
                    `package` = it,
                    name = packageHelper.getAppNameByPackageName(it) ?: it,
                    imageUrl = fileUtils.saveBitmap(
                        it,
                        packageHelper.getAppIconByPackageName(it)?.toBitmap()
                    )
                )
            }.sortedBy { it.name }
        }

    override fun openApp(`package`: String) {
        val intent = requireContext().packageManager
            .getLaunchIntentForPackage(`package`)
        if (intent == null) {
            Log.d(TAG, "openApp to launch $`package`, Intent not found")
            showToast(message = "$`package` Error, Please Try Again.")
        } else {
            startActivity(intent)
        }
    }

    override fun openAppSettings(`package`: String) = try {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package",
            `package`,
            null
        )
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        true
    } catch (ex: Exception) {
        Log.d(TAG, ex.localizedMessage ?: "Exception :3")
        false
    }

    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }
}