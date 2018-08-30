package com.rainbow6.siege.r6_app.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.rainbow6.siege.r6_app.DAO.ConnectDao;
import com.rainbow6.siege.r6_app.DAO.GeneralDao;
import com.rainbow6.siege.r6_app.DAO.PlayerDao;
import com.rainbow6.siege.r6_app.DAO.ProgressionDao;
import com.rainbow6.siege.r6_app.DAO.SkillDao;
import com.rainbow6.siege.r6_app.DAO.StatsDao;
import com.rainbow6.siege.r6_app.DAO.SynchDao;
import com.rainbow6.siege.r6_app.Entities.Connect;
import com.rainbow6.siege.r6_app.Entities.Converters;
import com.rainbow6.siege.r6_app.Entities.General;
import com.rainbow6.siege.r6_app.Entities.Player;
import com.rainbow6.siege.r6_app.Entities.Progression;
import com.rainbow6.siege.r6_app.Entities.Skill;
import com.rainbow6.siege.r6_app.Entities.Stats;
import com.rainbow6.siege.r6_app.Entities.Synch;

import java.util.Date;

@Database(entities = {Connect.class, General.class,Player.class, Progression.class, Skill.class, Stats.class, Synch.class},
        version = 3)
@TypeConverters({Converters.class})
public abstract  class RainbowRoomDatabase extends RoomDatabase {

    public abstract ConnectDao connectDao();
    public abstract GeneralDao generalDao();
    public abstract PlayerDao playerDao();
    public abstract ProgressionDao progressionDao();
    public abstract SkillDao skillDao();
    public abstract StatsDao statsDao();
    public abstract SynchDao synchDao();

    private static RainbowRoomDatabase INSTANCE;


    public static RainbowRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RainbowRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RainbowRoomDatabase.class, "rainbow_database")
                            .build();
//                    .fallbackToDestructiveMigration()
//                    .addMigrations(MIGRATION_2_3)
//                    .addCallback(sRoomDatabaseCallback).build();

                }
            }
        }
        return INSTANCE;
    }

    static void destroyInstance() {
        INSTANCE = null;
    }

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
//        PopulateDbAsync(RainbowRoomDatabase db) {
//            mDao = db.playerDao();
//        }
//
//        @Override
//        protected Void doInBackground(final Void... params) {
//
////             To delete all content and repopulate the database whenever the app is started
////            mDao.delete();
//            Player player = new Player("Hello","1","Test 1", new Date());
//            mDao.insert(player);
//            player = new Player("World","2","Test 2", new Date());
//            mDao.insert(player);
//            return null;
//        }
//    }

}
