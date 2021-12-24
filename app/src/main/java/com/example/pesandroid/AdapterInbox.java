package com.example.pesandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pesandroid.models.Mail;

import java.util.List;

public class AdapterInbox extends RecyclerView.Adapter<AdapterInbox.ViewHolder> {
    private List<Mail> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mailOut;
        public TextView dateOut;
        public TextView titleOut;
        public TextView bodyOut;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            mailOut = v.findViewById(R.id.firstLine);
            dateOut = v.findViewById(R.id.firstLine2);
            titleOut = v.findViewById(R.id.secondLine);
            bodyOut = v.findViewById(R.id.thirdLine);
        }
    }

    public void setData(List<Mail> myData) {
        values = myData;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout_no_images, parent, false);
        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your data at this position
        // - replace the contents of the view with that element
        Mail m = values.get(position);
        holder.mailOut.setText(m.mail);
        holder.dateOut.setText(m.date.toString());
        holder.titleOut.setText(m.message.title);
        holder.bodyOut.setText(m.message.body);
        holder.mailOut.setOnClickListener(v -> {
            /*int i = 0;
            boolean found = false;
            while(i < values.size() && !found) {
                if(values.get(i).getId() == Integer.parseInt(String.valueOf(holder.txtHeader.getText()).split(" ")[2])) found = true;
                else i++;
            }
            if(found) {
                Intent intent = new Intent(v.getContext(), Activity_Statistics_Specifications.class);
                intent.putExtra("id", String.valueOf(values.get(i).getId()));
                intent.putExtra("duration", String.valueOf(values.get(i).getDuration()));
                intent.putExtra("victory", String.valueOf(values.get(i).getVictory()));
                intent.putExtra("score", String.valueOf(values.get(i).getScore()));
                v.getContext().startActivity(intent);
            }*/
        });
    }

    // Return the size of your data (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(values == null) return 0;
        else return values.size();
    }
}