package net.instrument.free.utility.liedetectors.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import net.instrument.free.utility.liedetectors.R;
import net.instrument.free.utility.liedetectors.pojo.Country;

import java.util.ArrayList;

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.ViewHolder> {

    private int selectedPosition = -1;
    private Context context;
    private ArrayList<Country> states;
    private ItemOnClickListener listener;

    public CountAdapter(Context context, ArrayList<Country> configBeans,ItemOnClickListener listener) {
        this.context = context;
        this.states = configBeans;
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView state;
        private ImageView flag;
        private RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            state = itemView.findViewById(R.id.state);
            flag = itemView.findViewById(R.id.flag);
            layout = itemView.findViewById(R.id.layout);


        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_state,parent,false);
        return new ViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
      //  RandomBean configBean = randomBeans.get(position);
        // 设置itemView的状态为选中或未选中
        if (selectedPosition == position) {
            holder.layout.setBackgroundResource(R.drawable.at_sharp);
            holder.state.setTextColor(context.getColor(R.color.select_item));
        } else {
            holder.layout.setBackgroundResource(R.drawable.et_sharp);
            holder.state.setTextColor(context.getColor(R.color.no_select_item));
        }

        String s = states.get(position).getName();
        holder.state.setText(s);
        holder.flag.setImageBitmap(states.get(position).getImageView());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 设置选中位置为当前位置
                int previousSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);

                if (listener!=null){
                    listener.OnClick();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return states.size();
    }


    public void setSelectedPosition(int position) {
        int previousSelectedPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousSelectedPosition);
        notifyItemChanged(selectedPosition);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public interface ItemOnClickListener{
        void OnClick();
    }

}
