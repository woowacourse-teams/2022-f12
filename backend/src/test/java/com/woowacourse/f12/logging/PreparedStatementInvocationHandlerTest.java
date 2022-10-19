package com.woowacourse.f12.logging;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

class PreparedStatementInvocationHandlerTest {

    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ApiQueryCounter apiQueryCounter;
    @Mock
    private RequestAttributes requestAttributes;

    private PreparedStatementInvocationHandler preparedStatementInvocationHandler;

    @BeforeEach
    void setUp() {
        this.preparedStatement = mock(PreparedStatement.class);
        this.apiQueryCounter = mock(ApiQueryCounter.class);
        this.requestAttributes = mock(RequestAttributes.class);
        this.preparedStatementInvocationHandler = new PreparedStatementInvocationHandler(preparedStatement,
                apiQueryCounter);
    }

    @Test
    void Request_Scope_에서_execute_가_들어간_메서드를_실행시키면_쿼리_횟수를_카운트_한다() throws Exception {
        // given
        RequestContextHolder.setRequestAttributes(requestAttributes);
        Method executeMethod = preparedStatement.getClass().getMethod("execute");

        // when
        preparedStatementInvocationHandler.invoke(null, executeMethod, null);

        // then
        verify(apiQueryCounter).increaseCount();
    }

    @Test
    void Request_Scope_에서_execute_가_들어가지_않은_메서드를_실행시키면_쿼리_횟수를_카운트_하지_않는다() throws Exception {
        // given
        RequestContextHolder.setRequestAttributes(requestAttributes);
        Method method = preparedStatement.getClass().getMethod("getMetaData");

        // when
        preparedStatementInvocationHandler.invoke(null, method, null);

        // then
        verify(apiQueryCounter, times(0)).increaseCount();
    }

    @Test
    void Request_Scope_가_아닌_경우에_메서드를_실행시키면_쿼리_횟수를_카운트_하지_않는다() throws Exception {
        // given
        Method executeMethod = preparedStatement.getClass().getMethod("execute");

        // when
        preparedStatementInvocationHandler.invoke(null, executeMethod, null);

        // then
        verify(apiQueryCounter, times(0)).increaseCount();
    }
}
