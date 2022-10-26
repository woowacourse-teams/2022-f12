import {
  DefaultBodyType,
  MockedRequest,
  ResponseResolver,
  rest,
  RestContext,
  RestRequest,
} from 'msw';

import { BASE_URL, ENDPOINTS } from '@/constants/api';

import {
  InventoryProducts,
  InventoryReview,
  members,
  myUserData,
  otherUserData,
  products,
  reviewsWithOutProduct,
  reviewsWithProduct,
} from '@/mocks/data';

// let errorCall = 0;

// 인기 제품 목록 조회
const getPopularKeyboards: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  const response = {
    items: products.sort((a, b) => Math.random() - 0.5).slice(0, 4),
  };

  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

// 제품 목록 조회
const getKeyboards: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
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

// 제품 상세 조회
const getKeyboard: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  const { id } = req.params;

  const response = products.find(({ id: productId }) => productId === Number(id));

  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

// 제품 사용자 통계 조회
const getStatistics: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
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
const getReviews: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
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

// 특정 사용자가 작성한 전체 리뷰 목록 조회
const getReviewsByMemberId: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
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

// 내가 작성한 전체 리뷰 목록 조회
const getMyReviews: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
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

// 제품 별 리뷰 목록 조회
const getReviewsByProductId: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  const page = Number(req.url.searchParams.get('page'));
  const size = Number(req.url.searchParams.get('size'));

  const startIndex = page * size;
  const endIndex = (page + 1) * size;

  const response = {
    hasNext: page < 2,
    items: reviewsWithOutProduct.slice(startIndex, endIndex),
  };

  // 추후 accessToken 만료 테스트 용 핸들러
  // const errorResponse = {
  //   errorCode: 40101,
  // };

  // if (errorCall === 0) {
  //   errorCall += 1;
  //   return res(ctx.status(401), ctx.json(errorResponse), ctx.delay());
  // }

  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

// 리뷰 작성
const postReviewByProductId: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  const { shadowToken } = req.cookies;

  if (!shadowToken) {
    return res(ctx.status(403));
  }

  return res(ctx.status(201));
};

// 리뷰 수정
const updateReviewByReviewId: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  const { shadowToken } = req.cookies;

  if (!shadowToken) {
    return res(ctx.status(403));
  }
  return res(ctx.status(204));
};

// 리뷰 삭제
const deleteReviewByReviewId: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  const { shadowToken } = req.cookies;

  if (!shadowToken) {
    return res(ctx.status(403));
  }
  return res(ctx.status(204));
};

// 로그인
const handleLoginRequest: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  return res(
    ctx.status(200),
    ctx.json({
      token: 'token',
      registerCompleted: true,
      member: {
        id: 1,
        gitHubId: 'hamcheeseburger',
        name: '유현지',
        imageUrl: 'https://avatars.githubusercontent.com/u/61769743?v=4',
      },
    }),
    ctx.delay(),
    ctx.cookie('shadowToken', 'true')
  );
};

const getAccessToken: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  const { shadowToken } = req.cookies;

  if (shadowToken !== 'true') {
    return res(ctx.status(401), ctx.json({ errorCode: '40105' }));
  }

  const response = {
    accessToken: 'accessToken',
  };

  return res(
    ctx.status(200),
    ctx.json(response),
    ctx.cookie('shadowToken', 'true'),
    ctx.delay()
  );
};

const logout: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  return res(ctx.cookie('shadowToken', 'false'));
};

const getInventoryProducts: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }

  return res(ctx.status(200), ctx.json(InventoryProducts), ctx.delay());
};

const patchInventoryProducts: ResponseResolver<
  RestRequest<DefaultBodyType & { selectedInventoryProductIds: string[] }>,
  RestContext
> = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  const { selectedInventoryProductIds } = req.body;
  if (selectedInventoryProductIds === undefined) {
    return res(ctx.status(400));
  }
  return res(ctx.status(200));
};

const getMyInfo: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  return res(ctx.status(200), ctx.json(myUserData), ctx.delay());
};

