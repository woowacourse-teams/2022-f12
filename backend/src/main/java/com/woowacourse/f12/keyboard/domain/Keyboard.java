package com.woowacourse.f12.keyboard.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "keyboard")
@Getter
public class Keyboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    protected Keyboard() {

    }

    ;

    @Builder
    private Keyboard(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Keyboard keyboard = (Keyboard) o;
        return Objects.equals(id, keyboard.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
