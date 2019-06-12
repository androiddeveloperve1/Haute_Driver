package com.foodies.vedriver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.foodies.vedriver.R;
import com.foodies.vedriver.adapter.CurrentTaskAdapter;
import com.foodies.vedriver.adapter.NewTaskAdapter;
import com.foodies.vedriver.databinding.FragmentTasksBinding;
import com.foodies.vedriver.interfaces.RecycleItemClickListener;


/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class FragmentTasks extends Fragment {
    private FragmentTasksBinding binder;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_tasks, container, false);

        binder.rvCurrentTask.setLayoutManager(new LinearLayoutManager(getActivity()));

        binder.rvNewTask.setLayoutManager(new LinearLayoutManager(getActivity()));
        binder.setCurrentTaskAdapter(new CurrentTaskAdapter(new RecycleItemClickListener() {
            @Override
            public void onItemClicked(int position, Object data) {

            }
        }));
        binder.setNewTaskAdapter(new NewTaskAdapter(new RecycleItemClickListener() {
            @Override
            public void onItemClicked(int position, Object data) {

            }
        }));
        View view = binder.getRoot();
        return view;
    }

    public class Presenter {
        public void onHomeClicked(View view) {
        }
    }
}
