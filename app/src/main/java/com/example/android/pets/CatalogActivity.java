package com.example.android.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.android.pets.data.FeedReaderDbHelper;
import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 0;

    PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView petListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        mCursorAdapter = new PetCursorAdapter(this, null);

        petListView.setAdapter(mCursorAdapter);

        //Set intem click listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);
                //Set the URI on the data field on the intet
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }


    private void insertPet(){


        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        // Insert the new row, returning the uri with primary key value of the new row

        Uri uri = getContentResolver().insert(PetEntry.CONTENT_URI, values);

        long newRowId = ContentUris.parseId(uri);
        Toast.makeText(this, R.string.succes_adding_pet, Toast.LENGTH_SHORT).show();
    }

    private void updatePet(){

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Milo");
        values.put(PetEntry.COLUMN_PET_BREED, "French Bulldog");
        values.put(PetEntry.COLUMN_PET_WEIGHT, 20);

        String selection = PetEntry.COLUMN_PET_NAME+"=?";
        String[] selectionArgs = {"Toto"};

        // Updating the new row, returning number of updated rows
        int numberOfUpdatedRows = getContentResolver().update(PetEntry.CONTENT_URI, values, selection, selectionArgs);
        Toast.makeText(this, numberOfUpdatedRows+" Were updated", Toast.LENGTH_SHORT).show();
    }

    private void deletePet(){
        String selection = null;
        String[] selectionArgs = null;
        int numberOfDeletedRows = getContentResolver().delete(PetEntry.CONTENT_URI, selection, selectionArgs);
        Toast.makeText(this, "Au fost sterse " + numberOfDeletedRows + " rinduri", Toast.LENGTH_SHORT).show();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deletePet();
                return true;
            //Update all the pets that were added as dummy data
            case R.id.action_update_dummy_data:
                updatePet();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_WEIGHT
        };

        String selection = null;
        String[] selectionArgs = null;

        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Udate {@link PetCursorAdapter} with this new custor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback calles when this data needs to be deleted
        mCursorAdapter.swapCursor(null);

    }
}