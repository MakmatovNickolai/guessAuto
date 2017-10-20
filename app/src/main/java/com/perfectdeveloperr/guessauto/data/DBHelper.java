package com.perfectdeveloperr.guessauto.data;



        import android.content.ContentValues;
        import android.content.Context;
        import android.content.res.Resources;
        import android.content.res.XmlResourceParser;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;


        import com.perfectdev.auto.R;

        import java.io.IOException;

        import org.xmlpull.v1.XmlPullParser;
        import org.xmlpull.v1.XmlPullParserException;

public class DBHelper extends SQLiteOpenHelper {

    public final static String LOG_TAG="LOG_TAG";
    private final Context fContext;
    private static final String DATABASE_NAME = "myDB";
    public static final String TABLE_NAME = "mytable";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        fContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY, " + "autoID TEXT, " + "autoDrawableName TEXT, " + "buttons TEXT"
                + ");");

        // Добавляем записи в таблицу
        ContentValues values = new ContentValues();

        // Получим файл из ресурсов
        Resources res = fContext.getResources();

        // Открываем xml-файл

        XmlResourceParser _xml = res.getXml(R.xml.autobase);
        try {
            // Ищем конец документа
            int eventType = _xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Ищем теги record
                if ((eventType == XmlPullParser.START_TAG)
                        && (_xml.getName().equals("record"))) {
                    // Тег Record найден, теперь получим его атрибуты и
                    // вставляем в таблицу
                    String autoID = _xml.getAttributeValue(0);
                    String DrawableName = _xml.getAttributeValue(1);
                    String buttons = _xml.getAttributeValue(2);
                    values.put("autoID", autoID);
                    values.put("autoDrawableName", DrawableName);
                    values.put("buttons", buttons);
                    db.insert(TABLE_NAME, null, values);
                }
                eventType = _xml.next();
            }
        }
        // Catch errors
        catch (XmlPullParserException e) {
            Log.e("Test", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("Test", e.getMessage(), e);

        } finally {
            // Close the xml file
            _xml.close();
        }

    }

    /*    public static final String TABLE_NAME = "mytable";
       public final static String LOG_TAG="myLog";
       private static final int DATABASE_VERSION = 4;


       public DBHelper(Context context) {
           // конструктор суперкласса
           super(context, "myDB", null, DATABASE_VERSION);
       }

       @Override
       public void onCreate(SQLiteDatabase db) {
           Log.d(LOG_TAG, "--- onCreate database ---");
           // создаем таблицу с полями
           db.execSQL("create table " + TABLE_NAME +" ("
                   + "id integer primary key autoincrement,"
                   + "autoID text,"
                   + "auto text" + ");");
       }
   */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == 2){
            Log.d(LOG_TAG, "--- onUpgrade database ---");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            this.onCreate(db);
        }
    }
}
