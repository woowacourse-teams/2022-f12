import { rest } from 'msw';

const getKeyboards = (req, res, ctx) => {
  return res(ctx.status(200));
};

export const handlers = [rest.get('api/v1/keyboards', getKeyboards)];
