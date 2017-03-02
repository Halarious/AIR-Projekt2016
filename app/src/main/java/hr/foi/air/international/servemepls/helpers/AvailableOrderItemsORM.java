package hr.foi.air.international.servemepls.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import hr.foi.air.international.servemepls.models.AvailableItem;

public class AvailableOrderItemsORM
{
    private static final String TAG               = "AvailableOrderItemsORM";

    private static final String TABLE_NAME        = "available_order_items";

    private static final String COMMA_SEP         = ",";

    private static final String COLUMN_ID_TYPE    = "INTEGER PRIMARY KEY";
    private static final String COLUMN_ID         = "id";

    private static final String COLUMN_LABEL_TYPE = "TEXT";
    private static final String COLUMN_LABEL      = "label";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " +
                                                   TABLE_NAME     + "(" +
                                                   COLUMN_ID      + " " +
                                                   COLUMN_ID_TYPE + ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static void insertItem(Context context, AvailableItem availableItem)
    {
        SQLiteHandler  databaseWrapper = new SQLiteHandler(context);
        SQLiteDatabase database        = databaseWrapper.getWritableDatabase();

        ContentValues values = itemToContentValues(availableItem);
        long itemId = database.insert(AvailableOrderItemsORM.TABLE_NAME, "null", values);
        Log.i(TAG, "Inserted new item with ID: " + itemId);

        database.close();
    }

    private static ContentValues itemToContentValues(AvailableItem availableItem)
    {
        ContentValues values = new ContentValues();
        values.put( AvailableOrderItemsORM.COLUMN_ID,    availableItem.id);
        values.put( AvailableOrderItemsORM.COLUMN_LABEL, availableItem.label);

        return values;
    }

    public static ArrayList<AvailableItem> getItems(Context context)
    {
        SQLiteHandler databaseWrapper = new SQLiteHandler(context);
        SQLiteDatabase database       = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + AvailableOrderItemsORM.TABLE_NAME,
                                          null);

        Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
        ArrayList<AvailableItem> itemList = new ArrayList<AvailableItem>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                AvailableItem item = cursorToItem(cursor);
                itemList.add(item);
                cursor.moveToNext();
            }
            Log.i(TAG, "Posts loaded successfully.");
        }

        database.close();

        return itemList;
    }

    private static AvailableItem cursorToItem(Cursor cursor)
    {
        AvailableItem post = new AvailableItem();
        post.id    = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        post.label = cursor.getString(cursor.getColumnIndex(COLUMN_LABEL));

        return post;
    }
}
