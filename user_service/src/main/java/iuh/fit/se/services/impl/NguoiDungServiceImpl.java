/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package iuh.fit.se.services.impl;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 13-February-2025 8:15 PM
 */

import iuh.fit.se.entities.NguoiDung;
import iuh.fit.se.repositories.NguoiDungRepository;
import iuh.fit.se.services.NguoiDungService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NguoiDungServiceImpl implements NguoiDungService {

    @Autowired
    NguoiDungRepository nguoiDungRepository;

//    @Autowired
//    ModelMapper modelMapper;

    @Override
    public NguoiDung findById(int id) {
        return nguoiDungRepository.findById(id).orElseThrow(()-> new RuntimeException("cannot find user"));
    }

    @Override
    public List<NguoiDung> findAll() {
        return nguoiDungRepository.findAll().stream().toList();
    }

    @Transactional
    @Override
    public NguoiDung save(NguoiDung nguoiDung) {
        return nguoiDungRepository.save(nguoiDung);
    }

    @Override
    public boolean delete(int id) {
        this.findById(id);
        nguoiDungRepository.deleteById(id);
        return true;
    }
}
