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

@Controller
@RequiredArgsConstructor
public class ItemController {

    private ItemService itemService;

    @GetMapping(value ="/admin/item/new")
    public String addItemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
        //상품 등록을 누른다. -> itemForm으로 이동한다.
    }

    @PostMapping(value ="/admin/item/new")
    public  String addItemPost(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            //만약에 error가 있다면, 돌려보냄.
            return "/item/itemForm";
        }
        itemService.saveItem(itemFormDto);
        //오류가 없으면, 저장하러 itemservice로

        return "redirect:/";
    }

}
