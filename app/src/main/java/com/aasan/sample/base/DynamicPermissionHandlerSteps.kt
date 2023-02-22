package com.aasan.sample.base

enum class DynamicPermissionHandlerSteps {
    CHECK_PERMISSION,
    SHOW_USER_CONSENT,
    ASK_USER_PERMISSION,
    SHOULD_SHOW_RATIONAL,
    ACTION_APPROVED
}

interface DynamicPermissionActionHandler{
    fun onPermissionsApproved()
    fun onPermissionsRejected()
}