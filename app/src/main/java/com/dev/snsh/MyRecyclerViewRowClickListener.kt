package com.dev.snsh

import android.view.View

interface MyRecyclerViewRowClickListener2 {
    fun onRowClicked(position: Int)

    fun onViewClicked(v: View, position: Int)
}

