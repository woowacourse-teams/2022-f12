export const BASE_URL = 'http://3.34.47.160:8080/api/v1';

const githubClientId =
  process.env.NODE_ENV === 'development'
    ? '404072c5857d705db2d9'
    : 'f1e73a9ac502f1b6712a';

export const GITHUB_AUTH_URL = `https://github.com/login/oauth/authorize?client_id=${githubClientId}`;

export const ENDPOINTS = {
  PRODUCTS: '/products',
  PRODUCT: (id: number | ':id') => `/products/${id}`,
  REVIEWS: '/reviews',
  REVIEWS_BY_PRODUCT_ID: (id: number | ':id') => `/products/${id}/reviews`,
  REVIEWS_BY_REVIEW_ID: (reviewId: number | ':id') => `/reviews/${reviewId}`,
  LOGIN: '/login',
  INVENTORY_PRODUCTS: '/members/inventoryProducts',
  MEMBERS: '/members',
  ME: '/members/me',
} as const;
