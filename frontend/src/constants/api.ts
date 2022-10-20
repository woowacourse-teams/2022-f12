export const BASE_URL = __API_URL__;

const githubClientId = __GITHUB_CLIENT_ID__;

export const GITHUB_AUTH_URL = `https://github.com/login/oauth/authorize?client_id=${githubClientId}`;

export const DANAWA_SEARCH_URL = 'https://search.danawa.com/dsearch.php?k1=';
export const GOOGLE_SEARCH_URL = 'https://www.google.com/search?q=';

export const ENDPOINTS = {
  PRODUCTS: '/products',
  POPULAR_PRODUCTS: '/products/popular-list',
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
  ISSUE_ACCESS_TOKEN: '/accessToken',
  LOGOUT: '/logout',
} as const;
