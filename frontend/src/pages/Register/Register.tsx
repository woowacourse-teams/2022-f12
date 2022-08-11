import ConfirmInfoForm from './ConfirmInfoForm';
import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import AdditionalInfoForm from '@/pages/Register/AdditionalInfoForm';
import * as S from '@/pages/Register/Register.style';

import Stepper from '@/components/common/Stepper/Stepper';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import usePatch from '@/hooks/api/usePatch';
import useModal from '@/hooks/useModal';

import { ENDPOINTS } from '@/constants/api';
import ROUTES from '@/constants/routes';

const titles = {
  1: '경력을 선택해주세요',
  2: '직군을 선택해주세요',
  3: '입력한 정보를 확인해주세요',
};

const careerLevels = {
  none: '경력 없음',
  junior: '0-2년차',
  midlevel: '3-5년차',
  senior: '6년차 이상',
} as const;

const jobTypes = {
  frontend: '프론트엔드',
  backend: '백엔드',
  mobile: '모바일',
  etc: '기타',
} as const;

type UserInfo = {
  careerLevel: keyof typeof careerLevels;
  jobType: keyof typeof jobTypes;
};

function Register() {
  const [step, setStep] = useState(1);
  const [careerLevel, setCareerLevel] = useState<UserInfo['careerLevel']>(null);
  const [jobType, setJobType] = useState<UserInfo['jobType']>(null);
  const navigate = useNavigate();
  const userData = useContext(UserDataContext);

  const { getConfirm } = useModal();
  const patchAdditionalInfo = usePatch({
    url: ENDPOINTS.ME,
    headers: { Authorization: `Bearer ${userData?.token}` },
  });

  const handleAdditionalInfoSubmit = async () => {
    const confirmation = await getConfirm(
      `${careerLevels[careerLevel]}, ${jobTypes[jobType]} 개발자이신가요?`
    );
    if (confirmation) {
      patchAdditionalInfo({ careerLevel, jobType }).catch((error) => {
        console.log(error);
      });
    }
  };

  const handleEditButtonClick = () => {
    setStep(1);
  };

  useEffect(() => {
    if (!userData?.registerCompleted) return;

    navigate(ROUTES.HOME);
  }, [userData]);

  const stepNames = ['연차 입력', '직군 입력', '정보 확인'];
  const stepButtons = [
    <AdditionalInfoForm
      key="0"
      options={careerLevels}
      input={careerLevel}
      setInput={setCareerLevel}
      setStep={setStep}
    />,
    <AdditionalInfoForm
      key="1"
      options={jobTypes}
      input={jobType}
      setInput={setJobType}
      setStep={setStep}
    />,
    <ConfirmInfoForm
      key="2"
      careerLevel={careerLevels[careerLevel]}
      jobType={jobTypes[jobType]}
      handleConfirm={handleAdditionalInfoSubmit}
      handleEdit={handleEditButtonClick}
    />,
  ];

  return (
    <S.Container>
      <S.Header>추가 정보 입력하기</S.Header>
      <Stepper names={stepNames} currentStage={step} />
      <S.Title>{titles[step]}</S.Title>
      <S.FlexWrapper>
        <S.FlexGapWrapper>{stepButtons[step - 1]}</S.FlexGapWrapper>
      </S.FlexWrapper>
    </S.Container>
  );
}

export default Register;
