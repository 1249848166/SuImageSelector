package su.com.suimageselector;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.Holder> implements View.OnClickListener{

    List<String> urls;
    Context context;
    int span;
    int space;
    final CheckBoxImageViewGroup checkBoxImageViewGroup;
    IconImagePosition position;
    int drawable;
    int width,height;

    public MyImageAdapter(List<String> urls, Context context, int span, int space, CheckBoxImageViewGroup checkBoxImageViewGroup, IconImagePosition position, int drawable) {
        this.urls = urls;
        this.context = context;
        this.span=span;
        this.space=space;
        this.checkBoxImageViewGroup=checkBoxImageViewGroup;
        this.position=position;
        this.drawable=drawable;

        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CheckBoxImageView itemView = (CheckBoxImageView) View.inflate(context,R.layout.image_image_item,null);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.itemView.setTag(urls.get(position));
        if(checkBoxImageViewGroup.getCheckedItems().contains(urls.get(position)))
            holder.itemView.setChecked(true);
        int w,h;
        w=(width-(span+1)*space)/span;
        h=w;
        holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(w,h));
        MyImageImageLoader.getInstance(context).loadImage(holder.itemView,urls.get(position),w,h);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    @Override
    public void onClick(View view) {
        ((CheckBoxImageView)view).check(checkBoxImageViewGroup,!((CheckBoxImageView)view).isChecked(),position,drawable);
    }

    class Holder extends RecyclerView.ViewHolder{

        CheckBoxImageView itemView;

        Holder(CheckBoxImageView itemView) {
            super(itemView);
            this.itemView=itemView;
        }
    }
}
