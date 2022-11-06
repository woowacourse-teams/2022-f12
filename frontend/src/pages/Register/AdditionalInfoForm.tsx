import { useMemo } from 'react';

import * as S from '@/pages/Register/Register.style';

import useModal from '@/hooks/useModal';

import { VALIDATION_ERROR_MESSAGES } from '@/constants/messages';

type Entries<T> = {
  [K in keyof T]: [K, T[K]];
}[keyof T][];

type Props<T extends string> = {
  options: Record<T, string>;
  input: string;
  setInput: React.Dispatch<React.SetStateAction<T>>;
  setStep: React.Dispatch<React.SetStateAction<1 | 2 | 3>>;
};

const isValidValue = <T extends string>(
  input: string,
  options: Record<T, string>
): input is T => input in options;

function AdditionalInfoForm<T extends string>({
  options,
  input,
  setInput,
  setStep,
}: Props<T>) {
  const { showAlert } = useModal();

  const handleSelectButtonClick: React.MouseEventHandler<HTMLButtonElement> = (e) => {
    if (!(e.target instanceof HTMLButtonElement)) return;

    if (!isValidValue(e.target.value, options)) return;
    setInput(e.target.value);
  };

  const handleConfirmButtonClick: React.MouseEventHandler<
    HTMLButtonElement
  > = async () => {
    if (!input) {
      await showAlert(VALIDATION_ERROR_MESSAGES.ADDITIONAL_INFO_SELECT_REQUIRED);
      return;
    }

    setStep((prev) => {
      const nextStep = prev + 1;
      if (nextStep !== 1 && nextStep !== 2 && nextStep !== 3) return 1;

      return nextStep;
    });
  };

  const optionEntries = useMemo(() => Object.entries(options), []) as Entries<
    typeof options
  >;

  return (
    <>
      {optionEntries.map(([value, content], index: number) => (
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
