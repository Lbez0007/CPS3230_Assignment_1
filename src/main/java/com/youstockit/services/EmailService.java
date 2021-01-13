package com.youstockit.services;

import com.youstockit.users.Manager;
import com.youstockit.users.User;

public interface EmailService {
    public int numCallsSendEmail = 0;

    public void sendEmail(User user);
}
