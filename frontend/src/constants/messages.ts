export const API_ERROR_MESSAGES = {
  40000: '유효하지 않은 검색 요청입니다.',
  40001: '유효하지 않은 페이지입니다.',
  40002: '소셜 로그인에 실패했습니다.',
  40003: '입력 값이 올바르지 않습니다.',
  40004: 'URL의 길이가 너무 깁니다.',
  40010: '유효하지 않은 추가정보입니다.',
  40011: '추가 정보를 입력해야 합니다.',
  40030: '올바르지 않은 평점입니다.',
  40031: '총평을 작성해주세요.',
  40032: '총평의 길이가 최대 길이를 초과했습니다.',
  40033: '이미 리뷰를 작성한 제품입니다. 같은 제품에는 하나의 리뷰만 작성할 수 있습니다.',
  40040: '등록할 수 없는 제품이 포함되어있습니다.',
  40041: '한 카테고리에는 하나의 장비만 등록할 수 있습니다.',
  40042: '대표 장비로 등록한 제품이 없습니다.',
  40043: '리뷰를 작성한 항목만 등록할 수 있습니다.',
  40050: '그렇게까지 본인의 팔로우 수를 올리고 싶었어요?',
  40100: '인증이 필요합니다.',
  40101: '인증 정보가 만료되었습니다. 다시 로그인해주세요.',
  40102: '인정 정보가 유효하지 않습니다.',
  40103: '추가정보 입력이 완료되지 않았습니다. 입력 완료 후 서비스를 이용해주세요.',
  40104: '인증이 만료되었습니다. 다시 로그인해주세요',
  40105: '리프레시 토큰이 존재하지 않습니다.',
  40300: '해당 요청을 처리할 수 있는 권한이 없습니다.',
  40410: '존재하지 않는 회원정보입니다.',
  40420: '존재하지 않는 제품입니다.',
  40430: '존재하지 않는 리뷰입니다.',
  40440: '존재하지 않는 제품입니다.',
  50000: '서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.',
  50001: '서버에서 오류가 발생했습니다. 잠시 후 다시 시도해주세요.',
} as const;

export const API_ERROR_CODE_EXCEPTION_MESSAGES = {
  UNKNOWN: '알 수 없는 오류 발생',
  NO_CODE: '오류가 발생했으나 오류 코드가 존재하지 않음',
} as const;

export const VALIDATION_ERROR_MESSAGES = {
  LOGIN_REQUIRED: '로그인이 필요합니다.',
  FORM_INCOMPLETE: '모든 항목을 입력해주세요.',
  ADDITIONAL_INFO_REQUIRED: '추가 정보 입력 후 이용해주세요.',
  ADDITIONAL_INFO_SELECT_REQUIRED: '선택 후 이동 가능합니다.',
} as const;

export const FAILURE_MESSAGES = {
  LOGIN_CANCELED: '로그인을 취소했거나 오류가 발생했습니다.',
  LOGOUT: '로그아웃에 실패했습니다. 다시 시도해주세요.',
  NO_REFRESH_TOKEN: '로그인 하지 않은 사용자',
} as const;

export const SUCCESS_MESSAGES = {
  LOGOUT: '로그아웃이 완료되었습니다.',
  REVIEW_CREATE: '리뷰가 작성되었습니다.',
  REVIEW_EDIT: '리뷰가 수정되었습니다.',
  REVIEW_DELETE: '리뷰가 삭제되었습니다.',
} as const;

export const CONFIRM_MESSAGES = {
  LOGOUT: '로그아웃 하시겠습니까?',
  REVIEW_DELETE: '리뷰를 삭제하시겠습니까?',
} as const;
