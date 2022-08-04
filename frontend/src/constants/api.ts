export const BASE_URL = __API_URL__;

const githubClientId = __GITHUB_CLIENT_ID__;

export const GITHUB_AUTH_URL = `https://github.com/login/oauth/authorize?client_id=${githubClientId}`;

export const ENDPOINTS = {
  PRODUCTS: '/products',
  PRODUCT: (id: number | ':id') => `/products/${id}`,
  REVIEWS: '/reviews',
  REVIEWS_BY_PRODUCT_ID: (id: number | ':id') => `/products/${id}/reviews`,
  REVIEWS_BY_REVIEW_ID: (reviewId: number | ':id') => `/reviews/${reviewId}`,
  LOGIN: '/login',
  INVENTORY_PRODUCTS: '/members/inventoryProducts',
  OTHER_INVENTORY_PRODUCTS: (id: string) => `/members/${id}/inventoryProducts`,
  MEMBERS: '/members',
  ME: '/members/me',
} as const;
