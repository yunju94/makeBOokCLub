package com.make.bookclub.controller;

import com.make.bookclub.dto.itemFormDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ItemController {

    @GetMapping(value ="/admin/item/new")
    public String addItemForm(Model model){
        model.addAttribute("itemFormDto", new itemFormDto());
        return "/item/itemForm";
        //상품 등록을 누른다. -> itemForm으로 이동한다.
    }
}
