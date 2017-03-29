package fr.joffreylagut.itemdisplayer.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * PhotoDbHelper.java
 * Purpose: Handle all of the request to the database.
 *
 * @author Joffrey LAGUT
 * @version 1.2 2017-03-28
 */

public class PhotoDbHelper extends SQLiteOpenHelper {

    // Version of the database.
    private static final int DATABASE_VERSION = 1;

    // Name of the database
    private static final String DATABASE_NAME = "itemdisplayer.db";

    // Singleton
    private static PhotoDbHelper sInstance;

    // Constant used in log
    private String TAG = "UserDbHelper";

    // Constructor
    private PhotoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This function is returning the current instance of the database.
     * This ensure that there is only one instance running at the same time.
     *
     * @param context context needed to create the helper.
     * @return a PhotoDbHelper to communicate with the database.
     */
    public static synchronized PhotoDbHelper getInstance(Context context) {

        // Use the application context, which will ensure we
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new PhotoDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Called when the database is created for the first time.
     * This method create all of the tables.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // We create the Photos table
        createPhotosTable(db);
    }

    /**
     * Called when the database needs to be upgraded.
     * We can put the modifications to do in DB here.
     *
     * @param db         The database to work on.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // We drop the table because our DB is made to handle items offline
        dropPhotosTable(db);
    }

    /********************************************************************************************
     * PHOTOS TABLE
     ********************************************************************************************/

    /**
     * Function that create the Photos table in db.
     *
     * @param db Database that we are working on.
     */
    private void createPhotosTable(SQLiteDatabase db) {
        final String SQL_CREATE_PHOTOS_TABLE =
                "CREATE TABLE " + PhotoContract.PhotoEntry.TABLE_NAME + " (" +
                        PhotoContract.PhotoEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        PhotoContract.PhotoEntry.COLUMN_ALBUM_ID + " INTEGER, " +
                        PhotoContract.PhotoEntry.COLUMN_TITLE + " TEXT, " +
                        PhotoContract.PhotoEntry.COLUMN_URL + " TEXT, " +
                        PhotoContract.PhotoEntry.COLUMN_THUMBNAIL_URL + " TEXT " +
                        ");";
        db.execSQL(SQL_CREATE_PHOTOS_TABLE);
    }

    /**
     * Function that alter the Photos table in db.
     *
     * @param db Database that we are working on.
     */
    private void alterPhotosTable(SQLiteDatabase db) {
        // Write here the request to alter the Photos table.
        final String SQL_ALTER_PHOTOS_TABLE = "";
        // We execute the SQL request only if there is a request.
        if (SQL_ALTER_PHOTOS_TABLE.length() > 0) {
            db.execSQL(SQL_ALTER_PHOTOS_TABLE);
        }
    }

