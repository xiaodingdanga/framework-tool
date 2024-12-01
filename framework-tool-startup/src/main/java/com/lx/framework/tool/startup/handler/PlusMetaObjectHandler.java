package com.lx.framework.tool.startup.handler;


import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author xin.liu
 * @description 自动填充说明  注意！这里需要entity字段标记为填充字段
 * @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
 * @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
 * @date 2023-05-18  14:57
 * @Version 1.0
 */
@Slf4j
@Component
public class PlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * @param metaObject 元数据对象
     * @description 插入时填充指定字段
     * @return: void
     * @author xin.liu
     * @date 2022/12/1 11:26
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updatedTime", LocalDateTime::now, LocalDateTime.class);
//        // 自动填充UUID strictInsertFill有值不覆盖
//        this.strictInsertFill(metaObject, "uuid", () -> UUID.fastUUID().toString(), String.class);
//        // 逻辑删除 自动填充
//        this.strictInsertFill(metaObject, "deleted", () -> 0, Integer.class);
    }

    /**
     * @param metaObject 元数据对象
     * @description 更新时填充指定字段
     * @author xin.liu
     * @date 2022/12/1 11:26
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updatedTime", LocalDateTime::now, LocalDateTime.class);
        this.strictUpdateFill(metaObject,"updatedBy",Long.class, Long.valueOf(123));


    }

}
