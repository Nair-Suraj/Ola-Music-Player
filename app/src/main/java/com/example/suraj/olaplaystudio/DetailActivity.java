/*
 * Copyright (c) 2016. André Mion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.suraj.olaplaystudio;

import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.widget.TextView;

import com.andremion.music.MusicCoverView;
import com.example.suraj.olaplaystudio.util.MusicItem;
import com.example.suraj.olaplaystudio.util.TransitionAdapter;
import com.squareup.picasso.Picasso;

public class DetailActivity extends MusicPlayerActivity {

    private MusicCoverView mCoverView;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_detail);

        Bundle bundle=getIntent().getExtras();
        MusicItem item=(MusicItem) bundle.getSerializable("Music");
        mCoverView = (MusicCoverView) findViewById(R.id.cover);
        title=findViewById(R.id.title);
        assert item != null;
        Picasso.with(DetailActivity.this).load(item.getCover()).into(mCoverView);
        title.setText(item.getSong());

        mCoverView.setCallbacks(new MusicCoverView.Callbacks() {
            @Override
            public void onMorphEnd(MusicCoverView coverView) {
                // Nothing to do
            }

            @Override
            public void onRotateEnd(MusicCoverView coverView) {
                supportFinishAfterTransition();
            }
        });

        getWindow().getSharedElementEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                play();
                mCoverView.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onFabClick(null);
    }

    public void onFabClick(View view) {
        pause();
        mCoverView.stop();
    }
}
