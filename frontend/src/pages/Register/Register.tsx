import Stepper from '@/components/common/Stepper/Stepper';
import * as S from '@/pages/Register/Register.style';

function Register() {
  return (
    <S.Container>
      <S.Header>추가 정보 입력하기</S.Header>
      <Stepper />
      <S.FlexWrapper>
        <S.Title>경력을 선택해주세요</S.Title>
        <S.FlexGapWrapper>
          <S.SelectButton>경력 없음</S.SelectButton>
          <S.SelectButton>0-2년차</S.SelectButton>
          <S.SelectButton>3-5년차</S.SelectButton>
          <S.SelectButton>5년차 이상</S.SelectButton>
        </S.FlexGapWrapper>
        <S.FlexRowWrapper>
          <S.EditButton>수정하기</S.EditButton>
          <S.ConfirmButton>제출하기</S.ConfirmButton>
        </S.FlexRowWrapper>
      </S.FlexWrapper>
    </S.Container>
  );
}

export default Register;
