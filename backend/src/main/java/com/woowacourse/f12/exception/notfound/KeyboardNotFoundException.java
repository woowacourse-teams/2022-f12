package com.woowacourse.f12.exception.notfound;

import static com.woowacourse.f12.exception.ErrorCode.DATA_NOT_FOUND;

public class KeyboardNotFoundException extends NotFoundException {

    public KeyboardNotFoundException() {
        super("키보드를 찾을 수 없습니다.", DATA_NOT_FOUND);
    }
}
