package com.xk.demo.service.impl;

import com.xk.common.util.PageQuery;
import com.xk.demo.dao.repository.SysUserRepository;
import com.xk.demo.model.po.SysUser;
import com.xk.demo.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public void save(SysUser user) {
        sysUserRepository.save(user);
    }

    @Override
    public void delete(SysUser user) {
        sysUserRepository.delete(user);
    }

    @Override
    public List<SysUser> findAll() {
        return sysUserRepository.findAll();
    }

    @Override
    public Object findPage(PageQuery pageQuery) {
        return sysUserRepository.findAll(PageRequest.of(pageQuery.getPage(), pageQuery.getSize()));
    }

}