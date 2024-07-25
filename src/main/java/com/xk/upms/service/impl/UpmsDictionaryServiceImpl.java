package com.xk.upms.service.impl;

import com.xk.upms.dao.repository.UpmsDictionaryCategaryRepository;
import com.xk.upms.dao.repository.UpmsDictionaryDataRepository;
import com.xk.upms.model.bo.UpmsDictionaryCategoryReq;
import com.xk.upms.model.bo.UpmsDictionaryDataReq;
import com.xk.upms.model.po.UpmsDictionaryCategory;
import com.xk.upms.model.vo.UpmsDictionaryCategoryResp;
import com.xk.upms.model.vo.UpmsDictionaryDataResp;
import com.xk.upms.service.UpmsDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UpmsDictionaryService 實現 Created by yuan on 2024/05/28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsDictionaryServiceImpl implements UpmsDictionaryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpmsDictionaryServiceImpl.class);

	@Autowired
	private UpmsDictionaryCategaryRepository upmsDictionaryCategaryRepository;
	@Autowired
	private UpmsDictionaryDataRepository upmsDictionaryDataRepository;

    @Override
    public List list() {
        return upmsDictionaryCategaryRepository.findAll( );
    }

	@Override
	public List listDDbyDC(String categaryCode) {
		UpmsDictionaryCategory category = upmsDictionaryCategaryRepository.findOneByCode(categaryCode);
		if (category == null) {
			return null;
		}
		return upmsDictionaryDataRepository.findByParentId(category.getId());
	}

	@Override
	public UpmsDictionaryCategoryResp createCategory(UpmsDictionaryCategoryReq resources) {
		UpmsDictionaryCategoryResp result = new UpmsDictionaryCategoryResp();

		UpmsDictionaryCategory req = new UpmsDictionaryCategory();
		BeanUtils.copyProperties(resources, req);
		UpmsDictionaryCategory entity = upmsDictionaryCategaryRepository.save(req);

		BeanUtils.copyProperties(entity, result);
		return result;
	}

	@Override
	public UpmsDictionaryCategoryResp updateCategory(Long id, UpmsDictionaryCategoryReq resources) {
		UpmsDictionaryCategoryResp result = new UpmsDictionaryCategoryResp();

		UpmsDictionaryCategory req = new UpmsDictionaryCategory();
		BeanUtils.copyProperties(resources, req);
		UpmsDictionaryCategory entity = upmsDictionaryCategaryRepository.save(req);

		BeanUtils.copyProperties(entity, result);
		return result;
	}

	@Override
	public void deleteCategory(Long id) {
		upmsDictionaryCategaryRepository.deleteById(id);

	}

	@Override
	public void deleteCategoryByPrimaryKeys(String ids) {
		String[] idArray = ids.split("-");
		for (String idStr : idArray) {
			Long id = Long.valueOf(idStr);
			upmsDictionaryCategaryRepository.deleteById(id);
		}
	}

	@Override
	public UpmsDictionaryDataResp createData(UpmsDictionaryDataReq resources) {
		return null;
	}

	@Override
	public UpmsDictionaryDataResp updateData(Long id, UpmsDictionaryDataReq resources) {
		return null;
	}

	@Override
	public void deleteData(Long id) {

	}

	@Override
	public void deleteDataByPrimaryKeys(String ids) {

	}

}
