package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.product.Product;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "inventory_product")
@Getter
public class InventoryProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "selected")
    private boolean selected;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected InventoryProduct() {
    }

    @Builder
    private InventoryProduct(final Long id, final boolean selected, final Long memberId, final Product product) {
        this.id = id;
        this.selected = selected;
        this.memberId = memberId;
        this.product = product;
    }

    public void updateSelected(boolean selected) {
        this.selected = selected;
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
