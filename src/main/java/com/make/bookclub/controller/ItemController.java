package com.make.bookclub.controller;

import com.make.bookclub.dto.ItemFormDto;
import com.make.bookclub.dto.ItemSearchDto;
import com.make.bookclub.dto.MemberFormDto;
import com.make.bookclub.entity.Item;
import com.make.bookclub.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value ="/admin/item/new")
    public String addItemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
        //상품 등록을 누른다. -> itemForm으로 이동한다.
    }

    @PostMapping(value ="/admin/item/new")
    public  String addItemPost(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                               @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){
                                //이미지 파일을 불러내서 List에 넣음
        if (bindingResult.hasErrors()){
            model.addAttribute("errorMessage", "bindingResult");
            //만약에 error가 있다면, 돌려보냄.
            return "/item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() ==null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try{
            itemService.saveItem(itemFormDto, itemImgFileList);

        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId")Long itemId, Model model){
        try{
            //service에서 return한 itemForm Dto 값을 받음 = itemservice에  itemid값을 보내주고 처리
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);

            //service에서 받은 itemFormDto값을 html에 실어보냄
            model.addAttribute("itemFormDto", itemFormDto);

        }catch (EntityNotFoundException e){
            //만약 service에서 error가 발생할 경우 errormessage를 실어 html로
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            //service에서 받은 itemFormDto값을 html에 실어보냄
            model.addAttribute("itemFormDto", new ItemFormDto());

        }
        // itemForm html로
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public  String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult
                            , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                              Model model){
        //유효성 검사
        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }
        //첫번째 이미지가 비어있고  + itemFormDto의 id가 없을 경우
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() ==null){
            //errormessage를 담아서 다시 itemForm.html로 돌려보냄
            model.addAttribute("errorMessage" , "첫번째 상품 이미지는 필수 입력 값입니다.");
            return  "item/itemForm";
        }
        try{
            itemService.updateItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            //itemservice과정 중 exception 발생 시 errormessage를 담아서 다시 itemForm.html로 돌려보냄
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";

        }
        // try까지 끝나면 메인.html 호출하여 view보여짐
        return  "redirect:/";

    }


    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        // 페이지 값이 있는가? 예=> page의 값 : 아니요 => 0, 페이지당 사이즈 5개
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return  "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId")Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }


}
