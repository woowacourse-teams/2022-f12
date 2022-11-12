import { DefaultBodyType, ResponseResolver, RestContext, RestRequest } from 'msw';

import { InventoryProducts } from '@/mocks/data/inventoryProducts';
import { myUserData } from '@/mocks/data/member';
import { InventoryReview } from '@/mocks/data/reviews';

export const getInventoryProducts: ResponseResolver<RestRequest, RestContext> = (
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

export const patchInventoryProducts: ResponseResolver<
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

export const getMyInfo: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  return res(ctx.status(200), ctx.json(myUserData), ctx.delay());
};

export const getOtherMemberInventory: ResponseResolver<RestRequest, RestContext> = (
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

export const getInventoryReview: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  return res(ctx.status(200), ctx.json(InventoryReview), ctx.delay());
};
