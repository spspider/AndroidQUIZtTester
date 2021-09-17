package com.example;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by tanja on 08.05.2017.
 */

public class MyClass2 {

    public static void main(String[] arg) {
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "cp1251"));
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("VM does not support mandatory encoding UTF-8");
        }
        System.out.println("Works");
        System.out.println("Works".substring(2,3));
        getSimiliarQuestion();
    }

    public static void getSimiliarQuestion() {
       String Q1="Хто надає дозвіл на провадження діяльності у повітряному просторі, яка потребує встановлення короткочасних обмежень його використання в межах одного району польотної інформації тривалістю понад 2 год.;";
       String Q2="Хто надає дозвіл на провадження діяльності у повітряному просторі, яка потребує встановлення короткочасних обмежень його використання тривалістю не більше ніж 2 год.;";
        String[] QOrig_split;
        String[] QComp_split;
        QOrig_split = Q1.split(" ");
        QComp_split = Q2.split(" ");

        int similiar=0;
        for (int i=0;i<QOrig_split.length;i++){
            //if ((i<QComp_split.length)) {
            boolean founded=false;
            for (int i1 = 0; i1 < QComp_split.length; i1++) {
                if (QOrig_split[i].equals(QComp_split[i1])) {
                    if ((i-i1<4)||(i+i1<4)) {
                        founded=true;
                        similiar++;

                    }
                }
            }
            if (!founded){
                int Start=Q1.indexOf(QOrig_split[i]);
                int Finish = Q1.indexOf(QOrig_split[i])+QOrig_split[i].length();
                System.out.println("A:"+QOrig_split[i]+Start+" "+Finish);
            }
            //}

        }
        float simi= (float) (similiar*100.0/QOrig_split.length*1.00);
        System.out.println("A:"+simi);
    }
}
