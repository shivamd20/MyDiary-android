package in.ssec.myDiary.myDiary.data;
import android.provider.BaseColumns;
/**
 * Created by shiva on 21-12-2016.
 */

public final class DiaryContract {


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DiaryContract() {}

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class User implements BaseColumns {

        /**
         * Name of database table for pets
         */
        public final static String TABLE_NAME = "user";
        public final static String USER_NAME = "name";
        public final static String USER_PASSWORD = "password";
    }
    public static final class Notes implements BaseColumns {

        /**
         * Name of database table for pets
         */
        public final static String _id=BaseColumns._ID;
        public final static String TABLE_NAME = "note";
        public final static String HEAD = "head";
        public final static String NOTE= "note";
        public final static String DATE= "date";
        public final static String IMAGE="image";

    }

}