    /**
     * Function that drop the Photos table in db.
     *
     * @param db Database that we are working on.
     */
    private void dropPhotosTable(SQLiteDatabase db) {
        final String SQL_DELETE_USER_TABLE =
                "DROP TABLE IF EXISTS " + PhotoContract.PhotoEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_USER_TABLE);
    }

    /**
     * Function doing the request in database and returning the result.
     *
     * @param db        database that we are working on
     * @param columns   columns that we want in the cursor. All if null.
     * @param where     Where clauses.
     * @param whereArgs Where arguments.
     * @param groupBy   SQL Group By
     * @param having    SQL Having
     * @param order     Order
     * @param limit     limit
     * @return Cursor result
     */
    private Cursor selectPhotos(SQLiteDatabase db,
                                String[] columns,
                                String where,
                                String whereArgs[],
                                String groupBy,
                                String having,
                                String order,
                                String limit) {
        Cursor cursor = db.query(PhotoContract.PhotoEntry.TABLE_NAME,
                columns,
                where,
                whereArgs,
                groupBy,
                having,
                order,
                limit);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /**
     * Function returning all of the Photos in Photos table.
     *
     * @param db Database to look into.
     * @return List<Photo> list of all the photos in db. Null if there is none.
     */
    public List<Photo> getAllPhotos(SQLiteDatabase db) {
        return getAllPhotos(db, 0, 0);
    }

    /**
     * Function returning all of the Photos in Photos table.
     *
     * @param db            Database to look into.
     * @param page          Number of the page to display
     * @param photosPerPage Amount of photos that we wants to display per page.
     * @return List<Photo> list of all the photos in db. Null if there is none.
     */
    public List<Photo> getAllPhotos(SQLiteDatabase db, int page, int photosPerPage) {

        // We create a new list of photos
        List<Photo> allPhotos = new ArrayList<>();

        Cursor cursor;

        // If page and/or photosPerPage = 0, we get all of the photos
        if (page == 0 || photosPerPage == 0) {
            cursor = selectPhotos(db, null, null, null, null, null, null, null);
        } else {
            // We have to get only a range of photos
            int rowsToIgnore = photosPerPage * (page - 1);
            String limit = String.valueOf(rowsToIgnore) + "," + String.valueOf(photosPerPage);
            cursor = selectPhotos(db, null, null, null, null, null, null, limit);
        }
        // If there is no result, we return null and log a message.
        if (cursor.getCount() == 0) {
            Log.i(TAG, "getAllPhotos: There is no photos in db.");
            cursor.close();
            return allPhotos;
        }

        // There is photos, we have to get all of them
        allPhotos = cursorToPhotos(cursor);

        // We close the cursor
        cursor.close();

        // And return the photos
        return allPhotos;
    }

    /**
     * Function returning all of the Photos in the album with the id in parameter.
     *
     * @param db Database to look into.
     * @return List<Photo> list of all the photos in the album. Null if there is none.
     */
    public List<Photo> getAllPhotosInAlbumWithId(SQLiteDatabase db, int id,
                                                 int page, int photosPerPage) {

        // We create a new list of photos
        List<Photo> allPhotos = new ArrayList<>();

        // We prepare the conditions and execute the request
        String where = PhotoContract.PhotoEntry.COLUMN_ALBUM_ID + " =?";
        String whereArg[] = {String.valueOf(id)};

        Cursor cursor;

        // If page and/or photosPerPage = 0, we get all of the photos
        if (page == 0 || photosPerPage == 0) {
            cursor = selectPhotos(db, null, where, whereArg, null, null, null, null);
        } else {
            // We have to get only a range of photos
            int rowsToIgnore = photosPerPage * (page - 1);
            String limit = String.valueOf(rowsToIgnore) + "," + String.valueOf(photosPerPage);
            cursor = selectPhotos(db, null, where, whereArg, null, null, null, limit);
        }

        // If there is no result, we return null and log a message.
        if (cursor.getCount() == 0) {
            Log.i(TAG, "getAllPhotos: There is no photos in db.");
            cursor.close();
            return allPhotos;
        }

        // There is photos, we have to get all of them
        allPhotos = cursorToPhotos(cursor);

        // We close the cursor
        cursor.close();

        // And return the photos
        return allPhotos;
    }

    /**
     * Function returning the photo with the id in parameter
     *
     * @param db Database to look into.
     * @return photo with the id in parameter. Null if there is no photo.
     */
    public Photo getPhotoById(SQLiteDatabase db, int id) {

        String where = PhotoContract.PhotoEntry.COLUMN_ID + " =?";
        String whereArg[] = {String.valueOf(id)};

        Cursor cursor = selectPhotos(db, null, where, whereArg, null, null, null, null);
        // If there is no result, we return null and log a message.
        if (cursor.getCount() == 0) {
            Log.i(TAG, "getPhotoById: There is no photos with the id " + id + "in db.");
            cursor.close();
            return null;
        }
        // We are going to the first row
        cursor.moveToFirst();
        // We create a new photo object.
        Photo photo = new Photo();

        // We insert all the information of the row in the current photo.
        photo.setId(
                cursor.getInt(cursor.getColumnIndex(PhotoContract.PhotoEntry.COLUMN_ID)));
        photo.setAlbumId(
                cursor.getInt(cursor.getColumnIndex(PhotoContract.PhotoEntry.COLUMN_ALBUM_ID)));
        photo.setTitle(
                cursor.getString(cursor.getColumnIndex(PhotoContract.PhotoEntry.COLUMN_TITLE)));

        // We have to create URL. We surround it with a try catch to be sure that
        // we have valid url.
        try {
            URL urlPhoto = new URL(cursor.getString(cursor.getColumnIndex(
                    PhotoContract.PhotoEntry.COLUMN_URL)));
            photo.setUrl(urlPhoto);
            URL urlThumbnail = new URL(cursor.getString(cursor.getColumnIndex(
                    PhotoContract.PhotoEntry.COLUMN_THUMBNAIL_URL)));
            photo.setThumbnailUrl(urlThumbnail);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        cursor.close();
        return photo;
    }

    /**
     * Method that add a new photo in the photos table.
     *
     * @param db    database that we are working on.
     * @param photo photo that we want to insert in db.
     */
    public void addNewPhoto(SQLiteDatabase db, Photo photo) {

        // We create a new ContentValue that represent a row in our table
        ContentValues photoRow = new ContentValues();

        // We add all the columns and put the photo information
        photoRow.put(PhotoContract.PhotoEntry.COLUMN_ID, photo.getId());
        photoRow.put(PhotoContract.PhotoEntry.COLUMN_ALBUM_ID, photo.getAlbumId());
        photoRow.put(PhotoContract.PhotoEntry.COLUMN_TITLE, photo.getTitle());
        photoRow.put(PhotoContract.PhotoEntry.COLUMN_URL, photo.getUrl().toString());
        photoRow.put(PhotoContract.PhotoEntry.COLUMN_THUMBNAIL_URL,
                photo.getThumbnailUrl().toString());

        // We insert the photo in db.
        db.insert(PhotoContract.PhotoEntry.TABLE_NAME, null, photoRow);
    }

    /**
     * Method that update a photo in the photos table.
     *
     * @param db    database that we are working on.
     * @param photo photo that we want to update in db.
     */
    public void updatePhoto(SQLiteDatabase db, Photo photo) {

        // We create a new ContentValue that represent a row in our table
        ContentValues photoRow = new ContentValues();

        // We add all the columns and put the photo information
        photoRow.put(PhotoContract.PhotoEntry.COLUMN_ID, photo.getId());
        photoRow.put(PhotoContract.PhotoEntry.COLUMN_ALBUM_ID, photo.getAlbumId());
        photoRow.put(PhotoContract.PhotoEntry.COLUMN_TITLE, photo.getTitle());
        photoRow.put(PhotoContract.PhotoEntry.COLUMN_URL, photo.getUrl().toString());
        photoRow.put(PhotoContract.PhotoEntry.COLUMN_THUMBNAIL_URL,
                photo.getThumbnailUrl().toString());

        // We update the photo in db
        String where = PhotoContract.PhotoEntry.COLUMN_ID + "=?";
        String[] whereArg = {String.valueOf(photo.getId())};

        db.update(PhotoContract.PhotoEntry.TABLE_NAME, photoRow, where, whereArg);
    }

    /**
     * Delete the photo owning the id in parameter.
     *
     * @param db database that we are working on.
     * @param id id of the photo that we want to delete.
     * @return true if a photo have been deleted.
     */
    public boolean deletePhoto(SQLiteDatabase db, int id) {
        // We update the photo in db
        String where = PhotoContract.PhotoEntry.COLUMN_ID + "=?";
        String[] whereArg = {String.valueOf(id)};

        return db.delete(PhotoContract.PhotoEntry.TABLE_NAME, where, whereArg) > 0;
    }


    /**
     * Function returning all of the Albums in database.
     *
     * @return List<Photo> list of all the photos in db. Null if there is none.
     */
    public List<Album> getAllAlbums(SQLiteDatabase db) {

        return getAllAlbums(db, 0, 0);

    }

    /**
     * Function returning all of the Albums in database.
     * Return all the albums if one of the parameters is equal to 0.
     *
     * @param db           Database to look into.
     * @param page         Number of the page to display.
     * @param albumPerPage Amount of photos that we wants to display per page.
     * @return List<Photo> list of all the photos in db. Null if there is none.
     */
    public List<Album> getAllAlbums(SQLiteDatabase db, int page, int albumPerPage) {


        Cursor cursor;

        // We wants only the photos that are in an Album so we prepare the where condition
        String where = PhotoContract.PhotoEntry.COLUMN_ALBUM_ID + " IS NOT NULL";
        String select[] = {"DISTINCT " + PhotoContract.PhotoEntry.COLUMN_ALBUM_ID};

        // If page and/or albumPerPage = 0, we get all of the albums
        if (page == 0 || albumPerPage == 0) {
            cursor = selectPhotos(db, select, where, null, null, null, null, null);
        } else {
            // We have to get only a range of photos
            int rowsToIgnore = albumPerPage * (page - 1);
            String limit = String.valueOf(rowsToIgnore) + "," + String.valueOf(albumPerPage);
            cursor = selectPhotos(db, select, where, null, null, null, null, limit);
        }

        // If there is no result, we return null and log a message.
        if (cursor.getCount() == 0) {
            Log.i(TAG, "getAllAlbums: There is no album in db.");
            cursor.close();
            return null;
        }
        // We create a new list of albums
        List<Album> allAlbums = new ArrayList<>();

        // We now fetch all the rows in the cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // We create a new Album object and set his id
            Album currentAlbum = new Album();
            currentAlbum.setId(cursor.getInt(
                    cursor.getColumnIndex(PhotoContract.PhotoEntry.COLUMN_ALBUM_ID)));

            // Then we add the album in the list
            allAlbums.add(currentAlbum);

            // We move the cursor to the next line.
            cursor.moveToNext();
        }
        cursor.close();

        // Now that we have all of the albums in a list, we have to get their photos
        for (Album currentAlbum : allAlbums) {
            Log.i(TAG, "getAllAlbums: Loading picture of the album no " + currentAlbum.getId());
            // NOTE: It's not possible to have an Album without photo
            // We create a new list of photos
            List<Photo> allPhotos = new ArrayList<>();

            // We create the condition and launch the request
            where = PhotoContract.PhotoEntry.COLUMN_ALBUM_ID + "=" + currentAlbum.getId();
            cursor = selectPhotos(db, null, where, null, null, null, null, null);

            // We convert the result into a list of Photos
            allPhotos = cursorToPhotos(cursor);
            // And we close the cursor
            cursor.close();

            // We set the photos in the current album
            currentAlbum.setPhotos(allPhotos);
        }
        return allAlbums;
    }

    /**
     * This method is extracting the informations inside of the cursor in parameter to create
     * a list of Photo.class objects.
     *
     * @param cursor containing all the information that we want to extract
     * @return a list of Photo.class objects.
     */
    private List<Photo> cursorToPhotos(Cursor cursor) {

        // We declare a new list that we will send back
        List<Photo> photos = new ArrayList<>();

        // We first have to be sure that there is information in the cursor.
        if (cursor.getCount() == 0) {
            Log.i(TAG, "cursorToPhotos: There is no photos in the cursor.");
            cursor.close();
            return photos;
        }

        // We are going to the first row
        cursor.moveToFirst();
        // We now fetch all the rows in the cursor
        while (!cursor.isAfterLast()) {
            // We create a new photo object.
            Photo currentPhoto = new Photo();

            // We insert all the information of the row in the current photo.
            currentPhoto.setId(
                    cursor.getInt(cursor.getColumnIndex(PhotoContract.PhotoEntry.COLUMN_ID)));
            currentPhoto.setAlbumId(
                    cursor.getInt(cursor.getColumnIndex(PhotoContract.PhotoEntry.COLUMN_ALBUM_ID)));
            currentPhoto.setTitle(
                    cursor.getString(cursor.getColumnIndex(PhotoContract.PhotoEntry.COLUMN_TITLE)));

            // We have to create URL. We surround it with a try catch to be sure that
            // we have valid url.
            try {
                URL urlPhoto = new URL(cursor.getString(cursor.getColumnIndex(
                        PhotoContract.PhotoEntry.COLUMN_URL)));
                currentPhoto.setUrl(urlPhoto);

                URL urlThumbnail = new URL(cursor.getString(cursor.getColumnIndex(
                        PhotoContract.PhotoEntry.COLUMN_THUMBNAIL_URL)));
                currentPhoto.setThumbnailUrl(urlThumbnail);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // We add the new photo in the list.
            photos.add(currentPhoto);

            // We move the cursor to the next line.
            cursor.moveToNext();
        }

        // Now that we have all the photos, we send back the list
        return photos;
    }
}
