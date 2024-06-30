package com.make.bookclub.repository;

import com.make.bookclub.constant.ItemSellStatus;
import com.make.bookclub.dto.ItemSearchDto;
import com.make.bookclub.dto.MainItemDto;
import com.make.bookclub.dto.QMainItemDto;
import com.make.bookclub.entity.Item;
import com.make.bookclub.entity.QItem;
import com.make.bookclub.entity.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory;
    //JPAQueryFactory 객체를 생성하지 않았으므로
// ItemRepositoryCustomImpl가 생성될 때 entity를 받아서 생성된 객체를 넣어주는 과정
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
            // 쿼리문 조합하는 BooleanExpression

        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
       //searchSellStatus의 값이 null인가? 예=>null return,
       //                          아니요=> QIem에서 searchSellStatus(sell or soldout)값 return
    }

    private  BooleanExpression regDtsAfter(String searchDateType){//등록시간 찾기
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null){
            return  null; //등록 시간의 변화가 없는 전체 이므로 null 값 return
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime=dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime=dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
        //date time을 시간에 맞게 세팅한 후 시간에 맞는 등록된 상품을 조회하도록 조건값 반환
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if (StringUtils.equals("itemNm", searchBy)){//상품명
            return  QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createBy", searchBy)) {//등록자
                                    // 작성자 ==등록자
            return  QItem.item.createdBy.like("%" + searchQuery + "%");

        }
        return  null;
    }


    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),//등록시간
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),//상품 상태
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))//등록자, 상품명
                .orderBy(QItem.item.id.desc()) //내림차순
                .offset(pageable.getOffset()).limit(pageable.getPageSize())
                .fetchResults();
                 //쿼리문 실행 리스트와 개수 반환
        //QueryDSL의 장점. null값이 나오면 PASS됨.
        List<Item> content = results.getResults();
        long total = results.getTotal();
        System.out.println(total);
        return  new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return  StringUtils.isEmpty(searchQuery)? null : QItem.item.itemNm.like("%"+ searchQuery+"%");
    }
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        //QMainItemDto @QueryPrijection을 허용하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id, item.itemNm,
                item.itemDetail, itemImg.imgUrl, item.price))
                .from(itemImg).join(itemImg.item, item).where(itemImg.repImgYn.eq("Y"))//내부조인. 대표 이미지만 가져옴
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


}
