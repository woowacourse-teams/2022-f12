import Stepper from '@/components/common/Stepper/Stepper';

export default {
  component: Stepper,
  title: 'Components/Common/Stepper',
};

const stepNames = ['연차 입력', '직군 입력', '정보 확인'];

export const FirstStep = () => <Stepper names={stepNames} currentStage={1} />;
export const SecondStep = () => <Stepper names={stepNames} currentStage={2} />;
export const ThirdStep = () => <Stepper names={stepNames} currentStage={3} />;
