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

import iuh.fit.se.dtos.NguoiDungDTO;
import iuh.fit.se.entities.NguoiDung;
import iuh.fit.se.exceptions.ItemNotFoundException;
import iuh.fit.se.repositories.NguoiDungRepository;
import iuh.fit.se.services.NguoiDungService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NguoiDungServiceImpl implements NguoiDungService {

    @Autowired
    NguoiDungRepository nguoiDungRepository;

    @Autowired
    ModelMapper modelMapper;

    private NguoiDungDTO convertToDTO(NguoiDung nguoiDung) {
        NguoiDungDTO nguoiDungDTO = modelMapper.map(nguoiDung, NguoiDungDTO.class);
        return nguoiDungDTO;
    }

    private NguoiDung convertToEntity(NguoiDungDTO nguoiDungDTO) {
        NguoiDung nguoiDung = modelMapper.map(nguoiDungDTO, NguoiDung.class);
        return nguoiDung;
    }


    @Override
    public NguoiDungDTO findById(int id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(()-> new ItemNotFoundException("Can not find Employee with id: " + id));

        return this.convertToDTO(nguoiDung);
    }

    @Transactional
    @Override
    public List<NguoiDungDTO> findAll() {
        return nguoiDungRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public NguoiDungDTO save(NguoiDungDTO nguoiDungDTO) {
        NguoiDung nguoiDung = this.convertToEntity(nguoiDungDTO);
        nguoiDung = nguoiDungRepository.save(nguoiDung);
        return this.convertToDTO(nguoiDung);
    }

    @Override
    public boolean delete(int id) {
        this.findById(id);
        nguoiDungRepository.deleteById(id);
        return true;
    }
}
