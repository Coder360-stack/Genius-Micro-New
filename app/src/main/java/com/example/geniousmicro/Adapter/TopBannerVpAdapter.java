package com.example.geniousmicro.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.geniousmicro.R;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class TopBannerVpAdapter  extends RecyclerView.Adapter<TopBannerVpAdapter.TopBannerVpViewHolder> {

    List<Integer> latestModels;
    Context context;
    private ViewPager2 viewPager2;
    public TopBannerVpAdapter(Context context,  List<Integer> latestModels,ViewPager2 viewPager2){
        this.context= context;
        this.latestModels = latestModels;
        this.viewPager2=viewPager2;
        Log.d("this_logging", "yes"+latestModels.size());
    }

    @NonNull
    @Override
    public TopBannerVpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_viewpager_item,parent,false);
        return new TopBannerVpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopBannerVpViewHolder holder, int position) {
        Glide.with(context).load(latestModels.get(position)).into(holder.imageView);

        // holder.setVideoPath(latestModels.get(position));


    }

    @Override
    public int getItemCount() {
        return latestModels.size();
    }

    public static class TopBannerVpViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        //ExoPlayer exoPlayer;
        //StyledPlayerView styledPlayerView;
        public TopBannerVpViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivImage);
            //styledPlayerView = itemView.findViewById(R.id.video);


        }
//        public void setVideoPath(Video url){
//            exoPlayer = new ExoPlayer.Builder(itemView.getContext()).build();
//            exoPlayer.addListener(new Player.Listener() {
//                @Override
//                public void onPlayerError(PlaybackException error) {
//                    Player.Listener.super.onPlayerError(error);
//                    Toast.makeText(itemView.getContext(), "can't play this video",Toast.LENGTH_SHORT).show();
//
//                }
//            });
//            styledPlayerView.setPlayer(exoPlayer);
//            exoPlayer.seekTo(0);
//            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
//            MediaItem mediaItem = new MediaItem.Builder().setUri(url.getVideo()).build();
//            exoPlayer.setMediaItem(mediaItem);
//            exoPlayer.prepare();
//            if (getAbsoluteAdapterPosition()==0){
//                exoPlayer.setPlayWhenReady(true);
//                exoPlayer.play();
//            }
//        }
    }
}
