package com.Toffee.Wallet.utils;

import static com.Toffee.Wallet.utils.Const.QUIZ_TIME_LEFT;
import static com.Toffee.Wallet.utils.Const.SCRATCH_TIME_LEFT;
import android.app.Activity;
import android.os.CountDownTimer;

public class Cnt {
    public static CountDownTimer spinCnt;
    public static CountDownTimer scratchCnt;
    public static CountDownTimer quizCnt;


    public static void stopTimer(Activity activity, String type){
        try {
            Pref pref=new Pref(activity);
            switch (type){
                case "spin":
                    if(spinCnt!=null && pref.getInt(pref.SPIN_COUNTS)>0){
                        spinCnt.cancel();
                        spinCnt=null;
                        int cnttime=pref.getInt(pref.SPIN_COUNTS);
                        if(cnttime>=Const.SPIN_TIME_LEFT){
                            pref.setIntData(pref.SPIN_COUNTS,(cnttime-Const.SPIN_TIME_LEFT));
                        }else{
                            pref.setIntData(pref.SPIN_COUNTS,0);
                        }
                    }
                    break;

                case "scratch":
                    if(scratchCnt!=null && pref.getInt(pref.SCRATCH_COUNTS)>0){
                        scratchCnt.cancel();
                        scratchCnt=null;
                        int cnttime=pref.getInt(pref.SCRATCH_COUNTS);
                        if(cnttime>=SCRATCH_TIME_LEFT){
                            pref.setIntData(pref.SCRATCH_COUNTS,(cnttime-SCRATCH_TIME_LEFT));
                        }else{
                            pref.setIntData(pref.SCRATCH_COUNTS,0);
                        }
                    }
                    break;

                case "quiz":
                    if(quizCnt!=null && pref.getInt(pref.Quiz_COUNTS)>0){
                        quizCnt.cancel();
                        quizCnt=null;
                        int cnttime=pref.getInt(pref.Quiz_COUNTS);
                        if(cnttime>=QUIZ_TIME_LEFT){
                            pref.setIntData(pref.Quiz_COUNTS,(cnttime-QUIZ_TIME_LEFT));
                        }else{
                            pref.setIntData(pref.Quiz_COUNTS,0);
                        }
                    }
            }
        }catch (Exception e){
            System.out.println("spin_func___step4"+e.getMessage());
        }
    }

    public static void runTimer(Activity activity, String type) {
        try {
            Pref pref=new Pref(activity);
            switch (type){
                case "spin":
                    if(pref.getInt(pref.SPIN_COUNTS)>0) {
                        spinCnt = new CountDownTimer(pref.getInt(pref.SPIN_COUNTS) * 1000L, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Const.SPIN_TIME_LEFT = pref.getInt(pref.SPIN_COUNTS) - (int) (millisUntilFinished / 1000);
                                System.out.println("spin_backrunning__" + (millisUntilFinished / 1000));
                            }

                            @Override
                            public void onFinish() {
                                System.out.println("spin_backrunning__finish");
                                spinCnt = null;
                                pref.setIntData(pref.SPIN_COUNTS, 0);
                            }
                        }.start();
                    }
                    break;
                case "scratch":
                    if(pref.getInt(pref.SCRATCH_COUNTS)>0) {
                        scratchCnt = new CountDownTimer(pref.getInt(pref.SCRATCH_COUNTS) * 1000L, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                SCRATCH_TIME_LEFT = pref.getInt(pref.SCRATCH_COUNTS) - (int) (millisUntilFinished / 1000);
                                System.out.println("scrat_backrunning__" + (millisUntilFinished / 1000));
                            }

                            @Override
                            public void onFinish() {
                                System.out.println("scratch_backrunning__finish");
                                scratchCnt = null;
                                pref.setIntData(pref.SCRATCH_COUNTS, 0);
                            }
                        }.start();
                    }
                    break;

                case "quiz":
                    if(pref.getInt(pref.Quiz_COUNTS)>0) {
                        quizCnt = new CountDownTimer(pref.getInt(pref.Quiz_COUNTS) * 1000L, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                QUIZ_TIME_LEFT = pref.getInt(pref.Quiz_COUNTS) - (int) (millisUntilFinished / 1000);
                                System.out.println("quiz_backrunning__" + (millisUntilFinished / 1000));
                            }

                            @Override
                            public void onFinish() {
                                System.out.println("quiz_backrunning__finish");
                                quizCnt = null;
                                pref.setIntData(pref.Quiz_COUNTS, 0);
                            }
                        }.start();
                    }
                    break;
            }

        }catch (Exception e){

        }
    }

}
