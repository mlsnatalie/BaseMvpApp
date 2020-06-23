package com.libs.core.common.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.libs.core.R;
import com.libs.core.business.events.MusicEvent;

public class PlayStatusLayout extends LinearLayout {

    private ImageView imageView;
    private PlayStatusView playStatusView;

    public PlayStatusLayout(Context context) {
        super(context);
        init();
    }

    public PlayStatusLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayStatusLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        imageView = new ImageView(getContext());
        addView(imageView, lp);

        imageView.setVisibility(GONE);

        playStatusView = new PlayStatusView(getContext());
        addView(playStatusView, lp);

    }


    /**
     * 更改当前状态
     *
     * @param status
     */
    public void setStatus(int status) {
        if (status == MusicEvent.BUFFERING) {
            Glide.with(getContext()).load(R.drawable.ic_music_buffering).into(imageView);
            imageView.setVisibility(VISIBLE);
            playStatusView.setVisibility(GONE);
        } else if (status == MusicEvent.PLAYING) {
            Glide.with(getContext()).load(R.drawable.ic_music_playing).into(imageView);
            imageView.setVisibility(VISIBLE);
            playStatusView.setVisibility(GONE);
        } else {
            playStatusView.setVisibility(VISIBLE);
            imageView.setVisibility(GONE);
        }
    }


    public void setStatusGray(int statusGray) {
        if (statusGray == MusicEvent.BUFFERING) {
            Glide.with(getContext()).load(R.drawable.ic_music_buffering).into(imageView);
            imageView.setVisibility(VISIBLE);
            playStatusView.setVisibility(GONE);
        } else if (statusGray == MusicEvent.PLAYING) {
            //Glide.with(getContext()).load(R.drawable.icon_play_pause).into(imageView);
            imageView.setImageResource(R.drawable.icon_play_pause);
            imageView.setVisibility(VISIBLE);
            playStatusView.setVisibility(GONE);
        } else {
            imageView.setImageResource(R.drawable.icon_play_play);
            //Glide.with(getContext()).load(R.drawable.icon_play_play).into(imageView);
            imageView.setVisibility(VISIBLE);
            playStatusView.setVisibility(GONE);
        }
    }

    //
    public void setPlayStatus(int status) {
        if (status == MusicEvent.BUFFERING) {
            imageView.setImageResource(R.drawable.ic_music_buffering);
            imageView.setVisibility(VISIBLE);
            playStatusView.setVisibility(GONE);
        } else if (status == MusicEvent.PLAYING) {
            //Glide.with(getContext()).load(R.drawable.icon_play_pause).into(imageView);
            imageView.setImageResource(R.drawable.icon_radio_red_pause_btn);
            imageView.setVisibility(VISIBLE);
            playStatusView.setVisibility(GONE);
        } else {
            imageView.setImageResource(R.drawable.icon_radio_gray_play_btn);
            //Glide.with(getContext()).load(R.drawable.icon_play_play).into(imageView);
            imageView.setVisibility(VISIBLE);
            playStatusView.setVisibility(GONE);
        }
    }

    /**
     * 设置音乐播放的颜色
     *
     * @param contentColor
     */
    public void setPlayMusicContentColor(int contentColor) {
        if (playStatusView != null) {
            playStatusView.setContentColor(contentColor);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (imageView != null) {
            imageView.setImageResource(0);
        }
    }
}
