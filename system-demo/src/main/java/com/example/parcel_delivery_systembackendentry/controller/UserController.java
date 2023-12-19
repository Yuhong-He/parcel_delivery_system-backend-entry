package com.example.parcel_delivery_systembackendentry.controller;

import com.example.parcel_delivery_systembackendentry.common.Result;
import com.example.parcel_delivery_systembackendentry.enumeration.ResultCodeEnum;
import com.example.parcel_delivery_systembackendentry.dto.LoginData;
import com.example.parcel_delivery_systembackendentry.dto.RegisterData;
import com.example.parcel_delivery_systembackendentry.entity.EmailTimer;
import com.example.parcel_delivery_systembackendentry.entity.EmailVerification;
import com.example.parcel_delivery_systembackendentry.entity.User;
import com.example.parcel_delivery_systembackendentry.service.EmailTimerService;
import com.example.parcel_delivery_systembackendentry.service.EmailVerificationService;
import com.example.parcel_delivery_systembackendentry.service.UserService;
import com.example.parcel_delivery_systembackendentry.utils.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@Tag(name = "User", description = "User data controller")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailTimerService emailTimerService;

    @Autowired
    private EmailVerificationService evService;

    @Operation(summary = "Send a register email", description = "Allowed user send a verification code in email during registration.")
    @SecurityRequirements()
    @PostMapping("/sendRegisterEmail")
    public Result<Object> sendRegisterEmail(@Parameter(description = "Account Email Address") @RequestParam("email") String email,
                                            @Parameter(description = "Account User Type") @RequestParam("type") int type) {

        // 1. check email format
        if(UserUtils.validateEmail(email)) {

            // 2. check email registered
            if (userService.getUserByEmail(email, type) == null) {

                // 3. check that no emails have been sent within a minute
                EmailTimer oldEmailTimer = emailTimerService.read(email, "verificationCode");
                if(oldEmailTimer == null || !EmailTimerUtils.repeatEmail(oldEmailTimer)) {

                    // a. generate verification code
                    EmailVerification emailVerification = EmailVerificationUtils.createVerification(email);

                    // b. send email (may throw exception)
                    DirectMailUtils.sendEmail(
                            email, "Register Verification Code",
                            EmailContentHelper.getRegisterVerificationEmailBody(emailVerification.getVerificationCode()));

                    // c. record verification code
                    evService.create(emailVerification);

                    // d. record the action in email timer
                    EmailTimer newEmailTimer = new EmailTimer(email, "verificationCode");
                    emailTimerService.create(newEmailTimer);

                    return Result.ok();
                } else {
                    return Result.error(ResultCodeEnum.LESS_THAN_ONE_MINUTE);
                }
            } else {
                return Result.error(ResultCodeEnum.EMAIL_ALREADY_REGISTERED);
            }
        } else {
            return Result.error(ResultCodeEnum.INVALID_EMAIL);
        }
    }

    @Operation(summary = "User register", description = "Register user, all types allowed.")
    @SecurityRequirements()
    @PostMapping("/register")
    public Result<Object> register(@RequestBody RegisterData data) {

        String email = data.getEmail();
        String username = data.getUsername();
        String pwd = data.getPassword();
        String pwd2 = data.getPassword2();
        int type = data.getType();
        String code = data.getCode();

        EmailVerification ev = evService.read(email);

        // 1. check username valid
        if (UserUtils.validateUsername(username)) {

            // 2. check password length
            if (UserUtils.validatePassword(pwd)) {

                // 3. check password matches
                if (pwd.equals(pwd2)) {

                    // 4. check email registered
                    if (userService.getUserByEmail(email, type) == null) {

                        // 5. check email verification code send before
                        if(ev != null) {

                            // 6. check verification code expiration
                            if (!EmailVerificationUtils.isExpiration(ev)) {

                                // 7. check verification code correct
                                if (code.equals(ev.getVerificationCode())) {
                                    User user = new User(username, pwd, email, type);
                                    userService.register(user);
                                    return Result.ok();
                                } else {
                                    return Result.error(ResultCodeEnum.VERIFY_CODE_INCORRECT);
                                }
                            } else {
                                return Result.error(ResultCodeEnum.VERIFY_CODE_EXPIRED);
                            }
                        } else {
                            return Result.error(ResultCodeEnum.NO_VERIFICATION_CODE);
                        }
                    } else {
                        return Result.error(ResultCodeEnum.EMAIL_ALREADY_REGISTERED);
                    }
                } else {
                    return Result.error(ResultCodeEnum.PASSWORD_NOT_MATCH);
                }
            } else {
                return Result.error(ResultCodeEnum.PASSWORD_LENGTH);
            }
        } else {
            return Result.error(ResultCodeEnum.USERNAME_LENGTH);
        }
    }

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class))})
    @Operation(summary = "User login", description = "User login.")
    @SecurityRequirements()
    @PostMapping("/login")
    public Result<Object> login(@RequestBody LoginData data) {

        String email = data.getEmail();
        String password = data.getPassword();
        int type = data.getType();

        // 1. Check user exist
        User u = userService.getUserByEmail(email, type);
        if(u != null) {

            // 2. check password
            User user = userService.login(email, password, type);
            if(user != null) {

                Map<String, Object> map = new LinkedHashMap<>();
                map.put("access_token", JwtHelper.createAccessToken(user.getId().longValue()));
                map.put("expired_time", JwtHelper.getExpiredTime());
                map.put("username", user.getUsername());
                map.put("type", user.getType());

                return Result.ok(map);

            } else {
                return Result.error(ResultCodeEnum.PASSWORD_INCORRECT);
            }
        } else {
            return Result.error(ResultCodeEnum.EMAIL_NOT_REGISTERED);
        }
    }

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class))})
    @Operation(summary = "Search student by name", description = "When Estate Service Staff register a parcel, search student in this endpoint and select one to fill the parcel information.")
    @GetMapping("/searchStudentByName")
    public Result<Object> searchStudentByName(@Parameter(description = "Student Name") @RequestParam("searchTxt") String searchTxt) {
        if(!searchTxt.isEmpty()) {
            return Result.ok(userService.getStudentsBySearchName(searchTxt));
        } else {
            return Result.error(ResultCodeEnum.EMPTY_SEARCH);
        }
    }

}
