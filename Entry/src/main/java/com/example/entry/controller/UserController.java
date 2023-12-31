package com.example.entry.controller;

import com.example.entry.common.Result;
import com.example.entry.dto.Email;
import com.example.entry.enumeration.ResultCodeEnum;
import com.example.entry.dto.LoginData;
import com.example.entry.dto.RegisterData;
import com.example.entry.entity.User;
import com.example.entry.service.UserService;
import com.example.entry.utils.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@Tag(name = "User", description = "User data controller")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Result.class))})
    @Operation(summary = "Send a register email", description = "Allowed user send a verification code in email during registration.")
    @SecurityRequirements()
    @PostMapping("/sendRegisterEmail")
    public Result<Object> sendRegisterEmail(@Parameter(description = "Account Email Address") @RequestParam("email") String emailAddress,
                                            @Parameter(description = "Account User Type") @RequestParam("type") int type) {

        // 1. check email format
        if(UserUtils.validateEmail(emailAddress)) {

            // 2. check email registered
            if (userService.getUserByEmail(emailAddress, type) == null) {

                // 3. check that no emails have been sent within a minute
                String check_1_minute = stringRedisTemplate.opsForValue().get(emailAddress + "|check_1_minute");
                if(check_1_minute == null) {

                    // a. generate verification code
                    String verificationCode = VerificationUtils.generateVerificationCode();

                    // b. send email
                    Email email = new Email(emailAddress, "Register Verification Code",
                            getRegisterVerificationEmailBody(verificationCode));
                    RestTemplate template = new RestTemplate();
                    try {
                        template.postForEntity("https://mail.ucdparcel.ie/send", EmailEncryptor.encrypt(email), String.class);
                    } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                             IllegalBlockSizeException | BadPaddingException e) {
                        log.error("Problem on encrypt email: " + e.getMessage());
                        return Result.error(ResultCodeEnum.FAIL);
                    }

                    // c. record verification code
                    stringRedisTemplate.opsForValue().set(emailAddress, verificationCode);
                    stringRedisTemplate.expire(emailAddress, 5, TimeUnit.MINUTES);

                    // d. record 1 minute limitation
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = currentDateTime.format(formatter);
                    stringRedisTemplate.opsForValue().set(emailAddress + "|check_1_minute", "send at " + formattedDateTime);
                    stringRedisTemplate.expire(emailAddress + "|check_1_minute", 1, TimeUnit.MINUTES);

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

    @ApiResponse(responseCode = "200", description = "Success",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Result.class))})
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

        // 1. check username valid
        if (UserUtils.validateUsername(username)) {

            // 2. check password length
            if (UserUtils.validatePassword(pwd)) {

                // 3. check password matches
                if (pwd.equals(pwd2)) {

                    // 4. check email registered
                    if (userService.getUserByEmail(email, type) == null) {

                        String verificationCode = stringRedisTemplate.opsForValue().get(email);

                        // 5. check email verification code send before
                        if(verificationCode != null) {

                            // 6. check verification code correct
                            if (!verificationCode.isEmpty() && code.equals(verificationCode)) {
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

    private static String getRegisterVerificationEmailBody(String code) {
        return "<p>Welcome, this is your verification code: </p>" +
                "<p style='text-align:center; font-weight: bold;'>" + code + "</p>" +
                "<p>The verification code has 5 minutes expiration. Please verify as soon as possible.</p>";
    }

}
