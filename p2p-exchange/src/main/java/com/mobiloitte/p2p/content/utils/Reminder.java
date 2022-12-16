package com.mobiloitte.p2p.content.utils;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.mobiloitte.p2p.content.dao.TradingDao;

public class Reminder {
	
    Timer timer;
    @Autowired
    TradingDao tradingDao;
    

    public  Reminder(int seconds) {
    	
        timer = new Timer();
         timer.schedule(new RemindTask(), seconds*1000);
	}

    class RemindTask extends TimerTask {
        public void run() {
            System.out.println("Time's up!");
            timer.cancel(); //Terminate the timer thread
        }
    }
}

