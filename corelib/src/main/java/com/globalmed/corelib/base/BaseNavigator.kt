package com.globalmed.corelib.base

/**
 * @author rsharma
 * @since 26-04-2021
 * BaseNavigator exposes methods to be used on fragment started and destroyed. Helpful in case
 * of handling fragment lifecycle events.
 *
 * e.g.
 *
 * fragment -> onViewCreated will can invoke baseNavigator.onInit() to perform some initialization
 * process or to call some api on fragment started.
 *
 * fragment-> onDestroyView can invoke baseNavigator.onClear() to perform some finalization task in
 * view model.
 *
 */
interface BaseNavigator: MarkerBaseNavigator {
	fun onInit()
	fun onClear()
}