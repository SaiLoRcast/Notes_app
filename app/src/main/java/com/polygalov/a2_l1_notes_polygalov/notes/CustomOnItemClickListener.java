package com.polygalov.a2_l1_notes_polygalov.notes;

import android.view.View;

/**
 * Created by Константин on 22.09.2017.
 */

class CustomOnItemClickListener implements View.OnClickListener {

    private int position;
    private OnItemClickCallBack onItemClickCallBack;

    public CustomOnItemClickListener(int position, OnItemClickCallBack onItemClickCallBack) {
        this.position = position;
        this.onItemClickCallBack = onItemClickCallBack;
    }

    @Override
    public void onClick(View view) {
        onItemClickCallBack.OnItemClicked(view, position);
    }

    interface OnItemClickCallBack {
        void OnItemClicked(View view, int position);
    }
}