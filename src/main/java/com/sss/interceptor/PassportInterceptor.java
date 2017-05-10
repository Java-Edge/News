package com.sss.interceptor;

/**
 * Created by Shusheng Shi on 2017/5/10 21:28.
 */

import com.sss.dao.LoginTicketDAO;
import com.sss.dao.UserDAO;
import com.sss.model.HostHolder;
import com.sss.model.LoginTicket;
import com.sss.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 先检验是否为登录用户,再看有无其他权限
 * 此拦截器需注册到mvc中
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 用户数据存在cookie里,则从中读取字段是否有ticket
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

//      即使ticket非空但也依然有可能是伪造的,需要进一步验证
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
//              当做未发生任何有效事件
                return true;
            }
//          将用户记录下来,可被其它层调用查看,否则到了控制器还不知道又得重新验证
            User user = userDAO.selectById(loginTicket.getUserId());
//          确认用户并保存
            hostHolder.setUser(user);

        }



        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        if (modelAndView != null && hostHolder.getUser() != null) {
//          通过拦截器就在此将用户设置进来,保证了所有html页面都可直接访问此用户,非常方便
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
