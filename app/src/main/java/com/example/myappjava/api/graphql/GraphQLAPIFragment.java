package com.example.myappjava.api.graphql;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Todo;
import com.example.myappjava.R;
import com.example.myappjava.config.base.BaseFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GraphQLAPIFragment extends BaseFragment {
    private TodoAdapter adapter;
    private SwipeRefreshLayout todoSRL;
    private TextInputLayout todoTIL;
    private TextInputEditText todoET;
    private CheckBox statusCB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graphql_api, container, false);

        adapter = new TodoAdapter();
        RecyclerView todoRV = view.findViewById(R.id.todoRV);
        todoRV.setAdapter(adapter);

        todoSRL = view.findViewById(R.id.todoSRL);
        todoSRL.setOnRefreshListener(this::listTodo);

        todoTIL = view.findViewById(R.id.descriptionTIL);
        todoET = view.findViewById(R.id.descriptionET);
        statusCB = view.findViewById(R.id.completedCB);

        view.findViewById(R.id.modifyBtn).setOnClickListener(v -> addTodo());

        listTodo();

        return view;
    }

    private void addTodo() {
        String decs = Objects.requireNonNull(todoET.getText()).toString();

        if (decs.isEmpty()) {
            todoTIL.setError("This cannot be empty");
            return;
        }

        Todo todo = Todo.builder()
                .description(decs)
                .isCompleted(statusCB.isChecked())
                .build();

        Amplify.API.mutate(ModelMutation.create(todo),
                response -> {
                    Log.i(TAG, "New todo created : " + response.getData().toString());
                    if (isRemoving()) return;

                    requireActivity().runOnUiThread(() -> {
                        // clear the input
                        todoET.setText(null);
                        statusCB.setChecked(false);
                    });

                    // load the new list
                    listTodo();
                },
                error -> Log.e(TAG, "Create failed", error)
        );

    }

    private void listTodo() {
        Amplify.API.query(
                ModelQuery.list(Todo.class),
                response -> {
                    if (isRemoving()) return;

                    final List<Todo> target = new ArrayList<>();
                    if (response.hasData()) {
                        response.getData().forEach(target::add);
                        requireActivity().runOnUiThread(() -> {
                            adapter.setItems(target);
                            todoSRL.setRefreshing(false);
                        });
                    }
                },
                error -> Log.e(TAG, "Query failure", error)
        );
    }
}
