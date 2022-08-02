import { ENDPOINTS, BASE_URL } from '../../src/constants/api';
import { handlers } from '../../src/mocks/handlers';

import { setupServer } from 'msw/node';

const server = setupServer(...handlers);

beforeAll(() => server.listen());
afterAll(() => server.close());
afterEach(() => server.resetHandlers());

describe('비회원 사용자 기본 플로우', () => {
  beforeEach(() => {
    // cy.intercept(`${ENDPOINTS.REVIEWS}`).as('reviewsRequest');
    cy.visit('/');
    cy.intercept('GET', '**/products*', {
      statusCode: 201,
      body: {
        name: 'Peter Pan',
      },
    }).as('productsRequest');
    // cy.visit('');
  });
  it('메인 페이지에 접속할 수 있다.', () => {
    cy.visit('');
    cy.wait('@productsRequest');
    // cy.intercept({ method: 'GET', url: '**/api/v1/*' }).as('productsRequest');
  });

  // it('메인 페이지에 접속하면 인기 있는 상품을 조회할 수 있다.', () => {
  //   cy.visit('');
  //   // cy.intercept({ method: 'GET', url: '**/products/*' }).as('productsRequest');
  //   cy.wait('@productsRequest');
  // });

  // it('메인 페이지에 접속하면 최근에 작성된 후기를 조회할 수 있다.', () => {
  //   cy.visit('');
  // });

  // it('메인 페이지에서 스크롤을 내리면 최근에 작성된 후기가 추가 로딩이 된다.', () => {
  //   cy.visit('');
  // });
});
