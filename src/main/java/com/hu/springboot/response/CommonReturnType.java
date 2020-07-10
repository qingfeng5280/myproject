package com.hu.springboot.response;

public class CommonReturnType {

    // success, fail
    private String status;

    // status = success, data 内返回前端需要的 json数据
    // status = fail, data 内使用通用的错误码格式
    private Object data;

    public static com.hu.springboot.response.CommonReturnType create(Object result){

        return create(result, "success");
    }

    public static com.hu.springboot.response.CommonReturnType create(Object result, String status){

        com.hu.springboot.response.CommonReturnType type = new com.hu.springboot.response.CommonReturnType();
        type.setStatus(status);
        type.setData(result);

        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
