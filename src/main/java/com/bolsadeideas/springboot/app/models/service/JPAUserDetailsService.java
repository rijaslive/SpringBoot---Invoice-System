package com.bolsadeideas.springboot.app.models.service;

import java.util.ArrayList;
import java.util.List;

import com.bolsadeideas.springboot.app.security.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IUserDao;
import com.bolsadeideas.springboot.app.models.entity.Role;
import com.bolsadeideas.springboot.app.models.entity.User;

@Service("jpaUserDetailsService")
public class JPAUserDetailsService implements UserDetailsService{

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserDao userDao;

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if(user == null) {
			log.error("The user " + username + " not exists!");
			throw new UsernameNotFoundException("The user " + username + " not exists!");
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(Role role : user.getRoles()) {
			log.info("User Role: " + role.getAuthority());
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		if(authorities.isEmpty()) {
			log.error("The user " + username + " not assigned any roles!");
			throw new UsernameNotFoundException("The user " + username + " not assigned any roles!");
		}
		return new CurrentUser(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities, user.getId());
	}



}
