package com.make.bookclub.service;


import com.make.bookclub.entity.ItemImg;
import com.make.bookclub.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private  final ItemImgRepository itemImgRepository;
    private  final FileService fileService;

    public  void  saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws  Exception{

        String oriImgName = itemImgFile.getOriginalFilename(); //오리지널 이미지 경로
        String imgName = "";
        String imgUrl = "";
        System.out.println(oriImgName);
        //파일 업로드\
        if (!StringUtils.isEmpty(oriImgName)){
            //oriImgName 문자열로 비어있지 않으면 실행
            System.out.println("*************");
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            System.out.println(imgName);
            imgUrl = "/images/item/" + imgName;
        }
        System.out.println("1111111111111111111111111");
        //상품 이미지 정보 저장
        // oriImgName : 상품 이미지 파일의 원래 이름
        //imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름
        //imgurl : 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        System.out.println("<<<<<<<<<<<<<<<<<<<<<");
        itemImgRepository.save(itemImg);

    }
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws  Exception{
        //                받은 itemImgIds.get(i), itemImgFileList.get(i)
        //만약 itemImgFile이 비어있을 경우
        if (!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);
            //문자열이 있는지 없는지 확인. 만약에 문자열이 비어있지 않으면 ==> 문자열이 있다.
            if (!StringUtils.isEmpty(savedItemImg.getImgName())){
                //fileservice에서 파일을 삭제한다.
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }
            // oriImgName : 상품 이미지 파일의 원래 이름 = itemImgFile의 원래 가진 이름
            String oriImgName = itemImgFile.getOriginalFilename();
     //imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름
    // return 한 savedFileName을 imgName에 담음 = fileservice에서 uploadFile 호출.
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            //imgurl : 로컬에 저장된 상품 이미지 파일을 불러오는 경로
            String imgUrl = "/images/item/" + imgName;
            //상품 이미지 정보 저장
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            //* 이미지 파일의 경우 똑같은 파일을 지우고 다시쓰는 과정으로 update됨
            // => 같은 이미지 파일인지 일일히 확인 할 수 없으므로 지우고 다시 쓰는게 자원소모가 훨씬 적으므로
        }
    }


}
