/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package iuh.fit.se.repositories;


import iuh.fit.se.entities.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 13-February-2025 8:05 PM
 */
@RepositoryRestResource(collectionResourceRel = "nguoidung", path = "nguoidung")
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
}

    