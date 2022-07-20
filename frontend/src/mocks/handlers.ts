import { rest } from 'msw';
import { BASE_URL, ENDPOINTS } from '../constants/api';
import {
  InventoryProducts,
  myData,
  products,
  reviewsWithOutProduct,
  reviewsWithProduct,
} from '@/mocks/data';
import sampleProfile from '@/mocks/sample_profile.jpg';

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
  return res(ctx.status(200), ctx.json(response));
};

// 상품 상세 조회
const getKeyboard = (req, res, ctx) => {
  const { id } = req.params;

  const response = products.find(
    ({ id: productId }) => productId === Number(id)
  );

  return res(ctx.status(200), ctx.json(response));
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
  return res(ctx.status(200), ctx.json(response));
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

  return res(ctx.status(200), ctx.json(response));
};

// 리뷰 작성
const postReviewByProductId = (req, res, ctx) => {
  const userData: UserData | null =
    JSON.parse(window.sessionStorage.getItem('userData')) || null;

  if (!userData || !userData.token) {
    console.log('ho');
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
      imageUrl: sampleProfile,
      name: 'F12개발자',
    },
    token: 'iJ9.eyJzdWIiOiIyIiwiaWF0IjoxNjU4MTQ4Mzg1LCJleHAiOjE2NTgxNTE',
  };
  return res(ctx.status(200), ctx.json(response));
};

const getInventoryProducts = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }

  return res(ctx.status(200), ctx.json(InventoryProducts));
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
  return res(ctx.status(200), ctx.json(myData));
};

export const handlers = [
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCTS}`, getKeyboards),
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCT(':id')}`, getKeyboard),
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
];
