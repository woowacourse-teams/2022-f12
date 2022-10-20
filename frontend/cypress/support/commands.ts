import '@testing-library/cypress/add-commands';

Cypress.Commands.add(
  'isCardImageLoadDone',
  { prevSubject: true },
  (card: Cypress.Chainable<JQuery<HTMLElement>>) => {
    cy.wrap(card)
      .findAllByRole('link')
      .each((element) => {
        cy.wrap(element).get('img').should('be.visible');
      });
  }
);

Cypress.Commands.add(
  'isNotLoading',
  { prevSubject: true },
  (container: Cypress.Chainable<JQuery<HTMLElement>>) => {
    cy.wrap(container).findByRole('region', { name: 'loading' }).should('not.exist');
  }
);
