package com.example.multichoice_app.dbFlow;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

@Table(name = "type_data",database = MyDataBase.class)
public class TypeDataSave {

    @PrimaryKey
    String type;

    @Column
    String dataJson;

    public TypeDataSave(String type, String dataJson) {
        this.type = type;
        this.dataJson = dataJson;
    }

    public TypeDataSave() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }
}
