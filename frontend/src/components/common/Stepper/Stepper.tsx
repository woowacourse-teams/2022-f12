import * as S from '@/components/common/Stepper/Stepper.style';

function Stepper() {
  return (
    <S.Container>
      <S.Item>
        <S.Step>1</S.Step>
        <S.Title>연차 입력</S.Title>
      </S.Item>
      <S.Item>
        <S.Step>2</S.Step>
        <S.Title>직군 입력</S.Title>
      </S.Item>
      <S.Item>
        <S.Step>3</S.Step>
        <S.Title>정보 확인</S.Title>
      </S.Item>
    </S.Container>
  );
}

export default Stepper;
