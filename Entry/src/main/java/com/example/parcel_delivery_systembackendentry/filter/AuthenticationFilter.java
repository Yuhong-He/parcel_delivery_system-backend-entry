package com.example.parcel_delivery_systembackendentry.filter;

import com.alibaba.fastjson2.JSON;
import com.example.parcel_delivery_systembackendentry.common.BaseContext;
import com.example.parcel_delivery_systembackendentry.common.Result;
import com.example.parcel_delivery_systembackendentry.enumeration.ResultCodeEnum;
import com.example.parcel_delivery_systembackendentry.entity.User;
import com.example.parcel_delivery_systembackendentry.enumeration.UserTypeEnum;
import com.example.parcel_delivery_systembackendentry.service.UserService;
import com.example.parcel_delivery_systembackendentry.utils.JwtHelper;
import com.example.parcel_delivery_systembackendentry.utils.PermissionHelper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = "/*")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter implements Filter {

    @Autowired
    private UserService userService;

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. Get request url and permission path
        String requestURI = request.getRequestURI();

        String[] unregisteredRights = PermissionHelper.unregistered();
        String[] anyRegisteredRights = PermissionHelper.anyRegistered();
        String[] studentRights = PermissionHelper.student();
        String[] postmanRights = PermissionHelper.postman();
        String[] mervilleStaffRights = PermissionHelper.mervilleStaff();
        String[] estateServiceStaffRights = PermissionHelper.estateServiceStaff();

        // 2. If the request is for unregistered user
        if(match(unregisteredRights, requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Check the request belonging to which user rights group
        String rights = "";
        if(match(anyRegisteredRights, requestURI)) {
            rights = "any user";
        } else if(match(studentRights, requestURI)) {
            rights = "Student";
        } else if(match(postmanRights, requestURI)) {
            rights = "Postman";
        } else if(match(mervilleStaffRights, requestURI)) {
            rights = "Merville Staff";
        } else if(match(estateServiceStaffRights, requestURI)) {
            rights = "Estate Service Staff";
        }

        if ("OPTIONS".equals(request.getMethod())) {
            filterChain.doFilter(request, servletResponse);
        } else {
            String access_token = request.getHeader("AccessToken");

            // a. check access_token exist
            if(access_token != null && !access_token.isEmpty()) {

                // b. check access_token expired
                if(JwtHelper.notExpired(access_token)) {

                    // c. check access_token valid
                    Long userId = JwtHelper.getUserId(access_token);

                    // d. check userId exist (valid access_token should contain a valid userid)
                    if(userId != null) {
                        Integer userIdInt = Math.toIntExact(userId);
                        User user = userService.getUserById(userIdInt);

                        // e. check user exist
                        if(user != null) {

                            // f. check user rights
                            switch (rights) {
                                case "any user":
                                    BaseContext.setCurrentId(userId);
                                    filterChain.doFilter(request, response);
                                    break;
                                case "Student":
                                    if (user.getType() == UserTypeEnum.Student.getVal()) {
                                        BaseContext.setCurrentId(userId);
                                        filterChain.doFilter(request, response);
                                    } else {
                                        action(response, Result.error(ResultCodeEnum.NO_PERMISSION));
                                    }
                                    break;
                                case "Postman":
                                    if (user.getType() == UserTypeEnum.Postman.getVal()) {
                                        BaseContext.setCurrentId(userId);
                                        filterChain.doFilter(request, response);
                                    } else {
                                        action(response, Result.error(ResultCodeEnum.NO_PERMISSION));
                                    }
                                    break;
                                case "Merville Staff":
                                    if (user.getType() == UserTypeEnum.MervilleStaff.getVal()) {
                                        BaseContext.setCurrentId(userId);
                                        filterChain.doFilter(request, response);
                                    } else {
                                        action(response, Result.error(ResultCodeEnum.NO_PERMISSION));
                                    }
                                    break;
                                case "Estate Service Staff":
                                    if (user.getType() == UserTypeEnum.EstateServiceStaff.getVal()) {
                                        BaseContext.setCurrentId(userId);
                                        filterChain.doFilter(request, response);
                                    } else {
                                        action(response, Result.error(ResultCodeEnum.NO_PERMISSION));
                                    }
                                    break;
                                default:
                                    action(response, Result.error(ResultCodeEnum.NOT_REGISTERED_API));
                                    break;
                            }
                        } else {
                            action(response, Result.error(ResultCodeEnum.NO_USER));
                        }
                    } else {
                        action(response, Result.error(ResultCodeEnum.TOKEN_ERROR));
                    }
                } else {
                    action(response, Result.error(ResultCodeEnum.TOKEN_EXPIRED));
                }
            } else {
                action(response, Result.error(ResultCodeEnum.MISS_TOKEN));
            }
        }
    }

    public boolean match(String[] urls, String requestURI) {
        for (String url : urls) {
            if(PATH_MATCHER.match(url, requestURI)){
                return true;
            }
        }
        return false;
    }

    public void action(HttpServletResponse response, Result<Object> result) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "token, Content-Type");
        response.setStatus(200);
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        String resultJson = JSON.toJSON(result).toString();
        OutputStream out = response.getOutputStream();
        out.write(resultJson.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }
}
