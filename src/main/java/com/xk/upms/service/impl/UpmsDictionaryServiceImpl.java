package com.xk.upms.service.impl;

import com.xk.upms.dao.repository.UpmsDictionaryCategoryRepository;
import com.xk.upms.dao.repository.UpmsDictionaryDataRepository;
import com.xk.upms.model.bo.UpmsDictionaryCategoryReq;
import com.xk.upms.model.bo.UpmsDictionaryDataReq;
import com.xk.upms.model.po.UpmsDictionaryCategory;
import com.xk.upms.model.po.UpmsDictionaryData;
import com.xk.upms.model.vo.UpmsDictionaryCategoryResp;
import com.xk.upms.model.vo.UpmsDictionaryDataResp;
import com.xk.upms.service.UpmsDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * UpmsDictionaryService 實現 Created by yuan on 2024/05/28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsDictionaryServiceImpl implements UpmsDictionaryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpmsDictionaryServiceImpl.class);

	@Autowired
	private UpmsDictionaryCategoryRepository upmsDictionaryCategoryRepository;
	@Autowired
	private UpmsDictionaryDataRepository upmsDictionaryDataRepository;

    @Override
    public List list() {
        return upmsDictionaryCategoryRepository.findAll( );
    }

	@Override
	public List listDDbyDC(String categaryCode) {
		UpmsDictionaryCategory category = upmsDictionaryCategoryRepository.findOneByCode(categaryCode);
		if (category == null) {
			return null;
		}
		return upmsDictionaryDataRepository.findByParentIdOrderByCode(category.getId());
	}

	@Override
	public UpmsDictionaryCategoryResp createCategory(UpmsDictionaryCategoryReq resources) {
		UpmsDictionaryCategoryResp result = new UpmsDictionaryCategoryResp();

		UpmsDictionaryCategory req = new UpmsDictionaryCategory();
		BeanUtils.copyProperties(resources, req);
		UpmsDictionaryCategory entity = upmsDictionaryCategoryRepository.save(req);

		BeanUtils.copyProperties(entity, result);
		return result;
	}

	@Override
	public UpmsDictionaryCategoryResp updateCategory(Long id, UpmsDictionaryCategoryReq resources) {
		UpmsDictionaryCategoryResp result = new UpmsDictionaryCategoryResp();

		UpmsDictionaryCategory req = new UpmsDictionaryCategory();
		BeanUtils.copyProperties(resources, req);
		UpmsDictionaryCategory entity = upmsDictionaryCategoryRepository.save(req);

		BeanUtils.copyProperties(entity, result);
		return result;
	}

	@Override
	public void deleteCategory(Long id) {
		upmsDictionaryCategoryRepository.deleteById(id);

	}

	@Override
	public void deleteCategoryByPrimaryKeys(String ids) {
		String[] idArray = ids.split("-");
		for (String idStr : idArray) {
			Long id = Long.valueOf(idStr);
			upmsDictionaryCategoryRepository.deleteById(id);
		}
	}

	@Override
	public UpmsDictionaryDataResp createData(UpmsDictionaryDataReq resources) {
		return null;
	}

	@Override
	public List<UpmsDictionaryDataResp> updateData(Long categoryId, List<UpmsDictionaryDataReq> resources) {
    	List<UpmsDictionaryDataResp> resultList = new ArrayList<>();

    	for (UpmsDictionaryDataReq resource : resources) {
    		UpmsDictionaryDataResp result = new UpmsDictionaryDataResp();

			UpmsDictionaryData req = new UpmsDictionaryData();
			BeanUtils.copyProperties(resource, req);
			req.setParentId(categoryId);
			UpmsDictionaryData entity = upmsDictionaryDataRepository.save(req);

			BeanUtils.copyProperties(entity, result);
			resultList.add(result);
		}
		return resultList;
	}

	@Override
	public void deleteData(Long id) {

	}

	@Override
	public void deleteDataByPrimaryKeys(String ids) {

	}

}
