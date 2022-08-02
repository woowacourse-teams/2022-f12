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
    cy.wait('@reviewsRequest');

    cy.scrollTo('bottom');

    cy.wait('@reviewsRequest').then((res) => {
      const page = new URL(res.request.url).searchParams.get('page');
      expect(page).to.equal('1');

      cy.findByRole('region', { name: /후기/ })
        .findByRole('region', { name: 'loading' })
        .should('not.exist');
    });
  });
});
