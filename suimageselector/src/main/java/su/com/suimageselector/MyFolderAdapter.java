package su.com.suimageselector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MyFolderAdapter extends RecyclerView.Adapter<MyFolderAdapter.Holder> implements View.OnClickListener{

    List<MyImageFolder> folders;
    Context context;
    OnImageFolderListener listener;
    int width,height;

    public MyFolderAdapter(List<MyImageFolder> folders, Context context, OnImageFolderListener listener) {
        this.folders = folders;
        this.context = context;
        this.listener=listener;

        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.image_folder_item,parent,false);
        return new Holder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        CheckBoxImageView img=holder.itemView.findViewById(R.id.img);
        TextView name=holder.itemView.findViewById(R.id.name);
        TextView num=holder.itemView.findViewById(R.id.num);
        int w,h;
        w=width/4;
        h=w;
        MyImageImageLoader.getInstance(context).loadImage(img,folders.get(position).getFirstImagePath(),w,h);
        name.setText(folders.get(position).getName());
        num.setText(folders.get(position).getFileNum()+"张");
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    @Override
    public void onClick(View view) {
        if(listener!=null) {
            try {
                listener.onFolderSelect(folders.get((Integer) view.getTag()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Holder extends RecyclerView.ViewHolder{

        View itemView;

        Holder(View itemView) {
            super(itemView);
            this.itemView=itemView;
        }
    }
}
