package com.polygalov.a2_l1_notes_polygalov.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.polygalov.a2_l1_notes_polygalov.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Константин on 22.09.2017.
 */

public class FormAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTitle;
    private EditText editDescription;
    private Button addButton;

    public static String EXTRA_NOTE = "extra_note";
    public static String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;

    public static int REQUEST_ADD = 100;
    public static int RESULT_ADD = 101;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_UPDATE = 201;
    public static int RESULT_DELETE = 301;

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private Notes notes;
    private int position;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_form);

        editTitle = (EditText) findViewById(R.id.tittle_edit);
        editDescription = (EditText) findViewById(R.id.description_edit);
        addButton = (Button) findViewById(R.id.add_note_button);
        addButton.setOnClickListener(this);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        notes = getIntent().getParcelableExtra(EXTRA_NOTE);

        if (notes != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        }

        String actionBarTitle = null;
        String buttonTitle = null;

        if (isEdit) {
            actionBarTitle = "Изменить";
            buttonTitle = "Обновить";
            editTitle.setText(notes.getTitle());
            editDescription.setText(notes.getDescription());
        } else {
            actionBarTitle = "Добавить";
            buttonTitle = "Сохранить";
        }
        getSupportActionBar().setTitle(actionBarTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addButton.setText(buttonTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null) {
            noteHelper.close();
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.add_note_button) {
            String title = editTitle.getText().toString().trim();
            String description = editDescription.getText().toString().trim();

            boolean isEmpty = false;

            if (TextUtils.isEmpty(title)) {
                isEmpty = true;
                editTitle.setError("Поле не может быть пустым");
            }

            if (!isEmpty) {
                Notes newNote = new Notes();
                newNote.setTitle(title);
                newNote.setMessage(description);

                Intent intent = new Intent();
                if (isEdit) {
                    newNote.setDate(notes.getDate());
                    newNote.setId(notes.getId());
                    noteHelper.update(newNote);
                    intent.putExtra(EXTRA_POSITION, position);
                    setResult(RESULT_UPDATE, intent);
                    finish();
                } else {
                    newNote.setDate(getCurrentDate());
                    noteHelper.insert(newNote);

                    setResult(RESULT_ADD);
                    finish();
                }
            }
        }
    }

    private String getCurrentDate() {
        java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;

            case R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle = null, dialogMessage = null;

        if (isDialogClose) {
            dialogTitle = "null";
            dialogMessage = "Вы хотите отменить изменения в форме?";
        } else {
            dialogTitle = "Удалить заметку";
            dialogMessage = "Вы хотите удалить заметку?";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder.setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            noteHelper.delete(notes.getId());
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            setResult(RESULT_DELETE, intent);
                            finish();
                        }
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }
}
