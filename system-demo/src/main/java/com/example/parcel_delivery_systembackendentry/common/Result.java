package com.example.parcel_delivery_systembackendentry.common;

import com.example.parcel_delivery_systembackendentry.enumeration.ResultCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Common Response for Frontend")
public class Result<T> {

    @Schema(description = "Respond Code", example = "200")
    private Integer code;

    @Schema(description = "Respond Message", example = "OK")
    private String message;

    @Schema(description = "Respond Data Body", example = "{}")
    private T data;

    public Result() {
    }

    private static <T> Result<T> build(T data) {
        Result<T> result = new Result<>();
        if (data != null)
            result.setData(data);
        return result;
    }

    private static <T> Result<T> build(ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    private static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    public static <T> Result<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> ok() {
        return build(ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> error(T data, ResultCodeEnum resultCodeEnum) {
        return build(data, resultCodeEnum);
    }

    public static <T> Result<T> error(ResultCodeEnum resultCodeEnum) {
        return build(resultCodeEnum);
    }
}
