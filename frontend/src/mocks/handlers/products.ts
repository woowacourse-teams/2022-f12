import { ResponseResolver, RestContext, RestRequest } from 'msw';

import { products } from '@/mocks/data/products';

// 인기 제품 목록 조회
export const getPopularKeyboards: ResponseResolver<RestRequest, RestContext> = (
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
export const getKeyboards: ResponseResolver<RestRequest, RestContext> = (
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
    items: products.slice(startIndex, endIndex),
  };

  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

// 제품 상세 조회
export const getKeyboard: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  const { id } = req.params;

  const response = products.find(({ id: productId }) => productId === Number(id));
  console.log(products, id);

  return res(ctx.status(200), ctx.json(response), ctx.delay());
};

// 제품 사용자 통계 조회
export const getStatistics: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
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
