package com.cgm.kube.base;

/**
 * 基础异常接口
 * @author cgm
 */
public interface IBaseException {
    /**
     * 获取异常代码
     *
     * @return 异常代码
     */
    String getCode();

    /**
     * 获取异常描述的key
     *
     * @return 异常描述的key
     */
    String getDescriptionKey();

    /**
     * 获取参数数组
     *
     * @return 参数数组
     */
    Object[] getParameters();

    /**
     * 设置异常代码
     *
     * @param code 异常代码
     */
    void setCode(String code);

    /**
     * 设置异常描述的key
     *
     * @param descriptionKey 异常描述的key
     */
    void setDescriptionKey(String descriptionKey);

    /**
     * 设置参数数组
     *
     * @param parameters 参数数组
     */
    void setParameters(Object[] parameters);
}
