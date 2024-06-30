package com.make.bookclub.service;

import com.make.bookclub.dto.ItemFormDto;
import com.make.bookclub.dto.ItemImgDto;
import com.make.bookclub.dto.ItemSearchDto;
import com.make.bookclub.dto.MainItemDto;
import com.make.bookclub.entity.Item;
import com.make.bookclub.entity.ItemImg;
import com.make.bookclub.repository.ItemImgRepository;
import com.make.bookclub.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
       // 찾은 item담음 =          itemFormDto에 담긴 id를 이용해서 id를 찾음.넣었는데 안나올 경우 exception발생
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
     // 하나가 아니므로 list로 받음= itemFormDto에 있는 ItemImgIds에서 정보 빼옴.
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        for (int i = 0 ; i<itemImgFileList.size() ;i++){
            //itemImgIds에서 받은 정보를 가지고 itemImgService로 가서 img변경
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        // 따로 save하지 않음. 이유: db에서 변경감지(☆☆☆☆☆)를 통해 자동으로 변경 내용 저장됨.
        return  item.getId();
    }


    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
    //하나가 아니므로 list로 받음= item에서  id를 받아서 바로 쿼리문. 아이디에 해당하는 id 오름차순으로 정렬.
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
      // 화면으로 갈 수 있게 dto로 바꾸는 과정.
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
    // 찾은 item 정보받음 =item repository에서 쿼리문으로 id찾기. 넣었는데 안나올 경우 exception발생
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
 // 변경한 정보를 itemFormDto에 담음= entity와 dto를 연결한 mapper로 dto로 변경
    //**mapper가 없었다면? => ItemFormDto itemFormDto = new ItemFormDto();
    //                   => itemFormDto.setItemNm(item.getItemNm()); ..... 모두 담는 과정 필요
        //item의 정보만 들어간 상태, img에 대한 정보는 들어가지 않음
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        //위에서 뽑아낸 itemImgDtoList를 setItemImgDtoList에 담아주는 과정
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        //item과 itemimglist정보가 담긴 itemFormDto를 controller로 돌려보냄
        return itemFormDto;

    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }




}
