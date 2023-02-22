package com.aasan.sample.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.aasan.sample.R

open class PermissionHandlerActivity : AppCompatActivity() {

    private lateinit var _actionHandler: DynamicPermissionActionHandler
    private lateinit var _title: String
    private lateinit var _message: String
    private lateinit var _permissions: List<String>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun registerPermissionHandlers(){
        requestPermissionsLauncher = registerPermission()
        requestActivityLauncher = registerActivity()
    }

    private lateinit var requestPermissionsLauncher:
            ActivityResultLauncher<Array<String>>
    private lateinit var requestActivityLauncher:
            ActivityResultLauncher<Intent>

    fun hasPermissions(context: Context, permissions: List<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestDynamicPermission(
        title: String,
        message: String,
        actionHandler: DynamicPermissionActionHandler,
        permissions: List<String>,
        step: DynamicPermissionHandlerSteps = DynamicPermissionHandlerSteps.CHECK_PERMISSION
    ) {
        _actionHandler = actionHandler
        _title = title
        _message = message
        _permissions = permissions
        when (step) {
            DynamicPermissionHandlerSteps.CHECK_PERMISSION -> {
                if (hasPermissions(this, permissions)) {
                    actionHandler.onPermissionsApproved()
                } else {
                    requestDynamicPermission(
                        title = title,
                        message = message,
                        actionHandler = actionHandler,
                        permissions = permissions,
                        step = DynamicPermissionHandlerSteps.SHOW_USER_CONSENT
                    )

                }
            }
            DynamicPermissionHandlerSteps.SHOW_USER_CONSENT -> {
                showUserConsent(title, message, {
                    requestDynamicPermission(
                        title = title,
                        message = message,
                        actionHandler = actionHandler,
                        permissions = permissions,
                        step = DynamicPermissionHandlerSteps.SHOULD_SHOW_RATIONAL
                    )
                }, {
                    actionHandler.onPermissionsRejected()
                })
            }
            DynamicPermissionHandlerSteps.SHOULD_SHOW_RATIONAL -> {
                shouldShowRational(
                    title = "Error!",
                    rationalMessage = "message",
                    permissions = permissions,
                    {
                        requestDynamicPermission(
                            title = title,
                            message = message,
                            actionHandler = actionHandler,
                            permissions = permissions,
                            step = DynamicPermissionHandlerSteps.ASK_USER_PERMISSION
                        )
                    },
                    { actionHandler.onPermissionsRejected() })
            }
            DynamicPermissionHandlerSteps.ASK_USER_PERMISSION -> {
                askForPermission(permissions)
            }
            DynamicPermissionHandlerSteps.ACTION_APPROVED -> {
                _actionHandler.onPermissionsApproved()
            }
        }
    }

    private fun showUserConsent(
        title: String,
        message: String,
        successCallBack: () -> Unit,
        failedCallBack: (() -> Unit)?
    ) {
        successCallBack.invoke()
        /*showDialog(title, message, "Proceed", "Cancel",
            {
                successCallBack.invoke()
            }, {
                failedCallBack?.invoke()
            }, true
        )*/
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun shouldShowRational(
        title: String,
        rationalMessage: String,
        permissions: List<String>,
        successCallBack: () -> Unit,
        failedCallBack: (() -> Unit)?
    ) {
        val permissionName = permissions.reduce { acc, s ->
            "$acc,$s"
        }

        if (shouldShowRequestPermissionRationale(permissionName)) {
            showDialog(
                title,
                rationalMessage,
                "Settings",
                "Cancel",
                {
                    requestActivityLauncher.launch(Intent(Settings.ACTION_APPLICATION_SETTINGS))
                },
                {
                    failedCallBack?.invoke()
                },
                true
            )
        } else {
            successCallBack.invoke()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun askForPermission(
        permissions: List<String>
    ) {
        requestPermissionsLauncher.launch(permissions.toTypedArray())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun registerPermission() =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGranted ->
            val allGranted = isGranted.values.reduce { acc, b -> acc && b }
            if (allGranted) {
                if (this::_actionHandler.isInitialized)
                    requestDynamicPermission(
                        _title,
                        _message,
                        _actionHandler,
                        _permissions,
                        DynamicPermissionHandlerSteps.ACTION_APPROVED
                    )
            } else {
                if (this::_actionHandler.isInitialized)
                    _actionHandler.onPermissionsRejected()
            }
        }

    private fun registerActivity() = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (this::_actionHandler.isInitialized) {
                _actionHandler.onPermissionsApproved()
            }
        } else {
            if (this::_actionHandler.isInitialized)
                _actionHandler.onPermissionsRejected()
        }
    }

    fun showDialog(
        title: String?,
        message: String?,
        positiveBtnText: String?,
        negativeBtnText: String?,
        positiveBtnAction: (() -> Unit)? = null,
        negativeBtnAction: (() -> Unit)? = null,
        shouldCancelOutSideTouch: Boolean = true
    ) {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .let { builder ->
                positiveBtnAction?.let { action ->
                    builder.setPositiveButton(positiveBtnText ?: "Okay") { dialog, _ ->
                        dialog.dismiss()
                        action.invoke()
                    }
                }
                negativeBtnAction?.let { action ->
                    builder.setNegativeButton(negativeBtnText ?: "Cancel") { dialog, _ ->
                        dialog.dismiss()
                        action.invoke()
                    }
                }
                setFinishOnTouchOutside(shouldCancelOutSideTouch)
                builder
            }
        val alert = builder.create()
        alert.setCanceledOnTouchOutside(shouldCancelOutSideTouch)
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.green))
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.green))
        alert.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getColor(R.color.green))
    }
}