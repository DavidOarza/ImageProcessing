package com.university.ip.ui.viewer

import com.university.ip.ui.base.BaseContract

interface ViewerContract {

    interface View : BaseContract.View {
        //view functions for each change of activity
    }

    interface Presenter {
        //functions that are going to use our library
    }
}