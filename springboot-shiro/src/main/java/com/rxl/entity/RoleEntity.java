package com.rxl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author ren.xiaolong
 * @date 2022/4/18
 * @Description
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_role")
public class RoleEntity {

    @TableId
    private String id;
    private String name;
    //定义权限的集合

    @TableField(exist = false)
    private List<PermsEntity> perms;
}