// 추가 정보 입력
const submitAdditionalInfo: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  return res(ctx.status(200));
};

const getOtherMemberInfo: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  return res(ctx.json(otherUserData));
};

const searchMember: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  const page = Number(req.url.searchParams.get('page'));
  const size = Number(req.url.searchParams.get('size'));

  const startIndex = page * size;
  const endIndex = (page + 1) * size;

  const response = {
    hasNext: page < 2,
    items: members.slice(startIndex, endIndex),
  };

  return res(ctx.json(response));
};

const getOtherMemberInventory: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }

  return res(ctx.status(200), ctx.json(InventoryProducts), ctx.delay());
};

const getInventoryReview: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  return res(ctx.status(200), ctx.json(InventoryReview), ctx.delay());
};

const followUser: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  return res(ctx.status(204), ctx.delay());
};

const unfollowUser: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  return res(ctx.status(204), ctx.delay());
};

export const handlers = [
  rest.get(`${BASE_URL}${ENDPOINTS.LOGIN}`, handleLoginRequest),

  rest.get(`${BASE_URL}${ENDPOINTS.ME}`, getMyInfo),
  rest.patch(`${BASE_URL}${ENDPOINTS.ME}`, submitAdditionalInfo),
  // 아래 핸들러 순서 유의미 => getOtherMemberInfo보다 아래에 위치하면 요청 matching에서 오류 발생
  rest.get(`${BASE_URL}${ENDPOINTS.INVENTORY_PRODUCTS}`, getInventoryProducts),
  rest.patch(`${BASE_URL}${ENDPOINTS.INVENTORY_PRODUCTS}`, patchInventoryProducts),

  rest.get(`${BASE_URL}${ENDPOINTS.MEMBERS}`, searchMember),
  rest.get(`${BASE_URL}${ENDPOINTS.MEMBERS}/:memberId`, getOtherMemberInfo),
  rest.get(
    `${BASE_URL}${ENDPOINTS.MEMBERS}/:memberId/inventoryProducts`,
    getOtherMemberInventory
  ),

  rest.get(`${BASE_URL}${ENDPOINTS.POPULAR_PRODUCTS}`, getPopularKeyboards),
  // 순서 주의! 이 핸들러가 아래에 위치하면 products/:id'로 연결됩니다.
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCT(':id')}`, getKeyboard),
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCTS}`, getKeyboards),
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCT(':id')}/statistics`, getStatistics),

  rest.post(
    `${BASE_URL}${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(':id')}`,
    postReviewByProductId
  ),
  rest.get(`${BASE_URL}${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(':id')}`, getReviewsByProductId),
  rest.get(`${BASE_URL}${ENDPOINTS.REVIEWS_BY_MEMBER_ID(':id')}`, getReviewsByMemberId),
  rest.get(`${BASE_URL}${ENDPOINTS.MY_REVIEWS}`, getMyReviews),
  rest.get(`${BASE_URL}${ENDPOINTS.REVIEWS}`, getReviews),
  rest.get(
    `${BASE_URL}${ENDPOINTS.REVIEW_BY_INVENTORY_PRODUCT_ID(':id')}`,
    getInventoryReview
  ),
  rest.put(`${BASE_URL}${ENDPOINTS.REVIEWS_BY_REVIEW_ID(':id')}`, updateReviewByReviewId),
  rest.delete(
    `${BASE_URL}${ENDPOINTS.REVIEWS_BY_REVIEW_ID(':id')}`,
    deleteReviewByReviewId
  ),
  rest.get(`${BASE_URL}${ENDPOINTS.MY_FOLLOWING}`, searchMember),
  rest.post(`${BASE_URL}${ENDPOINTS.FOLLOWING(':id')}`, followUser),
  rest.delete(`${BASE_URL}${ENDPOINTS.FOLLOWING(':id')}`, unfollowUser),
  rest.post(`${BASE_URL}${ENDPOINTS.ISSUE_ACCESS_TOKEN}`, getAccessToken),
  rest.get(`${BASE_URL}${ENDPOINTS.LOGOUT}`, logout),
];
