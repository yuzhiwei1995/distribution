package com.i2pbridge.distribution.common;

import lombok.Data;

@Data
public class R<T> {

//    @ApiModelProperty(value = "是否成功")
    private Boolean success;

//    @ApiModelProperty(value = "返回码")
    private Integer code;

//    @ApiModelProperty(value = "返回消息")
    private String msg;

//    @ApiModelProperty(value = "返回数据")
    private T data;

    public void setCode(Integer code) {
        this.code = code;
    }

    //把构造方法私有
    private R() {}

    //成功静态方法
    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMsg("成功");
        return r;
    }

    //失败静态方法
    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMsg("失败");
        return r;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMsg(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R data(T t){
        this.setData(t);
        return this;
    }
}

