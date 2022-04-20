package com.rxl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ren.xiaolong
 * @date 2022/4/18
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_user_role")
public class UserRoleEntity {
    Integer id;
    Integer userid;
    Integer roleid;
}
