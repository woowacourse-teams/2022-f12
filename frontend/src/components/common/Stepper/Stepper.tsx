import * as S from '@/components/common/Stepper/Stepper.style';

type Prop = {
  step: number;
};

function Stepper({ step }: Prop) {
  return (
    <S.Container>
      <S.Item>
        <S.Step>1</S.Step>
        <S.Title>{step === 1 && '연차 입력'}</S.Title>
      </S.Item>
      <S.Item>
        <S.Step>2</S.Step>
        <S.Title>{step === 2 && '직군 입력'}</S.Title>
      </S.Item>
      <S.Item>
        <S.Step>3</S.Step>
        <S.Title>{step === 3 && '정보 확인'}</S.Title>
      </S.Item>
    </S.Container>
  );
}

export default Stepper;
