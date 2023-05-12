package com.sank.bookshop.frontlayer.mapper;

import com.sank.bookshop.frontlayer.model.BookResponse;
import com.sank.bookshop.frontlayer.model.CurrentOffer;
import com.sank.bookshop.frontlayer.model.ProcessedCart;
import com.sank.bookshop.frontlayer.model.ShoppingCart;
import com.sank.bookshop.repolayer.entity.Book;
import com.sank.bookshop.servicelayer.model.*;
import com.sank.bookshop.servicelayer.service.BookService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RequestResponseMapper {
    RequestResponseMapper MAPPER = Mappers.getMapper(RequestResponseMapper.class);

    List<BookResponse> mapToBookModelList(List<Book> source);

    @Mapping(target = "authorFullName", expression = "java(source.getAuthor().getFullName())")
    BookResponse mapBookModel(Book source);

    List<ShoppingOrder> mapShoppingCartEntityList(List<ShoppingCart> source, @Context BookService repoService);

    ShoppingOrder mapShoppingCartEntity(ShoppingCart source, @Context BookService repoService);

    @ObjectFactory
    default <T extends ShoppingOrder> T lookup(ShoppingCart source, @Context BookService repo, @TargetType Class<T> targetType) {
        Book entity = repo.findByIsbn(source.getIsbn());
        ShoppingOrder shoppingOrder = new ShoppingOrder(entity, source.getQuantity());
        return (T) shoppingOrder;
    }

    ProcessedCart mapCartModel(DiscountedCart source);

    List<com.sank.bookshop.frontlayer.model.BookSet> mapBookSetModel(List<BookSet> source);

    @Mapping(target = "discountApplied", expression = "java(source.getDiscount() + \"%\")")
    @Mapping(target = "costOfSetAfterDiscount", source = "discountedCost")
    @Mapping(target = "realCostOfSetWithoutDiscount", expression = "java(String.valueOf(source.getBooks().stream().filter(java.util.Objects::nonNull).mapToDouble(value -> Double.parseDouble(value.getPrice())).sum()) + \"€\")")
    com.sank.bookshop.frontlayer.model.BookSet mapBookSetModel(BookSet source);

    List<BookResponse> mapToBookResponse(Set<Book> source);

    List<CurrentOffer> mapToDiscountOfferModelList(List<? extends DiscountOffer> source);

    @Mapping(target = "uniqueCopiesOfBooks", source = "uniqueCopies")
    @Mapping(target = "discount", source = "discount")
    CurrentOffer mapToDiscountOfferModel(UniqueBookOffer source);

    @ObjectFactory
    default <T extends DiscountOffer> T resolve(DiscountOffer sourceDto, @TargetType Class<T> type) {
        return (T) new UniqueBookOffer();
    }

    @Mapping(target = "uniqueCopiesOfBooks", expression = "java(((UniqueBookOffer)source).getUniqueCopies())")
    @Mapping(target = "discount", expression = "java(((UniqueBookOffer)source).getDiscount() + \"%\") ")
    CurrentOffer mapToCurrentOfferModel(DiscountOffer source);


}
