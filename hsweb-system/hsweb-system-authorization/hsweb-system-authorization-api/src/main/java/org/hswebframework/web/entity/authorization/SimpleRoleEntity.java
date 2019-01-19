package org.hswebframework.web.entity.authorization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hswebframework.web.commons.entity.SimpleGenericEntity;

/**
 * @author zhouhao
 */
@Getter
@Setter
@NoArgsConstructor
public class SimpleRoleEntity extends SimpleGenericEntity<String> implements RoleEntity {
    private static final long serialVersionUID = -2857131363164004807L;
    private String name;
    private Long createTime ;
    private String creatorId ;
    private String describe;
    private Byte status;
    private UserEntity creator;

    @Override
    public SimpleRoleEntity clone() {
        return ((SimpleRoleEntity) super.clone());
    }
}
