package com.tripleying.dogend.mailbox.module.cdkeymail.placeholder;

import com.tripleying.dogend.mailbox.module.cdkeymail.CDKeyMail;
import com.tripleying.dogend.mailbox.module.mailplaceholder.MailExpansion;

public class CDKeyPlaceHolder {
    
    public static void registerPlaceHolder(){
        MailExpansion me = MailExpansion.getMailExpansion();
        me.registerPlayerPlaceholder("player_cdkey_input_day", p -> 
            Integer.toString(CDKeyMail.getMain().getPlayerInputDay(p))
        );
        me.registerServerPlaceholder("server_cdkey_input_every_day", () -> 
            Integer.toString(CDKeyMail.getMain().getInput_every_day())
        );
        me.registerServerPlaceholder("server_cdkey_input_every_times", () -> 
            Long.toString(CDKeyMail.getMain().getInput_every_times())
        );
    }
    
    public static void unregisterPlaceHolder(){
        MailExpansion me = MailExpansion.getMailExpansion();
        me.unregisterPlayerPlaceholder("player_cdkey_input_day");
        me.unregisterServerPlaceholder("server_cdkey_input_every_day");
        me.unregisterServerPlaceholder("server_cdkey_input_every_times");
    }
    
}
