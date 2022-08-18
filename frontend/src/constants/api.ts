export const BASE_URL = __API_URL__;

const githubClientId = __GITHUB_CLIENT_ID__;

export const GITHUB_AUTH_URL = `https://github.com/login/oauth/authorize?client_id=${githubClientId}`;

export const ENDPOINTS = {
  PRODUCTS: '/products',
  PRODUCT: (id: number | ':id') => `/products/${id}`,
  REVIEWS: '/reviews',
  REVIEWS_BY_PRODUCT_ID: (id: number | ':id') => `/products/${id}/reviews`,
  REVIEWS_BY_REVIEW_ID: (id: number | ':id') => `/reviews/${id}`,
  LOGIN: '/login',
  INVENTORY_PRODUCTS: '/members/inventoryProducts',
  OTHER_INVENTORY_PRODUCTS: (id: number) => `/members/${id}/inventoryProducts`,
  REVIEW_BY_INVENTORY_PRODUCT_ID: (id: number) => `/inventoryProducts/${id}/reviews`,
  MEMBERS: '/members',
  ME: '/members/me',
  MY_FOLLOWING: '/members/me/followees',
  FOLLOWING: (id: number | ':id') => `/members/${id}/following`,
} as const;
