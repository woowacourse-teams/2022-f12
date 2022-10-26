import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import AdditionalInfoForm from '@/pages/Register/AdditionalInfoForm';
import ConfirmInfoForm from '@/pages/Register/ConfirmInfoForm';
import * as S from '@/pages/Register/Register.style';

import Stepper from '@/components/common/Stepper/Stepper';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import usePatch from '@/hooks/api/usePatch';
import useModal from '@/hooks/useModal';

import { ENDPOINTS } from '@/constants/api';
import { CAREER_LEVELS, JOB_TYPES } from '@/constants/profile';
import ROUTES from '@/constants/routes';

const titles = {
  1: '경력을 선택해주세요',
  2: '직군을 선택해주세요',
  3: '입력한 정보를 확인해주세요',
} as const;

function Register() {
  const [step, setStep] = useState<keyof typeof titles>(1);
  const [careerLevel, setCareerLevel] = useState<CareerLevel>(null);
  const [jobType, setJobType] = useState<JobType>(null);
  const navigate = useNavigate();
  const userData = useContext(UserDataContext);

  const { getConfirm } = useModal();
  const patchAdditionalInfo = usePatch({
    url: ENDPOINTS.ME,
    headers: { Authorization: `Bearer ${userData?.token}` },
  });

  const handleAdditionalInfoSubmit = async () => {
    const confirmation = await getConfirm(
      `${CAREER_LEVELS[careerLevel]}, ${JOB_TYPES[jobType]} 개발자이신가요?`
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
      options={CAREER_LEVELS}
      input={careerLevel}
      setInput={setCareerLevel}
      setStep={setStep}
    />,
    <AdditionalInfoForm
      key="1"
      options={JOB_TYPES}
      input={jobType}
      setInput={setJobType}
      setStep={setStep}
    />,
    <ConfirmInfoForm
      key="2"
      careerLevel={CAREER_LEVELS[careerLevel]}
      jobType={JOB_TYPES[jobType]}
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
