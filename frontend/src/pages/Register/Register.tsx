import Stepper from '@/components/common/Stepper/Stepper';
import * as S from '@/pages/Register/Register.style';
import { useState } from 'react';

type Careers = ['경력 없음', '0-2년차', '3-5년차', '5년차 이상'];
type JobTypes = ['프론트엔드', '백엔드', '모바일', '기타'];
type UserInfo = {
  career: string;
  jobType: string;
};

const messages = {
  1: '경력을 선택해주세요',
  2: '직군을 선택해주세요',
  3: '입력한 정보를 확인해주세요',
};
const careers: Careers = ['경력 없음', '0-2년차', '3-5년차', '5년차 이상'];
const jobTypes: JobTypes = ['프론트엔드', '백엔드', '모바일', '기타'];

function Register() {
  const [step, setStep] = useState(1);
  const [additionalInfo, setAdditionalInfo] = useState<UserInfo>({
    career: null,
    jobType: null,
  });

  const renderSelectButton = (step: number) => {
    switch (step) {
      case 1:
        return careers;
      case 2:
        return jobTypes;
    }
  };

  const handleSelectButtonClick: React.MouseEventHandler<HTMLButtonElement> = (
    e
  ) => {
    if (!(e.target instanceof HTMLButtonElement)) return;

    if (step === 1) {
      setAdditionalInfo({
        ...additionalInfo,
        career: e.target.value,
      });
    } else {
      setAdditionalInfo({
        ...additionalInfo,
        jobType: e.target.value,
      });
    }
  };

  const handleConfirmButtonClick = () => {
    if (step === 1 && additionalInfo.career === null) {
      alert('경력을 선택해주세요.');
      return;
    }
    if (step === 2 && additionalInfo.jobType === null) {
      alert('직군을 선택해주세요.');
      return;
    }
    if (step === 1 || step === 2) {
      setStep(step + 1);
      return;
    }
    confirm(
      `${additionalInfo.career}, ${additionalInfo.jobType} 개발자이신가요?`
    );
  };

  const handleEditButtonClick = () => {
    setStep(1);
  };

  return (
    <S.Container>
      <S.Header>추가 정보 입력하기</S.Header>
      <Stepper step={step} />
      <S.FlexWrapper>
        <S.Title>{messages[step]}</S.Title>
        <S.FlexGapWrapper>
          {(step === 1 || step === 2) &&
            renderSelectButton(step).map((content: string, index: number) => (
              <S.SelectButton
                key={index}
                onClick={handleSelectButtonClick}
                value={content}
                selected={
                  content === additionalInfo.career ||
                  content === additionalInfo.jobType
                }
              >
                {content}
              </S.SelectButton>
            ))}
          {step === 3 && (
            <>
              <S.ConfirmInfo>{additionalInfo.career}</S.ConfirmInfo>
              <S.ConfirmInfo>{additionalInfo.jobType}</S.ConfirmInfo>
            </>
          )}
        </S.FlexGapWrapper>
        <S.FlexRowWrapper>
          {step === 3 && (
            <S.EditButton onClick={handleEditButtonClick}>
              수정하기
            </S.EditButton>
          )}
          <S.ConfirmButton onClick={handleConfirmButtonClick}>
            {step === 1 || step === 2 ? '선택 완료' : '제출하기'}
          </S.ConfirmButton>
        </S.FlexRowWrapper>
      </S.FlexWrapper>
    </S.Container>
  );
}

export default Register;
