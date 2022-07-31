import Stepper from '@/components/common/Stepper/Stepper';
import * as S from '@/pages/Register/Register.style';
import { useContext, useState } from 'react';
import usePatch from '@/hooks/api/usePatch';
import { UserDataContext } from '@/contexts/LoginContextProvider';
import { ENDPOINTS } from '@/constants/api';
import { useNavigate } from 'react-router-dom';
import ROUTES from '@/constants/routes';
import useModal from '@/hooks/useModal';

const messages = {
  1: '경력을 선택해주세요',
  2: '직군을 선택해주세요',
  3: '입력한 정보를 확인해주세요',
};

const careers = {
  NONE: '경력 없음',
  JUNIOR: '0-2년차',
  MID_LEVEL: '3-5년차',
  SENIOR: '5년차 이상',
} as const;

const jobTypes = {
  FRONT_END: '프론트엔드',
  BACK_END: '백엔드',
  MOBILE: '모바일',
  ETC: '기타',
} as const;

type UserInfo = {
  career: keyof typeof careers;
  jobType: keyof typeof jobTypes;
};

function Register() {
  const [step, setStep] = useState(1);
  const [additionalInfo, setAdditionalInfo] = useState<UserInfo>({
    career: null,
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
        return careers;
      case 2:
        return jobTypes;
    }
  };

  const handleSelectButtonClick: React.MouseEventHandler<HTMLButtonElement> = (
    e
  ) => {
    if (!(e.target instanceof HTMLButtonElement)) return;
    if (!(e.target.value in careers) && !(e.target.value in jobTypes)) return;

    if (step === 1) {
      setAdditionalInfo({
        ...additionalInfo,
        career: e.target.value as keyof typeof careers,
      });
    } else {
      setAdditionalInfo({
        ...additionalInfo,
        jobType: e.target.value as keyof typeof jobTypes,
      });
    }
  };

  const handleAdditionalInfoSubmit = async (input: UserInfo) => {
    const confirmation = await getConfirm(
      `${careers[input.career]}, ${jobTypes[input.jobType]} 개발자이신가요?`
    );
    if (confirmation) {
      patchAdditionalInfo(input)
        .then(() => {
          navigate(ROUTES.HOME);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  const handleConfirmButtonClick = async () => {
    if (step === 1 && additionalInfo.career === null) {
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
                    value === additionalInfo.career ||
                    value === additionalInfo.jobType
                  }
                >
                  {content}
                </S.SelectButton>
              )
            )}
          {step === 3 && (
            <>
              <S.ConfirmInfo>{careers[additionalInfo.career]}</S.ConfirmInfo>
              <S.ConfirmInfo>{jobTypes[additionalInfo.jobType]}</S.ConfirmInfo>
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
