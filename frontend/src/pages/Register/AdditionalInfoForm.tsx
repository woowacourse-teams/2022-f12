import * as S from '@/pages/Register/Register.style';

import useModal from '@/hooks/useModal';

import { VALIDATION_ERROR_MESSAGES } from '@/constants/messages';

type Props = {
  options: Record<string, string>;
  input: string;
  setInput: React.Dispatch<React.SetStateAction<string>>;
  setStep: React.Dispatch<React.SetStateAction<number>>;
};

function AdditionalInfoForm({ options, input, setInput, setStep }: Props) {
  const { showAlert } = useModal();

  const handleSelectButtonClick: React.MouseEventHandler<HTMLButtonElement> = (e) => {
    if (!(e.target instanceof HTMLButtonElement)) return;

    setInput(e.target.value);
  };

  const handleConfirmButtonClick: React.MouseEventHandler<
    HTMLButtonElement
  > = async () => {
    if (!input) {
      await showAlert(VALIDATION_ERROR_MESSAGES.ADDITIONAL_INFO_SELECT_REQUIRED);
      return;
    }

    setStep((prev) => prev + 1);
  };

  return (
    <>
      {Object.entries(options).map(([value, content], index: number) => (
        <S.SelectButton
          key={index}
          onClick={handleSelectButtonClick}
          value={value}
          selected={value === input}
        >
          {content}
        </S.SelectButton>
      ))}
      <S.FlexRowWrapper>
        <S.ConfirmButton onClick={handleConfirmButtonClick}>선택 완료</S.ConfirmButton>
      </S.FlexRowWrapper>
    </>
  );
}

export default AdditionalInfoForm;
