package com.example.suraj.olaplaystudio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suraj.olaplaystudio.util.FavList;
import com.example.suraj.olaplaystudio.util.MusicItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    private Context mContext;
    private ArrayList<MusicItem> modelList;

    private OnItemClickListener mItemClickListener;

    public static ArrayList<Integer> selectedPositions;

    private TextView favTextView;
    public RecyclerViewAdapter(Context context, ArrayList<MusicItem> modelList,TextView favTextView) {
        this.mContext = context;
        this.modelList = modelList;
        this.favTextView=favTextView;

        if(selectedPositions==null)
        selectedPositions = new ArrayList<>();

    }

    public void updateList(ArrayList<MusicItem> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_list, parent, false);
            return new ViewHolder(v);
        }
        return null;
    }

    boolean fav=true;
    @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
         if (holder instanceof ViewHolder) {
            final MusicItem model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.itemTxtTitle.setText(model.getSong());
            genericViewHolder.itemTxtMessage.setText(model.getArtist());

            loadImage(genericViewHolder.imgUser,model);
             favTextView.setText(String.valueOf(selectedPositions.size()));


             if (selectedPositions.contains(position)) {
                 genericViewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
             } else
                 genericViewHolder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);





            genericViewHolder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FavList obj = new FavList();

                    if (!selectedPositions.contains(position)) {
                        selectedPositions.add(position);
                        obj.setPos(position);
                        obj.save();
                        genericViewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);

                    } else {
                        selectedPositions.remove(selectedPositions.indexOf(position));
                        obj.delete();
                        genericViewHolder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }

                    favTextView.setText(String.valueOf(selectedPositions.size()));

                }
            });


        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }



    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }



    private MusicItem getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, MusicItem model);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUser;
        private TextView itemTxtTitle;
        private TextView itemTxtMessage;
        private ImageView fav;


        // @BindView(R.id.img_user)
        // ImageView imgUser;
        // @BindView(R.id.item_txt_title)
        // TextView itemTxtTitle;
        // @BindView(R.id.item_txt_message)
        // TextView itemTxtMessage;
        // @BindView(R.id.radio_list)
        // RadioButton itemTxtMessage;
        // @BindView(R.id.check_list)
        // CheckBox itemCheckList;
        public ViewHolder(final View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            this.imgUser = (ImageView) itemView.findViewById(R.id.img_user);
            this.itemTxtTitle = (TextView) itemView.findViewById(R.id.item_txt_title);
            this.itemTxtMessage = (TextView) itemView.findViewById(R.id.item_txt_message);
            this.fav=itemView.findViewById(R.id.fav);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));


                }
            });

        }
    }

    public void loadImage(final ImageView imageView, final MusicItem model){
        // Webview is used beacuse the url is redirecting and not loading image.
        WebView webView=new WebView(mContext);
        webView.loadUrl(model.getCover());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                Picasso.with(mContext)
                        .load(request.getUrl()).into((ImageView)imageView);
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                Picasso.with(mContext)
                        .load(url).
                        error(R.drawable.ic_image_black_24dp)
                        .fit()
                        .into((ImageView)imageView);

                model.setCover_image(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

    }

}

