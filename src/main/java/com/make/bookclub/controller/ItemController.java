package com.make.bookclub.controller;

import com.make.bookclub.dto.ItemFormDto;
import com.make.bookclub.dto.MemberFormDto;
import com.make.bookclub.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

}
