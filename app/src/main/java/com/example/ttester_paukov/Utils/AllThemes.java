package com.example.ttester_paukov.Utils;

/**
 * Created by sp_1 on 30.03.2017.
 */
public class AllThemes{
    private int ID;
    private int TCount;
    private String THEME;
    private int ThemeRowCount;
    public AllThemes(){
        ID=0;
        THEME="";
    }
    public AllThemes(String theme){
        ID=0;
        THEME=theme;
    }
    public void setTHEME(String THEMe) {
        THEME = THEMe;
    }
    public void setTHEME_questions_count(int setCount){setCount = TCount;}
    public int getTHEME_questions_count(){return TCount;}
    public void setTHEME_id(int THEMeid) {ID = THEMeid;}
    public void setRowCount(int RowCount) {ThemeRowCount = RowCount;}
    public int getRowCount(){return ThemeRowCount;}
    public String getTHEME() {
        return THEME;
    }
    public int getTHEME_id() {return ID;}
}