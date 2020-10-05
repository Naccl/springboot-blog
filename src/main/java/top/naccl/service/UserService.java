package top.naccl.service;

import top.naccl.po.User;

public interface UserService {

	User checkUser(String username, String password);
}
