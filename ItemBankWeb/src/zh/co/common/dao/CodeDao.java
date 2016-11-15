package zh.co.common.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Named;

import zh.co.item.bank.db.entity.TsCodeBean;

@Named
public class CodeDao extends BaseDao {

    @SuppressWarnings("unchecked")
    public List<TsCodeBean> selectCode(Map<String, Object> map) {

    	return getIbatisTemplate().selectList("CodeManage.selectCode", map);
    }

}
