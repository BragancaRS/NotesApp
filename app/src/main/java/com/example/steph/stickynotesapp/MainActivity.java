package com.example.steph.stickynotesapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    /**
     * The List view.
     */
    ListView listView;
    private String m_Text = "";
    /**
     * The Profile prefs.
     */
    SharedPreferences profilePrefs;
    /**
     * The constant Note.
     */
    public static final String Note = "note";
    /**
     * The Locked.
     */
    Boolean Locked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);

        profilePrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }


    @Override
    protected void onResume() {
        NoteDB db = new NoteDB(this);
        String title = null;
        ArrayList<Note> noteArrayList = db.getNote(title);
        List<Note> list = new ArrayList<>();
        if (noteArrayList != null) {
            int size = noteArrayList.size();
            for (int i = 0; i < size; i++) {
                list.add(noteArrayList.get(i));
            }
            Note[] dataSource = list.toArray(new Note[list.size()]);
            for (int i = 0; i < size; i++) {
                dataSource[i] = noteArrayList.get(i);
            }
            ArrayAdapter adapter = new NoteAdapter(MainActivity.this, R.layout.row, noteArrayList);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.setOnItemClickListener(this);
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        final Intent intent;
        switch (item.getItemId()) {
            case R.id.action_about:
                intent = new Intent(this, About.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                intent = new Intent(this, AddOrEditNotes.class);
                startActivity(intent);
                return true;
            case R.id.action_setting:
                intent = new Intent(this, Settings.class);
                try {
                    if (profilePrefs.getString("Password", null).isEmpty()) {
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Lock password");
// Set up the input
                        final EditText input = new EditText(this);
                        input.setHint("please enter your password");
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        builder.setView(input);
// Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text = input.getText().toString();
                                if (m_Text.matches(profilePrefs.getString("Password", null))) {
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Wrong password\n Hint: " + profilePrefs.getString("Hint", null), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                } catch (NullPointerException ex) {
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
        final TextView textView1 = (TextView) view.findViewById(R.id.notetitle);
        final TextView textView2 = (TextView) view.findViewById(R.id.noteDescription);
        NoteDB noteDB = new NoteDB(this);
        final ArrayList<Note> notes = noteDB.getNote(textView1.getText().toString());
        try {


            if (notes.get(0).getLocked() == 1 || profilePrefs.getString("Password", null).isEmpty()) {
                addOrEditNote(view, notes, textView1, textView2);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Lock password");
// Set up the input
                final EditText input = new EditText(this);
                input.setHint("please enter your password");
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                builder.setView(input);
// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        if (m_Text.matches(profilePrefs.getString("Password", null))) {
                            addOrEditNote(view, notes, textView1, textView2);
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong password\n Hint: " + profilePrefs.getString("Hint", null), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        } catch (NullPointerException ex) {

            addOrEditNote(view, notes, textView1, textView2);
        }
    }

    /**
     * Add or edit note.
     *
     * @param view      the view
     * @param notes     the notes
     * @param textView1 the text view 1
     * @param textView2 the text view 2
     */
    public void addOrEditNote(View view, ArrayList<Note> notes, TextView textView1, TextView textView2) {
        Intent intent = new Intent(this, AddOrEditNotes.class);
        int locked = notes.get(0).getLocked();
        Note note = new Note(textView1.getText().toString(), textView2.getText().toString(), notes.get(0).getContent(), notes.get(0).getCreateDate(), notes.get(0).getLastModified(), locked);
        intent.putExtra(Note, note);
        startActivity(intent);
    }

    /**
     * Go to add.
     *
     * @param view the view
     */
    public void goToAdd(View view) {
        Intent intent = new Intent(this, AddOrEditNotes.class);
        startActivity(intent);
    }

    /**
     * The type Note adapter.
     */
    public static class NoteAdapter extends ArrayAdapter<Note> {
        private final Context context;
        private final ArrayList<Note> data;
        private final int layoutResourceId;

        /**
         * Instantiates a new Note adapter.
         *
         * @param context          the context
         * @param layoutResourceId the layout resource id
         * @param data             the data
         */
        public NoteAdapter(Context context, int layoutResourceId, ArrayList<Note> data) {
            super(context, layoutResourceId, data);
            this.context = context;
            this.data = data;
            this.layoutResourceId = layoutResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;
            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.textView1 = (TextView) row.findViewById(R.id.notetitle);
                holder.textView2 = (TextView) row.findViewById(R.id.noteDescription);
                holder.textView3 = (TextView) row.findViewById(R.id.notemodified);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }
            Note note = data.get(position);
            holder.textView1.setText(note.getTitle());
            holder.textView2.setText(note.getDescription());
            if (note.getCreateDate().compareTo(note.getLastModified()) == 0)
                holder.textView3.setText("created: " + note.getCreateDate().toString());
            else
                holder.textView3.setText("Last modified: " + note.getLastModified().toString());
            return row;
        }

        /**
         * The type View holder.
         */
        static class ViewHolder {
            /**
             * The Text view 1.
             */
            TextView textView1;
            /**
             * The Text view 2.
             */
            TextView textView2;
            /**
             * The Text view 3.
             */
            TextView textView3;
        }
    }
}