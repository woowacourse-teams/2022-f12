import * as S from '@/components/common/Stepper/Stepper.style';

type Prop = {
  names: string[];
  currentStage: number;
};

function Stepper({ names, currentStage }: Prop) {
  return (
    <S.Container>
      {names.map((name, index) => {
        const stage = index + 1;
        const isComplete = stage < currentStage;
        const isCurrentStage = stage === currentStage;

        return (
          <S.Item key={name} isComplete={isComplete}>
            <S.Step isComplete={isComplete} isCurrentStage={isCurrentStage}>
              {stage}
            </S.Step>
            <S.Title>{name}</S.Title>
          </S.Item>
        );
      })}
    </S.Container>
  );
}

export default Stepper;
