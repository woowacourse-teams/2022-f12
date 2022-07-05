import { rest } from 'msw';
import { products, reviews } from './data';

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

const getKeyboard = (req, res, ctx) => {
  const { id } = req.params;

  const response = products[Number(id) + 1];

  return res(ctx.status(200), ctx.json(response));
};

const getReviews = (req, res, ctx) => {
  const page = Number(req.url.searchParams.get('page'));
  const size = Number(req.url.searchParams.get('size'));

  const startIndex = page * size;
  const endIndex = (page + 1) * size;

  const response = {
    hasNext: page < 2,
    items: reviews.slice(startIndex, endIndex),
  };
  return res(ctx.status(200), ctx.json(response));
};

const getReviewsByProductId = (req, res, ctx) => {
  const page = req.url.searchParams.get('page');
  const size = req.url.searchParams.get('size');

  const response = {
    hasNext: page < 2,
    reviews: [reviews.slice(0, size)],
  };

  return res(ctx.status(200), ctx.json(response));
};

const postReviewByProductId = (req, res, ctx) => {
  return res(ctx.status(201));
};

export const handlers = [
  rest.get('api/v1/keyboards', getKeyboards),
  rest.get('api/v1/keyboards/:id', getKeyboard),
  rest.get('api/v1/reviews', getReviews),
  rest.get('api/v1/keyboards/:id/reviews', getReviewsByProductId),
  rest.post('api/v1/keyboards/:id/reviews', postReviewByProductId),
];
