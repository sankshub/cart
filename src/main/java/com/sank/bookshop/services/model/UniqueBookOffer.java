package com.sank.bookshop.services.model;

import lombok.Data;

@Data
public class UniqueBookOffer extends DiscountOffer {
    private Integer uniqueCopies;
    private Integer discount;

    public UniqueBookOffer(){ }

    public UniqueBookOffer(Integer uniqueCopies, Integer discount){
        this.uniqueCopies = uniqueCopies;
        this.discount = discount;
        setDiscountMessage("Buy different copies of books to get maximum discount!");
    }
}
