package com.example.ttester_paukov.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class Question {
	private int ID;
	private String QUESTION;
	private String OPT1;
	private String OPT2;
	private String OPT3;
	private String OPT4;
	private int ANSWER;
	private int KEY_THEME;
	private ArrayList<String> NANSWERS;
	private String NALL_ANSWERS[][];
	public Question()
	{
		ID=0;
		KEY_THEME=0;
		QUESTION="";
		OPT1="";
		OPT2="";
		OPT3="";
		OPT4="";
		ANSWER=0;
		NANSWERS=new ArrayList<String>();
	}
	public Question(int THEME_id,String qUESTION, String oPT1, String oPT2, String oPT3,String oPT4,
					int aNSWER, ArrayList<String> NAnswers) {
		KEY_THEME = THEME_id;
		QUESTION = qUESTION;
		OPT1 = oPT1;
		OPT2 = oPT2;
		OPT3 = oPT3;
		OPT4 = oPT4;
		ANSWER = aNSWER;
		NANSWERS=NAnswers;
	}



    public int getID()
	{
		return ID;
	}
	public String getQUESTION() {
		return QUESTION;
	}
	public String getOPT1() {
		return OPT1;
	}
	public String getOPT2() {
		return OPT2;
	}
	public String getOPT3() {
		return OPT3;
	}
	public String getOPT4() {
		return OPT4;
	}
	public int getTHEMEID() {
		return KEY_THEME;
	}
	public int getANSWER() {
		return ANSWER;
	}
	public ArrayList<String> getANSWERS() {return NANSWERS;}
	public void setID(int id)
	{
		ID=id;
	}
	public void setQUESTION(String qUESTION) {
		QUESTION = qUESTION;
	}
	public void setOPT1(String oPT1) {
		OPT1 = oPT1;
	}
	public void setOPT2(String oPT2) {
		OPT2 = oPT2;
	}
	public void setOPT3(String oPT3) {OPT3 = oPT3;}
	public void setOPT4(String oPT4) {OPT4 = oPT4;}
	public void setTHEMEID(int THEME_id) {KEY_THEME = THEME_id;}
	public void setANSWER(int aNSWER) {
		ANSWER = aNSWER;
	}
	public  void setANSWERS(ArrayList<String> nAnswers){NANSWERS=nAnswers;}
}
