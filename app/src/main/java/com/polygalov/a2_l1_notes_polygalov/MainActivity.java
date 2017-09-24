package com.polygalov.a2_l1_notes_polygalov;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.polygalov.a2_l1_notes_polygalov.notes.FormAddUpdateActivity;
import com.polygalov.a2_l1_notes_polygalov.notes.NoteHelper;
import com.polygalov.a2_l1_notes_polygalov.notes.Notes;
import com.polygalov.a2_l1_notes_polygalov.notes.NotesAdapter;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener{

    private RecyclerView recyclerViewNotes;
    private ProgressBar progressBar;

    private LinkedList<Notes> list;
    private NotesAdapter notesAdapter;
    private NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Notes");

        recyclerViewNotes = (RecyclerView) findViewById(R.id.rv_notes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setHasFixedSize(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        noteHelper = new NoteHelper(this);
        noteHelper.open();

        list = new LinkedList<>();

        notesAdapter = new NotesAdapter(this);
        notesAdapter.setListNotes(list);
        recyclerViewNotes.setAdapter(notesAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        new LoadNoteAsync();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadNoteAsync().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {

        } else if (id == R.id.nav_calendar) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            Intent intent = new Intent(MainActivity.this, FormAddUpdateActivity.class);
            startActivityForResult(intent, FormAddUpdateActivity.REQUEST_ADD);
        }
    }

    private class LoadNoteAsync extends AsyncTask<Void, Void, ArrayList<Notes>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            if (list.size() > 0) {
                list.clear();
            }
        }

        @Override
        protected ArrayList<Notes> doInBackground(Void... voids) {
            return noteHelper.getAllData();
        }

        @Override
        protected void onPostExecute(ArrayList<Notes> notes) {
            super.onPostExecute(notes);
            progressBar.setVisibility(View.GONE);

            list.addAll(notes);
            notesAdapter.setListNotes(list);
            notesAdapter.notifyDataSetChanged();

            if (list.size() == 0) {
                showSnackbarMessage("Нет заметок");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FormAddUpdateActivity.REQUEST_ADD) {
            if (requestCode == FormAddUpdateActivity.RESULT_ADD) {
                recyclerViewNotes.getLayoutManager().smoothScrollToPosition(recyclerViewNotes, new RecyclerView.State(), 0);
            }
        }

        if (requestCode == FormAddUpdateActivity.REQUEST_UPDATE) {
            if (requestCode == FormAddUpdateActivity.RESULT_UPDATE) {
                int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
                showSnackbarMessage("Заметка успешно изменена");
                recyclerViewNotes.getLayoutManager().smoothScrollToPosition(recyclerViewNotes, new RecyclerView.State(), position);
            }
        }

        if (requestCode == FormAddUpdateActivity.RESULT_DELETE) {
            int position = data.getIntExtra(FormAddUpdateActivity.EXTRA_POSITION, 0);
            list.remove(position);
            notesAdapter.setListNotes(list);
            notesAdapter.notifyDataSetChanged();
            showSnackbarMessage("Заметка успешно удалена");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noteHelper != null) {
            noteHelper.close();
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(recyclerViewNotes, message, Snackbar.LENGTH_SHORT).show();
    }
}
