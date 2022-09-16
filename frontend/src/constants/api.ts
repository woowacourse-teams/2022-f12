export const BASE_URL = __API_URL__;

const githubClientId = __GITHUB_CLIENT_ID__;

export const GITHUB_AUTH_URL = `https://github.com/login/oauth/authorize?client_id=${githubClientId}`;

export const ENDPOINTS = {
  PRODUCTS: '/products',
  PRODUCT: (id: number | ':id') => `/products/${id}`,
  REVIEWS: '/reviews',
  REVIEWS_BY_PRODUCT_ID: (id: number | ':id') => `/products/${id}/reviews`,
  REVIEWS_BY_REVIEW_ID: (id: number | ':id') => `/reviews/${id}`,
  REVIEWS_BY_MEMBER_ID: (id: number | ':id') => `/members/${id}/reviews`,
  MY_REVIEWS: '/members/me/reviews',
  LOGIN: '/login',
  INVENTORY_PRODUCTS: '/members/inventoryProducts',
  OTHER_INVENTORY_PRODUCTS: (id: number | ':id') => `/members/${id}/inventoryProducts`,
  REVIEW_BY_INVENTORY_PRODUCT_ID: (id: number | ':id') =>
    `/inventoryProducts/${id}/reviews`,
  MEMBERS: '/members',
  ME: '/members/me',
  MY_FOLLOWING: '/members/me/followings',
  FOLLOWING: (id: number | ':id') => `/members/${id}/following`,
} as const;
