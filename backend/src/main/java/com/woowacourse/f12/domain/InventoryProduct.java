package com.woowacourse.f12.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "inventroy_product")
@Getter
public class InventoryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_selected")
    private boolean isSelected;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "keyboard_id")
    private Keyboard keyboard;

    protected InventoryProduct() {
    }

    public void updateIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Builder
    private InventoryProduct(final Long id, final boolean isSelected, final Long memberId, final Keyboard keyboard) {
        this.id = id;
        this.isSelected = isSelected;
        this.memberId = memberId;
        this.keyboard = keyboard;
    }
}
