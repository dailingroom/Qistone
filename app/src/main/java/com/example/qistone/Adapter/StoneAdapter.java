package com.example.qistone.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qistone.R;
import com.example.qistone.entity.Stone;

import java.util.ArrayList;

public class StoneAdapter extends RecyclerView.Adapter<StoneAdapter.ViewHolder> {
        ArrayList<Stone> mStoneArrayList;
public StoneAdapter(ArrayList<Stone> list) {
       mStoneArrayList=list;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//用来创建ViewHolder实例，再将加载好的布局传入构造函数，最后返回ViewHolder实例
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.stone_item,null);
        ViewHolder holder= new ViewHolder(view);
        return holder;
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) { //用于对RecyclerView的子项进行赋值，会在每个子项滚动到屏幕内的时候执行
        holder.iv.setImageResource(mStoneArrayList.get(position).getImageResourceID());
        holder.tv.setText(mStoneArrayList.get(position).getName());
        }

@Override
public int getItemCount() {
        return mStoneArrayList.size();
        }

public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView tv;
    ImageView iv;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tv=itemView.findViewById(R.id.stoneName);
        iv=itemView.findViewById(R.id.stoneImage);
    }

}
}