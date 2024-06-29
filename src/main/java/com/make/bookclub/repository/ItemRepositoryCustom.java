package com.make.bookclub.repository;

import com.make.bookclub.dto.ItemSearchDto;
import com.make.bookclub.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    // 게시판이므로 페이지 단위로 받음
}
