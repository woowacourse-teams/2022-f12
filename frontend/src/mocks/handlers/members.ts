import { ResponseResolver, RestContext, RestRequest } from 'msw';

import { members, myUserData, otherUserData } from '@/mocks/data/member';

// 로그인
export const handleLoginRequest: ResponseResolver<RestRequest, RestContext> = (
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

export const getAccessToken: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
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

export const logout: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  return res(ctx.cookie('shadowToken', 'false'));
};

export const getMyInfo: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  return res(ctx.status(200), ctx.json(myUserData), ctx.delay());
};

// 추가 정보 입력
export const submitAdditionalInfo: ResponseResolver<RestRequest, RestContext> = (
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

export const getOtherMemberInfo: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  return res(ctx.json(otherUserData));
};

export const searchMember: ResponseResolver<RestRequest, RestContext> = (
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
    items: members.slice(startIndex, endIndex),
  };

  return res(ctx.json(response));
};

export const followUser: ResponseResolver<RestRequest, RestContext> = (req, res, ctx) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  return res(ctx.status(204), ctx.delay());
};

export const unfollowUser: ResponseResolver<RestRequest, RestContext> = (
  req,
  res,
  ctx
) => {
  const token = req.headers.get('Authorization');
  if (token === undefined) {
    return res(ctx.status(401));
  }
  return res(ctx.status(204), ctx.delay());
};
