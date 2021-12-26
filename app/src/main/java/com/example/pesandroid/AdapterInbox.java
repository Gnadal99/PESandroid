package com.example.pesandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pesandroid.models.Mail;
import java.util.List;

public class AdapterInbox extends RecyclerView.Adapter<AdapterInbox.ViewHolder> {

    //Data to view on recyclerView is stored here
    private List<Mail> values;

    //Create complementary class ViewHolder for setting the layout structure
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Objects in the layout are saved here (see file res.values.row_layout_no_images.xml)
        public TextView mailOut;
        public TextView dateOut;
        public TextView titleOut;
        public TextView bodyOut;
        public View layout;

        //Constructor
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

    // Create new views (invoked by the layout manager) (view = line of recyclerView)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view giving an XML file as parameter
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout_no_images, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Get element from data at a given position
        Mail m = values.get(position);

        //Replace the contents of the view with that element
        holder.mailOut.setText(m.mail);
        holder.dateOut.setText(m.date.toString());
        holder.titleOut.setText(m.message.title);
        holder.bodyOut.setText(m.message.body);

        //The code inside will be executed when clicking the mail of any message in recyclerView
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

    // Return the size of data (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(values == null) return 0;
        else return values.size();
    }
}