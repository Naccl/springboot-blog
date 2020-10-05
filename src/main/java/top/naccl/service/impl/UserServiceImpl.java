package top.naccl.service.impl;

import top.naccl.dao.UserRepository;
import top.naccl.po.User;
import top.naccl.service.UserService;
import top.naccl.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 用户登录实现类
 * @Author: Naccl
 * @Date: 2020-04-24
 */

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User checkUser(String username, String password) {
		User user = userRepository.findByUsernameAndPassword(username, MD5Utils.getMD5(password));
		return user;
	}
}
