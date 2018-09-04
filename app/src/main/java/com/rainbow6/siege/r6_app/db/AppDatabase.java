package com.rainbow6.siege.r6_app.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.rainbow6.siege.r6_app.db.dao.ConnectionDao;
import com.rainbow6.siege.r6_app.db.dao.PlayerDao;
import com.rainbow6.siege.r6_app.db.dao.ProgressionDao;
import com.rainbow6.siege.r6_app.db.dao.SeasonDao;
import com.rainbow6.siege.r6_app.db.dao.StatsDao;
import com.rainbow6.siege.r6_app.db.dao.SyncDao;
import com.rainbow6.siege.r6_app.db.entity.ConnectionEntity;
import com.rainbow6.siege.r6_app.db.converter.DateConverter;
import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.db.entity.ProgressionEntity;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.db.entity.SyncEntity;

@Database(entities = {ConnectionEntity.class,PlayerEntity.class, ProgressionEntity.class, SeasonEntity.class, StatsEntity.class, SyncEntity.class},
        version = 9)
@TypeConverters({DateConverter.class})
public abstract  class AppDatabase extends RoomDatabase {

    public abstract ConnectionDao connectionDao();
    public abstract PlayerDao playerDao();
    public abstract ProgressionDao progressionDao();
    public abstract SeasonDao seasonDao();
    public abstract StatsDao statsDao();
    public abstract SyncDao syncDao();

    private static AppDatabase INSTANCE;


    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "rainbow_database")
                            .fallbackToDestructiveMigration()
                            .build();
//                    .fallbackToDestructiveMigration()
//                    .addMigrations(MIGRATION_2_3)
//                    .addCallback(sRoomDatabaseCallback).build();

                }
            }
        }
        return INSTANCE;
    }

   /* static void destroyInstance() {
        INSTANCE = null;
    }*/

    /*static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE player "
                    +"ADD COLUMN platformType TEXT");

        }

    };*/


//    To delete all content and repopulate the database whenever the app is started,
//    you create a RoomDatabase.Callback and override onOpen().
//    Because you cannot do Room database operations on the UI thread, onOpen() creates
//    and executes an AsyncTask to add content to the database.
//    private static RoomDatabase.Callback sRoomDatabaseCallback =
//            new RoomDatabase.Callback(){
//
//                @Override
//                public void onOpen (@NonNull SupportSQLiteDatabase db){
//                    super.onOpen(db);
//                    new PopulateDbAsync(INSTANCE).execute();
//                }
//            };
//
//
//    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
//
//        private final PlayerDao mDao;
//
//        PopulateDbAsync(AppDatabase db) {
//            mDao = db.playerDao();
//        }
//
//        @Override
//        protected Void doInBackground(final Void... params) {
//
////             To delete all content and repopulate the database whenever the app is started
////            mDao.delete();
//            PlayerEntity player = new PlayerEntity("Hello","1","Test 1", new Date());
//            mDao.insert(player);
//            player = new PlayerEntity("World","2","Test 2", new Date());
//            mDao.insert(player);
//            return null;
//        }
//    }

}
