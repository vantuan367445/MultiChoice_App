package com.example.multichoice_app.dbFlow;


import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

@Database(name = MyDataBase.NAME, version = MyDataBase.VERSION)
public class MyDataBase {
    public static final String NAME = "MyDataBase";
    public static final int VERSION = 1;

    public static void saveDataType(TypeDataSave dataSave){
        FlowManager.getModelAdapter(TypeDataSave.class).save(dataSave);
    }

    public static String loadDataType(String type){
        TypeDataSave dataLoad = SQLite.select().from(TypeDataSave.class).where(TypeDataSave_Table.type.eq(type))
                .querySingle();
        if(dataLoad != null)
            return dataLoad.getDataJson();
        return "";
    }
}
