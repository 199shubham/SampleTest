package com.globalmed.corelib.base

interface NetworkErrorListener {
    fun onApiNotFound()
    fun onDomainCouldNotReach()
    fun logout()
}