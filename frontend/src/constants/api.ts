export const BASE_URL =
  'http://ec2-52-78-192-78.ap-northeast-2.compute.amazonaws.com:8080/api/v1';

export const GITHUB_AUTH_URL =
  'https://github.com/login/oauth/authorize?client_id=f1e73a9ac502f1b6712a';

export const ENDPOINTS = {
  PRODUCTS: '/keyboards',
  PRODUCT: (id: number | ':id') => `/keyboards/${id}`,
  REVIEWS: '/reviews',
  REVIEWS_BY_PRODUCT_ID: (id: number | ':id') => `/keyboards/${id}/reviews`,
  REVIEWS_BY_REVIEW_ID: (id: number | ':id') =>
    `/keyboards/${id}/reviews/${id}`,
  LOGIN: '/login',
} as const;
