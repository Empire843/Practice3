package com.example.practice3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinner;
    private EditText editTitle, editPrice, editDate;
    private Button btnUpdate, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        editDate.setOnClickListener(this);
    }

    private void initView() {
        spinner = findViewById(R.id.spCategory);
        editTitle = findViewById(R.id.editTitle);
        editPrice = findViewById(R.id.editPrice);
        editDate = findViewById(R.id.editDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner, getResources().getStringArray(R.array.category)));

    }

    @Override
    public void onClick(View view) {
//        if (view == editDate) {
//            final Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
//                    String date = "";
//                    if (m > 8) {
//                        date = d + "/" + (m + 1) + "/" + y;
//                    } else {
//                        date = d + "/0" + (m + 1) + "/" + y;
//                    }
//                }
//            }, year, month, day);
//            dialog.show();
//        }
        if (view == editDate) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
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

            if (!title.isEmpty() && price.matches("\\d+")) {
                Item item = new Item(title, category, price, date);
                SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
                sqLiteHelper.addItem(item);
                finish();
            }
        }
        if (view == btnCancel) {
            finish();
        }
    }
}