package moun.com.deli.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class To write to and read from the database
 * we need a SQLiteDatabase object (that represents the database)
 * which can be obtained by calling getWritableDatabase() on the database helper.
 */
public class ItemsDBDAO {

    protected SQLiteDatabase database;
    private DataBaseHelper dbHelper;
    private Context mContext;

    public ItemsDBDAO(Context context) {
        this.mContext = context;
        dbHelper = DataBaseHelper.getHelper(mContext);
        open();

    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = DataBaseHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

	/*public void close() {
		dbHelper.close();
		database = null;
	}*/
}
