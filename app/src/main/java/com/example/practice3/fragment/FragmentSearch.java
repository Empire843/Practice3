package com.example.practice3.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice3.AddActivity;
import com.example.practice3.R;
import com.example.practice3.adapter.RecycleViewAdapter;
import com.example.practice3.database.SQLiteHelper;
import com.example.practice3.model.Item;

import java.util.Calendar;
import java.util.List;

public class FragmentSearch extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private TextView tvTong;
    private Button btnSearch;
    private SearchView searchView;
    private EditText editFrom;
    private EditText editTo;
    private Spinner spCategory;
    private RecycleViewAdapter adapter;
    private SQLiteHelper sqLiteHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        adapter = new RecycleViewAdapter();
        sqLiteHelper = new SQLiteHelper(getContext());
        List<Item> list = sqLiteHelper.getAll();
        adapter.setList(list);
        tvTong.setText("Tổng: " + sumPrice(list) + " VND");
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
//----------------------------------------------------------------------------------
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Item> list = sqLiteHelper.searchByTitle(s);
                tvTong.setText("Tổng: " + sumPrice(list) + " VND");
                adapter.setList(list);
                return true;
            }
        });
//        ----------------------------------------------------------
        btnSearch.setOnClickListener(this);
        editFrom.setOnClickListener(this);
        editTo.setOnClickListener(this);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String category = spCategory.getSelectedItem().toString();
                List<Item> list;

                if(!category.equals("All")){
                    list = sqLiteHelper.searchByCategory(category);
                }else{
                    list = sqLiteHelper.getAll();
                }
                tvTong.setText("Tổng: " + sumPrice(list) + " VND");
                adapter.setList(list);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private int sumPrice(List<Item> list) {
        int sum = 0;
        for (Item item : list) {
            sum += Integer.parseInt(item.getPrice());
        }
        return sum;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTong = view.findViewById(R.id.tvTong);
        btnSearch = view.findViewById(R.id.btnSearch);
        searchView = view.findViewById(R.id.searchView);
        editFrom = view.findViewById(R.id.editFrom);
        editTo = view.findViewById(R.id.editTo);
        spCategory = view.findViewById(R.id.spCategory);
        btnSearch.setOnClickListener(this);

        String[] arr = getResources().getStringArray(R.array.category);
        String[] arr1 = new String[arr.length + 1];
        arr1[0] = "All";

        for (int i = 0; i < arr.length; i++) {
            arr1[i + 1] = arr[i];
        }
        spCategory.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.item_spinner, arr1));
    }

    @Override
    public void onClick(View view) {
        if (view == editFrom) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String formattedMonth = (m + 1) < 10 ? "0" + (m + 1) : String.valueOf(m + 1);
                    String formattedDay = d < 10 ? "0" + d : String.valueOf(d);
                    String date = formattedDay + "/" + formattedMonth + "/" + y;
                    editFrom.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }
        if (view == editTo) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String formattedMonth = (m + 1) < 10 ? "0" + (m + 1) : String.valueOf(m + 1);
                    String formattedDay = d < 10 ? "0" + d : String.valueOf(d);
                    String date = formattedDay + "/" + formattedMonth + "/" + y;
                    editTo.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }
        if(view == btnSearch){
            String from = editFrom.getText().toString();
            String to = editTo.getText().toString();
            List<Item> list = sqLiteHelper.searchByDateFromTo(from, to);
            tvTong.setText("Tổng: " + sumPrice(list) + " VND");
            adapter.setList(list);

        }
    }
}
