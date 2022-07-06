export const BASE_URL = 'http:/localhost:8080/api/v1';

export const ENDPOINTS = {
  PRODUCTS: '/keyboards',
  PRODUCT: (id: number | ':id') => `/keyboards/${id}`,
  REVIEWS: '/reviews',
  REVIEWS_BY_PRODUCT_ID: (id: number | ':id') => `/keyboards/${id}/reviews`,
} as const;
