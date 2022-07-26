export const API_ERROR_MESSAGES = {
  4000: '유효하지 않은 Param',
  4001: '유효하지 않은 입력 값',
  4002: '유효하지 않은 토큰',
  4010: '토큰이 존재하지 않을 때',
  4011: '토큰이 만료되었을 때',
  4030: '타인의 정보를 수정/삭제하려고 할 때',
  4040: '존재하지 않는 API',
  5000: '서버 오류',
  5030: '서버 연결 불가',
} as const;

export const API_ERROR_CODE_EXCEPTION_MESSAGES = {
  UNKNOWN: '알 수 없는 오류 발생',
  NO_CODE: '오류가 발생했으나 오류 코드가 존재하지 않음',
} as const;
