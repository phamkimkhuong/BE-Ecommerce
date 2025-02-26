/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package iuh.fit.se.services;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 13-February-2025 8:12 PM
 */

import iuh.fit.se.dtos.NguoiDungDTO;
import iuh.fit.se.entities.NguoiDung;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NguoiDungService {
    public NguoiDungDTO findById(int id);

    public List<NguoiDungDTO> findAll();

    public NguoiDungDTO save(NguoiDungDTO nguoiDung);

    public boolean delete(int id);
}
