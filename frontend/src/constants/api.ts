export const BASE_URL = __API_URL__;

export const GITHUB_AUTH_URL =
  'https://github.com/login/oauth/authorize?client_id=f1e73a9ac502f1b6712a';

export const ENDPOINTS = {
  PRODUCTS: '/keyboards',
  PRODUCT: (id: number | ':id') => `/keyboards/${id}`,
  REVIEWS: '/reviews',
  REVIEWS_BY_PRODUCT_ID: (id: number | ':id') => `/keyboards/${id}/reviews`,
  REVIEWS_BY_REVIEW_ID: (reviewId: number | ':id') => `/reviews/${reviewId}`,
  LOGIN: '/login',
  INVENTORY_PRODUCTS: '/members/inventoryProducts',
  ME: '/members/me',
} as const;
