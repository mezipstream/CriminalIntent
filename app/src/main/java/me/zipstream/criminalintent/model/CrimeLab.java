package me.zipstream.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.zipstream.criminalintent.database.CrimeBaseHelper;
import me.zipstream.criminalintent.database.CrimeCursorWrapper;
import me.zipstream.criminalintent.database.CrimeDbSchema;
import me.zipstream.criminalintent.database.CrimeDbSchema.CrimeTable;

/**
 * 模型层的好处在添加数据库的时刻一览无遗.
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab;

    //    private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
//        mCrimes = new ArrayList<>();
    }

    public void addCrime(Crime crime) {
//        mCrimes.add(crime);
        ContentValues values = getContentValues(crime);

        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void deleteCrime(Crime crime) {
//        mCrimes.remove(crime);
        mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{crime.getId().toString()}
        );
    }

    public List<Crime> getCrimes() {
//        return mCrimes;
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID uuid) {
        /*for (Crime crime : mCrimes) {
            if (crime.getId().equals(uuid)) {
                return crime;
            }
        }*/

        CrimeCursorWrapper cursorWrapper = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuid.toString()}
        );

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();

            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }

    /**
     * it verifies that there is external storage to save them to.
     * if there is no external storage, return null.
     * it does not create any files on the filesystem.
     * @param crime
     * @return
     */
    public File getPhotoFile(Crime crime) {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFilename());
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }
}
