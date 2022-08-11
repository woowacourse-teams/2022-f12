import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import * as S from '@/pages/Register/Register.style';

import Stepper from '@/components/common/Stepper/Stepper';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import usePatch from '@/hooks/api/usePatch';
import useModal from '@/hooks/useModal';

import { ENDPOINTS } from '@/constants/api';
import ROUTES from '@/constants/routes';

const messages = {
  1: '경력을 선택해주세요',
  2: '직군을 선택해주세요',
  3: '입력한 정보를 확인해주세요',
};

const careerLevel = {
  none: '경력 없음',
  junior: '0-2년차',
  midlevel: '3-5년차',
  senior: '6년차 이상',
} as const;

const jobType = {
  frontend: '프론트엔드',
  backend: '백엔드',
  mobile: '모바일',
  etc: '기타',
} as const;

type UserInfo = {
  careerLevel: keyof typeof careerLevel;
  jobType: keyof typeof jobType;
};

function Register() {
  const [step, setStep] = useState(1);
  const [additionalInfo, setAdditionalInfo] = useState<UserInfo>({
    careerLevel: null,
    jobType: null,
  });
  const { showAlert, getConfirm } = useModal();

  const userData = useContext(UserDataContext);

  const patchAdditionalInfo = usePatch({
    url: ENDPOINTS.ME,
    headers: { Authorization: `Bearer ${userData?.token}` },
  });

  const navigate = useNavigate();

  const renderSelectButton = (step: number) => {
    switch (step) {
      case 1:
        return careerLevel;
      case 2:
        return jobType;
    }
  };

  const handleSelectButtonClick: React.MouseEventHandler<HTMLButtonElement> = (e) => {
    if (!(e.target instanceof HTMLButtonElement)) return;
    if (!(e.target.value in careerLevel) && !(e.target.value in jobType)) return;

    if (step === 1) {
      setAdditionalInfo({
        ...additionalInfo,
        careerLevel: e.target.value as keyof typeof careerLevel,
      });
    } else {
      setAdditionalInfo({
        ...additionalInfo,
        jobType: e.target.value as keyof typeof jobType,
      });
    }
  };

  const handleAdditionalInfoSubmit = async (input: UserInfo) => {
    const confirmation = await getConfirm(
      `${careerLevel[input.careerLevel]}, ${jobType[input.jobType]} 개발자이신가요?`
    );
    if (confirmation) {
      patchAdditionalInfo(input).catch((error) => {
        console.log(error);
      });
    }
  };

  const handleConfirmButtonClick = async () => {
    if (step === 1 && additionalInfo.careerLevel === null) {
      showAlert('경력을 선택해주세요.');
      return;
    }
    if (step === 2 && additionalInfo.jobType === null) {
      showAlert('직군을 선택해주세요.');
      return;
    }
    if (step === 1 || step === 2) {
      setStep(step + 1);
      return;
    }

    await handleAdditionalInfoSubmit(additionalInfo);
  };

  const handleEditButtonClick = () => {
    setStep(1);
  };

  useEffect(() => {
    if (!userData?.registerCompleted) return;

    navigate(ROUTES.HOME);
  }, [userData]);

  return (
    <S.Container>
      <S.Header>추가 정보 입력하기</S.Header>
      <Stepper step={step} />
      <S.FlexWrapper>
        <S.Title>{messages[step]}</S.Title>
        <S.FlexGapWrapper>
          {(step === 1 || step === 2) &&
            Object.entries(renderSelectButton(step)).map(
              ([value, content], index: number) => (
                <S.SelectButton
                  key={index}
                  onClick={handleSelectButtonClick}
                  value={value}
                  selected={
                    value === additionalInfo.careerLevel ||
                    value === additionalInfo.jobType
                  }
                >
                  {content}
                </S.SelectButton>
              )
            )}
          {step === 3 && (
            <>
              <S.ConfirmInfo>{careerLevel[additionalInfo.careerLevel]}</S.ConfirmInfo>
              <S.ConfirmInfo>{jobType[additionalInfo.jobType]}</S.ConfirmInfo>
            </>
          )}
        </S.FlexGapWrapper>
        <S.FlexRowWrapper>
          {step === 3 && (
            <S.EditButton onClick={handleEditButtonClick}>수정하기</S.EditButton>
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
