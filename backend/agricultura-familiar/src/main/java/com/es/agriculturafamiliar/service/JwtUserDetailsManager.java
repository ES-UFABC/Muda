package com.es.agriculturafamiliar.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.es.agriculturafamiliar.entity.Role;
import com.es.agriculturafamiliar.entity.User;
import com.es.agriculturafamiliar.enums.RoleType;
import com.es.agriculturafamiliar.exception.UserAlreadySignedUpException;
import com.es.agriculturafamiliar.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class JwtUserDetailsManager implements ICustomUserDetailsService<User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String NOT_FOUND_USER_MESSAGE = "Usuário com o email fornecido não encontrado";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {      
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_USER_MESSAGE));
        log.info("Usuário com email {} e id: {} encontrado", user.getEmail(), user.getId());
        return user;
    }

    @Override
    public User createUser(UserDetails user) {
    	User userToBePersisted = (User) user;

    	Optional<User> optionalUser = userRepository.findUserByEmail(user.getUsername());
    	
    	if (optionalUser.isPresent()) {
    		throw new UserAlreadySignedUpException();
    	}    	
    	
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        
        Set<Role> roles = getRoles(user);
        userToBePersisted.setPassword(encodedPassword);
        userToBePersisted.setRoles(roles);
        
        boolean isAccountEnabled = isAccountEnabled(userToBePersisted);
        userToBePersisted.setEnabled(isAccountEnabled);

        User persistedUser = userRepository.save(userToBePersisted);
        log.info("Usuário de id {} e email {} persistido com sucesso", persistedUser.getId(), persistedUser.getEmail());
        return persistedUser;
    }

	@Override
	public void enableUser(User user) {
		user.setEnabled(true);
		userRepository.save(user);		
	}    
    
    private boolean isAccountEnabled(User user) {
    	return user.getConfirmacaoCadastro() == null;
    }

	private Set<Role> getRoles(UserDetails user) {
		return user.getAuthorities().stream()
            .map(grantedAuthority -> Role.builder().role(RoleType.valueOfIgnoreCase(grantedAuthority.getAuthority())).build())
            .collect(Collectors.toSet());
	}
        
}
