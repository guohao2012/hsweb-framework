package org.hswebframework.web.entity.authorization;

import org.hswebframework.web.commons.entity.GenericEntity;
import org.hswebframework.web.commons.entity.RecordCreationEntity;

/**
 * 角色实体
 *
 * @author zhouhao
 */
public interface RoleEntity extends GenericEntity<String>, RecordCreationEntity {

    String name     = "name";
    String describe = "describe";
    String status   = "status";
    UserEntity creator = null;

    String getName();

    void setName(String name);

    String getDescribe();

    void setDescribe(String describe);

    void setStatus(Byte status);

    Byte getStatus();

    void setCreator(UserEntity creator);
    UserEntity getCreator();

}
