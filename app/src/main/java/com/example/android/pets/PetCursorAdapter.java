package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

/**
 * Created by dima on 6/19/17.
 */

public class PetCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link PetCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
       return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Display the number of rows in the Cursor (which reflects the number of rows in the
        // pets table in the database).
        TextView petNameView = (TextView) view.findViewById(R.id.pet_name);
        TextView petBreedView = (TextView) view.findViewById(R.id.pet_breed);


        // Figure out the index of each column
       // int idColumnIndex = cursor.getColumnIndex(PetContract.PetEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
//        int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
//        int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);

            // Use that Index to extract the String or Int Value of the word
            //  at the current row the cursor is on.
//        int currentID = cursor.getInt(idColumnIndex);
        String currentName = cursor.getString(nameColumnIndex);
        String currentBreed = cursor.getString(breedColumnIndex);
//        int currentGender = cursor.getInt(genderColumnIndex);
//        int currentWeight = cursor.getInt(weightColumnIndex);

        if(currentBreed.equals("")){
            currentBreed = "Unknown Breed";
        }
        petNameView.setText(currentName);
        petBreedView.setText(currentBreed);
    }


}
