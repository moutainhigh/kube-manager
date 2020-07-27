package com.cgm.kube.base;


/**
 * 基础异常实现
 * @author cgm
 */
public abstract class BaseException extends Exception implements IBaseException {
    private static final long serialVersionUID = 1L;
    /**
     * 异常代码
     */
    private String code;
    /**
     * 异常描述的key
     */
    private String descriptionKey;

    /**
     * 参数数组
     */
    private Object[] parameters;

    public BaseException(String code, String descriptionKey, Object... parameters) {
        super(descriptionKey);
        this.code = code;
        this.descriptionKey = descriptionKey;
        this.parameters = parameters;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescriptionKey() {
        return this.descriptionKey;
    }

    @Override
    public Object[] getParameters() {
        return this.parameters;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    @Override
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
