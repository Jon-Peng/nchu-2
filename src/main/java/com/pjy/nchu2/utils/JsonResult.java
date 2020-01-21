package com.pjy.nchu2.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public class JsonResult<T> {
    private T data;
    private int code;
    private String message = "";

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonResult() {
        this.code = 200;
        this.message = "success";
        this.data = (T) new ArrayList();
    }

    public JsonResult(T data) {
        this.data = data;
        this.code = 200;
        this.message = "";
    }

    public JsonResult(int code) {
        this.code = code;
        if (code == 200) {
            this.message = "success";
        }

        if (code == 500) {
            this.message = "error";
        }

        if (code == 501) {
            this.message = "server error";
        }
        this.data = (T) new ArrayList();
    }

    public JsonResult(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = (T) new ArrayList();
    }

    public JsonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String toString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", code);
            jsonObject.put("message", message);
            jsonObject.put("data", new JSONObject());

            return jsonObject.toString();
        } catch (Exception e) {
            return null;
        }

    }
}

