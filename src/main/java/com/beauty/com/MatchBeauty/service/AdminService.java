package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Admin;
import com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario;
import com.beauty.com.MatchBeauty.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Admin> listarAdmins() {
        return adminRepository.findAll();
    }

    public Admin buscarAdmin(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    public Admin criarAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setTipoUsuario(TipoUsuario.ADMIN);
        return adminRepository.save(admin);
    }

    public Admin atualizarAdmin(Admin admin) {
        if (adminRepository.existsById(admin.getIdUsuario())) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setTipoUsuario(TipoUsuario.ADMIN);
            return adminRepository.save(admin);
        }
        return null;
    }

    public boolean deletarAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 