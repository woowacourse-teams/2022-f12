import { rest } from 'msw';

import { BASE_URL, ENDPOINTS } from '@/constants/api';

import {
  getInventoryProducts,
  getInventoryReview,
  getOtherMemberInventory,
  patchInventoryProducts,
} from '@/mocks/handlers/inventoryProducts';
import {
  followUser,
  getAccessToken,
  getMyInfo,
  getOtherMemberInfo,
  handleLoginRequest,
  logout,
  searchMember,
  submitAdditionalInfo,
  unfollowUser,
} from '@/mocks/handlers/members';
import {
  getKeyboard,
  getKeyboards,
  getPopularKeyboards,
  getStatistics,
} from '@/mocks/handlers/products';
import {
  deleteReviewByReviewId,
  getMyReviews,
  getReviews,
  getReviewsByMemberId,
  getReviewsByProductId,
  postReviewByProductId,
  updateReviewByReviewId,
} from '@/mocks/handlers/reviews';

export const handlers = [
  rest.get(`${BASE_URL}${ENDPOINTS.LOGIN}`, handleLoginRequest),

  rest.get(`${BASE_URL}${ENDPOINTS.ME}`, getMyInfo),
  rest.patch(`${BASE_URL}${ENDPOINTS.ME}`, submitAdditionalInfo),
  // 아래 핸들러 순서 유의미 => getOtherMemberInfo보다 아래에 위치하면 요청 matching에서 오류 발생
  rest.get(`${BASE_URL}${ENDPOINTS.INVENTORY_PRODUCTS}`, getInventoryProducts),
  rest.patch(`${BASE_URL}${ENDPOINTS.INVENTORY_PRODUCTS}`, patchInventoryProducts),

  rest.get(`${BASE_URL}${ENDPOINTS.MEMBERS}`, searchMember),
  rest.get(`${BASE_URL}${ENDPOINTS.MEMBERS}/:memberId`, getOtherMemberInfo),
  rest.get(
    `${BASE_URL}${ENDPOINTS.MEMBERS}/:memberId/inventoryProducts`,
    getOtherMemberInventory
  ),

  rest.get(`${BASE_URL}${ENDPOINTS.POPULAR_PRODUCTS}`, getPopularKeyboards),
  // 순서 주의! 이 핸들러가 아래에 위치하면 products/:id'로 연결됩니다.
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCT(':id')}`, getKeyboard),
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCTS}`, getKeyboards),
  rest.get(`${BASE_URL}${ENDPOINTS.PRODUCT(':id')}/statistics`, getStatistics),

  rest.post(
    `${BASE_URL}${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(':id')}`,
    postReviewByProductId
  ),
  rest.get(`${BASE_URL}${ENDPOINTS.REVIEWS_BY_PRODUCT_ID(':id')}`, getReviewsByProductId),
  rest.get(`${BASE_URL}${ENDPOINTS.REVIEWS_BY_MEMBER_ID(':id')}`, getReviewsByMemberId),
  rest.get(`${BASE_URL}${ENDPOINTS.MY_REVIEWS}`, getMyReviews),
  rest.get(`${BASE_URL}${ENDPOINTS.REVIEWS}`, getReviews),
  rest.get(
    `${BASE_URL}${ENDPOINTS.REVIEW_BY_INVENTORY_PRODUCT_ID(':id')}`,
    getInventoryReview
  ),
  rest.put(`${BASE_URL}${ENDPOINTS.REVIEWS_BY_REVIEW_ID(':id')}`, updateReviewByReviewId),
  rest.delete(
    `${BASE_URL}${ENDPOINTS.REVIEWS_BY_REVIEW_ID(':id')}`,
    deleteReviewByReviewId
  ),
  rest.get(`${BASE_URL}${ENDPOINTS.MY_FOLLOWING}`, searchMember),
  rest.post(`${BASE_URL}${ENDPOINTS.FOLLOWING(':id')}`, followUser),
  rest.delete(`${BASE_URL}${ENDPOINTS.FOLLOWING(':id')}`, unfollowUser),
  rest.post(`${BASE_URL}${ENDPOINTS.ISSUE_ACCESS_TOKEN}`, getAccessToken),
  rest.get(`${BASE_URL}${ENDPOINTS.LOGOUT}`, logout),
];
