package com.make.bookclub.service;

import com.make.bookclub.dto.ItemFormDto;
import com.make.bookclub.dto.ItemImgDto;
import com.make.bookclub.entity.Item;
import com.make.bookclub.entity.ItemImg;
import com.make.bookclub.repository.ItemImgRepository;
import com.make.bookclub.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private  final ItemImgRepository itemImgRepository;

    // 저장소와 연결해서 상품 등록
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws  Exception{
        //상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        //이미지 등록
        for (int i = 0 ; i< itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i==0){
                itemImg.setRepImgYn("Y");
            }else {
                itemImg.setRepImgYn("N");
            }itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));

        }
        return  item.getId();
    }




}
