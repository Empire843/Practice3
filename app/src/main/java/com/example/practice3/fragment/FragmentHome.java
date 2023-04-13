package com.example.practice3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice3.R;
import com.example.practice3.UpdateDeleteActivity;
import com.example.practice3.adapter.RecycleViewAdapter;
import com.example.practice3.database.SQLiteHelper;
import com.example.practice3.model.Item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FragmentHome extends Fragment implements RecycleViewAdapter.ItemListener {
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private SQLiteHelper sqLiteHelper;
    private TextView tvTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String tableName = "my_table";

// Delete all rows from the table
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTotal = view.findViewById(R.id.tvTong);
        adapter = new RecycleViewAdapter();
        sqLiteHelper = new SQLiteHelper(getContext());
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<Item> list = sqLiteHelper.getByDate(simpleDateFormat.format(date));
        adapter.setList(list);
        tvTotal.setText("Tổng tiền: " + sum(list));
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
    }

    private int sum(List<Item> list) {
        int sum = 0;
        for (Item item : list) {
            sum += Integer.parseInt(item.getPrice());
        }
        return sum;
    }

    @Override
    public void onItemClick(View view, int position) {
        System.out.println("clicked");
        Item item = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), UpdateDeleteActivity.class);
        intent.putExtra("item", item);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<Item> list = sqLiteHelper.getByDate(simpleDateFormat.format(date));
        adapter.setList(list);
        tvTotal.setText("Tổng tiền: " + sum(list));
    }
}
