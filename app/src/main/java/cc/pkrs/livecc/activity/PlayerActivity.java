package cc.pkrs.livecc.activity;

import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

import cc.pkrs.livecc.R;

public class PlayerActivity extends BaseActivity {

    ExoPlayer player;
    StyledPlayerView styledPlayerView;

    final String videoUri = "https://alipullhls.cc.netease.com/pushstation/e2cde9a16b5147da1854266756597.m3u8?auth_key=1662476917-d326deb8eea544f7800bb22a2f79b777-0-b5f0b72ae7954bb3ed3f273b648d4d40";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        player = new ExoPlayer.Builder(this).build();
        styledPlayerView = findViewById(R.id.styledPlayerView);
        styledPlayerView.setPlayer(player);

        getVideoUrl();
    }

    private void getVideoUrl() {
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(videoUri)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build();
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player.isPlaying()) {
            player.release();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}