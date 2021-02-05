package com.youstockit.stubs;

import com.youstockit.services.EmailService;
import com.youstockit.users.User;

public class EmailServiceStub implements EmailService {

    public int numCallsSendEmail = 0;

    public void sendEmail(User user){
        numCallsSendEmail++;
    }
}

