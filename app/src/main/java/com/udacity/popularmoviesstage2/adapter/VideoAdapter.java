package com.udacity.popularmoviesstage2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.popularmoviesstage2.R;
import com.udacity.popularmoviesstage2.model.Video;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by hansend on 6/8/2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {
    private List<Video> videoList;
    private final Context context;

    final private VideoAdapter.VideoAdapterOnClickHandler videoAdapterOnClickHandler;

    public interface VideoAdapterOnClickHandler {
        void onClick(Video video);
    }

    public VideoAdapter(@NonNull Context context,
                        VideoAdapter.VideoAdapterOnClickHandler videoAdapterOnClickHandler,
                        List<Video> videoList) {
        this.context = context;
        this.videoAdapterOnClickHandler = videoAdapterOnClickHandler;
        this.videoList = videoList;
    }

    @Override
    public VideoAdapter.VideoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup,
                                                                  int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.video_list_item, viewGroup, false);

        return new VideoAdapter.VideoAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.VideoAdapterViewHolder videoAdapterViewHolder,
                                 int position) {
        final String _VIDEO_NAME_FORMAT = "%s: %s";
        Video video = videoList.get(position);
        videoAdapterViewHolder.setVideoName(String.format(_VIDEO_NAME_FORMAT,
                                                          video.getType(),
                                                          video.getName()));
    }

    @Override
    public int getItemCount() {
        if (null == videoList) return 0;
        return videoList.size();
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView videoName;

        public VideoAdapterViewHolder(View view) {
            super(view);

            videoName = (TextView) view.findViewById(R.id.textview_video_name);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Video video = videoList.get(adapterPosition);
            videoAdapterOnClickHandler.onClick(video);
        }

        public void setVideoName(String text) {
            videoName.setText(text);
        }
    }
}
