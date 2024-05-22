package com.xk.upms.service.impl;

import com.xk.upms.dao.repository.UpmsSystemRepository;
import com.xk.upms.model.bo.UpmsSystemSaveReq;
import com.xk.upms.model.po.UpmsSystem;
import com.xk.upms.model.vo.UpmsSystemSaveResp;
import com.xk.upms.service.UpmsSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UpmsPermissionService 實現 Created by yuan on 2022/06/10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsSystemServiceImpl implements UpmsSystemService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpmsSystemServiceImpl.class);

	@Autowired
	private UpmsSystemRepository upmsSystemRepository;

    @Override
    public List list() {
        return upmsSystemRepository.findAll(Sort.by(Sort.Direction.ASC, "orders"));
    }

	@Override
	public List listActive() {
		UpmsSystem system = new UpmsSystem();
		system.setStatus(true);
		system.setOrders(null);
		Example<UpmsSystem> example = Example.of(system);
		return upmsSystemRepository.findAll(example, Sort.by(Sort.Direction.ASC, "orders"));
	}

	@Override
	public UpmsSystemSaveResp create(UpmsSystemSaveReq resources) {
		UpmsSystemSaveResp result = new UpmsSystemSaveResp();

		UpmsSystem req = new UpmsSystem();
		BeanUtils.copyProperties(resources, req);
		UpmsSystem entity = upmsSystemRepository.save(req);

		BeanUtils.copyProperties(entity, result);
		return result;
	}

	@Override
	public UpmsSystemSaveResp update(Long id, UpmsSystemSaveReq resources) {
		UpmsSystemSaveResp result = new UpmsSystemSaveResp();

		UpmsSystem req = new UpmsSystem();
		BeanUtils.copyProperties(resources, req);
		UpmsSystem entity = upmsSystemRepository.save(req);

		BeanUtils.copyProperties(entity, result);
		return result;
	}

	@Override
	public void delete(Long id) {
		upmsSystemRepository.deleteById(id);
	}

	@Override
	public void deleteByPrimaryKeys(String ids) {
		String[] idArray = ids.split("-");
		for (String idStr : idArray) {
			Long id = Long.valueOf(idStr);
			upmsSystemRepository.deleteById(id);
		}
	}

	@Override
	public Object findOneByName(String name) {
		return upmsSystemRepository.findOneByName(name);
	}
}
