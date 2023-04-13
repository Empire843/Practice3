package com.example.practice3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.practice3.database.SQLiteHelper;
import com.example.practice3.model.Item;

import java.util.Calendar;

public class UpdateDeleteActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinner;
    private EditText editTitle, editPrice, editDate;
    private Button btnUpdate, btnBack, btnRemove;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        initView();
        btnBack.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        editDate.setOnClickListener(this);
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        editTitle.setText(item.getTitle());
        editPrice.setText(item.getPrice());
        editDate.setText(item.getDate());
        int p = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item.getCategory())) {
                p = i;
                break;
            }
        }
        spinner.setSelection(p);
    }

    private void initView() {
        spinner = findViewById(R.id.spCategory);
        editTitle = findViewById(R.id.editTitle);
        editPrice = findViewById(R.id.editPrice);
        editDate = findViewById(R.id.editDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
        btnRemove = findViewById(R.id.btnRemove);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner, getResources().getStringArray(R.array.category)));

    }

    @Override
    public void onClick(View view) {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        if (view == editDate) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(UpdateDeleteActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String formattedMonth = (m + 1) < 10 ? "0" + (m + 1) : String.valueOf(m + 1);
                    String formattedDay = d < 10 ? "0" + d : String.valueOf(d);
                    String date = formattedDay + "/" + formattedMonth + "/" + y;
                    editDate.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }
        if (view == btnUpdate) {
            String title = editTitle.getText().toString();
            String category = spinner.getSelectedItem().toString();
            String price = editPrice.getText().toString();
            String date = editDate.getText().toString();
            int id = item.getId();

            if (!title.isEmpty() && price.matches("\\d+")) {
                Item item = new Item(id, title, category, price, date);
                sqLiteHelper.update(item);
                finish();
            }
        }
        if (view == btnRemove) {
            int id = item.getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có chắc muốn xóa \"" + item.getTitle() + "\" không?");
            builder.setIcon(R.drawable.ic_delete);
            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                sqLiteHelper.delete(id);
                finish();
            });
            builder.setNegativeButton("Không", (dialogInterface, i) -> {

            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (view == btnBack) {
            finish();
        }
    }
}