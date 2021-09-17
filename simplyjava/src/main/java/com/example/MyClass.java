package com.example;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class MyClass {

    public static void main(String[] arg){
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "cp1251"));
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("VM does not support mandatory encoding UTF-8");
        }
        //consoleOut.println(filename);

        String RAnswer="рейс 901, Аеросвіт: сильна турбулентність була відмічена о 13 годині 05 хвилин  за всесвітнім скоординованим часом";
        ArrayList<String> NAnswers = new ArrayList<>();
        NAnswers.add("рейс 901, Аеросвіт: сильна турбулентність прогнозується  о 13 годині 05 хвилин  за всесвітнім скоординованим часом");
        NAnswers.add("рейс 901, Аеросвіт: сильна турбулентність була відмічена о 13 годині 05 хвилин  за київським скоординованим часом");
        NAnswers.add("рейс 901, Аеросвіт: помірна турбулентність була відмічена о 13 годині 05 хвилин  за всесвітнім скоординованим часом");
        ArrayList<int[]> marks = getValueRight(RAnswer, NAnswers);
        //marks.get(0);
        for (int A=0;A<marks.size();A++) {
            //int[] marksis=marks.get(A);
            System.out.println("A:"+marks.get(A)[0]+":"+marks.get(A)[1]);

        }
        getValue(RAnswer,NAnswers.get(0));

    }
    public static ArrayList<HashMap<Integer, Integer>> getValue(String RAnswer, String NegativeAnswer) {

        String[] splittedN;
        String[] splittedR;
        splittedN=NegativeAnswer.split(" ");
        splittedR=RAnswer.split(" ");
        ArrayList<HashMap<Integer,Integer>> NotMatched=new ArrayList<>();
        //HashMap<Integer,Integer> NegativeWord=new HashMap<>();
        for (int i=0;i<splittedN.length;i++){
            //if ((i<splittedR.length)) {
                boolean founded=false;
                for (int i1 = 0; i1 < splittedR.length; i1++) {
                    if (splittedN[i].equals(splittedR[i1])) {
                        if ((i-i1<4)||(i+i1<4)) {
                            founded=true;
                        }
                    }
                }
                if (!founded){
                    //System.out.println("A:"+splittedN[i]);
                    int Start=NegativeAnswer.indexOf(splittedN[i]);
                    int Finish = NegativeAnswer.indexOf(splittedN[i])+splittedN[i].length();
                    HashMap<Integer, Integer> my = new HashMap();
                    my.put(Start,Finish);
                    NotMatched.add(my);
                }
            //}

        }

        return NotMatched;
    }
    private static ArrayList<int[]> getValueRight(String RAnswer, ArrayList<String> NegativeAnswersArray) {

        String[] splittedN;
        String[] splittedR;
        ArrayList<int[]> NotMatched = new ArrayList<>();
        String NegativeAnswer="";
        for (int A=0;A<NegativeAnswersArray.size();A++) {
            if (!NegativeAnswersArray.get(A).equals(RAnswer)) {
                NegativeAnswer = NegativeAnswer + " " + NegativeAnswersArray.get(A);
            }
        }

        splittedN = NegativeAnswer.split(" ");
        splittedR = RAnswer.split(" ");

        //HashMap<Integer,Integer> NegativeWord=new HashMap<>();
        for (int i = 0; i < splittedR.length; i++) {

            boolean founded = false;
            for (int i1 = 0; i1 < splittedN.length; i1++) {
                if (splittedR[i].equals(splittedN[i1])) {
                    //if ((i - i1 < 4) || (i + i1 < 4)) {
                    founded = true;
                    //Log.d(LOG_TAG,"Found:"+splittedR[i]);
                    //}
                }
            }
            if (!founded) {

                System.out.println("A:"+splittedR[i]);
                int Start = RAnswer.indexOf(splittedR[i]);
                int Finish = RAnswer.indexOf(splittedR[i]) + splittedR[i].length();
                //HashMap<Integer, Integer> my = new HashMap();
                //my.put(Start, Finish);
                //System.out.println("A:"+Start+"B"+Finish);
                int StartFinish[]={Start,Finish};
                //StartFinish[Start]
                NotMatched.add(StartFinish);
            }

            //}
        }
        if (NotMatched.size()==0) {
            for (int aArr = 0; aArr < NegativeAnswersArray.size(); aArr++) {
                splittedN = NegativeAnswersArray.get(aArr).split(" ");
                for (int i = 0; i < splittedR.length; i++) {

                    boolean founded = false;
                    for (int i1 = 0; i1 < splittedN.length; i1++) {
                        if (splittedR[i].equals(splittedN[i1])) {
                            founded = true;
                        }
                    }
                    if (!founded) {

                        //System.out.println("A:" + splittedR[i]);
                        int Start = RAnswer.indexOf(splittedR[i]);
                        int Finish = RAnswer.indexOf(splittedR[i]) + splittedR[i].length();
                        //HashMap<Integer, Integer> my = new HashMap();
                        //my.put(Start, Finish);
                        //System.out.println("A:"+Start+"B"+Finish);
                        int StartFinish[] = {Start, Finish};
                        //StartFinish[Start]
                        NotMatched.add(StartFinish);
                    }

                    //}
                }
            }
        }

        return NotMatched;
    }
}
