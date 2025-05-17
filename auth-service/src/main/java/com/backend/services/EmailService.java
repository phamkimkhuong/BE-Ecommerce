/*
 * @(#) $(NAME).java    1.0     5/14/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.services;


/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 14-May-2025 8:52 PM
 */
public interface EmailService {
    public void sendMessage(String from,String to, String subject, String text);
}

    