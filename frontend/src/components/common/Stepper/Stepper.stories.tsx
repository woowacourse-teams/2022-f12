import Stepper from './Stepper';

export default {
  component: Stepper,
  title: 'Components/Stepper',
};

export const FirstStep = () => <Stepper step={1} />;
export const SecondStep = () => <Stepper step={2} />;
export const ThirdStep = () => <Stepper step={3} />;
