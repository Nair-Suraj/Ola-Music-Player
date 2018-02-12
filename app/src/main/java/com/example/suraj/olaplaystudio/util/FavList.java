package com.example.suraj.olaplaystudio.util;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by suraj on 19/12/17.
 */

@Table(database = MusicDatabase.class)
public class FavList extends BaseModel {

    @PrimaryKey
    @Column
     public int pos;


    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;

    }
}
