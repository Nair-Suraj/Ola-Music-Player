package com.example.suraj.olaplaystudio.util;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by suraj on 16/12/17.
 */

@Database(name = MusicDatabase.NAME, version = MusicDatabase.VERSION)
public class MusicDatabase {
    public static final String NAME = "favourite";
    public static final int VERSION = 1;
}
