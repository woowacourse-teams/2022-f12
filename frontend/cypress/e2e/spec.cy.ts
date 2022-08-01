describe('empty spec', () => {
  it('passes', () => {
    cy.visit('http://localhost:3000');
    cy.findByRole('link', {
      name: '전체 상품 목록',
    }).click();
  });
});
