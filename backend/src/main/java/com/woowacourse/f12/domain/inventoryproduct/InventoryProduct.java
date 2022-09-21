package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "inventory_product")
@Builder
@Getter
public class InventoryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "selected")
    private boolean selected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected InventoryProduct() {
    }

    private InventoryProduct(final Long id, final boolean selected, final Member member, final Product product) {
        this.id = id;
        this.selected = selected;
        this.member = member;
        this.product = product;
    }

    public boolean isSameCategory(final Category category) {
        return product.isSameCategory(category);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryProduct)) {
            return false;
        }
        final InventoryProduct that = (InventoryProduct) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
