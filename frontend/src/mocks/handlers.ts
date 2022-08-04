import { rest } from 'msw';
import { BASE_URL, ENDPOINTS } from '@/constants/api';
import {
  InventoryProducts,
  myData,
  products,
  reviewsWithOutProduct,
  reviewsWithProduct,
} from '@/mocks/data';

// 상품 목록 조회
const getKeyboards = (req, res, ctx) => {
  const page = Number(req.url.searchParams.get('page'));
  const size = Number(req.url.searchParams.get('size'));
  const startIndex = page * size;
  const endIndex = (page + 1) * size;

  const response = {
    hasNext: page < 2,
    items: products.slice(startIndex, endIndex),
  };
  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

// 상품 상세 조회
const getKeyboard = (req, res, ctx) => {
  const { id } = req.params;

  const response = products.find(
    ({ id: productId }) => productId === Number(id)
  );

  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

// 상품 사용자 통계 조회
// 상품 상세 조회
const getStatistics = (req, res, ctx) => {
  const response = {
    careerLevel: {
      midlevel: 0.2,
      senior: 0.3,
      none: 0.1,
      junior: 0.4,
    },
    jobType: {
      frontend: 0.45,
      backend: 0.25,
      mobile: 0.2,
      etc: 0.1,
    },
  };
  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

// 전체 리뷰 목록 조회
const getReviews = (req, res, ctx) => {
  const page = Number(req.url.searchParams.get('page'));
  const size = Number(req.url.searchParams.get('size'));

  const startIndex = page * size;
  const endIndex = (page + 1) * size;

  const response = {
    hasNext: page < 2,
    items: reviewsWithProduct.slice(startIndex, endIndex),
  };
  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

// 상품 별 리뷰 목록 조회
const getReviewsByProductId = (req, res, ctx) => {
  const page = Number(req.url.searchParams.get('page'));
  const size = Number(req.url.searchParams.get('size'));

  const startIndex = page * size;
  const endIndex = (page + 1) * size;

  const response = {
    hasNext: page < 2,
    items: reviewsWithOutProduct.slice(startIndex, endIndex),
  };

  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

// 리뷰 작성
const postReviewByProductId = (req, res, ctx) => {
  const userData: UserData | null =
    JSON.parse(window.sessionStorage.getItem('userData')) || null;

  if (!userData || !userData.token) {
    return res(ctx.status(403));
  }
  return res(ctx.status(201));
};

// 리뷰 수정 기능
const updateReviewByReviewId = (req, res, ctx) => {
  const userData: UserData | null =
    JSON.parse(window.sessionStorage.getItem('userData')) || null;

  if (!userData || !userData.token) {
    return res(ctx.status(403));
  }
  return res(ctx.status(204));
};

// 리뷰 삭제
const deleteReviewByReviewId = (req, res, ctx) => {
  const userData: UserData | null =
    JSON.parse(window.sessionStorage.getItem('userData')) || null;

  if (!userData || !userData.token) {
    return res(ctx.status(403));
  }
  return res(ctx.status(204));
};

// 로그인
const getToken = (req, res, ctx) => {
  const response = {
    member: {
      id: 1,
      gitHubId: '사용자2',
      imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
      name: 'F12개발자',
    },
    registerCompleted: false,
    token: 'iJ9.eyJzdWIiOiIyIiwiaWF0IjoxNjU4MTQ4Mzg1LCJleHAiOjE2NTgxNTE',
  };
  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

const getInventoryProducts = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }

  return res(ctx.status(200), ctx.json(InventoryProducts), ctx.delay());
};

const patchInventoryProducts = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  const { selectedInventoryProductId, unselectedInventoryProductId } = req.body;
  if (
    selectedInventoryProductId === undefined &&
    unselectedInventoryProductId === undefined
  ) {
    return res(ctx.status(400));
  }
  return res(ctx.status(200));
};

const getMyInfo = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  return res(ctx.status(200), ctx.json(myData), ctx.delay());
};

// 추가 정보 입력
const submitAdditionalInfo = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  return res(ctx.status(200));
};

const getOtherMemberInfo = (req, res, ctx) => {
  return res(ctx.json({ ...myData, jobType: null, careerLevel: null }));
};

export const handlers = [
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCTS}`, getKeyboards),
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCT(':id')}`, getKeyboard),
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCT(':id')}/statistics`, getStatistics),
  rest.get(`${BASE_URL}${ENDPOINTS.REVIEWS}`, getReviews),
  rest.get(
    `${BASE_URL}${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(':id')}`,
    getReviewsByProductId
  ),
  rest.post(
    `${BASE_URL}${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(':id')}`,
    postReviewByProductId
  ),
  rest.put(
    `${BASE_URL}${ENDPOINTS.REVIEWS_BY_REVIEW_ID(':id')}`,
    updateReviewByReviewId
  ),
  rest.delete(
    `${BASE_URL}${ENDPOINTS.REVIEWS_BY_REVIEW_ID(':id')}`,
    deleteReviewByReviewId
  ),
  rest.get(`${BASE_URL}${ENDPOINTS.LOGIN}`, getToken),
  rest.get(`${BASE_URL}${ENDPOINTS.INVENTORY_PRODUCTS}`, getInventoryProducts),
  rest.patch(
    `${BASE_URL}${ENDPOINTS.INVENTORY_PRODUCTS}`,
    patchInventoryProducts
  ),
  rest.get(`${BASE_URL}${ENDPOINTS.ME}`, getMyInfo),
  rest.patch(`${BASE_URL}${ENDPOINTS.ME}`, submitAdditionalInfo),
  rest.get(`${BASE_URL}${ENDPOINTS.MEMBERS}/:memberId`, getOtherMemberInfo),
];
