describe('비회원 사용자 기본 플로우', () => {
  beforeEach(() => {
    cy.intercept({ method: 'GET', url: '**/api/v1/products*' }).as(
      'productsRequest'
    );
    cy.intercept({ method: 'GET', url: '**/api/v1/reviews*' }).as(
      'reviewsRequest'
    );

    cy.visit('');
  });

  describe('홈 페이지 테스트', () => {
    it('홈 페이지에 접속할 수 있다.', () => {
      cy.visit('');
    });

    it('홈 페이지에 접속하면 상품을 조회할 수 있다.', () => {
      cy.wait('@productsRequest');

      cy.findByRole('region', { name: /상품/ })
        .findAllByRole('article')
        .should('be.visible');
    });

    it('홈 페이지에 접속하면 후기를 조회할 수 있다.', () => {
      cy.wait('@reviewsRequest');

      cy.findByRole('region', { name: /후기/ })
        .findAllByRole('article')
        .should('be.visible');
    });

    it('홈 페이지에서 스크롤을 내리면 후기가 추가 로딩이 된다.', () => {
      cy.wait('@reviewsRequest').then(() => {
        cy.findByRole('region', { name: /후기/ }).isCardImageLoadDone();
      });

      cy.scrollTo('bottom');

      cy.wait('@reviewsRequest').then((res) => {
        const page = new URL(res.request.url).searchParams.get('page');
        expect(page).to.equal('1'); // 요청 url에서 페이지 추가 로딩 확인

        cy.findByRole('region', { name: /후기/ }).isNotLoading();
      });
    });

    it('카테고리에서 전체 상품을 클릭하면 전체 상품 리스트를 조회할 수 있다.', () => {
      cy.findByRole('button', { name: '카테고리' }).click();
      cy.findByRole('link', { name: '전체 상품' }).click();
      cy.findByRole('region', { name: '모든 상품 목록' })
        .findAllByRole('article')
        .should('be.visible');
    });
    it('카테고리에서 키보드를 클릭하면 전체 키보드 상품 리스트를 조회할 수 있다.', () => {
      cy.findByRole('button', { name: '카테고리' }).click();
      cy.findByRole('link', { name: '키보드' }).click();

      cy.wait('@productsRequest');

      cy.findByRole('region', { name: '모든 키보드 목록' })
        .findAllByRole('article')
        .should('be.visible');
    });

    it('카테고리에서 마우스를 클릭하면 전체 마우스 상품 리스트를 조회할 수 있다.', () => {
      cy.findByRole('button', { name: '카테고리' }).click();
      cy.findByRole('link', { name: '마우스' }).click();

      cy.wait('@productsRequest');

      cy.findByRole('region', { name: '모든 마우스 목록' })
        .findAllByRole('article')
        .should('be.visible');
    });

    it('카테고리에서 모니터를 클릭하면 전체 모니터 상품 리스트를 조회할 수 있다.', () => {
      cy.findByRole('button', { name: '카테고리' }).click();
      cy.findByRole('link', { name: '모니터' }).click();

      cy.wait('@productsRequest');

      cy.findByRole('region', { name: '모든 모니터 목록' })
        .findAllByRole('article')
        .should('be.visible');
    });

    it('카테고리에서 거치대를 클릭하면 전체 거치대 상품 리스트를 조회할 수 있다.', () => {
      cy.findByRole('button', { name: '카테고리' }).click();
      cy.findByRole('link', { name: '거치대' }).click();

      cy.wait('@productsRequest');

      cy.findByRole('region', { name: '모든 거치대 목록' })
        .findAllByRole('article')
        .should('be.visible');
    });

    it('카테고리에서 소프트웨어를 클릭하면 전체 소프트웨어 상품 리스트를 조회할 수 있다.', () => {
      cy.findByRole('button', { name: '카테고리' }).click();
      cy.findByRole('link', { name: '소프트웨어' }).click();

      cy.wait('@productsRequest');
      cy.findByRole('region', { name: '모든 소프트웨어 목록' })
        .findAllByRole('article')
        .should('be.visible');
    });

    it('상품 상세 페이지에 진입하면, 상품 사진과 리뷰와 통계정보를 볼 수 있다.', () => {
      cy.wait('@productsRequest');

      cy.findByRole('region', { name: '인기 있는 상품' })
        .findAllByRole('article')
        .findByRole('img')
        .click();

      cy.findByRole('region', { name: '최근 후기' })
        .findAllByRole('article')
        .should('be.visible');

      cy.findByRole('img', { name: '상품 이미지' }).should('be.visible');

      cy.scrollTo('bottom');

      cy.findByRole('article', { name: '통계 정보' }).should('be.visible');
    });
  });
});
