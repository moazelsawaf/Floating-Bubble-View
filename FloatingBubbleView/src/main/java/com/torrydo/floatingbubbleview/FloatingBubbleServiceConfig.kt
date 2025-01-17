package com.torrydo.floatingbubbleview

import android.app.Service
import android.content.Intent

abstract class FloatingBubbleServiceConfig : Service() {

    companion object {
        internal const val BUBBLE = 1
        internal const val EXPANDABLE_VIEW = 2
    }

    private var floatingBubble: FloatingBubble? = null
    private var expandableView: ExpandableView? = null

    // lifecycle -----------------------------------------------------------------------------------

    override fun onDestroy() {
        tryRemoveAllView()
        super.onDestroy()
    }

    // override ------------------------------------------------------------------------------------

    abstract fun setupBubble(action: FloatingBubble.Action): FloatingBubble.Builder

    open fun setupExpandableView(action: ExpandableView.Action): ExpandableView.Builder? = null

    // public func ---------------------------------------------------------------------------------
    protected fun setupViewAppearance(defaultView: Int) {

        floatingBubble = setupBubble(customFloatingBubbleAction)
            .addFloatingBubbleListener(customFloatingBubbleListener)
            .build()

        expandableView = setupExpandableView(customExpandableViewListener)
            ?.build()


        onMainThread {
            when(defaultView){
                BUBBLE -> tryShowBubbles()
                EXPANDABLE_VIEW -> tryShowExpandableView()
            }

        }
    }


    // private func --------------------------------------------------------------------------------

    private val customExpandableViewListener = object : ExpandableView.Action {

        override fun popToBubble() {
            tryRemoveExpandableView()
            tryShowBubbles()
        }
    }

    private val customFloatingBubbleListener = object : FloatingBubble.Listener {

        override fun onDestroy() {
            tryStopService()
        }

    }

    private val customFloatingBubbleAction = object : FloatingBubble.Action {

        override fun navigateToExpandableView() {
            tryNavigateToExpandableView()
        }
    }


    private fun tryNavigateToExpandableView() {
        tryShowExpandableView()
            .onComplete {
                tryRemoveBubbles()
            }.onError {
                throw NullViewException("you DID NOT override expandable view")
            }
    }


    private fun tryStopService() {
        tryRemoveAllView()
        stopSelf()
    }

    private fun tryRemoveAllView() {
        tryRemoveExpandableView()
        tryRemoveBubbles()
    }

    // shorten -------------------------------------------------------------------------------------

    private fun tryRemoveExpandableView() = logIfError {
        expandableView!!.remove()
    }

    private fun tryShowExpandableView() = logIfError {
        expandableView!!.show()
    }

    private fun tryShowBubbles() = logIfError {
        floatingBubble!!.showIcon()
    }

    private fun tryRemoveBubbles() = logIfError {
        floatingBubble!!.removeIcon()
        floatingBubble!!.tryRemoveCloseBubbleAndBackground()
    }


}

