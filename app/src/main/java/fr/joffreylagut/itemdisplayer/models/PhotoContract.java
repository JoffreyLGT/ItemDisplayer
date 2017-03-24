package fr.joffreylagut.itemdisplayer.models;

/**
 * PhotoContract.java
 * Purpose: Container for the constants defining all the Database information
 *
 * @author Joffrey LAGUT
 * @version 1.0 2017-03-24
 */

public final class PhotoContract {

    /**
     * Empty constructor to prevent someone from accidentally instantiating the contract class
     */
    public PhotoContract() {
    }

    /**
     * Class that specify the layout of the Photo table.
     */
    public static abstract class PhotoEntry {
        // Table name
        public static final String TABLE_NAME = "PHOTOS";
        // Columns
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ALBUM_ID = "albumId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_URL = "URL";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnailUrl";

    }
}
