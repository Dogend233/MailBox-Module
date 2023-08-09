package com.tripleying.dogend.module.playermail.template;

import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.module.MailBoxModule;
import com.tripleying.dogend.mailbox.module.templatemail.TemplateMail;
import com.tripleying.dogend.mailbox.module.templatemail.mail.TemplateSystemMail;
import com.tripleying.dogend.module.playermail.PlayerMail;
import com.tripleying.dogend.module.singleplayermailapi.SinglePlayerMailAPI;
import java.util.List;

public class TemplatePlayerMail {
    
    private final TemplateMail tm;
    
    private TemplatePlayerMail(TemplateMail tm){
        this.tm = tm;
    }
    
    public boolean sendMail(long id, String player){
        TemplateSystemMail tsm = tm.getTemplateMail(id);
        if(tm!=null){
            tsm.setId(0);
            PersonMail pm = tsm.getPersonMail(PlayerMail.admin_type);
            List<String> list = SinglePlayerMailAPI.send2Player(pm, player);
            if(!list.isEmpty() && list.get(0).equals(player)){
                return true;
            }
        }
        return false;
    }
    
    public static TemplatePlayerMail newTemplatePlayerMail(MailBoxModule mbm){
        if(mbm instanceof TemplateMail){
            return new TemplatePlayerMail((TemplateMail)mbm);
        }else{
            return null;
        }
    }
    
}
