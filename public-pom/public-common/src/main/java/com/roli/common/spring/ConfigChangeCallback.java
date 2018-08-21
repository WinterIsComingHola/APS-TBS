package com.roli.common.spring;

/**
 *  当配置有变更时，期望回调。可实现此接口。 datasource实现类需要实现此接口且autoReload=true 方能自动刷新
 * 例如：动态变更连接池的url。那么应该在此自行重建连接池
 *
 * @author xuxinyu
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @date 2018/2/5 23:47
 */
public interface ConfigChangeCallback {

    /**
     * 刷新后置事件
     */
    void refreshAfter();

    /**
     * 刷新前置事件
     */
    void refreshBefore();

    /**
     * 有属性变更时
     * @warn 这个不是精确判断，存在误判。 如属性未变更被判断为变更
     */
    void changed();

    /**
     * 是否刷新
     */
    boolean autoReload = false;

}
