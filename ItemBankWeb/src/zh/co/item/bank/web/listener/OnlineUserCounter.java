package zh.co.item.bank.web.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import zh.co.common.exception.MessageId;
import zh.co.common.log.CmnLogger;
import zh.co.item.bank.web.filter.AccessFilter;

@WebListener
public class OnlineUserCounter implements HttpSessionListener {
	
    // Logger
    private CmnLogger logger = CmnLogger.getLogger(AccessFilter.class);
    
    private static int counter;
    
    public static  int getCounter()
    {
        return counter;        
    }
    
    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        OnlineUserCounter.counter++;
        //在线人数更新
        logger.log(MessageId.COMMON_I_0004, new Object[] {counter} );
    }
    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
        OnlineUserCounter.counter--;
         //在线人数更新
        logger.log(MessageId.COMMON_I_0004, new Object[] {counter} );
    }
}
