package com.example.ogenna_esimai_simple_to_do;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);
        //etItem.setText("I'm doing this using Java");

        loadItems();
        /*items = new ArrayList<>();
        items.add("Buy milk");
        items.add("Go to the gym");
        items.add("Finish CodePath Android course application prework already");*/

        //first thing we want to do is construct our Adapter
        //note that it is located below where we established what our items are
        //it will return to us an ItemsAdapter (he typically coded RHS of assignment = sign then coded LHS)

        //Ctrl-O to override, shows options
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //5) Delete the item from the model
                items.remove(position);
                // 6) notify the adapter the position we deleted from but before we can do that
                // let's make final ItemsAdapter itemsAdapter = new ItemsAdapter(items); instead of
                // being a local varaiable let's move it to a field where it'll be accessible to all
                // the methods in the class.
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single click at position" + position);
                // create the new activity via intent = request to Android system (learn more about intents CodePath )
                Intent i = new Intent( MainActivity.this, EditActivity.class);
                // pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        // 3) Let's do that. Let's define an instance of ItemsAdapter, OnLongClickListener


        // 4) this ItemsAdapter.OnLongClickListener onLongClickListener is what we'll pass on to
        // the final ItemsAdapter itemsAdapter = new ItemsAdapter(items);
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        // 7) previously final ItemsAdapter itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        // now itemsAdapter = new ItemsAdapter(items, onLongClickListener);

        // 2) Back from ItemsAdapter(.java), we now need to pass in a second parameter into
        // ItemsAdapter() which is the OnClickListener
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        //add OnClickListener so that when user clicks on btn, we take the corresponding action
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //add item to the model
                items.add(todoItem);
                //notify adapter that an item is inserted, added
                //notify adapter that an item is inserted, added
                itemsAdapter.notifyItemInserted(items.size()-1);
                //clear the edit text once we've submitted it
                etItem.setText("");
                //give the user feedback via a "toast" - a small transient pop-up or dialogue
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                // 1) Next, to remove logic from list by pressing and holding. remove any item from the
                // list(long holding) on that item. To ItemsAdapter(.java)
                saveItems();
            }
        });
    }

    // purpose here is handle the result of the edit Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // super.onActivityResult(requestCode, resultCode, data); //let's get rid of the initial implementation
        //check if result codes match what we passed in
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // update the model at the right position with new item text
            items.set(position, itemText);

            // notify the adapter
            itemsAdapter.notifyItemChanged(position);
            // persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }

    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }
    // This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // This function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error writing items", e);
        }

    }
}

