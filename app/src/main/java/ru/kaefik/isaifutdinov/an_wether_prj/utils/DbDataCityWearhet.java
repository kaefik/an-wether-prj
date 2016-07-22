package ru.kaefik.isaifutdinov.an_wether_prj.utils;

//// БД SQLite для сохранения данных о погоде городов
//public class DbDataCityWearher {
//
//
//
//}


//public class DbOpenHelper extends SQLiteOpenHelper {
//
//    private static final int DB_VERSION = 1;
//    private static final String DB_NAME = "test";
//
//    public static final String TABLE_NAME = "users";
//    public static final String LOGIN = "login";
//    public static final String PASSW = "passw";
//    private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( _id integer primary key autoincrement, "
//            + LOGIN + " TEXT, " + PASSW + " TEXT)";
//
//    public DbOpenHelper(Context context) {
//        super(context, DB_NAME, null,DB_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL(CREATE_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//    }
//}

//saveButton.setOnClickListener(new View.OnClickListener() {
//public void onClick(View view) {
//        DbOpenHelper dbOpenHelper = new DbOpenHelper(TestActivity.this);
//        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(DbOpenHelper.LOGIN,loginEditText.getText().toString());
//        cv.put(DbOpenHelper.PASSW,passEditText.getText().toString());
//        db.insert(DbOpenHelper.TABLE_NAME,null,cv);
//        db.close();
//        loginEditText.setText("");
//        passEditText.setText("");
//        }
//        });
//        }