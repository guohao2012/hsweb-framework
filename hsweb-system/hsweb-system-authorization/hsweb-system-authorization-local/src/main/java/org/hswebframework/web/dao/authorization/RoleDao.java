package org.hswebframework.web.dao.authorization;

import org.hswebframework.web.dao.CrudDao;
import org.hswebframework.web.entity.authorization.RoleEntity;

import java.util.List;

/**
 * @author zhouhao
 */
public interface RoleDao extends CrudDao<RoleEntity, String> {
    public void batchDelete(List<String> ids);
}
