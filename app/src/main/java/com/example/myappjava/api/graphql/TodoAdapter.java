package com.example.myappjava.api.graphql;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Todo;
import com.example.myappjava.R;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<Todo> items;

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.bindTo(items.get(position));
    }


    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void setItems(List<Todo> target) {
        items = target;
        notifyDataSetChanged();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        private final TextView descTV, statusTV;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            descTV = itemView.findViewById(R.id.descTV);
            statusTV = itemView.findViewById(R.id.statusTV);
        }

        public void bindTo(Todo todo) {
            descTV.setText(todo.getDescription());
            statusTV.setText(todo.getIsCompleted() ? "Completed" : "Pending");
        }
    }
}
