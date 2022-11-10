import { ResponseResolver, RestContext, RestRequest } from 'msw';

import { reviewsWithOutProduct, reviewsWithProduct } from '@/mocks/data/reviews';

// 전체 리뷰 목록 조회
export const getReviews: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
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
export const getReviewsByMemberId: ResponseResolver<RestRequest, RestContext> = (
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
export const getMyReviews: ResponseResolver<RestRequest, RestContext> = (
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

// 제품 별 리뷰 목록 조회
export const getReviewsByProductId: ResponseResolver<RestRequest, RestContext> = (
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
export const postReviewByProductId: ResponseResolver<RestRequest, RestContext> = (
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
export const updateReviewByReviewId: ResponseResolver<RestRequest, RestContext> = (
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
export const deleteReviewByReviewId: ResponseResolver<RestRequest, RestContext> = (
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
