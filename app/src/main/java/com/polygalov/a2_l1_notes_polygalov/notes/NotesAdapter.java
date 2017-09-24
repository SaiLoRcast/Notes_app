package com.polygalov.a2_l1_notes_polygalov.notes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polygalov.a2_l1_notes_polygalov.R;

import java.util.LinkedList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private LinkedList<Notes> notes;
    private Activity activity;

    public NotesAdapter(Activity activity) {
        this.activity = activity;
    }

    public LinkedList<Notes> getListNotes() {
        return notes;
    }

    public void setListNotes(LinkedList<Notes> notes) {
        this.notes = notes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NotesAdapter.ViewHolder holder, int position) {
        holder.titleTV.setText(getListNotes().get(position).getTitle());
        holder.dateTV.setText(getListNotes().get(position).getDate());
        holder.messageTV.setText(getListNotes().get(position).getDescription());
        holder.noteCV.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallBack() {
            @Override
            public void OnItemClicked(View view, int position) {
                Intent intent = new Intent(activity, FormAddUpdateActivity.class);
                intent.putExtra(FormAddUpdateActivity.EXTRA_POSITION, position);
                intent.putExtra(FormAddUpdateActivity.EXTRA_NOTE, getListNotes().get(position));
                activity.startActivityForResult(intent, FormAddUpdateActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return getListNotes().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTV, messageTV, dateTV;
        CardView noteCV;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.notes_title_tv);
            messageTV = itemView.findViewById(R.id.notes_message_tv);
            dateTV = itemView.findViewById(R.id.notes_date_tv);
            noteCV = itemView.findViewById(R.id.item_note_card_view);
        }
    }
}